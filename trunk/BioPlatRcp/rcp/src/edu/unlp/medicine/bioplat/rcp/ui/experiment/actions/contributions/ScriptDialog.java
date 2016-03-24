package edu.unlp.medicine.bioplat.rcp.ui.experiment.actions.contributions;

import java.awt.FlowLayout;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.bioplat.rcp.widgets.Widgets;

public class ScriptDialog extends Dialog{

	private String script;
	
	public ScriptDialog(String script) {
		super(PlatformUIUtils.findShell());
		this.script = script;
		//setText("R Script");
	}
	

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		//newShell.setText("R Scripts");
		
		
	}

	
	@Override
	protected Control createContents(Composite parent) {
		Composite container = Widgets.createDefaultContainer(parent);
		container.getShell().setText("R Scripts for the experiment");
		//container.getShell().setSize(parent.getDisplay().getBounds().width, parent.getDisplay().getBounds().height);
		Text t = new Text(container, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.H_SCROLL);
		t.setLayoutData(new GridData(GridData.FILL_BOTH));
	    t.setText(script);
	    Button ok = new Button(container, SWT.NONE);
		ok.setText("OK");
		ok.setLayoutData(GridDataFactory.fillDefaults().align(SWT.CENTER, SWT.CENTER).create());
		ok.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				close();
			}
		});
		container.layout(true);
		return container;
	}
}
