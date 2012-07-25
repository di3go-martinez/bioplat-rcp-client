package edu.unlp.medicine.bioplat.rcp.utils;

public class RLibrary {
	private String name;
	private String installation;

	public RLibrary(final String name, final String instalation) {
		this.setInstallation(instalation);
		this.setName(name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getInstallation() {
		return installation;
	}

	public void setInstallation(String installation) {
		this.installation = installation;
	}

}
