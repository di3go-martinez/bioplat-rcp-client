package edu.unlp.medicine.bioplat.rcp.ui.biomarker.editors;

import java.util.Observable;
import java.util.Observer;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import edu.unlp.medicine.bioplat.rcp.editor.AbstractEditorPart;
import edu.unlp.medicine.bioplat.rcp.ui.experiment.editors.AppliedExperimentEditor;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.ColumnBuilder;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableBuilder;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableReference;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.entity.biomarker.Biomarker;

public class BiomarkerAppliedExperimentsEditor extends AbstractEditorPart<Biomarker> {

	private TableReference tr;

	@Override
	protected void doCreatePartControl(Composite parent) {
		Composite c = new Composite(parent, SWT.NONE);
		c.setLayout(GridLayoutFactory.fillDefaults().numColumns(1).margins(10, 10).create());
		c.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());

		createButtonPart(c);
		createWorkAreaPart(c);

	}

	private void createWorkAreaPart(Composite parent) {
		tr = TableBuilder.create(parent).input(model().getExperimentsApplied())//
				// .model(model(), "experiemntsApplied")//
				.addColumn(ColumnBuilder.create().property("name").title("Experiment"))//
				.addColumn(ColumnBuilder.create().property("significanceValue.pvalue").title("p-value"))//
				.build();
	}

	private void createButtonPart(Composite parent) {
		Composite bc = new Composite(parent, SWT.NONE);
		bc.setLayout(new FillLayout(SWT.HORIZONTAL | SWT.END));

		Button b = new Button(bc, SWT.NONE);
		b.setText("Abrir seleccionados");
		b.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				for (Object o : tr.selectedElements())
					// TODO abrir el editor de experimentos aplicados, cuando
					// esté creado...
					PlatformUIUtils.openEditor(o, AppliedExperimentEditor.id());
			}
		});

	}

	@Override
	protected Observer createModificationObserver() {
		return new Observer() {

			@Override
			public void update(Observable o, Object arg) {
				tr.input(model().getExperimentsApplied());
			}

		};
	}

}
