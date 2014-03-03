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
package org.gitools.ui.app.analysis.clustering;

import org.gitools.analysis.clustering.ClusteringData;
import org.gitools.analysis.clustering.ClusteringMethod;
import org.gitools.analysis.clustering.method.value.HierarchicalMethod;
import org.gitools.analysis.clustering.method.value.MatrixClusteringData;
import org.gitools.analysis.clustering.method.value.WekaCobWebMethod;
import org.gitools.analysis.clustering.method.value.WekaKmeansMethod;
import org.gitools.api.matrix.MatrixDimensionKey;
import org.gitools.heatmap.Heatmap;
import org.gitools.ui.app.IconNames;
import org.gitools.ui.platform.IconUtils;
import org.gitools.ui.platform.wizard.AbstractWizard;
import org.gitools.ui.platform.wizard.IWizardPage;

import java.util.Arrays;
import java.util.List;

public class ClusteringWizard extends AbstractWizard {

    private final Heatmap heatmap;
    private final List<? extends ClusteringMethod> methods;

    private MethodSelectionPage methodPage;
    private CobwebParamsPage cobwebPage;
    private HCLParamsPage hclPage;
    private KmeansParamsPage kmeansPage;

    private static HierarchicalMethod HCL_METHOD = new HierarchicalMethod();
    private static WekaKmeansMethod KMEANS_METHOD = new WekaKmeansMethod();
    private static WekaCobWebMethod COWEB_METHOD = new WekaCobWebMethod();

    public ClusteringWizard(Heatmap heatmap) {
        super();
        this.heatmap = heatmap;
        this.methods = Arrays.asList(HCL_METHOD, KMEANS_METHOD, COWEB_METHOD);

        setTitle("Clustering by value");
        setLogo(IconUtils.getImageIconResourceScaledByHeight(IconNames.LOGO_CLUSTERING, 96));
    }

    @Override
    public void addPages() {

        methodPage = new MethodSelectionPage(methods, heatmap);
        addPage(methodPage);

        hclPage = new HCLParamsPage(HCL_METHOD);
        addPage(hclPage);

        kmeansPage = new KmeansParamsPage(KMEANS_METHOD);
        addPage(kmeansPage);

        cobwebPage = new CobwebParamsPage(COWEB_METHOD);
        addPage(cobwebPage);
    }

    @Override
    public boolean canFinish() {
        return currentPage == cobwebPage || currentPage == hclPage || currentPage == kmeansPage;
    }

    @Override
    public boolean isLastPage(IWizardPage page) {
        return currentPage != methodPage;
    }

    @Override
    public IWizardPage getNextPage(IWizardPage currentPage) {

        if (currentPage == methodPage) {
            return getMethodConfigPage();
        }

        return null;
    }

    private IWizardPage getMethodConfigPage() {

        ClusteringMethod method = methodPage.getSelectedMethod();

        if (method instanceof WekaCobWebMethod) {
            return cobwebPage;
        } else if (method instanceof HierarchicalMethod) {
            return hclPage;
        } else if (method instanceof WekaKmeansMethod) {
            return kmeansPage;
        }

        return null;
    }


    public ClusteringData getClusterData() {
        return new MatrixClusteringData(
                heatmap,
                methodPage.getClusteringDimension(),
                methodPage.getAggregationDimension(),
                methodPage.getClusteringLayer()
        );
    }

    public MatrixDimensionKey getClusteringDimension() {
        return methodPage.getClusteringDimension();
    }

    public String getClusteringLayer() {
        return methodPage.getClusteringLayer();
    }

    public ClusteringMethod getMethod() {
        return methodPage.getSelectedMethod();
    }
}
