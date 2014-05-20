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
package org.gitools.ui.app.dialog.attributes;

import org.gitools.ui.app.dialog.ListDialog;
import org.gitools.ui.core.utils.FileChooserUtils;
import org.gitools.ui.platform.dialog.ExceptionGlassPane;
import org.gitools.ui.platform.settings.Settings;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AttributesSelectionDialog<T> extends javax.swing.JDialog {
    /**
     * A return status code - returned if Cancel button has been pressed
     */
    private static final int RET_CANCEL = 0;
    /**
     * A return status code - returned if OK button has been pressed
     */
    public static final int RET_OK = 1;

    private final AttributesSelectionModel listModel;

    /**
     * Creates new form AttributesSelectionDialog
     */
    public AttributesSelectionDialog(java.awt.Frame parent, final T[] attributes) {
        super(parent, true);

        initComponents();

        listModel = new AttributesSelectionModel<>(attributes);

        listModel.addListDataListener(new ListDataListener() {
            @Override
            public void intervalAdded(ListDataEvent e) {
                contentsChanged(e);
            }

            @Override
            public void intervalRemoved(ListDataEvent e) {
                contentsChanged(e);
            }

            @Override
            public void contentsChanged(ListDataEvent e) {
                updateButtons();
            }
        });

        list.setModel(listModel);

        list.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                updateButtons();
            }
        });
    }

    private void updateButtons() {
        boolean thereIsElements = listModel.getSize() > 0;
        boolean thereIsSelection = !list.getSelectionModel().isSelectionEmpty();
        int totalSize = listModel.getAttributes().length;

        List<Integer> selIndices = listModel.getSelectedIndices();
        int lastSelectionIndex = selIndices.size() - 1;
        boolean firstSelected = false;
        boolean lastSelected = false;
        for (int i : list.getSelectedIndices()) {
            firstSelected |= (i == 0);
            lastSelected |= (i == lastSelectionIndex);
        }

        addBtn.setEnabled(listModel.getSize() < totalSize);
        removeBtn.setEnabled(thereIsElements && thereIsSelection);
        upBtn.setEnabled(thereIsElements && thereIsSelection && !firstSelected);
        downBtn.setEnabled(thereIsElements && thereIsSelection && !lastSelected);
        saveBtn.setEnabled(thereIsElements);

        okButton.setEnabled(thereIsElements);
    }

    /**
     * @return the return status of this dialog - one of RET_OK or RET_CANCEL
     */
    public int getReturnStatus() {
        return returnStatus;
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

        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        list = new javax.swing.JList();
        addBtn = new javax.swing.JButton();
        removeBtn = new javax.swing.JButton();
        upBtn = new javax.swing.JButton();
        downBtn = new javax.swing.JButton();
        loadBtn = new javax.swing.JButton();
        saveBtn = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();

        setTitle("Select attributes to export...");
        setLocationByPlatform(true);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        okButton.setText("OK");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        jScrollPane1.setViewportView(list);

        addBtn.setText("Add...");
        addBtn.setEnabled(false);
        addBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addBtnActionPerformed(evt);
            }
        });

        removeBtn.setText("Remove");
        removeBtn.setEnabled(false);
        removeBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeBtnActionPerformed(evt);
            }
        });

        upBtn.setText("Move up");
        upBtn.setEnabled(false);
        upBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                upBtnActionPerformed(evt);
            }
        });

        downBtn.setText("Move down");
        downBtn.setEnabled(false);
        downBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downBtnActionPerformed(evt);
            }
        });

        loadBtn.setText("Load...");
        loadBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadBtnActionPerformed(evt);
            }
        });

        saveBtn.setText("Save...");
        saveBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 424, Short.MAX_VALUE).addGroup(layout.createSequentialGroup().addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(cancelButton)).addGroup(layout.createSequentialGroup().addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 329, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false).addComponent(downBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(upBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(removeBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(addBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(loadBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(saveBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))).addContainerGap()));

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[]{cancelButton, okButton});

        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(addBtn).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(removeBtn).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(upBtn).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(downBtn).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(loadBtn).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(saveBtn)).addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(cancelButton).addComponent(okButton)).addContainerGap()));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        doClose(RET_OK);
    }//GEN-LAST:event_okButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        doClose(RET_CANCEL);
    }//GEN-LAST:event_cancelButtonActionPerformed

    /**
     * Closes the dialog
     */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        doClose(RET_CANCEL);
    }//GEN-LAST:event_closeDialog

    private void addBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addBtnActionPerformed
        ListDialog dlg = new ListDialog(getOwner(), true, listModel.getUnselectedAttributes().toArray());
        dlg.setTitle("Attributes to add");
        dlg.setVisible(true);

        List<Integer> unselIndices = listModel.getUnselectedIndices();
        List<Integer> selIndices = new ArrayList<>();
        if (dlg.getReturnStatus() == ListDialog.RET_OK) {
            for (int i : dlg.getSelectedIndices())
                selIndices.add(unselIndices.get(i));
            listModel.select(selIndices);
        }
    }//GEN-LAST:event_addBtnActionPerformed

    private void removeBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeBtnActionPerformed
        int[] listIndices = list.getSelectedIndices();
        List<Integer> selIndices = listModel.getSelectedIndices();
        List<Integer> removedIndices = new ArrayList<>(listIndices.length);

        for (int i : listIndices)
            removedIndices.add(selIndices.get(i));

        listModel.unselect(removedIndices);
        list.getSelectionModel().clearSelection();
    }//GEN-LAST:event_removeBtnActionPerformed

    private void loadBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadBtnActionPerformed
        Map<String, Integer> indexMap = new HashMap<>();
        Object[] attributes = listModel.getAttributes();
        for (int i = 0; i < attributes.length; i++)
            indexMap.put(attributes[i].toString().trim(), i);

        List<Integer> indices = new ArrayList<>();

        try {
            File file = FileChooserUtils.selectFile("Select file ...", Settings.get().getLastPath(), FileChooserUtils.MODE_OPEN).getFile();

            if (file == null) {
                return;
            }

            Settings.get().setLastPath(file.getParent());

            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    Integer index = indexMap.get(line);
                    if (index != null) {
                        indices.add(index);
                    }
                }
            }
        } catch (IOException ex) {
            ExceptionGlassPane edlg = new ExceptionGlassPane(getOwner(), ex);
            edlg.setVisible(true);
        }

        listModel.setSelectedIndices(indices);
        list.getSelectionModel().clearSelection();
    }//GEN-LAST:event_loadBtnActionPerformed

    private void saveBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveBtnActionPerformed
        Object[] attributes = listModel.getAttributes();

        try {
            File file = FileChooserUtils.selectFile("Select file name ...", Settings.get().getLastFilterPath(), FileChooserUtils.MODE_SAVE).getFile();

            if (file == null) {
                return;
            }

            Settings.get().setLastPath(file.getParent());

            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            List<Integer> selIndices = listModel.getSelectedIndices();
            for (Integer i : selIndices)
                bw.append(attributes[i].toString()).append('\n');
            bw.close();
        } catch (Exception ex) {
            ExceptionGlassPane edlg = new ExceptionGlassPane(getOwner(), ex);
            edlg.setVisible(true);
        }
    }//GEN-LAST:event_saveBtnActionPerformed

    private void upBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_upBtnActionPerformed
        int[] listIndices = list.getSelectedIndices();
        List<Integer> selIndices = listModel.getSelectedIndices();
        List<Integer> moveIndices = new ArrayList<>(listIndices.length);

        for (int i : listIndices)
            moveIndices.add(selIndices.get(i));

        listModel.moveUp(moveIndices);

        for (int i = 0; i < listIndices.length; i++)
            listIndices[i]--;

        list.setSelectedIndices(listIndices);
    }//GEN-LAST:event_upBtnActionPerformed

    private void downBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_downBtnActionPerformed
        int[] listIndices = list.getSelectedIndices();
        List<Integer> selIndices = listModel.getSelectedIndices();
        List<Integer> moveIndices = new ArrayList<>(listIndices.length);

        for (int i : listIndices)
            moveIndices.add(selIndices.get(i));

        listModel.moveDown(moveIndices);

        for (int i = 0; i < listIndices.length; i++)
            listIndices[i]++;

        list.setSelectedIndices(listIndices);
    }//GEN-LAST:event_downBtnActionPerformed

    private void doClose(int retStatus) {
        returnStatus = retStatus;
        setVisible(false);
        dispose();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addBtn;
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton downBtn;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JList list;
    private javax.swing.JButton loadBtn;
    private javax.swing.JButton okButton;
    private javax.swing.JButton removeBtn;
    private javax.swing.JButton saveBtn;
    private javax.swing.JButton upBtn;
    // End of variables declaration//GEN-END:variables

    private int returnStatus = RET_CANCEL;

    public List<Integer> getSelectedIndices() {
        return listModel.getSelectedIndices();
    }
}
