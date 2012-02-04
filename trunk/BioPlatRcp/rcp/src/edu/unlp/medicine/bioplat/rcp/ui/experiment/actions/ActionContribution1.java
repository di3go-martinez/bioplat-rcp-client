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

	final static ExecutorService service = Executors.newFixedThreadPool(100);

	@Override
	public void run() {

		AbstractExperiment e = model();
		for (final Sample s : e.getSamples())
			for (final Gene g : s.getGenes()) {

				service.submit(new Runnable() {
					@Override
					public void run() {
						elUpdate(s, g);
					}
				});

			}

		try {
			service.awaitTermination(Integer.MAX_VALUE, TimeUnit.DAYS);// "infinito"...
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	private void elUpdate(Sample s, Gene g) {
		Double expr = s.getExpressionLevelForAGene(g);
		s.setExpressionLevelForAGene(g, expr * 0.4);
		try {
			Thread.sleep(new Random().nextInt(10) * 1000l);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}
}