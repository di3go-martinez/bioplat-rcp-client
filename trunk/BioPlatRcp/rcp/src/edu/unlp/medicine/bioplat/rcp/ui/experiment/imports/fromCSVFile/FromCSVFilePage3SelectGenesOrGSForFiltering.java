package edu.unlp.medicine.bioplat.rcp.ui.experiment.imports.fromCSVFile;

import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.WizardPageDescriptor;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.ColumnBuilder;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableBuilder;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableReference;
import edu.unlp.medicine.bioplat.rcp.utils.GUIUtils;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.bioplat.rcp.utils.wizards.WizardModel;
import edu.unlp.medicine.domainLogic.framework.GeneSignatureProvider.IGeneSignatureProvider;
import edu.unlp.medicine.domainLogic.framework.exceptions.ProblemsGettingTheGeneSiganturesException;
import edu.unlp.medicine.entity.biomarker.Biomarker;
import edu.unlp.medicine.entity.biomarker.GeneSignature;

public class FromCSVFilePage3SelectGenesOrGSForFiltering extends WizardPageDescriptor {

	
	public static final String OPENED_SELECTED_BIOMARKERS = "OPENED_SELECTED_BIOMARKERS";
	public static final String SELECTED_GENES = "SELECTED_GENES";
	
	private static Logger logger = LoggerFactory.getLogger(FromCSVFilePage3SelectGenesOrGSForFiltering.class);

	public FromCSVFilePage3SelectGenesOrGSForFiltering(WizardModel wmodel) {
		super("Filter");
		wmodel.add(SELECTED_GENES, new WritableValue("", String.class));
		
	}


	private WizardModel wizardModel;
	private TableReference tr;

	@Override
	public Composite create(WizardPage wp, Composite parent, DataBindingContext dbc, WizardModel wmodel) {
		wp.setDescription("If your experiment is too big and you dont need all the gene information, you should filter it using a gene List");
		GridDataFactory gdf = GridDataFactory.fillDefaults().grab(true, false);
		
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(GridLayoutFactory.fillDefaults().numColumns(1).margins(30, 30).spacing(20, 20).create());
		
		createLocalBiomarkersSelector(wp, container, gdf, dbc, wmodel);
		createDialogArea(wp, container, gdf, dbc, wmodel);
		
		
		
		return container;
	}

	
		private void createDialogArea(WizardPage wp, Composite parent,
			GridDataFactory gdf, DataBindingContext dbc, final WizardModel wmodel) {
		
			
			GUIUtils.addWrappedText(parent, "\n\n...Or you can paste the genes you want to keep from the experiment\n", 9, false);
			
			final Text text = new Text(parent, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
			text.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());
			
			text.addModifyListener(new ModifyListener() {

				@Override
				public void modifyText(ModifyEvent e) {
					wmodel.set(SELECTED_GENES, text.getText());
				}
			});
			
			
			//text.setText((String)wmodel.value(SELECTED_GENES));
			
			
			dbc.bindValue(SWTObservables.observeText(text, SWT.Modify), wmodel.valueHolder(SELECTED_GENES), new UpdateValueStrategy(), null);
		
	}


		private void createLocalBiomarkersSelector(final WizardPage wp, Composite container, GridDataFactory gdf, DataBindingContext dbc, final WizardModel wmodel) {
		final List<Biomarker> openedBiomarkers = PlatformUIUtils.openedEditors(Biomarker.class);

		
		GUIUtils.addWrappedText(container, "\n\n...You can filter the experiment picking up a Gene Signature from your Bioplat desktop. Its genes will be used for filtering the experiment\n", 9, false);
		
		Group openedBiomarker = new Group(container, SWT.NONE);
		openedBiomarker.setLayout(GridLayoutFactory.fillDefaults().numColumns(2).margins(20, 20).create());
		openedBiomarker.setLayoutData(gdf.create());
		//openedBiomarker.setText("Gene Signatures in your Bioplat desktop");

		
		if (openedBiomarkers.isEmpty()){
			GUIUtils.addWrappedText(openedBiomarker, "\n\n\n\n                                There is no Gene Signature in your desktop.", 8, true);
		}
		
		
		else{
			
		
			tr = TableBuilder.create(openedBiomarker).input(openedBiomarkers)
					.addColumn(ColumnBuilder.create().property("name").title("Gene Signature name"))
					.addColumn(ColumnBuilder.create().property("numberOfGenes").title("Number of genes"))
					.build();
			tr.addSelectionChangeListener(new ISelectionChangedListener() {

				@Override
				public void selectionChanged(SelectionChangedEvent event) {
					wmodel.set(OPENED_SELECTED_BIOMARKERS, tr.selectedElements());
					wp.setPageComplete(isPageComplete(wmodel));
				}
			});
		}
		
		
		
		

		
	}




	@Override
	public boolean hasResultPage() {
		return false;
	}

	
	

	
	
	@Override
	public boolean isPageComplete(WizardModel model) {
		return true;
	}
	
	
	protected void resolveOpenedProvider(WizardModel model, List<IGeneSignatureProvider> result) {
		// opened Editors Provider
		// FIXME un biomarcador no es un GeneSignature
		final List<Biomarker> l = model.value(OPENED_SELECTED_BIOMARKERS);
		if (l != null && !l.isEmpty()) {
			result.add(new IGeneSignatureProvider() {

				@Override
				public List<GeneSignature> getGeneSignatures() throws ProblemsGettingTheGeneSiganturesException {
					List<GeneSignature> gs = Lists.newArrayList();
					for (Biomarker b : l)
						gs.add(new GeneSignature(b));
					return gs;
				}

				@Override
				public String getFriendlyDescription() {
					return "Editing Gene Signatures";
				}
			});
		}
	}

	
	
	
	
	
	
}
