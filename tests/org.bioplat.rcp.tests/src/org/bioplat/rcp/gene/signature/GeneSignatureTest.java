package org.bioplat.rcp.gene.signature;

import static org.junit.Assert.assertTrue;

import org.bioplat.rcp.conditions.Conditions;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotCTabItem;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import edu.unlp.medicine.bioplat.rcp.ui.genes.view.GeneViewPart;

@RunWith(SWTBotJunit4ClassRunner.class)
public class GeneSignatureTest {

	private static SWTWorkbenchBot bot;

	@BeforeClass
	public static void beforeClass() throws Exception {
		bot = new SWTWorkbenchBot();
		bot.waitUntil(Conditions.initialized());
	}

	@Test
	public void create_gene_signature() {

		bot.menu("Start").menu("Gene Signatures/Datasets").click();
		bot.tree().getTreeItem("Gene Signature").expand();
		bot.tree().getTreeItem("Gene Signature").getNode("Create a gene signature").select();

		bot.button("Next >").click();

		final String gsId = "Gene Signature Test";

		bot.text("NoName").setText(gsId);

		bot.button("Finish").click();

		// funciona como assert...
		SWTBotEditor geneSignatureEditor = bot.editorByTitle(gsId);
		geneSignatureEditor.show();

		assertTrue(bot.table().rowCount() == 0);
		bot.menu("Operations").menu("Add Genes to Gene Signature...").click();

		bot.text(1).setText("tp53");
		bot.button("OK").click();

		geneSignatureEditor.getWidget();

		assertTrue(bot.table().rowCount() == 1);

		bot.table().getTableItem(0).select();

		SWTBotView genesView = bot.viewById(GeneViewPart.id());

		genesView.show();
		// el título es por el gene agregado más arriba
		SWTBotCTabItem cTabItem = bot.cTabItem("Gen:TP53(7157)");
		cTabItem.activate();
		SWTBotCTabItem header = bot.cTabItem("Header");
		header.activate();

		//El Header tiene un campo de solo lectura con el nombre del gen
		assertTrue(bot.text("TP53").isReadOnly());

	}

	

}