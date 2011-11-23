package edu.unlp.medicine.bioplat.rcp.editor;

/**
 * 
 * @author diego
 * @deprecated usar Dirtable @ ModeloClienteRico
 */
@Deprecated
public interface Dirtable {
	boolean isDirty();

	void touch();

	void clear();
}
