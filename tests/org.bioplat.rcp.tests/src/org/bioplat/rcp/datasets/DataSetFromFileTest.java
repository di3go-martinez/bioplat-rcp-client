package org.bioplat.rcp.datasets;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.nio.file.Paths;

import org.bioplat.rcp.conditions.Conditions;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.unlp.medicine.bioplat.rcp.ui.views.messages.MessageViewPart;

/**
 * 
 * depende de bioplatmodel beta.13+
 */
public class DataSetFromFileTest {

	private static SWTWorkbenchBot bot;

	@BeforeClass
	public static void beforeClass() throws Exception {
		bot = new SWTWorkbenchBot();
		bot.waitUntil(Conditions.initialized());
	}
	
	@Test
	public void open_dataset_editor() {
		bot.menu("Start").menu("Gene Signatures/Datasets").click();
		bot.tree().getTreeItem("Dataset").expand();
		bot.tree().getTreeItem("Dataset").getNode("Import from TXT GEO file").select();
		bot.button("Next >").click();
		bot.button("Next >").click();
		File dataset = Paths.get("resources/datasets/test-dataset.csv").toFile();
		String filepath = dataset.getAbsolutePath();
		
		assertTrue(dataset.exists());
		bot.text().setText(filepath);
		bot.button("Finish").click();
		bot.viewById(MessageViewPart.id()).close();
		
		bot.editorByTitle(filepath).show();
		bot.text().setText("Test Dataset");
		
		
	}
}
