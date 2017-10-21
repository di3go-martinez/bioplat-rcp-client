package edu.unlp.medicine.bioplat.rcp.ui.biomarker.wizards.optimization;

import static edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableBuilder.*;
import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.part.ViewPart;

import edu.unlp.medicine.bioplat.rcp.ui.entities.EditorsId;
import edu.unlp.medicine.bioplat.rcp.ui.utils.accesors.ReadOnlyAccesor;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.ColumnBuilder;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableBuilder;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableBuilder.MenuBuilder;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableReference;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.menues.MenuItemContribution;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.bioplat.rcp.widgets.Widgets;
import edu.unlp.medicine.config.StatisticConfig;
import edu.unlp.medicine.domainLogic.ext.metasignatureCommands.optimizers.blindSearch.BlindSearchOptimizerCommand;
import edu.unlp.medicine.domainLogic.framework.metasignatureGeneration.validation.ValidationConfig4DoingCluster;
import edu.unlp.medicine.entity.biomarker.Biomarker;

public class GenericOptimizationResultViewPart extends ViewPart implements Observer {

	private List<Biomarker> betters;
	private TableReference tref;

	private ValidationConfig4DoingCluster forTraining;
	private Optional<ValidationConfig4DoingCluster> forTesting = Optional.empty();
	private Optional<ValidationConfig4DoingCluster> forValidation = Optional.empty();

	private BlindSearchOptimizerCommand command;

	private Composite parent;

	@Override
	public void createPartControl(Composite parent) {
		this.parent = parent;
		createContents(parent);
	}

	private void createContents(Composite parent) {
		final List<Biomarker> emptyList = Collections.emptyList();
		createTable(emptyList);
	}

	private MenuBuilder createMenuBuilder() {
		return new MenuBuilder() {

			@Override
			public void build(Menu menu) {
				Image openImage = PlatformUIUtils.findImage("openSelection.gif");
				MenuItemContribution.create(menu).image(openImage).text("Open Selection")
						.addSelectionListener(createOpenSelectionListener());
			}

			private SelectionAdapter createOpenSelectionListener() {
				return new SelectionAdapter() {
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
				};
			}
		};
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	private Composite actualContainer;

	public void setResultToShow(List<Biomarker> result) {
		createTable(result);
	}

	protected void createTable(List<Biomarker> result) {
		if (actualContainer != null)
			actualContainer.dispose();

		this.betters = result;
		Composite container = Widgets.createDefaultContainer(parent);

		actualContainer = container;

		TableBuilder tableBuilder = tableBuilder(container).input(betters).//
				addColumn(ColumnBuilder.create().property("name").title("Gene Signature name").width(200)).//
				addColumn(ColumnBuilder.create().numeric().property("numberOfGenes").title("Number of Genes"));//
		
		addTrainingColumn(tableBuilder);
		addTestingColumn(tableBuilder);
		addValidationColumn(tableBuilder);

		// if
		// (betters.get(0).getValidationManager().getLogRankTestValidationResults().size()
		// >=2)
		// tableBuilder.addColumn(ColumnBuilder.create().numeric().property("validationManager.logRankTestValidationResults[1].significanceValue.pvalue").title("Testing
		// set result").width(200));//
		// if
		// (betters.get(0).getValidationManager().getLogRankTestValidationResults().size()
		// >=3)
		// tableBuilder.addColumn(ColumnBuilder.create().numeric().property("validationManager.logRankTestValidationResults[2].significanceValue.pvalue").title("Validation
		// set result").width(200));//
		// .addColumn(ColumnBuilder.create().numeric().property("significanceValue.pvalue").title("p-value"))//

		tref = tableBuilder.contextualMenuBuilder(createMenuBuilder()).build();
		// actualContainer.layout();
		parent.layout();
	}

	private void addValidationColumn(TableBuilder tableBuilder) {
		String title = "Validation (" + StatisticConfig.getDescription() + ")";

		ColumnBuilder columnBuilder = ColumnBuilder.create().numeric().title(title).width(180);

		columnBuilder.accesor(new ReadOnlyAccesor() {

			@Override
			public Object get(Object element) {
				if (forValidation.isPresent())
					return StatisticConfig.getBiomarkerFitness((Biomarker) element, forValidation.get());
				else
					return "N/A";
			}
		});

		tableBuilder.addColumn(columnBuilder);

	}

	private void addTestingColumn(TableBuilder tableBuilder) {
		String title = "Testing (" + StatisticConfig.getDescription() + ")";
		ColumnBuilder columnBuilder = ColumnBuilder.create().numeric().title(title).width(180);

		columnBuilder.accesor(new ReadOnlyAccesor() {

			@Override
			public Object get(Object biomarker) {
				if (forTesting.isPresent())
					return StatisticConfig.getBiomarkerFitness((Biomarker) biomarker, forTesting.get());
				else
					return "N/A";
			}
		});

		tableBuilder.addColumn(columnBuilder);

	}

	private void addTrainingColumn(TableBuilder tableBuilder) {
		String title = "Training (" + StatisticConfig.getDescription() + ")";
		tableBuilder.addColumn(ColumnBuilder.create().numeric().title(title).width(180).accesor(new ReadOnlyAccesor() {

			@Override
			public Object get(Object element) {
				return StatisticConfig.getBiomarkerFitness((Biomarker) element, forTraining);
			}
		}));

	}

	
	public void setForTesting(ValidationConfig4DoingCluster forTesting) {
		this.forTesting = Optional.ofNullable(forTesting);
	}


	public void setForTraining(ValidationConfig4DoingCluster forTraining) {
		this.forTraining = forTraining;
	}

	

	public void setForValidation(ValidationConfig4DoingCluster forValidation) {
		this.forValidation = Optional.ofNullable(forValidation);
	}

	public BlindSearchOptimizerCommand getCommand() {
		return command;
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub

	}

	public void updateResults(List<Biomarker> betters) {
		tref.input(betters);
	}
}
