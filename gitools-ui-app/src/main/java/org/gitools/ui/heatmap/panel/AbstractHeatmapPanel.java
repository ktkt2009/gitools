/*
 * #%L
 * gitools-ui-app
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
package org.gitools.ui.heatmap.panel;

import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.HeatmapDim;
import org.gitools.heatmap.drawer.AbstractHeatmapDrawer;
import org.gitools.matrix.model.IMatrixView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class AbstractHeatmapPanel extends JPanel
{

    protected Heatmap heatmap;

    protected AbstractHeatmapDrawer drawer;
    @NotNull
    private final PropertyChangeListener heatmapListener;

    public AbstractHeatmapPanel(Heatmap heatmap, @NotNull AbstractHeatmapDrawer drawer)
    {
        this.heatmap = heatmap;
        this.drawer = drawer;

        heatmapListener = new PropertyChangeListener()
        {
            @Override
            public void propertyChange(@NotNull PropertyChangeEvent evt)
            {
                heatmapPropertyChanged(evt);
            }
        };

        updateSubscriptions(null);

        setPreferredSize(drawer.getSize());

        setBorder(null);
    }

    public Heatmap getHeatmap()
    {
        return heatmap;
    }

    public void setHeatmap(Heatmap heatmap)
    {
        Heatmap old = this.heatmap;
        this.heatmap = heatmap;
        this.drawer.setHeatmap(heatmap);
        updateSubscriptions(old);
        setPreferredSize(drawer.getSize());
    }

    public AbstractHeatmapDrawer getDrawer()
    {
        return drawer;
    }

    @Override
    protected void paintComponent(@NotNull Graphics g)
    {
        Dimension size = drawer.getSize();
        Rectangle box = new Rectangle(0, 0, size.width, size.height);
        Rectangle clip = g.getClipBounds();
        drawer.draw((Graphics2D) g, box, clip);
    }

    private void updateSubscriptions(@Nullable Heatmap old)
    {
        if (old != null)
        {
            old.removePropertyChangeListener(heatmapListener);
            /*old.getColumnLabelsHeader().removePropertyChangeListener(heatmapListener);
            old.getRowLabelsHeader().removePropertyChangeListener(heatmapListener);
			old.getCellDecorator().removePropertyChangeListener(heatmapListener);
			old.getMatrixView().removePropertyChangeListener(heatmapListener);*/
        }

        heatmap.addPropertyChangeListener(heatmapListener);
        /*heatmap.getColumnLabelsHeader().addPropertyChangeListener(heatmapListener);
		heatmap.getRowLabelsHeader().addPropertyChangeListener(heatmapListener);
		heatmap.getCellDecorator().addPropertyChangeListener(heatmapListener);
		heatmap.getMatrixView().addPropertyChangeListener(heatmapListener);*/
    }

    protected void heatmapPropertyChanged(@NotNull PropertyChangeEvent evt)
    {
        String pname = evt.getPropertyName();
        Object src = evt.getSource();

        //System.out.println(getClass().getSimpleName() + " " + src + " " + pname);

        if (src.equals(heatmap))
        {
            if (Heatmap.CELL_SIZE_CHANGED.equals(pname))
            {
                updateSize();
            }
        }
        else if (src.equals(heatmap.getMatrixView()))
        {
            if (IMatrixView.VISIBLE_COLUMNS_CHANGED.equals(pname)
                    || IMatrixView.VISIBLE_ROWS_CHANGED.equals(pname))
            {
                updateSize();
            }
        }
        else if (src.equals(heatmap.getRowDim())
                || src.equals(heatmap.getColumnDim()))
        {

            if (HeatmapDim.HEADER_SIZE_CHANGED.equals(pname)
                    || HeatmapDim.GRID_PROPERTY_CHANGED.equals(pname))
            {
                updateSize();
            }
        }

        repaint();
    }

    public void updateSize()
    {
        setPreferredSize(drawer.getSize());
        revalidate();
    }
}
