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
package org.gitools.ui.app.analysis.htest.editor;


import org.apache.commons.lang.WordUtils;
import org.apache.velocity.VelocityContext;
import org.gitools.analysis.htest.HtestAnalysis;
import org.gitools.analysis.stats.test.factory.TestFactory;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.resource.IResourceLocator;
import org.gitools.analysis._DEPRECATED.heatmap.Heatmap;
import org.gitools.analysis._DEPRECATED.model.ToolConfig;
import org.gitools.analysis._DEPRECATED.model.decorator.impl.BinaryDecorator;
import org.gitools.analysis.htest.enrichment.format.EnrichmentAnalysisFormat;
import org.gitools.ui.app.IconNames;
import org.gitools.ui.app.analysis.editor.AnalysisDetailsEditor;
import org.gitools.ui.app.heatmap.editor.HeatmapEditor;
import org.gitools.ui.platform.Application;
import org.gitools.ui.platform.IconUtils;
import org.gitools.ui.platform.editor.EditorsPanel;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.utils.cutoffcmp.CutoffCmp;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractHtestAnalysisEditor<T extends HtestAnalysis> extends AnalysisDetailsEditor<T> {

    private String formatExtension;

    protected AbstractHtestAnalysisEditor(T analysis, String template, String formatExtension) {
        super(analysis, template, null);
        this.formatExtension = formatExtension;
    }


    @Override
    protected void prepareContext(VelocityContext context) {

        IResourceLocator fileRef = analysis.getData().getLocator();

        context.put("dataFile", fileRef != null ? fileRef.getName() : "Not defined");

        ToolConfig testConfig = analysis.getTestConfig();
        if (!testConfig.get(TestFactory.TEST_NAME_PROPERTY).equals("")) {
            context.put("test", WordUtils.capitalize(testConfig.get(TestFactory.TEST_NAME_PROPERTY)));
            HashMap<String, Object> testAttributes = new HashMap<>();
            for (String key : testConfig.getConfiguration().keySet()) {
                if (!key.equals(TestFactory.TEST_NAME_PROPERTY)) {
                    testAttributes.put(WordUtils.capitalize(key), WordUtils.capitalize(testConfig.get(key)));
                }
            }
            if (testAttributes.size() > 0) {
                context.put("testAttributes", testAttributes);
            }

        }

        CutoffCmp cmp = analysis.getBinaryCutoffCmp();
        String filterDesc = cmp == null ? "Not filtered" : "Binary cutoff filter for values " + cmp.getLongName() + " " + analysis.getBinaryCutoffValue();
        context.put("filterDesc", filterDesc);

        fileRef = analysis.getModuleMap().getLocator();

        context.put("modulesFile", fileRef != null ? fileRef.getName() : "Unknown");

        context.put("moduleMinSize", analysis.getMinModuleSize());
        int maxSize = analysis.getMaxModuleSize();
        context.put("moduleMaxSize", maxSize != Integer.MAX_VALUE ? maxSize : "No limit");

        if (analysis.getMtc().equals("bh")) {
            context.put("mtc", "Benjamini Hochberg FDR");
        } else if (analysis.getMtc().equals("bonferroni")) {
            context.put("mtc", "Bonferroni");
        }

        if (analysis.getResults() != null) {
            fileRef = analysis.getResults().getLocator();
            context.put("resultsFile", fileRef != null ? fileRef.getName() : "Not defined");
        }

        fileRef = analysis.getLocator();
        if (fileRef != null) {
            context.put("analysisLocation", fileRef.getURL());

            if (fileRef.isWritable()) {
                setSaveAllowed(true);
            }
        }

        super.prepareContext(context);
    }

    @Override
    protected void performUrlAction(String name, Map<String, String> params) {
        if ("NewDataHeatmap".equals(name)) {
            newDataHeatmap();
        } else if ("NewResultsHeatmap".equals(name)) {
            newResultsHeatmap();
        }
    }

    protected void newDataHeatmap() {
        if (analysis.getData() == null) {
            Application.get().setStatusText("Analysis doesn't contain data.");
            return;
        }

        final EditorsPanel editorPanel = Application.get().getEditorsPanel();

        JobThread.execute(Application.get(), new JobRunnable() {
            @Override
            public void run(IProgressMonitor monitor) {
                monitor.begin("Creating new heatmap from data ...", 1);

                Heatmap heatmap = new Heatmap(analysis.getData().get());
                String testName = analysis.getTestConfig().getConfiguration().get(TestFactory.TEST_NAME_PROPERTY);
                if (!testName.equals(TestFactory.ZSCORE_TEST)) {
                    heatmap.getLayers().get(0).setDecorator(new BinaryDecorator());
                }
                heatmap.setTitle(analysis.getTitle() + " (data)");

                final HeatmapEditor editor = new HeatmapEditor(heatmap);

                editor.setName(editorPanel.deriveName(getName(), formatExtension, "-data", ""));

                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        editorPanel.addEditor(editor);
                        Application.get().setStatusText("New heatmap created.");
                    }
                });
            }
        });
    }

    protected void newResultsHeatmap() {
        if (analysis.getResults() == null) {
            Application.get().setStatusText("Analysis doesn't contain results.");
            return;
        }

        final EditorsPanel editorPanel = Application.get().getEditorsPanel();

        JobThread.execute(Application.get(), new JobRunnable() {
            @Override
            public void run(IProgressMonitor monitor) {
                monitor.begin("Creating new heatmap from results ...", 1);

                final HeatmapEditor editor = new HeatmapEditor(createHeatmap(analysis));
                editor.setIcon(IconUtils.getIconResource(IconNames.analysisHeatmap16));

                editor.setName(editorPanel.deriveName(getName(), EnrichmentAnalysisFormat.EXTENSION, "-results", ""));

                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        editorPanel.addEditor(editor);
                        Application.get().setStatusText("Heatmap results created.");
                    }
                });
            }
        });
    }

    protected Heatmap createHeatmap(T analysis) {
        Heatmap heatmap = new Heatmap(analysis.getResults().get());
        heatmap.setTitle(analysis.getTitle() + " (results)");
        return heatmap;
    }

    /*TODO
    protected static List<BaseAction> createToolBar(EnrichmentAnalysis analysis) {
        ViewRelatedDataFromAction action = new ViewRelatedDataFromAction(analysis.getTitle(), analysis.getData().get(), analysis.getModuleMap().get(), MatrixDimensionKey.ROWS);
        List<BaseAction> tb = new ArrayList<>();
        tb.add(action);
        return tb;
    }*/
}
