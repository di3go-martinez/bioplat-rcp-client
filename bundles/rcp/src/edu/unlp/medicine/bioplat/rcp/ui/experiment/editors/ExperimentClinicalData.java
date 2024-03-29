package edu.unlp.medicine.bioplat.rcp.ui.experiment.editors;

import static java.lang.Math.min;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

import edu.unlp.medicine.bioplat.rcp.editor.AbstractEditorPart;
import edu.unlp.medicine.bioplat.rcp.ui.experiment.preferences.ExperimentGeneralPreferencePage;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.ColumnBuilder;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableBuilder;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableReference;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.cells.CustomCellData;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.cells.CustomCellDataBuilder;
import edu.unlp.medicine.bioplat.rcp.ui.views.messages.Message;
import edu.unlp.medicine.bioplat.rcp.ui.views.messages.MessageManager;
import edu.unlp.medicine.bioplat.rcp.utils.Holder;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.entity.experiment.AbstractExperiment;
import edu.unlp.medicine.entity.experiment.Sample;

/**
 * 
 * Editor que muestra en una grilla los datos clínicos de los experimentos
 * 
 * @author Diego Martínez
 * 
 */
public class ExperimentClinicalData extends AbstractEditorPart<AbstractExperiment> {

	private static final Image ascimg = PlatformUIUtils.findImage("asc.png");
	private static final Image descimg = PlatformUIUtils.findImage("desc.png");

	private static final String CLINICAL_DATA = "Clinical Data";
	private TableReference tr;
	private List<ClinicalDataModel> gridModel;
	private ComboViewer sorterSelectionAttribute;
	private boolean editorOpen = true;

	private ExecutorService exec = Executors.newFixedThreadPool(1);
	
	public ExperimentClinicalData(boolean updatableTitle) {
		super(updatableTitle);
	}

	@Override
	protected void doCreatePartControl(Composite parent) {
		gridModel = makeModel(model());

		Composite container = new Composite(parent, SWT.NONE);
		container.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());
		GridLayoutFactory.fillDefaults().margins(10, 10).applyTo(container);

		TableBuilder tb = TableBuilder.create(container).input(gridModel);
		tb.addColumn(ColumnBuilder.create().property("data[0].value"));
		int index = 1;
		for (Sample s : resolveSamplesToLoad())
			// TODO numeric()?
			tb.addColumn(ColumnBuilder.create().title(s.getName()).editable().property("data[" + index++ + "].value")//
					.addHeadeMenuItemDescriptor(new RemoveSampleColumnDescriptor(model())));

		tr = tb.build();

		Composite sorterContainer = new Composite(container, SWT.BORDER);
		sorterContainer.setLayout(GridLayoutFactory.fillDefaults().numColumns(2).equalWidth(false).create());
		sorterSelectionAttribute = new ComboViewer(sorterContainer);
		sorterSelectionAttribute.setContentProvider(ArrayContentProvider.getInstance());

		// transformo la lista model en una lista de nombres de atributo.
		sorterSelectionAttribute.setInput(buildSorterSelectionAttributeInput());

		final Button toggleSorter = new Button(sorterContainer, SWT.TOGGLE | SWT.FLAT);
		// toggleSorter.setSize(new Point(20, 20));
		toggleSorter.setImage(ascimg);
		toggleSorter.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (toggleSorter.getImage() == ascimg)
					toggleSorter.setImage(descimg);
				else
					toggleSorter.setImage(ascimg);
				resort(tr, sorterSelectionAttribute, toggleSorter);
			}
		});

		sorterSelectionAttribute.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				resort(tr, sorterSelectionAttribute, toggleSorter);
			}

		});
		setPartName(CLINICAL_DATA);
	}

	/**
	 * En base a gridModle, extra el nombre del atributo
	 * 
	 * @return
	 * @see ClinicalDataModel
	 */
	protected Collection<String> buildSorterSelectionAttributeInput() {
		return Collections2.transform(gridModel, new Function<ClinicalDataModel, String>() {

			@Override
			public String apply(ClinicalDataModel input) {
				return input.attributeName();
			}
		});
	}

	// TODO mejorar los parámetros pasados al método, que sean los datos no los
	// widgets...
	private void resort(final TableReference tr, ComboViewer cv, Button b) {
		final int FIXED_COLUMNS = 3;
		
		Table t = tr.getTable();

		// Coincide el orden de los atributos en las grillas, por eso se usa el
		// índice de selección
		int selectionIndex = cv.getCombo().getSelectionIndex();
		if (selectionIndex == -1)
			return;
		TableItem ti = t.getItem(selectionIndex);
		ClinicalDataModel cdm = (ClinicalDataModel) ti.getData();

		final int samplesCount = Math.min(t.getColumnCount()- FIXED_COLUMNS, ExperimentEditor.getSampleCountToLoad());
		// sorted es la colección donde quedarán los datos ordenados
		CustomCellData[] sorted = Arrays.copyOfRange(cdm.getData(), 1, samplesCount + 1);
		// original es como están los datos (des)ordenados actualmente
		CustomCellData[] original = new CustomCellData[sorted.length];

		System.arraycopy(sorted, 0, original, 0, sorted.length);

		// el método sort esta bien optimizado, @see javadoc
		Arrays.sort(sorted, getComparator());
		if (b.getImage() == descimg)
			Arrays.sort(sorted, Collections.reverseOrder());

		// calculo el nuevo orden de los elementos, de acuerdo a como
		// quedaron ordenados
		int[] newOrder = new int[t.getColumnCount()];
		newOrder[0] = 0; // la primera columna no se ordena, es fija es
							// la de selección
		newOrder[1] = 1; // la segunda tampoco se ordena es la columna
							// de nombre de atributo
		newOrder[2] = 2; // hay una columna fija oculta por un bug!! ver
							// TableBuilder.create

		

		final List<CustomCellData> originalAsList = Arrays.asList(original);
		// · voy buscando los elementos en orden de sorted, y poniendo
		// su índice en la colección newOrder, la cual se usara para
		// indicarle a la tabla como ordenar las columnas
		// · 3 porque es la cantidad de columnas fijas! que no se ordenan
		int i;

		for (i = FIXED_COLUMNS; i < sorted.length + FIXED_COLUMNS; i++) {
			int newIndex = originalAsList.indexOf(sorted[i - FIXED_COLUMNS]) + FIXED_COLUMNS;
			newOrder[i] = newIndex;
		}

		t.setColumnOrder(newOrder);
	}

	// Comparador para ordenar las celdas
	private Comparator<? super CustomCellData> getComparator() {
		return new Comparator<CustomCellData>() {

			@Override
			public int compare(CustomCellData o1, CustomCellData o2) {

				// trato especial para parámetros nulos...
				if (o1 == null) {
					if (o2 == null)
						return 0;
					else
						return 1;
				} else if (o2 == null)
					return -1;

				final String strValue1 = o1.getValue().toString();
				final String strValue2 = o2.getValue().toString();
				try {
					Float thisValue = new Float(strValue1);
					Float otherValue = new Float(strValue2);
					return thisValue.compareTo(otherValue);
				} catch (Exception e) {
					return strValue1.compareTo(strValue2); // TODO definir
															// mejor, hay veces
															// que los valores
															// son 'N/A'
				}

			};

		};
	}

	// /
	/**
	 * 
	 * @return el subconjunto "inicial" de samples a cargar en la grilla; la
	 *         cantidad a cargar se carga en las propiedades del experimento
	 * @see ExperimentEditor#getSampleCountToLoad()
	 */
	private List<Sample> resolveSamplesToLoad() {
		int count = ExperimentEditor.getSampleCountToLoad();
		// se salva el caso de que la cantidad a cargar sea más grande que la
		// cantidad real que tenga el experimento
		count = min(count, model().getSamples().size());

		return model().getSamples().subList(0, count);
	}

	private List<ClinicalDataModel> makeModel(AbstractExperiment model) {
		return ClinicalDataModel.create(model);
	}
	
	private boolean isEditorOpen() {
		return editorOpen;
	}
	
	
	@Override
	public void dispose() {
		editorOpen = false;
		super.dispose();
	}

	@Override
	protected Observer createModificationObserver() {

		// TODO analizar si hace falta el lock ahora...
		final Object lock = new Object();
		final Holder<Boolean> changed = Holder.create(false);
		// table view refresher
		new Thread() {
			@Override
			public void run() {

				while (isEditorOpen()) {
					PlatformUIUtils.findDisplay().syncExec(new Runnable() {

						@Override
						public void run() {
							synchronized (lock) {
								if (changed.value()) {
									tr.input(gridModel);
									changed.hold(false);
								}
							}
						}
					});
					sleep();
				}
			}

			private void sleep() {
				try {
					Thread.sleep(1000);// TODO hacer configurable (de quedar)
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();

		return new Observer() {

			// uso para indicar que el autorefresh esta desactivado
			private boolean logged = false;

			// private int counter = 0;

			@Override
			public void update(Observable o, Object arg) {

				if (!autorefresh()) {
					warnAutoRefresh();
					return;
				}
				logged = false;
				
				exec.submit(new Runnable() {

					@Override
					public void run() {
						synchronized (lock) {
							// FIXME no va más esto?.... cómo queda ahora la
							// performance?....
//							gridModel = ExpressionDataModel.checkGenes(gridModel, model());
							changed.hold(true);
						}

					}
				});

				ExperimentEditor.checkColumns(tr, model());

			}

			private void warnAutoRefresh() {
				if (!logged) {
					final String msg = "The grid autorefreshing is disabled";
					MessageManager.INSTANCE.add(Message.warn(msg));
//					logger.warn(msg);
					logged = true;
				}
			}

			private boolean autorefresh() {
				return ExperimentEditor.preferences().getBoolean(ExperimentGeneralPreferencePage.EXPERIMENT_GRID_AUTO_REFRESH, true);
			}
		};
	}
}

/**
 * 
 * Modelo que representa a una fila de la grilla de datos clínicos
 * 
 * 
 */
class ClinicalDataModel {

	private CustomCellData[] data;

	public CustomCellData[] getData() {
		return data;
	}

	public String attributeName() {
		return getData()[0].getValue().toString();
	}

	private ClinicalDataModel(CustomCellData[] data) {
		this.data = data;
	}

	public static List<ClinicalDataModel> create(AbstractExperiment e) {
		List<ClinicalDataModel> result = Lists.newArrayList();

		// el nombre del atributo
		// for (String a : e.getClinicalAttributeNames()) {
		// // uno más para que entre
		// Object[] rowData = new Object[e.getSamples().size() + 1];
		// int index = 0;
		// rowData[index++] = a;
		// for (Sample s : e.getSamples())
		// rowData[index++] = e.getClinicalAttribute(s.getName(), a);
		// result.add(new ClinicalDataModel(rowData));
		//
		// }

		// el nombre del atributo
		for (String a : e.getClinicalAttributeNames()) {
			// uno más para que entre
			// FIXME notar que está cargando todos los samples y no solamente
			// los que está mostrando... si da problemas de performance acomodar
			CustomCellData[] rowData = new CustomCellData[e.getSamples().size() + 1];
			int index = 0;
			rowData[index++] = CustomCellDataBuilder.constant(a);
			for (Sample s : e.getSamples())
				rowData[index++] = CustomCellDataBuilder.create(new ExpClinicalDataResolver(e, a, s));
			result.add(new ClinicalDataModel(rowData));
		}

		return result;
	}

}
