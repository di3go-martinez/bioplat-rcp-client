package edu.unlp.medicine.bioplat.rcp.ui.experiment.actions.contributions;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.unlp.medicine.bioplat.rcp.ui.entities.editors.contributors.AbstractActionContribution;
import edu.unlp.medicine.bioplat.rcp.utils.Holder;
import edu.unlp.medicine.entity.experiment.AbstractExperiment;
import edu.unlp.medicine.entity.experiment.Sample;
import edu.unlp.medicine.entity.gene.Gene;

/**
 * Heavy update example
 * 
 * @author diego
 * 
 */
public class By4ActionContribution extends AbstractActionContribution<AbstractExperiment> {

	private static final Logger logger = LoggerFactory.getLogger(By4ActionContribution.class);

	public By4ActionContribution() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {

		final ExecutorService service = Executors.newFixedThreadPool(20);

		final AbstractExperiment e = model();
		long totalcells = e.getSamples().size() * e.getGenes().size();
		final Holder<Long> holder = Holder.create(0l);

		for (final Sample s : e.getSamples())
			for (final Gene g : e.getGenes()) {

				service.submit(new Runnable() {
					@Override
					public void run() {
						elUpdate(e, s, g);
						holder.hold(holder.value() + 1);
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

		String msg = "total a procesar " + totalcells + ": procesadas: " + holder.value();
		final long realtotal = holder.value() + e.getGenes().size();
		// esto hay que revisarlo bien todavía...
		if (realtotal < totalcells)
			logger.warn(msg + " faltaron: " + (totalcells - realtotal + 1 - e.getGenes().size()));
		else
			logger.info(msg);
	}

	private void elUpdate(AbstractExperiment e, Sample s, Gene g) {
		Double expr = e.getExpressionLevelForAGene(s,g);
		e.setExpressionLevelForAGene(s,g, expr * 0.4);
		// delay para que los cambios no sean "instantáneos". El delay puede ser
		// el tiempo consumido en: calcular el dato, hacer un análisis para
		// determinar algo, etc etc
		try {
			Thread.sleep(new Random().nextInt(5) * 10l);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}