/**
 * 
 */
package edu.unlp.medicine.bioplat.rcp.ui.biomarker.editors;

import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import edu.unlp.medicine.bioplat.rcp.ui.biomarker.exports.MevWizard;
import edu.unlp.medicine.bioplat.rcp.ui.entities.EditorsId;
import edu.unlp.medicine.bioplat.rcp.ui.experiment.actions.contributions.ConfigureClusterDialog;
import edu.unlp.medicine.bioplat.rcp.ui.utils.Provider;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableReference;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.domainLogic.framework.metasignatureGeneration.validation.Validation;
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
		tr.input(eas);
		// FIXME Horrible esto... "tapar" en el ColumnBuilder...
		Table table = tr.getTable();
		initialize(table);
		// para cada item de agrega el table editor
		// FIXME no es necesario pisar los que ya estÃ¡n creados...
		TableItem[] items = table.getItems();
		for (int i = 0; i < items.length; i++) {

			final Validation exp = eas.get(i);
			TableEditor editor;
			Button c;

			try {
				editor = new TableEditor(table);
				c = new Button(table, SWT.FLAT);
				c.setImage(PlatformUIUtils
						.findImage("Open original experiment.png"));
				c.addSelectionListener(this.openCurrentExperiment(exp));
				editor.grabHorizontal = true;
				editor.setEditor(c, items[i], this.newBaseColumnIndex);
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
			c.setImage(PlatformUIUtils.findImage("clustering.png"));
			c.addSelectionListener(openViewClusterDialog(exp
					.getClusteringResult().getClustersOfEachSample()));
			editor.grabHorizontal = true;
			editor.setEditor(c, items[i], this.viewClusterColIndex);
		}

	}

	private void initialize(Table table) {
		if (this.mustinitialize) {

			this.newBaseColumnIndex = table.getColumnCount();
			createTableColumn(table, 150, "Open Original Experiment",
					this.newBaseColumnIndex);

			this.exportColumnIndex = this.newBaseColumnIndex + 1;
			createTableColumn(table, 200, "Export gene signature data matrix",
					this.exportColumnIndex);

			this.viewClusterColIndex = this.newBaseColumnIndex + 2;
			createTableColumn(table, 100, "View used cluster",
					this.viewClusterColIndex);

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

}
