package edu.unlp.medicine.bioplat.rcp.ui.genes.view.dialogs;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.google.common.collect.Lists;

import edu.unlp.medicine.bioplat.rcp.ui.views.messages.Message;
import edu.unlp.medicine.bioplat.rcp.ui.views.messages.MessageManager;
import edu.unlp.medicine.bioplat.rcp.utils.GUIUtils;
import edu.unlp.medicine.bioplat.rcp.utils.Holder;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.domainLogic.framework.MetaPlat;
import edu.unlp.medicine.domainLogic.framework.exceptions.GeneNotFoundByIdException;
import edu.unlp.medicine.entity.gene.Gene;

public class BiomarkerFromBiomarkerInputDialog extends Dialog {

	private String separator = " ";

	public BiomarkerFromBiomarkerInputDialog(Shell parentShell) {
		super(parentShell);
		setBlockOnOpen(true);
		setShellStyle(SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
	}

	public BiomarkerFromBiomarkerInputDialog() {
		this(PlatformUIUtils.findShell());
	}

	/**
	 * Setea los genes como si fueran ingresados
	 * 
	 * @param genes
	 * @return
	 */
	public BiomarkerFromBiomarkerInputDialog genes(List<Gene> genes) {
		StringBuilder $value = new StringBuilder();
		for (Gene gene : genes) {
			$value.append(gene.getEntrezId() + "\n");
		}
		setValue($value.toString());
		return this;
	}

	private void setValue(String _value) {
		value = _value;
	}

	public List<Gene> genes() {
		List<Gene> result = Lists.newArrayList();
		List<String> genesNotGenes=new ArrayList<String>();
		for (String id : getids())
			try {
				result.add(MetaPlat.getInstance().findGene(id));
			} catch (GeneNotFoundByIdException e) {
				genesNotGenes.add(id);
			} catch (Exception e) {
				MessageManager.INSTANCE.add(Message.error(e.getLocalizedMessage()));
			}
		if (genesNotGenes.size()>0) MessageManager.INSTANCE.add(Message.warn("Entered IDs not found in Bioplat database (Take into account that you can use GeneSymbol, EntrezId or EnsmblID for identify genes): " + GUIUtils.getGeneListAsString(genesNotGenes)));
		return result;
	}
	
	public String[] getids() {
		return StringUtils.split(value, separator);
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Paste genes (EntrezID, EnsemblID or Gene name, separated by blank or enter)");

	}

	@Override
	protected Point getInitialSize() {
		return new Point(550, 400);
	}

	private String value = "";

	/*private String name = "noName";
	public String getName() {
		return name;
	}

	private String author = "";
	public String getAuthor() {
		return author;
	}

	private String description = "";
	public String getDescription() {
		return description;
	}*/


	@Override
	protected Control createDialogArea(Composite parent) {
		Composite result = (Composite) super.createDialogArea(parent);
		
		/*GridData gridData = new GridData();
		gridData.horizontalAlignment=SWT.FILL;
		gridData.grabExcessHorizontalSpace=true;
		
		new CLabel(result, SWT.BOLD).setText("Gene signature name:");
		Text nameHolder = new Text(result, SWT.BORDER);
		nameHolder.setLayoutData(gridData);
		nameHolder.setText(name);
		
		new CLabel(result, SWT.BOLD).setText("Gene signature author:");
		Text authorHolder = new Text(result, SWT.BORDER);
		authorHolder.setLayoutData(gridData);
		authorHolder.setText(author);
		
		new CLabel(result, SWT.BOLD).setText("Gene signature description:");
		Text descriptionHolder = new Text(result, SWT.BORDER);
		descriptionHolder.setLayoutData(gridData);
		descriptionHolder.setText(description);*/
		
		new CLabel(result, SWT.BOLD).setText("Genes:");
		final Text text = new Text(result, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
		text.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());
		text.setToolTipText(getShell().getText());
		text.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				value = StringUtils.replace(text.getText(), "\n", " ");
				value = StringUtils.replace(value, "\r", " ").replace("\t", " ");
			}
		});
		text.setText(value);
		return result;
	}

	/**
	 * Abre el diálogo y retorna si este fue aceptado o no, true o false
	 * respectivamente
	 * 
	 * @return
	 */
	public boolean accepted() {
		final Holder<Boolean> result = Holder.create();
		PlatformUIUtils.findDisplay().syncExec(new Runnable() {

			@Override
			public void run() {
				result.hold(open() == Dialog.OK);
			}
		});
		return result.value();
	}

}
