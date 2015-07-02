package edu.unlp.medicine.bioplat.rcp.ui.genes.view.dialogs;

import java.util.ArrayList;
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

import edu.unlp.medicine.bioplat.rcp.ui.views.messages.Message;
import edu.unlp.medicine.bioplat.rcp.ui.views.messages.MessageManager;
import edu.unlp.medicine.bioplat.rcp.utils.GUIUtils;
import edu.unlp.medicine.bioplat.rcp.utils.Holder;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.domainLogic.framework.MetaPlat;
import edu.unlp.medicine.domainLogic.framework.exceptions.GeneNotFoundByIdException;
import edu.unlp.medicine.entity.gene.Gene;

public class IntegerInputDialog extends Dialog {

	private String separator = " ";

	public IntegerInputDialog(Shell parentShell) {
		super(parentShell);
		setBlockOnOpen(true);
		setShellStyle(SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
	}

	public IntegerInputDialog() {
		this(PlatformUIUtils.findShell());
	}


	private void setValue(String _value) {
		value = _value;
	}


	public int getNumber(){
		return Integer.valueOf(value);
		
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("How many random genes would you like to add?");

	}

	@Override
	protected Point getInitialSize() {
		return new Point(450, 150);
	}

	private String value = "";

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite result = (Composite) super.createDialogArea(parent);

		final Text text = new Text(result,SWT.BORDER);
		text.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());
		text.setToolTipText(getShell().getText());
		text.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				value = text.getText();
				
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