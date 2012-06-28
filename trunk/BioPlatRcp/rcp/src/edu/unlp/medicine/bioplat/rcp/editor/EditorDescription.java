package edu.unlp.medicine.bioplat.rcp.editor;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

/**
 * 
 * @author Diego Martínez
 * 
 */
public class EditorDescription {

	private IEditorInput input;
	private IEditorPart editorPart;
	private String title = "";

	public EditorDescription(IEditorInput input, final IEditorPart editorPart) {
		this(input, editorPart, editorPart.getTitle());
	}

	public EditorDescription(IEditorInput input, final IEditorPart editorPart, String title) {
		this.input = input;
		this.editorPart = editorPart;
		this.title = title;
	}

	public IEditorInput createEditorInput() {
		return input;
	}

	public IEditorPart editor() {
		return editorPart;
	}

	public String title() {
		return title;
	}

	public static EditorDescription errorEditorDescription(String msg, Throwable exception) {
		return new EditorDescription(null, new ErrorPart(), "Error");
	}

	public static EditorDescription errorEditorDescription(String msg) {
		return errorEditorDescription(msg, null);
	}
}

class ErrorPart extends EditorPart {

	@Override
	public void doSave(IProgressMonitor monitor) {
		// TODO Auto-generated method stub

	}

	@Override
	public void doSaveAs() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isDirty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void createPartControl(Composite parent) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}