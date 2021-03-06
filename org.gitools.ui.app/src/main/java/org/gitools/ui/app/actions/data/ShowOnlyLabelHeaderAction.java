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
package org.gitools.ui.app.actions.data;

import com.google.common.base.Predicate;
import org.apache.commons.lang.StringUtils;
import org.gitools.heatmap.HeatmapDimension;
import org.gitools.heatmap.header.ColoredLabel;
import org.gitools.heatmap.header.HeatmapColoredLabelsHeader;
import org.gitools.heatmap.header.HeatmapHeader;
import org.gitools.heatmap.header.HierarchicalClusterHeatmapHeader;
import org.gitools.ui.core.HeatmapPosition;
import org.gitools.ui.core.actions.HeatmapAction;
import org.gitools.ui.core.actions.dynamicactions.IHeatmapHeaderAction;

import java.awt.event.ActionEvent;


public class ShowOnlyLabelHeaderAction extends HeatmapAction implements IHeatmapHeaderAction {

    private String annotationValue;
    private HeatmapColoredLabelsHeader coloredHeader;

    public ShowOnlyLabelHeaderAction() {
        super("Show only label header");
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (annotationValue == null) {
            return;
        }

        HeatmapDimension dimension = coloredHeader.getHeatmapDimension();
        dimension.show(new Predicate<String>() {
            @Override
            public boolean apply(String identifier) {
                String value = coloredHeader.getColoredLabel(identifier).getValue();
                return StringUtils.equals(value, annotationValue);
            }
        });

    }

    @Override
    public void onConfigure(HeatmapHeader header, HeatmapPosition position) {

        if(header instanceof HierarchicalClusterHeatmapHeader) {
            header = ((HierarchicalClusterHeatmapHeader) header).getInteractionLevelHeader();
        }
        setEnabled(header instanceof HeatmapColoredLabelsHeader);

        if (header instanceof HeatmapColoredLabelsHeader) {

            coloredHeader = (HeatmapColoredLabelsHeader) header;
            annotationValue = position.getHeaderAnnotation();

            ColoredLabel coloredLabel = coloredHeader.getAssignedColoredLabel(annotationValue);

            setName("<html><i>Show</i> only <b>" + (coloredLabel == null ? annotationValue : coloredLabel.getDisplayedLabel()) + "</b> labels</html>");
        }

    }
}
