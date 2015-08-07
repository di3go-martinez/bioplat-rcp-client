package edu.unlp.medicine.bioplat.rcp.ui.experiment.imports.fromTCGA;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.WizardPageDescriptor;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.ColumnBuilder;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableBuilder;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableReference;
import edu.unlp.medicine.bioplat.rcp.utils.GUIUtils;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.bioplat.rcp.utils.wizards.WizardModel;
import edu.unlp.medicine.bioplat.rcp.widgets.Widgets;
import edu.unlp.medicine.entity.biomarker.Biomarker;
import edu.unlp.medicine.entity.biomarker.GeneSignature;

public class ImportExperimentFromTCGATestPage4 extends WizardPageDescriptor {

	
	public static final String SELECTED_GENES = "SELECTED_GENES";
	private TableReference tr;
	
	
	public ImportExperimentFromTCGATestPage4(WizardModel wizardModel) {
		super("Get Studios");
	}
	
	

	@Override
	public Composite create(WizardPage wizardPage, Composite parent,
			DataBindingContext dbc, WizardModel wmodel) {
		
		GridDataFactory gdf = GridDataFactory.fillDefaults().grab(true, false);
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(GridLayoutFactory.fillDefaults().numColumns(1).margins(30, 30).spacing(20, 20).create());
//		Composite container = Widgets.createDefaultContainer(parent, 1);
		
		createLocalBiomarkersSelector(wizardPage, container, gdf, dbc, wmodel);
		
		createGSTextArea(wmodel,container,wizardPage);
		return container;
	}

	private void createGSTextArea(final WizardModel wizardModel, Composite parent, final WizardPage wp) {
//		GUIUtils.addWrappedText(parent, "\n\n...Or you can paste the genes you want to keep from the experiment\n", 9, false);
		
		final Text text = new Text(parent, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
		text.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());
		
		text.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				wizardModel.set(SELECTED_GENES, text.getText());
				wp.setPageComplete(isPageComplete(wizardModel));
			}
		});
	}
	
	@Override
	public boolean isPageComplete(WizardModel model) {
		return ((String)model.value(SELECTED_GENES)) != null && !((String)model.value(SELECTED_GENES)).isEmpty();
	}
	
	private List<GeneSignature> translateBiomarkerIntoGS(
			List<Biomarker> openedBiomarkers) {
		
		
		
		List<GeneSignature> result = new ArrayList<GeneSignature>();
		if (openedBiomarkers!=null){
			for (Biomarker biomarker : openedBiomarkers) {
				result.add(new GeneSignature(biomarker));
		}}
		return result;
	}
	
	private void createLocalBiomarkersSelector(final WizardPage wp, Composite container,GridDataFactory gdf, DataBindingContext dbc, final WizardModel wmodel) {
		final List<Biomarker> openedBiomarkers = PlatformUIUtils.openedEditors(Biomarker.class);

		
		GUIUtils.addWrappedText(container, "\n\n...You can filter the experiment picking up a Gene Signature from your Bioplat desktop. Its genes will be used for filtering the experiment\n", 9, false);
		
		Group openedBiomarker = new Group(container, SWT.NONE);
		openedBiomarker.setLayout(GridLayoutFactory.fillDefaults().numColumns(2).margins(20, 20).create());
		openedBiomarker.setLayoutData(gdf.create());
		openedBiomarker.setText("Gene Signatures in your Bioplat desktop");

		
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
					wmodel.set(SELECTED_GENES, tr.selectedElements());
					wp.setPageComplete(isPageComplete(wmodel));
				}
			});
		}
	}
	
}
