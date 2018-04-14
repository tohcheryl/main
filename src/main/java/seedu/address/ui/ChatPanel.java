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
    private static final String RESULT_ERROR_STYLE = "result-error";
    private static final String RESULT_SUCCESS_STYLE = "result-success";
    private static final String USER_LABEL_STYLE = "user-label";
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

    /**
     * Creates a label for a result message.
     *
     * @param message      String message from system for user feedback.
     * @param isSuccessful If the message represents a successful one.
     * @return A new javafx Label object.
     */
    private Label createResultMessageLabel(String message, boolean isSuccessful) {
        Label label = new Label(message);
        label.setWrapText(true);
        if (isSuccessful) {
            label.getStyleClass().add(RESULT_SUCCESS_STYLE);
        } else {
            label.getStyleClass().add(RESULT_ERROR_STYLE);
        }
        return label;
    }

    /**
     * Creates a label for a user message.
     *
     * @param message String message from user to the system.
     * @return A new javafx Label object.
     */
    private Label createUserMessageLabel(String message) {
        Label label = new Label(message);
        label.setWrapText(true);
        label.getStyleClass().add(USER_LABEL_STYLE);
        return label;
    }


    @Subscribe
    private void handleNewResultAvailableEvent(NewResultAvailableEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        chatPanel.getChildren().add(createResultMessageLabel(event.message, event.isSuccessful));
    }

    @Subscribe
    private void handleNewUserMessageEvent(NewUserMessageAvailableEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        chatPanel.getChildren().add(createUserMessageLabel(event.message));
    }

}
