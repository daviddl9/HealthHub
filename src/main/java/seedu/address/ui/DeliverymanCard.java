package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import seedu.address.model.healthworker.Healthworker;

/**
 * An UI component that displays information of a {@code Request}.
 */
public class DeliverymanCard extends UiPart<Region> {

    private static final String FXML = "DeliverymanListCard.fxml";
    private static final String BUSY_LABEL_CLASS = "busy";
    private static final String AVAILABLE_LABEL_CLASS = "available";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on RequestBook level 4</a>
     */

    public final Healthworker healthworker;

    @FXML
    private VBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label deliverymanIndicator;

    public DeliverymanCard(Healthworker healthworker, int displayedIndex) {
        super(FXML);
        this.healthworker = healthworker;
        name.setText(healthworker.getName().fullName);
        setDeliverymanStatus();
    }

    private void setDeliverymanStatus() {
        if (healthworker.getRequests().size() > 0) {
            deliverymanIndicator.getStyleClass().clear();
            deliverymanIndicator.getStyleClass().add(BUSY_LABEL_CLASS);
            deliverymanIndicator.setText("Assigned: " + healthworker.getRequests().size());
        } else {
            deliverymanIndicator.getStyleClass().clear();
            deliverymanIndicator.getStyleClass().add(AVAILABLE_LABEL_CLASS);
            deliverymanIndicator.setText("Available");
        }
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeliverymanCard)) {
            return false;
        }

        // state check
        DeliverymanCard card = (DeliverymanCard) other;
        return healthworker.equals(card.healthworker);
    }
}
