package edu.unlp.medicine.bioplat.rcp.ui.biomarker.editors;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import edu.unlp.medicine.bioplat.rcp.editor.AbstractEditorPart;
import edu.unlp.medicine.bioplat.rcp.ui.biomarker.actions.OpenBiomarkerAction;
import edu.unlp.medicine.bioplat.rcp.ui.biomarker.editors.nls.Messages;
import edu.unlp.medicine.bioplat.rcp.ui.utils.accesors.OgnlAccesor;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.ColumnBuilder;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableBuilder;
import edu.unlp.medicine.bioplat.rcp.widgets.Widgets;
import edu.unlp.medicine.entity.biomarker.Biomarker;

public class BiomarkerEditor extends AbstractEditorPart<Biomarker> {

    public static String id() {
        return "bio.plat.biomarker.editor"; //$NON-NLS-1$
    }

    @Override
    protected void doCreatePartControl(Composite parent) {

        getSite().setSelectionProvider(createSP());

        Composite container = Widgets.createDefaultContainer(parent);
        container.setLayout(GridLayoutFactory.fillDefaults().numColumns(4).margins(10, 10).create());

        Biomarker model = model();

        Widgets.createTextWithLabel(container, Messages.BiomarkerEditor_name_label, model, "name");
        Widgets.readonly();
        // TODO mostrar el tamaño de la lista... si o si hacer método
        // getGeneCount??
        Widgets.createTextWithLabel(container, Messages.BiomarkerEditor_gene_count_label, model, "genes");
        Widgets.noreadonly();
        Widgets.createTextWithLabel(container, Messages.BiomarkerEditor_author_label, model, "author");
        Widgets.readonly();
        Widgets.createTextWithLabel(container, Messages.BiomarkerEditor_original_gene_count_label, model, "originalNumberOfGenes");
        Widgets.noreadonly();
        Widgets.createMultiTextWithLabel(container, Messages.BiomarkerEditor_description_label, model, "description");
        Widgets.readonly();
        Widgets.createTextWithLabel(container, Messages.BiomarkerEditor_significance_value, model, "significanceValue.pvalue");

        Composite subcontainer = Widgets.createDefaultContainer(container);
        subcontainer.setLayoutData(GridDataFactory.fillDefaults().span(4, 1).grab(true, true).create());
        createTable(subcontainer);

        Widgets.noreadonly();
    }

    /**
     * create Selection Provider
     * 
     * @return
     */
    private ISelectionProvider createSP() {
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
        ColumnBuilder columnBuilder;

        columnBuilder = ColumnBuilder.create().numeric().mutator(OgnlAccesor.createFor("entrezId")).title("Id");

        final TableBuilder tb = TableBuilder.create(parent).input(getInputElements());

        tb //
        .addColumn(ColumnBuilder.create().title("Nombre").centeredText().mutator(OgnlAccesor.createFor("name")))//
                // .addColumn(ColumnBuilder.create().title("Descripción").mutator(OgnlAccesor.createFor("description")))
                .addColumn(columnBuilder);

        tb.build();

        new Button(parent, SWT.FLAT).addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                tb.refresh();
            }
        });

    }

    private Object getInputElements() {
        return model().getGenes();
    }


}
