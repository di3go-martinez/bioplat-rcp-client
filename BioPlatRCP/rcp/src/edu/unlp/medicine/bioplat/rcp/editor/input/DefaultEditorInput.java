package edu.unlp.medicine.bioplat.rcp.editor.input;

import edu.unlp.medicine.bioplat.rcp.utils.EditorInputFactory;

/**
 * 
 * @author diego
 * 
 * @param <T>
 * 
 * @deprecated no usar directamente, usar la factory
 * @see EditorInputFactory
 */

@Deprecated
public class DefaultEditorInput<T> extends AbstractEditorInput<T> {

	public DefaultEditorInput(T object) {
		super(object);
	}
}