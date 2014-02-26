package edu.unlp.medicine.bioplat.rcp.application;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.bioplat.rcp.widgets.Widgets;

public class AboutAction extends Action {

	public AboutAction() {
		setText("About BioPlat");
	}

	@Override
	public void run() {
		Dialog d = new Dialog(PlatformUIUtils.findShell()) {
			@Override
			protected Control createDialogArea(Composite parent) {

				Composite control = Widgets.createDefaultContainer(parent);
				Button b = new Button(control, SWT.FLAT);
				b.setImage(PlatformUIUtils.findImage("about.png"));
				b.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());

				SelectionListener closeListener;
				b.addSelectionListener(closeListener = new SelectionListener() {

					@Override
					public void widgetSelected(SelectionEvent e) {
						close();

					}

					@Override
					public void widgetDefaultSelected(SelectionEvent e) {
						widgetSelected(e);
					}
				});

				return control;

			}

			@Override
			protected void createButtonsForButtonBar(Composite parent) {
				createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
			}

		};
		d.open();
	}

}
