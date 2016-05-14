package edu.unlp.medicine.bioplat.rcp.application;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Link;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

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

				Composite container = Widgets.createDefaultContainer(parent);
				Button b = new Button(container, SWT.FLAT);
				b.setImage(PlatformUIUtils.findImage("About.png"));
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

				Composite dummy = new Composite(container, SWT.CENTER);
				dummy.setLayout(GridLayoutFactory.fillDefaults().margins(10, 10).create());
				dummy.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());
				// dummy.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
				Link link = new Link(dummy, SWT.NONE);
				String message = "Copyright (c) 2011, 2014 Bioplat. All rights reserved.\n" + "Visit <a href=\"http://www.cancergenomics.net/\">http://www.cancergenomics.net/</a>";
				link.setText(message);
				link.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());
				link.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						try {
							// Open default external browser
							PlatformUI.getWorkbench().getBrowserSupport().getExternalBrowser().openURL(new URL(e.text));
						} catch (PartInitException ex) {
							// TODO Auto-generated catch block
							ex.printStackTrace();
						} catch (MalformedURLException ex) {
							// TODO Auto-generated catch block
							ex.printStackTrace();
						}
					}
				});

				return container;

			}

			@Override
			protected void createButtonsForButtonBar(Composite parent) {
				createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
			}

		};
		d.open();
	}
}
