package edu.unlp.medicine.bioplat.rcp.ui.utils.tables;

import java.util.List;

import ognl.Ognl;
import ognl.OgnlException;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
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
	private List<T> realList = Lists.newArrayList();
	private Object model;
	private String propertyPath;

	private TableConfigurer tableConfigurer;

	/**
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

		// FIXME no anda cuando se baja con la flecha y no es virtual la table
		final Table table = viewer.getTable();
		final ScrollBar scrollbar = table.getVerticalBar();

		if (scrollbar == null)
			logger.warn("No se agregará el listener de paginado ya que no está disponible el scrollbar");
		else {
			if ((table.getStyle() & SWT.VIRTUAL) == SWT.VIRTUAL) // isVirtual?
				table.addListener(SWT.SetData, new Listener() {
					@Override
					public void handleEvent(Event event) {
						// TableItem item = (TableItem) event.item;
						int index = event.index;
						// si el index se acerca al último de los elementos se
						// trae la próxima página
						if (index + 1 >= list().size()) {
							loadNextPage();
							viewer.refresh(false, false);
						}
						// item.setText(itemStrings [index]);
						// System.out.println(index);
					}
				});
			else
				scrollbar.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						if ((scrollbar.getSelection() != 0) && (scrollbar.getMaximum() == (scrollbar.getThumb() + scrollbar.getSelection()))) {
							loadNextPage();
							viewer.refresh();
						}
					}
				});
		}

	}

	/**
	 * Lista los elementos que estan cargados en función de la cantidad de
	 * páginas que se cargaron
	 * 
	 * @return
	 */
	public List<T> list() {
		return realList;
	}

	private void loadNextPage() {
		logger.debug("Intentando cargar la página " + (realList.size() / pagesize) + 1);
		for (int j = 0; j < pagesize; j++) {
			T e = findNextElement();
			if (e != null)
				realList.add(e);
		}
	}

	/**
	 * @return el próximo elemento o null si no hay
	 */
	private T findNextElement() {
		T result = null;
		try {
			if (currentElementIndex < limit()) { // si entra uno más
				result = (T) Ognl.getValue(propertyPath + "[" + currentElementIndex + "]", model);
				currentElementIndex++;
			}
		} catch (IndexOutOfBoundsException e) {
			logger.debug("Se alcanzó el final de la lista");
		} catch (OgnlException e) {
			logger.error("Error intentando acceder al elemento", e);
		}
		return result;
	}

	private int limit() {
		return tableConfigurer.limit();
	}

	public int pagesize() {
		return pagesize;
	}
}
