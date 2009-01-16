package es.imim.bg.ztools.ui.dialogs;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;

import es.imim.bg.ztools.ui.dialogs.ValueListDialog.ValueCondition;
import es.imim.bg.ztools.ui.dialogs.ValueListDialog.ValueCriteria;

public class ValueCriteriaDialog extends JDialog {
	
	private static final long serialVersionUID = 4201760423693544699L;
	
	private Object[] params;
	private ValueCriteria criteria;

	public ValueCriteriaDialog(JDialog owner, Object[] params) {
		super(owner);
		
		setModal(true);
		setTitle("Create Criteria");
		
		setLocationRelativeTo(owner);
		
		this.params = params;
		
		setLocationByPlatform(true);		
		createComponents();
		pack();
	}

	private void createComponents() {
		
		final JComboBox paramBox = new JComboBox();
		for (Object o : params)
			paramBox.addItem(o);
		
		final JComboBox conditionBox = new JComboBox();
		for (ValueCondition ce : ValueCondition.values())
			conditionBox.addItem(ce);
		
		final JTextField valueField = new JTextField();
	
		final JButton acceptBtn = new JButton("OK");
		acceptBtn.setMargin(new Insets(0, 30, 0, 30));
		acceptBtn.setEnabled(false);
		acceptBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				acceptChanges(paramBox, conditionBox, valueField);
			}
		});
		
		valueField.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				checkField();
			}

			@Override
			public void keyTyped(KeyEvent e) {
			}
			
			private void checkField(){
				if(valueField.getText().isEmpty())
					acceptBtn.setEnabled(false);
				else
					acceptBtn.setEnabled(true);
			}
		});

		final JButton cancelBtn = new JButton("Cancel");
		cancelBtn.setMargin(new Insets(0, 30, 0, 30));
		cancelBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				discardChanges();
			}
		});
		
		final JPanel optionPanel = new JPanel();
		optionPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		optionPanel.setLayout(new GridLayout(1,3));
		optionPanel.add(paramBox);
		optionPanel.add(conditionBox);
		optionPanel.add(valueField);
		
		JPanel contPanel = new JPanel();
		contPanel.setLayout(new BorderLayout());
		contPanel.add(optionPanel, BorderLayout.CENTER);
		
		JPanel mainButtonEastPanel = new JPanel();
		mainButtonEastPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		mainButtonEastPanel.setLayout(new BoxLayout(mainButtonEastPanel, BoxLayout.X_AXIS));
		mainButtonEastPanel.add(cancelBtn);
		mainButtonEastPanel.add(acceptBtn);
		JPanel mainButtonPanel = new JPanel();
		mainButtonPanel.setLayout(new BorderLayout());
		mainButtonPanel.add(mainButtonEastPanel, BorderLayout.EAST);
		
		setLayout(new BorderLayout());
		add(contPanel, BorderLayout.CENTER);
		add(mainButtonPanel, BorderLayout.SOUTH);
	}

	protected void acceptChanges(JComboBox paramBox, JComboBox conditionBox, JTextField valueField) {
		Object param = params[paramBox.getSelectedIndex()];
		ValueCondition valueCondition = (ValueCondition) conditionBox.getSelectedObjects()[0];
		String text = valueField.getText();
		if(!text.isEmpty()){
			criteria = new ValueCriteria(param, valueCondition, text);
			setVisible(false);
		}
	}
	
	protected void discardChanges() {
		criteria = null;
		setVisible(false);
	}
	
	public ValueCriteria getCriteria() {
		setVisible(true);
		return criteria;
	}
}