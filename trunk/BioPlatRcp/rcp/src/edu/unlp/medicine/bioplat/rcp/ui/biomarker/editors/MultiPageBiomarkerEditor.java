package edu.unlp.medicine.bioplat.rcp.ui.biomarker.editors;

import java.util.List;

import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPropertyListener;

import com.google.common.collect.Lists;

import edu.unlp.medicine.bioplat.rcp.editor.AbstractFormEditor;
import edu.unlp.medicine.bioplat.rcp.editor.EditorDescription;
import edu.unlp.medicine.bioplat.rcp.ui.utils.preferences.ExternalURLInformationPage;
import edu.unlp.medicine.bioplat.rcp.utils.EditorInputFactory;
import edu.unlp.medicine.entity.biomarker.Biomarker;

@SuppressWarnings("unchecked")
public class MultiPageBiomarkerEditor extends AbstractFormEditor<Biomarker> {

	public static String id() {
		return "edu.unlp.medicine.biomarker.editor.multipage";
	}

	@Override
	protected List<EditorDescription> createEditors() {
		List<EditorDescription> result = Lists.newArrayList();

		final IEditorInput input = EditorInputFactory.createDefaultEditorInput(model());
		final BiomarkerEditor editor = new BiomarkerEditor(false);
		editor.addPropertyListener(new IPropertyListener() {

			@Override
			public void propertyChanged(Object arg0, int arg1) {
				// TODO revisar que siempre es un biomarker editor el arg0
				if (arg0 instanceof BiomarkerEditor)
					setPartName(((BiomarkerEditor) arg0).model().id());
			}
		});
		result.add(new EditorDescription(input, editor, "General"));		
		result.add(new EditorDescription(input, new SurvCompBiomarkerExperimentsEditor(false), "Concordance index validations"));
		result.add(new EditorDescription(input, new LogRankTestBiomarkerExperimentsEditor(false), "Kaplan-Meier and Log-Rank validations"));
		result.add(new EditorDescription(input, new BiomarkerExperimentsEditor(false), "Validations"));
		

		// Estaría configurado en las preferences ahora
		// result.add(new EditorDescription(input, new BrowserEditor() {
		// @Override
		// protected String resolveUrl() {
		// return model().getDavidURLForFunctionalAnalysis();
		// }
		// }, "David"));

		new BrowserEditorManager(ExternalURLInformationPage.APPLY_TO_GENE_SIGNATURE) {
			@Override
			protected String getAllGenesAsList() {
				return model().getGenesAsList();
			}
		}.createEditorBrowsers(result, input);

		return result;
	}

}
