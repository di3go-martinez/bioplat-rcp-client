package edu.unlp.medicine.bioplat.rcp.ui.biomarker.editors;

import static edu.unlp.medicine.bioplat.rcp.widgets.Widgets.*;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.internal.AbstractSelectionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableMap;

import edu.unlp.medicine.bioplat.rcp.editor.AbstractEditorPart;
import edu.unlp.medicine.bioplat.rcp.editor.Constants;
import edu.unlp.medicine.bioplat.rcp.ui.biomarker.editors.nls.Messages;
import edu.unlp.medicine.bioplat.rcp.ui.entities.actions.CopyColumnTextMenuItemDescriptor;
import edu.unlp.medicine.bioplat.rcp.ui.experiment.editors.ShowHideColumnMenuItemDescriptor;
import edu.unlp.medicine.bioplat.rcp.ui.utils.Models;
import edu.unlp.medicine.bioplat.rcp.ui.utils.accesors.OgnlAccesor;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.ColumnBuilder;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableBuilder;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableBuilder.MenuBuilder;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableReference;
import edu.unlp.medicine.bioplat.rcp.utils.GUIUtils;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.bioplat.rcp.widgets.Widget;
import edu.unlp.medicine.bioplat.rcp.widgets.Widgets;
import edu.unlp.medicine.bioplat.rcp.widgets.listeners.ModificationListener;
import edu.unlp.medicine.bioplat.rcp.widgets.listeners.ModificationTextEvent;
import edu.unlp.medicine.domainLogic.ext.metasignatureCommands.save.MetaSignatureMarshaller;
import edu.unlp.medicine.entity.biomarker.Biomarker;

public class BiomarkerEditor extends AbstractEditorPart<Biomarker>
    implements ISelectionChangedListener, TableReferenceProvider, MouseListener {

  private static Logger logger = LoggerFactory.getLogger(BiomarkerEditor.class);

  public static String id() {
    return "bio.plat.biomarker.editor"; //$NON-NLS-1$
  }

  private boolean autoUpdateTitle = true;

  private MetaSignatureMarshaller marshaller = new MetaSignatureMarshaller();

  public BiomarkerEditor(boolean autoUpdateTitle) {
    super(autoUpdateTitle);
    this.autoUpdateTitle = autoUpdateTitle;
  }

  public BiomarkerEditor() {
    this(true);
  }

  // TODO hasta implementar el save as
  @Override
  protected void doCreatePartControl(Composite parent) {

    Composite container = Widgets.createDefaultContainer(parent);
    container.setLayout(
        GridLayoutFactory.fillDefaults().numColumns(4).equalWidth(false).margins(10, 10).create());

    Biomarker model = model();

    // FIXME analizar no sacar ya que las acciones dependenden de que haya
    // un biomarcador activo de esta manera.
    Models.getInstance().setActiveBiomarker(model());

    Widget w = createTextWithLabel(container, Messages.BiomarkerEditor_name_label, model, "name");
    // TODO mover a la superclase
    if (autoUpdateTitle)
      w.addModificationListener(new ModificationListener() {
		
		@Override
		public void modify(ModificationTextEvent event) {
			setPartName(event.getNewText());
		}
	});

    Widgets.createTextWithLabel(container, Messages.BiomarkerEditor_gene_count_label, model,
        "geneCount").readOnly();
    Widgets.createTextWithLabel(container, Messages.BiomarkerEditor_author_label, model, "author");
    // Widgets.createTextWithLabel(container,
    // Messages.BiomarkerEditor_original_gene_count_label, model,
    // "originalNumberOfGenes").readOnly();
    Widgets.createMultiTextWithLabel(container, Messages.BiomarkerEditor_description_label, model,
        "description");

    // Label label = GUIUtils.addiItalicText(container, "\nHelp: You can use plus button on the
    // toolbar for addding genes.\n\n", 8);


    Composite compHelp = new Composite(container, SWT.NONE);
    compHelp.setLayout(new GridLayout(2, false));
    GridData gridData = GridDataFactory.fillDefaults().span(2, 2).create();
    gridData.horizontalAlignment = GridData.CENTER;
    compHelp.setLayoutData(gridData);

    final Button button = new Button(compHelp, SWT.PUSH);
    GridData gridData2 = new GridData();
    gridData.horizontalAlignment = GridData.CENTER;
    button.setImage(PlatformUIUtils.findImage("help.png"));
    button.setLayoutData(gridData2);
    button.setText("How can i add genes?");
    button.addListener(SWT.Selection, new Listener() {
      public void handleEvent(Event e) {
        switch (e.type) {
          case SWT.Selection:
            PlatformUIUtils.openInformation("How can i add genes",
                "You should use plus button on the toolbar. Then you can write them down or paste them. You can use Gene Symbol, Entrez id o ensemblId'");
            break;
        }
      }
    });


    final Button button2 = new Button(compHelp, SWT.PUSH);
    GridData gridData3 = new GridData();
    gridData.horizontalAlignment = GridData.CENTER;
    button2.setImage(PlatformUIUtils.findImage("help.png"));
    button2.setLayoutData(gridData3);
    button2.setText("How can i do survival analysis?");
    button2.addListener(SWT.Selection, new Listener() {
      public void handleEvent(Event e) {
        switch (e.type) {
          case SWT.Selection:
            PlatformUIUtils.openInformation("How can i do survival analysis",
                "Select the menu option or button named 'Statistic analysis for validating your gene signature'");
            break;
        }
      }
    });



    Composite subcontainer = Widgets.createDefaultContainer(container);
    subcontainer.setLayoutData(GridDataFactory.fillDefaults().span(4, 1).grab(true, true).create());
    createTable(subcontainer);

  }

  private void createTable(Composite parent) {

    final TableBuilder tb = TableBuilder.createVirtual(parent)//
        .model(model(), "genes")
    // TODO .hasRadioBehavior()
    ;
    // .input(model().getGenes());

		//tb.contextualMenuBuilder(MenuBuilder.create)
		tb.addColumn(ColumnBuilder.create().property("name").title("Gene Name").centered().accesor(OgnlAccesor.createFor("name")).addHeadeMenuItemDescriptor(createCopyColumn(false), createCopyColumn(true)).addHeadeMenuItemDescriptor(new ShowHideColumnMenuItemDescriptor(this, "Name", "name", false), //
				new ShowHideColumnMenuItemDescriptor(this, "Gene Alternative IDs (e.g EnsemblID)", "alternativeIds"), //
     new ShowHideColumnMenuItemDescriptor(this, "Gene Description", "description"), //
				new ShowHideColumnMenuItemDescriptor(this, "Gene Chromosome Location", "chromosomeLocation")))
    
    
    
		.addColumn(ColumnBuilder.create().rightAligned().property("entrezId").centered().title("Gene EntrezID"))
		.addColumn(ColumnBuilder.create().property("alternativeIds").title("Gene alternative IDs (e.g EnsemblID)").width(350).hidden().resizable(false).fixed())//
				//.addColumn(ColumnBuilder.create().title("Gene Name").centered().accesor(OgnlAccesor.createFor("name")))//
				.addColumn(ColumnBuilder.create().property("description").title("Gene Description").width(800));
    
    
    tr = tb.build();

    tr.addSelectionChangeListener(this);

    // TODO no usar getTable, agregar a TableReference
    tr.getTable().addMouseListener(this);

  }

  protected CopyColumnTextMenuItemDescriptor createCopyColumn(boolean all) {
    final String name = "Copy " + ((all) ? "All" : "Selected") + " Genes (Gene Symbol)";
    return new CopyColumnTextMenuItemDescriptor(
        new FromTabletMenuItemDescriptorProvider(this, name, OgnlAccesor.createFor("name"))
            .includeAll(all)) {
      // FIXME si se usa el default (\n) no copia bien los genes
      // en la acción de agregado de genes: ver por qué y/o
      // dejarlo así
      @Override
      protected String itemSeparator() {
        return "\t";
      }

    };
  }

  private TableReference tr;

  @Override
  protected Observer createModificationObserver() {

    return new Observer() {

      @Override
      public void update(Observable o, Object arg) {
        // tr.refresh();
        tr.input(null);
      }
    };
  }

  @Override
  protected Map<Object, IStructuredSelection> getAdditionalSelections() {
    final IStructuredSelection element = new StructuredSelection(tr.focusedElements());
    final IStructuredSelection element1 = new StructuredSelection(tr.selectedElements());
    return ImmutableMap.of((Object) Constants.GENES, element, Constants.SELECTED_GENES, element1);
  }

  @SuppressWarnings("restriction")
  @Override
  public void selectionChanged(SelectionChangedEvent event) {
    // FIXME sacar el downcast, avisar a de una manera prolija
    try {
      AbstractSelectionService ass =
          (AbstractSelectionService) getSite().getWorkbenchWindow().getSelectionService();
      ass.setActivePart(null);
      ass.setActivePart(this);
    } catch (NullPointerException npe) {
      logger.debug("Null pointer exception on selection changed");
    }
  }



  // TODO es hasta definir el save as.
  @Override
  public final void doSave(IProgressMonitor monitor) {
    FileDialog dialog = new FileDialog(this.getSite().getShell(), SWT.SAVE);
    dialog.setFilterNames(new String[] {"BPL Files"});
    dialog.setFilterExtensions(new String[] {"*.bpl"}); // Windows // wild

    String filename = dialog.open();
    if (filename != null)
      marshaller.marshal(this.model(), filename);
  }

  @Override
  public TableReference tableReference() {
    return tr;
  }

  @Override
  public void mouseDoubleClick(MouseEvent e) {
    // TODO Auto-generated method stub

  }

  @Override
  public void mouseDown(MouseEvent e) {
    // TODO Auto-generated method stub

  }

  @Override
  public void mouseUp(MouseEvent e) {
    if (e.button == 3) {
      final Menu menu = new Menu(tr.getTable());
      MenuItem item = new MenuItem(menu, SWT.PUSH);
      item.setText("Menu");
      tr.getTable().setMenu(menu);
    }

  }

}
