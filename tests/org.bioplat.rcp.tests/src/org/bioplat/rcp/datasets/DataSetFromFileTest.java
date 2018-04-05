package org.bioplat.rcp.datasets;

import java.nio.file.Paths;

import org.bioplat.rcp.conditions.Conditions;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 * 
 * depende de bioplatmodel beta.13+
 */
@Ignore
public class DataSetFromFileTest {

	private static SWTWorkbenchBot bot;

	@BeforeClass
	public static void beforeClass() throws Exception {
		bot = new SWTWorkbenchBot();
		bot.waitUntil(Conditions.initialized());
	}
	
	@Test
	public void open() {
		bot.toolbarButtonWithTooltip("New (Ctrl+N)").click();
		bot.tree().getTreeItem("Dataset").expand();
		bot.tree().getTreeItem("Dataset").getNode("Import from TXT GEO file").select();
		bot.button("Next >").click();
		bot.button("Next >").click();
		String filepath = Paths.get("resources/test-dataset.csv").toFile().getAbsolutePath(); 
		bot.text().setText(filepath);
		bot.button("Next >").click();
		bot.button("Finish").click();
		
		bot.editorByTitle(filepath).show();
		bot.text().setText("Test Dataset");
		
		
	}
}
