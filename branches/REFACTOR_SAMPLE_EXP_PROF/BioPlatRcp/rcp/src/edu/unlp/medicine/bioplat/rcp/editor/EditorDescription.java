package edu.unlp.medicine.bioplat.rcp.editor;

import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;

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
}
