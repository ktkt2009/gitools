/*
 *  Copyright 2010 xavi.
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

package org.gitools.ui.biomart.wizard;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import org.gitools.biomart.restful.BiomartRestfulService;
import org.gitools.biomart.restful.model.DatasetConfig;
import org.gitools.biomart.restful.model.FilterGroup;
import org.gitools.biomart.restful.model.FilterPage;
import org.gitools.biomart.restful.model.DatasetInfo;
import org.gitools.biomart.restful.model.Filter;
import org.gitools.biomart.restful.model.FilterCollection;
import org.gitools.ui.biomart.filter.FilterCollectionPanel;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.dialog.ExceptionDialog;
import org.gitools.ui.platform.dialog.MessageStatus;
import org.gitools.ui.platform.wizard.AbstractWizardPage;

public class BiomartFilterConfigurationPage extends AbstractWizardPage {


	// Biomart Configuration Wrappers
	private static class PageListWrapper {

		private FilterPage page;

		public PageListWrapper(FilterPage dataset) {
			this.page = dataset;
		}

		public FilterPage getFilterPage() {
			return page;
		}

		@Override
		public String toString() {

			String res = page.getDisplayName();
			if (res != null) {
				res = res.replace(":", "");
			}

			return res;
		}
	}

	private static class GroupListWrapper {

		private FilterGroup group;

		public GroupListWrapper(FilterGroup dataset) {
			this.group = dataset;
		}

		public FilterGroup getFilterGroup() {
			return group;
		}

		@Override
		public String toString() {

			String res = group.getDisplayName();
			if (res != null) {
				res = res.replace(":", "");
			}

			return res;
		}
	}
	private final Integer FILTER_PANEL_WEIGHT = 333;
	private final Integer FILTER_PANEL_HEIGHT = 330;

	private DatasetInfo dataset;
	private DatasetConfig dsConfiguration;
	private BiomartRestfulService biomartService;
	private FilterGroup lastGroupSelected;
	private HashMap<String,Filter> filters;

	private HashMap<FilterPage,CollectionsPanelsCache> collectionsCache; // Attribute to retain user panel selections
	private Boolean updateFilterData; //When to update panel data

	public static class CollectionsPanelsCache{
		
		public HashMap<FilterGroup,List<FilterCollectionPanel>> collections = new HashMap<FilterGroup, List<FilterCollectionPanel>>();

	}

	/*
	 * Default class constructor.
	 * Member attributes and graphic components are initialised
	 *
	 */
	public BiomartFilterConfigurationPage() {

		initComponents();
		
		collectionsCache = null;
		lastGroupSelected = null;
		updateFilterData = true;

		filters = new HashMap<String,Filter>();
		collectionsCache = new HashMap<FilterPage, CollectionsPanelsCache>();
		
		setComplete(true); //Next button always is true, input filters is not mandatory

	}

	@Override
	public JComponent createControls() {
		return this;
	}

	/**
	 * Method called each time this class is shown in the application
	 * All components from collections panel are cleaned, to avoid wrong
	 * component visualisations
	 *
	 */
	@Override
	public void updateControls() {


		//Clean main collections panel
		collectionsPanel.removeAll();
		collectionsPanel.setPreferredSize(new Dimension(FILTER_PANEL_WEIGHT,FILTER_PANEL_HEIGHT));
		lastGroupSelected = null;
		
		if (updateFilterData) {
			setMessage(MessageStatus.PROGRESS, "Retrieving available filters ...");
			new Thread(new Runnable() {
				@Override public void run() {
					try {
						dsConfiguration = biomartService.getConfiguration(dataset);
						setMessage(MessageStatus.INFO, "");
						updatePageFilterList();
						initCollectionsCache();
					}
					catch (final Exception ex) {
						SwingUtilities.invokeLater(new Runnable() {
							@Override public void run() {
								setStatus(MessageStatus.ERROR);
								setMessage(ex.getMessage());
							}
						});
						ExceptionDialog dlg = new ExceptionDialog(AppFrame.instance(), ex);
						dlg.setVisible(true);
					}
				}

			}).start();
		}
	}

	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        filterPageCombo = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        filterGroupList = new javax.swing.JList();
        scrollPanel = new javax.swing.JScrollPane();
        collectionsPanel = new javax.swing.JPanel();

        setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        filterPageCombo.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                filterPageComboPropertyChange(evt);
            }
        });

        jLabel1.setText("Page");

        filterGroupList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        filterGroupList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                filterGroupListValueChanged(evt);
            }
        });

        scrollPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        scrollPanel.setViewportBorder(null);

        collectionsPanel.setBorder(null);

        javax.swing.GroupLayout collectionsPanelLayout = new javax.swing.GroupLayout(collectionsPanel);
        collectionsPanel.setLayout(collectionsPanelLayout);
        collectionsPanelLayout.setHorizontalGroup(
            collectionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 333, Short.MAX_VALUE)
        );
        collectionsPanelLayout.setVerticalGroup(
            collectionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 362, Short.MAX_VALUE)
        );

        scrollPanel.setViewportView(collectionsPanel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(filterGroupList, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(scrollPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 333, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(filterPageCombo, 0, 494, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(filterPageCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrollPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 362, Short.MAX_VALUE)
                    .addComponent(filterGroupList, javax.swing.GroupLayout.DEFAULT_SIZE, 362, Short.MAX_VALUE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

	private void filterGroupListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_filterGroupListValueChanged

		updateCollectionControls();
		
	}//GEN-LAST:event_filterGroupListValueChanged

	private void filterPageComboPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_filterPageComboPropertyChange

		updateGroupFilterList();

	}//GEN-LAST:event_filterPageComboPropertyChange
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel collectionsPanel;
    private javax.swing.JList filterGroupList;
    private javax.swing.JComboBox filterPageCombo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane scrollPanel;
    // End of variables declaration//GEN-END:variables

	public void updatePageFilterList() {

		DefaultComboBoxModel model = new DefaultComboBoxModel();

		for (FilterPage p : dsConfiguration.getFilterPages()) {
			if (p.getHideDisplay() == null || !p.getHideDisplay().equals("true")) {
				model.addElement(new PageListWrapper(p));

			}
		}

		this.filterPageCombo.setModel(model);

		updateGroupFilterList();

	}

	private void updateGroupFilterList() {

		//Clean main collections panel
		collectionsPanel.removeAll();
		collectionsPanel.setPreferredSize(new Dimension(FILTER_PANEL_WEIGHT,FILTER_PANEL_HEIGHT));
		lastGroupSelected = null;

		DefaultListModel model = new DefaultListModel();

		if (this.filterPageCombo.getSelectedItem() != null) {
			FilterPage indexPage = ((PageListWrapper) this.filterPageCombo.getSelectedItem()).getFilterPage();


			for (FilterGroup g : indexPage.getFilterGroups()) {
				if (g.getHideDisplay() == null || !g.getHideDisplay().equals("true")) {
					model.addElement(new GroupListWrapper(g));
				}
			}

			this.filterGroupList.setModel(model);

		}

	}

	/**
	 * Initialisation of user collections panels shown
	 */
	private void initCollectionsCache() {

		if (collectionsCache == null) collectionsCache = new HashMap<FilterPage, CollectionsPanelsCache>();
		else
		collectionsCache.clear();

		for (int i = 0; i< this.filterPageCombo.getModel().getSize(); i ++){

			FilterPage page = ((PageListWrapper) filterPageCombo.getModel().getElementAt(i)).getFilterPage();
			
			collectionsCache.put(page, null);

			CollectionsPanelsCache panels = new CollectionsPanelsCache();

			for (int j = 0; j< this.filterGroupList.getModel().getSize();j++)
				panels.collections.put(((GroupListWrapper) filterGroupList.getModel().getElementAt(j)).
						getFilterGroup(), new ArrayList(0));

			collectionsCache.put(page, panels);
		}
	}

	private void updateCollectionControls() {

		// Avoid update process when null value or unaltered filter group selection
		if (filterGroupList.getSelectedValue() == null ||
				(lastGroupSelected != null &&
				lastGroupSelected.getInternalName().equals(((GroupListWrapper) filterGroupList.getSelectedValue()).getFilterGroup().getInternalName()))
				)
			return;

		lastGroupSelected = ((GroupListWrapper) filterGroupList.getSelectedValue()).getFilterGroup();

		Integer collectionPanelHeight = 0;
		FilterCollectionPanel collectionPanel = null;

		collectionsPanel.removeAll();
		collectionsPanel.repaint();
		collectionsPanel.setLayout(new BoxLayout(collectionsPanel, BoxLayout.Y_AXIS));

		//Check if collections have been showed previously (cache)
		if (collectionsCache.get(((PageListWrapper) filterPageCombo.getModel().getSelectedItem()).getFilterPage()).
				collections.get(lastGroupSelected).size()>0 )
		{
			for (FilterCollectionPanel col : collectionsCache.get(((PageListWrapper) filterPageCombo.getModel().getSelectedItem()).getFilterPage()).
				collections.get(lastGroupSelected))
			{
				collectionsPanel.add(col);
				collectionPanelHeight += col.getCurrentHeigh();
			}
		}
		else //first time collections are shown
		{
			List<FilterCollectionPanel> listCollections = new ArrayList<FilterCollectionPanel>();
			for (FilterCollection collection : lastGroupSelected.getFilterCollections()) {

				collectionPanel = new FilterCollectionPanel(collection,this);
				if (collectionPanel.isPanelRendered())
				{
					collectionsPanel.add(collectionPanel);
					collectionPanelHeight += collectionPanel.getCurrentHeigh();
					//add collectionPanel in list
					listCollections.add(collectionPanel);
				}

			}
			//add collectionPanel list in cache
			collectionsCache.get(((PageListWrapper) filterPageCombo.getModel().getSelectedItem()).
			getFilterPage()).collections.put(lastGroupSelected, listCollections);
		}

		Dimension d = new Dimension(collectionsPanel.getWidth(),collectionPanelHeight);
		collectionsPanel.setPreferredSize(d);

		collectionsPanel.repaint();
		scrollPanel.validate();

		validate();

	}

	/**
	 * Loop through all groups and their collections.
	 * If checkBox collection checked the filter is annotated
	 * @return
	*/
	public Collection<Filter> getFilters() {

		List<Filter> listFilters = new ArrayList<Filter>();

		for (FilterPage page :collectionsCache.keySet())
			for(FilterGroup group : collectionsCache.get(page).collections.keySet())
				for (FilterCollectionPanel panel : collectionsCache.get(page).collections.get(group))
						listFilters.addAll(panel.getFilters());

		return listFilters;
	}

	public void setFilter(String name, Filter f) {
		filters.put(name,f);
	}

	public void setFilters(HashMap<String,Filter> filters) {
		filters.putAll(filters);
	}

	public void deleteFilter(String name) {
			filters.remove(name);
	}

	public void deleteFilters(HashMap<String,Filter> delFilters) {

		for (String name : delFilters.keySet())
			filters.remove(name);

	}
	public void setSource(BiomartRestfulService service, DatasetInfo dataset) {

		if (this.dataset != null && this.dataset.getName().equals(dataset.getName()))
			updateFilterData = false;
		else
			updateFilterData = true;

		this.dataset = dataset;
		this.biomartService = service;
		
	}

	public BiomartRestfulService getBiomartService(){
		return this.biomartService;
	}

	public HashMap<FilterPage, CollectionsPanelsCache> getCollectionsCache() {
		return collectionsCache;
	}

	public void setCollectionsCache(HashMap<FilterPage, CollectionsPanelsCache> collectionsCache) {
		this.collectionsCache = collectionsCache;
	}
	
	/*
	public void resetCollectionCache() {

		if (collectionsCache == null) return;

		for (FilterPage p : collectionsCache.keySet())
		{
			for(FilterGroup g : collectionsCache.get(p).collections.keySet())
			{
				for ( FilterCollectionPanel f : collectionsCache.get(p).collections.get(g))
					this.remove(f);
			}
			collectionsCache.get(p).collections.clear();
		}
		collectionsCache.clear();
		this.validate();

	}
	*/
}
