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
package org.gitools.model.xml;

import org.gitools.matrix.model.IMatrixView;
import org.gitools.matrix.model.MatrixView;
import org.jetbrains.annotations.NotNull;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class MatrixViewXmlAdapter extends XmlAdapter<MatrixView, IMatrixView>
{

    @NotNull
    @Override
    public MatrixView marshal(@NotNull IMatrixView matrixView) throws Exception
    {
        return (MatrixView) matrixView;
    }

    @Override
    public IMatrixView unmarshal(MatrixView matrixView) throws Exception
    {
        return matrixView;
    }

}
