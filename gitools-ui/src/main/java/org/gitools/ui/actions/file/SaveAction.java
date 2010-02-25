package org.gitools.ui.actions.file;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import org.gitools.ui.dialog.progress.JobRunnable;
import org.gitools.ui.dialog.progress.JobThread;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.EditorsPanel;

import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.editor.IEditor;

public class SaveAction extends BaseAction {

	private static final long serialVersionUID = -6528634034161710370L;

	public SaveAction() {
		super("Save");
		setDesc("Save changes");
		setMnemonic(KeyEvent.VK_S);
	}

	@Override
	public boolean isEnabledByEditor(IEditor editor) {
		return editor != null && editor.isDirty();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		EditorsPanel editorPanel = AppFrame.instance().getEditorsPanel();

		final IEditor currentEditor = editorPanel.getSelectedEditor();

		JobThread.execute(AppFrame.instance(), new JobRunnable() {
			@Override
			public void run(IProgressMonitor monitor) {
				currentEditor.doSave(monitor);
			}
		});
	}
}
