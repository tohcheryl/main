package guitests.guihandles;

import javafx.stage.Stage;

/**
 * Provides a handle for {@code MainWindow}.
 */
public class MainWindowHandle extends StageHandle {

    private final FoodListPanelHandle foodListPanel;
    private final ChatPanelHandle chatPanel;
    private final CommandBoxHandle commandBox;
    private final StatusBarFooterHandle statusBarFooter;
    private final MainMenuHandle mainMenu;

    public MainWindowHandle(Stage stage) {
        super(stage);

        foodListPanel = new FoodListPanelHandle(getChildNode(FoodListPanelHandle.FOOD_LIST_VIEW_ID));
        chatPanel = new ChatPanelHandle(getChildNode(ChatPanelHandle.CHAT_PANEL_ID));
        commandBox = new CommandBoxHandle(getChildNode(CommandBoxHandle.COMMAND_INPUT_FIELD_ID));
        statusBarFooter = new StatusBarFooterHandle(getChildNode(StatusBarFooterHandle.STATUS_BAR_PLACEHOLDER));
        mainMenu = new MainMenuHandle(getChildNode(MainMenuHandle.MENU_BAR_ID));
    }

    public FoodListPanelHandle getFoodListPanel() {
        return foodListPanel;
    }

    public ChatPanelHandle getChatPanelHandle() {
        return chatPanel;
    }

    public CommandBoxHandle getCommandBox() {
        return commandBox;
    }

    public StatusBarFooterHandle getStatusBarFooter() {
        return statusBarFooter;
    }

    public MainMenuHandle getMainMenu() {
        return mainMenu;
    }
}
