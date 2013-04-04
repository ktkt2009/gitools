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
package org.gitools.ui.analysis.groupcomparison.wizard;

import org.gitools.matrix.model.element.IElementAttribute;
import org.gitools.ui.IconNames;
import org.gitools.ui.platform.IconUtils;
import org.gitools.ui.platform.dialog.MessageStatus;
import org.gitools.ui.platform.wizard.AbstractWizardPage;
import org.gitools.ui.utils.DocumentChangeListener;
import org.gitools.utils.cutoffcmp.CutoffCmp;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import java.util.List;

public class GroupComparisonGroupingByValuePage extends AbstractWizardPage
{

    private static final long serialVersionUID = 3840797252370672587L;

    /**
     * Creates new form DataSourcePanel
     */
    public GroupComparisonGroupingByValuePage()
    {
        setTitle("Select data filtering options");

        setLogo(IconUtils.getImageIconResourceScaledByHeight(IconNames.LOGO_DATA, 96));

        initComponents();

        DocumentChangeListener docCompleteListener = new DocumentChangeListener()
        {
            @Override
            protected void update(DocumentEvent e)
            {
                updateState(e);
            }
        };

        String[] cmpNames = new String[CutoffCmp.comparators.length];
        for (int i = 0; i < cmpNames.length; i++)
            cmpNames[i] = CutoffCmp.comparators[i].getLongName();
        cutoffCmpGroup1Cb.setModel(new DefaultComboBoxModel(cmpNames));
        cutoffCmpGroup1Cb.setSelectedItem(CutoffCmp.GE.getLongName());
        cutoffCmpGroup2Cb.setModel(new DefaultComboBoxModel(cmpNames));
        cutoffCmpGroup2Cb.setSelectedItem(CutoffCmp.LT.getLongName());

        cutoffValueGroup1.getDocument().addDocumentListener(docCompleteListener);
        cutoffValueGroup1.setText("1.5");
        cutoffValueGroup2.getDocument().addDocumentListener(docCompleteListener);
        cutoffValueGroup2.setText("1.5");

    }

    public class AttrOption
    {
        private String name;
        private IElementAttribute attr;

        public AttrOption(String name)
        {
            this.name = name;
        }

        public AttrOption(IElementAttribute attr)
        {
            this.attr = attr;
        }

        public IElementAttribute getAttr()
        {
            return attr;
        }

        @Override
        public String toString()
        {
            return attr != null ? attr.getName() : name;
        }
    }

    private boolean isValidNumber(String text)
    {
        try
        {
            Double.parseDouble(text);
        } catch (NumberFormatException ex)
        {
            return false;
        }
        return true;
    }

    private void updateState(DocumentEvent e)
    {
        boolean complete = false;

        complete = (isValidNumber(cutoffValueGroup1.getText()) &&
                isValidNumber(cutoffValueGroup2.getText()));

        if (complete)
        {
            setMessage(MessageStatus.INFO, "Define the two groups by cutoff-values");
        }
        else
        {
            setMessage(MessageStatus.WARN, "Put valid cutoff-values");
        }
        setComplete(complete);
    }

    public int getCutoffAttributeIndex()
    {
        int index = cutoffAttributeSelect.getSelectedIndex();
        return index;
    }

    public String getCutoffAttributeString()
    {
        return cutoffAttributeSelect.getSelectedItem().toString();
    }

    @NotNull
    @Override
    public JComponent createControls()
    {
        return this;
    }

    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        cutoffCmpGroup1Cb = new javax.swing.JComboBox();
        cutoffValueGroup1 = new javax.swing.JTextField();
        attributeLabel3 = new javax.swing.JLabel();
        attributeLabel = new javax.swing.JLabel();
        cutoffAttributeSelect = new javax.swing.JComboBox();
        attributeLabel4 = new javax.swing.JLabel();
        cutoffCmpGroup2Cb = new javax.swing.JComboBox();
        cutoffValueGroup2 = new javax.swing.JTextField();
        attributeLabel1 = new javax.swing.JLabel();
        attributeLabel2 = new javax.swing.JLabel();

        cutoffValueGroup1.setColumns(6);

        attributeLabel3.setText("Group 1:");

        attributeLabel.setText("Take values to group from:");

        attributeLabel4.setText("Group 2:");

        cutoffValueGroup2.setColumns(6);

        attributeLabel1.setText("Values that are");

        attributeLabel2.setText("Values that are");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(attributeLabel)
                                                .addGap(54, 54, 54)
                                                .addComponent(cutoffAttributeSelect, 0, 424, Short.MAX_VALUE))
                                        .addComponent(attributeLabel3)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addGroup(layout.createSequentialGroup()
                                                        .addComponent(attributeLabel4)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 602, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGroup(layout.createSequentialGroup()
                                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                .addComponent(attributeLabel1)
                                                                .addComponent(attributeLabel2))
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 76, Short.MAX_VALUE)
                                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                                        .addComponent(cutoffCmpGroup1Cb, javax.swing.GroupLayout.PREFERRED_SIZE, 390, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                        .addComponent(cutoffValueGroup1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                                        .addComponent(cutoffCmpGroup2Cb, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                        .addComponent(cutoffValueGroup2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(attributeLabel)
                                        .addComponent(cutoffAttributeSelect, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(attributeLabel3)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(cutoffCmpGroup1Cb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(cutoffValueGroup1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(attributeLabel2))
                                .addGap(18, 18, 18)
                                .addComponent(attributeLabel4)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(cutoffCmpGroup2Cb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(cutoffValueGroup2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(attributeLabel1))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel attributeLabel;
    private javax.swing.JLabel attributeLabel1;
    private javax.swing.JLabel attributeLabel2;
    private javax.swing.JLabel attributeLabel3;
    private javax.swing.JLabel attributeLabel4;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JComboBox cutoffAttributeSelect;
    private javax.swing.JComboBox cutoffCmpGroup1Cb;
    private javax.swing.JComboBox cutoffCmpGroup2Cb;
    private javax.swing.JTextField cutoffValueGroup1;
    private javax.swing.JTextField cutoffValueGroup2;
    // End of variables declaration//GEN-END:variables


    @NotNull
    public CutoffCmp[] getGroupCutoffCmps()
    {
        return new CutoffCmp[]{
                CutoffCmp.getFromName((String) cutoffCmpGroup1Cb.getSelectedItem()),
                CutoffCmp.getFromName((String) cutoffCmpGroup2Cb.getSelectedItem())
        };
    }

	/*public void setBinaryCutoffCmp(CutoffCmp cmp) {
        cutoffCmpGroup1Cb.setSelectedItem(cmp.getLongName());
	}*/

    @NotNull
    public double[] getGroupCutoffValues()
    {
        return new double[]{
                Double.parseDouble(cutoffValueGroup1.getText()),
                Double.parseDouble(cutoffValueGroup2.getText())
        };
    }

    public void setAttributes(@Nullable List<IElementAttribute> attrs)
    {

        if (attrs != null)
        {
            AttrOption[] attrOptions = new AttrOption[attrs.size()];

            for (int i = 0; i < attrs.size(); i++)
                attrOptions[i] = new AttrOption(attrs.get(i));

            cutoffAttributeSelect.setModel(new DefaultComboBoxModel(attrOptions));
            cutoffAttributeSelect.setSelectedIndex(0);
            cutoffAttributeSelect.setEnabled(true);
            cutoffAttributeSelect.setVisible(true);
            attributeLabel.setVisible(true);
        }
        else
        {
            dissableAttrCb();
        }
    }

    private void dissableAttrCb()
    {
        cutoffAttributeSelect.setModel(new DefaultComboBoxModel());
        cutoffAttributeSelect.setEnabled(false);
        cutoffAttributeSelect.setVisible(false);
        attributeLabel.setVisible(false);
    }

	/*public void setBinaryCutoffValue(double value) {
        cutoffValueGroup1.setText(Double.toString(value));
	}*/

}
