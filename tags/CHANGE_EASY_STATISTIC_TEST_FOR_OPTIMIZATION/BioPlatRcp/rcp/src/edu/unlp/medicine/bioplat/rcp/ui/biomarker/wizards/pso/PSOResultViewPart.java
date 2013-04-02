package edu.unlp.medicine.bioplat.rcp.ui.biomarker.wizards.pso;

import java.util.Collections;
import java.util.List;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.part.ViewPart;

import edu.unlp.medicine.bioplat.rcp.ui.entities.EditorsId;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.ColumnBuilder;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableBuilder;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableBuilder.MenuBuilder;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableReference;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.menues.MenuItemContribution;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.bioplat.rcp.widgets.Widgets;
import edu.unlp.medicine.domainLogic.framework.optimizers.BiomarkerOptimizationResult;
import edu.unlp.medicine.entity.biomarker.Biomarker;

/**
 * @author Diego Martínez
 */
public class PSOResultViewPart extends ViewPart {

	private BiomarkerOptimizationResult result;
	private TableReference tref;
	Composite parent;

	@Override
	public void createPartControl(Composite parent) {
		this.parent = parent;
	}



	private MenuBuilder createMenuBuilder() {
		return new MenuBuilder() {

			@Override
			public void build(Menu menu) {
				Image openImage = PlatformUIUtils.findImage("openSelection.gif");
				MenuItemContribution.create(menu).image(openImage).text("Open Selection").addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						// intenta abrir los seleccionados o si no hay
						// seleccionados, abre el que tenga el foco
						List<Biomarker> biomarkers = tref.selectedElements();
						if (biomarkers.isEmpty())
							biomarkers = tref.focusedElements();
						for (Biomarker b : biomarkers)
							PlatformUIUtils.openEditor(b, EditorsId.biomarkerEditorId());
					}
				});
			}
		};
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	public static String id() {
		return "edu.unlp.medicine.bioplat.rcp.ui.biomarker.wizards.pso.result.view";
	}

	
	Composite actualContainer;
	public void setResultToShow(BiomarkerOptimizationResult result) {
		if (actualContainer!=null) actualContainer.dispose();
		this.result = result;
		Composite container = Widgets.createDefaultContainer(parent);
		actualContainer=container;
		
		
		TableBuilder tableBuilder = TableBuilder.create(container).input(result.getBettersTouchedDuringTheTrip()).addColumn(ColumnBuilder.create().property("name").title("Gene Signature name").width(200));
		tableBuilder.addColumn(ColumnBuilder.create().numeric().property("numberOfGenes").title("Number of Genes"));//
		tableBuilder.addColumn(ColumnBuilder.create().numeric().property("validationManager.logRankTestValidationResults[0].significanceValue.pvalue").title("Training set result").width(200));//

		
		 //TableBuilder tableBuilder = TableBuilder.create(container).input(list).addColumn(ColumnBuilder.create().property("name").title("Name")).addColumn(ColumnBuilder.create().numeric().property("validationManager.logRankTestValidationResults[0].significanceValue.pvalue").title("Training set result").width(100));//
				
		if (result.getBettersTouchedDuringTheTrip().get(0).getValidationManager().getLogRankTestValidationResults().size() >=2) tableBuilder.addColumn(ColumnBuilder.create().numeric().property("validationManager.logRankTestValidationResults[1].significanceValue.pvalue").title("Testing set result").width(200));//
		if (result.getBettersTouchedDuringTheTrip().get(0).getValidationManager().getLogRankTestValidationResults().size() >=3) tableBuilder.addColumn(ColumnBuilder.create().numeric().property("validationManager.logRankTestValidationResults[2].significanceValue.pvalue").title("Validation set result").width(200));//
				//.addColumn(ColumnBuilder.create().numeric().property("significanceValue.pvalue").title("p-value"))//
		tableBuilder.addColumn(ColumnBuilder.create().numeric().property("numberOfGenes").title("Genes"));//
				
		tref = tableBuilder.contextualMenuBuilder(createMenuBuilder()).build();
		
		
		parent.layout();
		
	}

}