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
package org.gitools.ui.app.analysis.correlation.wizard;

import org.gitools.api.matrix.IMatrixLayer;
import org.gitools.api.matrix.view.IMatrixViewLayers;
import org.gitools.ui.core.utils.DocumentChangeListener;
import org.gitools.ui.platform.IconUtils;
import org.gitools.ui.platform.dialog.MessageStatus;
import org.gitools.ui.platform.icons.IconNames;
import org.gitools.ui.platform.wizard.AbstractWizardPage;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

public class CorrelationFromEditorPage extends AbstractWizardPage {

    /**
     * Creates new form CorrelationPage
     */
    public CorrelationFromEditorPage(IMatrixViewLayers<? extends IMatrixLayer> layers) {
        super();

        initComponents();

        setTitle("Configure correlation options");

        setLogo(IconUtils.getImageIconResourceScaledByHeight(IconNames.LOGO_METHOD, 96));

        setComplete(true);

        Vector<IMatrixLayer> layersModel = new Vector<>();
        for (IMatrixLayer layer : layers) {
            layersModel.add(layer);
        }
        attributeCb.setModel(new DefaultComboBoxModel(layersModel));
        attributeCb.setSelectedItem(layers.getTopLayer());

        replaceEmptyValuesCheck.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                replaceValueField.setEnabled(replaceEmptyValuesCheck.isSelected());
            }
        });

        replaceValueField.getDocument().addDocumentListener(new DocumentChangeListener() {
            @Override
            protected void update(DocumentEvent e) {
                boolean valid = isValidNumber(replaceValueField.getText());
                boolean completed = !replaceEmptyValuesCheck.isSelected() || valid;
                setComplete(completed);

                if (!valid) {
                    setStatus(MessageStatus.ERROR);
                    setMessage("Invalid replacement for empty values, it should be a real number");
                } else {
                    setMessage(MessageStatus.INFO, "");
                }
            }
        });
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

        applyGroup = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        attributeCb = new javax.swing.JComboBox();
        replaceEmptyValuesCheck = new javax.swing.JCheckBox();
        replaceValueField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        applyToColumnsRb = new javax.swing.JRadioButton();
        applyToRowsRb = new javax.swing.JRadioButton();

        jLabel1.setText("Take values from");

        replaceEmptyValuesCheck.setText("Replace empty values by");

        replaceValueField.setText("0");
        replaceValueField.setEnabled(false);

        jLabel2.setText("Apply to:");

        applyGroup.add(applyToColumnsRb);
        applyToColumnsRb.setSelected(true);
        applyToColumnsRb.setText("Columns");

        applyGroup.add(applyToRowsRb);
        applyToRowsRb.setText("Rows");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(jLabel1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(attributeCb, javax.swing.GroupLayout.PREFERRED_SIZE, 301, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(layout.createSequentialGroup().addComponent(replaceEmptyValuesCheck).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(replaceValueField, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)).addComponent(jLabel2).addComponent(applyToColumnsRb).addComponent(applyToRowsRb)).addContainerGap(100, Short.MAX_VALUE)));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel1).addComponent(attributeCb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(18, 18, 18).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(replaceEmptyValuesCheck).addComponent(replaceValueField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(18, 18, 18).addComponent(jLabel2).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(applyToColumnsRb).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(applyToRowsRb).addContainerGap(116, Short.MAX_VALUE)));
    }// </editor-fold>//GEN-END:initComponents


    @Override
    public JComponent createControls() {
        return this;
    }

    private boolean isValidNumber(String text) {
        try {
            Double.parseDouble(text);
        } catch (NumberFormatException ex) {
            return false;
        }
        return true;
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup applyGroup;
    private javax.swing.JRadioButton applyToColumnsRb;
    private javax.swing.JRadioButton applyToRowsRb;
    private javax.swing.JComboBox attributeCb;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JCheckBox replaceEmptyValuesCheck;
    private javax.swing.JTextField replaceValueField;
    // End of variables declaration//GEN-END:variables

    public int getAttributeIndex() {
        return attributeCb.getSelectedIndex();
    }

    public boolean isReplaceNanValuesEnabled() {
        return replaceEmptyValuesCheck.isSelected();
    }

    public double getReplaceNanValue() {
        return Double.parseDouble(replaceValueField.getText());
    }

    public boolean isTransposeEnabled() {
        return applyToRowsRb.isSelected();
    }
}
