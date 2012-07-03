package edu.unlp.medicine.bioplat.rcp.ui.utils.tables;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.collections.ComparatorUtils;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.TableColumn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import edu.unlp.medicine.bioplat.rcp.application.Activator;
import edu.unlp.medicine.bioplat.rcp.ui.utils.accesors.Accesor;
import edu.unlp.medicine.bioplat.rcp.ui.utils.accesors.OgnlAccesor;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.editing.support.CheckBoxEditingSupport;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.editing.support.NumberEditingSupport;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.editing.support.TextEditingSupport;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.menues.MenuItemDescriptor;

public class ColumnBuilder {

	private static final Logger logger = LoggerFactory.getLogger(ColumnBuilder.class);

	public static ColumnBuilder create() {
		return new ColumnBuilder();
	}

	private DataTransformer<Object, Object> transformer = new AbstractDataTransformer<Object, Object>() {
		@Override
		public Object doTransform(Object from) {
			return from;
		}
	};

	private CellLabelProvider clp = new ColumnLabelProvider() {
		// @Override si extiendo columnLabelProvider por ejemplo
		@Override
		public String getText(Object element) {
			String current;
			if (accesor != null)
				current = transformer.transform(accesor.get(element)).toString();
			else
				current = "";
			return current;
		};

		// TODO colorear! no se ve alineado el texto...
		// private Map<ViewerCell, Object> ref = Maps.newHashMap();

		// @Override
		// public void update(ViewerCell cell) {
		// Object oldValue = current; // ref.get(cell);
		// String text = getText(cell.getElement());
		// if (!text.equals(oldValue)) {
		// cell.setForeground(new Color(cell.getBackground().getDevice(), 255,
		// 0, 0));
		// // ref.put(cell, text);
		// }
		// cell.setText(text);
		// };

	};

	private String title = "";
	private int width = 100;
	// TODO Provider
	private Class<? extends EditingSupport> editingSupport = TextEditingSupport.class;
	private boolean editable = false;

	private int alignStyle = SWT.NONE;
	private Accesor accesor; // TODO hacer un default cuando se acomode lo del
								// editingSupport
	private boolean resizable = true;

	private TableConfigurer tableConfigurer;

	/**
	 * 
	 * Construye la columna en un viewer y en una posición
	 * 
	 * @internal
	 * @param viewer
	 * @param index
	 */
	TableColumnReference build(TableConfigurer tableConfigurer, TableViewer viewer, int index) {

		this.tableConfigurer = tableConfigurer;

		// TODO No todas las columnas son ordenable, por ahora el orden está
		// disponible en todas...
		addHeadeMenuItemDescriptor(new ToggleSortColumnMenuItemDescriptor(this, viewer));

		TableViewerColumn tvc = createTableViewerColumn(viewer, title, width, index, alignStyle, resizable);

		new MenuConfigurerByHeaderSelection(viewer).configure(tvc, this);

		tvc.setLabelProvider(clp);
		if (editingSupport != null && editable)
			tvc.setEditingSupport(newInstance(editingSupport, viewer));

		return new TableColumnRef(columnId, tvc);

	}

	private EditingSupport newInstance(Class<? extends EditingSupport> clazz, TableViewer viewer) {
		try {
			// TODO revisar... cuando se acomode la clase a recibir debería
			// extender a AbstractEditingSupport
			if (accesor == null) {
				return clazz.getConstructor(TableViewer.class).newInstance(viewer);
			} else {
				return clazz.getConstructor(TableViewer.class, Accesor.class, ColumnBuilder.class).newInstance(viewer, accesor, this);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	// TODO analizar mejor y abrir a configuración...
	private boolean calculateDefault = true;

	// TODO analizar un mejor default
	private Comparator comparator = ComparatorUtils.naturalComparator();

	private boolean moveable = true;

	// indica el id de la columna, por default es el property path
	private String columnId;

	public ColumnBuilder moveable() {
		moveable = true;
		return this;
	}

	public ColumnBuilder fixed() {
		moveable = false;
		return this;
	}

	public ColumnBuilder id(String id) {
		columnId = id;
		return this;
	}

	/**
	 * El título por default es el propertypath... problema la
	 * internacionalización
	 * 
	 * @return
	 */
	// TODO 1 implementar algo o borrar
	// TODO 2 hacer que primero el property vaya como clave en la NLS
	private String calculateDefault() {
		return title;
	}

	private TableViewerColumn createTableViewerColumn(TableViewer viewer, String title, int bound, final int colNumber, int alignStyle, boolean resizable) {
		final TableViewerColumn viewerColumn = new TableViewerColumn(viewer, alignStyle);
		final TableColumn column = viewerColumn.getColumn();
		if (title.isEmpty() && calculateDefault)
			title = calculateDefault();
		column.setText(title);
		column.setWidth(bound);
		column.setResizable(resizable);
		column.setMoveable(moveable);
		return viewerColumn;

	}

	/**
	 * 
	 * 
	 * 
	 * <b>inner use</b>
	 * 
	 * @param viewer
	 * @param column
	 * @param index
	 * @return Un listener para ordenar columnas por default
	 */
	public SelectionAdapter getSelectionSorterAdapter(final TableViewer viewer, final TableColumn column, final int index) {
		SelectionAdapter selectionAdapter = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO sacar el downcasting
				final MyViewerComparator comparator = (MyViewerComparator) viewer.getComparator();
				comparator.setColumn(index, accesor, ColumnBuilder.this.comparator);
				int dir = comparator.getDirection();
				viewer.getTable().setSortDirection(dir);
				viewer.getTable().setSortColumn(column);
				viewer.refresh();
			}
		};
		return selectionAdapter;
	}

	private ColumnBuilder align(int style) {
		alignStyle = style;
		return this;
	}

	public ColumnBuilder title(String title) {
		this.title = title;
		return this;
	}

	/**
	 * usar width con resizable(false) ?
	 * 
	 * @param bound
	 * @return
	 */
	@Deprecated
	public ColumnBuilder bound(int bound) {
		return width(bound).resizable(false);
	}

	public ColumnBuilder width(int width) {
		this.width = width;
		return this;
	}

	/**
	 * setea el {@link ColumnLabelProvider} que recibe como parámetro el objeto
	 * que está detrás de la fila
	 * 
	 * @param columnLabelProvider
	 * @return this
	 */
	public ColumnBuilder labelprovider(CellLabelProvider columnLabelProvider) {
		this.clp = columnLabelProvider;
		return this;
	}

	public ColumnBuilder cellEditorSupport(Class<? extends EditingSupport> editingSupportClass) {
		this.editingSupport = editingSupportClass;
		return this;
	}

	public ColumnBuilder rightAligned() {
		align(SWT.RIGHT);
		return this;
	}

	public ColumnBuilder centered() {
		align(SWT.CENTER);
		return this;
	}

	public ColumnBuilder leftAligned() {
		align(SWT.LEFT);
		return this;
	}

	public ColumnBuilder accesor(Accesor accesor) {
		this.accesor = accesor;
		return this;
	}

	public ColumnBuilder editable(boolean editable) {
		this.editable = editable;
		return this;
	}

	public ColumnBuilder editable() {
		return editable(true);
	}

	public ColumnBuilder numeric() {
		return comparator(createNumericComparator()).align(SWT.RIGHT).cellEditorSupport(NumberEditingSupport.class);
	}

	private ColumnBuilder comparator(Comparator comparator) {
		this.comparator = comparator;
		return this;
	}

	// TODO implementar afuera y "mejor"
	private Comparator<Number> createNumericComparator() {
		return new Comparator<Number>() {

			@Override
			public int compare(Number oo1, Number oo2) {
				Float o1 = oo1.floatValue();
				Float o2 = oo2.floatValue();
				if (o1 < o2)
					return -1;
				else if (o1 > o2)
					return 1;
				else
					return 0;
			}
		};
	}

	// We use icons for checkboxes
	private static final Image CHECKED = Activator.imageDescriptorFromPlugin("resources/icons/checked.gif").createImage();
	private static final Image UNCHECKED = Activator.imageDescriptorFromPlugin("resources/icons/unchecked.gif").createImage();

	public ColumnBuilder checkbox() {
		return width(20).cellEditorSupport(CheckBoxEditingSupport.class)//
				.labelprovider(new ColumnLabelProvider() {
					@Override
					public String getText(Object element) {
						return "";
					}

					@Override
					public Image getImage(Object element) {
						if ((Boolean) accesor.get(element))
							return CHECKED;
						else
							return UNCHECKED;
					}
				});
	}

	public ColumnBuilder createButton(String text/* , Executable e */) {
		logger.warn("TODO...");
		return this;
	}

	public ColumnBuilder resizable(boolean b) {
		resizable = b;
		return this;
	}

	/**
	 * 
	 * se accede al modelo por property path, utilizando OGNL
	 * 
	 * @param string
	 * @return
	 */
	public ColumnBuilder property(String propertyPath) {
		if (columnId == null)
			columnId = propertyPath;
		return accesor(OgnlAccesor.createFor(propertyPath));
	}

	public ColumnBuilder transformer(DataTransformer dataTransformer) {
		this.transformer = dataTransformer;
		return this;
	}

	/**
	 * @internal
	 * @return el transformer configurado para la columna
	 */
	public DataTransformer transformer() {
		return transformer;
	}

	// NOTE: sirve para antes de estar build... luego ya no se actualiza (al
	// menos, por ahora)
	private List<MenuItemDescriptor> headersMenuDescriptors = Lists.newArrayList();

	public List<MenuItemDescriptor> headerMenuItemDescriptors() {
		return headersMenuDescriptors;
	}

	public ColumnBuilder addHeadeMenuItemDescriptor(MenuItemDescriptor... descriptors) {
		headersMenuDescriptors.addAll(Arrays.asList(descriptors));
		return this.fixed(); // TODO sacar el fixed cuano ande bien lo del menú
								// en las columnas...
	}

	public ColumnBuilder removeHeaderMenuItemsDescriptor(MenuItemDescriptor... descriptors) {
		headersMenuDescriptors.removeAll(Arrays.asList(descriptors));
		return this;
	}

	public ColumnBuilder hidden() {
		return width(0);
	}

	void reconfigureMenu() {
		// TODO propias?
		tableConfigurer.configureMenu();
	}
}
