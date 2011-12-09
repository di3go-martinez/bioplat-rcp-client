package edu.unlp.medicine.bioplat.rcp.editor;

import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;

public class EditorDescription {

	private IEditorInput input;
	private IEditorPart editorPart;
	private String title = "";

	public EditorDescription(IEditorInput input, IEditorPart editorPart) {
		this(input, editorPart, editorPart.getTitle()); // agregar un listener
														// por cambio de título?
	}

	public EditorDescription(IEditorInput input, IEditorPart editorPart, String title) {
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
