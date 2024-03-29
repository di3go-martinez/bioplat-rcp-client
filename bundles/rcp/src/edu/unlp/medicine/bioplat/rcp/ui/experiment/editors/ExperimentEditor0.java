package edu.unlp.medicine.bioplat.rcp.ui.experiment.editors;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import edu.unlp.medicine.bioplat.rcp.editor.AbstractEditorPart;
import edu.unlp.medicine.bioplat.rcp.editor.Constants;
import edu.unlp.medicine.bioplat.rcp.ui.biomarker.editors.FromTabletMenuItemDescriptorProvider;
import edu.unlp.medicine.bioplat.rcp.ui.biomarker.editors.TableReferenceProvider;
import edu.unlp.medicine.bioplat.rcp.ui.entities.actions.CopyColumnTextMenuItemDescriptor;
import edu.unlp.medicine.bioplat.rcp.ui.experiment.preferences.ExperimentGeneralPreferencePage;
import edu.unlp.medicine.bioplat.rcp.ui.utils.accesors.OgnlAccesor;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.ColumnBuilder;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableBuilder;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableReference;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.cells.CustomCellData;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.cells.CustomCellDataBuilder;
import edu.unlp.medicine.bioplat.rcp.ui.views.messages.Message;
import edu.unlp.medicine.bioplat.rcp.ui.views.messages.MessageManager;
import edu.unlp.medicine.bioplat.rcp.utils.Holder;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUtils;
import edu.unlp.medicine.bioplat.rcp.utils.events.GeneChangeEvent;
import edu.unlp.medicine.bioplat.rcp.widgets.Widget;
import edu.unlp.medicine.bioplat.rcp.widgets.Widgets;
import edu.unlp.medicine.bioplat.rcp.widgets.listeners.ModificationListener;
import edu.unlp.medicine.bioplat.rcp.widgets.listeners.ModificationTextEvent;
import edu.unlp.medicine.entity.experiment.AbstractExperiment;
import edu.unlp.medicine.entity.experiment.Experiment;
import edu.unlp.medicine.entity.experiment.Sample;
import edu.unlp.medicine.entity.gene.Gene;
import edu.unlp.medicine.entity.generic.AbstractEntity;

class ExperimentEditor0 extends AbstractEditorPart<AbstractExperiment> implements ISelectionChangedListener, TableReferenceProvider {

	private static final Logger logger = LoggerFactory.getLogger(ExperimentEditor0.class);
	private List<ExpressionDataModel> data;
	private TableReference tr;

	@Override
	protected void doCreatePartControl(Composite parent) {

		Composite container = new Composite(parent, SWT.BORDER);// Widgets.createDefaultContainer(parent)

		Composite c = new Composite(container, SWT.BORDER);
		c.setLayout(new GridLayout(4, false));
		c.setLayoutData(GridDataFactory.fillDefaults().span(1, 2).create());
		Widget w = Widgets.createTextWithLabel(c, "Name", model(), "name");
		w.addModificationListener(new ModificationListener() {

			@Override
			public void modify(ModificationTextEvent event) {
				setPartName(event.getNewText());

			}
		});

		Widgets.createTextWithLabel(c, "Genes", model(), "numberOfGenes").readOnly();
		Widgets.createTextWithLabel(c, "Author", model(), "author");
		Widgets.createTextWithLabel(c, "Samples", model(), "numberOfSamples").readOnly();

		// construyo el input para el tablebuilder en background
		BusyIndicator.showWhile(Display.getCurrent(), new Runnable() {

			@Override
			public void run() {
				data = ExpressionDataModel.create(model());
			}

		});

		Composite compHelp = new Composite(container, SWT.NONE);
		compHelp.setLayout(new GridLayout(1, false));
		GridData gridData = GridDataFactory.fillDefaults().span(1, 2).create();
		gridData.horizontalAlignment = GridData.CENTER;
		compHelp.setLayoutData(gridData);
		
		final Button button = new Button(compHelp, SWT.PUSH);
		GridData gridData2 = new GridData() ;
		gridData.horizontalAlignment = GridData.CENTER;
		//final Image image2 = new Image(PlatformUIUtils.findDisplay(), new ByteArrayInputStream(stream));
		button.setImage(PlatformUIUtils.findImage("help.png"));
		button.setLayoutData(gridData2);
		button.setText("How can i do survival analysis?");
        button.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event e) {
              switch (e.type) {
              case SWT.Selection:
            	PlatformUIUtils.openInformation("How can i validate data of this experiment", "If you want to do survival analysis using cluster by expression data, you have to create a Gene Signature. For that, follow this sequence: 1-Click on the 'name' column of the table below  2-Select copy all genes 3-Go to start menu, select 'gene Signature/Datasets option 4-Select 'Create gene Signature'  5-Throw the validation over this new gene signature using 'Statistic analysis using your gene signature'. \nYou can also do the same selecting a subset of genes on the table below and using 'create gene signature using selected genes'. \n\nBut, if you are not interested in using expression data and you would like to set cluster manually, you can do it directly over this expeirment using the option 'Operation/Statistic Analysis for validating this experiment setting clusters manually...'");
                break;
              }
            }
          });
        
        //Label label=GUIUtils.addiItalicText4(c, "\nIf you want to do survival analysis on the data of this experiment, clustering by expression data, you have to create a Gene Signature. Please, follow this sequence: 1-Click on the 'name' column of the table below  2-Select copy all genes 3-Go to start menu, select 'gene Signature/Experiments option 4-Select 'Create gene Signature'  5-Throw the validation over this new gene signature using 'Statistic analysis using your gene signature'. You can also do the same selecting a subset of genes selecting genes and using 'create gene signature using selected genes'. \nBut, if you are not interested in using expression data and you would like to set cluster manually, you can do it directly over this expeirment using the option 'Operation/Statistic Analysis for validating this experiment setting clusters manually...'\n\n", 8);
		
		
		TableBuilder tb = TableBuilder.create(container)//
				.keyLimit(ExperimentGeneralPreferencePage.EXPERIMENT_GRID_MAX_GENES)//
				.model(new AbstractEntity() {
					@SuppressWarnings("unused")
					// sí es used, por reflection...
					public List<ExpressionDataModel> getData() {
						return data;
					}
				}, "data")
		// .input(inputHolder.value())
		;

		// data[0] es porque ahí está el gen, ver ExpressionDataModel
		tb.addColumn(ColumnBuilder.create().title("Name").id("name").centered().resizable(true).property("data[0].value.name").fixed()
				.addHeadeMenuItemDescriptor(createCopyMenuForName(false), createCopyMenuForName(true)).addHeadeMenuItemDescriptor(//
						new ShowHideColumnMenuItemDescriptor(this, "Name", "name", false), //
						new ShowHideColumnMenuItemDescriptor(this, "Gene Alternative IDs (e.g EnsemblID)", "alternativeIds"), //
						new ShowHideColumnMenuItemDescriptor(this, "Gene Description", "description"),//
						new ShowHideColumnMenuItemDescriptor(this, "Gene Chromosome Location", "chromosomeLocation")))
				.addColumn(ColumnBuilder.create().title("Gene Entrez ID").centered().property("data[0].value.entrezId"))
				.addColumn(ColumnBuilder.create().title("Description").id("description").isHiden().property("data[0].value.description"))//
				.addColumn(ColumnBuilder.create().title("Chromosome Location").id("chromosomeLocation").isHiden().property("data[0].value.chromosomeLocation"))//
				.addColumn(ColumnBuilder.create().title("Alternative Ids").resizable(false).id("alternativeIds").property("data[0].value.alternativeIds").hidden().fixed());
		
			
		
		int index = 1;
		final List<Sample> sampleToLoad = resolveSamplesToLoad();
		for (Sample s : sampleToLoad)
			tb.addColumn( //
			ColumnBuilder.create().numeric().title(s.getName()) //
					.editable(false).property("data[" + index++ + "].value")//
					.addHeadeMenuItemDescriptor(new RemoveSampleColumnDescriptor(model())));

		tr = tb.build();
		tr.addSelectionChangeListener(this);
		GridLayoutFactory.fillDefaults().margins(10, 10).numColumns(1).generateLayout(container);
	}

	protected CopyColumnTextMenuItemDescriptor createCopyMenuForName(boolean all) {
		final String name = "Copy " + ((all) ? "All" : "Selected") + " Genes (Gene Symbol)";
		return new CopyColumnTextMenuItemDescriptor(new FromTabletMenuItemDescriptorProvider(this, name, OgnlAccesor.createFor("data[0].value.name")).includeAll(all)) {
			// FIXME si se usa el default (\n) no copia bien los genes
			// en la acción de agregado de genes: ver por qué y/o
			// dejarlo así
			@Override
			protected String itemSeparator() {
				return "\t";
			}
		};
	}

	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		Gene gene = ((ExpressionDataModel) event.getStructuredSelection().getFirstElement()).findGene();
		PlatformUtils.eventbus.instance.post(new GeneChangeEvent(gene ));
	}

	private List<Sample> resolveSamplesToLoad() {
		int max = ExperimentEditor.getSampleCountToLoad();
		return model().getSamples().subList(0, Math.min(model().getSampleCount(), max));
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Map<Object, IStructuredSelection> getAdditionalSelections() {
		Map<Object, IStructuredSelection> selections = Maps.newHashMap();

		List<ExpressionDataModel> elements = tr.focusedElements();

		List<Gene> genes = Lists.transform(elements, toGene());

		// pongo los genes con foco no necesariamente los que están
		// seleccionados
		selections.put(Constants.GENES, new StructuredSelection(genes));

		elements = tr.selectedElements();
		genes = Lists.transform(elements, toGene());

		selections.put(Constants.SELECTED_GENES, new StructuredSelection(genes));
		return selections;
	}

	/**
	 * 
	 * @return una función {@link ExpressionDataModel} ::> {@link Gene}
	 */
	private Function<ExpressionDataModel, Gene> toGene() {
		return new Function<ExpressionDataModel, Gene>() {

			@Override
			public Gene apply(ExpressionDataModel edm) {
				return edm.findGene();
			}
		};
	}

	private ExecutorService exec = Executors.newFixedThreadPool(1);

	private boolean isEditorOpen() {
		return editorOpen;
	}

	private boolean editorOpen = true;

	@Override
	public void dispose() {
		editorOpen = false;
		super.dispose();
	}

	@Override
	protected Observer createModificationObserver() {

		// TODO analizar si hace falta el lock ahora...
		final Object lock = new Object();
		final Holder<Boolean> changed = Holder.create(false);
		// table view refresher
		new Thread() {
			@Override
			public void run() {

				while (isEditorOpen()) {
					PlatformUIUtils.findDisplay().syncExec(new Runnable() {

						@Override
						public void run() {
							synchronized (lock) {
								if (changed.value()) {
									tr.input(data);
									changed.hold(false);
								}
							}
						}
					});
					sleep();
				}
			}

			private void sleep() {
				try {
					Thread.sleep(1000);// TODO hacer configurable (de quedar)
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();

		return new Observer() {

			// uso para indicar que el autorefresh esta desactivado
			private boolean logged = false;

			// private int counter = 0;

			@Override
			public void update(Observable o, Object arg) {

				if (!autorefresh()) {
					warnAutoRefresh();
					return;
				}
				logged = false;

				exec.submit(new Runnable() {

					@Override
					public void run() {
						synchronized (lock) {
							// FIXME no va más esto?.... cómo queda ahora la
							// performance?....
							data = ExpressionDataModel.checkGenes(data, model());
							changed.hold(true);
						}

					}
				});

				ExperimentEditor.checkColumns(tr, model());

			}

			private void warnAutoRefresh() {
				if (!logged) {
					final String msg = "The grid autorefreshing is disabled";
					MessageManager.INSTANCE.add(Message.warn(msg));
					logger.warn(msg);
					logged = true;
				}
			}

			private boolean autorefresh() {
				return ExperimentEditor.preferences().getBoolean(ExperimentGeneralPreferencePage.EXPERIMENT_GRID_AUTO_REFRESH, true);
			}
		};
	}

	public void showGene(Gene selectedGene) {
		tr.show(selectedGene);

	}

	/**
	 * @return
	 */
	@Override
	public TableReference tableReference() {
		return tr;
	}

}

class ExpressionDataModel /* TODO borrar extends AbstractEntity */{

	/**
	 * Usar con cuidado, puede colgar la memoria de la aplicación...
	 * 
	 * @see #merge(Experiment)
	 * 
	 * @param e
	 * @return
	 */
	public static List<ExpressionDataModel> create(AbstractExperiment e) {
		List<ExpressionDataModel> result = Lists.newArrayList();

		for (Gene g : e.getGenes()) {
			final int sampleCount = ExperimentEditor.getSampleCountToLoad();// e.getSamples().size();
			CustomCellData[] data = new CustomCellData[sampleCount + 1];
			data[0] = CustomCellDataBuilder.constant(g);
			int index = 1;

			List<Sample> samples = e.getSamples();
			for (int i = 0; i < sampleCount && i < samples.size(); i++) {
				Sample s = samples.get(i);
				data[index++] = CustomCellDataBuilder.create(new ExpressionLevelResolver(e, s, g));
			}

			result.add(new ExpressionDataModel(data));

		}
		return result;
	}

	public static List<ExpressionDataModel> checkGenes(List<ExpressionDataModel> current, final AbstractExperiment model) {
		if (current.size() != model.getGenes().size())
			current = Lists.newArrayList(Collections2.filter(current, new Predicate<ExpressionDataModel>() {

				@Override
				public boolean apply(ExpressionDataModel edm) {
					return model.containsGene(edm.findGene());
				}
			}));

		return current;
	}

	/**
	 * 
	 * 
	 * @param current
	 *            es el modelo del experimento transformado, puede ser null, en
	 *            este caso se crea uno a partir de e (debería pasar solo la
	 *            primera vez que se invoca)
	 * @param e
	 *            es el nuevo experimento
	 * @param changed
	 *            indica si el merge produjo cambios o no //TODO revisar mejor
	 *            como devolver esta situación...
	 * @return el objeto current actualizado con el experimento e
	 */
	@Deprecated
	private static List<ExpressionDataModel> merge(List<ExpressionDataModel> current, AbstractExperiment e, Holder<Boolean> changed) {
		// TODO mejorar implementación: contempla el caso que se haya removido
		// genes o incluso agregado...
		if (current == null //
				|| current.size() != e.getGenes().size()) {
			current = create(e);
			changed.hold(true);
		} else {
			int index0 = 0;
			for (Gene g : e.getGenes()) {
				int index1 = 1;
				ExpressionDataModel cdm = current.get(index0);

				List<Sample> samples = e.getSamples();
				for (int i = 0; i < ExperimentEditor.getSampleCountToLoad(); i++) {

					Sample s = samples.get(i);
					// TODO revisar bien el equals....
					if (!cdm.data[index1].equals(CustomCellDataBuilder.constant(e.getExpressionLevelForAGene(s.getName(), g)))) {
						cdm.data[index1] = CustomCellDataBuilder.create(new ExpressionLevelResolver(e, s, g)); // e.getExpressionLevelForAGene(s.getName(),
																												// g);
						changed.hold(true);
					}
					index1++;

				}
				index0++;
			}
		}
		return current;
	}

	// columnas: gen columna1 columna2 columna3
	// [0]=> genid; [1..n]=>expressión génica del sample 1 al n para el gen
	// data[0]
	private CustomCellData[] data;

	public CustomCellData[] getData() {
		return data;
	}

	public ExpressionDataModel(CustomCellData[] data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return Arrays.toString(data);
	}

	public Gene findGene() {
		// final long id = Long.parseLong(data[0].getValue().toString());
		// return MetaPlat.getInstance().getGeneByEntrezId(id);
		return (Gene) data[0].getValue();
	}

	@Override
	public int hashCode() {
		return 32 * data.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj instanceof ExpressionDataModel) {
			ExpressionDataModel other = (ExpressionDataModel) obj;
			return Arrays.equals(this.data, other.data);
		}
		return false;
	}
}
