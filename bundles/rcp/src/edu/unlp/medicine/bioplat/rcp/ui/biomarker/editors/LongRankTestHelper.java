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
import edu.unlp.medicine.bioplat.rcp.ui.experiment.editors.AppliedExperimentEditor;
import edu.unlp.medicine.bioplat.rcp.ui.utils.Provider;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableReference;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.bioplat.rcp.widgets.Widgets;
import edu.unlp.medicine.domainLogic.framework.metasignatureGeneration.validation.SurvCompValidationResult;
import edu.unlp.medicine.domainLogic.framework.statistics.hierarchichalClustering.ClusteringResult;
import edu.unlp.medicine.entity.experiment.ClusterData;
import edu.unlp.medicine.entity.experiment.ExperimentAppliedToAMetasignature;
import edu.unlp.medicine.entity.experiment.Sample;
import edu.unlp.medicine.entity.experiment.exception.ExperimentBuildingException;

public class LongRankTestHelper implements Observer {

	private static final String KAPLAN_MEIER = "Kaplan-Meier";
	// indica si se tiene que inicializar
	private Boolean mustinitialize = true;
	// índice de la columna a la cual se le agregan las nuevas columnas
	private Integer newBaseColumnIndex;
	private TableReferenceProvider2 provider;
	private Provider<List<ExperimentAppliedToAMetasignature>> experimentProvider;
	boolean isExperimentValidation;
	int exportColumnIndex;
	int viewClusterColIndex;

	public LongRankTestHelper(TableReferenceProvider2 provider, Provider<List<ExperimentAppliedToAMetasignature>> experimentProvider, boolean isExperimentValidation) {
		this.provider = provider;
		this.experimentProvider = experimentProvider;
		this.isExperimentValidation=isExperimentValidation;
	}

	@Override
	public void update(Observable o, Object arg) {
		refreshView();
	}

	public void refreshView() {
		final List<ExperimentAppliedToAMetasignature> eas = experimentProvider.get();
		TableReference tr = provider.tableReference();
		tr.input(eas);
		// FIXME Horrible esto... "tapar" en el ColumnBuilder...
		Table table = tr.getTable();
		TableColumn tc;
		if (mustinitialize) {
			newBaseColumnIndex = table.getColumnCount();
			// creo las nuevas columnas
			tc = new TableColumn(table, SWT.NONE, newBaseColumnIndex);
			tc.setWidth(150);
			tc.setText("View " + KAPLAN_MEIER);
			
			 exportColumnIndex = newBaseColumnIndex + 1;
			 viewClusterColIndex=newBaseColumnIndex + 2;
			
			if (!isExperimentValidation){
				tc = new TableColumn(table, SWT.NONE, newBaseColumnIndex + 1);
				tc.setWidth(150);
				tc.setText("Open Original Experiment");
				exportColumnIndex++;
				viewClusterColIndex++;
			}

			tc = new TableColumn(table, SWT.NONE, exportColumnIndex);
			tc.setWidth(200);
			tc.setText("Export gene signature data matrix");

			if (isExperimentValidation){
			tc = new TableColumn(table, SWT.NONE, viewClusterColIndex);
			tc.setWidth(100);
			tc.setText("View used cluster");
			}
			
			// ok, ya inicializado
			mustinitialize = false;
		}
		// para cada item de agrega el table editor
		// FIXME no es necesario pisar los que ya están creados...
		TableItem[] items = table.getItems();
		for (int i = 0; i < items.length; i++) {

			final ExperimentAppliedToAMetasignature exp = eas.get(i);
			TableEditor editor;
			editor = new TableEditor(table);
			Button c = new Button(table, SWT.FLAT);
			c.setImage(PlatformUIUtils.findImage("View result details.16.png"));
			c.addSelectionListener(openExperimentAppliedDialog(exp));
			editor.grabHorizontal = true;
			// editor.minimumHeight = 100;
			editor.setEditor(c, items[i], newBaseColumnIndex);

			// createAndConfigureEditor(table, c, items[i],
			// newBaseColumnIndex).minimumHeight = 100;

			if (!isExperimentValidation){
			editor = new TableEditor(table);
			try {
				c = createOpenEditorButton(exp.getOriginalExperiment(), table, "Open Original Experiment", EditorsId.experimentEditorId());
				c.setImage(PlatformUIUtils.findImage("Open original experiment.png"));
				editor.grabHorizontal = true;
				editor.setEditor(c, items[i], newBaseColumnIndex + 1);
			} catch (ExperimentBuildingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}}

			editor = new TableEditor(table);
			c = new Button(table, SWT.FLAT);
			// c.setText("Export to MEV");;
			c.setImage(PlatformUIUtils.findImage("Export experiment used for validation.16.png"));
			c.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					new MevWizard(exp, isExperimentValidation, false, null).blockOnOpen().open(); 
				}
			});
			editor.grabHorizontal = true;
			editor.setEditor(c, items[i], exportColumnIndex);
			// createAndConfigureEditor(table, c, items[i],
			// newBaseColumnIndex + 2);

			// View Cluster
			if (isExperimentValidation){
						editor = new TableEditor(table);
						c = new Button(table, SWT.FLAT);
						c.setImage(PlatformUIUtils.findImage("clustering.png"));
						//c.addSelectionListener(openViewClusterDialog(exp.getGroups())); //TODO: DavidClustering
						editor.grabHorizontal = true;
						// editor.minimumHeight = 100;
						editor.setEditor(c, items[i], viewClusterColIndex);
			}
		}

	}

	protected SelectionAdapter openExperimentAppliedDialog(final ExperimentAppliedToAMetasignature exp) {
		return new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				final Shell shell = PlatformUIUtils.findShell();
				Dialog dialog = new DialogModel<ExperimentAppliedToAMetasignature>(shell, new ModelProvider<ExperimentAppliedToAMetasignature>() {

					@Override
					public ExperimentAppliedToAMetasignature model() {
						return exp;
					}
				}) {
					@Override
					protected Control createDialogArea(Composite parent) {
						final Composite container = Widgets.createDefaultContainer((Composite) super.createDialogArea(parent));
						//AppliedExperimentEditor.makeView(container, model());
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
						newShell.setText(KAPLAN_MEIER);
					}
				};
				dialog.create();

				dialog.open();
			}
		};
	}

	private TableEditor createAndConfigureEditor(Table t, Control c, TableItem ti, int index) {
		TableEditor editor = new TableEditor(t);
		editor.grabHorizontal = true;
		editor.setEditor(t, ti, index);
		return editor;
	}

	private Button createOpenEditorButton(final Object o, Composite parent, String label, final String editorId) {
		Button b = new Button(parent, SWT.FLAT);
		// b.setText(label);
		b.setImage(PlatformUIUtils.findImage(label));
		b.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				try {
					PlatformUIUtils.openEditor(o, editorId);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});
		return b;
	}

	
	
	private SelectionListener openViewClusterDialog(final ClusteringResult groups) {
		return new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//new ConfigureClusterDialog(groups.getClusterDataList()).open();
			}
		};
	}



	
	
}
