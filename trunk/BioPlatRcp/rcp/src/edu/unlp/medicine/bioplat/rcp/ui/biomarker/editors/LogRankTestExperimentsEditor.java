package edu.unlp.medicine.bioplat.rcp.ui.biomarker.editors;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import edu.unlp.medicine.bioplat.rcp.editor.AbstractEditorPart;
import edu.unlp.medicine.bioplat.rcp.editor.ModelProvider;
import edu.unlp.medicine.bioplat.rcp.ui.biomarker.exports.MevWizard;
import edu.unlp.medicine.bioplat.rcp.ui.entities.DialogModel;
import edu.unlp.medicine.bioplat.rcp.ui.entities.EditorsId;
import edu.unlp.medicine.bioplat.rcp.ui.experiment.editors.AppliedExperimentEditor;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.ColumnBuilder;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableBuilder;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableReference;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.bioplat.rcp.widgets.Widgets;
import edu.unlp.medicine.entity.biomarker.Biomarker;
import edu.unlp.medicine.entity.experiment.ExperimentAppliedToAMetasignature;
import edu.unlp.medicine.entity.experiment.exception.ExperimentBuildingException;

public class LogRankTestExperimentsEditor extends AbstractEditorPart<Biomarker> {

	private TableReference tr;
	private Helper helper;

	public LogRankTestExperimentsEditor(boolean autoUpdatableTitle) {
		super(autoUpdatableTitle);
	}

	@Override
	protected void doCreatePartControl(Composite parent) {
		Composite c = new Composite(parent, SWT.NONE);
		c.setLayout(GridLayoutFactory.fillDefaults().numColumns(1).margins(10, 10).create());
		c.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());

		// createButtonPart(c);
		createWorkAreaPart(c);

		getHelper().refreshView();
	}

	private void createWorkAreaPart(Composite parent) {
		tr = TableBuilder.create(parent).input(model().getExperimentsApplied())//
				// .model(model(), "experiemntsApplied")//
				.hideSelectionColumn()//
				.addColumn(ColumnBuilder.create().property("name").title("Experiment"))//
				.addColumn(ColumnBuilder.create().property("significanceValue.pvalue").title("Log-Rank test p-value").width(160))//
				// TODO .addContextualMenu(new MyMenu())//
				.build();

	}

	// @Deprecated
	// private void createButtonPart(Composite parent) {
	// Composite bc = new Composite(parent, SWT.NONE);
	// bc.setLayout(new FillLayout(SWT.HORIZONTAL | SWT.END));
	//
	// Button b = new Button(bc, SWT.NONE);
	// b.setText(Messages.open_selected);
	// b.addSelectionListener(new SelectionAdapter() {
	// @Override
	// public void widgetSelected(SelectionEvent e) {
	// for (Object o : tr.selectedElements())
	// PlatformUIUtils.openEditor(o, AppliedExperimentEditor.id());
	// }
	// });
	//
	// }

	@Override
	protected Observer createModificationObserver() {
		return getHelper();
	}

	protected Helper getHelper() {
		if (helper == null)
			helper = new Helper();
		return helper;
	}

	private class Helper implements Observer {

		// indica si se tiene que inicializar
		private Boolean mustinitialize = true;
		// índice de la columna a la cual se le agregan las nuevas columnas
		private Integer newBaseColumnIndex;

		@Override
		public void update(Observable o, Object arg) {
			refreshView();
		}

		public void refreshView() {
			final List<ExperimentAppliedToAMetasignature> eas = model().getExperimentsApplied();
			tr.input(eas);

			// FIXME Horrible esto... "tapar" en el ColumnBuilder...
			Table table = tr.getTable();
			TableColumn tc;
			if (mustinitialize) {
				newBaseColumnIndex = table.getColumnCount();
				// creo las nuevas columnas
				tc = new TableColumn(table, SWT.NONE, newBaseColumnIndex);
				tc.setWidth(150);
				tc.setText("View Kaplan-Meier");

				tc = new TableColumn(table, SWT.NONE, newBaseColumnIndex + 1);
				tc.setWidth(150);
				tc.setText("Open Original Experiment");

				tc = new TableColumn(table, SWT.NONE, newBaseColumnIndex + 2);
				tc.setWidth(200);
				tc.setText("Export gene signature data matrix");
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

				editor = new TableEditor(table);
				try {
					c = createOpenEditorButton(exp.getOriginalExperiment(), table, "Open Original Experiment", EditorsId.experimentEditorId());
					c.setImage(PlatformUIUtils.findImage("Open original experiment.png"));
					editor.grabHorizontal = true;
					editor.setEditor(c, items[i], newBaseColumnIndex + 1);
				} catch (ExperimentBuildingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				editor = new TableEditor(table);
				c = new Button(table, SWT.FLAT);
				// c.setText("Export to MEV");;
				c.setImage(PlatformUIUtils.findImage("Export experiment used for validation.16.png"));
				c.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						new MevWizard(exp).blockOnOpen().open();
					}
				});
				editor.grabHorizontal = true;
				editor.setEditor(c, items[i], newBaseColumnIndex + 2);
				// createAndConfigureEditor(table, c, items[i],
				// newBaseColumnIndex + 2);

			}

		}

		protected SelectionAdapter openExperimentAppliedDialog(final ExperimentAppliedToAMetasignature exp) {
			return new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					final Shell shell = LogRankTestExperimentsEditor.this.getSite().getShell();
					Dialog dialog = new DialogModel<ExperimentAppliedToAMetasignature>(shell, new ModelProvider<ExperimentAppliedToAMetasignature>() {

						@Override
						public ExperimentAppliedToAMetasignature model() {
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

	}

}
