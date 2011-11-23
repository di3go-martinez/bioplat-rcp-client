package edu.unlp.medicine.bioplat.rcp.ui.utils.tables;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TableColumn;

import edu.unlp.medicine.bioplat.rcp.ui.utils.accesors.Accesor;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.editing.support.CheckBoxEditingSupport;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.editing.support.NumberEditingSupport;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.editing.support.TextEditingSupport;

public class ColumnBuilder {

    public static ColumnBuilder create() {
        return new ColumnBuilder();
    }

    private ColumnLabelProvider clp = new ColumnLabelProvider() {
        @Override
        public String getText(Object element) {
            if (accesor != null) return accesor.get(element).toString();
            else return super.getText(element);

        };
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
        if (editingSupport != null && editable) tvc.setEditingSupport(newInstance(editingSupport, viewer));

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
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    private TableViewerColumn createTableViewerColumn(TableViewer viewer, String title, int bound, final int colNumber, int alignStyle, boolean rezisable) {
        final TableViewerColumn viewerColumn = new TableViewerColumn(viewer, alignStyle);
        final TableColumn column = viewerColumn.getColumn();
        column.setText(title);
        column.setWidth(bound);
        column.setResizable(rezisable);// TODO
        column.setMoveable(true);// TODO
        return viewerColumn;

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

    public ColumnBuilder labelprovider(ColumnLabelProvider columnLabelProvider) {
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

    public ColumnBuilder numeric() {
        return align(SWT.RIGHT).cellEditorSupport(NumberEditingSupport.class);
    }

    public ColumnBuilder checkbox() {
        return width(20).cellEditorSupport(CheckBoxEditingSupport.class);
    }

    public ColumnBuilder resizable(boolean b) {
        resizable = b;
        return this;
    }

}
