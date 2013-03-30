package edu.unlp.medicine.bioplat.rcp.ui.utils.components;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class GUIUtils {
	
	public static Label addWrappedText(Composite container, String text, int size, boolean italic){
		GridLayout gridLayout = new GridLayout(1,false) ;
		gridLayout.marginWidth=30;
		gridLayout.marginHeight=30;
		container.setLayout( gridLayout ) ;
		GridData layoutData = new GridData(GridData.FILL_BOTH);
		layoutData.horizontalSpan = 1;
		container.setLayoutData(layoutData);
		Label label = new Label(container, SWT.WRAP);
		GridData gridData = new GridData( GridData.FILL_HORIZONTAL ) ;
		gridData.grabExcessHorizontalSpace = true ;
		gridData.horizontalAlignment = SWT.FILL;
		gridData.widthHint = 200;
		gridData.horizontalSpan = 2;
		label.setLayoutData( gridData ) ;
		
		FontData[] fD = label.getFont().getFontData();
		fD[0].setHeight(size);
		if (italic) fD[0].setStyle(SWT.ITALIC);
		label.setFont( new Font(container.getDisplay(),fD[0]));
		
		label.setText(text);
		
		return label;
	}
	
}
