import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.wm.WindowManager;
import scivisum.pycharmplugins.pyregex.ui.RegexPanel;

/**
 * Created by christian on 05/05/16.
 */
public class OpenRegexTestWindow extends AnAction {
    public OpenRegexTestWindow(){
        super("_Regex Tester");
    }

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        Project project = anActionEvent.getData(PlatformDataKeys.PROJECT);
        RegexPanel regexPanel = new RegexPanel(WindowManager.getInstance().suggestParentWindow(project));
        regexPanel.centerOnParent();
    }

    @Override
    public void update(AnActionEvent anActionEvent) {
        anActionEvent.getPresentation().setIcon(IconLoader.getIcon("images/dotstar.jpg"));
    }
}
