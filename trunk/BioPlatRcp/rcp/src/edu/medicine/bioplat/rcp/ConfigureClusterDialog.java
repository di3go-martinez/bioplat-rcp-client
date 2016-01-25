package edu.medicine.bioplat.rcp;

import java.util.List;
import java.util.Map;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;

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
import edu.unlp.medicine.entity.experiment.AbstractExperiment;
import edu.unlp.medicine.entity.experiment.ClusterData;
import edu.unlp.medicine.entity.experiment.Sample;

//TODO: DavidClustering (Revisar si esta siendo llamada)
@Deprecated
public class ConfigureClusterDialog extends Dialog {

	private AbstractExperiment experiment;
	private List<Data> data;
	private TableReference tref;

	protected ConfigureClusterDialog(AbstractExperiment experiment) {
		super(PlatformUIUtils.findShell());
		this.experiment = experiment;
	}

	@Override
	protected Control createContents(Composite parent) {
		Composite container = Widgets.createDefaultContainer(parent);

		// Label introdudctionLabel = new Label(parent, SWT.WRAP);
		// introdudctionLabel.setText("Use shift and control for select multiple samples. Then, right click and use set cluster to assign a clusterID to all selected samples");
		tref = TableBuilder.create(container).input(data = createData())//
				.hideSelectionColumn()//
				.addColumn(ColumnBuilder.create().property("sample").title("Sample").width(200))//
				.addColumn(ColumnBuilder.create().editable().property("groupid").title("Cluster ID")).contextualMenuBuilder(menuBuilder()).build();
		Button ok = new Button(container, SWT.NONE);
		ok.setText("OK");
		ok.setLayoutData(GridDataFactory.fillDefaults().align(SWT.CENTER, SWT.CENTER).create());
		ok.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setGroups();
				close();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});
		return container;
	}

	private MenuBuilder menuBuilder() {

		return new MenuBuilder() {

			@Override
			public void build(Menu menu) {
				Image openImage = PlatformUIUtils.findImage("clustering.png");
				MenuItemContribution.create(menu).image(openImage).text("set cluster...").addSelectionListener(new SelectionAdapter() {
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
						List<Data> sampleData = tref.selectedElements();
						if (sampleData.isEmpty())
							sampleData = tref.focusedElements();
						for (Data datum : sampleData)
							datum.setGroupid(clusterid);

						tref.refresh();
					}
				});
			}
		};
	}

	private List<Data> createData() {
		List<Data> result = Lists.newArrayList();

		/*List<ClusterData> groups = experiment.getGroups().getClusterDataList();

		if (groups != null && !groups.isEmpty()){
			for(ClusterData cd : groups){
				//result.add(new Data(cd.getSample(),String.valueOf(cd.getGroupId())));
			}
		}else{
			// TODO: DavidClustering ¿es necesario si se setea previamente?
			for (Sample s : experiment.getSamples()){
				result.add(new Data(s, "-1"));
			}
		}*/
			/*for (Map.Entry<Sample, ClusterData> entry : groups.entrySet())
				result.add(new Data(entry.getKey(), entry.getValue().toString()));
		else
			for (Sample s : experiment.getSamples())
				result.add(new Data(s, "-1"));*/
		return result;

	}

	@Override
	protected Point getInitialSize() {
		return new Point(400, 500);
	}

	private void setGroups() {
		/*List<ClusterData> groups = Lists.newArrayList();
		for (Data datum : data){
			ClusterData cd = new ClusterData();
			//cd.setSample(datum.getSample());
			cd.setGroupId(Integer.parseInt(datum.getGroupid()));
			groups.add(cd);
		}
		experiment.getGroups().setClusterDataList(groups); //experiment.setGroups(groups);*/
	}

	public static class Data {
		public Data(Sample s, String i) {
			this.sample = s;
			this.groupid = i;
		}

		// FIXME hacer que groupid sea un integer y no un string?
		public void setGroupid(Integer clusterid) {
			setGroupid(clusterid.toString());
		}

		private Sample sample;

		public String getGroupid() {
			return groupid;
		}

		@Deprecated
		public void setGroupid(String groupid) {
			this.groupid = groupid;
		}

		public Sample getSample() {
			return sample;
		}

		public void setSample(Sample sample) {
			this.sample = sample;
		}

		// TODO hacer que sea integer!!
		private String groupid;
	}

}
