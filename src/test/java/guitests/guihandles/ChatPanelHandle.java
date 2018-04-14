package guitests.guihandles;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * A handler for the {@code ChatPanel} of the UI
 */
public class ChatPanelHandle extends NodeHandle<ScrollPane> {

    public static final String CHAT_PANEL_ID = "#chatPanel";

    public ChatPanelHandle(ScrollPane chatPanelNode) {
        super(chatPanelNode);
    }

    /**
     * Returns the last result text in the chat panel.
     */
    public String getText() {
        VBox chatPanel = (VBox) getRootNode().getContent();
        ObservableList<Node> messageContainers = chatPanel.getChildrenUnmodifiable();
        HBox lastResultMessageHBox = (HBox) messageContainers.get(messageContainers.size() - 1);
        Label lastResultLabel = (Label) lastResultMessageHBox.getChildren().get(0);
        return lastResultLabel.getText();
    }
}
