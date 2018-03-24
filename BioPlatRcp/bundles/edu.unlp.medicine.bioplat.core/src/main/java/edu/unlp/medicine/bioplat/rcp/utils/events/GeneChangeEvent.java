package edu.unlp.medicine.bioplat.rcp.utils.events;

import edu.unlp.medicine.entity.gene.Gene;

public class GeneChangeEvent {

	private Gene gene;
	public GeneChangeEvent(Gene gene) {
		this.gene = gene;
	}
	
	public Gene gene() {return gene;}
}
