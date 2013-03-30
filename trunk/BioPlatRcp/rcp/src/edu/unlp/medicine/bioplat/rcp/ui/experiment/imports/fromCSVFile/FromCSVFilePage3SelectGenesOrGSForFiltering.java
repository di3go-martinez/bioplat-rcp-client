package edu.unlp.medicine.bioplat.rcp.ui.experiment.imports.fromCSVFile;

import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.WizardPageDescriptor;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.ColumnBuilder;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableBuilder;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableReference;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.bioplat.rcp.utils.wizards.WizardModel;
import edu.unlp.medicine.domainLogic.framework.GeneSignatureProvider.IGeneSignatureProvider;
import edu.unlp.medicine.domainLogic.framework.exceptions.ProblemsGettingTheGeneSiganturesException;
import edu.unlp.medicine.entity.biomarker.Biomarker;
import edu.unlp.medicine.entity.biomarker.GeneSignature;

public class FromCSVFilePage3SelectGenesOrGSForFiltering extends WizardPageDescriptor {

	
	public static final String OPENED_SELECTED_BIOMARKERS = "OPENED_SELECTED_BIOMARKERS";
	
	private static Logger logger = LoggerFactory.getLogger(FromCSVFilePage3SelectGenesOrGSForFiltering.class);

	public FromCSVFilePage3SelectGenesOrGSForFiltering() {
		super("Filter");
	}


	private WizardModel wizardModel;
	private TableReference tr ;

	@Override
	public Composite create(WizardPage wp, Composite parent, DataBindingContext dbc, WizardModel wmodel) {
		wp.setDescription("Would you like to add Gene Signatures from your Bioplat Desktop?");
		GridDataFactory gdf = GridDataFactory.fillDefaults().grab(true, false);
		
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(GridLayoutFactory.fillDefaults().numColumns(2).margins(30, 30).spacing(20, 20).create());
		
		createLocalBiomarkersSelector(wp, container, gdf, dbc, wmodel);
		
		return container;
	}

	
		private void createLocalBiomarkersSelector(final WizardPage wp, Composite container, GridDataFactory gdf, DataBindingContext dbc, final WizardModel wmodel) {
		final List<Biomarker> openedBiomarkers = PlatformUIUtils.openedEditors(Biomarker.class);
		if (!openedBiomarkers.isEmpty()) {
			Group openedBiomarker = new Group(container, SWT.NONE);
			openedBiomarker.setLayout(GridLayoutFactory.fillDefaults().numColumns(2).margins(20, 20).create());
			openedBiomarker.setLayoutData(gdf.create());
			openedBiomarker.setText("Gene Signatures in your Bioplat desktop");
			tr = TableBuilder.create(openedBiomarker).input(openedBiomarkers)
					.addColumn(ColumnBuilder.create().property("name").title("Biomarker name"))
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
