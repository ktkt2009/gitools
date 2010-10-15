package org.gitools.ui.actions.file;

import org.gitools.analysis.correlation.CorrelationAnalysis;
import org.gitools.analysis.htest.HtestAnalysis;
import org.gitools.ui.utils.FileFormatFilter;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import org.gitools.ui.IconNames;
import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.settings.Settings;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;
import org.gitools.analysis.combination.CombinationAnalysis;
import org.gitools.analysis.htest.enrichment.EnrichmentAnalysis;
import org.gitools.analysis.htest.oncozet.OncodriveAnalysis;
import org.gitools.model.Analysis;
import org.gitools.persistence.FileFormat;
import org.gitools.persistence.FileFormats;
import org.gitools.persistence.MimeTypes;
import org.gitools.persistence.PersistenceManager;
import org.gitools.ui.analysis.combination.editor.CombinationAnalysisEditor;
import org.gitools.ui.analysis.correlation.editor.CorrelationAnalysisEditor;
import org.gitools.ui.analysis.htest.editor.EnrichmentAnalysisEditor;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.analysis.htest.editor.OncodriveAnalysisEditor;
import org.gitools.ui.platform.editor.AbstractEditor;
import org.gitools.ui.utils.FileChooserUtils;

public class OpenAnalysisAction extends BaseAction {

	private static final long serialVersionUID = -6528634034161710370L;

	public OpenAnalysisAction() {
		super("Analysis ...");
		setDesc("Open an analysis from the file system");
		setSmallIconFromResource(IconNames.openAnalysis16);
		setLargeIconFromResource(IconNames.openAnalysis24);
		setMnemonic(KeyEvent.VK_A);
		setDefaultEnabled(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		FileFilter[] filters = new FileFilter[] {
			new FileFormatFilter("Any analysis", null, new FileFormat[] {
				FileFormats.ENRICHMENT,
				FileFormats.ONCODRIVE,
				FileFormats.CORRELATIONS,
				FileFormats.COMBINATION
			}),
			new FileFormatFilter(FileFormats.ENRICHMENT),
			new FileFormatFilter(FileFormats.ONCODRIVE),
			new FileFormatFilter(FileFormats.CORRELATIONS),
			new FileFormatFilter(FileFormats.COMBINATION)
		};

		FileChooserUtils.FileAndFilter ret = FileChooserUtils.selectFile(
				"Select the analysis file",
				Settings.getDefault().getLastPath(),
				FileChooserUtils.MODE_OPEN,
				filters);

		final File file = ret.getFile();
		final FileFormatFilter filter = (FileFormatFilter) ret.getFilter();

		if (file != null) {
			Settings.getDefault().setLastPath(file.getParent());
			Settings.getDefault().save();

			JobThread.execute(AppFrame.instance(), new JobRunnable() {
				@Override
				public void run(IProgressMonitor monitor) {
					try {
						AbstractEditor editor = null;

						String mime = filter.getMime();
						if (mime == null)
							mime = PersistenceManager.getDefault().getMimeFromFile(file.getName());
						
						Analysis analysis =	(Analysis) PersistenceManager.getDefault()
								.load(file, mime, monitor);

						if (monitor.isCancelled())
							return;

						if (mime.equals(MimeTypes.ENRICHMENT_ANALYSIS))
							editor = new EnrichmentAnalysisEditor((EnrichmentAnalysis) analysis);
						else if (mime.equals(MimeTypes.ONCODRIVE_ANALYSIS))
							editor = new OncodriveAnalysisEditor((OncodriveAnalysis) analysis);
						else if (mime.equals(MimeTypes.CORRELATIONS_ANALYSIS))
							editor = new CorrelationAnalysisEditor((CorrelationAnalysis) analysis);
						else if (mime.equals(MimeTypes.COMBINATION_ANALYSIS))
							editor = new CombinationAnalysisEditor((CombinationAnalysis) analysis);

						editor.setName(file.getName());

						final AbstractEditor newEditor = editor;
						SwingUtilities.invokeLater(new Runnable() {
							@Override
							public void run() {
								AppFrame.instance().getEditorsPanel().addEditor(newEditor);
								AppFrame.instance().refresh();
							}
						});
					} catch (Exception ex) {
						monitor.exception(ex);
					}
				}
			});
		}
	}
}