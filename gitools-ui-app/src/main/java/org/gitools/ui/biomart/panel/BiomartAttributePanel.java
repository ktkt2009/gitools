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
package org.gitools.ui.biomart.panel;

import org.gitools.biomart.BiomartService;
import org.gitools.biomart.BiomartServiceFactory;
import org.gitools.biomart.restful.model.AttributeDescription;
import org.gitools.biomart.restful.model.AttributePage;
import org.gitools.biomart.restful.model.DatasetInfo;
import org.gitools.biomart.restful.model.MartLocation;
import org.gitools.biomart.settings.BiomartSource;
import org.gitools.biomart.settings.BiomartSourceManager;
import org.gitools.ui.biomart.panel.AttributesTreeModel.AttributeWrapper;
import org.gitools.ui.wizard.common.FilteredTreePanel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BiomartAttributePanel extends FilteredTreePanel
{

    @Nullable
    private BiomartService port;
    @Nullable
    private MartLocation mart;
    @Nullable
    private DatasetInfo dataset;

    @Nullable
    private List<AttributePage> attrPages;

    private List<AttributeDescription> selectedAttr;
    private List<String> selectedAttrNames;
    private Set<String> selectedAttrNamesSet;

    public static interface AttributeSelectionListener
    {
        void selectionChanged();
    }

    @NotNull
    private List<AttributeSelectionListener> attributeSelectionListeners =
            new ArrayList<AttributeSelectionListener>();

    public BiomartAttributePanel()
    {
        super();

        this.port = null;
        this.mart = null;
        this.dataset = null;
        this.attrPages = null;

        this.selectedAttr = new ArrayList<AttributeDescription>();
        this.selectedAttrNames = new ArrayList<String>();
        this.selectedAttrNamesSet = new HashSet<String>();

        tree.setRootVisible(false);
        tree.addTreeSelectionListener(new TreeSelectionListener()
        {
            @Override
            public void valueChanged(@NotNull TreeSelectionEvent e)
            {
                selectionChanged(e);
            }
        });
    }

    /*
        public void setBiomartParameters(
                MartServiceSoap port,
                MartLocation mart,
                DatasetInfo dataset) {

            this.port = port;
            this.mart = mart;
            this.dataset = dataset;

            loadAttributePages();
        }
        */
    public void setBiomartParameters(
            BiomartService port,
            MartLocation mart,
            DatasetInfo dataset)
    {

        this.port = port;
        this.mart = mart;
        this.dataset = dataset;

        loadAttributePages();
    }


    private void loadAttributePages()
    {
        setControlsEnabled(false);

        TreeNode node = new DefaultMutableTreeNode("Loading...");
        TreeModel model = new DefaultTreeModel(node);
        setModel(model);

        //TODO fire loading starts

        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                loadingThread();
            }
        }).start();
    }

    public List<AttributeDescription> getSelectedAttributes()
    {
        return selectedAttr;
    }

    public List<String> getSelectedAttributeNames()
    {
        return selectedAttrNames;
    }

    //FIXME
    private void loadingThread()
    {
        try
        {

            List<BiomartSource> lBs = BiomartSourceManager.getDefault().getSources();
            BiomartSource bsrc = lBs.get(0);
            BiomartService service = BiomartServiceFactory.createService(bsrc);

            final List<AttributePage> pages = service.getAttributes(mart, dataset);
            //BiomartCentralPortalSoapService.getDefault().getAttributes(mart, dataset);

            SwingUtilities.invokeAndWait(new Runnable()
            {
                @Override
                public void run()
                {
                    setControlsEnabled(true);

                    setAttributePages(pages);

                    //TODO fire loading ends
                }
            });
        } catch (Exception e)
        {
            //TODO fire loading exception
            e.printStackTrace();
        }
    }

    private void selectionChanged(@NotNull TreeSelectionEvent e)
    {
        TreePath[] paths = e.getPaths();
        if (paths == null)
        {
            return;
        }

        StringBuilder sb = new StringBuilder();

        for (TreePath sel : paths)
        {
            DefaultMutableTreeNode node =
                    (DefaultMutableTreeNode) sel.getLastPathComponent();
            AttributeWrapper attrw =
                    (AttributeWrapper) node.getUserObject();
            AttributeDescription attribute =
                    attrw.getType() == AttributeWrapper.NodeType.ATTRIBUTE
                            ? (AttributeDescription) attrw.getObject() : null;

            if (e.isAddedPath(sel))
            {
                if (attribute != null)
                {
                    if (!selectedAttrNamesSet.contains(attribute.getInternalName()))
                    { // xrp: check if internal name instead
                        sb.setLength(0);

                        Object[] opath = sel.getPath();
                        if (opath.length > 1)
                        {
                            sb.append(opath[1].toString());
                            for (int i = 2; i < opath.length; i++)
                                sb.append(" > ").append(opath[i].toString());
                        }

                        selectedAttr.add(attribute);
                        selectedAttrNames.add(sb.toString());
                        selectedAttrNamesSet.add(attribute.getInternalName()); // xrp: check if internal name instead

                        for (AttributeSelectionListener l : attributeSelectionListeners)
                            l.selectionChanged();
                    }
                }
                else
                {
                    tree.getSelectionModel().removeSelectionPath(sel);
                }
            }
            else if (attribute != null)
            {
                int i = selectedAttr.indexOf(attribute);
                selectedAttr.remove(i);
                selectedAttrNames.remove(i);
                selectedAttrNamesSet.remove(attribute.getInternalName());// xrp: check if internal name instead

                for (AttributeSelectionListener l : attributeSelectionListeners)
                    l.selectionChanged();
            }
        }
    }

    public void addAttributeSelectionListener(AttributeSelectionListener listener)
    {
        attributeSelectionListeners.add(listener);
    }

    public void removeAttributeSelectionListener(AttributeSelectionListener listener)
    {
        attributeSelectionListeners.remove(listener);
    }

    private void setControlsEnabled(boolean enabled)
    {
        filterField.setEnabled(enabled);
        //expandBtn.setEnabled(enabled);
        //collapseBtn.setEnabled(enabled);
    }

    @Nullable
    @Override
    protected TreeModel updateModel(String filterText)
    {
        if (attrPages == null)
        {
            return null;
        }

        return new AttributesTreeModel(attrPages, filterText);
    }

    public synchronized void setAttributePages(@Nullable List<AttributePage> attrPages)
    {
        this.attrPages = attrPages;

        final AttributesTreeModel model = attrPages == null ? null :
                new AttributesTreeModel(attrPages);

        setModel(model);
        expandAll();
    }

    @Nullable
    public synchronized List<AttributePage> getAttributePages()
    {
        return attrPages;
    }
}
