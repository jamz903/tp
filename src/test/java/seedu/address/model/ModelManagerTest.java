package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalTransactions.BUYING_GROCERIES;
import static seedu.address.testutil.TypicalTransactions.NUS;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.GuiSettings;
import seedu.address.model.transaction.exceptions.TransactionNotFoundException;
import seedu.address.testutil.UniCashBuilder;

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

    // TODO: Add equivalent hasTransaction tests

    //    @Test
    //    public void hasPerson_nullPerson_throwsNullPointerException() {
    //        assertThrows(NullPointerException.class, () -> modelManager.hasPerson(null));
    //    }
    //
    //    @Test
    //    public void hasPerson_personNotInAddressBook_returnsFalse() {
    //        assertFalse(modelManager.hasPerson(ALICE));
    //    }
    //
    //    @Test
    //    public void hasPerson_personInAddressBook_returnsTrue() {
    //        modelManager.addPerson(ALICE);
    //        assertTrue(modelManager.hasPerson(ALICE));
    //    }
    //
    //    @Test
    //    public void getFilteredPersonList_modifyList_throwsUnsupportedOperationException() {
    //        assertThrows(UnsupportedOperationException.class, () -> modelManager
    //        .getFilteredPersonList().remove(0));
    //    }

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

        assertNotEquals(differentUniCash, modelManager);

        // TODO: Replicate this for transaction list
        // different filteredList -> returns false
        //String[] keywords = ALICE.getName().fullName.split("\\s+");
        //modelManager.updateFilteredPersonList(new NameContainsKeywordsPredicate(Arrays.asList(keywords)));
        //assertFalse(modelManager.equals(new ModelManager(uniCash, userPrefs)));

        // resets modelManager to initial state for upcoming tests
        //modelManager.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        // different userPrefs -> returns false
        UserPrefs differentUserPrefs = new UserPrefs();
        differentUserPrefs.setUniCashFilePath(Paths.get("differentFilePath"));
        assertNotEquals(modelManager, new ModelManager(uniCash, differentUserPrefs));

        // different differentUniCash -> returns false
        assertNotEquals(modelManager, new ModelManager(differentUniCash, userPrefs));
    }
}
