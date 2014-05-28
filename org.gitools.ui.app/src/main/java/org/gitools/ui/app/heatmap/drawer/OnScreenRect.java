/*
 * #%L
 * org.gitools.ui.app
 * %%
 * Copyright (C) 2013 - 2014 Universitat Pompeu Fabra - Biomedical Genomics group
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
package org.gitools.ui.app.heatmap.drawer;

/**
 * Holds the information about which rows and coluns fit on
 * screen and are thus visible
 */
public class OnScreenRect {

    public final int rowStart;
    public final int rowEnd;
    public final int colStart;
    public final int colEnd;
    public final int width;
    public final int height;

    public OnScreenRect(int rowStart, int rowEnd, int colStart, int colEnd, int width, int height) {
        this.rowStart = rowStart;
        this.rowEnd = rowEnd;
        this.colStart = colStart;
        this.colEnd = colEnd;
        this.width = width;
        this.height = height;
    }

    public OnScreenRect() {
        this(0, 0, 0, 0, 0, 0);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof OnScreenRect) {

            OnScreenRect rect = (OnScreenRect) obj;

            return (this.rowStart == rect.rowStart &&
                    this.rowEnd == rect.rowEnd &&
                    this.colStart == rect.colStart &&
                    this.colEnd == rect.colEnd &&
                    this.width == rect.width &&
                    this.height == rect.height);

        }

        return false;
    }
}