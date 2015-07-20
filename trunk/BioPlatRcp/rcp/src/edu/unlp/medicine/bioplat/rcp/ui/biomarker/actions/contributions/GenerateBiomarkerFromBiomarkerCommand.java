package edu.unlp.medicine.bioplat.rcp.ui.biomarker.actions.contributions;

import java.util.List;

import edu.unlp.medicine.bioplat.rcp.ui.entities.EditorsId;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.entity.biomarker.Biomarker;
import edu.unlp.medicine.entity.biomarker.EditedBiomarker;
import edu.unlp.medicine.entity.gene.Gene;


public class GenerateBiomarkerFromBiomarkerCommand {
	
	private List<Gene> genesToCopy;
	private String name = "noName";
	private String author = "";
	private String description = "";
	
	public GenerateBiomarkerFromBiomarkerCommand(List<Gene> genesToCopy){
		this.genesToCopy = genesToCopy;
	}
	
	public void execute(){
		PlatformUIUtils.findDisplay().asyncExec(new Runnable() {
			 public void run() {
				 Biomarker newBiomarker = new EditedBiomarker(genesToCopy,name);
				 newBiomarker.setAuthor(author);
				 newBiomarker.setDescription(description);
			     PlatformUIUtils.openEditor(newBiomarker, EditorsId.biomarkerEditorId());  
			 }
		});
	}
	

}
