package edu.unlp.medicine.bioplat.rcp.widgets;


import edu.unlp.medicine.bioplat.rcp.widgets.listeners.ModificationListener;
import edu.unlp.medicine.entity.generic.AbstractEntity;

public interface Widget {

	void retarget(AbstractEntity newModel);

	void addModificationListener(ModificationListener listener);
}
