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
package org.gitools.heatmap.drawer;

import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.drawer.header.HeatmapHeaderDrawer;
import org.gitools.heatmap.drawer.header.HeatmapHeaderIntersectionDrawer;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class HeatmapDrawer extends AbstractHeatmapDrawer
{

    private HeatmapBodyDrawer body;
    @NotNull
    private final HeatmapHeaderDrawer rowsHeader;
    @NotNull
    private final HeatmapHeaderDrawer colsHeader;
    @NotNull
    private final HeatmapHeaderIntersectionDrawer headerIntersection;
    /*private HeatmapLabelsDrawer rows;
    private HeatmapLabelsDrawer columns;
	private HeatmapColoredClustersDrawer rowsClusterSet;
	private HeatmapColoredClustersDrawer columnsClusterSet;*/

    public HeatmapDrawer(Heatmap heatmap)
    {
        super(heatmap);

        body = new HeatmapBodyDrawer(heatmap);
        rowsHeader = new HeatmapHeaderDrawer(heatmap, false);
        colsHeader = new HeatmapHeaderDrawer(heatmap, true);
        headerIntersection = new HeatmapHeaderIntersectionDrawer(heatmap, colsHeader, rowsHeader);

		/*rows = new HeatmapLabelsDrawer(heatmap, false);
        columns = new HeatmapLabelsDrawer(heatmap, true);
		rowsClusterSet = new HeatmapColoredClustersDrawer(heatmap, false);
		columnsClusterSet = new HeatmapColoredClustersDrawer(heatmap, true);*/
    }

    @Override
    public void draw(@NotNull Graphics2D g, Rectangle box, Rectangle clip)
    {
        Dimension bodySize = body.getSize();
        Dimension rowsSize = rowsHeader.getSize();
        Dimension columnsSize = colsHeader.getSize();
		/*Dimension rowsCSSize = rowsClusterSet.getSize();
		Dimension columnsCSSize = columnsClusterSet.getSize();*/

        Rectangle columnsBounds = new Rectangle(0, 0, columnsSize.width, columnsSize.height);
        //Rectangle columnsCSBounds = new Rectangle(0 - columnsSize.height, 0, columnsCSSize.width, columnsCSSize.height);
        Rectangle bodyBounds = new Rectangle(0, columnsSize.height, bodySize.width, bodySize.height);
        //Rectangle rowsCSBounds = new Rectangle(bodySize.width, columnsSize.height + columnsCSSize.height, rowsCSSize.width, rowsCSSize.height);
        Rectangle rowsBounds = new Rectangle(bodySize.width, columnsSize.height, rowsSize.width, rowsSize.height);
        Rectangle headerIntersectionBounds = new Rectangle(bodySize.width, 0, rowsSize.width, columnsSize.height);

        AffineTransform at = new AffineTransform();

        colsHeader.draw(g, columnsBounds, columnsBounds);
        at.setToIdentity();
        g.setTransform(at);
        body.draw(g, bodyBounds, bodyBounds);
        at.setToIdentity();
        g.setTransform(at);
        rowsHeader.draw(g, rowsBounds, rowsBounds);
        at.setToIdentity();
        g.setTransform(at);
        //headerIntersection.updateDrawers(null);
        headerIntersection.draw(g, headerIntersectionBounds, headerIntersectionBounds);

        /*rowsHeader.drawHeaderIntersection(g, headerIntersection);
        at.setToIdentity();
        g.setTransform(at);
        colsHeader.drawHeaderIntersection(g, headerIntersection);
        at.setToIdentity();
        g.setTransform(at); */
    }

    @NotNull
    @Override
    public Dimension getSize()
    {
        Dimension bodySize = body.getSize();
        Dimension rowsSize = rowsHeader.getSize();
        Dimension columnsSize = colsHeader.getSize();
        //Dimension rowsCSSize = rowsClusterSet.getSize();
        //Dimension columnsCSSize = columnsClusterSet.getSize();
        return new Dimension(bodySize.width + rowsSize.width, bodySize.height + columnsSize.height);
    }

    @NotNull
    @Override
    public HeatmapPosition getPosition(Point p)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @NotNull
    @Override
    public Point getPoint(HeatmapPosition p)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setPictureMode(boolean pictureMode)
    {
        super.setPictureMode(pictureMode);
        body.setPictureMode(pictureMode);
        rowsHeader.setPictureMode(pictureMode);
        colsHeader.setPictureMode(pictureMode);
        //rowsClusterSet.setPictureMode(pictureMode);
        //columnsClusterSet.setPictureMode(pictureMode);
    }

}
