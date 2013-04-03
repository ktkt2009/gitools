/*
 * #%L
 * gitools-core
 * %%
 * Copyright (C) 2013 Universitat Pompeu Fabra - Biomedical Genomics group
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package org.gitools.persistence.formats.matrix;

import cern.colt.matrix.ObjectFactory1D;
import cern.colt.matrix.ObjectMatrix1D;
import org.gitools.analysis.combination.CombinationResult;
import org.gitools.analysis.correlation.CorrelationResult;
import org.gitools.analysis.groupcomparison.GroupComparisonResult;
import org.gitools.analysis.overlapping.OverlappingResult;
import org.gitools.datafilters.DoubleTranslator;
import org.gitools.datafilters.ValueTranslator;
import org.gitools.matrix.model.ObjectMatrix;
import org.gitools.matrix.model.element.*;
import org.gitools.persistence.IResourceLocator;
import org.gitools.persistence.PersistenceException;
import org.gitools.persistence._DEPRECATED.FileSuffixes;
import org.gitools.persistence._DEPRECATED.MimeTypes;
import org.gitools.stats.test.results.BinomialResult;
import org.gitools.stats.test.results.FisherResult;
import org.gitools.stats.test.results.ZScoreResult;
import org.gitools.utils.csv.CSVReader;
import org.gitools.utils.csv.RawCsvWriter;
import org.gitools.utils.fileutils.IOUtils;
import org.gitools.utils.progressmonitor.IProgressMonitor;

import java.io.*;
import java.util.*;
import java.util.zip.DataFormatException;

public class MultiValueMatrixFormat extends AbstractMatrixFormat<ObjectMatrix>
{

    public static final String VALUE_INDICES = "valueIndices";

    private static final String META_TAG = "#?";
    private static final String TYPE_TAG = "type";

    private static final Map<String, Class<?>> typeToClass = new HashMap<String, Class<?>>();
    private static final Map<Class<?>, String> classToType = new HashMap<Class<?>, String>();

    static
    {
        typeToClass.put("zscore-test", ZScoreResult.class);
        typeToClass.put("binomial-test", BinomialResult.class);
        typeToClass.put("fisher-test", FisherResult.class);
        typeToClass.put("correlation", CorrelationResult.class);
        typeToClass.put("combination", CombinationResult.class);
        typeToClass.put("overlapping", OverlappingResult.class);
        typeToClass.put("group-comparison", GroupComparisonResult.class);

        for (Map.Entry<String, Class<?>> e : typeToClass.entrySet())
            classToType.put(e.getValue(), e.getKey());
    }

    public MultiValueMatrixFormat()
    {
        super(FileSuffixes.OBJECT_MATRIX, MimeTypes.OBJECT_MATRIX, ObjectMatrix.class);
    }

    /* This information will be used to infer the element class
     * to use when loading an old tabulated file
     * using only its headers */
    private static final Map<String, Class<?>> elementClasses = new HashMap<String, Class<?>>();

    static
    {
        Class<?>[] classes = new Class<?>[]{
                ZScoreResult.class,
                BinomialResult.class,
                FisherResult.class,
                CorrelationResult.class,
                CombinationResult.class,
                OverlappingResult.class,
                GroupComparisonResult.class
        };

        for (Class<?> elementClass : classes)
        {
            IElementAdapter adapter =
                    new BeanElementAdapter(elementClass);

            elementClasses.put(
                    getElementClassId(adapter.getProperties()),
                    elementClass);
        }
    }

    private static String getElementClassId(List<IElementAttribute> properties)
    {
        String[] ids = new String[properties.size()];
        for (int i = 0; i < properties.size(); i++)
            ids[i] = properties.get(i).getId();

        return getElementClassId(ids);
    }

    private static String getElementClassId(String[] ids)
    {
        String[] ids2 = new String[ids.length];
        System.arraycopy(ids, 0, ids2, 0, ids.length);
        Arrays.sort(ids2);

        StringBuilder sb = new StringBuilder();
        for (String id : ids2)
            sb.append(':').append(id);
        return sb.toString();
    }

    private Map<String, String> meta;

    @Override
    protected void configureResource(IResourceLocator resourceLocator, Properties properties, IProgressMonitor progressMonitor) throws PersistenceException
    {
        super.configureResource(resourceLocator, properties, progressMonitor);

        meta = new HashMap<String, String>();
        try
        {
            InputStream in = resourceLocator.openInputStream();
            BufferedReader r = new BufferedReader(new InputStreamReader(in));
            boolean done = false;
            String cl;
            while (!done && (cl = r.readLine()) != null)
            {
                if (cl.startsWith(META_TAG))
                {
                    cl = cl.substring(META_TAG.length()).trim();
                    int pos = cl.indexOf(':');
                    if (pos > 0 && pos < cl.length() - 1)
                    {
                        String key = cl.substring(0, pos).trim();
                        String value = cl.substring(pos + 1).trim();
                        meta.put(key, value);
                    }
                }
                else
                {
                    done = true;
                }
            }
            r.close();
            in.close();
        } catch (Exception ex)
        {
            throw new PersistenceException(ex);
        }

    }

    @Override
    protected ObjectMatrix readResource(IResourceLocator resourceLocator, IProgressMonitor monitor) throws PersistenceException
    {

        ObjectMatrix resultsMatrix = new ObjectMatrix();

        monitor.begin("Loading results ...", 1);

        try
        {
            InputStream in = resourceLocator.openInputStream();
            CSVReader parser = new CSVReader(new InputStreamReader(in));

            String[] populationLabels = getPopulationLabels();
            final Set<String> popLabelsSet = populationLabels != null ?
                    new HashSet<String>(Arrays.asList(populationLabels)) : null;

            String[] line = parser.readNext();
            if (line.length < 3)
            {
                throw new DataFormatException("At least 3 columns expected.");
            }


            int[] indices;
            int numAttributes;
            Properties properties = getProperties();
            if (properties.containsKey(VALUE_INDICES))
            {
                indices = (int[]) properties.get(VALUE_INDICES);
                numAttributes = indices.length;
            }
            else
            {
                numAttributes = line.length - 2;
                indices = new int[numAttributes];
                for (int i = 0; i < indices.length; i++)
                {
                    indices[i] = i;
                }
            }

            ValueTranslator[] valueTranslators = new ValueTranslator[numAttributes];
            if (properties.containsKey(VALUE_TRANSLATORS))
            {
                for (int i = 0; i < numAttributes; i++)
                {
                    valueTranslators[i] = getValueTranslator(i);
                }
            }

            // read header
            // first the wanted, and then all column headers
            String[] attributeNames = new String[numAttributes];
            for (int i = 0; i < numAttributes; i++)
            {
                attributeNames[i] = line[indices[i] + 2];
            }
            String[] allAttributeNames = new String[line.length - 2];
            System.arraycopy(line, 2, allAttributeNames, 0, line.length - 2);

            boolean readAllValues = allAttributeNames.length == attributeNames.length;

            // infer element class and create corresponding adapter and factory
            Class<?> elementClass = null;

            // only try to force special type if all the values are going to be loaded
            if (readAllValues)
            {
                if (meta.containsKey(TYPE_TAG))
                {
                    elementClass = typeToClass.get(meta.get(TYPE_TAG));
                }

                if (elementClass == null)
                {
                    // infer element class and create corresponding adapter and factory
                    elementClass = elementClasses.get(
                            getElementClassId(allAttributeNames));
                }
            }

            IElementAdapter origElementAdapter = null;
            IElementAdapter destElementAdapter = null;
            IElementFactory elementFactory = null;
            if (elementClass == null)
            {
                origElementAdapter = new ArrayElementAdapter(allAttributeNames);
                destElementAdapter = new ArrayElementAdapter(attributeNames);
                elementFactory = new ArrayElementFactory(attributeNames.length);
            }
            else
            {
                origElementAdapter = new BeanElementAdapter(elementClass);
                destElementAdapter = new BeanElementAdapter(elementClass);
                elementFactory = new BeanElementFactory(elementClass);
            }

            Map<String, Integer> attrIdmap = new HashMap<String, Integer>();
            for (int i = 0; i < allAttributeNames.length; i++)
            {
                attrIdmap.put(allAttributeNames[i], i);
            }

            // read body
            Map<String, Integer> columnMap = new HashMap<String, Integer>();
            Map<String, Integer> rowMap = new HashMap<String, Integer>();
            List<Object[]> list = new ArrayList<Object[]>();

            while ((line = parser.readNext()) != null)
            {
                final String columnName = line[0];
                final String rowName = line[1];

                if (popLabelsSet != null && !(popLabelsSet.contains(rowName)))
                {
                    continue;
                }


                Integer columnIndex = columnMap.get(columnName);
                if (columnIndex == null)
                {
                    columnIndex = columnMap.size();
                    columnMap.put(columnName, columnIndex);
                }

                Integer rowIndex = rowMap.get(rowName);
                if (rowIndex == null)
                {
                    rowIndex = rowMap.size();
                    rowMap.put(rowName, rowIndex);
                }

                Object element = elementFactory.create();

                for (int i = 0; i < indices.length; i++)
                {
                    final String property = allAttributeNames[indices[i]];
                    final Integer sourceIdx = attrIdmap.get(property);
                    final Integer destIdx = destElementAdapter.getPropertyIndex(property);

                    Object value;
                    if (sourceIdx != null)
                    {
                        if (valueTranslators[i] != null)
                        {
                            value = valueTranslators[i].stringToValue(line[sourceIdx + 2]);
                        }
                        else
                        {
                            value = parsePropertyValue(
                                    origElementAdapter.getProperty(origElementAdapter.getPropertyIndex(property)), line[sourceIdx + 2]);
                        }
                        destElementAdapter.setValue(element, destIdx, value);
                    }
                }

                list.add(new Object[]{
                        new int[]{columnIndex, rowIndex}, element});
            }

            // add missing population labels to matrix
            if (popLabelsSet != null)
            {
                for (String rowName : popLabelsSet)
                {
                    if (!rowMap.containsKey(rowName))
                    {
                        int rowIndex = rowMap.size();
                        rowMap.put(rowName, rowIndex);
                        for (Integer columnIndex : columnMap.values())
                        {
                            Object element = elementFactory.create();
                            for (int i = 0; i < indices.length; i++)
                            {
                                Double backgroundValue = getBackgroundValue();
                                origElementAdapter.setValue(element, i, backgroundValue);
                            }
                            list.add(new Object[]{
                                    new int[]{columnIndex, rowIndex}, element
                            });
                        }
                    }
                }
            }

            int numColumns = columnMap.size();
            int numRows = rowMap.size();

            ObjectMatrix1D columns = ObjectFactory1D.dense.make(numColumns);
            for (Map.Entry<String, Integer> entry : columnMap.entrySet())
                columns.setQuick(entry.getValue(), entry.getKey());

            ObjectMatrix1D rows = ObjectFactory1D.dense.make(numRows);
            for (Map.Entry<String, Integer> entry : rowMap.entrySet())
                rows.setQuick(entry.getValue(), entry.getKey());

            resultsMatrix.setColumns(columns);
            resultsMatrix.setRows(rows);
            resultsMatrix.makeCells();

            resultsMatrix.setCellAdapter(destElementAdapter);

            for (Object[] result : list)
            {
                int[] coord = (int[]) result[0];
                final int columnIndex = coord[0];
                final int rowIndex = coord[1];

                Object element = result[1];
                resultsMatrix.setCell(rowIndex, columnIndex, element);
            }

            in.close();
        } catch (Exception e)
        {
            throw new PersistenceException(e);
        }

        monitor.end();

        return resultsMatrix;
    }

    private Object parsePropertyValue(IElementAttribute property, String string)
    {

        final Class<?> propertyClass = property.getValueClass();

        Object value = null;
        try
        {
            if (propertyClass.equals(double.class)
                    || propertyClass.equals(Double.class))
            {
                value = Double.parseDouble(string);
            }
            else if (propertyClass.equals(float.class)
                    || propertyClass.equals(Float.class))
            {
                value = Double.parseDouble(string);
            }
            else if (propertyClass.equals(int.class)
                    || propertyClass.equals(Integer.class))
            {
                value = Integer.parseInt(string);
            }
            else if (propertyClass.equals(long.class)
                    || propertyClass.equals(Long.class))
            {
                value = Long.parseLong(string);
            }
            else if (propertyClass.isEnum())
            {
                Object[] cts = propertyClass.getEnumConstants();
                for (Object o : cts)
                    if (o.toString().equals(string))
                    {
                        value = o;
                    }
            }
            else
            {
                value = string;
            }
        } catch (Exception e)
        {
            if (propertyClass.equals(double.class)
                    || propertyClass.equals(Double.class))
            {
                value = Double.NaN;
            }
            else if (propertyClass.equals(float.class)
                    || propertyClass.equals(Float.class))
            {
                value = Float.NaN;
            }
            else if (propertyClass.equals(int.class)
                    || propertyClass.equals(Integer.class))
            {
                value = new Integer(0);
            }
            else if (propertyClass.equals(long.class)
                    || propertyClass.equals(Long.class))
            {
                value = new Long(0);
            }
            else if (propertyClass.isEnum())
            {
                value = string;
            }
        }
        return value;
    }

    @Override
    protected void writeResource(IResourceLocator resourceLocator, ObjectMatrix results, IProgressMonitor monitor) throws PersistenceException
    {

        boolean orderByColumn = true;

        monitor.begin("Saving results...", results.getRowCount() * results.getColumnCount());

        try
        {
            OutputStream out = resourceLocator.openOutputStream();
            Writer writer = new OutputStreamWriter(out);
            IElementAdapter cellAdapter = results.getCellAdapter();

            Class<?> elementClass = cellAdapter.getElementClass();
            String typeName = classToType.get(elementClass);
            if (typeName != null)
            {
                writer.write(META_TAG + " " + TYPE_TAG + ": " + typeName + "\n");
            }

            writeCells(writer, results, orderByColumn, monitor);
            writer.close();
            out.close();
        } catch (Exception e)
        {
            throw new PersistenceException(e);
        } finally
        {
            monitor.end();
        }
    }

    private void writeCells(Writer writer, ObjectMatrix resultsMatrix, boolean orderByColumn, IProgressMonitor progressMonitor)
    {

        RawCsvWriter out = new RawCsvWriter(writer, '\t', '"');
        IElementAdapter cellAdapter = resultsMatrix.getCellAdapter();

        out.writeQuotedValue("column");
        out.writeSeparator();
        out.writeQuotedValue("row");

        for (IElementAttribute prop : cellAdapter.getProperties())
        {
            out.writeSeparator();
            out.writeQuotedValue(prop.getId());
        }

        out.writeNewLine();

        int numColumns = resultsMatrix.getColumnCount();
        int numRows = resultsMatrix.getRowCount();

        if (orderByColumn)
        {
            for (int colIndex = 0; colIndex < numColumns; colIndex++)
                for (int rowIndex = 0; rowIndex < numRows; rowIndex++)
                    writeLine(out, resultsMatrix, colIndex, rowIndex, progressMonitor);
        }
        else
        {
            for (int rowIndex = 0; rowIndex < numRows; rowIndex++)
                for (int colIndex = 0; colIndex < numColumns; colIndex++)
                    writeLine(out, resultsMatrix, colIndex, rowIndex, progressMonitor);
        }
    }

    private void writeLine(RawCsvWriter out, ObjectMatrix resultsMatrix, int colIndex, int rowIndex, IProgressMonitor progressMonitor)
    {

        Object element = resultsMatrix.getCell(rowIndex, colIndex);
        if (element == null)
        {
            return;
        }

        final String colName = resultsMatrix.getColumnLabel(colIndex);
        final String rowName = resultsMatrix.getRowLabel(rowIndex);

        out.writeQuotedValue(colName);
        out.writeSeparator();
        out.writeQuotedValue(rowName);

        IElementAdapter cellsAdapter = resultsMatrix.getCellAdapter();

        int numProperties = cellsAdapter.getPropertyCount();

        DoubleTranslator doubleTrans = new DoubleTranslator();

        for (int propIndex = 0; propIndex < numProperties; propIndex++)
        {
            out.writeSeparator();

            Object value = cellsAdapter.getValue(element, propIndex);
            if (value instanceof Double)
            {
                Double v = (Double) value;
                out.write(doubleTrans.valueToString(v));
            }
            else if (value instanceof Integer)
            {
                out.writeValue(value.toString());
            }
            else
            {
                out.writeQuotedValue(value.toString());
            }
        }

        out.writeNewLine();

        progressMonitor.worked(1);
    }

    @Deprecated
    public static List<IElementAttribute> readMetaAttributes(File file, IProgressMonitor monitor) throws PersistenceException
    {
        IElementAdapter elementAdapter = null;

        Map<String, String> meta = readFormatAttributes(file, monitor);

        try
        {
            Reader reader = IOUtils.openReader(file);

            CSVReader parser = new CSVReader(reader);

            String[] line = parser.readNext();

            // read header
            if (line.length < 3)
            {
                throw new DataFormatException("At least 3 columns expected.");
            }

            int numAttributes = line.length - 2;
            String[] attributeNames = new String[numAttributes];
            System.arraycopy(line, 2, attributeNames, 0, line.length - 2);

            Class<?> elementClass = null;

            if (meta.containsKey(TYPE_TAG))
            {
                elementClass = typeToClass.get(meta.get(TYPE_TAG));
            }

            if (elementClass == null)
            {
                // infer element class and create corresponding adapter and factory
                elementClass = elementClasses.get(
                        getElementClassId(attributeNames));
            }

            if (elementClass == null)
            {
                elementAdapter = new ArrayElementAdapter(attributeNames);
            }
            else
            {
                elementAdapter = new BeanElementAdapter(elementClass);
            }

            reader.close();
        } catch (Exception ex)
        {
            throw new PersistenceException(ex);
        }

        return elementAdapter.getProperties();
    }

    @Deprecated
    private static Map<String, String> readFormatAttributes(File file, IProgressMonitor monitor) throws PersistenceException
    {
        Map<String, String> meta = new HashMap<String, String>();
        try
        {
            BufferedReader r = new BufferedReader(IOUtils.openReader(file));
            boolean done = false;
            String cl = null;
            while (!done && (cl = r.readLine()) != null)
            {
                if (cl.startsWith(META_TAG))
                {
                    cl = cl.substring(META_TAG.length()).trim();
                    int pos = cl.indexOf(':');
                    if (pos > 0 && pos < cl.length() - 1)
                    {
                        String key = cl.substring(0, pos).trim();
                        String value = cl.substring(pos + 1).trim();
                        meta.put(key, value);
                    }
                }
                else
                {
                    done = true;
                }
            }
            r.close();
        } catch (Exception ex)
        {
            throw new PersistenceException(ex);
        }
        return meta;
    }
}