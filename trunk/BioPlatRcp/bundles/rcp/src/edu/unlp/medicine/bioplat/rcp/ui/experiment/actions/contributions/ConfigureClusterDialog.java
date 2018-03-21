package edu.unlp.medicine.bioplat.rcp.ui.experiment.actions.contributions;


import java.util.List;
import java.util.Map;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
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

@Deprecated
public class ConfigureClusterDialog extends Dialog {

	// puede ser null
	private AbstractExperiment experiment;
	//private List<Data> data;
	private TableReference tref;
	private List<Sample> data;
	
	
	@Deprecated
	protected ConfigureClusterDialog(AbstractExperiment experiment) {
		super(PlatformUIUtils.findShell());
		this.experiment = experiment;
		data = experiment.getSamples();
		//data = createData(experiment);
	}

	public ConfigureClusterDialog(ClusteringResult clusteringResult) {
		super(PlatformUIUtils.findShell());
		data = createData(clusteringResult);
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Cluster Configuration");
	}

	@Override
	protected Control createContents(Composite parent) {
		Composite container = Widgets.createDefaultContainer(parent);
		
		// Label introdudctionLabel = new Label(parent, SWT.WRAP);
		// introdudctionLabel.setText("Use shift and control for select multiple samples. Then, right click and use set cluster to assign a clusterID to all selected samples");
		final ColumnBuilder titleColumnBuilder = ColumnBuilder.create().editable(!readOnly()).property("predefinedCluster.groupId").title("Cluster ID");
		final TableBuilder tableBuilder = TableBuilder.create(container).input(data)//
				.hideSelectionColumn()//
				.addColumn(ColumnBuilder.create().property("name").title("Sample").width(150))
				.addColumn(titleColumnBuilder);
		generateClinicalDataColumns(tableBuilder);		
		if (!readOnly())
			tableBuilder.contextualMenuBuilder(menuBuilder());
		tref = tableBuilder.build();
		if (readOnly())
			generateClusterColors(tref);
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
		container.setSize(800, container.getSize().y);
		return container;
	}

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
		if(!this.experiment.getSamples().isEmpty()){
			List<String> attrs = experiment.getSamples().get(0).getClinicalAttributeNames();
			for(String attr : attrs){
				tableBuilder.addColumn(ColumnBuilder.create().property("sampleClincalData.clinicalData['" + attr + "']").title(attr));
			}
		}
	}

	private boolean readOnly() {
		return experiment == null;
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
								if (Ints.tryParse(newText) == null)
									return "The input '" + newText + "' is not a valid cluster id";
								return null;
							}
						});
						if (id.open() != Dialog.OK)
							return;

						Integer clusterid = Ints.tryParse(id.getValue());
						List<Sample> sampleData = tref.selectedElements();
						if (sampleData.isEmpty())
							sampleData = tref.focusedElements();
						for (Sample datum : sampleData)
							//datum.getPredefinedCluster().setGroupId(clusterid);

						tref.refresh();
					}
				});
			}
		};
	}

//	private List<Data> createData(AbstractExperiment experiment) {
//		return createData(experiment.getSamples());
//	}

	private List<Sample> createData(ClusteringResult clusteringResult) {
		List<Sample> result = Lists.newArrayList();
		if (clusteringResult != null && !clusteringResult.getClusterDataList().isEmpty()){
			for(ClusterData cd : clusteringResult.getClusterDataList()){
				//result.add(cd.getSample());
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
		
		return result;
	}
	

	@Override
	protected Point getInitialSize() {
		return new Point(500, 500);
	}

	private void setGroups() {
		if (readOnly())
			return;
		/*Map<Sample, ClusterData> groups = Maps.newHashMap();
		for (Data datum : data)
			groups.put(datum.getSample(), datum.getGroupid());
		experiment.setGroups(groups);*/
	}

	// TODO: DavidClustering ya no tiene sentido al parecer. FIXIT
	public static class Data {
		
		// TODO hacer que sea integer!!
		private ClusterData groupid;
		
		public Data(ClusterData cd) {
			this.groupid = cd;
		}

		//private Sample sample;
		
		public ClusterData getGroupid() {
			return groupid;
		}

		public void setGroupid(ClusterData groupid) {
			this.groupid = groupid;
		}
		
	}

}
