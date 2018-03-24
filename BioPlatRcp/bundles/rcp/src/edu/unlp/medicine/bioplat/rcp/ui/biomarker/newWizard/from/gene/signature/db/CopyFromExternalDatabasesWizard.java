package edu.unlp.medicine.bioplat.rcp.ui.biomarker.newWizard.from.gene.signature.db;

import java.util.Arrays;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.WizardPageDescriptor;
import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.databinding.UpdateStrategies;
import edu.unlp.medicine.bioplat.rcp.utils.GUIUtils;
import edu.unlp.medicine.bioplat.rcp.utils.wizards.WizardModel;
import edu.unlp.medicine.bioplat.rcp.widgets.wizards.Utils;
import edu.unlp.medicine.domainLogic.framework.constants.Constants;

/**
 * 
 * @author diego martínez
 * 
 */
public class CopyFromExternalDatabasesWizard extends WizardPageDescriptor {

	public CopyFromExternalDatabasesWizard(WizardModel wmodel) {
		super("Configuration");

		addParameters(wmodel);

	}

	static final String DATABASE = "DATABASE";
	static final String GENE_SIGNATURE_OR_ID = "GENE_SIGNATURE_OR_ID";


	
	
	
	@Override
	public Composite create(WizardPage wizardPage, Composite parent, DataBindingContext dbc, WizardModel wmodel) {
		
		wizardPage.setTitle("Which gene Signature to copy?");
		wizardPage.setDescription("It creates a new Bioplat geneSignature as copy of an existing gene signature published in a secondary database: geneSigDB or MolSigDB.");

		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(GridLayoutFactory.fillDefaults().numColumns(1).margins(10, 0).create());
		

		StyledText label = new StyledText(container, SWT.WRAP );
		//Label label = new Label(parent, SWT.WRAP );
		label.setJustify(true);
		label.setBackground(container.getBackground());
		label.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).indent(10, 20).create());
		label.setEditable(false);
		label.setText("Tip: If you know the exact gene signature name, write it or paste it.  However, if you know just part of the name, you can use % as the widcard; for example, if you know that the gene signature contains the name Cecco09, but you dont know the full name, you can use %Cecco09% for finding it. \nDont forget to put the % before and after your keyword, as the example shows. \n");
		GUIUtils.setFont(label, 9, true);

		Composite group = new Group(container, SWT.NONE);
		group.setLayout(GridLayoutFactory.fillDefaults().numColumns(2).margins(20,20).spacing(7, 20).create());
		group.setLayoutData(GridDataFactory.fillDefaults().grab(false, true).create());
		
		ComboViewer cv = Utils.newComboViewer(group, "Database:", Arrays.asList(Constants.GENE_SIG_DB, Constants.MOL_SIG_DB));
		dbc.bindValue(ViewersObservables.observeSingleSelection(cv), wmodel.valueHolder(DATABASE));
		cv.setSelection(new StructuredSelection(Constants.GENE_SIG_DB));
		//cv.getCombo().setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());
		//cv.setLayoutData(group);

		new Label(group, SWT.NONE).setText("Gene Signature Name or Id");
		Text t = new Text(group, SWT.BORDER);
		dbc.bindValue(SWTObservables.observeText(t, SWT.Modify), wmodel.valueHolder(GENE_SIGNATURE_OR_ID), UpdateStrategies.nonNull(GENE_SIGNATURE_OR_ID), null);
		t.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());
		

		return container;
	}

	
	
	CopyFromExternalDatabasesWizard addParameters(WizardModel wmodel) {

		wmodel.add(DATABASE);
		wmodel.add(GENE_SIGNATURE_OR_ID, new WritableValue("", String.class));
		return this;
	}

}
