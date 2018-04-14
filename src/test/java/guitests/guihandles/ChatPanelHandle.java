package guitests.guihandles;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

//@@author jaxony
/**
 * A handler for the {@code ChatPanel} of the UI
 */
public class ChatPanelHandle extends NodeHandle<VBox> {

    public static final String CHAT_PANEL_ID = "#chatPanel";

    public ChatPanelHandle(VBox chatPanelNode) {
        super(chatPanelNode);
    }

    /**
     * Returns the last result text in the chat panel.
     */
    public String getText() {
        ObservableList<Node> messageContainers = getRootNode().getChildrenUnmodifiable();
        int numResults = messageContainers.size();
        if (numResults == 0) {
            return null;
        }
        HBox lastResultMessageHBox = (HBox) messageContainers.get(messageContainers.size() - 1);
        Label lastResultLabel = (Label) lastResultMessageHBox.getChildren().get(0);
        return lastResultLabel.getText();
    }
}
