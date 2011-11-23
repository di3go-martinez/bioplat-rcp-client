package edu.unlp.medicine.bioplat.rcp.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Observable;
import java.util.Observer;

import edu.unlp.medicine.bioplat.rcp.editor.Dirtable;

/**
 * 
 * @author diego
 * @deprecated Usar AbstractEntity
 */
@Deprecated
public class Model extends Observable implements Dirtable, Observer {
	private boolean dirty = false;

	@Override
	public boolean isDirty() {
		return dirty;
	}

	@Override
	public void touch() {
		dirty = true;
	}

	@Override
	public void clear() {
		dirty = false;
		addPropertyChangeListener();
	}

	public Model() {
		clear();
	}

	public void addPropertyChangeListener(PropertyChangeListener p) {
		pcs.addPropertyChangeListener(p);
	}

	private void addPropertyChangeListener() {
		addPropertyChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				touch();
				// para que se ejecute una sola vez
				// pcs.removePropertyChangeListener(this);
				setChanged();
				notifyObservers();
			}
		});
	}

	public void addPropertyChangeListener(String propertyName,
			PropertyChangeListener listener) {
		pcs.addPropertyChangeListener(propertyName, listener);
	}

	public void removePropertyChangeListener(String propertyName,
			PropertyChangeListener listener) {
		pcs.removePropertyChangeListener(propertyName, listener);
	}

	protected void firePropertyChange(String propertyName, Object oldValue,
			Object newValue) {
		pcs.firePropertyChange(propertyName, oldValue, newValue);

	}

	/**
	 * Fire Property Change
	 */
    protected final void fpc(String propertyName, Object oldValue, Object newValue) {
		firePropertyChange(propertyName, oldValue, newValue);
	}

	private PropertyChangeSupport pcs = new PropertyChangeSupport(this);

	@Override
	public void update(Observable o, Object arg) {
		firePropertyChange("", null, new Object()); // fuerza un cambio
	}

}
