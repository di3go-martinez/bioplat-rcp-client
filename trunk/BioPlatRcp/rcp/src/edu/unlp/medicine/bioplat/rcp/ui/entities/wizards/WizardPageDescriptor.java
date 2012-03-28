package edu.unlp.medicine.bioplat.rcp.ui.entities.wizards;

import javax.annotation.Nullable;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;

import edu.unlp.medicine.bioplat.rcp.utils.wizards.WizardModel;

public abstract class WizardPageDescriptor {

	private String name;

	public WizardPageDescriptor(String name) {
		this.name = name;
	}

	public String getPageName() {
		return name;
	};

	/**
	 * 
	 * Create el composite para una página del wizard <code>wizardPage</code>
	 * 
	 * @param wizardPage
	 *            es la página para la cual se creará el composite. Notar que
	 *            puede ser null
	 * @param parent
	 *            donde se crearán los controles
	 * @param dbc
	 *            Es el contexto actual para la configuración de bindings entre
	 *            el model y la vista
	 * @param wmodel
	 *            es el model del wizard, donde se registran las propiedades que
	 *            luego se accederán, en principio, desde el wizard "contenedor"
	 *            en el proceso de "finish()"
	 * @return el composite creado
	 */
	public abstract Composite create(@Nullable WizardPage wizardPage, Composite parent, DataBindingContext dbc, WizardModel wmodel);

	@Override
	public String toString() {
		return "Wizard page " + getPageName();
	}

	public boolean isPageComplete(WizardModel model) {
		return true;
	}

}
