package org.gitools.ui.platform.editor;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.gitools.ui.platform.view.AbstractView;

public abstract class AbstractEditor
		extends AbstractView
		implements IEditor {

	private static final long serialVersionUID = -2379950551933668781L;

	public static abstract class EditorListener {
		public void nameChanged(IEditor editor) {}
		public void fileChanged(IEditor editor) {};
		public void dirtyChanged(IEditor editor) {};
		public void saved(IEditor editor) {};
	}

	private File file;
	private boolean dirty = false;
	private boolean saveAsAllowed = false;

	private List<EditorListener> listeners = new ArrayList<EditorListener>();

	@Override
	public void setName(String name) {
		super.setName(name);
		for (EditorListener l : listeners) l.nameChanged(this);
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
		for (EditorListener l : listeners) l.fileChanged(this);
		setName(file.getName());
	}
	
	@Override
	public boolean isDirty() {
		return dirty;
	}
	
	protected void setDirty(boolean dirty) {
		this.dirty = dirty;
		for (EditorListener l : listeners) l.dirtyChanged(this);
	}
	
	@Override
	public boolean isSaveAsAllowed() {
		return saveAsAllowed;
	}
	
	public void setSaveAsAllowed(boolean saveAsAllowed) {
		this.saveAsAllowed = saveAsAllowed;
	}
	
	@Override
	public void doSave(IProgressMonitor monitor) {
		for (EditorListener l : listeners) l.saved(this);
	}
	
	@Override
	public void doSaveAs(IProgressMonitor monitor) {
		for (EditorListener l : listeners) l.saved(this);
	}

	@Override
	public void doVisible() {
	}

	public void addEditorListener(EditorListener listener) {
		listeners.add(listener);
	}

	public void removeEditorListener(EditorListener listener) {
		listeners.add(listener);
	}
}
