package edu.unlp.medicine.bioplat.rcp.ui.genes.view;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;

import com.google.common.collect.Lists;

import edu.unlp.medicine.bioplat.rcp.core.selections.MultipleSelection;
import edu.unlp.medicine.bioplat.rcp.editor.Constants;
import edu.unlp.medicine.bioplat.rcp.ui.genes.preferences.ExternalGeneInformationPage;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUtils;
import edu.unlp.medicine.bioplat.rcp.widgets.Widget;
import edu.unlp.medicine.bioplat.rcp.widgets.Widgets;
import edu.unlp.medicine.entity.gene.Gene;

public class GeneViewPart extends ViewPart {

	private static final String GEN_HOLDER = "{genId}";
	private static final String REST_URL_NCBI = "http://www.ncbi.nlm.nih.gov/gene/?term=" + GEN_HOLDER;

	public static String id() {
		return "edu.medicine.bioplat.rcp.gene.view";
	}

	// private Cache<String, String> genBrowserCache =
	// CacheBuilder.newBuilder().expireAfterAccess(10,
	// TimeUnit.MINUTES).build();

	private ISelectionListener listener;

	@Override
	public void createPartControl(final Composite parent) {

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

	private Composite container;
	private List<Widget> ws = Lists.newArrayList();
	private List<Browser> browsers = Lists.newArrayList();
	// urls rest configuradas, contiene variables seguramente ej el id del gen
	private String[] $variableUrls;

	private void updateComposite(Composite parent, Gene gene) {

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
		return !ws.isEmpty();
	}

	private void refreshView(Gene gene) {
		for (Widget w : ws)
			w.retarget(gene);

		for (String resturl : $variableUrls)
			seturl(convertRestUrl(resturl, gene));
		setPartName(gene.toString());
	}

	private void buildView(Gene gene) {
		ws.add(Widgets.createTextWithLabel(container, "nombre", gene, "name", true));

		buildBrowsers(gene);

		setPartName(gene.toString());
	}

	private void buildBrowsers(Gene gene) {

		String urls = PlatformUtils.preferences().get(ExternalGeneInformationPage.URLS, "");

		if (urls.isEmpty())
			return;

		CTabFolder t = new CTabFolder(container, SWT.BORDER);
		t.setLayout(GridLayoutFactory.fillDefaults().create());
		t.setLayoutData(GridDataFactory.fillDefaults().span(2, 1).grab(true, true).create());

		$variableUrls = StringUtils.split(urls, '|');

		for (String url : $variableUrls) {
			final Browser browser = new Browser(t, SWT.BORDER);
			browser.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).span(2, 1).create());
			final String realurl = convertRestUrl(url, gene);
			seturl(browser, realurl);
			browsers.add(browser);

			// agrego el tab contendor
			final CTabItem i = new CTabItem(t, SWT.NONE);
			i.setControl(browser);
			i.setText(realurl);

			browser.addProgressListener(new ProgressListener() {

				@Override
				public void completed(ProgressEvent event) {
					i.setText(((Browser) event.widget).getUrl());
				}

				@Override
				public void changed(ProgressEvent event) {
					// TODO Auto-generated method stub

				}
			});

		}

		// browser.addProgressListener(new ProgressListener() {
		//
		// @Override
		// public void completed(ProgressEvent event) {
		// // TODO que no cachee las páginas cargadas con error...
		// // genBrowserCache.put(browser.getUrl(), browser.getText());
		// }
		//
		// @Override
		// public void changed(ProgressEvent event) {
		//
		// }
		// });
	}

	private void seturl(Browser browser, final String url) {
		// TODO hacer uso de la cache String html =
		// genBrowserCache.getIfPresent(url);

		// if (html == null)
		browser.setUrl(url);
		// else
		// browser.setText(html, false);
	}

	/**
	 * Configura las urls de todos los browsers abiertos
	 * 
	 * @param url
	 */
	private void seturl(final String url) {
		for (Browser browser : browsers) {
			seturl(browser, url);
		}
	}

	@Deprecated
	private String makeRestUrl(Gene input) {
		return REST_URL_NCBI.replace(GEN_HOLDER, input.getEntrezIdAsString());
	}

	private String convertRestUrl(String restUrl, Gene input) {
		return restUrl.replace(GEN_HOLDER, input.getEntrezIdAsString());
	}

	@Override
	public void setFocus() {
	}

}
