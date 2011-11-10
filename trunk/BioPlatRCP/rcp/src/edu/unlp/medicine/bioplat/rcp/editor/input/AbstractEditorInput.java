package edu.unlp.medicine.bioplat.rcp.editor.input;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

public abstract class AbstractEditorInput<T> implements IEditorInput {

	// FIXME Analizar: el model no tiene que estar en el editorinput, ya que es
	// un objeto cacheado por la API; en su lugar tendr�a que tener una
	// referencia
	private final T model;


	public AbstractEditorInput(T model) {
		this.model = model;
	}

	@Override
	public Object getAdapter(@SuppressWarnings("rawtypes") Class adapter) {
		return null;
	}

	@Override
	public boolean exists() {
		return false;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	@Override
	public String getName() {
		return model().toString();
	}

	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	@Override
	public String getToolTipText() {
		return getName();
	}

	public T model() {
		return model;
	}

	// @Override
	// protected void finalize() throws Throwable {
	// model = null;
	// modelr = null;
	// super.finalize();
	// }

}
