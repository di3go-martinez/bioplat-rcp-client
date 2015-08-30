/**
 * 
 */
package edu.unlp.medicine.bioplat.rcp.ui.biomarker.editors;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.ColumnBuilder;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableBuilder;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableReference;
import edu.unlp.medicine.domainLogic.framework.metasignatureGeneration.validation.Validation;

/**
 * @author Juan
 * 
 */
public class BiomarkerExperimetnsGUIMaker extends GuiMaker {

	private BiomarkerExperimentsHelper helper;
	private TableReferenceProvider2 provider;
	private List<Validation> list = new ArrayList<Validation>();

	
	
	public BiomarkerExperimetnsGUIMaker(BiomarkerExperimentsHelper helper,
			TableReferenceProvider2 provider) {
		super();
		this.helper = helper;
		this.provider = provider;
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
		TableReference tr = TableBuilder.create(parent).input(list)//
				//.hideSelectionColumn()//
				.addColumn(ColumnBuilder.create().property("name").title("Experiment"))//
				.addColumn(ColumnBuilder.create().property("validationResult.concordance_index_cindex").title("Concordance Index").width(150))
				.addColumn(ColumnBuilder.create().property("validationResult.survdiff_pvalue").title("Log-Rank Test P-value").width(160))				
				.addColumn(ColumnBuilder.create().property("validationResult.roc_AUC").title("ROC AUC").width(150))
				.build();
		provider.setTableReference(tr);
	}

}
