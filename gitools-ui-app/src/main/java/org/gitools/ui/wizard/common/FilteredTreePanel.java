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
package org.gitools.ui.wizard.common;

import org.gitools.ui.utils.DocumentChangeListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;

public abstract class FilteredTreePanel extends javax.swing.JPanel
{

    private String lastFilterText = "";

    /**
     * Creates new form FilteredTreePanel
     */
    public FilteredTreePanel()
    {
        initComponents();

        filterField.getDocument().addDocumentListener(new DocumentChangeListener()
        {
            @Override
            protected void update(DocumentEvent e)
            {
                updateFilter();
            }
        });

        clearBtn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                filterField.setText("");
                filterField.requestFocusInWindow();
            }
        });

        expandBtn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                expandAll();
            }
        });

        collapseBtn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                collapseAll();
            }
        });

        updateFilter();
    }

    private void updateFilter()
    {
        final String filterText = getFilterText();

        clearBtn.setEnabled(!filterText.isEmpty());

        if (!filterText.equalsIgnoreCase(lastFilterText))
        {
            lastFilterText = filterText;
            tree.setModel(updateModel(getFilterText()));
            expandAll();
        }
    }

    @Nullable
    protected abstract TreeModel updateModel(String filterText);

    private String getFilterText()
    {
        return filterField.getText();
    }

    public TreeModel getModel()
    {
        return tree.getModel();
    }

    public void setModel(TreeModel model)
    {
        tree.setModel(model);

        updateExpandCollapseButtons();
    }

    private void updateExpandCollapseButtons()
    {
        boolean enabled = false;

        TreeModel model = tree.getModel();
        if (model != null)
        {
            enabled = model.getRoot() != null;
        }

        expandBtn.setEnabled(enabled);
        collapseBtn.setEnabled(enabled);
    }

    // If expand is true, expands all nodes in the tree.
    // Otherwise, collapses all nodes in the tree.
    public void expandCollapse(@NotNull JTree tree, boolean expand)
    {
        TreeModel model = tree.getModel();
        if (model == null)
        {
            return;
        }

        TreeNode root = (TreeNode) model.getRoot();

        // Traverse tree from root
        expandCollapse(tree, new TreePath(root), expand);

        // Do not collapse first level nodes
        if (!tree.isRootVisible() && !expand)
        {
            tree.expandPath(new TreePath(root));
        }
    }

    private void expandCollapse(@NotNull JTree tree, @NotNull TreePath parent, boolean expand)
    {
        // Traverse children
        TreeNode node = (TreeNode) parent.getLastPathComponent();
        if (node.getChildCount() >= 0)
        {
            for (Enumeration<?> e = node.children(); e.hasMoreElements(); )
            {
                TreeNode n = (TreeNode) e.nextElement();
                TreePath path = parent.pathByAddingChild(n);
                expandCollapse(tree, path, expand);
            }
        }

        // Expansion or collapse must be done bottom-up
        if (expand)
        {
            tree.expandPath(parent);
        }
        else
        {
            tree.collapsePath(parent);
        }
    }

    public void expandAll()
    {
        expandCollapse(tree, true);
        filterField.requestFocusInWindow();
    }

    public void collapseAll()
    {
        expandCollapse(tree, false);
        filterField.requestFocusInWindow();
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

        filterField = new javax.swing.JTextField();
        clearBtn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tree = new javax.swing.JTree();
        collapseBtn = new javax.swing.JButton();
        expandBtn = new javax.swing.JButton();

        filterField.setFocusCycleRoot(true);

        clearBtn.setText("Clear");

        tree.setModel(null);
        jScrollPane1.setViewportView(tree);

        collapseBtn.setText("Collapse all");

        expandBtn.setText("Expand all");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 378, Short.MAX_VALUE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(filterField, javax.swing.GroupLayout.DEFAULT_SIZE, 327, Short.MAX_VALUE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(clearBtn))
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(expandBtn)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(collapseBtn)))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(clearBtn)
                                        .addComponent(filterField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 159, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(collapseBtn)
                                        .addComponent(expandBtn))
                                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton clearBtn;
    public javax.swing.JButton collapseBtn;
    public javax.swing.JButton expandBtn;
    public javax.swing.JTextField filterField;
    private javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JTree tree;
    // End of variables declaration//GEN-END:variables

}
