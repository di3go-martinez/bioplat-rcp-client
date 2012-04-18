package edu.unlp.medicine.bioplat.rcp.ui.experiment.editors;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

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
import edu.unlp.medicine.bioplat.rcp.ui.utils.accesors.Accesor;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.ColumnBuilder;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableBuilder;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableReference;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.cells.CustomCellDataBuilder;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.entity.experiment.AbstractExperiment;
import edu.unlp.medicine.entity.experiment.Sample;

public class ExperimentClinicalData extends AbstractEditorPart<AbstractExperiment> {

	private static final Image ascimg = PlatformUIUtils.findImage("asc.png");
	private static final Image descimg = PlatformUIUtils.findImage("desc.png");

	private static final String CLINICAL_DATA = "Clinical Data";
	private TableReference tr;

	public ExperimentClinicalData(boolean updatableTitle) {
		super(updatableTitle);
	}

	@Override
	protected void doCreatePartControl(Composite parent) {
		List<ClinicalDataModel> model = makeModel(model());

		Composite container = new Composite(parent, SWT.NONE);
		container.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());
		GridLayoutFactory.fillDefaults().margins(10, 10).applyTo(container);

		TableBuilder tb = TableBuilder.create(container).input(model);
		tb.addColumn(ColumnBuilder.create().property("data[0].value"));
		int index = 1;
		for (Sample s : resolveSamplesToLoad())
			tb.addColumn(ColumnBuilder.create().title(s.getName()).editable().numeric().property("data[" + index++ + "].value")//
					.addHeadeMenuItemDescriptor(new RemoveSampleColumnDescriptor(model())));

		tr = tb.build();

		Composite sorterContainer = new Composite(container, SWT.BORDER);
		sorterContainer.setLayout(GridLayoutFactory.fillDefaults().numColumns(2).equalWidth(false).create());
		final ComboViewer cv = new ComboViewer(sorterContainer);
		cv.setContentProvider(ArrayContentProvider.getInstance());

		// transformo la lista model en una lista de nombres de atributo.
		cv.setInput(Collections2.transform(model, new Function<ClinicalDataModel, String>() {

			@Override
			public String apply(ClinicalDataModel input) {
				return input.getData()[0].getValue().toString();
			}
		}));

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
				resort(tr, cv, toggleSorter);
			}
		});

		cv.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				resort(tr, cv, toggleSorter);
			}

		});
		setPartName(CLINICAL_DATA);
	}

	// TODO mejorar los parámetros pasados al método, que sean los datos no los
	// widgets...
	private void resort(final TableReference tr, ComboViewer cv, Button b) {
		Table t = tr.getTable();

		// Coincide el orden de los atributos en las grillas, por eso se usa el
		// índice de selección
		int selectionIndex = cv.getCombo().getSelectionIndex();
		if (selectionIndex == -1)
			return;
		TableItem ti = t.getItem(selectionIndex);
		ClinicalDataModel cdm = (ClinicalDataModel) ti.getData();

		// sorted es la colección donde quedarán los datos ordenados
		CellData[] sorted = Arrays.copyOfRange(cdm.getData(), 1, ExperimentEditor.getSampleCountToLoad() + 1);
		// original es como están los datos (des)ordenados actualmente
		CellData[] original = new CellData[sorted.length];

		System.arraycopy(sorted, 0, original, 0, sorted.length);

		// el método sort esta bien optimizado, @see javadoc
		Arrays.sort(sorted);
		if (b.getImage() == descimg)
			Arrays.sort(sorted, Collections.reverseOrder());

		// calculo el nuevo orden de los elementos, de acuerdo a como
		// quedaron ordenados
		int[] newOrder = new int[t.getColumnCount()];
		newOrder[0] = 0; // la primera columna no se ordena, es fija es
							// la de selección
		newOrder[1] = 1; // la segunda tampoco se ordena es la columna
							// de nombre de atributo

		final List<CellData> originalAsList = Arrays.asList(original);
		// · voy buscando los elementos en orden de sorted, y poniendo
		// su índice en la colección newOrder, la cual se usara para
		// indicarle a la tabla como ordenar las columnas
		// · 2 porque son dos fijas
		for (int i = 2; i < sorted.length + 2; i++) {
			int newIndex = originalAsList.indexOf(sorted[i - 2]) + 2;
			newOrder[i] = newIndex;
		}
		t.setColumnOrder(newOrder);
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
		return model().getSamples().subList(0, count);
	}

	private List<ClinicalDataModel> makeModel(AbstractExperiment model) {
		return ClinicalDataModel.create(model);
	}

	@Override
	protected Observer createModificationObserver() {

		return new Observer() {

			@Override
			public void update(Observable o, Object arg) {
				// FIXME PARCHE PARA QUE SE BORREN LOS SAMPLES QUE SE BORRAN EN
				// LA OTRA SOLAPA,
				// TODO POSIBLE SOLUCIÓN, AGREGAR UN CHECKEO O VALIDACION DE LA
				// TABLEREFENCE CON EL MODELO
				Table t = tr.getTable();

				// if (t.isDisposed())
				// return;

				ExperimentEditor.checkColumns(tr, model());
			}

		};
	}
}

class ClinicalDataModel /* extends AbstractEntity */{

	private CellData[] data;

	public CellData[] getData() {
		return data;
	}

	private ClinicalDataModel(CellData[] data) {
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
			CellData[] rowData = new CellData[e.getSamples().size() + 1];
			int index = 0;
			rowData[index++] = CellData.constant(a);
			for (Sample s : e.getSamples())
				rowData[index++] = CellData.create(e, a, s.getName());
			result.add(new ClinicalDataModel(rowData));

		}

		return result;
	}
}

/**
 * 
 * @author diego
 * @deprecated migrar a {@link CustomCellDataBuilder}
 */
@Deprecated
class CellData implements Accesor, Comparable<CellData> {
	private static final String _DEFAULT = "0";
	private AbstractExperiment experiment;
	private String attributeName;
	private String sampleId;
	private String text;

	static CellData create(AbstractExperiment e, String attributeName, String sampleId) {
		return new CellData(e, attributeName, sampleId);
	}

	static CellData constant(String text) {
		return new CellData(text);
	}

	private CellData(AbstractExperiment e, String attributeName, String sampleId) {
		this.experiment = e;
		this.attributeName = attributeName;
		this.sampleId = sampleId;
	}

	private CellData(String text) {
		this.text = text;
	}

	@Override
	public Object get(Object element) {
		if (experiment == null)
			return text;
		if (experiment.getSampleNames().contains(sampleId))
			return experiment.getClinicalAttribute(sampleId, attributeName);
		else
			return _DEFAULT;
	}

	@Override
	public void set(Object element, Object value) {
		if (experiment != null)
			experiment.setClinicalAttribute(sampleId, attributeName, value.toString());
	}

	public void setValue(Object e) {
		set(null, e);
	}

	public Object getValue() {
		return get(null);
	}

	@Override
	public int compareTo(CellData o) {
		Float thisValue = new Float(getValue().toString());
		Float otherValue = new Float(o.getValue().toString());

		return thisValue.compareTo(otherValue);
	}
}