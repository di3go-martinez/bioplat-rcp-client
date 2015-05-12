/**
 * 
 */
package edu.unlp.medicine.bioplat.rcp.ui.biomarker.editors;

import java.util.List;
import java.util.Observer;

import org.eclipse.swt.widgets.Composite;

import edu.unlp.medicine.bioplat.rcp.editor.AbstractEditorPart;
import edu.unlp.medicine.bioplat.rcp.ui.utils.Provider;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableReference;
import edu.unlp.medicine.domainLogic.framework.metasignatureGeneration.validation.Validation;
import edu.unlp.medicine.entity.biomarker.Biomarker;

/**
 * @author Juan
 * 
 */
public class BiomarkerExperimentsEditor extends AbstractEditorPart<Biomarker>
		implements TableReferenceProvider2 {

	private TableReference tr;
	private BiomarkerExperimentsHelper helper;
	private GuiMaker guiMaker;

	public BiomarkerExperimentsEditor(boolean updatableTitle) {
		super(updatableTitle);
	}

	@Override
	public void setTableReference(TableReference tr) {
		this.tr = tr;
	}

	@Override
	public TableReference tableReference() {
		return this.tr;
	}

	@Override
	protected void doCreatePartControl(Composite parent) {
		this.guiMaker = new BiomarkerExperimetnsGUIMaker(this.getHelper(), this);
		this.guiMaker.build(parent);

	}

	@Override
	protected Observer createModificationObserver() {
		return getHelper();
	}

	protected BiomarkerExperimentsHelper getHelper() {
		if (this.helper == null)
			this.helper = new BiomarkerExperimentsHelper(this,
					new Provider<List<Validation>>() {

						@Override
						public List<Validation> get() {
							return model().getValidations();
						}
					});
		return this.helper;
	}

}
