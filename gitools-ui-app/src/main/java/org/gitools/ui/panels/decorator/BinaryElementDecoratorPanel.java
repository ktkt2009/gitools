/*
 *  Copyright 2010 Universitat Pompeu Fabra.
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

package org.gitools.ui.panels.decorator;

import edu.upf.bg.cutoffcmp.CutoffCmp;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.gitools.model.decorator.impl.BinaryElementDecorator;
import org.gitools.heatmap.Heatmap;
import org.gitools.matrix.model.element.IElementAdapter;
import org.gitools.ui.platform.component.ColorChooserLabel;
import org.gitools.ui.platform.component.ColorChooserLabel.ColorChangeListener;
import org.gitools.ui.platform.AppFrame;

public class BinaryElementDecoratorPanel extends AbstractElementDecoratorPanel {

	private static final long serialVersionUID = -930914489603614155L;

	private BinaryElementDecorator decorator;

	private JComboBox valueCb;

	private JComboBox cmpCb;

	private JTextField cutoffTf;

	private ColorChooserLabel emptyCc;

	public BinaryElementDecoratorPanel(Heatmap model) {
		super(model);
		
		this.decorator = (BinaryElementDecorator) getDecorator();
		
		final IElementAdapter adapter = decorator.getAdapter();
		
		valueProperties = new ArrayList<IndexedProperty>();
		loadAllProperties(valueProperties, adapter);
		
		createComponents();
	}

	private void createComponents() {
		valueCb = new JComboBox(new DefaultComboBoxModel(valueProperties.toArray()));
		
		valueCb.addItemListener(new ItemListener() {
			@Override public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED)
					valueChanged();
			}
		});
		
		cmpCb = new JComboBox(CutoffCmp.comparators);
		
		cmpCb.addItemListener(new ItemListener() {
			@Override public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED)
					cmpChanged();
			}
		});
		
		cutoffTf = new JTextField(Double.toString(decorator.getCutoff()));
		cutoffTf.getDocument().addDocumentListener(new DocumentListener() {
			@Override public void changedUpdate(DocumentEvent e) {
				cutoffChanged(); }
			@Override public void insertUpdate(DocumentEvent e) {	
				cutoffChanged(); }
			@Override public void removeUpdate(DocumentEvent e) { 
				cutoffChanged(); }
		});

		final ColorChooserLabel colorCc = new ColorChooserLabel(decorator.getColor());
		colorCc.setToolTipText("Condition color");
		colorCc.addColorChangeListener(new ColorChangeListener() {
			@Override public void colorChanged(Color color) {
				decorator.setColor(color); }
		});
		
		final ColorChooserLabel nonSigColorCc = new ColorChooserLabel(decorator.getNonSignificantColor());
		nonSigColorCc.setToolTipText("Non condition color");
		nonSigColorCc.addColorChangeListener(new ColorChangeListener() {
			@Override public void colorChanged(Color color) {
				decorator.setNonSignificantColor(color); }
		});

		emptyCc = new ColorChooserLabel(decorator.getEmptyColor());
		emptyCc.setToolTipText("Empty cell color");
		emptyCc.addColorChangeListener(new ColorChangeListener() {
			@Override public void colorChanged(Color color) {
				decorator.setEmptyColor(color); }
		});

		refresh();
		
		setLayout(new FlowLayout(FlowLayout.LEFT));
		add(new JLabel("Value"));
		add(valueCb);
		add(cmpCb);
		add(cutoffTf);
		add(colorCc);
		add(nonSigColorCc);
		add(new JLabel("Empty"));
		add(emptyCc);
	}

	private void refresh() {
		for (int i = 0; i < valueProperties.size(); i++)
			if (valueProperties.get(i).getIndex() == decorator.getValueIndex())
				valueCb.setSelectedIndex(i);
		
		getTable().setSelectedPropertyIndex(decorator.getValueIndex());
		
		CutoffCmp cmp = decorator.getCutoffCmp();
		for (int i = 0; i < cmpCb.getItemCount(); i++) {
			CutoffCmp cc = (CutoffCmp) cmpCb.getItemAt(i);
			if (cc.equals(cmp))
				cmpCb.setSelectedIndex(i);
		}
	}

	private void valueChanged() {
		IndexedProperty propAdapter = 
			(IndexedProperty) valueCb.getSelectedItem();
		
		decorator.setValueIndex(propAdapter.getIndex());
		
		getTable().setSelectedPropertyIndex(propAdapter.getIndex());
	}
	
	protected void cmpChanged() {
		CutoffCmp cc = (CutoffCmp) cmpCb.getSelectedItem();
		decorator.setCutoffCmp(cc);
	}
	
	protected void cutoffChanged() {
		try {
			double cutoff = Double.parseDouble(cutoffTf.getText());
			decorator.setCutoff(cutoff);
			
			AppFrame.instance().setStatusText("Cutoff changed to " + cutoff);
		}
		catch (Exception e) { 
			AppFrame.instance().setStatusText("Invalid cutoff.");
		}
	}
}
