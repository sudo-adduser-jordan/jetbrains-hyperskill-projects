package banking.view;

import banking.controller.AccountDAO;
import banking.model.Account;

import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class BankingSystem {
    private final AccountDAO accountDAO;
    private final Scanner scanner;

    public BankingSystem(AccountDAO accountDAO, Scanner scanner) {
        this.accountDAO = accountDAO;
        this.scanner = scanner;
    }

    public void startSystem() {
        while (true) {
            printMainMenu();
            switch (scanner.nextInt()) {
                case 1 -> createAccount();
                case 2 -> loginAccount();
                case 0 -> System.exit(0);
                default -> System.out.println("Invalid input");
            }
        }

    }

    private void printMainMenu(){
        System.out.println();
        System.out.println("1. Create an account");
        System.out.println("2. Log into account");
        System.out.println("0. Exit");
    }

    private void createAccount() {
        System.out.println();

        String cardNumber = generateAccountNumber();
        System.out.println("Your card has been created");
        System.out.println("Your card number:");
        System.out.println(cardNumber);

        String pin = generatePin();
        System.out.println("Your card PIN:");
        System.out.println(pin);

        Account account = new Account();
        account.setAccountNumber(cardNumber);
        account.setPin(pin);

        accountDAO.create(account);
    }

    private void printAccountMenu(){
        System.out.println();
        System.out.println("1. Balance");
        System.out.println("2. Add income");
        System.out.println("3. Do transfer");
        System.out.println("4. Close account");
        System.out.println("5. Log out");
        System.out.println("0. Exit");
    }

    private void loginAccount() {

        System.out.println();
        System.out.println("Enter your card number:");
        String cardNumber = scanner.next();
        System.out.println("Enter your PIN:");
        String pin = scanner.next();

        Account account = accountDAO.login(cardNumber, pin);

        if (account == null) {
            System.out.println("Wrong card number or PIN!");
            startSystem();
        } else {
            System.out.println();
            System.out.println("You have successfully logged in!");

            while (true) {
                printAccountMenu();
                switch (scanner.nextInt()) {
                    case 1 -> balance(account);
                    case 2 -> addIncome(account);
                    case 3 -> doTransfer(account);
                    case 4 -> closeAccount(account);
                    case 5 -> logout();
                    case 0 -> System.exit(0);
                    default -> System.out.println("Invalid input");
                }
            }
        }

    }

    private void balance(Account account) {
        System.out.println();
        System.out.println("Balance: " + account.getBalance());
    }

    private void addIncome(Account account) {
        System.out.println("Enter income: ");
        int income = scanner.nextInt();

        accountDAO.addToBalance(income, account.getAccountNumber());
        account.setBalance(income + account.getBalance());
        System.out.println("Income was added!");
    }

    private void doTransfer(Account account) {
        System.out.println("Enter card number: ");
        String cardNumber = scanner.next();

        if (!accountDAO.doesCardExist(cardNumber)) {
            System.out.println("Probably you made a mistake in the card number. Please try again!");
        }
        if (!accountDAO.doesCardExist(cardNumber)) {
            System.out.println("Such a card does not exist.");
        }

        if (accountDAO.doesCardExist(cardNumber)) {
            System.out.println("Enter how much money you want to transfer: ");
            int amount = scanner.nextInt();

            if (account.getBalance() < amount){
                System.out.println("Not enough money!");
            } else {
                accountDAO.addToBalance(amount, cardNumber);
                accountDAO.withdrawFromBalance(amount, account.getAccountNumber());
                account.setBalance(account.getBalance() - amount);
                System.out.println("Success!");
            }
        }
    }

    private void closeAccount(Account account) {
        accountDAO.deleteAccount(account.getId());
        System.out.println("The account has been closed!");
    }

    private void logout() {
        System.out.println();
        System.out.println("You have successfully logged out!");
        startSystem();
    }

    private String generateAccountNumber() {
        String bin = "400000";

        ThreadLocalRandom random = ThreadLocalRandom.current();
        long uniqueNumber = random.nextLong(100_000_000L, 1_000_000_000L);

        int checksum = generateChecksum(bin + uniqueNumber);

        String n = bin + uniqueNumber + checksum;

        if (!accountDAO.doesCardExist(n)) {
            return n;
        } else {
            generateAccountNumber();
        }
        return n;
    }

    private int generateChecksum(String initialNumber) {
        int sum = 0;
        int value;
        var array = (initialNumber).toCharArray();

        for (int i = 0; i < array.length; i++) {
            value = Character.getNumericValue(array[i]);
            sum += value;

            if (i % 2 == 0) {
                sum += value < 5 ? value : value - 9;
            }
        }
        return (sum / 10 * 10 + 10 - sum) % 10;
    }

    private String generatePin() {
        Random random = new Random();
        int n = 1000 + random.nextInt(9000);
        return String.valueOf(n);
    }

}