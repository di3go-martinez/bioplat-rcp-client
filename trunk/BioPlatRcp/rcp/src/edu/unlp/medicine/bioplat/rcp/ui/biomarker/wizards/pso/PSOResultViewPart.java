package edu.unlp.medicine.bioplat.rcp.ui.biomarker.wizards.pso;

import edu.unlp.medicine.bioplat.rcp.ui.biomarker.wizards.optimization.GenericOptimizationResultViewPart;

/**
 * @author Diego Martínez
 */
public class PSOResultViewPart extends GenericOptimizationResultViewPart {

	
	public static String id() {
		return "edu.unlp.medicine.bioplat.rcp.ui.biomarker.wizards.pso.result.view";
				
		//return "edu.unlp.medicine.bioplat.rcp.ui.biomarker.wizards.optimization.blindSearch.view";
	}
	
//	private BiomarkerOptimizationResult result;
//	private TableReference tref;
//	Composite parent;
//	
//	
//	private ValidationConfig4DoingCluster forTesting;
//	ValidationConfig4DoingCluster forTraining;
//	ValidationConfig4DoingCluster forValidation;
//
//	@Override
//	public void createPartControl(Composite parent) {
//		this.parent = parent;
//	}
//
//
//
//	private MenuBuilder createMenuBuilder() {
//		return new MenuBuilder() {
//
//			@Override
//			public void build(Menu menu) {
//				Image openImage = PlatformUIUtils.findImage("openSelection.gif");
//				MenuItemContribution.create(menu).image(openImage).text("Open Selection").addSelectionListener(new SelectionAdapter() {
//					@Override
//					public void widgetSelected(SelectionEvent e) {
//						// intenta abrir los seleccionados o si no hay
//						// seleccionados, abre el que tenga el foco
//						List<Biomarker> biomarkers = tref.selectedElements();
//						if (biomarkers.isEmpty())
//							biomarkers = tref.focusedElements();
//						for (Biomarker b : biomarkers)
//							PlatformUIUtils.openEditor(b, EditorsId.biomarkerEditorId());
//					}
//				});
//			}
//		};
//	}
//
//	@Override
//	public void setFocus() {
//		// TODO Auto-generated method stub
//
//	}
//
//
//
//	
//	Composite actualContainer;
//	public void setResultToShow(BiomarkerOptimizationResult result) {
//		if (actualContainer!=null) actualContainer.dispose();
//		this.result = result;
//		Composite container = Widgets.createDefaultContainer(parent);
//		actualContainer=container;
//		
//		
//		TableBuilder tableBuilder = TableBuilder.create(container).input(result.getBettersTouchedDuringTheTrip()).addColumn(ColumnBuilder.create().property("name").title("Gene Signature name").width(200));
//		tableBuilder.addColumn(ColumnBuilder.create().numeric().property("numberOfGenes").title("Number of Genes"));//
//
//		
//		addTrainingColumn(tableBuilder);
//		addTestingColumn(tableBuilder);
//		addValidationColumn(tableBuilder);
//		
//				
//		tref = tableBuilder.contextualMenuBuilder(createMenuBuilder()).build();
//		
//		
//		parent.layout();
//		
//	}
//
//
//
//	public ValidationConfig4DoingCluster getForTesting() {
//		return forTesting;
//	}
//
//
//
//	public void setForTesting(ValidationConfig4DoingCluster forTesting) {
//		this.forTesting = forTesting;
//	}
//
//
//
//	public ValidationConfig4DoingCluster getForTraining() {
//		return forTraining;
//	}
//
//
//
//	public void setForTraining(ValidationConfig4DoingCluster forTraining) {
//		this.forTraining = forTraining;
//	}
//
//
//
//	public ValidationConfig4DoingCluster getForValidation() {
//		return forValidation;
//	}
//
//
//
//	public void setForValidation(ValidationConfig4DoingCluster forValidation) {
//		this.forValidation = forValidation;
//	}
//	
	
	
	

}
