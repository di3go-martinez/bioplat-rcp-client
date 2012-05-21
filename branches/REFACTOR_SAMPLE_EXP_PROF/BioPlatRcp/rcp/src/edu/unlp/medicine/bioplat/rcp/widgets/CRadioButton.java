package edu.unlp.medicine.bioplat.rcp.widgets;


import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public class CRadioButton {

	public void setText(String string) {
		radioButton.setText(string);
	}

	private Button radioButton;

	        /**
     * 
     * @param container
     * @param model
     * @param property
     * @param style
     * @deprecated agregar a alg�n factory y hacer package-private
     */
	@Deprecated
    public <T> CRadioButton(Composite container, T model, String property, int style) {
		radioButton = new Button(container, style | SWT.RADIO);
        DataBindingContextHolder.dataBindingGlobalContext()//
				.bindValue(WidgetProperties.selection().observe(radioButton), BeanProperties.value(property).observe(model));
	}

}
