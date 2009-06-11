package org.gitools.ui.actions.file;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import org.gitools.ui.AppFrame;
import org.gitools.ui.actions.BaseAction;
import org.gitools.ui.dialog.ProgressMonitorDialog;
import org.gitools.ui.jobs.ZCalcCommandJob;
import org.gitools.ui.wizardmess.AnalysisWizard;
import org.gitools.ui.wizardmess.WizardDataModel;
import org.gitools.ui.wizardmess.zetcalc.ZCalcAnalysisWizard;

import edu.upf.bg.progressmonitor.ProgressMonitor;
import org.gitools.commands.ZCalcCommand;

public class NewZCalcAnalysisAction extends BaseAction {

	private static final long serialVersionUID = -8592231961109105958L;

	public NewZCalcAnalysisAction() {
		super("New ZetCalc analysis...");

		setDesc("Run a new ZetCalc analysis");
		setMnemonic(KeyEvent.VK_Z);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		final ZCalcAnalysisWizard wizard = 
			new ZCalcAnalysisWizard(AppFrame.instance());

		final WizardDataModel dialogData = 
			wizard.getWizardDataModel();

		final ZCalcCommand command = wizard.getCommand();

		final String workDir = (String) dialogData
				.getValue(AnalysisWizard.ANALYSIS_WORKING_DIR);
		final String analysisName = (String) dialogData
				.getValue(AnalysisWizard.ANALYSIS_NAME);

		final File analysisPath = new File(workDir, analysisName);

		if (command != null) {

			final ProgressMonitorDialog monitorDialog = new ProgressMonitorDialog(
					AppFrame.instance(), "Log");

			final ProgressMonitor monitor = 
				monitorDialog.getProgressMonitor();

			AppFrame.instance().getJobProcessor().addJob(
					new ZCalcCommandJob(command, monitor, analysisPath));
			
			monitorDialog.setVisible(true);
		}
	}
}
