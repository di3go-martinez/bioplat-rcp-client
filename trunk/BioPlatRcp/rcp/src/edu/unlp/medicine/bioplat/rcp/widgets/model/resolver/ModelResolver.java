package edu.unlp.medicine.bioplat.rcp.widgets.model.resolver;

import edu.unlp.medicine.entity.generic.AbstractEntity;

//TODO no está en uso todavía... esta hecha por si no se conoce el modelo a la hora de instanciar un widget... puede pasar???
public interface ModelResolver<M extends AbstractEntity> {

	M model();
}
