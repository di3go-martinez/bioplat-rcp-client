package edu.unlp.medicine.bioplat.rcp.ui.biomarker.editors;

import java.util.List;
import java.util.Observer;

import org.eclipse.swt.widgets.Composite;

import edu.unlp.medicine.bioplat.rcp.editor.AbstractEditorPart;
import edu.unlp.medicine.bioplat.rcp.ui.utils.Provider;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableReference;
import edu.unlp.medicine.domainLogic.framework.metasignatureGeneration.validation.SurvCompValidationResult;
import edu.unlp.medicine.entity.biomarker.Biomarker;

//TODO sacar este copy&paste de LogRankTestExperimentsEditor!!
public class SurvCompBiomarkerExperimentsEditor extends AbstractEditorPart<Biomarker> implements TableReferenceProvider2 {

	private TableReference tr;
	private SurvCompHelper helper;
	private GuiMaker guiMaker;

	public SurvCompBiomarkerExperimentsEditor(boolean autoUpdatableTitle) {
		super(autoUpdatableTitle);
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
			},false);
		return helper;
	}

	@Override
	public void setTableReference(TableReference tr2) {
		this.tr = tr2;
	}

	@Override
	public TableReference tableReference() {
		return tr;
	}

}
