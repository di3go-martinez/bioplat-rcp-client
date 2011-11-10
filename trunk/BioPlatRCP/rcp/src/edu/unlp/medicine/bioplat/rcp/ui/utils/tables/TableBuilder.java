package edu.unlp.medicine.bioplat.rcp.ui.utils.tables;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.jface.databinding.viewers.ViewerSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;

import edu.unlp.medicine.entity.generic.AbstractEntity;

public class TableBuilder {
    // TODO que sea no "rezisable"
    private static final ColumnBuilder HIDDEN_COLUMN = ColumnBuilder.create().resizable(false).editable(false).width(0);
    // TODO
    private static final ColumnBuilder ROW_SELECT_COLUMN = ColumnBuilder.create().checkbox();

    private TableViewer viewer;

    private TableBuilder(Composite parent) {
        viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);

        final Table table = viewer.getTable();
        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        // viewer.setContentProvider(new ArrayContentProvider());
        // Make the selection available to other views
        // getSite().setSelectionProvider(viewer);

        // Layout the viewer
        GridData gridData = new GridData(GridData.FILL, GridData.FILL, true, true);
        gridData.horizontalSpan = 2;
        viewer.getControl().setLayoutData(gridData);

    }

    public static TableBuilder create(Composite container) {
        return new TableBuilder(container);
    }

    /**
     * soporta databinding en la lista...
     * 
     * @param input
     * @param klazz
     * @return
     */
    private <T> TableBuilder input(List<T> input, Class<? extends AbstractEntity> klazz) {
        // ObservableListContentProvider provider = new
        // ObservableListContentProvider();
        // viewer.setContentProvider(provider);
        // viewer.setInput(new WritableList(listenToChangeList(input), klazz));
        ViewerSupport.bind(viewer, new WritableList(input, klazz), BeanProperties.value(""));
        return this;
    }

    public <T extends AbstractEntity> TableBuilder input(List<T> input) {
        return input(input, AbstractEntity.class);
    }

    private List<ColumnBuilder> columns = new ArrayList<ColumnBuilder>();

    public TableBuilder addColumn(ColumnBuilder cb) {
        columns.add(cb);
        return this;
    }

    private boolean built = false;
    private TableReference table;

    public TableReference build() {

        if (built) return table;

        int columnCount = 0;

        // TODO
        // ROW_SELECT_COLUMN.build(viewer, columnCount++);

        for (ColumnBuilder cb : columns)
            cb.build(viewer, columnCount++);

        viewer.refresh(true);

        built = true;

        return table = new TableReference() {

            @Override
            public void refresh() {
                if (!viewer.isBusy()) viewer.refresh();
            }
        };
    }

    /**
     * Cada vez que se modifica la lista (agrega o borra elementos) se refresca
     * el viewer
     * 
     * @param list
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private List listenToChangeList(final List list) {
        return new List() {
            private List delegatee = list;

            @Override
            public int size() {
                return delegatee.size();
            }

            @Override
            public boolean isEmpty() {
                return delegatee.isEmpty();
            }

            @Override
            public boolean contains(Object o) {
                return delegatee.contains(o);
            }

            @Override
            public Iterator iterator() {
                viewer.refresh();
                return delegatee.iterator();
            }

            @Override
            public Object[] toArray() {
                viewer.refresh();
                return delegatee.toArray();
            }

            @Override
            public Object[] toArray(Object[] a) {
                viewer.refresh();
                return delegatee.toArray(a);
            }

            @Override
            public boolean add(Object e) {
                boolean added = delegatee.add(e);
                viewer.refresh();
                return added;
            }

            @Override
            public boolean remove(Object o) {
                boolean removed = delegatee.remove(o);
                if (removed) viewer.refresh();
                return removed;
            }

            @Override
            public boolean containsAll(Collection c) {
                return delegatee.containsAll(c);
            }

            @Override
            public boolean addAll(Collection c) {
                boolean addedAll = delegatee.addAll(c);
                viewer.refresh();
                return addedAll;
            }

            @Override
            public boolean addAll(int index, Collection c) {
                boolean addedAll = delegatee.addAll(index, c);
                viewer.refresh();
                return addedAll;
            }

            @Override
            public boolean removeAll(Collection c) {
                boolean removedAll = delegatee.removeAll(c);
                viewer.refresh();
                return removedAll;
            }

            @Override
            public boolean retainAll(Collection c) {
                return delegatee.retainAll(c);
            }

            @Override
            public void clear() {
                delegatee.clear();
                viewer.refresh();
            }

            @Override
            public boolean equals(Object o) {
                return delegatee.equals(o);
            }

            @Override
            public int hashCode() {
                return delegatee.hashCode();
            }

            @Override
            public Object get(int index) {
                return delegatee.get(index);
            }

            @Override
            public Object set(int index, Object element) {
                return delegatee.set(index, element);
            }

            @Override
            public void add(int index, Object element) {
                delegatee.add(index, element);
            }

            @Override
            public Object remove(int index) {
                return delegatee.remove(index);
            }

            @Override
            public int indexOf(Object o) {
                return delegatee.indexOf(o);
            }

            @Override
            public int lastIndexOf(Object o) {
                return delegatee.lastIndexOf(o);
            }

            @Override
            public ListIterator listIterator() {
                return delegatee.listIterator();
            }

            @Override
            public ListIterator listIterator(int index) {
                return delegatee.listIterator(index);
            }

            @Override
            public List subList(int fromIndex, int toIndex) {
                return delegatee.subList(fromIndex, toIndex);
            }
        };
    }
}
