package edu.unlp.medicine.bioplat.rcp.editor;

import edu.unlp.medicine.entity.generic.AbstractEntity;

public interface ModelProvider {
	<T extends AbstractEntity> T model();
}
