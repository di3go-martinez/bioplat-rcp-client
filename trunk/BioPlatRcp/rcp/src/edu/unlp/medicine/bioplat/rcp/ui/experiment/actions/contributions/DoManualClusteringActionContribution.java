package edu.unlp.medicine.bioplat.rcp.ui.experiment.actions.contributions;

import edu.unlp.medicine.bioplat.rcp.editor.ModelProvider;
import edu.unlp.medicine.bioplat.rcp.ui.entities.editors.contributors.AbstractActionContribution;
import edu.unlp.medicine.bioplat.rcp.ui.views.messages.Message;
import edu.unlp.medicine.bioplat.rcp.ui.views.messages.MessageManager;
import edu.unlp.medicine.domainLogic.framework.metasignatureGeneration.validation.ValidationConfig4DoingCluster;
import edu.unlp.medicine.entity.biomarker.Biomarker;
import edu.unlp.medicine.entity.biomarker.EditedBiomarker;
import edu.unlp.medicine.entity.experiment.Experiment;

/**
 * Aplica un cluster precalculado en el experimento actual, si es que lo tiene
 * configurado
 * 
 * @author Diego Martínez
 * 
 */
public abstract class DoManualClusteringActionContribution extends AbstractActionContribution<Experiment> {

	@Override
	public void run() {

		if (model().hasClusteringInformation()) {
			// ValidationConfig4DoingCluster v =
			// ValidationConfig4DoingCluster.withPrecalculatedCluster(new
			// FromMemoryExperimentDescriptor(model()));
			// final DialogModel<?> dm = new
			// DialogModel<ValidationConfig4DoingCluster>(PlatformUIUtils.findShell(),
			// this.modelP(v)) {
			// @Override
			// protected Control createDialogArea(Composite parent) {
			// final Composite container =
			// Widgets.createDefaultContainer(parent);
			// container.setLayout(GridLayoutFactory.createFrom((GridLayout)
			// container.getLayout()).numColumns(2).margins(10, 10).create());
			// Widgets.createTextWithLabel(container, "Number Of Clusters",
			// modelProvider().model(), "numberOfClusters");
			// return container;
			// }
			// };
			//
			// PlatformUIUtils.findDisplay().syncExec(new Runnable() {
			//
			// @Override
			// public void run() {
			// dm.open();
			// }
			// });

			run0();

		} else
			MessageManager.INSTANCE.add(Message.warn("The experiment " + model().getName() + " has no configuration for clustering"));

	}

	private ModelProvider<ValidationConfig4DoingCluster> modelP(final ValidationConfig4DoingCluster v) {
		return new ModelProvider<ValidationConfig4DoingCluster>() {

			@Override
			public ValidationConfig4DoingCluster model() {
				return v;
			}
		};
	}

	@Override
	public Experiment model() {
		return super.model();
	}

	/**
	 * TODO a sacar...
	 */
	@Deprecated
	protected final Biomarker dummyBiomarker() {
		Biomarker b = new EditedBiomarker("dummy");
		b.addRandomGenes(1);
		return b;
	}

	protected abstract void run0();
}
