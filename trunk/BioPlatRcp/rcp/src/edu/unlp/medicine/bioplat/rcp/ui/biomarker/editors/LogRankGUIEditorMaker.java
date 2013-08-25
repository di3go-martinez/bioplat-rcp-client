package edu.unlp.medicine.bioplat.rcp.ui.biomarker.editors;

import java.util.List;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.ColumnBuilder;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableBuilder;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableReference;
import edu.unlp.medicine.entity.experiment.ExperimentAppliedToAMetasignature;

public class LogRankGUIEditorMaker extends GuiMaker {

	private LongRankTestHelper helper;
	private TableReferenceProvider2 provider;
	private List<ExperimentAppliedToAMetasignature> expApplied;

	public LogRankGUIEditorMaker(TableReferenceProvider2 provider, LongRankTestHelper helper, List<ExperimentAppliedToAMetasignature> list) {
		this.helper = helper;
		this.provider = provider;
		this.expApplied = list;
	}

	@Override
	public void build(Composite parent) {
		Composite c = new Composite(parent, SWT.NONE);
		c.setLayout(GridLayoutFactory.fillDefaults().numColumns(1).margins(10, 10).create());
		c.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());

		// createButtonPart(c);
		createWorkAreaPart(c);

		helper.refreshView();
	}

	private void createWorkAreaPart(Composite parent) {
		TableReference tr = TableBuilder.create(parent).input(expApplied)//
				// .model(model(), "experiemntsApplied")//
				.hideSelectionColumn()//
				.addColumn(ColumnBuilder.create().property("name").title("Experiment"))//
				.addColumn(ColumnBuilder.create().property("significanceValue.pvalue").title("Log-Rank test p-value").width(160))//
				// TODO .addContextualMenu(new MyMenu())//
				.build();

		provider.setTableReference(tr);
	}
}
