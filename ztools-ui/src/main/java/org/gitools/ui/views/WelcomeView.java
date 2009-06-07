package org.gitools.ui.views;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;

import org.gitools.ui.actions.file.NewZCalcAnalysisAction;
import org.gitools.ui.actions.file.OpenAnalysisAction;


public class WelcomeView extends HtmlView {

	private static final long serialVersionUID = 6851947500231401412L;

	public WelcomeView() {
		super("Welcome");
		
		try {
			URL url = getClass().getResource("/html/welcome.html");
			setPage(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void performUrlAction(String name) {
		if (name.equals("newZetCalcAnalysis")) {
			new NewZCalcAnalysisAction()
				.actionPerformed(new ActionEvent(this, 0, name));
		}
		else if (name.equals("openAnalysis")) {
			new OpenAnalysisAction()
				.actionPerformed(new ActionEvent(this, 0, name));
		}
	}
}