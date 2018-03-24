package edu.unlp.medicine.bioplat.rcp.ui.biomarker.exports;

import java.util.List;

import edu.unlp.medicine.bioplat.rcp.ui.biomarker.actions.contributions.GenerateBiomarkerExportCommand;
import edu.unlp.medicine.bioplat.rcp.ui.biomarker.contributor.BiomarkerGenesSelectedActionContribution;
import edu.unlp.medicine.entity.experiment.Experiment;
import edu.unlp.medicine.entity.gene.Gene;

public class BiomarkerExportContribution extends BiomarkerGenesSelectedActionContribution<Experiment> {

	@Override
	protected void executeOn(List<Gene> genes,String name,String author, String description) {
		new GenerateBiomarkerExportCommand(genes,name,author,description).execute();
	}
}
