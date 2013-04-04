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
package org.gitools.exporter;

import org.gitools.datafilters.ValueTranslator;
import org.gitools.datafilters.ValueTranslatorFactory;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.matrix.model.element.IElementAttribute;
import org.gitools.utils.fileutils.IOUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class TextMatrixViewExporter
{

    public static void exportMatrix(@NotNull IMatrixView matrixView, int propIndex, File file) throws IOException
    {
        PrintWriter pw = new PrintWriter(IOUtils.openWriter(file));

        int rowCount = matrixView.getRowCount();
        int colCount = matrixView.getColumnCount();

        //header

        for (int c = 0; c < colCount; c++)
            pw.print("\t" + matrixView.getColumnLabel(c));
        pw.println();

        // body

        IElementAttribute attr = matrixView.getContents().getCellAttributes().get(propIndex);

        ValueTranslator vt = ValueTranslatorFactory.createValueTranslator(attr.getValueClass());

        for (int r = 0; r < rowCount; r++)
        {
            pw.print(matrixView.getRowLabel(r).toString());
            for (int c = 0; c < colCount; c++)
            {
                Object value = matrixView.getCellValue(r, c, propIndex);
                String valueString = vt.valueToString(value);
                pw.print("\t" + valueString);
            }
            pw.println();
        }

        pw.close();
    }

    public static void exportTable(@NotNull IMatrixView matrixView, @NotNull int[] propIndices, File file) throws IOException
    {
        PrintWriter pw = new PrintWriter(IOUtils.openWriter(file));

        List<IElementAttribute> attributes = matrixView.getCellAttributes();

        final int rowCount = matrixView.getRowCount();
        final int colCount = matrixView.getColumnCount();

        final int propCount = propIndices.length;

        ValueTranslator[] vt = new ValueTranslator[propCount];

        // header

        pw.print("column\trow");
        for (int i = 0; i < propIndices.length; i++)
        {
            IElementAttribute attr = attributes.get(propIndices[i]);

            vt[i] = ValueTranslatorFactory.createValueTranslator(attr.getValueClass());

            pw.print("\t" + attr.getId());
        }
        pw.println();

        // body

        for (int i = 0; i < rowCount * colCount; i++)
        {
            final int r = i / colCount;
            final int c = i % colCount;

            if (matrixView.getCell(r, c) != null)
            {
                pw.print(matrixView.getColumnLabel(c));
                pw.print("\t" + matrixView.getRowLabel(r));
                for (int p = 0; p < propCount; p++)
                {
                    Object value = matrixView.getCellValue(r, c, propIndices[p]);
                    String valueString = vt[p].valueToString(value);
                    pw.print("\t" + valueString);
                }
                pw.println();
            }
        }

        pw.close();
    }
}
