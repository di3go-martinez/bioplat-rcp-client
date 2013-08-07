package edu.unlp.medicine.bioplat.rcp.ui.biomarker.newWizard;

import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.INewWizard;

import com.google.common.collect.Lists;

import edu.unlp.medicine.bioplat.rcp.ui.entities.EditorsId;
import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.AbstractWizard;
import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.WizardPageDescriptor;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.bioplat.rcp.utils.wizards.WizardModel;
import edu.unlp.medicine.entity.biomarker.Biomarker;
import edu.unlp.medicine.entity.biomarker.EditedBiomarker;
import edu.unlp.medicine.utils.monitor.Monitor;

/**
 * Wizard de creación de un biomarcador "en blanco"
 * 
 * @author diego martínez
 * 
 */
public class CreateEmptyBiomarkerWizard extends AbstractWizard<Biomarker> implements INewWizard {

	private static final String NAME_K = "Name";
	private static final String AUTHOR = "author";
	private static final String DESCRIPTION = "desccription";
	
	
	@Override
	public int getWizardWidth() {
		
		return 500;
	}
	
	@Override
	public int getWizardHeight() {
		
		return 400;
	}
	
	@Override
	protected WizardModel createWizardModel() {
		WizardModel wm = new WizardModel().add(NAME_K, new WritableValue("noName", String.class));
		wm.add(AUTHOR, new WritableValue("", String.class));
		wm.add(DESCRIPTION, new WritableValue("", String.class));
		return wm;
	}

	@Override
	protected void configureParameters() {
		name = wizardModel().value(NAME_K).toString();
		author = wizardModel().value(AUTHOR).toString();
		description = wizardModel().value(DESCRIPTION).toString();
	}

	private String name = "";
	private String author = "";
	private String description = "";

	@Override
	public Biomarker backgroundProcess(Monitor m) {
		Biomarker newBiomarker = new EditedBiomarker(name);
		newBiomarker.setAuthor(author);
		newBiomarker.setDescription(description);
		return newBiomarker;
	}

	@Override
	protected void doInUI(Biomarker result) throws Exception {
		PlatformUIUtils.openEditor(result, EditorsId.biomarkerEditorId());
	}

	@Override
	protected List<WizardPageDescriptor> createPagesDescriptors() {
		List<WizardPageDescriptor> result = Lists.newArrayList();

		result.add(new WizardPageDescriptor("Basic data.") {

			@Override
			public Composite create(WizardPage wp, Composite parent, DataBindingContext dbc, WizardModel wmodel) {
				
				//parent.setLayout(GridLayoutFactory.fillDefaults().numColumns(1).margins(20,20).create());
				
				GridData gridData = new GridData();
				gridData.horizontalAlignment=SWT.FILL;
				gridData.grabExcessHorizontalSpace=true;
				
				
				
				wp.setDescription("You can later add genes (EntrezID, EnsemblID or Gene name), writing them or copying and pasting.");
				Composite group = new Group(parent, SWT.NONE);
				group.setLayout(GridLayoutFactory.fillDefaults().numColumns(2).margins(20,20).spacing(2, 20).create());
				group.setLayoutData(gridData);

				
				new CLabel(group, SWT.BOLD).setText("Gene signature name:");
				Text nameHolder = new Text(group, SWT.BORDER);
				nameHolder.setLayoutData(gridData);
				dbc.bindValue(SWTObservables.observeText(nameHolder, SWT.Modify), wizardModel().valueHolder(NAME_K));
				
				new CLabel(group, SWT.BOLD).setText("Gene signature author:");
				Text authorHolder = new Text(group, SWT.BORDER);
				authorHolder.setLayoutData(gridData);
				dbc.bindValue(SWTObservables.observeText(authorHolder, SWT.Modify), wizardModel().valueHolder(AUTHOR));
				
				new CLabel(group, SWT.BOLD).setText("Gene signature description:");
				Text descriptionHolder = new Text(group, SWT.BORDER);
				descriptionHolder.setLayoutData(gridData);
				dbc.bindValue(SWTObservables.observeText(descriptionHolder, SWT.Modify), wizardModel().valueHolder(DESCRIPTION));

				
				//GridLayoutFactory.swtDefaults().numColumns(1).margins(5, 5).generateLayout(group);
				return group;
			}
		});

		return result;
	}

	@Override
	protected String getTaskName() {
		return "Creation of " + name + "(new empty gene signature) ";
	}

	public CreateEmptyBiomarkerWizard(){
		this.setWindowTitle("Create empty BioPlat gene signature");	
	}
	
	
	
	
}
