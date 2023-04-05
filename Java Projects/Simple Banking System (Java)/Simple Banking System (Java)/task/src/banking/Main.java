package banking;

import banking.config.Database;
import banking.controller.AccountDAO;
import banking.view.BankingSystem;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        if (args.length == 2 && args[0].equals("-fileName")) {
            Database database = new Database(args[1]);
            AccountDAO accountDAO = new AccountDAO(database);
            BankingSystem bankingSystem = new BankingSystem(accountDAO, new Scanner(System.in));
            bankingSystem.startSystem();
        } else {
            throw new IllegalArgumentException("Enter the '-fileName' and SQLite db name");
        }
    }
}




