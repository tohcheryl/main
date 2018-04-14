package seedu.address.ui;

import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.ui.NewResultAvailableEvent;
import seedu.address.commons.events.ui.NewUserMessageAvailableEvent;

//@@author jaxony
/**
 * Panel containing the chat history.
 */
public class ChatPanel extends UiPart<Region> {
    private static final int SPACING = 10;
    private static final double WIDTH_DIVISOR = 4.0;
    private static final double WIDTH_MULTIPLIER = 3.0;
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
     * Creates a result message.
     * @param message      String message from system for user feedback.
     * @param isSuccessful If the message represents a successful one.
     * @return A new JavaFX HBox object.
     */
    private HBox createResultMessage(String message, boolean isSuccessful) {
        Label label = createLabel(message, isSuccessful ? RESULT_SUCCESS_STYLE : RESULT_ERROR_STYLE);
        return createHBox(label, Pos.CENTER_LEFT);
    }

    /**
     * Creates a JavaFX label from a message and style class name.
     * @param message String message to add to label.
     * @param styleClassName String name of CSS class for the label.
     * @return New JavaFX Label object.
     */
    private Label createLabel(String message, String styleClassName) {
        Label label = new Label(message);
        label.maxWidthProperty().bind(chatPanel.widthProperty()
                .multiply(WIDTH_MULTIPLIER).divide(WIDTH_DIVISOR));
        label.setWrapText(true);
        label.getStyleClass().add(styleClassName);
        return label;
    }

    /**
     * Creates a HBox object with a specific label and alignment.
     * @param label JavaFX Label object to contain inside HBox.
     * @param alignment Pos constant specifying where to display contents of HBox.
     * @return New HBox object.
     */
    private HBox createHBox(Label label, Pos alignment) {
        HBox hbox = new HBox();
        hbox.setAlignment(alignment);
        hbox.getChildren().add(label);
        return hbox;
    }

    /**
     * Creates a user message.
     * @param message String message from user to the system.
     * @return A new JavaFX HBox object.
     */
    private HBox createUserMessage(String message) {
        Label label = createLabel(message, USER_LABEL_STYLE);
        return createHBox(label, Pos.CENTER_RIGHT);
    }


    @Subscribe
    private void handleNewResultAvailableEvent(NewResultAvailableEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        chatPanel.getChildren().add(createResultMessage(event.message, event.isSuccessful));
    }

    @Subscribe
    private void handleNewUserMessageEvent(NewUserMessageAvailableEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        chatPanel.getChildren().add(createUserMessage(event.message));
    }

}
