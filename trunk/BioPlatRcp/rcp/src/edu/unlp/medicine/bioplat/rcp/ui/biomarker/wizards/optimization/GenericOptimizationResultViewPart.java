package edu.unlp.medicine.bioplat.rcp.ui.biomarker.wizards.optimization;

import java.util.List;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.part.ViewPart;

import edu.unlp.medicine.bioplat.rcp.ui.entities.EditorsId;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.ColumnBuilder;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableBuilder;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableReference;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableBuilder.MenuBuilder;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.menues.MenuItemContribution;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.bioplat.rcp.widgets.Widgets;
import edu.unlp.medicine.config.StatisticConfig;
import edu.unlp.medicine.domainLogic.ext.metasignatureCommands.optimizers.blindSearch.BlindSearchOptimizerCommand;
import edu.unlp.medicine.domainLogic.framework.metasignatureGeneration.validation.ValidationConfig4DoingCluster;
import edu.unlp.medicine.domainLogic.framework.optimizers.BiomarkerOptimizationResult;
import edu.unlp.medicine.entity.biomarker.Biomarker;

public class GenericOptimizationResultViewPart extends ViewPart{
	
	private List<Biomarker> betters;
	private TableReference tref;
	
	private ValidationConfig4DoingCluster forTesting;
	ValidationConfig4DoingCluster forTraining;
	ValidationConfig4DoingCluster forValidation;
	
	BlindSearchOptimizerCommand command;
	
	Composite parent;

	@Override
	public void createPartControl(Composite parent) {
		
		this.parent = parent;
		createContents(parent);
		
	}

	private void createContents(Composite parent) {
		
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

	

	Composite actualContainer;
	public void setResultToShow(List<Biomarker> result) {
		if (actualContainer!=null) actualContainer.dispose();
		
		this.betters = result;
		Composite container = Widgets.createDefaultContainer(parent);
		
		actualContainer=container;
		
		
		TableBuilder tableBuilder = TableBuilder.create(container).input(betters).addColumn(ColumnBuilder.create().property("name").title("Gene Signature name").width(200));
		tableBuilder.addColumn(ColumnBuilder.create().numeric().property("numberOfGenes").title("Number of Genes"));//
		//tableBuilder.addColumn(ColumnBuilder.create().numeric().property("validationManager.logRankTestValidationResults[0].significanceValue.pvalue").title("Training set result").width(200));//

		addTrainingColumn(tableBuilder);
		addTestingColumn(tableBuilder);
		addValidationColumn(tableBuilder);
		
		//if (betters.get(0).getValidationManager().getLogRankTestValidationResults().size() >=2) tableBuilder.addColumn(ColumnBuilder.create().numeric().property("validationManager.logRankTestValidationResults[1].significanceValue.pvalue").title("Testing set result").width(200));//
		//if (betters.get(0).getValidationManager().getLogRankTestValidationResults().size() >=3) tableBuilder.addColumn(ColumnBuilder.create().numeric().property("validationManager.logRankTestValidationResults[2].significanceValue.pvalue").title("Validation set result").width(200));//
				//.addColumn(ColumnBuilder.create().numeric().property("significanceValue.pvalue").title("p-value"))//
		
				
		tref = tableBuilder.contextualMenuBuilder(createMenuBuilder()).build();
		//actualContainer.layout();
		parent.layout();
	}

	private void addValidationColumn(TableBuilder tableBuilder) {
		String title = "Validation (" + StatisticConfig.getDescription() + ")";
		
		ColumnBuilder columnBuilder = ColumnBuilder.create().numeric().title(title).width(150);
		
		if (forValidation!=null){
				
		columnBuilder.labelprovider(new ColumnLabelProvider() {
			@Override
			public String getText(Object biomarker) {
				return String.valueOf(StatisticConfig.getBiomarkerFitness((Biomarker)biomarker, forValidation));
			}

		});
		}
		
		tableBuilder.addColumn(columnBuilder);
		
		
	}

	private void addTestingColumn(TableBuilder tableBuilder) {
		String title = "Testing (" + StatisticConfig.getDescription() + ")";
		ColumnBuilder columnBuilder = ColumnBuilder.create().numeric().title(title).width(150);
		
		if (forTesting!=null){
				
		columnBuilder.labelprovider(new ColumnLabelProvider() {
			@Override
			public String getText(Object biomarker) {
				return String.valueOf(StatisticConfig.getBiomarkerFitness((Biomarker)biomarker, forTesting));
			}

		});
		}
		
		tableBuilder.addColumn(columnBuilder);
		
	}

	private void addTrainingColumn(TableBuilder tableBuilder) {
		String title = "Training (" + StatisticConfig.getDescription() + ")";
		tableBuilder.addColumn(ColumnBuilder.create().numeric().title(title).width(150).labelprovider(new ColumnLabelProvider() {
			@Override
			public String getText(Object biomarker) {
				return String.valueOf(StatisticConfig.getBiomarkerFitness((Biomarker)biomarker, forTraining));
			}

		}));


		
	}

	public ValidationConfig4DoingCluster getForTesting() {
		return forTesting;
	}

	public void setForTesting(ValidationConfig4DoingCluster forTesting) {
		this.forTesting = forTesting;
	}

	public ValidationConfig4DoingCluster getForTraining() {
		return forTraining;
	}

	public void setForTraining(ValidationConfig4DoingCluster forTraining) {
		this.forTraining = forTraining;
	}

	public ValidationConfig4DoingCluster getForValidation() {
		return forValidation;
	}

	public void setForValidation(ValidationConfig4DoingCluster forValidation) {
		this.forValidation = forValidation;
	}


//	
//	@Override
//	public void selectionChanged(SelectionChangedEvent event) {
//		System.out.println("LLEGOOOOOOS");
//		
//	}

	public BlindSearchOptimizerCommand getCommand() {
		return command;
	}

//	public void setCommand(BlindSearchOptimizerCommand command) {
//		command.addPropertyChangeListener(new PropertyChangeListener() {
//			
//			@Override
//			public void propertyChange(PropertyChangeEvent evt) {
//				System.out.println("SIIIIIIII");
//				
//			}
//		});
//		this.command = command;
//	}
//	
	
	
	


}
