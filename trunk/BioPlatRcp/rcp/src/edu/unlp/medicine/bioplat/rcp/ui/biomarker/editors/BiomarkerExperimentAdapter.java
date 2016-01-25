/**
 * 
 */
package edu.unlp.medicine.bioplat.rcp.ui.biomarker.editors;

import java.util.List;
import java.util.Map;

import edu.unlp.medicine.domainLogic.framework.BiologicalValue;
import edu.unlp.medicine.domainLogic.framework.metasignatureGeneration.validation.SurvCompValidationResult;
import edu.unlp.medicine.domainLogic.framework.statistics.hierarchichalClustering.ClusteringResult;
import edu.unlp.medicine.entity.experiment.AbstractExperiment;
import edu.unlp.medicine.entity.experiment.ClusterData;
import edu.unlp.medicine.entity.experiment.ExperimentAppliedToAMetasignature;
import edu.unlp.medicine.entity.experiment.Sample;

/**
 * @author Juan
 * 
 */
public class BiomarkerExperimentAdapter {

	private ExperimentAppliedToAMetasignature appliedToAMetasignature;
	private SurvCompValidationResult survCompValidationResult;
	private boolean kaplanMaier = false;
	private String experimentName;
	private Double survCompIndex;
	private BiologicalValue significanceValue;
	private AbstractExperiment experiment;
	private List<ClusterData> groups;

	/**
	 * 
	 */
	public BiomarkerExperimentAdapter(
			ExperimentAppliedToAMetasignature appliedToAMetasignature) {
		this.appliedToAMetasignature = appliedToAMetasignature;
		this.experimentName = appliedToAMetasignature.getName();
		this.significanceValue = appliedToAMetasignature.getSignificanceValue();
		this.experiment = appliedToAMetasignature.getOriginalExperiment();
		//this.groups = appliedToAMetasignature.getGroups().getClusterDataList();
		this.experiment = appliedToAMetasignature.getOriginalExperiment();
		this.kaplanMaier = true;
	}

	public BiomarkerExperimentAdapter(
			SurvCompValidationResult survCompValidationResult) {
		this.survCompValidationResult = survCompValidationResult;
		this.experimentName = survCompValidationResult.getExperimentName();
		this.survCompIndex = new Double(
				survCompValidationResult.getSurvCompIndex());
		this.groups = survCompValidationResult.getSurvCompValidationConfig()
				.getGroups();
		this.experiment = survCompValidationResult
				.getSurvCompValidationConfig().getExperimentToValidate();
	}

	/**
	 * @return the appliedToAMetasignature
	 */
	public ExperimentAppliedToAMetasignature getAppliedToAMetasignature() {
		return appliedToAMetasignature;
	}

	/**
	 * @return the survCompValidationResult
	 */
	public SurvCompValidationResult getSurvCompValidationResult() {
		return survCompValidationResult;
	}

	public AbstractExperiment getExperiment() {
		return this.experiment;
	}

	public List<ClusterData> getGroups() {
		return this.groups;
	}

	/**
	 * @return the experimentName
	 */
	public String getExperimentName() {
		return experimentName;
	}

	/**
	 * @return the survCompIndex
	 */
	public Double getSurvCompIndex() {
		return survCompIndex;
	}

	/**
	 * @return the significanceValue
	 */
	public BiologicalValue getSignificanceValue() {
		return significanceValue;
	}

	public boolean isKaplanMaier() {
		return kaplanMaier;
	}

}
