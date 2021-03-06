package seedu.address.ui.display;

import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import seedu.address.model.request.Condition;
import seedu.address.model.request.Request;
import seedu.address.ui.UiPart;

/**
 * UI Component representing the display of a single request in a list.
 */
public class RequestDisplayListCard extends UiPart<Region> {
    private static final String FXML = "display/RequestDisplayCard.fxml";
    private static final String NAME_LABEL_FORMAT = "Contact: %1$s (%2$s)";
    private static final String FOOD_LABEL_FORMAT = "Request: %s";

    public final Request request;

    @FXML
    private Label address;
    @FXML
    private Label name;
    @FXML
    private Label condition;

    public RequestDisplayListCard(Request request) {
        super(FXML);
        this.request = request;
        address.setText(request.getAddress().toString());
        name.setText(String.format(NAME_LABEL_FORMAT, request.getName().fullName, request.getPhone().toString()));

        condition.setText(String.format(FOOD_LABEL_FORMAT, String.join(", ",
            request.getCondition().stream().map(Condition::toString).collect(Collectors.toSet()))));
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof RequestDisplayListCard)) {
            return false;
        }

        // state check
        RequestDisplayListCard card = (RequestDisplayListCard) other;
        return request.equals(card.request);
    }
}
