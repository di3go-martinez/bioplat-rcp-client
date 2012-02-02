package edu.unlp.medicine.bioplat.rcp.ui.biomarker.editors.nls;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = Messages.class.getPackage().getName() + ".messages"; //$NON-NLS-1$

	public static String BiomarkerEditor_description_label;
	public static String BiomarkerEditor_author_label;
	public static String BiomarkerEditor_original_gene_count_label;
	public static String BiomarkerEditor_gene_count_label;
	public static String BiomarkerEditor_name_label;
	public static String BiomarkerEditor_significance_value;

	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
