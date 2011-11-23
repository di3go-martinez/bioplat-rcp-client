package edu.unlp.medicine.bioplat.rcp.utils;

import org.eclipse.ui.IEditorInput;

import edu.unlp.medicine.bioplat.rcp.editor.input.DefaultEditorInput;

public class EditorInputFactory {
	private EditorInputFactory() {
	}

	public static <T> IEditorInput createDefaultEditorInput(T anObject) {
		return new DefaultEditorInput<T>(anObject);
	}

}






