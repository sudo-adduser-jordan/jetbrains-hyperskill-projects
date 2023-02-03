package budget.manager;

enum ExpenseType {
    FOOD, ENTERTAINMENT, CLOTHES, OTHER;

    @Override
    public String toString() {
        //
        return name().charAt(0) + name().substring(1).toLowerCase();
    }
}

//
public class Expense implements Comparable<Expense> {
    private final String name;
    private final double value;
    private final ExpenseType expenseType;

    public Expense(String name, double value, ExpenseType expenseType) {
        this.name = name;
        this.value = value;
        this.expenseType = expenseType;
    }

    public String getName() {
        return name;
    }

    public double getValue() {
        return value;
    }

    public ExpenseType getExpenseType() {
        return expenseType;
    }

    @Override
    public int compareTo(Expense o) {
        if (o.getValue() == this.value) {
            return o.getName().compareTo(this.name);
        }
        //
        return o.getValue() > this.value ? 1 : -1;

    }

    @Override
    public String toString() {
        //
        return String.format(name + " $%.2f", value);
    }

}