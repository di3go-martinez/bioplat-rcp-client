package layouts;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class BioplatLayouts {
	
//	public static CLabel getLabelWrapped(Composite parent, String texto, int size){
//		
//
//	}

	public static void configureAndWrapLabel(CLabel aLabel, int i, String string) {
		
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL); 
		aLabel.setLayoutData(gridData);
		
		FontData[] fD = aLabel.getFont().getFontData();
		fD[0].setHeight(10);
		fD[0].setStyle(SWT.ITALIC);
		aLabel.setFont( new Font(aLabel.getDisplay(),fD[0]));
		
	}

	

}
