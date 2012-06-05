package edu.unlp.medicine.bioplat.rcp.ui.biomarker.newWizard.metasignature.generation;

import java.util.Arrays;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.WizardPageDescriptor;
import edu.unlp.medicine.bioplat.rcp.utils.wizards.WizardModel;
import edu.unlp.medicine.bioplat.rcp.widgets.wizards.Utils;

/**
 * 
 * Descriptor de página de wizard para la selección de algoritmos a aplicar.
 * 
 * @author diego martínez
 * 
 */
public class AlgorithmPageDescriptor extends WizardPageDescriptor {

	static final String BASOS = "BASOS (Based on significance value)";
	static final String CONT = "CONT (Considering Ontrology)";
	static final String SMS = "SMS (Smart Selector)";
	static final String IES = "IES (In every Signature)";
	static final String UNION = "UNION";
	static final String ALGORITHM = "ALGORITHM";
	static final String SEPARATOR = "#";
	static final String ALIX_X_PARAMETER = "X";

	// Algorithm names
	static final String ALIX = "ALIX (At least in X signatures)";

	private Composite alixComposite;

	public AlgorithmPageDescriptor() {
		super("Algorithms");
	}

	@Override
	public Composite create(WizardPage wp, Composite parent, DataBindingContext dbc, WizardModel wmodel) {
		Composite c = new Composite(parent, SWT.NONE);
		c.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());

		ComboViewer cv = Utils.newComboViewer(c, "Algorithm", Arrays.asList(ALIX, UNION, IES, SMS, CONT, BASOS));
		dbc.bindValue(ViewersObservables.observeSingleSelection(cv), wmodel.valueHolder(AlgorithmPageDescriptor.ALGORITHM));

		cv.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				Object s = ((StructuredSelection) event.getSelection()).getFirstElement();
				alixComposite.setVisible(ALIX.equals(s));
			}
		});

		createAlixComposite(c, dbc, wmodel);

		cv.setSelection(new StructuredSelection(UNION));

		return c;
	}

	private void createAlixComposite(Composite parent, DataBindingContext dbc, WizardModel wmodel) {
		alixComposite = new Group(parent, SWT.NONE);
		alixComposite.setLayout(GridLayoutFactory.fillDefaults().numColumns(2).create());
		alixComposite.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());
		alixComposite.setVisible(false); // TODO revisar
		new Label(alixComposite, SWT.NONE).setText(ALIX_X_PARAMETER);
		Text t = new Text(alixComposite, SWT.BORDER);
		// TODO definir mejor el id de la key a poner en el wizard model
		dbc.bindValue(SWTObservables.observeText(t, SWT.Modify), wmodel.valueHolder(ALIX + SEPARATOR + ALIX_X_PARAMETER));

	}

	public WizardPageDescriptor addParameters(WizardModel wizardModel) {
		wizardModel.add(ALGORITHM);
		wizardModel.add(ALIX + SEPARATOR + ALIX_X_PARAMETER, new WritableValue(5, int.class));
		return this;
	}
}
