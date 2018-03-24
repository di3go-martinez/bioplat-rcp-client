package edu.unlp.medicine.bioplat.rcp.ui.experiment.actions.contributions.split;

public class SplitterSetupResult {
	
	
	private boolean isOk ;
	private String message = "";

	private SplitterSetupResult(boolean isOk, String message) {
		this.isOk = isOk;
		this.message = message;
	}
	
	
	
	boolean isOk() {
		return isOk;
	}
	
	String message() {
		return this.message ;
	}



	public static SplitterSetupResult fail(String cause) {
		
		return new SplitterSetupResult(false, cause);
	}



	public static SplitterSetupResult ok() {
		return new SplitterSetupResult(true, "Partition is OK");
	}
	
}
