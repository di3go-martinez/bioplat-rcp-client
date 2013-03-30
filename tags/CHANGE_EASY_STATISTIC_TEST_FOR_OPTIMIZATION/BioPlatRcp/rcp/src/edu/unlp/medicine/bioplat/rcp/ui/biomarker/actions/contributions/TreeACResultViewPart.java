package edu.unlp.medicine.bioplat.rcp.ui.biomarker.actions.contributions;

import java.util.Collections;
import java.util.List;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.ColumnBuilder;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableBuilder;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableReference;
import edu.unlp.medicine.bioplat.rcp.widgets.Widgets;
import edu.unlp.medicine.entity.biomarker.Biomarker;

/**
 * @author Diego Martínez
 */
public class TreeACResultViewPart extends ViewPart {

	private List<Biomarker> result;
	private TableReference tref;

	@Override
	public void createPartControl(Composite parent) {
		createContents(parent);
	}

	private void createContents(Composite parent) {
		Composite container = Widgets.createDefaultContainer(parent);

		final List<Biomarker> list;
		if (result != null)
			list = result;
		else
			list = Collections.emptyList();

		tref = TableBuilder.create(container).hideSelectionColumn().input(list) //
				.addColumn(ColumnBuilder.create().property("name").title("Name"))//
				.addColumn(ColumnBuilder.create().numeric().property("significanceValue.pvalue").title("p-value"))//
				.addColumn(ColumnBuilder.create().numeric().property("numberOfGenes").title("Genes"))//
				.build();

	}

	@Override
	public void setFocus() {

	}

	public static String id() {
		return "edu.unlp.medicine.bioplat.rcp.ui.biomarker.wizards.tree.result.view";
	}

	public void setResultToShow(List<Biomarker> result) {
		this.result = result;
		tref.input(result);
	}

}
