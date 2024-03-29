package edu.unlp.medicine.bioplat.rcp.ui.entities.wizards;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;

import com.google.common.annotations.Beta;

import edu.unlp.medicine.bioplat.rcp.utils.wizards.WizardModel;

/**
 * 
 * 
 * Es la descripción de una página de un wizard. Una página tiene una
 * configuración (widgets) y puede originar una página adiciones (seguiente) a
 * modo de resultado de algún cálculo.
 * 
 * 
 * @author Diego Martínez
 */
public abstract class WizardPageDescriptor {

	private static final ImageDescriptor DEFAULT = ImageDescriptor.getMissingImageDescriptor();
	private String name;
	private String title;
	private ImageDescriptor imageDescriptor;
	private String description;
	
	
	//FIXME: david - mejorar esto para que sea mas transparente el pasar de pagina.
	private boolean manualFlip;
	private boolean allowFlip;
	private boolean allowFinish;
	//
	

	public WizardPageDescriptor(String name, String title, ImageDescriptor imageDescriptor) {
		this.name = name;
		this.title = title;
		this.imageDescriptor = imageDescriptor;
	}

	public WizardPageDescriptor(String name) {
		this(name, name, DEFAULT);
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
	public abstract Composite create(WizardPage wizardPage, Composite parent, DataBindingContext dbc, WizardModel wmodel);

	@Override
	public String toString() {
		return "Wizard page " + getPageName();
	}

	public void setTitle(String title){
		this.title = title;
	}
	
	public String getTitle() {
		return title;
	}

	public ImageDescriptor getImageDescriptor() {
		if (imageDescriptor == null)
			imageDescriptor = DEFAULT;
		return imageDescriptor;
	}

	public boolean isPageComplete(WizardModel model) {
		return true;
	}

	/**
	 * Indica si está página origina otra página que sería de resultado, y por
	 * ende, calculada dinámicamente
	 * 
	 * @return
	 */
	// TODO soporte para que pueda cambiar "dinámica", por ahora si se crea en
	// false, luego no podrá cambiar a true, tampoco al reves. Como workaround
	// se propone crearla en true y en la correspondiente página de resultado
	// poner que no se requiere configuración y que siga a la próxima
	public boolean hasResultPage() {
		return false;
	}

	/**
	 * 
	 * Inicializa una página de resultado. Se ejecuta al ingresar la pagina de
	 * resultados. En caso de que no
	 * 
	 * @param parent
	 * @param wizardModel
	 */
	public void initializeResultPage(Composite parent, WizardModel wizardModel, IWizard wizard, WizardPage resultPage) {
		createResultPage(parent, wizardModel, wizard, resultPage);
		refreshResultPage(wizardModel, wizard);
	}

	protected void createResultPage(Composite parent, WizardModel wizardModel, IWizard wizard, WizardPage resultPage) {
		// nada, por default no hay página de resultados a crear
	}

	public void refreshResultPage(WizardModel wizardModel, IWizard wizard) {
		// nada, por default no hay página de resultados a refrescar
	}

	/**
	 * 
	 * por defecto está completa, puede ser redefinido
	 * 
	 * @param wizardModel
	 * @return true si está completa la página de resultado, false si no.
	 */
	public boolean isResultPageComplete(WizardModel wizardModel) {
		return true;
	}

	public WizardPageDescriptor setImageDescriptor(ImageDescriptor imageDescriptor) {

		this.imageDescriptor = imageDescriptor;
		return this;
	}

	// TODO mejorar nombre
	//FIXME no se usa más esto... por ahora...
	@Beta
	public boolean allowContinueWizardSetup() {
		return true; // por default siempre se podría pasar a una página
						// siguiente
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Beta
	protected final void fireUpdateButtons(WizardPage wizardPage) {
		wizardPage.getWizard().getContainer().updateButtons();
		
	}
	/**
	 * Éste metodo se llamará al hacerse visible la pagina (es decir, cuando ingresa)
	 */
	public void doOnEnter(){};
	
	/**
	 * Este metodo se llamará UNICAMENTE al pasar a otra pagina, ya sea la proxima, o la anterior, 
	 * pero no al cancelar
	 */
	public void doOnExit(){}

	public boolean isManualFlip() {
		return manualFlip;
	}


	@Deprecated 
	public void setManualFlip(boolean manualFlip) {
		this.manualFlip = manualFlip;
	};
	
	@Deprecated
	public boolean isAllowFlip() {
		return allowFlip;
	}

	@Deprecated
	public void setAllowFlip(boolean manualFlip) {
		this.allowFlip = manualFlip;
	}

	@Deprecated
	public boolean isAllowFinish() {
		return allowFinish;
	}

	@Deprecated
	public void setAllowFinish(boolean allowFinish) {
		this.allowFinish = allowFinish;
	}

	public boolean performCancel(WizardModel wModel) {
		return true;
	};
	
	
}
