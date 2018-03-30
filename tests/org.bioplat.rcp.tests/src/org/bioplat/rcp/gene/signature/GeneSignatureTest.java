package org.bioplat.rcp.gene.signature;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.waits.ICondition;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import edu.unlp.medicine.domainLogic.framework.MetaPlat;

@RunWith(SWTBotJunit4ClassRunner.class)
public class GeneSignatureTest {

	private static SWTWorkbenchBot bot;

	@BeforeClass
	public static void beforeClass() throws Exception {
		bot = new SWTWorkbenchBot();
		bot.waitUntil(initialized());

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

		//funciona como assert...
		bot.editorByTitle(gsId).show();

		bot.menu("Operations").menu("Add Genes to Gene Signature...").click();

		bot.text(1).setText("tp53");
		bot.button("OK").click();

	}

	private static ICondition initialized() {
		return new ICondition() {

			@Override
			public boolean test() throws Exception {
				MetaPlat.getInstance();
				return true;
			}

			@Override
			public void init(SWTBot bot) {
				// TODO Auto-generated method stub
			}

			@Override
			public String getFailureMessage() {
				return "No se pudo inicializar correctamente la app";
			}
		};
	}

}