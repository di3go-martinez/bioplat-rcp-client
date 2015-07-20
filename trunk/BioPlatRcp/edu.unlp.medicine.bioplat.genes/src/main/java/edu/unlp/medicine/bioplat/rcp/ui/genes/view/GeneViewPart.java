package edu.unlp.medicine.bioplat.rcp.ui.genes.view;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;

import com.google.common.collect.Lists;

import edu.unlp.medicine.bioplat.core.Activator;
import edu.unlp.medicine.bioplat.rcp.core.selections.MultipleSelection;
import edu.unlp.medicine.bioplat.rcp.editor.Constants;
import edu.unlp.medicine.bioplat.rcp.ui.genes.GenesPluginActivator;
import edu.unlp.medicine.bioplat.rcp.ui.genes.startup.InitializeGenesUrlStartup;
import edu.unlp.medicine.bioplat.rcp.ui.genes.view.parser.GeneUrl;
import edu.unlp.medicine.bioplat.rcp.ui.genes.view.parser.GeneUrlParser;
import edu.unlp.medicine.bioplat.rcp.ui.genes.view.preferences.ExternalGeneInformationPage;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUtils;
import edu.unlp.medicine.bioplat.rcp.widgets.Widget;
import edu.unlp.medicine.bioplat.rcp.widgets.Widgets;
import edu.unlp.medicine.entity.gene.Gene;

/**
 * 
 * Vista de un Gen
 * 
 * @author diego martínez
 * 
 */
public class GeneViewPart extends ViewPart {

	//private static final String LOADING_HTML = "<div style='{position:absolute;right:1;top:1;}'><i>Loading...</i></div>";

	public static String id() {
		return "edu.medicine.bioplat.rcp.gene.view";
	}

	// problema con las páginas que quedan cacheadas, por ejemplo no carga el
	// estilo para la página cacheada...
	// private Cache<String, String> genBrowserCache =
	// CacheBuilder.newBuilder().expireAfterAccess(10,
	// TimeUnit.MINUTES).build();

	private ISelectionListener listener;
	private Gene currentGene;

	private Composite container;
	private List<Widget> widgets = Lists.newArrayList();
	// private List<String> browserTitles = Lists.newArrayList();
	private List<Browser> browsers = Lists.newArrayList();

	// urls rest configuradas, contiene variables seguramente ej el id del gen
	// Agrego la de NCBI por default
	private String[] DEFAULTS = InitializeGenesUrlStartup.fillDefaults();

	private List<GeneUrl> geneUrls = Lists.newArrayList();
	private CTabFolder tabContainer;
	
	
	@Override
	public void createPartControl(final Composite parent) {
		
		Action actionClean = new Action("Search All", Activator.imageDescriptorFromPlugin("resources/icons/mundo.png")) {

			@Override
			public void run() {
				int i = 0;
				for (GeneUrl gurl : geneUrls) {
					final String url = gurl.url(currentGene);
					if (!url.equals(browsers.get(i).getUrl())) {
						tabContainer.setSelection(i+1);
						browsers.get(i).setUrl(url);
					}
					i++;

				}
			}
		};
		
		IActionBars actionBars = getViewSite().getActionBars();
		IMenuManager dropDownMenu = actionBars.getMenuManager();
		IToolBarManager toolBar = actionBars.getToolBarManager();
		dropDownMenu.add(actionClean);
		toolBar.add(actionClean);
		
		getSelectionService().addSelectionListener(listener = new ISelectionListener() {

			private Gene oldGene;

			@Override
			public void selectionChanged(IWorkbenchPart part, ISelection selection) {
				Gene gene = null;

				if (!isMySelf(part) && isValidSelection(selection))
					gene = getGeneSelection(selection);

				if (gene != null && (oldGene == null || !gene.equals(oldGene)))
					updateComposite(parent, gene);

				oldGene = gene;

			}

			private Gene getGeneSelection(ISelection selection) {
				Object result = null;
				if (selection instanceof MultipleSelection)
					result = ((MultipleSelection) selection).get(Constants.GENES).getFirstElement();

				// result ((StructuredSelection) selection).getFirstElement();
				return (Gene) result;
			}

			private boolean isValidSelection(ISelection s) {
				return !s.isEmpty() && //
						s instanceof MultipleSelection //
				// && ((StructuredSelection) s).getFirstElement()
				// instanceof Gene
				;
			}

			private boolean isMySelf(IWorkbenchPart part) {
				return part == GeneViewPart.this;
			}
			
			
			

		});
		
		parent.addDisposeListener(new DisposeListener() {

            @Override
            public void widgetDisposed(DisposeEvent e) {
               for(Browser browser : browsers){
            	   browser.stop();
            	   //PlatformUIUtils.findDisplay().timerExec(-1, freezedPageCheckers.get(browsers.indexOf(browser))); 
               }
            }
        });
		
		
				
	}

	@Override
	public void dispose() {
		getSelectionService().removeSelectionListener(listener);
		super.dispose();
	}

	// TODO mover a un abstract...?
	protected ISelectionService getSelectionService() {
		return getSite().getWorkbenchWindow().getSelectionService();
	}


	private void updateComposite(Composite parent, Gene gene) {
		currentGene = gene;

		// c.dispose();
		// c = null;

		// c = new PageBook(parent, SWT.BORDER);
		// c.setLayoutData(GridDataFactory.fillDefaults().grab(true,
		// true).create());
		if (container == null) {
			container = Widgets.createDefaultContainer(parent);
			container.setLayout(GridLayoutFactory.fillDefaults().numColumns(2).create());
		}

		container.setVisible(gene != null);

		if (gene != null)

			if (viewBuilt())
				refreshView(gene);
			else
				buildView(gene);

		// FIXME no se repinta bien la primera vez que aparece si no se crea el
		// composite container, se fuerza con el -1
		parent.setSize(parent.getSize().x, parent.getSize().y);
		parent.layout();
		parent.redraw();
		parent.update();

	}

	private boolean viewBuilt() {
		return !widgets.isEmpty();
	}

	private void refreshView(Gene gene) {
		for (Widget w : widgets)
			w.retarget(gene);

		for (Browser b : browsers) {
			b.stop();
			b.setText("", false);
		}
				
		updateTitle(gene);
		tabContainer.setSelection(0);
	}

	private void updateTitle(Gene gene) {
		setPartName("Gen:" + gene);
	}

	private void buildView(Gene gene) {

		tabContainer = new CTabFolder(container, SWT.BORDER);
		tabContainer.setLayout(GridLayoutFactory.swtDefaults().create());
		tabContainer.setLayoutData(GridDataFactory.fillDefaults().span(2, 1).grab(true, true).create());

		// para acomodar problemas entre el foco y la selección de una solapa en
		// la vista de genes y la carga de url "lazy"
		/*tabContainer.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseUp(MouseEvent e) {
				setMeFocus();				
			}
		});*/
		
		tabContainer.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(e.item != null && e.item instanceof CTabItem){
					CTabItem tabItem = (CTabItem) e.item;
					int index = tabItem.getParent().getSelectionIndex() - 1; 
					// Evitamos el primer tab 
					if(index > -1){
						final String url = geneUrls.get(index).url(currentGene);
						Browser browser = browsers.get(index);
						if (!url.equals(browser.getUrl())) {
							final String[] header = new String[] {
					                "Cache-Control: no-cache, no-store, must-revalidate",
					                "Pragma: no-cache",
					                "Expires: 0",
					                "Accept: */*",
					                "Accept-Encoding: gzip,deflate,sdch",
					                "Accept-Charset: ISO-8859-1,utf-8;q=0.7,*;q=0.3" };
							browser.setUrl(url,null,header);
						}	
					}
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
	
		
		// Creo la solapa cabecera
		CTabItem headerTab = new CTabItem(tabContainer, SWT.NONE);
		Composite c = Widgets.createDefaultContainer(tabContainer, 2);
		final GridLayout layout = GridLayoutFactory.createFrom((GridLayout) c.getLayout()).margins(5, 5).create();
		c.setLayout(layout);
		widgets.add(Widgets.createTextWithLabel(c, "Name", gene, "name").readOnly());
		widgets.add(Widgets.createTextWithLabel(c, "Description", gene, "description").readOnly());
		widgets.add(Widgets.createTextWithLabel(c, "EntrezID", gene, "entrezIdAsString").readOnly());
		widgets.add(Widgets.createTextWithLabel(c, "Ensemble Id", gene, "ensemblId").readOnly());
		// widgets.add(Widgets.createTextWithLabel(c, "Alternative IDs", gene,
		// "alternativeIds").readOnly());
		widgets.add(Widgets.createTextWithLabel(c, "Chromosome Location", gene, "chromosomeLocation").readOnly());

		headerTab.setControl(c);
		headerTab.setText("Header");

		// Creo las solapas de los browsers
		buildBrowsers(gene, tabContainer);

		updateTitle(gene);
		tabContainer.setSelection(0);
	}

	private void buildBrowsers(Gene gene, CTabFolder t) {

		String urls = PlatformUtils.preferences(GenesPluginActivator.id()).get(ExternalGeneInformationPage.URLS, "");

		if (urls.isEmpty()) {

			urls = StringUtils.join(DEFAULTS, '|');
			PlatformUtils.preferences(GenesPluginActivator.id()).put(ExternalGeneInformationPage.URLS, urls);
		}

		geneUrls = GeneUrlParser.parse(urls);
		
		
		
		for (final GeneUrl gurl : geneUrls) {

			final Browser browser = new Browser(t, SWT.NONE); 
			browser.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).span(2, 1).create());
			
			browsers.add(browser);

			// agrego el tab contendor
			final CTabItem tab = new CTabItem(t, SWT.NONE);
			tab.setText(gurl.title());
			

			final Composite loadingMessage = Widgets.createDefaultContainer(tabContainer, 1);
			loadingMessage.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).span(2, 1).create());
			loadingMessage.setBackground(new Color(null, 255, 255, 255));
			
			final Label label = new Label(loadingMessage, SWT.NONE);
			label.setText("Getting required information from server, plase wait");
			label.setBackground(new Color(null, 255, 255, 255));
			final ProgressBar progressBar = new ProgressBar(loadingMessage, SWT.INDETERMINATE);
			
			browser.addProgressListener( new ProgressListener() {
				
				public boolean cargaCompleta = false;
				
				@Override
				public void completed(ProgressEvent event) {
					// TODO que no cachee las páginas cargadas con error...
					// genBrowserCache.put(browser.getUrl(), browser.getText());
					tab.setText(gurl.title());
					tab.setControl(browser);
				}

				@Override
				public void changed(final ProgressEvent event) {
						if (event.current != 0) {
							if(event.current < event.total){
								tab.setControl(loadingMessage);	
								tab.setText(gurl.title() + "(Loading: " + (event.current * 100.0 / event.total) + "%)");
							}
						}
				}
			});
			
		}
	}

	
	@Override
	public void setFocus() {
	}

}
