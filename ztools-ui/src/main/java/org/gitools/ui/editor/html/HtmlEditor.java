package org.gitools.ui.editor.html;

import java.awt.BorderLayout;
import java.io.IOException;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.event.HyperlinkEvent.EventType;

import org.gitools.ui.editor.AbstractEditor;

public class HtmlEditor extends AbstractEditor {

	private static final long serialVersionUID = 1693342849779799326L;

	private String title;
	private JTextPane htmlPane;

	@Override
	public String getName() {
		return title;
	}
	
	public HtmlEditor(String title) {
		this.title = title;
		
		createComponents();
	}
	
	private void createComponents() {
		htmlPane = new JTextPane();
		htmlPane.setEditable(false);
		//htmlPane.setBackground(Color.WHITE);
		htmlPane.setContentType("text/html");

		htmlPane.addHyperlinkListener(new HyperlinkListener() {
			@Override
			public void hyperlinkUpdate(HyperlinkEvent e) {
				if (e.getEventType() == EventType.ACTIVATED) {
					final String desc = e.getDescription();
					if (desc.startsWith("action:"))
						performUrlAction(desc.substring("action:".length()));
					else {
						try {
							htmlPane.setPage(e.getURL());
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				}
			}
		});
		
		final JScrollPane scrollPane = new JScrollPane(htmlPane);
		scrollPane.setBorder(
				BorderFactory.createEmptyBorder());
		
		setLayout(new BorderLayout());
		add(scrollPane, BorderLayout.CENTER);
	}

	protected void performUrlAction(String name) {
	}

	/*protected JTextPane getHtmlPane() {
		return htmlPane;
	}*/
	
	protected void setPage(URL url) throws IOException {
		htmlPane.setPage(url);
	}
	
	protected URL getPage() {
		return htmlPane.getPage();
	}
	
	@Override
	public Object getModel() {
		return htmlPane.getDocument();
	}

	@Override
	public void refresh() {
	}

}