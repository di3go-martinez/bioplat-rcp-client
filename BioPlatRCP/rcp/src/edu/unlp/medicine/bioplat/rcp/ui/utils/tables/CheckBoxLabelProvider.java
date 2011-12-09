package edu.unlp.medicine.bioplat.rcp.ui.utils.tables;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.swt.graphics.Image;

import edu.unlp.medicine.bioplat.rcp.ui.utils.accesors.Accesor;

//TODO no está en uso
public class CheckBoxLabelProvider extends ColumnLabelProvider {

	private Accesor accesor;

	public CheckBoxLabelProvider(Accesor accesor) {
		Assert.isNotNull(accesor);
		this.accesor = accesor;
	}

	@Override
	public Image getImage(Object element) {
		return super.getImage(element);
	}
}
