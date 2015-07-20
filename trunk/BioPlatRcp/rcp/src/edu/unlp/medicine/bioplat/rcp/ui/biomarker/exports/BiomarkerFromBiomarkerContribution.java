package edu.unlp.medicine.bioplat.rcp.ui.biomarker.exports;

import java.util.List;

import edu.unlp.medicine.bioplat.rcp.ui.biomarker.actions.contributions.GenerateBiomarkerFromBiomarkerCommand;
import edu.unlp.medicine.bioplat.rcp.ui.biomarker.contributor.BiomarkerGenesSelectedActionContribution;
import edu.unlp.medicine.entity.experiment.Experiment;
import edu.unlp.medicine.entity.gene.Gene;

public class BiomarkerFromBiomarkerContribution extends BiomarkerGenesSelectedActionContribution<Experiment> {

	@Override
	protected void executeOn(List<Gene> genes) {
		new GenerateBiomarkerFromBiomarkerCommand(genes).execute();
	}
}
