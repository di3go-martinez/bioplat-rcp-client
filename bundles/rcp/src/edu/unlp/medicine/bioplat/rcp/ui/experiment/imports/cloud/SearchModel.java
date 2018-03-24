package edu.unlp.medicine.bioplat.rcp.ui.experiment.imports.cloud;

import java.util.Set;

import edu.unlp.medicine.entity.generic.AbstractEntity;
import zinbig.bioplatcloud.api.dto.DatasetDTO;

public class SearchModel extends AbstractEntity {

	private String value = "";
	private String tagValue="";

	public void setKey(String value) {
		String old = this.value;
		this.value = value;
		fpc("key", old, value);
	}

	public String getKey() {
		return value.trim();
	}

	public void setTag(String value) {
		String old = this.tagValue;
		this.tagValue = value;
		fpc("tag", old, value);
	}
	
	public String getTag() {
		return tagValue.trim();
	}

	public boolean byName() {
		return !getKey().isEmpty();
	}
	
	public boolean byTag() {
		return !getTag().isEmpty();
	}

}
