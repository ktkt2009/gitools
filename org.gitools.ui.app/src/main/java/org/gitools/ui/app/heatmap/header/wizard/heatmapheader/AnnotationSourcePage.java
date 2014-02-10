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

import org.gitools.api.matrix.IAnnotations;
import org.gitools.api.ApplicationContext;
import org.gitools.heatmap.HeatmapDimension;
import org.gitools.matrix.format.AnnotationMatrixFormat;
import org.gitools.matrix.model.matrix.AnnotationMatrix;
import org.gitools.api.resource.ResourceReference;
import org.gitools.persistence.locators.UrlResourceLocator;
import org.gitools.ui.platform.dialog.MessageStatus;
import org.gitools.ui.platform.wizard.AbstractWizardPage;
import org.gitools.ui.app.settings.Settings;
import org.gitools.ui.app.utils.FileChooserUtils;
import org.gitools.ui.app.utils.LogUtils;
import org.gitools.ui.app.wizard.common.AnnotationOption;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AnnotationSourcePage extends AbstractWizardPage {

    private final HeatmapDimension hdim;
    public String infoMessage = "";
    private int[] selectedIndices;
    private List<AnnotationOption> annotationOptions;

    public AnnotationSourcePage(HeatmapDimension hdim, String infoMessage) {
        this.hdim = hdim;

        initComponents();

        this.selectedIndices = new int[0];
        this.infoMessage = infoMessage;
        setTitle("Select the annotation");
        setMessage(MessageStatus.INFO, infoMessage);
        setComplete(false);
    }


    @Override
    public void updateControls() {
        super.updateControls();

        IAnnotations am = hdim.getAnnotations();
        if (am != null && !am.getLabels().isEmpty() && annList.getModel().getSize() != am.getLabels().size()) {
            DefaultListModel<AnnotationOption> model = new DefaultListModel<>();

            annotationOptions = new ArrayList<>(am.getLabels().size());
            for (String key : am.getLabels()) {
                String description = hdim.getAnnotations().getAnnotationMetadata("description", key);
                annotationOptions.add(new AnnotationOption(key, description));
            }

            Collections.sort(annotationOptions, new Comparator<AnnotationOption>() {
                @Override
                public int compare(AnnotationOption o1, AnnotationOption o2) {
                    return o1.toString().toUpperCase().compareTo(o2.toString().toUpperCase());
                }
            });

            for (AnnotationOption annotationOption : annotationOptions) {
                model.addElement(annotationOption);
            }

            annList.setModel(model);
        }
        annList.setSelectedIndices(selectedIndices);
    }

    @Override
    public void updateModel() {
        super.updateModel();
    }


    public String getSelectedPattern() {

        StringBuilder sb = new StringBuilder();
        int[] values = annList.getSelectedIndices();
        if (values.length == 0) {
            return "";
        }

        sb.append("${");
        sb.append(getSelectedAnnotation());
        sb.append("}");

        return sb.toString();
    }


    public String getSelectedAnnotation() {
        if (annList.getSelectedIndex() != -1) {
            return annotationOptions.get(annList.getSelectedIndex()).getKey();
        } else {
            return "";
        }
    }

    public String getAnnotationMetadata(String key) {
        return hdim.getAnnotations().getAnnotationMetadata(key, getSelectedAnnotation());
    }


    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        optGroup = new javax.swing.ButtonGroup();
        jScrollPane1 = new javax.swing.JScrollPane();
        annList = new javax.swing.JList();
        jLabel1 = new javax.swing.JLabel();
        loadAnnotations = new javax.swing.JButton();

        annList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        annList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                annListValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(annList);

        jLabel1.setText("Available " + hdim.getId() + " annotations");

        loadAnnotations.setText("Add annotations from file...");
        loadAnnotations.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadAnnotationsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(24, 24, 24)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel1)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(loadAnnotations))
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 462, Short.MAX_VALUE))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(19, 19, 19)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel1)
                                        .addComponent(loadAnnotations))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 302, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(29, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void annListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_annListValueChanged
        boolean complete = annList.getSelectedIndices().length > 0;
        setComplete(complete);

        if (complete) {
            int oldvalue = selectedIndices.length > 0 ? selectedIndices[0] : -1;
            selectedIndices = annList.getSelectedIndices();
            if (oldvalue != selectedIndices[0]) {
                setMessage(MessageStatus.INFO, this.infoMessage);
            }
        }
    }//GEN-LAST:event_annListValueChanged

    private void loadAnnotationsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadAnnotationsActionPerformed
        try {
            File file = FileChooserUtils.selectFile("Open annotations file", Settings.getDefault().getLastAnnotationPath(), FileChooserUtils.MODE_OPEN).getFile();

            if (file != null) {
                hdim.addAnnotations(new ResourceReference<>(new UrlResourceLocator(file), ApplicationContext.getPersistenceManager().getFormat(AnnotationMatrixFormat.EXTENSION, AnnotationMatrix.class)).get());
                updateControls();
                //annFile.setText(file.getName());
            }
        } catch (Exception ex) {
            LogUtils.logException(ex, LoggerFactory.getLogger(getClass()));
        }
    }//GEN-LAST:event_loadAnnotationsActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList annList;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton loadAnnotations;
    private javax.swing.ButtonGroup optGroup;
    // End of variables declaration//GEN-END:variables

}
