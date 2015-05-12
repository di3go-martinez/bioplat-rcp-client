package edu.unlp.medicine.bioplat.rcp.utils;

import java.awt.Container;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
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

	public static Font getFontForGrouptTitle(Composite container) {
		Label label = new Label(container, SWT.WRAP);

		FontData[] fD = label.getFont().getFontData();
		fD[0].setHeight(8);
		fD[0].setStyle(SWT.BOLD);
		
		return new Font(container.getDisplay(),fD[0]);
	}
	
	public static String getGeneListAsString(List<String> genesAddedList) {
		StringBuffer result = new StringBuffer("");
		for (String id : genesAddedList) {
			result.append(", " + id);
		}
		return result.toString().substring(2, result.toString().length());
		
		
	}
	
	
	
}
