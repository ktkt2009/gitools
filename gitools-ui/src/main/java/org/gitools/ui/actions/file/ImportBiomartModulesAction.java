package org.gitools.ui.actions.file;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import java.awt.event.ActionEvent;
import java.io.File;
import org.gitools.biomart.restful.BiomartRestfulService;
import org.gitools.biomart.restful.model.Query;

import org.gitools.ui.IconNames;

import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.wizard.WizardDialog;
import org.gitools.ui.biomart.wizard.BiomartModulesWizard;
import org.gitools.ui.platform.progress.JobRunnable;

public class ImportBiomartModulesAction extends BaseAction {

	private static final long serialVersionUID = 668140963768246841L;

	public ImportBiomartModulesAction() {
		super("Biomart Modules (advanced users) ...");
		setLargeIconFromResource(IconNames.biomart24);
		setSmallIconFromResource(IconNames.biomart16);
		setDefaultEnabled(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

			//final BiomartCentralPortalSoapService service = BiomartCentralPortalSoapService.getDefault();
			//List<BiomartSource> lBs = BiomartSourceManager.getDefault().getSources();
			//BiomartSource bsrc = lBs.get(0);
			//final BiomartRestfulService service = BiomartServiceFactory.createRestfulService(bsrc);
		
			final BiomartModulesWizard wizard = new BiomartModulesWizard();
			WizardDialog wdlg = new WizardDialog(AppFrame.instance(), wizard);
			wdlg.open();
			if (wdlg.isCancelled()) {
				return;
			}
			final File file = wizard.getSelectedFile();
			JobThread.execute(AppFrame.instance(), new JobRunnable() {

				@Override
				public void run(IProgressMonitor monitor) {
					monitor.begin("Downloading data...", 1);
					Query query = wizard.getQuery();
					String format = (String) wizard.getFormat().getMime();
					BiomartRestfulService service = wizard.getService();
					try {
						service.queryModule(query, file, format, monitor);
					} catch (Exception ex) {
						monitor.exception(ex);
					}
					monitor.end();
				}
			});		 
	}
}
