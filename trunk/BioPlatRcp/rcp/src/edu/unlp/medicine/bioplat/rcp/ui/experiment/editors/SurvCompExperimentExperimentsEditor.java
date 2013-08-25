package edu.unlp.medicine.bioplat.rcp.ui.experiment.editors;

import java.util.List;
import java.util.Observer;

import org.eclipse.swt.widgets.Composite;

import edu.unlp.medicine.bioplat.rcp.editor.AbstractEditorPart;
import edu.unlp.medicine.bioplat.rcp.ui.biomarker.editors.GuiMaker;
import edu.unlp.medicine.bioplat.rcp.ui.biomarker.editors.SurvCompGUIMaker;
import edu.unlp.medicine.bioplat.rcp.ui.biomarker.editors.SurvCompHelper;
import edu.unlp.medicine.bioplat.rcp.ui.biomarker.editors.TableReferenceProvider2;
import edu.unlp.medicine.bioplat.rcp.ui.utils.Provider;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableReference;
import edu.unlp.medicine.domainLogic.framework.metasignatureGeneration.validation.SurvCompValidationResult;
import edu.unlp.medicine.entity.experiment.Experiment;

public class SurvCompExperimentExperimentsEditor extends AbstractEditorPart<Experiment> implements TableReferenceProvider2 {
	private TableReference tr;
	private SurvCompHelper helper;
	private GuiMaker guiMaker;

	public SurvCompExperimentExperimentsEditor(boolean b) {
		super(b);
	}

	@Override
	protected void doCreatePartControl(Composite parent) {
		guiMaker = new SurvCompGUIMaker(this, getHelper(), model().getSurvCompValidationResults());
		guiMaker.build(parent);
	}

	@Override
	protected Observer createModificationObserver() {
		return getHelper();
	}

	protected SurvCompHelper getHelper() {
		if (helper == null)
			helper = new SurvCompHelper(this, new Provider<List<SurvCompValidationResult>>() {

				@Override
				public List<SurvCompValidationResult> get() {
					return model().getSurvCompValidationResults();
				}
			});
		return helper;
	}

	@Override
	public void setTableReference(TableReference tr) {
		this.tr = tr;
	}

	@Override
	public TableReference tableReference() {
		return tr;
	}
}
