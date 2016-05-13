package scivisum.pycharmplugins.pyregex;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.ui.Messages;
/**
 * Created by christian on 05/05/16.
 */
public class PyRegexPlugin implements ApplicationComponent{

    public PyRegexPlugin() {
        super();
    }


    public void initComponent() {
    }
    public void apply() {
        if (0 == Messages.showYesNoDialog(DIALOG_MSG, "Warning!", null)) {
            ApplicationManager.getApplication().exit();
        }
    }

    public void reset() {
    }

    /**
     * This method is called on plugin disposal.
     */
    public void disposeComponent() {
    }

    public String getComponentName() {
        return "PyRegexPlugin";
    }

    private static String DIALOG_MSG =
            "IDEA must be restarted for changes to take" +
                    "\neffect. Would you like to shutdown IDEA?\n";
}
