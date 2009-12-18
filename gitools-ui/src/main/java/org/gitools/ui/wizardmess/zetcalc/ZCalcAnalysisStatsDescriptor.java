package org.gitools.ui.wizardmess.zetcalc;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.gitools.ui.wizardmess.AbstractWizard;
import org.gitools.ui.wizardmess.AnalysisWizard;
import org.gitools.ui.wizardmess.AnalysisWizardPanelDescriptor;
import org.gitools.ui.wizardmess.WizardDataModel;
import org.gitools.ui.wizardmess.AnalysisWizard.StatTest;



public class ZCalcAnalysisStatsDescriptor extends AnalysisWizardPanelDescriptor {
    
    public static final String IDENTIFIER = "STATS_PANEL";
    final ZCalcAnalysisStatsPanel statsPanel;
    WizardDataModel dataModel;
    final JTextField sampleSizeField;
    final JComboBox statTestBox;
    final JLabel helpLabel;

    
    public ZCalcAnalysisStatsDescriptor(AbstractWizard aw, Object BackPanelDescriptor, Object NextPanelDescriptor) {    	
        super(IDENTIFIER, new ZCalcAnalysisStatsPanel(), BackPanelDescriptor, NextPanelDescriptor);
        this.statsPanel = (ZCalcAnalysisStatsPanel) getPanelComponent();
        this.dataModel = aw.getWizardDataModel();
        
        sampleSizeField = statsPanel.getSampleSizeField();
        statTestBox = statsPanel.getStatTestBox();
        helpLabel = statsPanel.getHelpLabel();
        final String helpLabelText = helpLabel.getText().replaceAll("\\<.*?\\>", "");
        
        statTestBox.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				setSampleSizeFieldAccordingToStatTest();			
			}
        	
        });
        
        sampleSizeField.addKeyListener(new KeyListener(){

			@Override
			public void keyPressed(KeyEvent e) { }

			@Override
			public void keyReleased(KeyEvent e) {
				setNextButtonAccordingToInputs();
			}

			@Override
			public void keyTyped(KeyEvent e) { }
        });
        
        helpLabel.addMouseListener(new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent e) {
				changeToHelpPanel();
				setNextButtonAccordingToInputs();
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				helpLabel.setText("<html><u>"+ helpLabelText +"</u></html>");
			}
			
			@Override
			public void mouseExited(MouseEvent e) { 
				helpLabel.setText("<html>"+ helpLabelText +"</html>");
			}
			
			@Override
			public void mousePressed(MouseEvent e) { }
			
			@Override
			public void mouseReleased(MouseEvent e) { }
        });
    }
            
    
    protected void setSampleSizeFieldAccordingToStatTest() {
		String selected = statTestBox.getSelectedItem().toString();
		if (selected.equals(StatTest.ZSCORE_MEAN.toString()) ||
				selected.equals(StatTest.ZSCORE_MEDIAN.toString())) {
			sampleSizeField.setEnabled(true);
		}
		else 
			sampleSizeField.setEnabled(false);
	}


	protected void changeToHelpPanel() {
        Object descriptor = ZCalcAnalysisStatsHelpDescriptor.IDENTIFIER;
        getWizard().setCurrentPanel(descriptor);
        //aw.setCurrentPanel(descriptor);
	}


	public void aboutToDisplayPanel() {
    	setNextButtonAccordingToInputs();
    	setSampleSizeFieldAccordingToStatTest();
    }
    
    public void aboutToHidePanel() {
    	if (!sampleSizeField.getText().isEmpty()) {
    		dataModel.setValue(AnalysisWizard.SAMPLE_SIZE, sampleSizeField.getText());
    	}
    	
    	StatTest[] statTests = StatTest.values();
    	StatTest st = statTests[statTestBox.getSelectedIndex()];
    	dataModel.setValue(AnalysisWizard.STAT_TEST, st.toCommandLineArgument());
    }    
    
    private void setNextButtonAccordingToInputs() {
         if (sampleSizeInputIsOK())
            getWizard().setNextFinishButtonEnabled(true);
         else
            getWizard().setNextFinishButtonEnabled(false);        
    }
    
    
    private boolean sampleSizeInputIsOK() {
    	String input = sampleSizeField.getText();
		if (!input.isEmpty() && isInteger(input))
				return true;
		else if (input.isEmpty())
			return true;
		else
			return false;
	}


	private boolean isInteger(String text) {
		try {
			Integer.parseInt(text);
		}
		catch(Exception e) {
			return false;
		}
		return true;
	}
    
}
