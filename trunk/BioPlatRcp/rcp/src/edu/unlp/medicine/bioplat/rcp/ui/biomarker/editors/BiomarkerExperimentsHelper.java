/**
 * 
 */
package edu.unlp.medicine.bioplat.rcp.ui.biomarker.editors;

import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import edu.unlp.medicine.bioplat.rcp.editor.ModelProvider;
import edu.unlp.medicine.bioplat.rcp.ui.biomarker.exports.MevWizard;
import edu.unlp.medicine.bioplat.rcp.ui.entities.DialogModel;
import edu.unlp.medicine.bioplat.rcp.ui.entities.EditorsId;
import edu.unlp.medicine.bioplat.rcp.ui.experiment.actions.contributions.ConfigureClusterDialog;
import edu.unlp.medicine.bioplat.rcp.ui.experiment.actions.contributions.ScriptDialog;
import edu.unlp.medicine.bioplat.rcp.ui.experiment.editors.AppliedExperimentEditor;
import edu.unlp.medicine.bioplat.rcp.ui.utils.Provider;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableReference;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.bioplat.rcp.widgets.Widgets;
import edu.unlp.medicine.domainLogic.framework.metasignatureGeneration.validation.Validation;
import edu.unlp.medicine.entity.experiment.Experiment;
import edu.unlp.medicine.entity.experiment.ExperimentAppliedToAMetasignature;
import edu.unlp.medicine.entity.experiment.Sample;
import edu.unlp.medicine.entity.experiment.exception.ExperimentBuildingException;

/**
 * @author Juan
 * 
 */
public class BiomarkerExperimentsHelper implements Observer {

	private static final String CLUSTERING_PNG = "clustering.png";
	// indica si se tiene que inicializar
	private boolean mustinitialize = true;

	private Provider<List<Validation>> dataProvider;
	private TableReferenceProvider2 provider;

	// Ã­ndice de la columna a la cual se le agregan las nuevas columnas
	private int newBaseColumnIndex;
	private int exportColumnIndex;
	private int viewClusterColIndex;
	private int kaplanMeierColIndex;
	private int viewOriginalExperimentIndex;
	private int exportRScriptIndex;

	/**
	 * 
	 * @param provider
	 * @param dataProvider
	 */
	public BiomarkerExperimentsHelper(TableReferenceProvider2 provider,
			Provider<List<Validation>> dataProvider) {
		this.provider = provider;
		this.dataProvider = dataProvider;
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		refreshView();
	}

	public void refreshView() {
		final List<Validation> eas = this.dataProvider.get();
		TableReference tr = this.provider.tableReference();
		// FIXME Horrible esto... "tapar" en el ColumnBuilder...
		Table table = tr.getTable();
		removeAllFromTable(table);
		tr.input(eas);
		initialize(table);
		// para cada item de agrega el table editor
		// FIXME no es necesario pisar los que ya estÃ¡n creados...
		TableItem[] items = table.getItems();
		for (int i = 0; i < items.length; i++) {

			final Validation exp = eas.get(i);
			TableEditor editor;
			Button c;

			editor = new TableEditor(table);
			c = new Button(table, SWT.FLAT);
			c.setImage(PlatformUIUtils.findImage("View result details.16.png"));
			c.addSelectionListener(openExperimentAppliedDialog(exp));
			editor.grabHorizontal = true;
			editor.setEditor(c, items[i], this.kaplanMeierColIndex);
			
			editor = new TableEditor(table);
			c = new Button(table, SWT.FLAT);
			c.setImage(PlatformUIUtils.findImage("clustering.png"));
			c.addSelectionListener(openViewClusterDialog(exp
					.getClusteringResult().getClustersOfEachSample()));
			editor.grabHorizontal = true;
			editor.setEditor(c, items[i], this.viewClusterColIndex);
			
			
			try {
				editor = new TableEditor(table);
				c = new Button(table, SWT.FLAT);
				c.setImage(PlatformUIUtils
						.findImage("Open original experiment.png"));
				c.addSelectionListener(this.openCurrentExperiment(exp));
				editor.grabHorizontal = true;
				editor.setEditor(c, items[i], this.viewOriginalExperimentIndex);
			} catch (ExperimentBuildingException e) {

				e.printStackTrace();
			}

			editor = new TableEditor(table);
			c = new Button(table, SWT.FLAT);
			c.setImage(PlatformUIUtils
					.findImage("Export experiment used for validation.16.png"));
			c.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					//Revisar el tercer parametro. Chusmear new MevWizard(survCompValidationResult.getSurvCompValidationConfig().getExperimentToValidate(), true, survCompValidationResult.itUsesManualPredefinedCluster()).blockOnOpen().open();
					new MevWizard(exp.getValidationConfig()
							.getExperimentToValidate(), false, false).blockOnOpen()
							.open();
				}
			});
			editor.grabHorizontal = true;
			editor.setEditor(c, items[i], this.exportColumnIndex);
			
			
			editor = new TableEditor(table);
			c = new Button(table, SWT.FLAT);
			c.setImage(PlatformUIUtils
					.findImage("rlogo.png"));
			c.addSelectionListener(
					openViewScriptDialog(exp.getScript()));
			editor.grabHorizontal = true;
			editor.setEditor(c, items[i], this.exportRScriptIndex);
			
			
			
		}

	}

	
	// TODO: No me convence hacer esto
	private void removeAllFromTable(Table table) {
		Control[] controls = table.getChildren();
		for(Control ctr : controls){
			if(ctr != null){
				ctr.dispose();	
			}
		}
		table.removeAll();
	}



	private void initialize(Table table) {
		if (this.mustinitialize) {
			
			this.newBaseColumnIndex = table.getColumnCount();
			
			this.kaplanMeierColIndex = this.newBaseColumnIndex;
			createTableColumn(table, 120, "Statistic and Graphics", this.kaplanMeierColIndex);
			
			this.viewClusterColIndex = this.newBaseColumnIndex + 1;
			createTableColumn(table, 110, "View Used Cluster",
					this.viewClusterColIndex);

			this.viewOriginalExperimentIndex = this.newBaseColumnIndex + 2;
			createTableColumn(table, 160, "Open Original Experiment",
					this.viewOriginalExperimentIndex);
			
			
			this.exportColumnIndex = this.newBaseColumnIndex + 3;
			createTableColumn(table, 200, "Export Gene Signature Data Matrix",
					this.exportColumnIndex);
			
			this.exportRScriptIndex = this.newBaseColumnIndex + 4;
			createTableColumn(table, 110, "Copy R Script",
					this.exportRScriptIndex);
			


			// ok, ya inicializado
			this.mustinitialize = false;
		}
	}

	/**
	 * 
	 * @param table
	 * @param width
	 * @param text
	 * @param index
	 */
	private void createTableColumn(Table table, int width, String text,
			int index) {
		TableColumn tc;
		tc = new TableColumn(table, SWT.NONE, index);
		tc.setWidth(width);
		tc.setText(text);
	}

	/**
	 * 
	 * @param validation
	 * @return
	 */
	private SelectionListener openCurrentExperiment(final Validation validation) {
		return new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				try {
					PlatformUIUtils.openEditor(validation.getValidationConfig()
							.getExperimentToValidate(), EditorsId
							.experimentEditorId());
				} catch (Exception e1) {
					e1.printStackTrace();
				}

			}
		};
	}

	/**
	 * 
	 * @param groups
	 * @return
	 */
	private SelectionListener openViewClusterDialog(
			final Map<Sample, Integer> groups) {
		return new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				new ConfigureClusterDialog(groups).open();
			}
		};
	}
	
	
	
	private SelectionListener openViewScriptDialog(
			final String script) {
		return new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				new ScriptDialog(script).open();
			}
		};
	}
	
	
	
	//Metodo para el muestreo de graficas de Kaplan-Meier
	protected SelectionAdapter openExperimentAppliedDialog(final Validation exp) {
		return new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				final Shell shell = PlatformUIUtils.findShell();
				Dialog dialog = new DialogModel<Validation>(shell, new ModelProvider<Validation>() {

					@Override
					public Validation model() {
						return exp;
					}
				}) {
					@Override
					protected Control createDialogArea(Composite parent) {
						final Composite container = Widgets.createDefaultContainer((Composite) super.createDialogArea(parent));
						AppliedExperimentEditor.makeView(container, model());
						return container;
					}

					@Override
					protected Point getInitialSize() {
						return new Point(800, 600);
					}

					@Override
					protected Point getInitialLocation(Point initialSize) {
						return PlatformUIUtils.centerPointFor(this.getShell());
					}

					@Override
					protected void configureShell(Shell newShell) {
						super.configureShell(newShell);
						newShell.setText("Statistic and Graphics");
					}
				};
				dialog.create();

				dialog.open();
			}
		};
	}

}
