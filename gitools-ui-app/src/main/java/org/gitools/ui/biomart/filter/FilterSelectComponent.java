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
package org.gitools.ui.biomart.filter;

import org.gitools.biomart.restful.model.*;
import org.gitools.ui.biomart.wizard.BiomartFilterConfigurationPage.CollectionsPanelsCache;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FilterSelectComponent extends FilterComponent
{

    // Biomart Configuration Wrappers
    private static class OptionListWrapper
    {

        private Option option;

        public OptionListWrapper(Option filterOption)
        {
            this.option = filterOption;
        }

        public Option getOption()
        {
            return option;
        }

        @Override
        public String toString()
        {

            String res = option.getDisplayName();
            if (res != null)
            {
                res = res.replace(":", "");
            }

            return res;
        }
    }

    //Members of class
    private String component;

    private final Integer COMBO_HEIGHT = 45;

    private final Integer LIST_HEIGHT = 190;

    private HashMap<Option, HashMap<String, List<Option>>> pushActions; // N options, each one with its list of components and options to show


    public FilterSelectComponent(FilterDescription d, FilterDescriptionPanel collectionParent)
    {

        super(d, collectionParent);

        initComponents();

        pushActions = new HashMap<Option, HashMap<String, List<Option>>>();

        buildComponent();

        if (component.equals("ComboBox") && pushActions.size() > 0)
        {
            comboComponent.addItemListener(new ItemListener()
            {
                @Override
                public void itemStateChanged(@NotNull ItemEvent e)
                {
                    if (e.getStateChange() == ItemEvent.SELECTED)
                    {

                        setPushOptionsComponent(((OptionListWrapper) comboComponent.getSelectedItem()).getOption());

                    }

                }
            });
        }
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

        jScrollPane1 = new javax.swing.JScrollPane();
        listComponent = new javax.swing.JList();
        comboComponent = new javax.swing.JComboBox();

        jScrollPane1.setViewportView(listComponent);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE)
                                        .addComponent(comboComponent, javax.swing.GroupLayout.Alignment.TRAILING, 0, 388, Short.MAX_VALUE))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(comboComponent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox comboComponent;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JList listComponent;
    // End of variables declaration//GEN-END:variables

    //FIXME : Test initialisation String list of options
    private void buildComponent()
    {

        List<OptionListWrapper> options = InitListOptions();

        if (filterDescription.getMultipleValues() == 0)
        {

            component = "ComboBox";

            comboComponent.setVisible(true);

            comboComponent.setAlignmentY(TOP_ALIGNMENT);

            jScrollPane1.setVisible(false);

            listComponent.setVisible(false);

            listComponent.setAlignmentY(BOTTOM_ALIGNMENT);

            DefaultComboBoxModel m = new DefaultComboBoxModel();

            for (OptionListWrapper o : options)
                m.addElement(o);

            comboComponent.setModel(m);

            currentHeight = COMBO_HEIGHT;

            //process pushActions
            if (filterDescription != null)
            {
                loadPushActions();
            }


        }
        else
        {

            component = "List";
            listComponent.setVisible(true);
            listComponent.setAlignmentY(TOP_ALIGNMENT);

            DefaultListModel m = new DefaultListModel();
            for (OptionListWrapper o : options)
                m.addElement(o);
            listComponent.setModel(m);

            comboComponent.setVisible(false);
            comboComponent.setAlignmentY(BOTTOM_ALIGNMENT);

            currentHeight = LIST_HEIGHT;

        }
        validate();


    }

    private void setPushOptionsComponent(Option optionSelected)
    {

        //search and set component options

        HashMap<FilterPage, CollectionsPanelsCache> collectionsCache = getDescriptionPanel().getParentCollection().getFilterConfigurationPage().getCollectionsCache();

        FilterDescriptionPanel descriptionPanel = null;

        FilterComponent filterCompo = null;

        //System.out.println("Actions: "+pushActions.keySet());


        for (FilterPage page : collectionsCache.keySet())

            for (FilterGroup group : collectionsCache.get(page).collections.keySet())
            {
                //System.out.println("Group: "+group.getInternalName());

                for (FilterCollectionPanel panel : collectionsCache.get(page).collections.get(group))

                    for (Component componentPanel : panel.getDescriptionsPanel().getComponents())
                    {

                        descriptionPanel = ((FilterDescriptionPanel) componentPanel);

                        for (Component compo : descriptionPanel.getComponents())
                        {

                            filterCompo = (FilterComponent) compo;

                            if (filterCompo.getFilterDescription() != null && filterCompo.getFilterDescription().getInternalName() != null)
                            {
                                //System.out.println ("Component: "+filterCompo.getFilterDescription().getInternalName());

                                if (pushActions.get(optionSelected).get(filterCompo.getFilterDescription().getInternalName()) != null)
                                {
                                    //System.out.println ("options; "+ pushActions.get(optionSelected).get(filterCompo.getFilterDescription().getInternalName()));

                                    filterCompo.setListOptions(pushActions.get(optionSelected).get(filterCompo.getFilterDescription().getInternalName()));

                                }
                            }
                        }
                    }
            }
    }


    @NotNull
    private List<OptionListWrapper> InitListOptions()
    {

        List<OptionListWrapper> res = new ArrayList<OptionListWrapper>();

        for (Option o : filterDescription.getOptions())
        {
            OptionListWrapper wrapper = new OptionListWrapper(o);
            res.add(wrapper);
        }

        // Load default options from some component with push action
        if (parentPanel != null && parentPanel.getParentCollection().getFilterConfigurationPage().getDefaultSelecComposData().size() > 0)
        {
            if (parentPanel.getParentCollection().getFilterConfigurationPage().
                    getDefaultSelecComposData().get(filterDescription.getInternalName()) != null)

            {
                for (Option o : parentPanel.getParentCollection().getFilterConfigurationPage().
                        getDefaultSelecComposData().get(filterDescription.getInternalName()))
                {

                    OptionListWrapper wrapper = new OptionListWrapper(o);

                    res.add(wrapper);
                }
            }
        }
        return res;
    }

    /**
     * Load PushActions of component
     */
    private void loadPushActions()
    {

        for (Option o : filterDescription.getOptions())
        {
            if (o.getPushactions() != null)
            {
                HashMap<String, List<Option>> actions = new HashMap<String, List<Option>>();

                for (PushAction action : o.getPushactions())
                {

                    actions.put(action.getRef(), action.getOptions());
                }
                pushActions.put(o, actions);
            }

        }

    }

    @NotNull
    @Override
    // FIXME : get Filter for selected value/s in list
    public List<Filter> getFilters()
    {

        List<Filter> filters = new ArrayList<Filter>();

        Filter f = null;

        if (hasChild())
        {

            if (component.equals("ComboBox"))
            {
                f = new Filter();

                f.setName(((OptionListWrapper) comboComponent.getSelectedItem()).getOption().getInternalName());

                f.setValue(getChildComponent().getFilters().get(0).getValue());

                f.setRadio(getChildComponent().getFilters().get(0).getRadio());

                filters.add(f);
            }
            else
            {
                for (Object optionWrapper : listComponent.getSelectedValues())
                {
                    f = new Filter();

                    f.setName(((OptionListWrapper) optionWrapper).getOption().getInternalName());

                    f.setValue(getChildComponent().getFilters().get(0).getValue());

                    f.setRadio(getChildComponent().getFilters().get(0).getRadio());

                    filters.add(f);
                }
            }

        }
        else
        {

            f = new Filter();

            f.setName(filterDescription.getInternalName());

            if (component.equals("ComboBox"))
            {
                if (comboComponent.getSelectedItem() != null)
                {
                    f.setValue(comboComponent.getSelectedItem().toString());
                }
            }
            else
            {
                if (listComponent.getSelectedValues() != null && listComponent.getSelectedValues().length > 0)
                {
                    StringBuilder sb = new StringBuilder();
                    Object[] list = listComponent.getSelectedValues();
                    if (list.length > 0)
                    {
                        sb.append(((OptionListWrapper) list[0]).getOption().getValue());
                        for (int i = 1; i < list.length; i++)
                            sb.append(", ").append(((OptionListWrapper) list[i]).getOption().getValue());
                    }

                    f.setValue(sb.toString());
                }
            }
            filters.add(f);
        }
        return filters;
    }

    @NotNull
    @Override
    //Always render filter from select component filter
    public Boolean hasChanged()
    {
        return true;
    }

    @Override
    public void setListOptions(@NotNull List<Option> optionList)
    {

        if (this.component.equals("ComboBox"))
        {

            DefaultComboBoxModel m = new DefaultComboBoxModel();

            for (Option o : optionList)
                m.addElement(new OptionListWrapper(o));

            comboComponent.setModel(m);

        }
        else
        {

            DefaultListModel m = new DefaultListModel();

            for (Option o : optionList)
                m.addElement(new OptionListWrapper(o));

            listComponent.setModel(m);

        }
    }

    public HashMap<Option, HashMap<String, List<Option>>> getPushActions()
    {

        return pushActions;
    }

    public HashMap<String, List<Option>> getPushActionData_defaultOption()
    {

        if (pushActions.size() > 0)

        {
            if (component.equals("ComboBox"))

            {
                return pushActions.get(((OptionListWrapper) comboComponent.getSelectedItem()).getOption());
            }

            else

            {
                return pushActions.get(((OptionListWrapper) listComponent.getSelectedValue()).getOption());
            }
        }

        else

        {
            return new HashMap<String, List<Option>>();
        }
    }

}
