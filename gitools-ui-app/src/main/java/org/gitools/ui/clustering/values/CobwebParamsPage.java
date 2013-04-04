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
package org.gitools.ui.clustering.values;

import org.gitools.clustering.ClusteringMethodDescriptor;
import org.gitools.clustering.ClusteringMethodFactory;
import org.gitools.clustering.method.value.AbstractClusteringValueMethod;
import org.gitools.clustering.method.value.WekaCobWebMethod;
import org.gitools.ui.platform.wizard.AbstractWizardPage;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CobwebParamsPage extends AbstractWizardPage implements ClusteringValueMethodPage
{

    public CobwebParamsPage()
    {

        initComponents();

        setTitle("Clustering method selection");
        setComplete(true);
    }

    private boolean validated()
    {
        return
                (isValidNumber(cutOffField.getText()) && isValidInteger(seedField.getText()) &&
                        isValidNumber(acuityField.getText()));
    }

    @Override
    public void updateModel()
    {
        super.updateModel();

    }

    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        optGroup = new javax.swing.ButtonGroup();
        acuityField = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        cutOffField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        seedField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();

        acuityField.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        acuityField.setText("0.5");
        acuityField.setToolTipText("Set the minimum standard deviation of a node of the tree with an only \ninstance.");

        jLabel1.setText("Acuity: ");
        jLabel1.setToolTipText("Set the minimum standard deviation of a node of the tree with an only \ninstance.\n");

        jLabel4.setFont(new java.awt.Font("DejaVu LGC Sans", 0, 10));
        jLabel4.setText("(Hint: decrease this value to obtain more clusters)");

        cutOffField.setText("0.0028");
        cutOffField.setToolTipText("Set the category utility threshold by which to prune nodes.");

        jLabel2.setText("Cutoff: ");
        jLabel2.setToolTipText("Set the category utility threshold by which to prune nodes.");

        jLabel5.setFont(new java.awt.Font("DejaVu LGC Sans", 0, 10));
        jLabel5.setText("(Hint: decrease this value to obtain more clusters)");

        seedField.setText("42");
        seedField.setToolTipText("Set a random value used to start clustering method");

        jLabel3.setText("Seed: ");
        jLabel3.setToolTipText("Set a random value used to start clustering method");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel1)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel4)
                                                        .addComponent(acuityField, javax.swing.GroupLayout.DEFAULT_SIZE, 378, Short.MAX_VALUE)))
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel2)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel5)
                                                        .addComponent(cutOffField, javax.swing.GroupLayout.DEFAULT_SIZE, 378, Short.MAX_VALUE)))
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel3)
                                                .addGap(17, 17, 17)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(seedField, javax.swing.GroupLayout.DEFAULT_SIZE, 378, Short.MAX_VALUE))))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                                        .addComponent(acuityField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel1))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(cutOffField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel2))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(seedField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel3))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField acuityField;
    private javax.swing.JTextField cutOffField;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.ButtonGroup optGroup;
    private javax.swing.JTextField seedField;
    // End of variables declaration//GEN-END:variables

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

    private boolean isValidInteger(String text)
    {
        try
        {
            Integer.parseInt(text);
        } catch (NumberFormatException ex)
        {
            return false;
        }
        return true;
    }

    @Nullable
    @Override
    public AbstractClusteringValueMethod getMethod()
    {

        WekaCobWebMethod cobweb = null;

        if (validated())
        {
            cobweb = (WekaCobWebMethod) ClusteringMethodFactory.getDefault().create(getMethodDescriptor());

            cobweb.setCutoff(new Float(cutOffField.getText()));
            cobweb.setSeed(new Integer(seedField.getText()));
            cobweb.setAcuity(new Float(acuityField.getText()));
        }

        return cobweb;
    }

    @Nullable
    @Override
    public ClusteringMethodDescriptor getMethodDescriptor()
    {

        List<ClusteringMethodDescriptor> descriptors = ClusteringMethodFactory.getDefault().getDescriptors();

        for (ClusteringMethodDescriptor desc : descriptors)
            if (desc.getMethodClass().equals(WekaCobWebMethod.class))
            {
                return desc;
            }

        return null;
    }
}
