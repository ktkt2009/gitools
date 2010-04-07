/*
 * ModuleFilteringPanel.java
 *
 * Created on September 3, 2009, 6:30 PM
 */

package org.gitools.ui.analysis.htest.wizard;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComponent;
import javax.swing.event.DocumentEvent;
import org.gitools.persistence.FileFormat;
import org.gitools.persistence.FileFormats;
import org.gitools.ui.IconNames;
import org.gitools.ui.platform.IconUtils;
import org.gitools.ui.platform.dialog.MessageStatus;
import org.gitools.ui.platform.wizard.AbstractWizardPage;
import org.gitools.ui.settings.Settings;
import org.gitools.ui.utils.DocumentChangeListener;
import org.gitools.ui.utils.FileChooserUtils;

public class ModulesPage extends AbstractWizardPage {

	private static final long serialVersionUID = -3938595143114651781L;

	private static final FileFormat[] formats = new FileFormat[] {
			FileFormats.MODULES_2C_MAP,
			FileFormats.GENE_MATRIX,
			FileFormats.GENE_MATRIX_TRANSPOSED,
			FileFormats.DOUBLE_MATRIX,
			FileFormats.DOUBLE_BINARY_MATRIX,
			FileFormats.MODULES_INDEXED_MAP
	};

	/** Creates new form ModuleFilteringPanel */
    public ModulesPage() {
		setTitle("Select modules");
		
		setLogo(IconUtils.getImageIconResourceScaledByHeight(IconNames.LOGO_MODULES, 96));

        initComponents();

		fileFormatCb.setModel(new DefaultComboBoxModel(formats));
		fileFormatCb.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				updateState(); }
		});

		filePath.getDocument().addDocumentListener(new DocumentChangeListener() {
			@Override protected void update(DocumentEvent e) {
				updateState(); }
		});

		ItemListener il = new ItemListener() {
			@Override public void itemStateChanged(ItemEvent e) {
				updateState(); }
		};

		minSizeEnableChk.addItemListener(il);
		maxSizeEnableChk.addItemListener(il);

		ActionListener al = new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				updateState(); }
		};

		minSizeValueCb.addActionListener(al);
		maxSizeValueCb.addActionListener(al);
    }

	@Override
	public JComponent createControls() {
		return this;
	}

	private void updateState() {
		minSizeValueCb.setEnabled(minSizeEnableChk.isSelected());
		maxSizeValueCb.setEnabled(maxSizeEnableChk.isSelected());

		boolean completed = !filePath.getText().isEmpty();

		setMessage(MessageStatus.INFO, "");

		try {
			Integer.parseInt(((String) maxSizeValueCb.getSelectedItem()));
		}
		catch (NumberFormatException ex) {
			setStatus(MessageStatus.ERROR);
			setMessage("Invalid number for modules having more annotated rows filter");
			completed = false;
		}

		try {
			Integer.parseInt(((String) minSizeValueCb.getSelectedItem()));
		}
		catch (NumberFormatException ex) {
			setStatus(MessageStatus.ERROR);
			setMessage("Invalid number for modules having less annotated rows filter");
			completed = false;
		}

		String path = filePath.getText().trim().toLowerCase();
		if (!path.isEmpty()) {
			if (!getFileFormat().checkExtension(path))
				setMessage(MessageStatus.WARN, "The file extension doesn't match the selected format");

			/*String ext = getFileFormat().getExtension().toLowerCase();
			if (!path.endsWith(ext) &&
					!path.endsWith(ext + ".gz"))
				setMessage(MessageStatus.WARN, "The extension of the data file doesn't match the selected format");*/
		}

		setComplete(completed);
	}

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        minSizeEnableChk = new javax.swing.JCheckBox();
        minSizeValueCb = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        fileFormatCb = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        filePath = new javax.swing.JTextField();
        fileBrowseBtn = new javax.swing.JButton();
        maxSizeEnableChk = new javax.swing.JCheckBox();
        maxSizeValueCb = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();

        minSizeEnableChk.setSelected(true);
        minSizeEnableChk.setText("Omit modules having less annotated rows than");
        minSizeEnableChk.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                minSizeEnableChkItemStateChanged(evt);
            }
        });

        minSizeValueCb.setEditable(true);
        minSizeValueCb.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "0", "1", "20", "30", "50", "100" }));
        minSizeValueCb.setSelectedIndex(2);

        jLabel1.setText("File format");

        fileFormatCb.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Two columns mappings", "Binary data matrix" }));

        jLabel2.setText("File");

        fileBrowseBtn.setText("Browse...");
        fileBrowseBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileBrowseBtnActionPerformed(evt);
            }
        });

        maxSizeEnableChk.setText("Omit modules having more annotated rows than");

        maxSizeValueCb.setEditable(true);
        maxSizeValueCb.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "20", "30", "50", "100" }));
        maxSizeValueCb.setEnabled(false);

        jLabel3.setText("Modules filtering options:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(fileFormatCb, 0, 455, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(filePath, javax.swing.GroupLayout.DEFAULT_SIZE, 426, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(fileBrowseBtn)))
                        .addGap(26, 26, 26))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addContainerGap(402, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(minSizeEnableChk, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(maxSizeEnableChk))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(minSizeValueCb, 0, 209, Short.MAX_VALUE)
                            .addComponent(maxSizeValueCb, 0, 209, Short.MAX_VALUE))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fileFormatCb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(fileBrowseBtn)
                    .addComponent(filePath, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(minSizeEnableChk)
                    .addComponent(minSizeValueCb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(maxSizeValueCb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(maxSizeEnableChk))
                .addContainerGap(125, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

private void minSizeEnableChkItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_minSizeEnableChkItemStateChanged
	minSizeValueCb.setEnabled(minSizeEnableChk.isSelected());
}//GEN-LAST:event_minSizeEnableChkItemStateChanged

private void fileBrowseBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileBrowseBtnActionPerformed
	File selPath = FileChooserUtils.selectFile(
			"Select file",
			Settings.getDefault().getLastMapPath(),
			FileChooserUtils.MODE_OPEN);

	if (selPath != null) {
		String fileName = selPath.getName().toLowerCase();
		for (FileFormat ff : formats) {
			if (ff.checkExtension(fileName)) {
				fileFormatCb.setSelectedItem(ff);
				break;
			}
		}
		filePath.setText(selPath.getAbsolutePath());
		Settings.getDefault().setLastMapPath(selPath.getParentFile().getAbsolutePath());
	}
}//GEN-LAST:event_fileBrowseBtnActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton fileBrowseBtn;
    private javax.swing.JComboBox fileFormatCb;
    private javax.swing.JTextField filePath;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    public javax.swing.JCheckBox maxSizeEnableChk;
    public javax.swing.JComboBox maxSizeValueCb;
    public javax.swing.JCheckBox minSizeEnableChk;
    public javax.swing.JComboBox minSizeValueCb;
    // End of variables declaration//GEN-END:variables

	protected FileFormat getFileFormat() {
		return (FileFormat) fileFormatCb.getSelectedItem();
	}

	public String getFileMime() {
		return ((FileFormat) fileFormatCb.getSelectedItem()).getMime();
	}

	public File getSelectedFile() {
		return new File(filePath.getText());
	}

	public int getMinSize() {
		int value = Integer.parseInt(((String) minSizeValueCb.getSelectedItem()));
		return minSizeEnableChk.isSelected() ? value : 0;
	}

	public int getMaxSize() {
		int value = Integer.parseInt(((String) maxSizeValueCb.getSelectedItem()));
		return maxSizeEnableChk.isSelected() ? value : Integer.MAX_VALUE;
	}
}
