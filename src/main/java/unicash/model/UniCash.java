package unicash.model;

import static java.util.Objects.requireNonNull;

import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import javafx.collections.ObservableList;
import unicash.commons.util.ToStringBuilder;
import unicash.model.category.Category;
import unicash.model.transaction.DateTime;
import unicash.model.transaction.Transaction;
import unicash.model.transaction.TransactionList;

/**
 * Wraps all data in UniCash
 */
public class UniCash implements ReadOnlyUniCash {

    private final TransactionList transactions;

    /*
     * The 'unusual' code block below is a non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        transactions = new TransactionList();
    }

    public UniCash() {}

    /**
     * Creates an UniCash using the Transactions in the {@code toBeCopied}
     */
    public UniCash(ReadOnlyUniCash toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the Transaction list with {@code transactions}.
     * {@code transactions} must not contain any null.
     */
    public void setTransactions(List<Transaction> transactions) {
        this.transactions.setTransactions(transactions);
    }

    /**
     * Resets the existing data of this {@code UniCash} with {@code newData}.
     */
    public void resetData(ReadOnlyUniCash newData) {
        requireNonNull(newData);

        setTransactions(newData.getTransactionList());
    }

    //// Transaction-level operations

    /**
     * Returns true if a Transaction with the same identity as {@code Transaction} exists in UniCash.
     */
    public boolean hasTransaction(Transaction transaction) {
        requireNonNull(transaction);
        return transactions.contains(transaction);
    }

    /**
     * Adds a transaction to UniCash
     */
    public void addTransaction(Transaction p) {
        transactions.add(p);
    }

    /**
     * Replaces the given Transaction {@code target} in the list with {@code editedTransaction}.
     */
    public void setTransaction(Transaction target, Transaction editedTransaction) {
        requireNonNull(editedTransaction);
        transactions.setTransaction(target, editedTransaction);
    }

    /**
     * Removes {@code key} from this {@code UniCash}.
     * {@code key} must exist in UniCash.
     */
    public void removeTransaction(Transaction key) {
        transactions.remove(key);
    }

    private List<Transaction> getAllExpenses() {
        return getTransactionList()
                .stream()
                .filter(t -> t.getType().toString().equals("expense"))
                .collect(Collectors.toUnmodifiableList());
    }

    /**
     * Returns the expense amount per yearmonth. (E.g. 2023 Jan, 2023 Feb, 2024 Jan, etc.)
     * Note: This function ignores all 'income' transactions
     */
    HashMap<YearMonth, Double> getSumOfExpensePerYearMonth() {
        HashMap<YearMonth, Double> sumPerMonth = new HashMap<>();
        List<Transaction> allExpenses = getAllExpenses();

        for (Transaction t : allExpenses) {
            Double transactionAmount = t.getAmountAsDouble();
            YearMonth yearMonth = t.getDateTime().getYearMonth();
            if (!sumPerMonth.containsKey(yearMonth)) {
                sumPerMonth.put(yearMonth, 0.0);
            }
            Double currAmount = sumPerMonth.get(yearMonth);
            sumPerMonth.put(yearMonth, currAmount + transactionAmount);
        }
        return sumPerMonth;
    }

    /**
     * Returns the amount for each category of expenses.
     * Note: This function ignores all 'income' transactions
     */
    HashMap<String, Double> getSumOfExpensePerCategory() {
        HashMap<String, Double> sumPerCategory = new HashMap<>();
        List<Transaction> allExpenses = getAllExpenses();
        String uncategorizedCategoryName = "Uncategorized";

        for (Transaction t : allExpenses) {
            Double transactionAmount = t.getAmountAsDouble();
            boolean hasNoCategory = t.getCategories().asUnmodifiableObservableList().isEmpty();

            if (hasNoCategory) {
                handleNoCategoryTransaction(sumPerCategory, uncategorizedCategoryName, transactionAmount);
            } else {
                handleCategoryTransaction(t.getCategories().asUnmodifiableObservableList(), sumPerCategory, transactionAmount);
            }
        }

        return sumPerCategory;
    }

    private void handleNoCategoryTransaction(HashMap<String, Double> sumPerCategory, String uncategorizedCategoryName, Double transactionAmount) {
        if (!sumPerCategory.containsKey(uncategorizedCategoryName)) {
            sumPerCategory.put(uncategorizedCategoryName, (double) 0);
        }
        Double currAmount = sumPerCategory.get(uncategorizedCategoryName);
        sumPerCategory.put(uncategorizedCategoryName, currAmount + transactionAmount);
    }

    private void handleCategoryTransaction(ObservableList<Category> categories, HashMap<String, Double> sumPerCategory, Double transactionAmount) {
        for (Category transactionCategory : categories) {
            if (!sumPerCategory.containsKey(transactionCategory.category)) {
                sumPerCategory.put(transactionCategory.category, (double) 0);
            }
            Double currAmount = sumPerCategory.get(transactionCategory.category);
            sumPerCategory.put(transactionCategory.category, currAmount + transactionAmount);
        }
    }

    //// util methods

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("transactions", transactions)
                .toString();
    }

    @Override
    public ObservableList<Transaction> getTransactionList() {
        return transactions.asUnmodifiableObservableList();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof UniCash)) {
            return false;
        }

        UniCash otherUniCash = (UniCash) other;
        return transactions.equals(otherUniCash.transactions);
    }

    @Override
    public int hashCode() {
        return transactions.hashCode();
    }
}
