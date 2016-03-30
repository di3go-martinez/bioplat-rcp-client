package edu.unlp.medicine.bioplat.rcp.ui.experiment.actions.contributions;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.layout.GridDataFactory;
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
import edu.unlp.medicine.domainLogic.framework.metasignatureGeneration.validation.Validation;
import edu.unlp.medicine.utils.stringUtils.StringBuilderUtils;

public class ExecutionCommentsDialog extends Dialog{

		private Validation validation;
		
		public ExecutionCommentsDialog(Validation validation) {
			super(PlatformUIUtils.findShell());
			this.validation = validation;
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
			container.getShell().setText("Preprocessing done over the experiment");
			//container.getShell().setSize(parent.getDisplay().getBounds().width, parent.getDisplay().getBounds().height);
			Text t = new Text(container, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.H_SCROLL);
			t.setLayoutData(new GridData(GridData.FILL_BOTH));
			StringBuilder sb = new StringBuilder("Information about the execution:");
			StringBuilderUtils.appendAndNewLine(sb, "Samples removed because all of its expression values are incorrect: " + (validation.getValidationResult().getSamples_removed_expression_error().isEmpty()?"none":validation.getValidationResult().getSamples_removed_expression_error()));
			StringBuilderUtils.appendAndNewLine(sb, "Samples removed because time attribute is incorrect: " + (validation.getValidationResult().getSamples_removed_time_error().isEmpty()?"none":validation.getValidationResult().getSamples_removed_time_error()));
			StringBuilderUtils.appendAndNewLine(sb, "Samples removed because event attribute is incorrect: " + (validation.getValidationResult().getSamples_removed_event_error().isEmpty()?"none":validation.getValidationResult().getSamples_removed_event_error()));
			StringBuilderUtils.appendAndNewLine(sb, "Genes remove because it has at least one incorrect value: " + (validation.getValidationResult().getGenes_removed().isEmpty()?"none":validation.getValidationResult().getGenes_removed()));
			t.setText(sb.toString());
		    t.setEditable(false);
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