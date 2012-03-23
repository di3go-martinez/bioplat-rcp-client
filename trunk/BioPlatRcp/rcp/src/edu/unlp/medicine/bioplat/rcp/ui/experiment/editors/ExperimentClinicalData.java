package edu.unlp.medicine.bioplat.rcp.ui.experiment.editors;

import java.util.Arrays;
import java.util.List;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
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
import edu.unlp.medicine.entity.experiment.AbstractExperiment;
import edu.unlp.medicine.entity.experiment.Sample;
import edu.unlp.medicine.entity.generic.AbstractEntity;

public class ExperimentClinicalData extends AbstractEditorPart<AbstractExperiment> {

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
			tb.addColumn(ColumnBuilder.create().title(s.getName()).editable().numeric().property("data[" + index++ + "].value"));

		final TableReference tr = tb.build();

		ComboViewer c = new ComboViewer(container);
		c.setContentProvider(ArrayContentProvider.getInstance());
		c.setInput(Collections2.transform(model, new Function<ClinicalDataModel, String>() {

			@Override
			public String apply(ClinicalDataModel input) {
				return input.getData()[0].getValue().toString();
			}
		}));

		c.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				// TODO revisar, documentar, porque no se entiende....
				Table t = tr.getTable();

				// Coincide el orden por eso se usa el índice de selección
				int selectionIndex = ((ComboViewer) event.getSource()).getCombo().getSelectionIndex();
				TableItem ti = t.getItem(selectionIndex);
				ClinicalDataModel cdm = (ClinicalDataModel) ti.getData();
				CellData[] sorted = Arrays.copyOfRange(cdm.getData(), 1, ExperimentEditor.getSampleCountToLoad() + 1);
				CellData[] original = new CellData[sorted.length];
				System.arraycopy(sorted, 0, original, 0, sorted.length);
				Arrays.sort(sorted);

				int[] newOrder = new int[t.getColumnCount()];
				newOrder[0] = 0; // la primera columna no se ordena, es fija es
									// la de selección
				newOrder[1] = 1; // la segunda tampoco se ordena es la columna
									// de nombre de atributo

				final List<CellData> originalAsList = Arrays.asList(original);
				// 2 porque son dos fijas
				for (int i = 2; i < sorted.length + 2; i++) {
					int newIndex = originalAsList.indexOf(sorted[i - 2]) + 2;
					newOrder[i] = newIndex;

				}
				t.setColumnOrder(newOrder);

			}
		});
		setPartName("Clinical Data");
	}

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
}

class ClinicalDataModel extends AbstractEntity {

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

interface CellValueResolver {
	void doSet(Object value);

	Object doGet();
}

class CustomCellData {
	private CellValueResolver resolver;

	public CustomCellData(CellValueResolver resolver) {
		this.resolver = resolver;
	}

	public void setValue(Object value) {
		resolver.doSet(value);
	}

	public Object getValue() {
		return resolver.doGet();
	}
}

class CustomCellDataBuilder {
	private CustomCellDataBuilder() {
		// TODO Auto-generated constructor stub
	}

	public static CustomCellData create(CellValueResolver resolver) {
		return new CustomCellData(resolver);
	}

	public static CustomCellData constant(final Object constantValue) {
		return new CustomCellData(new CellValueResolver() {

			@Override
			public void doSet(Object value) {
				// nothing, it's CONSTANT!

			}

			@Override
			public Object doGet() {
				return constantValue;
			}
		});
	}
}

class CellData implements Accesor, Comparable<CellData> {
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
		return experiment.getClinicalAttribute(sampleId, attributeName);
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
		Integer thisValue = new Integer(getValue().toString());
		Integer otherValue = new Integer(o.getValue().toString());

		return thisValue.compareTo(otherValue);
	}
}