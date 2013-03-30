package edu.unlp.medicine.bioplat.rcp.ui.experiment.imports.fromCSVFile;

import static edu.unlp.medicine.domainLogic.common.constants.CommonConstants.COLLAPSE_STRATEGY_AVERAGE;
import static edu.unlp.medicine.domainLogic.common.constants.CommonConstants.COLLAPSE_STRATEGY_VARIANCE;

import java.util.Arrays;
import java.util.Map;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.observable.DisposeEvent;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.databinding.wizard.WizardPageSupport;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

import edu.unlp.medicine.bioplat.core.Activator;
import edu.unlp.medicine.bioplat.rcp.ui.biomarker.newWizard.metasignature.generation.GMSPage2FIlterExternalDBs;
import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.WizardPageDescriptor;
import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.databinding.UpdateStrategies;
import edu.unlp.medicine.bioplat.rcp.ui.utils.databinding.validators.FilePathValidator;
import edu.unlp.medicine.bioplat.rcp.ui.utils.databinding.validators.RequiredValidator;
import edu.unlp.medicine.bioplat.rcp.utils.wizards.WizardModel;
import edu.unlp.medicine.bioplat.rcp.widgets.FileText;
import edu.unlp.medicine.bioplat.rcp.widgets.Widgets;
import edu.unlp.medicine.domainLogic.common.constants.CommonConstants;

/**
 * 
 * Descritor de página de wizard para los proveedores de genes signatures
 * 
 * @author Matias Butti
 * 
 */
public class FromCSVFilePage2Main extends WizardPageDescriptor {

	private static Logger logger = LoggerFactory.getLogger(FromCSVFilePage2Main.class);
	
	public static final String FILE_PATH = "FILE_PATH";
	public static final String COLLAPSE_STRATEGY = "COLLAPSE_STRATEGY";
	public static final String CLINICAL_FIRST_LINE	= "CLINICAL_FIRST_LINE";

	
	//////////////////////////INITIALIZATION OF THE PAGE. It is executed when the wizard is started.////////////////////////////////
	public FromCSVFilePage2Main(WizardModel wizardModel) {
		super("File configuration and collapsing strategy                                                                               ");
		addParameters(wizardModel);
	}
	
	public FromCSVFilePage2Main addParameters(WizardModel wizardModel) {
		wizardModel.add(FILE_PATH, new WritableValue("", String.class))
				.add(COLLAPSE_STRATEGY, new WritableValue("", String.class))
				.add(CLINICAL_FIRST_LINE, new WritableValue("", Integer.class));

		return this;
	}
	
	@Override
	public Composite create(final WizardPage wp, Composite parent, DataBindingContext dbc, final WizardModel wmodel) {
		
		wp.setDescription("Select the file, the collapse strategy and the line number of the first expression data");
		
		Composite container = Widgets.createDefaultContainer(parent, 1);

		init(container, wp, parent, dbc, wmodel);
		//createFiltersGroup(container, wp, parent, dbc, wmodel);
		
		return container;
	}

	
	public void init(Composite container, final WizardPage wp, Composite parent, DataBindingContext dbc, final WizardModel wm) {
		IWorkbench workbench; 
		IStructuredSelection selection;
		
		GridData gridData = new GridData();
		gridData.horizontalAlignment=SWT.FILL;
		gridData.grabExcessHorizontalSpace=true;

				//this.setDescription("Import the experiment using a .CSV file following the GEO format. Take a look below at the file format template");
				
				Composite c = new Group(container, SWT.NONE);
				c.setLayout(GridLayoutFactory.fillDefaults().numColumns(2).margins(20,20).spacing(5, 10).create());
				//group.setLayoutData(gridData);


				new Label(c, SWT.NONE).setText("File path (take a look at the format example below):");
				FileText filePath = new FileText(c, SWT.NONE);
				Map<String, String> filters = Maps.newHashMap();
				filters.put("*.csv", "CSV File");
				filters.put("*", "All");
				filePath.setFilter(filters);

				dbc.bindValue(SWTObservables.observeText(filePath.textControl(), SWT.Modify), wm.valueHolder(FILE_PATH), new UpdateValueStrategy().setAfterConvertValidator(FilePathValidator.create().fileMustExist()), null);

				new Label(c, SWT.NONE).setText("Collapse Strategy. If there is more than one probe \nfor the same gene, represent the gene with the probe with highest:");
				ComboViewer collapseStrategyCombo = new ComboViewer(c, SWT.BORDER | SWT.READ_ONLY);
				collapseStrategyCombo.setContentProvider(ArrayContentProvider.getInstance());
				collapseStrategyCombo.setInput(//
						Arrays.asList(CommonConstants.COLLAPSE_STRATEGY_MEDIAN, COLLAPSE_STRATEGY_AVERAGE, COLLAPSE_STRATEGY_VARIANCE));

				//collapseStrategyCombo.getCombo().setLayoutData(gridData);
				
				IObservableValue widgetObservable = ViewersObservables.observeSingleSelection(collapseStrategyCombo);
				dbc.bindValue(widgetObservable,  wm.valueHolder(COLLAPSE_STRATEGY), //
						new UpdateValueStrategy().setAfterConvertValidator(RequiredValidator.create("Collapse Strategy")), null);

				wm.valueHolder(COLLAPSE_STRATEGY).setValue(CommonConstants.COLLAPSE_STRATEGY_MEDIAN);

				new Label(c, SWT.NONE).setText("First line number of expression data");
				Text t = new Text(c, SWT.BORDER);
				t.setLayoutData(gridData);
				dbc.bindValue(SWTObservables.observeText(t, SWT.Modify),  wm.valueHolder(CLINICAL_FIRST_LINE), null, null);
						
//						new UpdateValueStrategy().setBeforeSetValidator(new IValidator() {
//
//					@Override
//					public IStatus validate(Object value) {
//						String l = value.toString();
//						if (l.matches("\\d+,\\d+,\\d+,\\d+"))
//							return ValidationStatus.ok();
//						else
//							return ValidationStatus.error("Invalid format: ###,###,###,###");
//					}
//				}), null);
				//t.setText("4");
				//t.setSize(100, 10);
				t.setText("4");
				

//				GridLayoutFactory.fillDefaults().numColumns(2).generateLayout(c);
				//setControl(c);

				
				//Composite formatExampleGroup = new Composite(c, SWT.NONE);
				//formatExampleGroup.setText("Format example");
				//formatExampleGroup.setLayout(GridLayoutFactory.fillDefaults().numColumns(1).margins(5,5).spacing(7, 10).create());
				GridData formatExampleGroupLD = new GridData();
				formatExampleGroupLD.horizontalAlignment = GridData.CENTER;
				formatExampleGroupLD.horizontalSpan = 2;
				formatExampleGroupLD.verticalIndent = 15;
				//formatExampleGroup.setLayoutData(formatExampleGroupLD);
//				new Label(formatExampleGroup, SWT.NONE).setText("OS_Months	135.996	141.996	141.996\nOS_Event	0	0	0\nsampleId	GSM79364	GSM79114	GSM79115\n53	9.015745	8.249458	8.323728\n32	8.323749	8.677738	6.834595\n24	6.308628	6.744825	6.201588\n23	9.525107	9.090437	9.885698\n780	10.726804	10.544961	10.795536\n1130	6.284713	6.092771	6.086131\n");

				Image image = Activator.imageDescriptorFromPlugin("ImportExpTemplateEx1.jpg").createImage();
				Button button = new Button(c, SWT.FLAT);
				
				button.setImage(image);
				button.setLayoutData(formatExampleGroupLD);
		}
	
	
	
	
	
	
	
//	
//	private void createFiltersGroup(Composite container, final WizardPage wp, Composite parent, DataBindingContext dbc, final WizardModel wmodel) {
//		container.setLayout(GridLayoutFactory.fillDefaults().numColumns(1).margins(30, 50).create());
//		
//		Group filterGroup = new Group(container, SWT.NONE);
//		//filterGroup.setText("Description");
//		
//		//new Image(filterGroup.getDisplay(),"resources/icons/gene.gif");
//		
//		filterGroup.setLayout(GridLayoutFactory.fillDefaults().margins(50, 50).create());
//		filterGroup.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());
//
//		//GridData layoutdata = new GridData(1000, SWT.DEFAULT);
//		
//		
//		Label introdudctionLabel = new Label(filterGroup, SWT.WRAP);
//		
//		GridData gridData = new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL); 
//		introdudctionLabel.setLayoutData(gridData);
//		
//		FontData[] fD = introdudctionLabel.getFont().getFontData();
//		fD[0].setHeight(10);
//		fD[0].setStyle(SWT.ITALIC);
//		introdudctionLabel.setFont( new Font(filterGroup.getDisplay(),fD[0]));
//		
//		
//		introdudctionLabel.setText("The file must have the following format...");
//		
//
//	}

	
	@Override
	public boolean isPageComplete(WizardModel model) {
		return true;
		
	}

	
	
}
