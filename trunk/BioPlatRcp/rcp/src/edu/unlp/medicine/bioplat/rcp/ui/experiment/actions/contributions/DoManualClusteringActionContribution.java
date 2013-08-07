package edu.unlp.medicine.bioplat.rcp.ui.experiment.actions.contributions;

import java.util.List;

import com.google.common.collect.Lists;

import edu.unlp.medicine.bioplat.rcp.ui.entities.editors.contributors.AbstractActionContribution;
import edu.unlp.medicine.bioplat.rcp.ui.views.messages.Message;
import edu.unlp.medicine.bioplat.rcp.ui.views.messages.MessageManager;
import edu.unlp.medicine.domainLogic.framework.MetaPlat;
import edu.unlp.medicine.domainLogic.framework.metasignatureGeneration.validation.ValidationConfig4DoingCluster;
import edu.unlp.medicine.domainLogic.framework.metasignatureGeneration.validation.experimentDescriptor.FromMemoryExperimentDescriptor;
import edu.unlp.medicine.entity.biomarker.Biomarker;
import edu.unlp.medicine.entity.biomarker.EditedBiomarker;
import edu.unlp.medicine.entity.experiment.Experiment;
import edu.unlp.medicine.entity.gene.Gene;

/**
 * Aplica un cluster precalculado en el experimento actual, si es que lo tiene
 * configurado
 * 
 * @author diego
 * 
 */
public abstract class DoManualClusteringActionContribution extends AbstractActionContribution<Experiment> {

	@Override
	public void run() {

		if (model().hasClusteringInformation()) {
			ValidationConfig4DoingCluster v = ValidationConfig4DoingCluster.withPrecalculatedCluster(new FromMemoryExperimentDescriptor(model()));
			run0(v);
		} else
			MessageManager.INSTANCE.add(Message.warn("The experiment " + model().getName() + " has no configuration for clustering"));

	}

	/**
	 * TODO a sacar...
	 */
	@Deprecated
	protected final Biomarker dummyBiomarker() {
		Biomarker b = new EditedBiomarker("dummy");
		List<Gene> genes = Lists.newArrayList();
		genes.add(MetaPlat.getInstance().findGene("1"));
		return b;
	}

	protected abstract void run0(ValidationConfig4DoingCluster validation);
}
