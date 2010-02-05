/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.gitools.ui.dialog.progress;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import org.gitools.ui.platform.dialog.ExceptionDialog;
import java.awt.Window;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.SwingUtilities;

public class JobThread implements JobRunnable {

    private Window parent;

	private JobRunnable runnable;

    private Thread thread;

	private JobProgressDialog dlg;

	private JobProgressMonitor monitor;

	public static final void execute(Window parent, JobRunnable runnable) {
		new JobThread(parent, runnable).execute();
	}
    
	public JobThread(Window parent, JobRunnable runnable) {
        this.parent = parent;
		this.runnable = runnable;
    }

	public JobThread(Window parent) {
        this.parent = parent;
		this.runnable = this;
    }

    public Window getParent() {
        return parent;
    }

    /*public synchronized boolean isCancelled() {
        return cancelled;
    }

    public synchronized void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }*/

    public Thread getThread() {
        return thread;
    }

    public synchronized void setThread(Thread jobThread) {
        this.thread = jobThread;
    }

    private synchronized JobProgressDialog getDlg() {
        if (dlg == null) {
            dlg = new JobProgressDialog(parent, false);
            dlg.addCancelListener(new JobProgressDialog.CancelListener() {
				@Override public void cancelled() {
                    cancelJob();
                }
            });
        }
        return dlg;
    }

    private synchronized void setDlg(JobProgressDialog dlg) {
        this.dlg = dlg;
    }

	protected void cancelJob() {
        getMonitor().cancel();

        dlg.setMessage("Cancelling...");

        Timer timer = new Timer("JobThread.cancelJob");
        timer.schedule(new TimerTask() {
            @Override public void run() {
                synchronized(this) {
                    Thread jobThread = getThread();
                    if (jobThread != null && jobThread.isAlive())
                        jobThread.interrupt();
                }
            }

        }, 250);
    }

    protected void done() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                JobProgressDialog dlg = getDlg();
                dlg.setVisible(false);
                dlg.dispose();
                setDlg(null);
            }
        });
    }

	protected synchronized JobProgressMonitor getMonitor() {
		return monitor;
	}

	protected synchronized void setMonitor(JobProgressMonitor monitor) {
		this.monitor = monitor;
	}

    public void startThread() {
        thread = new Thread() {
            @Override
            public void run() {
				setMonitor(new JobProgressMonitor(
						getDlg(), System.out, true, false));

                runnable.run(monitor);

				done();

                setThread(null);

                if (monitor.getCause() != null) {
                    ExceptionDialog ed = new ExceptionDialog(parent, monitor.getCause());
                    ed.setVisible(true);
                }
            }
        };

        thread.start();
    }

	public void execute() {
		startThread();

		getDlg().setModal(true);
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