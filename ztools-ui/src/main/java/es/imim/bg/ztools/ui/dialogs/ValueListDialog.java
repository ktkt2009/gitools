package es.imim.bg.ztools.ui.dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import es.imim.bg.ztools.ui.AppFrame;
import es.imim.bg.ztools.ui.dialogs.DefineValueCriteriaDialog.ValueCriteria;

public class ValueListDialog extends JDialog {
	
	private static final long serialVersionUID = 4201760423693544699L;
	
	private Object[] params;
	private List<ValueCriteria> valueList;

	private void setValueList(List<ValueCriteria> newValueList) {
		this.valueList = newValueList;
	}

	public List<ValueCriteria> getValueList() {
		setVisible(true);
		return valueList;
	}
	
	public ValueListDialog(JFrame owner, Object[] params) {
		super(owner);
		
		setModal(true);
		setTitle("Criteria list for values");
		
		this.params = params;
		
		setLocationByPlatform(true);				
		createComponents();
		getContentPane().setBackground(Color.WHITE);
		pack();
	}

	private void createComponents() {
				
		final DefaultListModel tempListModel = new DefaultListModel();    
			// Create the temporary model object.
		final JList valueList = new JList(tempListModel);   
			// Create the temporary list component.
		
		final JScrollPane scrollPane = new JScrollPane(valueList);
		scrollPane.setBorder(
				BorderFactory.createEmptyBorder(8, 8, 0, 0));

		final JButton addBtn = new JButton("Add...");
		addBtn.setMargin(new Insets(0, 30, 0, 30));
		addBtn.addActionListener(new ActionListener() {
			//@Override
			public void actionPerformed(ActionEvent e) {
				addElmenent(tempListModel);
			}
		});
		
		final JButton removeBtn = new JButton("Remove");
		removeBtn.setMargin(new Insets(0, 30, 0, 30));
		removeBtn.setEnabled(false);
		removeBtn.addActionListener(new ActionListener() {
			//@Override
			public void actionPerformed(ActionEvent e) {
				removeElement(tempListModel, valueList);
			}
		});
		
		final JButton upBtn = new JButton("Up");
		upBtn.setMargin(new Insets(0, 30, 0, 30));
		upBtn.setEnabled(false);
		upBtn.addActionListener(new ActionListener() {
			//@Override
			public void actionPerformed(ActionEvent e) {
				moveUp(tempListModel, valueList);
			}
		});
		
		final JButton downBtn = new JButton("Down");
		downBtn.setMargin(new Insets(0, 30, 0, 30));
		downBtn.setEnabled(false);
		downBtn.addActionListener(new ActionListener() {
			//@Override
			public void actionPerformed(ActionEvent e) {
				moveDown(tempListModel, valueList);
			}
		});
		
		valueList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if(valueList.getSelectedValue() == null){
					upBtn.setEnabled(false);
					downBtn.setEnabled(false);
					removeBtn.setEnabled(false);
				}else{
					upBtn.setEnabled(true);
					downBtn.setEnabled(true);
					removeBtn.setEnabled(true);
				}
			}
		});
		
		final JButton acceptBtn = new JButton("OK");
		acceptBtn.setMargin(new Insets(0, 30, 0, 30));
		acceptBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				acceptChanges(tempListModel);
			}
		});
		
		final JButton cancelBtn = new JButton("Cancel");
		cancelBtn.setMargin(new Insets(0, 30, 0, 30));
		cancelBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				discardChanges(tempListModel);
				closeDialog();
			}
		});
		
		final JPanel btnPanel = new JPanel();
		btnPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		btnPanel.setLayout(new GridLayout(7,1));
		btnPanel.add(addBtn);
		btnPanel.add(removeBtn);
		btnPanel.add(upBtn);
		btnPanel.add(downBtn);
		
		final JPanel contPanel = new JPanel();
		contPanel.setLayout(new BorderLayout());
		contPanel.add(scrollPane, BorderLayout.CENTER);
		contPanel.add(btnPanel, BorderLayout.EAST);
		
		final JPanel mainButtonEastPanel = new JPanel();
		mainButtonEastPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		mainButtonEastPanel.setLayout(new BoxLayout(mainButtonEastPanel, BoxLayout.X_AXIS));
		mainButtonEastPanel.add(cancelBtn);
		mainButtonEastPanel.add(acceptBtn);
		final JPanel mainButtonPanel = new JPanel();
		mainButtonPanel.setLayout(new BorderLayout());
		mainButtonPanel.add(mainButtonEastPanel, BorderLayout.EAST);
		
		setLayout(new BorderLayout());
		add(contPanel, BorderLayout.CENTER);
		add(mainButtonPanel, BorderLayout.SOUTH);
	}

	protected void addElmenent(DefaultListModel tempListModel) {
		DefineValueCriteriaDialog d = new DefineValueCriteriaDialog(AppFrame.instance(), params);
		ValueCriteria c = d.getCriteria();
		tempListModel.addElement(c);
	}
	
	protected void removeElement(DefaultListModel listModel, JList criteriaList) {
		int[] selectedIndices = criteriaList.getSelectedIndices();
		for (int i = selectedIndices.length; i > 0; i--)
			listModel.remove(selectedIndices[i-1]);		
	}

	protected void moveUp(final DefaultListModel listModel,
			final JList criteriaList) {
		int[] selectedIndices = criteriaList.getSelectedIndices();
		int actualIndex;
		int prevIndex = -1;
		int lag = 0;
		Object el;
		for (int i = 0; i < selectedIndices.length ; i++) {

			actualIndex = selectedIndices[i];
			if(actualIndex == 0)
				lag++;
			if (prevIndex != actualIndex -1 && lag > 0)
				lag--;
			prevIndex = actualIndex;

			if(lag == 0) {
				int newIndex = actualIndex - 1 + lag;
				el = listModel.getElementAt(actualIndex);
				listModel.remove(actualIndex);
				listModel.add(newIndex, el);
				selectedIndices[i] = newIndex;
			}
		}
		criteriaList.setSelectedIndices(selectedIndices);
	}

	protected void moveDown(final DefaultListModel listModel,
			final JList criteriaList) {
		int[] selectedIndices = criteriaList.getSelectedIndices();
		int actualIndex;
		int prevIndex = listModel.getSize();
		int lag = 0;
		Object el;
		for (int i = selectedIndices.length; i > 0; i--) {
			actualIndex = selectedIndices[i-1];
			if(actualIndex == listModel.getSize() - 1)
				lag++;
			if (prevIndex != actualIndex +1 && lag > 0)
				lag--;
			prevIndex = actualIndex;

			if (lag==0) {
				int newIndex = actualIndex + 1 - lag;
				el = listModel.getElementAt(actualIndex);
				listModel.remove(actualIndex);
				listModel.add(newIndex, el);
				selectedIndices[i-1] = newIndex;
			}
		}
		criteriaList.setSelectedIndices(selectedIndices);
	}

	protected void acceptChanges(DefaultListModel tempListModel) {
		List<ValueCriteria> newCriteriaList = new ArrayList<ValueCriteria>();
		for (int i = 0; i < tempListModel.getSize(); i++){
			ValueCriteria c = (ValueCriteria) tempListModel.getElementAt(i);
			newCriteriaList.add(c);
		}
		setValueList(newCriteriaList);
		closeDialog();
	}
	
	protected void discardChanges(DefaultListModel tempListModel) {
		setValueList(null);
		closeDialog();
	}
	
	protected void closeDialog() {
		setVisible(false);
	}
}