package edu.unlp.medicine.bioplat.rcp.ui.biomarker.editors;

import java.util.List;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.ColumnBuilder;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableBuilder;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableReference;
import edu.unlp.medicine.domainLogic.framework.metasignatureGeneration.validation.SurvCompValidationResult;

/**
 * usado por SurvCompExperimentsEditor
 * 
 * @author diego
 * 
 */
public class SurvCompGUIMaker extends GuiMaker {
	private TableReferenceProvider2 provider;
	private SurvCompHelper helper;
	private List<SurvCompValidationResult> eas;

	public SurvCompGUIMaker(TableReferenceProvider2 provider, SurvCompHelper helper, List<SurvCompValidationResult> eas) {
		this.provider = provider;
		this.helper = helper;
		this.eas = eas;

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
		TableReference tr = TableBuilder.create(parent).input(eas)//
				// .model(model(), "experiemntsApplied")//
				.hideSelectionColumn()//
				.addColumn(ColumnBuilder.create().property("experimentName").title("Experiment"))//
				.addColumn(ColumnBuilder.create().property("survCompIndex").title("Concordance index").width(270))//
				// .addColumn(ColumnBuilder.create().property("significanceValue.pvalue").title("Log Rank test p-value").width(160))//
				// TODO .addContextualMenu(new MyMenu())//
				.build();
		provider.setTableReference(tr);

	}
}
