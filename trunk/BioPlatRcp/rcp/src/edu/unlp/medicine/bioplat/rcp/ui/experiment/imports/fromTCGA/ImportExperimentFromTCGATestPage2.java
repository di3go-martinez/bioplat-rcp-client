package edu.unlp.medicine.bioplat.rcp.ui.experiment.imports.fromTCGA;

import java.util.ArrayList;

import org.bioplat.r4j.R4JCore.values.R4JStringDataMatrix;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.WizardPageDescriptor;
import edu.unlp.medicine.bioplat.rcp.utils.wizards.WizardModel;
import edu.unlp.medicine.entity.experiment.tcga.api.TCGAApi;

public class ImportExperimentFromTCGATestPage2 extends WizardPageDescriptor {

	private TableViewer tv;
	private WizardPage wPage;
	private WizardModel wModel;
	public static String STUDY = "STUDY";
	
	
	public ImportExperimentFromTCGATestPage2(WizardModel wmodel) {
		super("Select TCGA study (step 1 of 4)");
		
	}
	
	@Override
	public Composite create(final WizardPage wizardPage, Composite parent, 
			final DataBindingContext dbc, final WizardModel wmodel) {
		
		wizardPage.setDescription("Select an study from TCGA");
		
		this.wPage = wizardPage;
		this.wModel = wmodel;
		
		R4JStringDataMatrix matrix = TCGAApi.getInstance().get_studies();
		
		Composite container = new Group(parent, SWT.CENTER);
		container.setLayoutData(configureGridData());
		//container.setSize(0, 0);
//		container.setLayout(GridLayoutFactory.fillDefaults().numColumns(1).margins(30, 30).create());
//		container.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());
		createTable(container, matrix);
		return container;
	}

	private GridData configureGridData(){
		GridData gridData = new GridData();
		gridData.horizontalAlignment=SWT.FILL;
		gridData.grabExcessHorizontalSpace=true;
		return gridData;
	}
	private TableViewer createTable(Composite container, R4JStringDataMatrix input ){
		
		
		tv = new TableViewer(container, SWT.BORDER | SWT.FULL_SELECTION);
		tv.setUseHashlookup(true);

		createSearchText(tv);
		
		final Table table = tv.getTable();
		
		TableViewerColumn nameColumn = new TableViewerColumn(tv, SWT.NONE);
		TableViewerColumn descriptionColumn = new TableViewerColumn(tv, SWT.NONE);
		
		nameColumn.getColumn().setWidth(300);
		nameColumn.getColumn().setText("Name");
		nameColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				String[] experiment = (String[]) element;
				return experiment[0];
			}
		});
		
		descriptionColumn.getColumn().setWidth(300);
		descriptionColumn.getColumn().setText("Description");
		descriptionColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				String[] experiment = (String[]) element;
				return experiment[1];
			}
		});
		
		table.setHeaderVisible(true);
		table.setLinesVisible(true); 
		
		tv.setContentProvider(ArrayContentProvider.getInstance());
		tv.setInput(matrixToArray(input));
		
		tv.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				wPage.setPageComplete(isPageComplete(wModel));
				wModel.set(STUDY, (String[])((StructuredSelection) tv.getSelection()).getFirstElement() );
				
			}
		});
		tv.refresh();
		return tv;
	}
	
	private void createSearchText(final TableViewer tviewer) {
		final Text searchbox = new Text(tviewer.getTable().getParent()	, SWT.BORDER);
		searchbox.setMessage("Filter...");
		searchbox.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				if (searchbox.getText().isEmpty())
					tviewer.resetFilters();
				else 
					tviewer.setFilters(createFilter(searchbox.getText()));
			}

			private ViewerFilter[] createFilter(String text) {
				ViewerFilter[] result = new ViewerFilter[1];
				result[0]=new ImportTCGATableFilter(text);
				return result;
			}
		});
		//facilidad para navegar la tabla con las teclas arriba/abajo luego de filtrar
		searchbox.addKeyListener(new KeyListener() {
			
			@Override
			public void keyReleased(KeyEvent e) {
				//TODO buscar y usar las constantes...
				if(e.keyCode == 16777218 || e.keyCode == 16777217) //arriba o abajo
					tviewer.getTable().setFocus();
				
				e.doit=false;
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
			
				
			}
		});
		searchbox.moveAbove(tviewer.getTable());
	}

	private ArrayList<String[]> matrixToArray(R4JStringDataMatrix matrix){
		ArrayList<String[]> arrayReturn = new ArrayList<String[]>();
		for (String[] row : matrix.getData()){
			arrayReturn.add(row);
		}
		return arrayReturn;
	}

	@Override
	public boolean isPageComplete(WizardModel model) {
		return !tv.getSelection().isEmpty();
	}
}
