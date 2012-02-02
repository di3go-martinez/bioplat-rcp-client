package edu.unlp.medicine.bioplat.rcp.ui.experiment.actions;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import edu.unlp.medicine.bioplat.rcp.ui.entities.editors.contributors.AbstractActionContribution;
import edu.unlp.medicine.entity.experiment.AbstractExperiment;
import edu.unlp.medicine.entity.experiment.Sample;
import edu.unlp.medicine.entity.gene.Gene;

/**
 * Heavy update example
 * 
 * @author diego
 * 
 */
public class ActionContribution1 extends AbstractActionContribution<AbstractExperiment> {

	public ActionContribution1() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		final ExecutorService service = Executors.newFixedThreadPool(100);
		AbstractExperiment e = model();
		for (final Sample s : e.getSamples())
			for (final Gene g : s.getGenes()) {

				service.execute(new Runnable() {
					@Override
					public void run() {
						elUpdate(s, g);
					}
				});

			}
		service.shutdown();
		try {
			service.awaitTermination(Integer.MAX_VALUE, TimeUnit.DAYS);// "infinito"...
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	private void elUpdate(Sample s, Gene g) {
		Double expr = s.getExpressionLevelForAGene(g);
		try {
			Thread.sleep(new Random().nextInt(1) * 1000l);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		s.setExpressionLevelForAGene(g, expr * 0.4);
	}
}