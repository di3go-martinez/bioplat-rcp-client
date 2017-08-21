package edu.unlp.medicine.bioplat.core;

/**
 * 
 * 
 * Contiene los parámetros de la aplicación (<b>no los de sistema, que deberían
 * ser migrados a esta manera</b>)
 * 
 * Cuando la aplicación inicia, llama a <code>initialize</code>
 *
 * @see #initialize(String[])
 * @see Application#start
 */
public enum ApplicationParametersHolder {

	parameters;

	
	/**
	 * 
	 * @param commandLineArgs
	 * Formato libre, se procesa segun la implementación
	 * <br>
	 * ejemplo: <br> 
	 * 		--flag --param param-value --> { "--flag", "--param", "param-value"]
	 * 		
	 */
	public static void initialize(String[] commandLineArgs) {
		String[] args = commandLineArgs;

		int i = 0;
		while (i < args.length) {
			if (args[i].equals("--multiomics-url"))
				parameters.setMultiomicsUrl(args[++i]);
			i++;
		}
	}

	private String multiomicsUrl;

	private void setMultiomicsUrl(String url) {
		this.multiomicsUrl = url;
	}

	public String multiomicsUrl() {
		return multiomicsUrl;
	}
}
