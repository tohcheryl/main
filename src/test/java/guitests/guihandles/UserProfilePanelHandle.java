package guitests.guihandles;

import java.util.List;
import java.util.stream.Collectors;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;

//@@author tohcheryl
/**
 * A handle to the {@code UserProfilePanel} in the GUI.
 */
public class UserProfilePanelHandle extends NodeHandle<Node> {

    private static final String NAME_FIELD_ID = "#name";
    private static final String ADDRESS_FIELD_ID = "#address";
    private static final String PHONE_FIELD_ID = "#phone";
    private static final String ALLERGIES_FIELD_ID = "#allergies";

    private final Label nameLabel;
    private final Label addressLabel;
    private final Label phoneLabel;
    private final List<Label> allergyLabels;

    public UserProfilePanelHandle(Node cardNode) {
        super(cardNode);

        this.nameLabel = getChildNode(NAME_FIELD_ID);
        this.addressLabel = getChildNode(ADDRESS_FIELD_ID);
        this.phoneLabel = getChildNode(PHONE_FIELD_ID);
        Region allergiesContainer = getChildNode(ALLERGIES_FIELD_ID);
        this.allergyLabels = allergiesContainer
                .getChildrenUnmodifiable()
                .stream()
                .map(Label.class::cast)
                .collect(Collectors.toList());
    }

    public String getName() {
        return nameLabel.getText();
    }

    public String getAddress() {
        return addressLabel.getText();
    }

    public String getPhone() {
        return phoneLabel.getText();
    }

    public List<String> getAllergies() {
        return allergyLabels
                .stream()
                .map(Label::getText)
                .collect(Collectors.toList());
    }
}
