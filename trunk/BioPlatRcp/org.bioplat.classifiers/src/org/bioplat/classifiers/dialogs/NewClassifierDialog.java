package org.bioplat.classifiers.dialogs;

import java.util.List;
import java.util.Map;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.google.common.collect.Maps;

import edu.unlp.medicine.bioplat.core.preferences.AuthorPreferencePage;
import edu.unlp.medicine.bioplat.rcp.ui.views.messages.Message;
import edu.unlp.medicine.bioplat.rcp.ui.views.messages.MessageManager;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.bioplat.rcp.widgets.Widgets;
import edu.unlp.medicine.domainLogic.framework.classifiers.ClassifierCreator;
import edu.unlp.medicine.domainLogic.framework.metasignatureGeneration.validation.Validation;
import edu.unlp.medicine.entity.experiment.ClusterData;

public class NewClassifierDialog extends Dialog {

	private Validation validation;
	private String classifierName;

	public NewClassifierDialog(Validation validation) {
		super(PlatformUIUtils.findShell());
		setBlockOnOpen(true);
		this.validation = validation;
	}

	@Override
	protected void configureShell(Shell newShell) {
		// TODO Auto-generated method stub
		super.configureShell(newShell);
		newShell.setText("New Classifier");
	}

	@Override
	protected Point getInitialSize() {
		return new Point(600, 500);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = Widgets.createDefaultContainer(parent, 2);
		container.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());
		GridLayoutFactory layoutf = GridLayoutFactory.fillDefaults().numColumns(2).equalWidth(false).margins(10, 10)
				.spacing(10, 10);
		container.setLayout(layoutf.create());
		new Label(container, SWT.SHADOW_ETCHED_IN).setText("Name: ");
		Text t = new Text(container, SWT.BORDER);
		t.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());
		t.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				classifierName = ((Text) e.widget).getText();
				checkOkEnabled();
			}

		});

		Group group = new Group(container, SWT.NONE);
		group.setLayout(layoutf.create());
		group.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).span(2, 1).create());
		for (final ClusterData cluster : clusters()) {
			new Label(group, SWT.BOLD).setText("Grupo " + cluster.getGroupId() + ": ");
			t = new Text(group, SWT.FLAT);
			t.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());
			t.addModifyListener(new ModifyListener() {

				@Override
				public void modifyText(ModifyEvent e) {
					String text = ((Text) e.widget).getText();
					groupLabels.put(cluster.getGroupId(), text);
				}
			});
		}
		return container;
	}

	private void checkOkEnabled() {
		boolean enabled = !classifierName.trim().isEmpty();

		for (String value : groupLabels.values())
			enabled = value != null && !value.trim().isEmpty();

		getButton(IDialogConstants.OK_ID).setEnabled(enabled);

	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		super.createButtonsForButtonBar(parent);
		getButton(IDialogConstants.OK_ID).setEnabled(false);
	}

	private Map<Integer, String> groupLabels = Maps.newHashMap();

	private List<ClusterData> clusters() {
		return validation.getClusteringResult().getClusterDataList();
	}

	@Override
	protected void okPressed() {
		new ClassifierCreator().create(validation, author(), classifierName, groupLabels);
		MessageManager.INSTANCE.add(Message.info("Classifier " + classifierName + " created."));
		super.okPressed();
	}

	private String author() {
		return AuthorPreferencePage.author();
	}

}
