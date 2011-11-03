package edu.unlp.medicine.bioplat.rcp.ui.utils.tables;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;

public class TableBuilder {

    private static final ColumnBuilder HIDDEN_COLUMN = ColumnBuilder.create().editable(false).width(0);

    private static final ColumnBuilder ROW_SELECT_COLUMN = ColumnBuilder.create().checkbox();

    private TableViewer viewer;

    private TableBuilder(Composite parent) {
        viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);

        final Table table = viewer.getTable();
        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        viewer.setContentProvider(new ArrayContentProvider());
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

    public TableBuilder input(Object input) {
        viewer.setInput(input);
        return this;
    }

    private List<ColumnBuilder> columns = new ArrayList<ColumnBuilder>();

    public TableBuilder addColumn(ColumnBuilder cb) {
        columns.add(cb);
        return this;
    }

    private boolean built = false;

    public void build() {

        if (built) return;

        int columnCount = 0;

        HIDDEN_COLUMN.build(viewer, columnCount++);
        ROW_SELECT_COLUMN.build(viewer, columnCount++);


        for (ColumnBuilder cb : columns)
            cb.build(viewer, columnCount++);

        viewer.refresh(true);

        built = true;
    }

    /**
     * No va este m�todo, en todo caso se deber�a hacer que el m�todo build
     * devuelva un objeto "facilitador"
     */
    @Deprecated
    public void refresh() {
        viewer.refresh();
    }

}
