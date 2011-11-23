package edu.unlp.medicine.bioplat.rcp.widgets.model.resolver;

import edu.unlp.medicine.bioplat.rcp.model.Model;

//TODO no está en uso todavía... esta hecha por si no se conoce el modelo a la hora de instanciar un widget... puede pasar???
public interface ModelResolver<M extends Model> {

	M model();
}
