/*
 * #%L
 * gitools-ui-app
 * %%
 * Copyright (C) 2013 Universitat Pompeu Fabra - Biomedical Genomics group
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package org.gitools.ui.actions.file;

import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.matrix.IMatrixLayer;
import org.gitools.api.matrix.view.IMatrixView;
import org.gitools.api.matrix.view.IMatrixViewLayers;
import org.gitools.ui.actions.HeatmapAction;
import org.gitools.ui.export.TextMatrixViewExporter;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.settings.Settings;
import org.gitools.ui.utils.FileChooserUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

public class ExportMatrixAction extends HeatmapAction {

    private static final long serialVersionUID = -7288045475037410310L;

    public ExportMatrixAction() {
        super("Export matrix ...");

        setDesc("Export a matrix");
        setMnemonic(KeyEvent.VK_P);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        final IMatrixView matrixView = getHeatmap();
        IMatrixViewLayers layers = matrixView.getLayers();

        String selectedId = (String) JOptionPane.showInputDialog(
                AppFrame.get(),
                "What do you want to export ?",
                "Export table data", JOptionPane.QUESTION_MESSAGE, null,
                layers.getIds(),
                layers.getTopLayer().getId()
        );

        if (selectedId == null || selectedId.isEmpty()) {
            return;
        }

        final IMatrixLayer selected = layers.get(selectedId);
        final File file = FileChooserUtils.selectFile("Select destination file", Settings.getDefault().getLastExportPath(), FileChooserUtils.MODE_SAVE).getFile();

        if (file == null) {
            return;
        }

        Settings.getDefault().setLastExportPath(file.getParentFile().getAbsolutePath());

        JobThread.execute(AppFrame.get(), new JobRunnable() {
            @Override
            public void run(IProgressMonitor monitor) {
                try {
                    monitor.begin("Exporting to image ...", 1);
                    monitor.info("File: " + file.getName());

                    TextMatrixViewExporter.exportMatrix(matrixView, selected, file);

                    monitor.end();
                } catch (IOException ex) {
                    monitor.exception(ex);
                }
            }
        });

        AppFrame.get().setStatusText(selected + " exported.");
    }
}
