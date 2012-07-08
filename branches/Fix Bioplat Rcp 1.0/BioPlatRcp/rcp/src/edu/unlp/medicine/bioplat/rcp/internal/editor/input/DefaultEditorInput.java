package edu.unlp.medicine.bioplat.rcp.internal.editor.input;

import edu.unlp.medicine.bioplat.rcp.editor.input.AbstractEditorInput;
import edu.unlp.medicine.bioplat.rcp.utils.EditorInputFactory;

/**
 * 
 * @author diego
 * 
 * @param <T>
 * 
 * @see EditorInputFactory
 */
public class DefaultEditorInput<T> extends AbstractEditorInput<T> {

	public DefaultEditorInput(T object) {
		super(object);
	}
}