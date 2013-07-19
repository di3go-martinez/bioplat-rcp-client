package edu.medicine.bioplat.rcp;

import java.util.List;
import java.util.Map;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.ColumnBuilder;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableBuilder;
import edu.unlp.medicine.bioplat.rcp.ui.utils.tables.TableReference;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.bioplat.rcp.widgets.Widgets;
import edu.unlp.medicine.entity.experiment.AbstractExperiment;
import edu.unlp.medicine.entity.experiment.Sample;

public class ConfigureClusterDialog extends Dialog {

	private AbstractExperiment experiment;
	private List<Data> data;

	protected ConfigureClusterDialog(AbstractExperiment experiment) {
		super(PlatformUIUtils.findShell());
		this.experiment = experiment;
	}

	@Override
	protected Control createContents(Composite parent) {
		Composite container = Widgets.createDefaultContainer(parent);
		TableReference ref = TableBuilder.create(container).input(data = createData())//
				.addColumn(ColumnBuilder.create().property("sample"))//
				.addColumn(ColumnBuilder.create().editable().property("groupid")).build();
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
		return container;
	}

	private List<Data> createData() {
		List<Data> result = Lists.newArrayList();
		for (Sample s : experiment.getSamples())
			result.add(new Data(s, "-1"));
		return result;

	}

	@Override
	protected Point getInitialSize() {
		return new Point(400, 500);
	}

	private void setGroups() {
		Map<Sample, Integer> groups = Maps.newHashMap();
		for (Data datum : data)
			groups.put(datum.getSample(), new Integer(datum.getGroupid()));
		experiment.setGroups(groups);
	}

	public static class Data {
		public Data(Sample s, String i) {
			this.sample = s;
			this.groupid = i;
		}

		private Sample sample;

		public String getGroupid() {
			return groupid;
		}

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
