package systemtests;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static seedu.address.ui.StatusBarFooter.SYNC_STATUS_INITIAL;
import static seedu.address.ui.StatusBarFooter.SYNC_STATUS_UPDATED;
import static seedu.address.ui.testutil.GuiTestAssert.assertListMatching;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;

import guitests.guihandles.BrowserPanelHandle;
import guitests.guihandles.CommandBoxHandle;
import guitests.guihandles.MainMenuHandle;
import guitests.guihandles.MainWindowHandle;
import guitests.guihandles.OrderListPanelHandle;
import guitests.guihandles.ResultDisplayHandle;
import guitests.guihandles.StatusBarFooterHandle;
import seedu.address.TestApp;
import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.request.ClearCommand;
import seedu.address.logic.commands.request.FindCommand;
import seedu.address.logic.commands.request.ListCommand;
import seedu.address.logic.commands.request.RequestCommand;
import seedu.address.logic.commands.request.SelectCommand;
import seedu.address.model.Model;
import seedu.address.model.RequestBook;
import seedu.address.model.UsersList;
import seedu.address.model.healthworker.HealthworkerList;
import seedu.address.testutil.TypicalDeliverymen;
import seedu.address.testutil.TypicalOrders;
import seedu.address.testutil.user.TypicalUsers;
import seedu.address.ui.CommandBox;

/**
 * A system test class for RequestBook, which provides access to handles of GUI components and helper methods
 * for test verification.
 */
public abstract class RequestBookSystemTest {
    private static final List<String> COMMAND_BOX_DEFAULT_STYLE = Arrays.asList("text-input", "text-field");
    private static final List<String> COMMAND_BOX_ERROR_STYLE =
        Arrays.asList("text-input", "text-field", CommandBox.ERROR_STYLE_CLASS);
    @ClassRule
    public static ClockRule clockRule = new ClockRule();
    protected TestApp testApp;

    private MainWindowHandle mainWindowHandle;
    private SystemTestSetupHelper setupHelper;

    @BeforeClass
    public static void setupBeforeClass() {
        SystemTestSetupHelper.initialize();
    }

    @Before
    public void setUp() {
        setupHelper = new SystemTestSetupHelper();
        testApp = setupHelper.setupApplication(this::getInitialOrdersData, this::getInitialDeliverymenData,
            this::getInitialUsersData, getDataFileLocation(), getUsersDataFileLocation());
        mainWindowHandle = setupHelper.setupMainWindowHandle();

        assertApplicationStartingStateIsCorrect();
    }

    @After
    public void tearDown() {
        setupHelper.tearDownStage();
        EventsCenter.clearSubscribers();
    }

    /**
     * Returns the orders data to be loaded into the file in {@link #getDataFileLocation()}.
     */
    protected RequestBook getInitialOrdersData() {
        return TypicalOrders.getTypicalOrderBook();
    }

    /**
     * Returns the deliverymen data to be loaded into the file in {@link #getDataFileLocation()}.
     */
    protected HealthworkerList getInitialDeliverymenData() {
        return TypicalDeliverymen.getTypicalDeliverymenList();
    }

    /**
     * Returns the deliverymen data to be loaded into the file in {@link #getDataFileLocation()}.
     */
    protected UsersList getInitialUsersData() {
        return TypicalUsers.getTypicalUsersList();
    }

    /**
     * Returns the directory of the data file.
     */
    protected Path getDataFileLocation() {
        return TestApp.SAVE_LOCATION_FOR_TESTING;
    }

    /**
     * Returns the directory of the data file.
     */
    protected Path getUsersDataFileLocation() {
        return TestApp.USERS_SAVE_LOCATION_FOR_TESTING;
    }

    public MainWindowHandle getMainWindowHandle() {
        return mainWindowHandle;
    }

    public CommandBoxHandle getCommandBox() {
        return mainWindowHandle.getCommandBox();
    }

    public OrderListPanelHandle getOrderListPanel() {
        return mainWindowHandle.getOrderListPanel();
    }

    public MainMenuHandle getMainMenu() {
        return mainWindowHandle.getMainMenu();
    }

    public StatusBarFooterHandle getStatusBarFooter() {
        return mainWindowHandle.getStatusBarFooter();
    }

    public ResultDisplayHandle getResultDisplay() {
        return mainWindowHandle.getResultDisplay();
    }

    /**
     * Executes {@code command} in the application's {@code CommandBox}.
     * Method returns after UI components have been updated.
     */
    protected void executeCommand(String command) {
        rememberStates();
        // Injects a fixed clock before executing a command so that the time stamp shown in the status bar
        // after each command is predictable and also different from the previous command.
        clockRule.setInjectedClockToCurrentTime();

        mainWindowHandle.getCommandBox().run(command);
    }

    protected void setUpOrderListPanel() {
        mainWindowHandle.setOrderListPanel();
        getOrderListPanel().rememberSelectedOrderCard();
        assertListMatching(getOrderListPanel(), getModel().getFilteredOrderList());
    }

    /**
     * Displays all request in the address book.
     */
    protected void showAllOrders() {
        executeCommand(RequestCommand.COMMAND_WORD + " " + ListCommand.COMMAND_WORD);
        assertEquals(getModel().getOrderBook().getRequestList().size(), getModel().getFilteredOrderList().size());
    }

    /**
     * Displays all orders with any parts of their names matching {@code keyword} (case-insensitive).
     */
    protected void showOrdersWithName(String keyword) {
        executeCommand(RequestCommand.COMMAND_WORD + " " + FindCommand.COMMAND_WORD + " " + keyword);
        assertTrue(getModel().getFilteredOrderList().size() < getModel().getOrderBook().getRequestList().size());
    }

    /**
     * Selects the request at {@code index} of the displayed list.
     */
    protected void selectOrder(Index index) {
        executeCommand(RequestCommand.COMMAND_WORD + " " + SelectCommand.COMMAND_WORD + " " + index.getOneBased());
        assertEquals(index.getZeroBased(), getOrderListPanel().getSelectedCardIndex());
    }

    /**
     * Deletes all orders in the request book.
     */
    protected void deleteAllOrders() {
        executeCommand(RequestCommand.COMMAND_WORD + " " + ClearCommand.COMMAND_WORD);
        assertEquals(0, getModel().getOrderBook().getRequestList().size());
    }

    /**
     * Asserts that the {@code CommandBox} displays {@code expectedCommandInput}, the {@code ResultDisplay} displays
     * {@code expectedResultMessage}, the storage contains the same request objects as {@code expectedModel}
     * and the request list panel displays the orders in the model correctly.
     */
    protected void assertApplicationDisplaysExpected(String expectedCommandInput, String expectedResultMessage,
                                                     Model expectedModel) {
        assertEquals(expectedCommandInput, getCommandBox().getInput());
        assertEquals(expectedResultMessage, getResultDisplay().getText());
        assertEquals(new RequestBook(expectedModel.getOrderBook()), testApp.readStorageOrderBook());
        //assertListMatching(getOrderListPanel(), expectedModel.getFilteredOrderList());
    }

    /**
     * Calls {@code BrowserPanelHandle}, {@code OrderListPanelHandle} and {@code StatusBarFooterHandle} to remember
     * their current state.
     */
    private void rememberStates() {
        StatusBarFooterHandle statusBarFooterHandle = getStatusBarFooter();
        statusBarFooterHandle.rememberSaveLocation();
        statusBarFooterHandle.rememberSyncStatus();
    }

    /**
     * Asserts that the previously selected card is now deselected and the browser's url remains displaying the details
     * of the previously selected common.
     *
     * @see BrowserPanelHandle#isUrlChanged()
     */
    protected void assertSelectedCardDeselected() {
        assertFalse(getOrderListPanel().isAnyCardSelected());
    }

    /**
     * Asserts that the browser's url and the selected card in the common list panel remain unchanged.
     *
     * @see BrowserPanelHandle#isUrlChanged()
     * @see OrderListPanelHandle#isSelectedOrderCardChanged() ()
     */
    protected void assertSelectedCardUnchanged() {
        assertFalse(getOrderListPanel().isSelectedOrderCardChanged());
    }

    /**
     * Asserts that the command box's shows the default style.
     */
    protected void assertCommandBoxShowsDefaultStyle() {
        assertEquals(COMMAND_BOX_DEFAULT_STYLE, getCommandBox().getStyleClass());
    }

    /**
     * Asserts that the command box's shows the error style.
     */
    protected void assertCommandBoxShowsErrorStyle() {
        assertEquals(COMMAND_BOX_ERROR_STYLE, getCommandBox().getStyleClass());
    }

    /**
     * Asserts that the entire status bar remains the same.
     */
    protected void assertStatusBarUnchanged() {
        StatusBarFooterHandle handle = getStatusBarFooter();
        assertFalse(handle.isSaveLocationChanged());
        assertFalse(handle.isSyncStatusChanged());
    }

    /**
     * Asserts that only the sync status in the status bar was changed to the timing of
     * {@code ClockRule#getInjectedClock()}, while the save location remains the same.
     */
    protected void assertStatusBarUnchangedExceptSyncStatus() {
        StatusBarFooterHandle handle = getStatusBarFooter();
        String timestamp = new Date(clockRule.getInjectedClock().millis()).toString();
        String expectedSyncStatus = String.format(SYNC_STATUS_UPDATED, timestamp);
        assertEquals(expectedSyncStatus, handle.getSyncStatus());
        assertFalse(handle.isSaveLocationChanged());
    }

    /**
     * Asserts that the starting state of the application is correct.
     */
    private void assertApplicationStartingStateIsCorrect() {
        assertEquals("", getCommandBox().getInput());
        assertEquals("", getResultDisplay().getText());
        assertEquals(Paths.get(".").resolve(testApp.getStorageSaveLocation()).toString(),
            getStatusBarFooter().getSaveLocation());
        assertEquals(SYNC_STATUS_INITIAL, getStatusBarFooter().getSyncStatus());
    }

    /**
     * Returns a defensive copy of the current model.
     */
    protected Model getModel() {
        return testApp.getModel();
    }
}
