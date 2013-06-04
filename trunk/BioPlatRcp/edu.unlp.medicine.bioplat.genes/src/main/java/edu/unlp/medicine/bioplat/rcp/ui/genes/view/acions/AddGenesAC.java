package edu.unlp.medicine.bioplat.rcp.ui.genes.view.acions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.unlp.medicine.bioplat.rcp.ui.entities.editors.contributors.AbstractActionContribution;
import edu.unlp.medicine.bioplat.rcp.ui.genes.view.dialogs.GenesInputDialog;
import edu.unlp.medicine.bioplat.rcp.ui.views.messages.Message;
import edu.unlp.medicine.bioplat.rcp.ui.views.messages.MessageManager;
import edu.unlp.medicine.bioplat.rcp.utils.GUIUtils;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.domainLogic.framework.MetaPlat;
import edu.unlp.medicine.entity.biomarker.Biomarker;
import edu.unlp.medicine.entity.gene.Gene;

public class AddGenesAC extends AbstractActionContribution<Biomarker> {
	private static Logger logger = LoggerFactory.getLogger(AddGenesAC.class);
	@Override
	public void run() {

		PlatformUIUtils.findDisplay().syncExec(new Runnable() {

			@Override
			public void run() {
				MessageManager mm = MessageManager.INSTANCE;

				Biomarker b = getActiveBiomarker();
				if (b == null) {
					mm.add(Message.warn("There is no gene signature selected"));
					return;
				}

				
				List<String> genesAddedList=new ArrayList<String>(), genesAlreadyInList=new ArrayList<String>(), genesNotFoundList = new ArrayList<String>();
				
				GenesInputDialog mydialog = new GenesInputDialog(PlatformUIUtils.findShell());
				if (mydialog.open() == Dialog.OK)
					for (String id : mydialog.getids()) {
						try {
							final Gene gene = MetaPlat.getInstance().findGene(id);
							if (!b.getGenes().contains(gene)) {
								b.addGene(gene);
								genesAddedList.add(id);
								//mm.add(Message.info("Gene " + gene + " added to " + b.getName() + "."));
							} else
								genesAlreadyInList.add(id);
								//mm.add(Message.warn("The gene " + gene + " is already in the gene signature"));
						} catch (Exception e) {
							genesNotFoundList.add(id);
							//mm.add(Message.error("Couldnt add the gene with id '" + id + "'", e));
						}
					}
					
					
					if (genesAddedList.size()>0){
						String geneList= GUIUtils.getGeneListAsString(genesAddedList);
						mm.add(Message.info("Genes added to the Gene Signature(" + genesAddedList.size() + "): " + geneList));
						//logger.info("Genes added: " + geneList);
					}
					if (genesAlreadyInList.size()>0){ 
						String geneAlreadyInList= GUIUtils.getGeneListAsString(genesAlreadyInList);
						mm.add(Message.warn("Genes already in the Gene Signature; they were discarded (" + genesAlreadyInList.size() + "): " + geneAlreadyInList));
						//logger.info("Genes alreadt in GS: " + geneAlreadyInList);
					}
						if (genesNotFoundList.size()>0){
							String genesNotFound= GUIUtils.getGeneListAsString(genesNotFoundList);
							mm.add(Message.warn("Genes not added because they were not found in Bioplat database(" +  genesNotFoundList.size() + "). Remember you can use GeneSymbol, EntrezId or EnsmblID for identify genes: " +  genesNotFound));
							//logger.info("Genes not  found: " + genesNotFound);	
						}

			}

			
		});

	}

	private Biomarker getActiveBiomarker() {
		return model();
	}

}
