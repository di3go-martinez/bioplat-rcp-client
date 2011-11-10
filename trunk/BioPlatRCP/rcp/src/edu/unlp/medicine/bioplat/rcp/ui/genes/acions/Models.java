package edu.unlp.medicine.bioplat.rcp.ui.genes.acions;

import edu.unlp.medicine.entity.biomarker.Biomarker;

@Deprecated
public class Models {
	
	private Biomarker biomarker;
	
	private static Models instance = new Models();
	
	public static Models getInstance() {
		return instance;
	}
	
	public Biomarker getActiveBiomarker() {
		return biomarker;
	}

	public void setActiveBiomarker(Biomarker biomarker) {
		this.biomarker = biomarker;
	}
}
