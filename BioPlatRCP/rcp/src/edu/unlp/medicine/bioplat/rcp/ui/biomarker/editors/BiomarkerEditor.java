package edu.unlp.medicine.bioplat.rcp.ui.biomarker.editors;

import java.util.Observable;
import java.util.Observer;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.widgets.Composite;

import edu.unlp.medicine.bioplat.rcp.editor.AbstractEditorPart;
import edu.unlp.medicine.bioplat.rcp.ui.biomarker.actions.OpenBiomarkerAction;
import edu.unlp.medicine.bioplat.rcp.ui.biomarker.editors.nls.Messages;
import edu.unlp.medicine.bioplat.rcp.ui.utils.accesors.OgnlAccesor;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.ColumnBuilder;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableBuilder;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableReference;
import edu.unlp.medicine.bioplat.rcp.widgets.Widgets;
import edu.unlp.medicine.entity.biomarker.Biomarker;

public class BiomarkerEditor extends AbstractEditorPart<Biomarker> {

    public static String id() {
        return "bio.plat.biomarker.editor"; //$NON-NLS-1$
    }

    @Override
    protected void doCreatePartControl(Composite parent) {

        getSite().setSelectionProvider(createSelectionProvider());

        Composite container = Widgets.createDefaultContainer(parent);
        container.setLayout(GridLayoutFactory.fillDefaults().numColumns(4).margins(10, 10).create());

        Biomarker model = model();

        Widgets.createTextWithLabel(container, Messages.BiomarkerEditor_name_label, model, "name");
        Widgets.createTextWithLabel(container, Messages.BiomarkerEditor_gene_count_label, model, "geneCount", true);
        Widgets.createTextWithLabel(container, Messages.BiomarkerEditor_author_label, model, "author");
        Widgets.createTextWithLabel(container, Messages.BiomarkerEditor_original_gene_count_label, model, "originalNumberOfGenes", true);
        Widgets.createMultiTextWithLabel(container, Messages.BiomarkerEditor_description_label, model, "description");
        Widgets.createTextWithLabel(container, Messages.BiomarkerEditor_significance_value, model, "significanceValue.pvalue", true);

        Composite subcontainer = Widgets.createDefaultContainer(container);
        subcontainer.setLayoutData(GridDataFactory.fillDefaults().span(4, 1).grab(true, true).create());
        createTable(subcontainer);

    }

    /**
     * create Selection Provider
     * 
     * @return
     */
    private ISelectionProvider createSelectionProvider() {
        return new ISelectionProvider() {
            private ISelection s;

            @Override
            public void setSelection(ISelection selection) {
                this.s = selection;
            }

            @Override
            public void removeSelectionChangedListener(ISelectionChangedListener listener) {
                System.out.println("TODO removeselectionChangedListener!");
            }

            @Override
            public ISelection getSelection() {
                return new ISelection() {

                    @Override
                    public boolean isEmpty() {
                        return false;
                    }

                    /**
                     * hace disponible el biomarker desde las acciones que estén
                     * registradas como selectionListeners
                     * 
                     * @see OpenBiomarkerAction
                     * @return
                     */
                    @SuppressWarnings("unused")
                    public Biomarker getBiomarker() {
                        return model();
                    }
                };
            }

            @Override
            public void addSelectionChangedListener(ISelectionChangedListener listener) {
                System.out.println("TODO addSelectionChangedListener!");
            }
        };
    }

    private void createTable(Composite parent) {

        final TableBuilder tb = TableBuilder.create(parent).input(model().getGenes());

        tr = tb.addColumn(ColumnBuilder.create().numeric().accesor(OgnlAccesor.createFor("entrezId")).title("Id"))
                .addColumn(ColumnBuilder.create().editable(true).title("Nombre").centeredText().accesor(OgnlAccesor.createFor("name")))
                .addColumn(ColumnBuilder.create().accesor(OgnlAccesor.createFor("description")).title("Descripción")).build();
    }

    private TableReference tr;

    @Override
    protected Observer createModificationObserver() {

        return new Observer() {

            @Override
            public void update(Observable o, Object arg) {
                tr.refresh();
            }
        };
    }

    // /**
    // * @deprecated siempre guardar el root, la implementación por default...
    // */
    // @Deprecated
    // @Override
    // protected void doSave0() {
    // for (Gene g : model().getGenes())
    // RepositoryFactory.getRepository().save(g);
    // }
}
