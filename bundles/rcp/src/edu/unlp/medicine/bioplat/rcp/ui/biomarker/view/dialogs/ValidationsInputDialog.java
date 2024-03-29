package edu.unlp.medicine.bioplat.rcp.ui.biomarker.view.dialogs;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.google.common.collect.Lists;

import edu.unlp.medicine.bioplat.rcp.utils.Holder;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.domainLogic.framework.metasignatureGeneration.validation.Validation;

public class ValidationsInputDialog extends Dialog {

	private String separator = " ";
	private List<Validation> validations;

	public ValidationsInputDialog(Shell parentShell) {
		super(parentShell);
		setBlockOnOpen(true);
		setShellStyle(SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
	}

	public ValidationsInputDialog() {
		this(PlatformUIUtils.findShell());
	}

	/**
	 * Setea los genes como si fueran ingresados
	 * 
	 * @param genes
	 * @return
	 */
	public ValidationsInputDialog validations(List<Validation> validations) {
		this.validations = validations;
		StringBuilder $value = new StringBuilder();
		for (Validation validation : validations) {
			$value.append(validation.getName() + "\n");
		}
		setValue($value.toString());
		return this;
	}

	private void setValue(String _value) {
		value = _value;
	}

	public List<Validation> validations() {
		return this.validations;
	}

	public String[] getids() {
		return StringUtils.split(value, separator);
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Experiment you want to remove");

	}

	@Override
	protected Point getInitialSize() {
		return new Point(550, 400);
	}

	private String value = "";

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite result = (Composite) super.createDialogArea(parent);

		final Text text = new Text(result, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.READ_ONLY);
		text.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());
		text.setToolTipText(getShell().getText());
		text.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				value = StringUtils.replace(text.getText(), "\n", " ");
				value = StringUtils.replace(value, "\r", " ").replace("\t", " ");
			}
		});
		text.setText(value);
		return result;
	}

	/**
	 * Abre el diálogo y retorna si este fue aceptado o no, true o false
	 * respectivamente
	 * 
	 * @return
	 */
	public boolean accepted() {
		final Holder<Boolean> result = Holder.create();
		PlatformUIUtils.findDisplay().syncExec(new Runnable() {

			@Override
			public void run() {
				result.hold(open() == Dialog.OK);
			}
		});
		return result.value();
	}

}
