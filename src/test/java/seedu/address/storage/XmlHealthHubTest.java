package seedu.address.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.google.common.collect.Streams;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.XmlUtil;
import seedu.address.model.RequestBook;
import seedu.address.model.healthworker.HealthworkerList;
import seedu.address.testutil.TypicalDeliverymen;
import seedu.address.testutil.TypicalOrders;

public class XmlHealthHubTest {
    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data",
        "XmlHealthHubTest");
    private static final Path TYPICAL_FOODZOOM_FILE = TEST_DATA_FOLDER.resolve("typicalHealthHub.xml");
    private static final Path INVALID_FOODZOOM_FILE = TEST_DATA_FOLDER.resolve("invalidHealthHub.xml");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void getRequestsAndHealthworkers_typicalHealthHub_success() throws Exception {
        XmlHealthHub dataFromFile = XmlUtil.getDataFromFile(TYPICAL_FOODZOOM_FILE,
            XmlHealthHub.class);
        RequestBook orderBookFromFile = dataFromFile.getOrderBook();
        RequestBook typicalOrdersOrderBook = TypicalOrders.getTypicalOrderBook();
        HealthworkerList healthworkerListFromFile = dataFromFile.getDeliverymenList();
        HealthworkerList typicalDeliverymenHealthworkerList = TypicalDeliverymen.getTypicalDeliverymenList();
        assertEquals(orderBookFromFile, typicalOrdersOrderBook);
        assertTrue(Streams.zip(orderBookFromFile.getRequestList().stream(),
            typicalOrdersOrderBook.getRequestList().stream(), (a, b) -> a.hasSameTag(b)).allMatch(x -> x));
        assertEquals(healthworkerListFromFile, typicalDeliverymenHealthworkerList);
        assertTrue(Streams.zip(healthworkerListFromFile.getDeliverymenList().stream(),
            typicalDeliverymenHealthworkerList.getDeliverymenList().stream(), (a, b) -> a.hasSameTag(b))
            .allMatch(x -> x));
    }

    @Test
    public void getRequestsAndHealthworkers_invalidFile_throwsIllegalValueException() throws Exception {
        XmlHealthHub dataFromFile = XmlUtil.getDataFromFile(INVALID_FOODZOOM_FILE, XmlHealthHub.class);
        thrown.expect(IllegalValueException.class);
        dataFromFile.getOrderBook();
    }
}
