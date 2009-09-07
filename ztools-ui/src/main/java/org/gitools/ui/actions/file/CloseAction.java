package org.gitools.ui.actions.file;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import org.gitools.ui.AppFrame;
import org.gitools.ui.IconNames;
import org.gitools.ui.WorkspacePanel;
import org.gitools.ui.actions.BaseAction;
import org.gitools.ui.editor.AbstractEditor;


public class CloseAction extends BaseAction {

	private static final long serialVersionUID = 2399811452235609343L;

	public CloseAction() {
		super("Close");
		
		setDesc("Close current tab");
		setSmallIconFromResource(IconNames.close16);
		setLargeIconFromResource(IconNames.close24);
		setMnemonic(KeyEvent.VK_O);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		WorkspacePanel workspace = AppFrame.instance().getWorkspace();
		AbstractEditor currentView = workspace.getSelectedEditor();
		if (currentView != null)
			workspace.removeEditor(currentView);
		
		AppFrame.instance().refresh();
		AppFrame.instance().setStatusText("View closed.");
	}

}
