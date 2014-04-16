/*
 * #%L
 * gitools-ui-platform
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
package org.gitools.ui.platform.progress;

import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.ui.platform.dialog.ExceptionDialog;

import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CancellationException;

public class JobThread implements JobRunnable {

    private final JFrame parent;

    private final JobRunnable runnable;

    private Thread thread;

    private JobProgressGlassPane dlg;

    private JobProgressMonitor monitor;

    public static void execute(JFrame parent, JobRunnable runnable) {
        new JobThread(parent, runnable).execute();
    }

    private JobThread(JFrame parent, JobRunnable runnable) {
        this.parent = parent;
        this.runnable = runnable;
    }

    public JobThread(JFrame parent) {
        this.parent = parent;
        this.runnable = this;
    }

    public Window getParent() {
        return parent;
    }

    Thread getThread() {
        return thread;
    }

    synchronized void setThread(Thread jobThread) {
        this.thread = jobThread;
    }

    private synchronized JobProgressGlassPane getDlg() {
        if (dlg == null) {
            dlg = new JobProgressGlassPane(parent, true);
            dlg.addCancelListener(new CancelListener() {
                @Override
                public void cancelled() {
                    cancelJob();
                }
            });
        }
        return dlg;
    }

    private synchronized void setDlg(JobProgressGlassPane dlg) {
        this.dlg = dlg;
    }

    void cancelJob() {
        getMonitor().cancel();

        dlg.setMessage("Cancelling...");

        Timer timer = new Timer("JobThread.cancelJob");
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                synchronized (JobThread.this) {
                    Thread jobThread = getThread();
                    if (jobThread != null && jobThread.isAlive()) {
                        jobThread.interrupt();
                    }
                }
            }

        }, 250);
    }

    void done() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JobProgressGlassPane dlg = getDlg();
                dlg.setVisible(false);
                //TODO dlg.dispose();
                setDlg(null);
            }
        });
    }

    synchronized JobProgressMonitor getMonitor() {
        return monitor;
    }

    synchronized void setMonitor(JobProgressMonitor monitor) {
        this.monitor = monitor;
    }

    void startThread() {
        thread = new Thread("JobThread") {
            @Override
            public void run() {
                newRunnable().run();
            }
        };
        thread.start();
    }


    private Runnable newRunnable() {
        return new Runnable() {
            @Override
            public void run() {
                JobProgressMonitor m = new JobProgressMonitor(getDlg(), System.out, false, false);

                setMonitor(m);
                org.gitools.utils.progressmonitor.ProgressMonitor.set(m);

                try {

                    monitor.start();
                    runnable.run(monitor);
                    monitor.end();

                } catch (CancellationException e) {
                    if (!monitor.isCancelled()) {
                        monitor.exception(e);
                    }
                } catch (Throwable cause) {
                    m.exception(cause);
                }

                done();

                setThread(null);

                if (monitor.getCause() != null) {
                    ExceptionDialog ed = new ExceptionDialog(parent, monitor.getCause());
                    ed.setVisible(true);
                }
            }
        };
    }

    void execute() {
        startThread();

        //getDlg().setModal(true);
        getDlg().setVisible(true);
    }

    public boolean isCancelled() {
        return getMonitor().isCancelled();
    }

    public Throwable getCause() {
        return getMonitor().getCause();
    }

    @Override
    public void run(IProgressMonitor monitor) {
        throw new UnsupportedOperationException("Opperation should be overrided");
    }
}
