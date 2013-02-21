package edu.unlp.medicine.bioplat.rcp.ui.experiment.actions.contributions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.eclipse.core.databinding.observable.value.WritableValue;

import com.google.common.base.Function;
import com.google.common.collect.DiscreteDomains;
import com.google.common.collect.Lists;
import com.google.common.collect.Ranges;

import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.AbstractWizard;
import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.PagesDescriptors;
import edu.unlp.medicine.bioplat.rcp.ui.entities.wizards.WizardPageDescriptor;
import edu.unlp.medicine.bioplat.rcp.utils.wizards.WizardModel;
import edu.unlp.medicine.domainLogic.ext.metasignatureCommands.LogRankTestCommand;
import edu.unlp.medicine.domainLogic.framework.metasignatureCommands.OneBiomarkerCommand;
import edu.unlp.medicine.domainLogic.framework.metasignatureGeneration.validation.LogRankTestValidationConfig;
import edu.unlp.medicine.domainLogic.framework.metasignatureGeneration.validation.ValidationConfig4DoingCluster;
import edu.unlp.medicine.domainLogic.framework.metasignatureGeneration.validation.experimentDescriptor.AbstractExperimentDescriptor;
import edu.unlp.medicine.domainLogic.framework.metasignatureGeneration.validation.experimentDescriptor.FromMemoryExperimentDescriptor;
import edu.unlp.medicine.domainLogic.framework.metasignatureGeneration.validation.significanceTest.AttributeTypeEnum;
import edu.unlp.medicine.domainLogic.framework.metasignatureGeneration.validation.significanceTest.IStatisticsSignificanceTest;
import edu.unlp.medicine.entity.biomarker.Biomarker;
import edu.unlp.medicine.entity.experiment.Experiment;
import edu.unlp.medicine.entity.experiment.exception.ExperimentBuildingException;
import edu.unlp.medicine.utils.monitor.Monitor;

/**
 * Wizard de creación de ValidationConfigs para la aplicación de experimentos a
 * biomarcadores
 * 
 * @author diego martínez
 * 
 */
public class ApplyLogRankTestWizard extends ValidationConfigWizard {

	public ApplyLogRankTestWizard(Biomarker biomarker) {
		super(biomarker);
		// TODO Auto-generated constructor stub
	}

	@Override
	public OneBiomarkerCommand createCommand(Biomarker biomarker,
			ArrayList<ValidationConfig4DoingCluster> validationConfigs) {
		 return new LogRankTestCommand(biomarker, validationConfigs);
		
	}


}
