package edu.unlp.medicine.bioplat.rcp.ui.experiment.actions;

import edu.unlp.medicine.bioplat.rcp.ui.entities.editors.contributors.AbstractActionContribution;
import edu.unlp.medicine.bioplat.rcp.ui.views.messages.Message;
import edu.unlp.medicine.bioplat.rcp.ui.views.messages.MessageManager;
import edu.unlp.medicine.entity.experiment.AbstractExperiment;
import edu.unlp.medicine.entity.experiment.Sample;
import edu.unlp.medicine.entity.gene.Gene;

public class AbsoluteValue extends AbstractActionContribution<AbstractExperiment> {

	@Override
	public void run() {
		AbstractExperiment experiment = model();
		double expressionValue;
		for (Sample sample : experiment.getSamples()) {
			
			for (Gene gene : experiment.getGenes()) {

				expressionValue = experiment.getExpressionLevelForAGene(sample, gene);
				if (expressionValue < 0)
					experiment.setExpressionLevelForAGene(sample, gene, expressionValue * (-1));

			}
		}
		MessageManager.INSTANCE.add(Message.info("Absolute value on expression data...was successfully executed"));
	}

	
	public static void main(String[] args) {
		System.out.println("Total Memory"+(Runtime.getRuntime().totalMemory()/1024)/1024);    
	       System.out.println("Free Memory"+(Runtime.getRuntime().freeMemory()/1024)/1024);
	}
	
}
