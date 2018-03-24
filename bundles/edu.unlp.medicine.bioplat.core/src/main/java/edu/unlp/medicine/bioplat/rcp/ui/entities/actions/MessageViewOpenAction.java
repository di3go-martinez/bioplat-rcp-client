package edu.unlp.medicine.bioplat.rcp.ui.entities.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.swt.events.HelpListener;
import org.eclipse.swt.widgets.Event;

import edu.unlp.medicine.bioplat.rcp.ui.views.messages.MessageManager;
import edu.unlp.medicine.bioplat.rcp.ui.views.messages.MessageViewPart;
import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;

/**
 * 
 * Esta acción ejecutar la acción configurada como target, y cuando esta termina
 * abrir la vista de mansajes si hay mensajes a mostar
 * 
 * @author diego martínez
 * 
 */
public class MessageViewOpenAction extends Action {

	private IAction target;

	@Override
	public void addPropertyChangeListener(IPropertyChangeListener listener) {
		target.addPropertyChangeListener(listener);
	}

	@Override
	public int getAccelerator() {
		return target.getAccelerator();
	}

	@Override
	public String getActionDefinitionId() {
		return target.getActionDefinitionId();
	}

	@Override
	public String getDescription() {
		return target.getDescription();
	}

	@Override
	public ImageDescriptor getDisabledImageDescriptor() {
		return target.getDisabledImageDescriptor();
	}

	@Override
	public HelpListener getHelpListener() {
		return target.getHelpListener();
	}

	@Override
	public ImageDescriptor getHoverImageDescriptor() {
		return target.getHoverImageDescriptor();
	}

	@Override
	public String getId() {
		return target.getId();
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return target.getImageDescriptor();
	}

	@Override
	public IMenuCreator getMenuCreator() {
		return target.getMenuCreator();
	}

	@Override
	public int getStyle() {
		return target.getStyle();
	}

	@Override
	public String getText() {
		return target.getText();
	}

	@Override
	public String getToolTipText() {
		return target.getToolTipText();
	}

	@Override
	public boolean isChecked() {
		return target.isChecked();
	}

	@Override
	public boolean isEnabled() {
		return target.isEnabled();
	}

	@Override
	public boolean isHandled() {
		return target.isHandled();
	}

	@Override
	public void removePropertyChangeListener(IPropertyChangeListener listener) {
		target.removePropertyChangeListener(listener);
	}

	@Override
	public void setActionDefinitionId(String id) {
		target.setActionDefinitionId(id);
	}

	@Override
	public void setChecked(boolean checked) {
		target.setChecked(checked);
	}

	@Override
	public void setDescription(String text) {
		target.setDescription(text);
	}

	@Override
	public void setDisabledImageDescriptor(ImageDescriptor newImage) {
		target.setDisabledImageDescriptor(newImage);
	}

	@Override
	public void setEnabled(boolean enabled) {
		target.setEnabled(enabled);
	}

	@Override
	public void setHelpListener(HelpListener listener) {
		target.setHelpListener(listener);
	}

	@Override
	public void setHoverImageDescriptor(ImageDescriptor newImage) {
		target.setHoverImageDescriptor(newImage);
	}

	@Override
	public void setId(String id) {
		target.setId(id);
	}

	@Override
	public void setImageDescriptor(ImageDescriptor newImage) {
		target.setImageDescriptor(newImage);
	}

	@Override
	public void setMenuCreator(IMenuCreator creator) {
		target.setMenuCreator(creator);
	}

	@Override
	public void setText(String text) {
		target.setText(text);
	}

	@Override
	public void setToolTipText(String text) {
		target.setToolTipText(text);
	}

	@Override
	public void setAccelerator(int keycode) {
		target.setAccelerator(keycode);
	}

	public MessageViewOpenAction(IAction action) {
		target = action;
	}

	@Override
	public void run() {
		run0(null);
	}

	private void run0(Event event) {
		try {
			beforeRun();
			if (event == null)
				target.run();
			else
				target.runWithEvent(event);
		} finally {
			afterRun();
		}
	}

	@Override
	public void runWithEvent(Event event) {
		run0(event);
	}

	private void afterRun() {
		if (MessageManager.INSTANCE.isThereAnyMessage())
			PlatformUIUtils.openView(MessageViewPart.id());
	}

	private void beforeRun() {
		// Ahora no se borran
		// MessageManager.INSTANCE.clear();
	}

	public static IAction wrap(IAction action) {
		return new MessageViewOpenAction(action);
	}

}
