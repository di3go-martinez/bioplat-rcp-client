package edu.unlp.medicine.bioplat.rcp.ui.biomarker.editors;

import java.util.List;
import java.util.Observer;

import org.eclipse.swt.widgets.Composite;

import edu.unlp.medicine.bioplat.rcp.editor.AbstractEditorPart;
import edu.unlp.medicine.bioplat.rcp.ui.utils.Provider;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableReference;
import edu.unlp.medicine.entity.biomarker.Biomarker;
import edu.unlp.medicine.entity.experiment.ExperimentAppliedToAMetasignature;

public class LogRankTestBiomarkerExperimentsEditor extends AbstractEditorPart<Biomarker> implements TableReferenceProvider2 {

	private TableReference tr;
	private LongRankTestHelper helper;
	private GuiMaker guiMaker;

	public LogRankTestBiomarkerExperimentsEditor(boolean autoUpdatableTitle) {
		super(autoUpdatableTitle);
	}

	@Override
	protected void doCreatePartControl(Composite parent) {
		guiMaker = new LogRankGUIEditorMaker(this, getHelper(), model().getExperimentsApplied());
		guiMaker.build(parent);
	}

	// @Deprecated
	// private void createButtonPart(Composite parent) {
	// Composite bc = new Composite(parent, SWT.NONE);
	// bc.setLayout(new FillLayout(SWT.HORIZONTAL | SWT.END));
	//
	// Button b = new Button(bc, SWT.NONE);
	// b.setText(Messages.open_selected);
	// b.addSelectionListener(new SelectionAdapter() {
	// @Override
	// public void widgetSelected(SelectionEvent e) {
	// for (Object o : tr.selectedElements())
	// PlatformUIUtils.openEditor(o, AppliedExperimentEditor.id());
	// }
	// });
	//
	// }

	@Override
	protected Observer createModificationObserver() {
		return getHelper();
	}

	protected LongRankTestHelper getHelper() {
		if (helper == null)
			helper = new LongRankTestHelper(this, new Provider<List<ExperimentAppliedToAMetasignature>>() {

				@Override
				public List<ExperimentAppliedToAMetasignature> get() {
					return model().getExperimentsApplied();
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
