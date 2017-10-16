package edu.unlp.medicine.bioplat.rcp.ui.entities.editors.contributors;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IContributionManager;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.part.EditorActionBarContributor;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;

import edu.unlp.medicine.bioplat.core.Activator;
import edu.unlp.medicine.bioplat.rcp.editor.ModelProvider;
import edu.unlp.medicine.bioplat.rcp.ui.entities.actions.MessageViewOpenAction;
import edu.unlp.medicine.bioplat.rcp.ui.utils.extension.points.DefaultExtensionLoaderRunnable;
import edu.unlp.medicine.bioplat.rcp.ui.utils.extension.points.ExtensionPointLoader;

// TODO Ver si se puede usar para el selectionType por ejemplo
// TODO agregar configuración en populateMenu, por ejemplo, para agregar una operación al toolbar,
// al menu (por default)
// TODO agregar método para permitir la ejecución en el threadui... ?¿
// TODO agregado de otras opciones para las acciones, como por ejemplo imágenes

//TODO refactor de AbstractEditorActionBarContributor, probar y reemplazarlo
public abstract class AbstractEditorActionBarContributornuevo<T> extends EditorActionBarContributor
    implements ModelProvider<T> {

  private List<ActionContribution2<T>> actions = Lists.newArrayList();

  public AbstractEditorActionBarContributornuevo() {
    actions.addAll(readActionsByExtensionPoint());
  }

  /**
   * Se agregan las operaciones a realizar sobre una entidad al menu
   * 
   * @deprecated usar extension point para agregar las acciones al menú
   * @param menu
   */
  @Deprecated
  protected abstract void populateMenu(IMenuManager menu);

  @Override
  public void contributeToMenu(IMenuManager menuManager) {
    MenuManager mm = createMenuManager();
    populateMenu0(mm);
    contribute(menuManager, mm);
  }

  private void populateMenu0(MenuManager mm) {
    populateMenu(mm);
    populateGrouped(mm, new Predicate<ActionContribution2<?>>() {

      @Override
      public boolean apply(ActionContribution2<?> input) {
        return input.onMenu();
      }
    });
  }

  private void populateGrouped(IContributionManager mm,
      Predicate<ActionContribution2<?>> predicate) {
    boolean atLeastOnce = false;
    Map<String, List<ActionContribution2<T>>> groupedActions = groupActions(actions);
    for (String groupKey : ordered(groupedActions.keySet())) {
      atLeastOnce = false;
      for (ActionContribution2<T> actionc : groupedActions.get(groupKey)) {
        if (predicate.apply(actionc)) {
          add(mm, actionc);
          atLeastOnce = true;
        }
      }

      if (atLeastOnce)
        mm.add(new Separator());
    }
  }

  private List<String> ordered(Set<String> set) {
    Ordering<String> ordening = Ordering.natural().nullsLast();
    return ordening.sortedCopy(set);
  }

  /**
   * Agrupa las acciones en función del id del grupo
   * 
   * @param ungroupedActions
   * @return
   */
  private Map<String, List<ActionContribution2<T>>> groupActions(
      List<ActionContribution2<T>> ungroupedActions) {
    Map<String, List<ActionContribution2<T>>> result =
        new HashMap<String, List<ActionContribution2<T>>>();
    for (ActionContribution2<T> actionc : ungroupedActions) {
      List<ActionContribution2<T>> l = result.get(actionc.group()); // FIXME
                                                                   // puede
                                                                   // devolver
                                                                   // null
      if (l == null)
        l = Lists.newArrayList();
      l.add(actionc);
      result.put(actionc.group(), l);

    }
    return result;
  }

  private void add(IContributionManager mm, ActionContribution2<T> actionc) {
    mm.add(wrap(actionc.action()));
  }

  @Override
  public void contributeToCoolBar(ICoolBarManager coolBarManager) {
    ToolBarManager tbm = new ToolBarManager();
    populateGrouped(tbm, new Predicate<ActionContribution2<?>>() {

      @Override
      public boolean apply(ActionContribution2<?> input) {
        return input.onToolbar();
      }
    });
    coolBarManager.add(tbm);
  }

  /**
   * Lee los extension points que se hayan registrado y los procesa
   */
  private List<ActionContribution2<T>> readActionsByExtensionPoint() {
    final List<ActionContribution2<T>> actions = Lists.newArrayList();
    ExtensionPointLoader.create("edu.medicine.bioplat.rcp.editor.operation.contribution")
        .load(new DefaultExtensionLoaderRunnable() {
          @Override
          protected void runOn(IConfigurationElement configurationElement) throws Exception {
            if (shouldAdd(configurationElement))
              actions.add(createAndConfigureContribution(configurationElement));
          }

          private boolean shouldAdd(IConfigurationElement celement) throws ClassNotFoundException {
            // tiene 1 o ningún elemento. es opcional, en caso de no estar seteada va para cualquier
            // entidad
            IConfigurationElement[] selectionHolder = celement.getChildren("selection");
            String selectionClassName = (ok(selectionHolder) ? extractClassName(selectionHolder)
                : defaultSelectionClassName());
            final Class<?> configuredSelectionClass = Class.forName(selectionClassName);
            return configuredSelectionClass.isAssignableFrom(getSelectionType());
          }

          private String extractClassName(IConfigurationElement[] selectionHolder) {
            return selectionHolder[0].getAttribute("class");
          }

          private boolean ok(IConfigurationElement[] selectionHolder) {
            return selectionHolder.length != 0;
          }

          private String defaultSelectionClassName() {
            return Object.class.getName();
          }

          private ActionContribution2<T> createAndConfigureContribution(
              IConfigurationElement celement) throws CoreException {
            // TODO evitar warning
            return ((ActionContribution2<T>) celement.createExecutableExtension("class")) 
                .modelProvider(AbstractEditorActionBarContributornuevo.this)
                .caption(celement.getAttribute("caption"))
                .image(Activator.imageDescriptorFromPlugin(celement.getAttribute("image")))
                .onMenu(Boolean.valueOf(celement.getAttribute("onMenu")))
                .onToolbar(Boolean.valueOf(celement.getAttribute("onToolbar")))
                .group(celement.getAttribute("groupId"));
          }

        });
    return actions;
  }

  private void contribute(IMenuManager menuManager, IContributionItem item) {
    menuManager.prependToGroup("entidad.additions", item);
  }

  protected Class<?> getSelectionType() {
    return Object.class; // Default
  }


  private MenuManager createMenuManager() {
    return new MenuManager("Operations");
  }

  /**
   * Devuelve el model para cuyo editor se contribuye
   * 
   * @return
   */
  @Override
  public T model() {
    final IEditorPart activeEditor = getPage().getActiveEditor();
    Assert.isNotNull(activeEditor, "Early attempt to access to editor. It is not created yet.");
    return ((ModelProvider<T>) activeEditor).model();
  }

  private static IAction wrap(IAction action) {
    action = AsyncAction.wrap(action);
    action = MessageViewOpenAction.wrap(action);
    return action;
  }
}
