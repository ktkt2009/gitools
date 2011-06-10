/*
 *  Copyright 2011 Universitat Pompeu Fabra.
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

/*
 * ColoredClustersPage.java
 *
 * Created on 03-mar-2011, 18:51:34
 */

package org.gitools.ui.heatmap.header.coloredlabels;

import java.awt.Color;
import javax.swing.ListModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.gitools.heatmap.header.HeatmapColoredLabelsHeader;
import org.gitools.heatmap.header.HeatmapColoredLabelsHeader.ColoredLabel;
import org.gitools.ui.platform.component.ColorChooserLabel.ColorChangeListener;
import org.gitools.ui.platform.wizard.AbstractWizardPage;
import org.gitools.ui.utils.DocumentChangeListener;

public class ColoredLabelsGroupsPage extends AbstractWizardPage {

	private static class ClusterListModel implements ListModel {

		private ColoredLabel[] clusters;

		public ClusterListModel(ColoredLabel[] clusters) {
			this.clusters = clusters;
		}

		@Override
		public int getSize() {
			return clusters.length;
		}

		@Override
		public Object getElementAt(int index) {
			return clusters[index];
		}

		@Override
		public void addListDataListener(ListDataListener l) {
			//TODO
		}

		@Override
		public void removeListDataListener(ListDataListener l) {
			//TODO
		}
	}

	private HeatmapColoredLabelsHeader header;

    public ColoredLabelsGroupsPage(HeatmapColoredLabelsHeader header) {
		this.header = header;
		
        initComponents();

		clusterList.addListSelectionListener(new ListSelectionListener() {
			@Override public void valueChanged(ListSelectionEvent e) {
				clusterSelectionChanged(); }
		});

		clusterName.getDocument().addDocumentListener(new DocumentChangeListener() {
			@Override protected void update(DocumentEvent e) {
				clusterNameChanged(); }
		});

		clusterColor.addColorChangeListener(new ColorChangeListener() {
			@Override public void colorChanged(Color color) {
				clusterColorChanged(); }
		});

		setTitle("Labels configuration");
		setComplete(true);
    }

	@Override
	public void updateControls() {
		super.updateControls();

		ClusterListModel clusterListModel =
				new ClusterListModel(header.getClusters());

		clusterList.setModel(clusterListModel);
		clusterSelectionChanged();
	}

	private void clusterSelectionChanged() {
		int index = clusterList.getSelectedIndex();
		boolean sel = index != -1;
		clusterName.setEnabled(sel);
		clusterColor.setEnabled(sel);

		if (sel) {
			ColoredLabel cluster = header.getClusters()[index];
			clusterName.setText(cluster.getName());
			clusterColor.setColor(cluster.getColor());
		}
	}

	private void clusterNameChanged() {
		int index = clusterList.getSelectedIndex();
		ColoredLabel cluster = header.getClusters()[index];
		cluster.setName(clusterName.getText());
	}

	private void clusterColorChanged() {
		int index = clusterList.getSelectedIndex();
		ColoredLabel cluster = header.getClusters()[index];
		cluster.setColor(clusterColor.getColor());
	}


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        clusterList = new javax.swing.JList();
        jLabel6 = new javax.swing.JLabel();
        clusterName = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        clusterColor = new org.gitools.ui.platform.component.ColorChooserLabel();

        jLabel5.setText("Labels");

        clusterList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(clusterList);

        jLabel6.setText("Name");

        jLabel7.setText("Color");

        clusterColor.setColor(new java.awt.Color(1, 1, 1));
        clusterColor.setPreferredSize(new java.awt.Dimension(28, 28));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 510, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(clusterName, javax.swing.GroupLayout.DEFAULT_SIZE, 378, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(clusterColor, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 209, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel6)
                        .addComponent(clusterName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel7))
                    .addComponent(clusterColor, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.gitools.ui.platform.component.ColorChooserLabel clusterColor;
    private javax.swing.JList clusterList;
    private javax.swing.JTextField clusterName;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables

}
