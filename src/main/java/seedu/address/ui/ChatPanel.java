package seedu.address.ui;

import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.ui.NewResultAvailableEvent;
import seedu.address.commons.events.ui.NewUserMessageAvailableEvent;

/**
 * Panel containing the chat history.
 */
public class ChatPanel extends UiPart<Region> {
    private static final int SPACING = 10;
    private static final String FXML = "ChatPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(ChatPanel.class);

    @FXML
    private ScrollPane chatScrollPane;

    @FXML
    private VBox chatPanel;

    public ChatPanel() {
        super(FXML);
        chatScrollPane.setContent(chatPanel);
        chatScrollPane.setFitToWidth(true);
        chatScrollPane.vvalueProperty().bind(chatPanel.heightProperty());
        chatPanel.setSpacing(SPACING);
        registerAsAnEventHandler(this);
    }


    @Subscribe
    private void handleNewResultAvailableEvent(NewResultAvailableEvent event) {
        chatPanel.getChildren().add(new Label(event.message));
    }

    @Subscribe
    private void handleNewUserMessageEvent(NewUserMessageAvailableEvent event) {
        chatPanel.getChildren().add(new Label(event.message));
    }

}
