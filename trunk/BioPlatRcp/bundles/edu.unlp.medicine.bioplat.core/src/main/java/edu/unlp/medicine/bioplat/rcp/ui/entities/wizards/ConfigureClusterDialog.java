package edu.unlp.medicine.bioplat.rcp.ui.entities.wizards;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;




import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.ColumnBuilder;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableBuilder;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableBuilder.MenuBuilder;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableReference;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.menues.MenuItemContribution;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.bioplat.rcp.widgets.Widgets;
import edu.unlp.medicine.domainLogic.framework.statistics.hierarchichalClustering.ClusteringResult;
import edu.unlp.medicine.entity.experiment.AbstractExperiment;
import edu.unlp.medicine.entity.experiment.ClusterData;
import edu.unlp.medicine.entity.experiment.Sample;

public class ConfigureClusterDialog extends Dialog {

	// puede ser null
	//private AbstractExperiment experiment;
	private ClusteringResult clusteringResult;
	private TableReference tref;
	private List<Data> data;
	private boolean readOnly;
	
	protected ConfigureClusterDialog(AbstractExperiment experiment, ClusteringResult clusteringResult,boolean readOnly) {
		super(PlatformUIUtils.findShell());
		if (clusteringResult == null) {
			this.clusteringResult = new ClusteringResult();
			this.clusteringResult.setExperiment(experiment);
		}else{
			this.clusteringResult = clusteringResult;
		}
		this.readOnly = readOnly; 
		createData(this.clusteringResult);
	}
	
	// 
	protected ConfigureClusterDialog(AbstractExperiment experiment, ClusteringResult clusteringResult) {
		this(experiment,clusteringResult,false);
	}
		

	public ConfigureClusterDialog(ClusteringResult clusteringResult) {
		this(null,clusteringResult,true);
		
		//createData(clusteringResult);
	}
	
	
	public ClusteringResult getClusteringResult() {
		return clusteringResult;
	}

	/*public AbstractExperiment getExperiment() {
		return experiment;
	}*/


	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Cluster Configuration");
	}

	@Override
	protected Control createContents(Composite parent) {
		Composite container = Widgets.createDefaultContainer(parent);
		int columnWidth = 150;
		
		// Label introdudctionLabel = new Label(parent, SWT.WRAP);
		// introdudctionLabel.setText("Use shift and control for select multiple samples. Then, right click and use set cluster to assign a clusterID to all selected samples");
		
		if (!readOnly()){
			Text message = new Text(container, SWT.MULTI | SWT.READ_ONLY);
			message.setText("Note: on the table select the samples (SHIFT + LEFT CLICK for multiple selection) and \n then RIGHT CLICK to set clusters.");
			//FontData fd = message.getFont().getFontData()[0];
			//message.setFont(new Font(Display.getCurrent(),fd.getName(),fd.getHeight() + 1, fd.getStyle()));
			message.setLayoutData(GridDataFactory.fillDefaults().align(SWT.CENTER, SWT.CENTER).create());
		}
			
		final ColumnBuilder titleColumnBuilder = ColumnBuilder.create().editable(!readOnly()).property("clusterId").title("Cluster ID");
		final TableBuilder tableBuilder = TableBuilder.create(container).input(data)//
				.hideSelectionColumn()//
				.addColumn(ColumnBuilder.create().property("sample.name").title("Sample").width(columnWidth))
				.addColumn(titleColumnBuilder);
		generateClinicalDataColumns(tableBuilder);		
		if (!readOnly())
			tableBuilder.contextualMenuBuilder(menuBuilder());
		tref = tableBuilder.build();
		Button ok = new Button(container, SWT.NONE);
		ok.setText("OK");
		ok.setLayoutData(GridDataFactory.fillDefaults().align(SWT.CENTER, SWT.CENTER).create());
		ok.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setGroups();
				close();
			}
		});
		//No se toma en cuenta la columna oculta por eso el -1 
		int maxSizeX = (tref.getTable().getColumnCount() - 1) * columnWidth; 
		int sizeX = maxSizeX > 1020? 1020 : maxSizeX; 
		parent.setSize(sizeX, 550);
		return container;
	}
	
	

	//TODO: DavidClustering -> no usar de momento
	private void generateClusterColors(TableReference tref2) {
		Table table = tref2.getTable();
		Color red = PlatformUIUtils.findDisplay().getSystemColor(SWT.COLOR_RED);
	    Color blue = PlatformUIUtils.findDisplay().getSystemColor(SWT.COLOR_BLUE);
		int columnCount = table.getColumnCount();
		TableItem[] items = table.getItems();
		for(TableItem item : items){
				item.setBackground(2, blue);
				
		}
	}

	private void generateClinicalDataColumns(TableBuilder tableBuilder) {
		if(!this.clusteringResult.getClusterDataList().isEmpty() && !(this.clusteringResult.getClusterDataList().get(0)).getSampleList().isEmpty()){
			List<String> attrs = this.clusteringResult.getClusterDataList().get(0).getSampleList().get(0).getClinicalAttributeNames();
			for(String attr : attrs){
				tableBuilder.addColumn(ColumnBuilder.create().property("sample.sampleClincalData.clinicalData['" + attr + "']").title(attr));
			}
		}else{
			if(!this.clusteringResult.getExperiment().getSamples().isEmpty()){
				List<String> attrs = this.clusteringResult.getExperiment().getSamples().get(0).getClinicalAttributeNames();
				for(String attr : attrs){
					tableBuilder.addColumn(ColumnBuilder.create().property("sample.sampleClincalData.clinicalData['" + attr + "']").title(attr));
				}		
			}
		}
	}

	

	//TODO: DavidClustering
	private boolean readOnly() {
		//return experiment == null;
		return readOnly;
	}

	private MenuBuilder menuBuilder() {

		return new MenuBuilder() {

			@Override
			public void build(Menu menu) {
				Image openImage = PlatformUIUtils.findImage("clustering.png");
				MenuItemContribution.create(menu).image(openImage).text("Set cluster...").addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {

						// abre un diálogo para ingresar un valor, lo setea a
						// cada data de los seleccionados

						InputDialog id = new InputDialog(PlatformUIUtils.findShell(), "Set cluster id", "Condigure a number id to cluster", "", new IInputValidator() {

							@Override
							public String isValid(String newText) {
								Integer value = Ints.tryParse(newText);
								if (value == null || value < 1)
									return "The input '" + newText + "' is not a valid cluster id";
								return null;
							}
						});
						if (id.open() != Dialog.OK){
							return;
						}
							
							

						Integer clusterId = Ints.tryParse(id.getValue());
						List<Data> datum = tref.focusedElements();
						List<Sample> samples = new ArrayList<Sample>();
						for(Data d : datum){
							d.setClusterId(clusterId);
							samples.add(d.getSample());
						}
						ClusterData clusterData = clusteringResult.getClusterDataForCluster(clusterId); 
						if(clusterData != null){
							clusterData.getSampleList().clear();
							clusterData.setSampleList(samples);
						}else{
							clusteringResult.addClusterData(new ClusterData(samples, clusterId, null));
						}
							
							/*
							ClusterData newClusterData = new ClusterData(samples,clusterId,null);
							clusteringResult.addClusterData(newClusterData);
							createData(clusteringResult);*/
					
						
						/*if (sampleData.isEmpty())
							sampleData = tref.focusedElements();
						for (Sample datum : sampleData)
							datum.getPredefinedCluster().setGroupId(clusterid);*/

						tref.refresh();
					}

					
				});
			}
		};
	}

//	private List<Data> createData(AbstractExperiment experiment) {
//		return createData(experiment.getSamples());
//	}

	private void createData(ClusteringResult clusteringResult) {
		data = new LinkedList<Data>(); 
		if (clusteringResult != null && !clusteringResult.getClusterDataList().isEmpty()){
			for(Sample expsample : clusteringResult.getExperiment().getSamples()){
				ClusterData cd  = clusteringResult.getClusterDataForSample(expsample);
				if(cd != null){
					data.add(new Data(expsample,cd.getGroupId(),cd.getColorId()));	
				}else{
					data.add(new Data(expsample,-1,null));		
				}
			}
		}else{
			for(Sample sample : clusteringResult.getExperiment().getSamples()){
				data.add(new Data(sample,-1,null));
			}
		}
		
		/*if (groups != null && !groups.isEmpty())
			for (Map.Entry<Sample, ClusterData> entry : groups.entrySet())
				result.add(new Data(entry.getKey(), entry.getValue()));
		else
			for (Sample s : experiment.getSamples())
				result.add(new Data(s, new ClusterData(-1)));*/
		
		/*if (groups != null && !groups.isEmpty()){
			for (ClusterData entry : groups)
				result.add(new Data(entry.getSample(), entry.getGroupId()));
		}else {
			for (Sample s : experiment.getSamples()){
				result.add(new Data(s, new ClusterData(-1)));
			}
		}*/
		
		//return result;
	}
	

	@Override
	protected Point getInitialSize() {
		return new Point(500, 400);
	}

	private void setGroups() {
		if (readOnly())
			return;
		/*Map<Sample, ClusterData> groups = Maps.newHashMap();
		for (Data datum : data)
			groups.put(datum.getSample(), datum.getGroupid());
		experiment.setGroups(groups);*/
	}

	
	
	
	
	public static class Data {
		
		private Sample sample;
		private Integer clusterId;
		private String colorId;
		
		public Data(Sample sample, Integer clusterId, String colorId) {
			this.sample = sample;
			this.clusterId = clusterId;
			this.colorId = colorId;
		}
		
		public Sample getSample() {
			return sample;
		}
		
		public void setSample(Sample sample) {
			this.sample = sample;
		}
		
		public Integer getClusterId() {
			return clusterId;
		}
		
		public void setClusterId(Integer clusterId) {
			this.clusterId = clusterId;
		}

		public String getColorId() {
			return colorId;
		}  
		
	}

}
