/*
 *  Copyright 2010 Universitat Pompeu Fabra.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package org.gitools.ui.actions.data;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import org.gitools.clustering.ClusteringMethod;
import org.gitools.clustering.ClusteringResults;
import org.gitools.clustering.HierarchicalClusteringResults;
import org.gitools.clustering.method.value.ClusterUtils;
import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.HeatmapDim;
import org.gitools.heatmap.header.HeatmapColoredLabelsHeader;
import org.gitools.heatmap.header.HeatmapHierarchicalColoredLabelsHeader;
import org.gitools.matrix.TransposedMatrixView;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.ui.actions.ActionUtils;
import org.gitools.ui.clustering.values.ClusteringValueWizard;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.platform.wizard.WizardDialog;

public class ClusteringByValueAction extends BaseAction {

	public ClusteringByValueAction() {
		super("Clustering");
		setDesc("Cluster by values");
	}

	@Override
	public boolean isEnabledByModel(Object model) {
		return model instanceof Heatmap
			|| model instanceof IMatrixView;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		final Heatmap heatmap = ActionUtils.getHeatmap();

		if (heatmap == null)
			return;

		final ClusteringValueWizard wiz = new ClusteringValueWizard(heatmap);
		WizardDialog wdlg = new WizardDialog(AppFrame.instance(), wiz);
		wdlg.setVisible(true);
		if (wdlg.isCancelled())
			return;

		final ClusteringMethod method = wiz.getMethod();

		JobThread.execute(AppFrame.instance(), new JobRunnable() {
			@Override public void run(IProgressMonitor monitor) {
				try {
					monitor.begin("Clustering  ...", 1);

					ClusteringResults results = method.cluster(wiz.getClusterData(), monitor);

					if (wiz.isSortDataSelected()) {
						if (wiz.isApplyToRows()) {
							TransposedMatrixView transposedMatrix = new TransposedMatrixView(heatmap.getMatrixView());
							ClusterUtils.updateVisibility(transposedMatrix, results.getDataIndicesByClusterTitle());
						}
						else
							ClusterUtils.updateVisibility(heatmap.getMatrixView(), results.getDataIndicesByClusterTitle());
					}

					if (wiz.isNewickExportSelected()) {
							File path = wiz.getSaveFilePage().getPathAsFile();
							BufferedWriter out = new BufferedWriter(new FileWriter(path));
							out.write(((HierarchicalClusteringResults) results).getTree().toString());
							out.flush();
							out.close();
					}

					if (wiz.isHeaderSelected()) {
						boolean hcl = results instanceof HierarchicalClusteringResults;

						HeatmapDim hdim = wiz.isApplyToRows() ?
							heatmap.getRowDim() : heatmap.getColumnDim();

						HeatmapColoredLabelsHeader header = hcl ?
							new HeatmapHierarchicalColoredLabelsHeader(hdim)
							: new HeatmapColoredLabelsHeader(hdim);

						header.setTitle("Clustering: " + wiz.getMethodName());

						if (hcl) {
							HeatmapHierarchicalColoredLabelsHeader hclHeader = (HeatmapHierarchicalColoredLabelsHeader) header;
							HierarchicalClusteringResults hclResults = (HierarchicalClusteringResults) results;

							hclHeader.setClusteringResults(hclResults);
							int level = hclResults.getTree().getDepth() / 2;
							hclHeader.setTreeLevel(level);
						}

						header.updateFromClusterResults(results);

						if (wiz.isApplyToRows())
							heatmap.getRowDim().addHeader(header);
						else
							heatmap.getColumnDim().addHeader(header);
					}

					monitor.end();
				}
				catch (Throwable ex) {
					monitor.exception(ex);
				}
			}
		});

		AppFrame.instance().setStatusText("Clusters created");
	}
}