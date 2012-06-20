package edu.unlp.medicine.bioplat.rcp.ui.utils.tables;

import java.util.List;

import ognl.Ognl;
import ognl.OgnlException;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

public class Paging<T> {

	private static Logger logger = LoggerFactory.getLogger(Paging.class);

	// TODO no es configurable, podría serlo...
	private int pagesize = 100;
	private int currentElementIndex = 0;

	// lista de elementos cargados
	private List<T> realList = Lists.newArrayList();

	private Object model;
	private String propertyPath;

	private TableConfigurer tableConfigurer;

	private boolean virtual;

	// listener para decidir el cargado de páginas
	private Object listener;

	private TableViewer viewer;

	/**
	 * 
	 * Crea un manejador de paginado, muestra la primer página sobre el viewer
	 * 
	 * @param model
	 * @param propertyPath
	 * @param viewer
	 */
	public Paging(Object model, String propertyPath, final TableViewer viewer, TableConfigurer config) {
		this.model = model;
		this.propertyPath = propertyPath;
		this.tableConfigurer = config;

		loadNextPage();// la primera página

		this.viewer = viewer;
		// FIXME no anda cuando se baja con la flecha y no es virtual la tabla
		final Table table = viewer.getTable();
		final ScrollBar scrollbar = table.getVerticalBar();

		if (scrollbar == null)
			logger.warn("No se agregará el listener de paginado ya que no está disponible el scrollbar");
		else {
			virtual = (table.getStyle() & SWT.VIRTUAL) == SWT.VIRTUAL;
			if (virtual) {
				listener = new Listener() {
					@Override
					public void handleEvent(Event event) {
						// TableItem item = (TableItem) event.item;
						int index = event.index;
						// si el index se acerca al último de los elementos se
						// trae la próxima página
						if (index + 1 >= list().size()) {

							loadNextPage();
							// TODO no hace falta hacer un refresh de toda la
							// tabla, y habría que hacer un update desde el
							// último que se estaba viendo
							viewer.refresh(true, false);

						}
						// item.setText(itemStrings [index]);
						// System.out.println(index);
					}

				};
				table.addListener(SWT.SetData, (Listener) listener);
			} else {// !virtual
				listener = new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						if ((scrollbar.getSelection() != 0) && (scrollbar.getMaximum() == (scrollbar.getThumb() + scrollbar.getSelection()))) {
							// se asume que la lista donde se carga la próxima
							// página es la que está siendo "apuntada" por el
							// viewer
							loadNextPage();
							viewer.refresh(true, false);
						}
					}
				};
				scrollbar.addSelectionListener((SelectionAdapter) listener);
			}
		}

	}

	/**
	 * Retorna el último elemento de la lista
	 * 
	 * @return el último elemento de la lista o null si la lista está vacía
	 */
	private Object lastElement() {
		int size = list().size();
		if (size == 0)
			return null;
		else
			return list().get(size - 1);
	}

	/**
	 * 
	 * Crea un objeto paging tomando como base basePaging, por ejemplo para
	 * cargar la misma cantidad de páginas, etc. Se utiliza ante un cambio de
	 * modelo o propertyPath por ejemplo.
	 * 
	 * @param model
	 * @param propertyPath
	 * @param viewer
	 * @param config
	 * @param basePaging
	 */
	public Paging(Object model, String propertyPath, final TableViewer viewer, TableConfigurer config, Paging basePaging) {
		this(model, propertyPath, viewer, config);
		if (basePaging == null) {
			logger.warn("Se inicializó un nuevo paging ya que el que se intenta replicar no existía...");
			return;
		}
		basePaging.unplug();
		// está cargada la primera página, tengo que cargar una menos
		int cantPages = basePaging.getCurrentPageNumber();
		for (int i = 1; i < cantPages; i++)
			loadNextPage();

	}

	private int getCurrentPageNumber() {
		return this.list().size() / this.pagesize;
	}

	/**
	 * Lista los elementos que estan cargados en función de la cantidad de
	 * páginas que se cargaron
	 * 
	 * list().list() == list()
	 * 
	 * @return
	 */
	public List<T> list() {
		return realList;
	}

	private void loadNextPage() {
		logger.debug("Cargando página " + ((realList.size() / pagesize) + 1));

		try {
			for (int j = 0; j < pagesize; j++) {
				T e = findNextElement();
				if (e != null)
					realList.add(e);
			}
		} catch (EndOfListException e) {
			logger.debug("Se alcanzó el final de la lista");
		}
	}

	/**
	 * @return el próximo elemento o null si no hay
	 * @throws EndOfListException
	 */
	private T findNextElement() throws EndOfListException {
		T result = null;
		try {
			if (currentElementIndex < limit()) { // si entra uno más
				result = (T) Ognl.getValue(propertyPath + "[" + currentElementIndex + "]", model);
				currentElementIndex++;
			}
		} catch (IndexOutOfBoundsException e) {
			throw new EndOfListException();
		} catch (OgnlException e) {
			logger.error("Error intentando acceder al elemento de la lista " + model + "." + propertyPath + "[" + currentElementIndex + "]", e);
		}
		return result;
	}

	private int limit() {
		return tableConfigurer.limit();
	}

	public int pagesize() {
		return pagesize;
	}

	/**
	 * Desengancha los listeners del viewer
	 */
	private void unplug() {
		final Table table = viewer.getTable();
		if (virtual)
			table.removeListener(SWT.SetData, (Listener) listener);
		else
			table.getVerticalBar().removeSelectionListener((SelectionListener) listener);
	}

	private static class EndOfListException extends Exception {
		private static final long serialVersionUID = 1L;
	}
}
