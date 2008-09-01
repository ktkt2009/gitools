package es.imim.bg.ztools.resources;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;
import java.util.zip.DataFormatException;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVStrategy;

import es.imim.bg.progressmonitor.ProgressMonitor;

public class ModulesFile extends ResourceFile {

	private static final CSVStrategy csvStrategy = 
		new CSVStrategy('\t', '"', '#', true, true, true);
	
	public static final int defaultMinModuleSize = 20;
	public static final int defaultMaxModuleSize = Integer.MAX_VALUE;
	
	// Output
	protected String[] moduleNames;
	protected int[][] moduleItemIndices;
	
	protected int[] itemsOrder;
	
	public ModulesFile(String fileName) {
		super(fileName);
	}
	
	public void load(
			int minModuleSize, 
			int maxModuleSize,
			String[] itemNames,
			ProgressMonitor monitor) throws FileNotFoundException, IOException, DataFormatException {
	
		monitor.begin("Reading modules ...", 1);
		monitor.info("Module size filter: minimum = " + minModuleSize + ", maximum = " + maxModuleSize);

		// create a map between the item names and its row position
		
		Map<String, Integer> itemNameToRowMapping = new TreeMap<String, Integer>();
		for (int i = 0; i < itemNames.length; i++)
			itemNameToRowMapping.put(itemNames[i], i);
		
		// read group item indices
		
		Reader reader = openReader();
		
		CSVParser parser = new CSVParser(reader, csvStrategy);
		
		final Map<String, SortedSet<Integer>> moduleItemsMap = 
			new HashMap<String, SortedSet<Integer>>();
		
		readModuleMappings(parser, itemNameToRowMapping, moduleItemsMap);
		
		parser = null;
		
		// copy module names and module item indices to arrays
		
		final Set<Entry<String, SortedSet<Integer>>> entries = moduleItemsMap.entrySet();
		final String[] tmpModuleNames = new String[entries.size()];
		final int[][] tmpModuleItemIndices = new int[entries.size()][];
		int index = 0;
		
		for (Entry<String, SortedSet<Integer>> entry : entries) {
			SortedSet<Integer> indices = entry.getValue();
			tmpModuleNames[index] = entry.getKey();
			int[] ia = tmpModuleItemIndices[index] = new int[indices.size()];
			int i = 0;
			for (Integer idx : indices )
				ia[i++] = idx;
			index++;
		}
		
		// sort groups by number of items
		
		int numModules = entries.size();
		final Integer[] moduleOrder = new Integer[numModules];
		for (int i = 0; i < moduleOrder.length; i++)
			moduleOrder[i] = i;
		
		Arrays.sort(moduleOrder, new Comparator<Integer>() {
			public int compare(Integer o1, Integer o2) {
				int l1 = tmpModuleItemIndices[o1].length;
				int l2 = tmpModuleItemIndices[o2].length;
				return l2 - l1;
			}
		});
		
		// filter by number of items
		
		int start = 0;
		int end = numModules - 1;
		while (start < numModules 
				&& tmpModuleItemIndices[moduleOrder[start]].length > maxModuleSize) {
			int order = moduleOrder[start];
			tmpModuleNames[order] = null;
			tmpModuleItemIndices[order] = null;
			start++;
		}
		while (end >= start 
				&& tmpModuleItemIndices[moduleOrder[end]].length < minModuleSize) {
			int order = moduleOrder[end];
			tmpModuleNames[order] = null;
			tmpModuleItemIndices[order] = null;
			end--;
		}
		end++;
		
		final int fileNumModules = numModules;
		
		numModules = end - start;
		moduleNames = new String[numModules];
		moduleItemIndices = new int[numModules][];
		
		// Prepare map between original item index 
		// and data row where will be stored,
		// sorted according to group size.
		// Get group names and update group item indices.
		
		itemsOrder = new int[itemNames.length];
		Arrays.fill(itemsOrder, -1);
		
		int numItems = 0;
		for (int i = start; i < end; i++) {
			int order = moduleOrder[i];
			
			moduleNames[i - start] = tmpModuleNames[order];
			moduleItemIndices[i - start] = tmpModuleItemIndices[order];
			
			final int[] indices = tmpModuleItemIndices[order];
			
			for (int j = 0; j < indices.length; j++) {
				int idx = indices[j];
				if (itemsOrder[idx] < 0)
					itemsOrder[idx] = numItems++;
				indices[j] = itemsOrder[idx];
			}
		}
		
		// Put not used items at the end
		
		for (int i = 0; i < itemsOrder.length; i++)
			if (itemsOrder[i] < 0)
				itemsOrder[i] = numItems++;
		
		monitor.info(numModules + " modules loaded");
		monitor.info((fileNumModules - numModules) + " modules discarded");
		
		monitor.end();
	}
	
	protected void readModuleMappings(
			CSVParser parser,
			Map<String, Integer> itemNameToRowMapping, 
			Map<String, SortedSet<Integer>> moduleItemsMap) throws IOException, DataFormatException {
	
		String[] fields;
		
		while ((fields = parser.getLine()) != null) {
			if (fields.length < 2)
				throw new DataFormatException(
						"At least 2 columns expected at " 
						+ parser.getLineNumber() 
						+ "(item name and group name).");
			
			String itemName = fields[0];
			String groupName = fields[1];
			
			Integer itemIndex = itemNameToRowMapping.get(itemName);
			if (itemIndex != null) {
				SortedSet<Integer> itemIndices = moduleItemsMap.get(groupName);
				if (itemIndices == null) {
					itemIndices = new TreeSet<Integer>();
					moduleItemsMap.put(groupName, itemIndices);
				}
				itemIndices.add(itemIndex);
			}
		}
	}
	
	public String[] getModuleNames() {
		return moduleNames;
	}
	
	public int[][] getModuleItemIndices() {
		return moduleItemIndices;
	}
	
	public int[] getItemsOrder() {
		return itemsOrder;
	}
}
