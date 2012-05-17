package edu.unlp.medicine.bioplat.rcp.ui.utils.tables;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

import ognl.Ognl;
import ognl.OgnlException;

import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.jface.databinding.viewers.ViewerSupport;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import edu.unlp.medicine.bioplat.rcp.application.Activator;
import edu.unlp.medicine.bioplat.rcp.ui.utils.accesors.Accesor;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.entity.gene.Gene;
import edu.unlp.medicine.entity.generic.AbstractEntity;

public class TableBuilder implements TableConfigurer {

	private final static Logger logger = LoggerFactory.getLogger(TableBuilder.class);

	private static final ColumnBuilder HIDDEN_COLUMN = ColumnBuilder.create().resizable(false).editable(false).width(0);

	private Set<Object> selectedElements = Sets.newHashSet();
	// TODO
	private final ColumnBuilder ROW_SELECT_COLUMN = ColumnBuilder.create().checkbox().resizable(false).editable().accesor(new Accesor() {

		@Override
		public void set(Object element, Object checked) {
			if ((Boolean) checked)
				selectedElements.add(element);
			else
				selectedElements.remove(element);
		}

		@Override
		public Object get(Object element) {
			return selectedElements.contains(element);
		}
	});

	private TableViewer viewer;

	// TODO revisar.... usar input resolver?
	private List<?> input = Lists.newArrayList();
	private Class<?> inputClass;

	private WritableList realInput;

	private boolean viewTableLines = true;

	private TableBuilder(Composite parent, boolean virtual) {
		// SWT.VIRTUAL tira error cuando se quiere editar un item.... ver
		// javadoc de TableViewer...
		// implementar el ILazyContentProvider
		int style = SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER;
		if (virtual)
			style |= SWT.VIRTUAL;

		viewer = new TableViewer(parent, style);
		viewer.setUseHashlookup(true);
		viewer.setComparer(new CustomComparer());

		final Table table = viewer.getTable();
		table.setHeaderVisible(true); // TODO configurable
		table.setLinesVisible(viewTableLines);

		// viewer.setContentProvider(new ArrayContentProvider());
		// Make the selection available to other views
		// getSite().setSelectionProvider(viewer);

		// Layout the viewer
		GridData gridData = new GridData(GridData.FILL, GridData.FILL, true, true);
		gridData.horizontalSpan = 1;
		viewer.getControl().setLayoutData(gridData);

	}

	private Table getTable() {
		return viewer.getTable();
	}

	public static TableBuilder create(Composite container) {
		return new TableBuilder(container, false);
	}

	/**
	 * Parche para poder crear una tabla virtual...
	 * 
	 * @param container
	 * @return
	 */
	public static TableBuilder createVirtual(Composite container) {
		return new TableBuilder(container, true);
	}

	/**
	 * configuro el input para la tablas
	 * 
	 * @param input
	 * @param klazz
	 * @return
	 */
	private <T> TableBuilder input(List<T> input, Class<?> klazz) {
		// ObservableListContentProvider provider = new
		// ObservableListContentProvider();
		// viewer.setContentProvider(provider);
		// viewer.setInput(new WritableList(listenToChangeList(input), klazz));

		// ViewerSupport.bind(viewer, new WritableList(input, klazz),
		// BeanProperties.value(""));

		this.input = input;
		this.inputClass = klazz;
		return this;
	}

	/**
	 * Se recomienda usar model+propertyPath
	 * 
	 * @param input
	 * @return
	 * @deprecated Se recomienda usar model+propertyPath
	 */
	@Deprecated
	public <T> TableBuilder input(List<T> input) {
		return input(input, Object.class);
	}

	public TableBuilder hideTableLines() {
		viewTableLines = false;
		return this;
	}

	public TableBuilder hideSelectionColumn() {
		showSelectionColumn = false;
		return this;
	}

	public TableBuilder showSelectionColumn() {
		showSelectionColumn = true;
		return this;
	}

	private List<ColumnBuilder> columns = new ArrayList<ColumnBuilder>();

	public TableBuilder addColumn(ColumnBuilder cb) {
		columns.add(cb);
		return this;
	}

	private boolean built = false;
	private TableReference table;
	private boolean showSelectionColumn = true;

	private Map<String, TableColumnReference> columnsHolder = Maps.newHashMap();

	/**
	 * Construye la grilla preconfigurada. Esta operación es idempotente
	 * 
	 * @return
	 */
	public TableReference build() {

		if (built)
			return table;

		// agrego el input al viewer con soporte para databinding
		realInput = new WritableList(input, inputClass);
		ViewerSupport.bind(viewer, realInput, BeanProperties.value(""));

		int columnCount = 0;

		if (showSelectionColumn)
			ROW_SELECT_COLUMN.build(viewer, columnCount++);

		for (ColumnBuilder cb : columns) {
			TableColumnReference tcr = cb.build(viewer, columnCount++);
			columnsHolder.put(tcr.id(), tcr);
		}

		viewer.refresh(true);
		viewer.setComparator(new MyViewerComparator());
		viewer.getTable().setLinesVisible(viewTableLines);

		built = true;

		// TODO sacar a una clase afuera...
		return table = new TableReference() {

			@Override
			public void refresh() {
				PlatformUIUtils.findDisplay().syncExec(new Runnable() {

					@Override
					public void run() {
						if (!viewer.isBusy()) {
							resolver.resolveInput(viewer);
							viewer.refresh();
						}
					}
				});

			}

			/**
			 * 
			 * <b>si el input fue configurado se requerirá que tenga una
			 * newInput, si no es porque la tabla fue configurada con un
			 * modelo</b>
			 * 
			 * @param newInput
			 *            puede ser null, si es null será necesario que la tabla
			 *            haya sido construída con model()
			 * @see TableBuilder#model()
			 */
			// TODO acomodar la desprolijidad del newInput=null
			@Override
			public void input(@Nullable final List newInput) {
				// si el model es null es porque la input fue configurada
				// "estáticamente", se usa newInput
				if (model == null)
					//
					paging = new Paging(new Object() {
						@SuppressWarnings("unused")
						public List getData() {
							return newInput;
						}
					}, "data", viewer, TableBuilder.this, paging);

				else
					// TODO es necesario recrear el paging?
					paging = new Paging(model, propertyPath, viewer, TableBuilder.this, paging);

				// input.clear();
				// input.addAll(paging.list());

				viewer.setInput(realInput = new WritableList(input = paging.list(), inputClass));
				viewer.refresh(true, false);

				refreshSelectedElements();

			}

			private void refreshSelectedElements() {
				List<Object> toRemove = Lists.newArrayList();
				for (Object element : selectedElements)
					if (!input.contains(element))
						toRemove.add(element);
				selectedElements.removeAll(toRemove);
				// TODO no hace falta un refresh();, no?
			}

			/**
			 * no se está usando
			 */
			@Override
			public ColumnManager columnManager() {
				logger.debug("sacar comentario, ahora se usa!");
				return DefaultColumnManager.createOn(viewer);
			}

			@Override
			public void show(Object element) {
				viewer.setFilters((ViewerFilter[]) Arrays.asList(new MyElementFilter((Gene) element)).toArray());

			}

			@Override
			public void add(Object element) {
				realInput.add(element);
			}

			@Override
			public List<?> selectedElements() {
				return ImmutableList.copyOf(selectedElements);
			}

			@Override
			public List<?> focusedElements() {
				TableItem[] items = viewer.getTable().getSelection();
				List result = Lists.transform(Arrays.asList(items), new Function<TableItem, Object>() {

					@Override
					public Object apply(TableItem input) {
						return input.getData();
					}
				});
				return result;

			}

			@Override
			public void addSelectionChangeListener(ISelectionChangedListener listener) {
				viewer.addSelectionChangedListener(listener);
			}

			@Override
			public Table getTable() {
				return viewer.getTable();
			}

			@Override
			public void remove(Object element) {
				viewer.remove(element);
				selectedElements.remove(element);
			}

			@Override
			public void removeSelected() {
				for (Object elemtent : selectedElements())
					remove(elemtent);
			}

			@Override
			public TableColumn column(String id) {
				final TableColumnReference tableColumnReference = columnsHolder.get(id);
				if (tableColumnReference == null) {
					logger.error("La columna '" + id + "' no existe...");
					return null;
				}
				return tableColumnReference.column();
			}

			// @Override
			// public void input(AbstractEntity model) {
			// model(model, propertyPath);
			// viewer.refresh(true, true);
			// }

		};
	}

	// TODO analizar el InputResolver... ya no va más... no?
	@Deprecated
	private InputResolver resolver = new NullInputResolver();
	private Paging paging;

	private String propertyPath = null;

	private AbstractEntity model;

	/**
	 * 
	 * <b>tiene que ser lo último en configurar</b>
	 * 
	 * @param model
	 * @param propertyPath
	 * @return
	 */
	// TODO que no sea si o si el último en configurar
	// TODO ¿por qué obliga a que sea un AbstractEntity?
	public TableBuilder model(AbstractEntity model, String propertyPath) {
		this.propertyPath = propertyPath;
		this.model = model;
		resolver = new OgnlRefreshableInputResolver(model, propertyPath, this);
		paging = new Paging(model, propertyPath, viewer, this);
		return input(paging.list(), Object.class);
	}

	// TODO diseñar mejor, hacer un resolver para sacar el ConfigurationScope y
	// su respectivo getInt
	private String keyLimit = "asdf_asdf";

	@Override
	public int limit() {
		IEclipsePreferences prefs = ConfigurationScope.INSTANCE.getNode(Activator.id());
		return prefs.getInt(keyLimit, Integer.MAX_VALUE);
	}

	public TableBuilder keyLimit(String keyLimit) {
		this.keyLimit = keyLimit;
		return this;
	}

}

/**
 * TODO Analizar bien el caso en el que esto haría falta
 * 
 * @author diego
 * 
 */
interface InputResolver {
	void resolveInput(TableViewer viewer);
}

class NullInputResolver implements InputResolver {

	@Override
	public void resolveInput(TableViewer viewer) {

	}

}

class ConstantResolver implements InputResolver {
	private List input;

	public ConstantResolver(List cinput) {
		this.input = cinput;
	}

	public List input() {
		return input;
	}

	@Override
	public void resolveInput(TableViewer viewer) {
		viewer.setInput(input());
	}
}

/**
 * para poder hacer retarget sin construir toda la tabla devuelta....
 * 
 * @author diego
 * 
 */
// TODO
class OgnlRefreshableInputResolver implements InputResolver {

	private AbstractEntity model;
	private String propertyPath;
	private TableBuilder tb;

	public OgnlRefreshableInputResolver(AbstractEntity model, String propertyPath, TableBuilder tb) {
		this.model = model;
		this.propertyPath = propertyPath;
		this.tb = tb;
	}

	public List input() {
		try {
			return (List) Ognl.getValue(propertyPath, model);
		} catch (OgnlException e) {
			e.printStackTrace();
			return (List) Lists.newArrayList();
		}
	}

	@Override
	public void resolveInput(TableViewer viewer) {
		// tb.reInput(input());
		System.err.println("Not Implemented Yet!");
	}

}

// TODO que se pueda setear desde afuera?
class MyElementFilter extends ViewerFilter {
	private Object filter;

	public MyElementFilter(Object filter) {
		this.filter = filter;
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		return element.equals(filter);
	}
}
