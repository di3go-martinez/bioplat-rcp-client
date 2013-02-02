package edu.unlp.medicine.bioplat.rcp.ui.biomarker.wizards.optimization.blindSearch;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

import edu.unlp.medicine.bioplat.core.Activator;
import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.WizardPageDescriptor;
import edu.unlp.medicine.bioplat.rcp.ui.utils.databinding.validators.FilePathValidator;
import edu.unlp.medicine.bioplat.rcp.ui.utils.databinding.validators.IntegerHigherThanValueValidator;
import edu.unlp.medicine.bioplat.rcp.ui.utils.databinding.validators.IntegerInRangeValidator;
import edu.unlp.medicine.bioplat.rcp.utils.wizards.BindingResult;
import edu.unlp.medicine.bioplat.rcp.utils.wizards.WizardModel;
import edu.unlp.medicine.bioplat.rcp.utils.wizards.WizardPageUtils;
import edu.unlp.medicine.bioplat.rcp.widgets.Widgets;
import edu.unlp.medicine.entity.biomarker.Biomarker;

public class GeneralBlindSearchConfiguration extends WizardPageDescriptor {

	public static final String NUMBER_OF_GENES_FROM = "NUMBER_OF_GENES_FROM";
	public static final String NUMBER_OF_GENES_TO = "NUMBER_OF_GENES_TO";
	public static final String NUMBER_OF_RESULTS_TO_SHOW = "NUMBER_OF_RESULTS_TO_SHOW";

	// TODO analizar si vale la pena, e implementar mejor en todo caso
	public static List<String> allAvailableParams() {

		return Lists.transform(Arrays.asList(GeneralBlindSearchConfiguration.class.getFields()), new Function<Field, String>() {

			@Override
			public String apply(Field input) {
				try {
					return input.get(null).toString();
				} catch (Exception e) {
					e.printStackTrace();
				}
				return "";
			}
		});

	}

	Biomarker biomarker;
	public GeneralBlindSearchConfiguration(Biomarker biomarker) {
		super("PSO Configuration");
		this.biomarker = biomarker;
	}

	@Override
	public Composite create(WizardPage wizardPage, Composite parent, DataBindingContext dbc, WizardModel wmodel) {
		//Composite container = Widgets.createDefaultContainer(parent, 5);
		wizardPage.setTitle("Configure the blind search");
		wizardPage.setDescription("You can restrict the solution space and the result.");
		wizardPage.setImageDescriptor(Activator.imageDescriptorFromPlugin("blindSearch-page.png"));
		
		//WizardPageUtils.createText(container, dbc, wmodel.valueHolder(NUMBER_OF_GENES_TO_REMOVE_FROM), "I want gene Signatures with no less than ");
		
		
		GridData extensor = new GridData();
		extensor.horizontalAlignment=SWT.FILL;
		extensor.grabExcessHorizontalSpace=true;
		
		
		Composite group = new Group(parent, SWT.NONE);
		group.setLayout(GridLayoutFactory.fillDefaults().numColumns(6).margins(10,10).spacing(2, 20).create());
		group.setLayoutData(extensor);

		
		new Label(group, SWT.NONE).setText("I am interesting in analyzying gene Signatures with no less than ");
		
		
		Text t = new Text(group, SWT.BORDER);
		t.setLayoutData(new GridData(30, 13));
		IValidator fromVvalidator = new IntegerHigherThanValueValidator(1, "The from value must be higer than 1", "from Value");
		dbc.bindValue(SWTObservables.observeText(t, SWT.Modify), wmodel.valueHolder(NUMBER_OF_GENES_FROM), new UpdateValueStrategy().setAfterConvertValidator(fromVvalidator), null);
		
		
		new Label(group, SWT.NONE).setText(" genes and with no more than ");
		
		Text t2 = new Text(group, SWT.BORDER);
		t2.setLayoutData(new GridData(30, 13));
		IValidator toValidator = new IntegerInRangeValidator((Integer)wmodel.value(NUMBER_OF_GENES_FROM),  this.biomarker.getNumberOfGenes(), "The gene signature size can not be higher than the size of the original Gene Signature(" + this.biomarker.getNumberOfGenes() +")", "The From value must be lower than the To value", "To value");
		dbc.bindValue(SWTObservables.observeText(t2, SWT.Modify), wmodel.valueHolder(NUMBER_OF_GENES_TO), new UpdateValueStrategy().setAfterConvertValidator(toValidator), null);
		
		
		new Label(group, SWT.NONE).setText(" genes.");
		
		
		//WizardPageUtils.createText(group, dbc, "Show me the betters ", wmodel.valueHolder(NUMBER_OF_RESULTS_TO_SHOW), " gene Signatures" );
		Composite c = new Composite(group, SWT.NONE);
		GridData extensor4NumberOfResults = new GridData();
		extensor4NumberOfResults.horizontalSpan=6;
		c.setLayout(GridLayoutFactory.fillDefaults().numColumns(3).create());
		c.setLayoutData(extensor4NumberOfResults);
		new Label(c, SWT.NONE).setText("Show me the betters ");
		Text t3 = new Text(c, SWT.BORDER);
		t3.setLayoutData(new GridData(30, 13));
		dbc.bindValue(SWTObservables.observeText(t3, SWT.Modify),  wmodel.valueHolder(NUMBER_OF_RESULTS_TO_SHOW));
		new Label(c, SWT.NONE).setText( " gene Signatures");
		
		
		
		
		
		
		Label noteLabel = new Label(group, SWT.WRAP);
		GridData gridData4noteLabel = new GridData(GridData.FILL_HORIZONTAL);
		gridData4noteLabel.horizontalSpan=5;
		
		noteLabel.setLayoutData(gridData4noteLabel);
		FontData[] fD = noteLabel.getFont().getFontData();
		fD[0].setHeight(8);
		fD[0].setStyle(SWT.ITALIC);
		noteLabel.setFont( new Font(group.getDisplay(),fD[0]));
		noteLabel.setText("\nNote: Take into account that the time to finish the execution is proportional to the number of genes of the original Gene Siganture and the result range you configure. If the time for searching the whole space is too large, consider PSO instead of blind search.\nWe are working on Parallel blind search to execute in a cluster or a Grid");

		
		
		
		
		
		
		return group;
	}
	
	@Override
	public boolean isPageComplete(WizardModel model) {
		try{
			int from = model.value(NUMBER_OF_GENES_FROM);
			int to = model.value(NUMBER_OF_GENES_TO);
			return from<=to && to<=this.biomarker.getNumberOfGenes();	
		}
		catch(NullPointerException e){
			return false;
		}
		   
		
	}

}
