package edu.unlp.medicine.bioplat.rcp.widgets;

import com.google.common.annotations.Beta;

import edu.unlp.medicine.bioplat.rcp.widgets.listeners.ModificationListener;
import edu.unlp.medicine.entity.generic.AbstractEntity;

public interface Widget {

	void retarget(AbstractEntity newModel);

	Widget addModificationListener(ModificationListener listener);

	Widget readOnly();

	@Beta
	Widget setLayoutData(Object layoutData);
}
