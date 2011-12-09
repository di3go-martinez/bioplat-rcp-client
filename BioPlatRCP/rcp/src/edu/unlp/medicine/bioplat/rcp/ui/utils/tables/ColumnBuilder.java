package edu.unlp.medicine.bioplat.rcp.ui.utils.tables;

import java.util.Comparator;

import org.apache.commons.collections.ComparatorUtils;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.TableColumn;

import edu.unlp.medicine.bioplat.rcp.application.Activator;
import edu.unlp.medicine.bioplat.rcp.ui.utils.accesors.Accesor;
import edu.unlp.medicine.bioplat.rcp.ui.utils.accesors.OgnlAccesor;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.editing.support.CheckBoxEditingSupport;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.editing.support.NumberEditingSupport;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.editing.support.TextEditingSupport;

public class ColumnBuilder {

	public static ColumnBuilder create() {
		return new ColumnBuilder();
	}

	private CellLabelProvider clp = new ColumnLabelProvider() {

		{
			addListener(new ILabelProviderListener() {

				@Override
				public void labelProviderChanged(LabelProviderChangedEvent event) {
					event.getElement();
				}
			});
		}

		// @Override si extiendo columnLabelProvider por ejemplo
		@Override
		public String getText(Object element) {
			if (accesor != null)
				return accesor.get(element).toString();
			else
				return "";
		};

		// TODO colorear! no se ve alineado el texto...
		// private Map<ViewerCell, Object> ref = Maps.newHashMap();

		// @Override
		// public void update(ViewerCell cell) {
		// Object oldValue = ref.get(cell);
		// String text = getText(cell.getElement());
		// if (oldValue != null)
		// if (!oldValue.equals(text)) {
		// cell.setForeground(new Color(cell.getBackground().getDevice(), 255,
		// 0, 0));
		// ref.put(cell, text);
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

	/**
	 * @internal
	 * @param viewer
	 * @param index
	 */
	void build(TableViewer viewer, int index) {
		TableViewerColumn tvc = createTableViewerColumn(viewer, title, width, index, alignStyle, resizable);
		tvc.setLabelProvider(clp);
		if (editingSupport != null && editable)
			tvc.setEditingSupport(newInstance(editingSupport, viewer));
	}

	private EditingSupport newInstance(Class<? extends EditingSupport> clazz, TableViewer viewer) {
		try {
			// TODO revisar... cuando se acomode la clase a recibir deber�a
			// extender a AbstractEditingSupport
			if (accesor == null) {
				return clazz.getConstructor(TableViewer.class).newInstance(viewer);
			} else {
				return clazz.getConstructor(TableViewer.class, Accesor.class).newInstance(viewer, accesor);
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
		column.setMoveable(true);// TODO
		column.addSelectionListener(getSelectionAdapter(viewer, column, colNumber));
		return viewerColumn;

	}

	private SelectionAdapter getSelectionAdapter(final TableViewer viewer, final TableColumn column, final int index) {
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
	 * usar width
	 * 
	 * @param bound
	 * @return
	 */
	@Deprecated
	public ColumnBuilder bound(int bound) {
		return width(bound);
	}

	public ColumnBuilder width(int width) {
		this.width = width;
		return this;
	}

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

	public ColumnBuilder centeredText() {
		align(SWT.CENTER);
		return this;
	}

	public ColumnBuilder leftAligned() {
		align(SWT.LEFT);
		return this;
	}

	public ColumnBuilder accesor(Accesor mutator) {
		this.accesor = mutator;
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

	// We use icons
	private static final Image CHECKED = Activator.imageDescriptorFromPlugin("resources/icons/checked.gif").createImage();
	private static final Image UNCHECKED = Activator.imageDescriptorFromPlugin("resources/icons/unchecked.gif").createImage();

	public ColumnBuilder checkbox() {
		return width(20).cellEditorSupport(CheckBoxEditingSupport.class)//
				.labelprovider(new ColumnLabelProvider() {
					@Override
					public Image getImage(Object element) {
						if ((Boolean) accesor.get(element))
							return CHECKED;
						else
							return UNCHECKED;
					}
				});
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
		return accesor(OgnlAccesor.createFor(propertyPath));
	}

}
