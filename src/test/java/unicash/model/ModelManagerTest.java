package unicash.model;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static unicash.model.Model.PREDICATE_SHOW_ALL_TRANSACTIONS;
import static unicash.testutil.Assert.assertThrows;
import static unicash.testutil.TypicalTransactions.BUYING_GROCERIES;
import static unicash.testutil.TypicalTransactions.NUS;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import unicash.commons.core.GuiSettings;
import unicash.model.transaction.TransactionNameContainsKeywordsPredicate;
import unicash.model.transaction.exceptions.TransactionNotFoundException;
import unicash.testutil.UniCashBuilder;

public class ModelManagerTest {

    private ModelManager modelManager = new ModelManager();

    @Test
    public void constructor() {
        assertEquals(new UserPrefs(), modelManager.getUserPrefs());
        assertEquals(new GuiSettings(), modelManager.getGuiSettings());
        assertEquals(new UniCash(), new UniCash(modelManager.getUniCash()));
    }

    @Test
    public void setUserPrefs_nullUserPrefs_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setUserPrefs(null));
    }

    @Test
    public void setUserPrefs_validUserPrefs_copiesUserPrefs() {
        UserPrefs userPrefs = new UserPrefs();
        userPrefs.setUniCashFilePath(Paths.get("address/book/file/path"));
        userPrefs.setGuiSettings(new GuiSettings(1, 2, 3, 4));
        modelManager.setUserPrefs(userPrefs);
        assertEquals(userPrefs, modelManager.getUserPrefs());

        // Modifying userPrefs should not modify modelManager's userPrefs
        UserPrefs oldUserPrefs = new UserPrefs(userPrefs);
        userPrefs.setUniCashFilePath(Paths.get("new/address/book/file/path"));
        assertEquals(oldUserPrefs, modelManager.getUserPrefs());
    }

    @Test
    public void setGuiSettings_nullGuiSettings_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setGuiSettings(null));
    }

    @Test
    public void setGuiSettings_validGuiSettings_setsGuiSettings() {
        GuiSettings guiSettings = new GuiSettings(1, 2, 3, 4);
        modelManager.setGuiSettings(guiSettings);
        assertEquals(guiSettings, modelManager.getGuiSettings());
    }

    @Test
    public void setUniCashFilePath_nullPath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setUniCashFilePath(null));
    }

    @Test
    public void setUniCashFilePath_validPath_setsUniCashFilePath() {
        Path path = Paths.get("address/book/file/path");
        modelManager.setUniCashFilePath(path);
        assertEquals(path, modelManager.getUniCashFilePath());
    }

    @Test
    public void setUniCash_nullTransaction_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setUniCash(null));
    }

    @Test
    public void hasTransaction_nullTransaction_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.hasTransaction(null));
    }

    @Test
    public void hasTransaction_transactionNotInUniCash_returnsFalse() {
        assertFalse(modelManager.hasTransaction(NUS));
    }

    @Test
    public void hasTransaction_transactionInUniCash_returnsTrue() {
        modelManager.addTransaction(NUS);
        assertTrue(modelManager.hasTransaction(NUS));
    }

    @Test
    public void deleteTransaction_transactionNotInUniCash_throws() {
        modelManager.addTransaction(NUS);
        assertThrows(TransactionNotFoundException.class, () -> modelManager.deleteTransaction(BUYING_GROCERIES));
    }

    @Test
    public void deleteTransaction_transactionInUniCash_throws() {
        modelManager.addTransaction(NUS);
        assertDoesNotThrow(() -> modelManager.deleteTransaction(NUS));
    }

    @Test
    public void getFilteredUniCash_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> modelManager.getFilteredTransactionList().remove(0));
    }

    @Test
    public void equals() {
        UniCash uniCash = new UniCashBuilder().withTransaction(NUS).build();
        UniCash differentUniCash = new UniCash();
        UserPrefs userPrefs = new UserPrefs();

        // same values -> returns true
        modelManager = new ModelManager(uniCash, userPrefs);
        ModelManager modelManagerCopy = new ModelManager(uniCash, userPrefs);
        assertEquals(modelManager, modelManagerCopy);

        // same object -> returns true
        assertEquals(modelManager, modelManager);

        // null -> returns false
        assertNotEquals(null, modelManager);

        // different types -> returns false
        assertNotEquals(5, modelManager);

        assertFalse(modelManager.equals(uniCash));

        // TODO: Replicate this for transaction list
        // different filteredList -> returns false
        String[] keywords = new String[] {"internship"};
        modelManager.updateFilteredTransactionList(
                new TransactionNameContainsKeywordsPredicate(Arrays.asList(keywords))
        );
        assertFalse(modelManager.equals(new ModelManager(uniCash, userPrefs)));

        // resets modelManager to initial state for upcoming tests
        modelManager.updateFilteredTransactionList(PREDICATE_SHOW_ALL_TRANSACTIONS);

        // different userPrefs -> returns false
        UserPrefs differentUserPrefs = new UserPrefs();
        differentUserPrefs.setUniCashFilePath(Paths.get("differentFilePath"));
        assertNotEquals(modelManager, new ModelManager(uniCash, differentUserPrefs));

        // different differentUniCash -> returns false
        assertNotEquals(modelManager, new ModelManager(differentUniCash, userPrefs));
    }
}
