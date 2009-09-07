package org.gitools.commands;

import java.io.File;

import org.gitools.datafilters.ValueParser;
import org.gitools.model.ModuleMap;
import org.gitools.model.ToolConfig;
import org.gitools.model.analysis.Analysis;
import org.gitools.model.matrix.DoubleMatrix;
import org.gitools.persistence.PersistenceException;
import org.gitools.persistence.SimpleMapPersistence;
import org.gitools.persistence.TextDoubleMatrixPersistence;
import org.gitools.resources.FileResource;
import org.gitools.resources.IResource;
import org.gitools.stats.test.factory.TestFactory;
import org.gitools.tool.processors.OncozProcessor;

import edu.upf.bg.progressmonitor.IProgressMonitor;

public class OncozCommand extends AnalysisCommand {
	
	public OncozCommand(String analysisName, String testName,
			int samplingNumSamples, String dataFile, ValueParser valueParser, 
			String groupsFile, int minModuleSize, int maxModuleSize, 
			String workdir, String outputFormat, boolean resultsByCond) {
		
		super(analysisName, testName, samplingNumSamples, dataFile, valueParser, 
				groupsFile, minModuleSize, maxModuleSize, true,
				workdir, outputFormat, resultsByCond);
	}

	@Override
	public void run(IProgressMonitor monitor) 
			throws PersistenceException, InterruptedException {
		
		// Prepare test factory
		
		TestFactory testFactory = 
			createTestFactory(ToolConfig.ONCOZET, testName);
		
		// Load data and modules
		
		monitor.begin("Loading ...", 1);
		monitor.info("Data: " + dataFile);
		monitor.info("Modules: " + modulesFile);
		
		DoubleMatrix doubleMatrix = new DoubleMatrix();
		//FIXME: id
		ModuleMap moduleMap = new ModuleMap();
		loadDataAndModules(
				doubleMatrix, moduleMap, 
				dataFile, valueParser, 
				modulesFile, minModuleSize, maxModuleSize,
				includeNonMappedItems,
				monitor.subtask());
		
		monitor.end();
		
		// Create and process analysis
		
		Analysis analysis = new Analysis();
		analysis.setTitle(title);
		analysis.setToolConfig(testFactory.getTestConfig());
		analysis.setDataTable(doubleMatrix);
		analysis.setModuleSet(moduleMap);
		
		OncozProcessor processor = 
			new OncozProcessor(analysis);
		
		processor.run(monitor);
		
		// Save analysis
		
		save(analysis, monitor);
	}

	private void loadDataAndModules(
			DoubleMatrix doubleMatrix, ModuleMap moduleMap,
			String dataFileName, ValueParser valueParser, 
			String modulesFileName,	int minModuleSize, int maxModuleSize,
			boolean includeNonMappedItems,
			IProgressMonitor monitor)
			throws PersistenceException {
		
		// Load metadata
		
		IResource resource = new FileResource(dataFileName);
		TextDoubleMatrixPersistence dmPersistence = new TextDoubleMatrixPersistence();
		dmPersistence.readMetadata(resource, doubleMatrix, valueParser, monitor);
		
		// Load modules
		
		if (modulesFileName != null) {
			File file = new File(modulesFileName);
			moduleMap.setTitle(file.getName());
			
			SimpleMapPersistence simpleMapPersistence = new SimpleMapPersistence(file);
			simpleMapPersistence.load(
				moduleMap,
				minModuleSize,
				maxModuleSize,
				doubleMatrix.getColNames(),
				includeNonMappedItems,
				monitor);
		}
		else {
			moduleMap.setItemNames(doubleMatrix.getColNames());
			moduleMap.setModuleNames(new String[] {"all"});
			int num = doubleMatrix.getColNames().length;
			int[][] indices = new int[1][num];
			for (int i = 0; i < num; i++)
				indices[0][i] = i;
			moduleMap.setItemIndices(indices);
		}
		
		// Load data
		
		dmPersistence.readData(
				resource,
				doubleMatrix,
				valueParser,
				moduleMap.getItemsOrder(),
				null,
				monitor);		
	}
}
