package org.gitools.ui.actions.help;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.dialog.AboutDialog;
import org.gitools.ui.platform.AppFrame;


public class AboutAction extends BaseAction {

	private static final long serialVersionUID = 8302818623988394433L;

	public AboutAction() {
		super("About " + AppFrame.getAppName() + "...");
		setDesc("Know more about this application");
		setMnemonic(KeyEvent.VK_A);
		setDefaultEnabled(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		new AboutDialog(AppFrame.instance())
			.setVisible(true);
	}

}