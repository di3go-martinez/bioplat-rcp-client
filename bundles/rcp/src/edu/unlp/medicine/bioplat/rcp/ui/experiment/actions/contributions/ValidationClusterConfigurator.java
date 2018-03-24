package edu.unlp.medicine.bioplat.rcp.ui.experiment.actions.contributions;

import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.PagesDescriptors;
import edu.unlp.medicine.bioplat.rcp.utils.wizards.WizardModel;
import edu.unlp.medicine.domainLogic.framework.statistics.clusterers.RClustererKMeans;
import edu.unlp.medicine.domainLogic.framework.statistics.clusterers.RClustererKmeansHClust;
import edu.unlp.medicine.domainLogic.framework.statistics.clusterers.RClustererPAM;
import edu.unlp.medicine.domainLogic.framework.statistics.hierarchichalClustering.IClusterer;

public class ValidationClusterConfigurator {

	public static ValidationClusterConfigurator instance = new ValidationClusterConfigurator();
	
	private ValidationClusterConfigurator(){}
	
	public static ValidationClusterConfigurator getInstance(){
		return instance;
	}

	
	// mejor esta parte para no utilizar el instanceof
	public void configure(IClusterer clusterer, WizardModel model) {
		if(clusterer instanceof RClustererKMeans){
			RClustererKMeans concreteCluster = (RClustererKMeans)clusterer;
			concreteCluster.setAlgorithm((String)model.value(PagesDescriptors.KMEANS_ALGORITHM));
			concreteCluster.setIterations((String)model.value(PagesDescriptors.KMEANS_ITER));
		}else if(clusterer instanceof RClustererPAM){
			RClustererPAM concreteCluster = (RClustererPAM)clusterer;
			concreteCluster.setMetric((String)model.value(PagesDescriptors.PAM_METRIC));
			concreteCluster.setStandarized(model.booleanValue(PagesDescriptors.PAM_STANDARDIZED));
		}else if(clusterer instanceof RClustererKmeansHClust){
			RClustererKmeansHClust concreteCluster = (RClustererKmeansHClust)clusterer;
			concreteCluster.setMetric((String)model.value(PagesDescriptors.KMEANSHCLUST_METRIC));
		}
	}
}
