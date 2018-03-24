package edu.unlp.medicine.bioplat.rcp.ui.experiment.actions.contributions.split;

import static edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils.*;
import java.util.Map;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.google.common.collect.Maps;

import edu.unlp.medicine.bioplat.rcp.ui.experiment.editors.ExperimentEditor;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.bioplat.rcp.widgets.Widgets;
import edu.unlp.medicine.entity.experiment.AbstractExperiment;
import edu.unlp.medicine.entity.experiment.Experiment;

public class SplitterDialog extends Dialog {

	private static final String VALIDATION = "Validation (%): ";

	private static final String TESTING = "Testing (%): ";

	private static final String TRAINNING = "Trainning (%): ";

	private AbstractExperiment dataset;

	private SplitterDialog(AbstractExperiment dataset) {
		super(PlatformUIUtils.findShell());

		initialize(dataset);
	}

	public static SplitterDialog create(AbstractExperiment experiment) {
		SplitterDialog dialog = new SplitterDialog(experiment);
		dialog.setBlockOnOpen(true);
		return dialog;
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Split Dataset");
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite result = (Composite) super.createDialogArea(parent);
		result.setLayout(GridLayoutFactory.fillDefaults().margins(10, 10).numColumns(2).equalWidth(false).create());

		createPercentageInput(TRAINNING, result);
		createPercentageInput(TESTING, result);
		createPercentageInput(VALIDATION, result);
		createInfoLabel(result);

		return result;
	}

	private Label info;

	private void createInfoLabel(Composite result) {
		info = new Label(result, SWT.BOLD);
		info.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());
		info.setForeground(info.getDisplay().getSystemColor(SWT.COLOR_RED));
	}

	private void createPercentageInput(final String label, Composite result) {
		new Label(result, SWT.NONE).setText(label);
		Text percentInput = new Text(result, SWT.BORDER);
		percentInput.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());
		percentInput.setText(values.get(label).toString());
		percentInput.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				Text text = (Text) e.getSource();
				try {
					Double i = Double.valueOf(text.getText());
					configure(label, i);
				} catch (NumberFormatException error) {
					setInfo("'" + text.getText() + "' is not valid");
				} catch (IllegalArgumentException illegal) {
					setInfo(text.getText() + " is not valid");
				}
			}

		});

	}

	private Splitter splitter;

	private void initialize(AbstractExperiment dataset) {
		this.dataset = dataset;
		values = Maps.newHashMap();
		values.put(TRAINNING, 33.0);
		values.put(TESTING, 33.0);
		values.put(VALIDATION, 34.0);
		splitter = new Splitter();
		splitter.setup(dataset, values.get(TRAINNING), values.get(TESTING), values.get(VALIDATION));
	}
	
	@Override
	protected Control createButtonBar(Composite parent) {
	//esto es lo último que se hace, está todo el diálogo creado
		Control result = super.createButtonBar(parent);
		updateChecks();
		return result;
		
	}

	private Map<String, Double> values;

	private void configure(String label, Double value) {
		values.put(label, value);
		updateChecks();
	}

	private void updateChecks() {
		SplitterSetupResult result = splitter.setup(dataset, values.get(TRAINNING), values.get(TESTING),
				values.get(VALIDATION));
		if (!result.isOk())
			setInfo(result.message());
		else
			cleanInfo();

		Button button = getButton(OK);

		button.setEnabled(result.isOk());
	}

	private void cleanInfo() {
		setInfo("");
	}

	private void setInfo(String message) {
		info.setText(message);
		info.redraw();
		info.getParent().layout();
	}

	@Override
	protected void okPressed() {
		for (Experiment dataset : splitter.split())
			openEditor(dataset, ExperimentEditor.id());
		super.okPressed();
	}

	@Override
	protected Point getInitialSize() {
		return new Point(400, 250);
	}
}
