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
package org.gitools.ui.app.heatmap.header.wizard.heatmapheader;

import com.google.common.collect.Sets;
import org.gitools.analysis.clustering.ClusteringData;
import org.gitools.analysis.clustering.ClusteringException;
import org.gitools.analysis.clustering.ClusteringMethod;
import org.gitools.analysis.clustering.annotations.AnnPatClusteringData;
import org.gitools.analysis.clustering.annotations.AnnPatClusteringMethod;
import org.gitools.api.analysis.Clusters;
import org.gitools.api.analysis.IAggregator;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.matrix.IMatrixLayer;
import org.gitools.api.matrix.IMatrixPosition;
import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.HeatmapDimension;
import org.gitools.heatmap.HeatmapLayers;
import org.gitools.heatmap.header.HeatmapDecoratorHeader;
import org.gitools.matrix.model.matrix.AnnotationMatrix;
import org.gitools.ui.platform.dialog.MessageStatus;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.platform.progress.ProgressUtils;
import org.gitools.ui.platform.wizard.IWizardPage;
import org.gitools.utils.progressmonitor.ProgressMonitor;

import java.awt.*;
import java.util.*;

public class AggregationDecoratorHeaderWizard extends DecoratorHeaderWizard {

    private final HeatmapDimension headerDimension;
    private final HeatmapDimension aggregationDimension;
    private final Heatmap heatmap;
    private Collection<String> aggregationAnnotationLabels;
    private Map<String, Set<String>> aggregationIndicesByAnnotation;
    private AggregationDataSourcePage dataSourceAggregationPage;
    private AnnotationSourcePage dataSourceAnnotationPage;

    public AggregationDecoratorHeaderWizard(HeatmapDecoratorHeader header, Heatmap heatmap, HeatmapDimension headerDimension, HeatmapDimension aggregationDimension) {
        super(header);

        this.heatmap = heatmap;
        this.headerDimension = headerDimension;
        this.aggregationDimension = aggregationDimension;
    }

    @Override
    public void addPages() {

        HeatmapLayers layers = heatmap.getLayers();
        dataSourceAggregationPage = new AggregationDataSourcePage(headerDimension, aggregationDimension, layers.getLayerNames(), layers.getTopLayerIndex());
        addPage(dataSourceAggregationPage);

        dataSourceAnnotationPage = new AnnotationSourcePage(aggregationDimension, "The aggregation is calculated for each distinct value of the chosen annotation individually");
        addPage(dataSourceAnnotationPage);

        super.addPages();
    }

    @Override
    public IWizardPage getNextPage(IWizardPage page) {

        if (page == dataSourceAggregationPage) {
            if (dataSourceAggregationPage.aggregateAnnotationsSeparately()) {
                return dataSourceAnnotationPage;
            } else {
                try {
                    prepareAggregationByColumns();
                } catch (Exception e) {
                    dataSourceAggregationPage.setMessage(MessageStatus.ERROR, "Error aggregating the values");
                    e.printStackTrace();
                    return dataSourceAggregationPage;
                }

                IWizardPage successPage = super.getNextPage(super.getNextPage(page));
                createAggregationAnnotations(successPage);
                return page;
            }
        }

        if (page == this.dataSourceAnnotationPage) {
            try {
                prepareAggregationByAnnotations();
            } catch (Exception e) {
                dataSourceAnnotationPage.setMessage(MessageStatus.ERROR, "Error clustering the selected annotation.");
                e.printStackTrace();
                return dataSourceAnnotationPage;
            }
            createAggregationAnnotations(super.getNextPage(page));
            return page;
        }

        return super.getNextPage(page);

    }

    private void prepareAggregationByAnnotations() throws ClusteringException {

        String pattern = dataSourceAnnotationPage.getSelectedPattern();
        getHeader().setAnnotationPattern(pattern);
        final ClusteringData data = new AnnPatClusteringData(aggregationDimension, pattern);

        String annotationLabelPrefix = dataSourceAggregationPage.getAggregator() + " of " + dataSourceAggregationPage.getSelectedDataValueName() + ": ";
        ClusteringMethod clusteringMethod = new AnnPatClusteringMethod(annotationLabelPrefix);
        Clusters results = clusteringMethod.cluster(data, ProgressMonitor.get());
        aggregationAnnotationLabels = results.getClusters();
        aggregationIndicesByAnnotation = results.getClustersMap();
    }

    private void prepareAggregationByColumns() {

        String annotationLabel = dataSourceAggregationPage.getAggregator() + " of ";

        Set<String> indicesToAggregate = aggregationDimension.getSelected();

        if (dataSourceAggregationPage.useAllColumnsOrRows()) {
            indicesToAggregate = Sets.newHashSet(aggregationDimension);
        }

        annotationLabel += " " + dataSourceAggregationPage.getSelectedDataValueName();
        aggregationAnnotationLabels = Arrays.asList(annotationLabel);

        aggregationIndicesByAnnotation = new HashMap<>();
        aggregationIndicesByAnnotation.put(aggregationAnnotationLabels.iterator().next(), indicesToAggregate);

    }

    private void createAggregationAnnotations(final IWizardPage successPage) {

        JobThread.execute(ProgressUtils.getParentGlassPaneWindow((Component) getCurrentPage()), new JobRunnable() {
            @Override
            public void run(IProgressMonitor monitor) {


                // Compute the annotations and store the results.
                AnnotationMatrix annotations = headerDimension.getAnnotations();
                IAggregator aggregator = dataSourceAggregationPage.getAggregator();
                IMatrixLayer<Double> aggregationLayer = heatmap.getLayers().get(dataSourceAggregationPage.getAggregationLayer());

                for (String annotationLabel : aggregationAnnotationLabels) {
                    Set<String> aggregationIdentifiers = aggregationIndicesByAnnotation.get(annotationLabel);

                    IMatrixPosition position = heatmap.newPosition();

                    for (String identifier : position.iterate(headerDimension).monitor(monitor, "Aggregating")) {

                        Double aggregatedValue = aggregator.aggregate(position.iterate(aggregationLayer, aggregationDimension.subset(aggregationIdentifiers)));

                        if (aggregatedValue != null) {
                            annotations.setAnnotation(identifier, annotationLabel, Double.toString(aggregatedValue));
                        }
                    }
                }

                int maxSize = 160;
                int preferredSize;
                if (aggregationAnnotationLabels.size() < 2) {
                    preferredSize = aggregationAnnotationLabels.size() * 30;
                } else {
                    preferredSize = headerDimension.getCellSize() * aggregationAnnotationLabels.size();
                }

                getHeader().setAnnotationLabels(aggregationAnnotationLabels);
                getHeader().setSize(preferredSize > maxSize ? maxSize : preferredSize);
                getHeader().setTitle(dataSourceAggregationPage.getAggregator() +
                        " of " + dataSourceAggregationPage.getSelectedDataValueName());

                setCurrentPage(successPage);
                fireWizardUpdate();

            }
        });
    }
}
