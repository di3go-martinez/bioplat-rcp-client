package edu.unlp.medicine.bioplat.rcp.ui.biomarker.editors;

import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.ui.IEditorInput;

import edu.unlp.medicine.bioplat.rcp.editor.EditorDescription;
import edu.unlp.medicine.bioplat.rcp.ui.utils.preferences.ExternalURLInformationPage;
import edu.unlp.medicine.bioplat.rcp.ui.views.messages.Message;
import edu.unlp.medicine.bioplat.rcp.ui.views.messages.MessageManager;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUtils;

/**
 * 
 * Crea los browser editors configurados para la entidad
 * 
 * @author diego martínez
 * 
 */
public abstract class BrowserEditorManager {

	private String key;

	public BrowserEditorManager(String key) {
		this.key = key;
	}

	public void createEditorBrowsers(List<EditorDescription> result, final IEditorInput input) {
		IEclipsePreferences pref = PlatformUtils.preferences();

		Boolean apply = pref.getBoolean(key(), false);
		if (apply) {
			String urls = pref.get(ExternalURLInformationPage.EXTERNAL_URLS, "");
			
			StringTokenizer st = new StringTokenizer(urls, ExternalURLInformationPage.SEPARATOR);
			while (st.hasMoreElements()) {
				EditorDescription ed = createBrowserEditor(input, st.nextElement().toString());
				if (ed != null)
					result.add(ed);
			}
		}
	}

	/**
	 * Clave de la preferencia que indica si se agregan o no los browsers
	 * configurados
	 * 
	 * @return
	 */
	protected String key() {
		return key;
	}

	private EditorDescription createBrowserEditor(IEditorInput input, final String url) {

		String title = "";
		final String[] s = StringUtils.splitByWholeSeparator(url, "::");

		if (s.length < 2) {
			final String msg = "The URL '" + url + "' is not valid";
			MessageManager.INSTANCE.add(Message.error(msg));
			return null;
		}

		try {
			title = s[0];
		} catch (Exception e) {
			MessageManager.INSTANCE.add(Message.error("Error trying to parse the url +" + url));
		}

		return new EditorDescription(input, new BrowserEditor() {

			@Override
			protected String resolveUrl() {
				s[1]= s[1].replace("{allGenes}", getAllGenesAsList());
				return s[1].replace("{allGenesWithPipes}", getAllGenesAsListWithPipes());
			}

		}, title);
	}

	/**
	 * @return todos los entrezId de genes separados por ','
	 */
	protected abstract String getAllGenesAsList();
	
	/**
	 * @return todos los entrezId de genes separados por ','
	 */
	protected abstract String getAllGenesAsListWithPipes();

}
