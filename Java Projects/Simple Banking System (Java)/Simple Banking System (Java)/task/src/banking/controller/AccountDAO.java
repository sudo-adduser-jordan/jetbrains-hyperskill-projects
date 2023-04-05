package banking.controller;


import banking.config.Database;
import banking.model.Account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class AccountDAO {

    private static final String INSERT_INTO_ACCOUNT = "INSERT INTO card (number, pin) VALUES (?, ?)";
    private static final String SELECT_COUNT_NUMBER = "SELECT count(number) FROM card WHERE number = (?)";
    private static final String SELECT_ACCOUNT_BY_NUMBER_AND_PIN = "SELECT * FROM card WHERE number=? AND pin=?";
    private static final String ADD_BALANCE = "UPDATE card SET balance = (balance + ?) WHERE number = ?";
    private static final String MINUS_BALANCE = "UPDATE card SET balance = (balance - ?) WHERE number = ?";
    private static final String DELETE_ACCOUNT = "DELETE FROM card WHERE id = ?";

    private final Database database;

    public AccountDAO(Database database) {
        this.database = database;
    }

    public void create(Account account) {
        try (
            Connection connection = database.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_INTO_ACCOUNT)
        ) {
            preparedStatement.setString(1, account.getAccountNumber());
            preparedStatement.setString(2, account.getPin());
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public boolean doesCardExist(String cardNumber) {
        try (
            Connection connection = database.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_COUNT_NUMBER)
        ) {
            preparedStatement.setString(1, cardNumber);
            ResultSet res = preparedStatement.executeQuery();
            if (res.next()) {
                return res.getBoolean(1);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public Account login(String cardNumber, String pin) {
        try (
            Connection connection = database.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ACCOUNT_BY_NUMBER_AND_PIN)
        ) {
            preparedStatement.setString(1, cardNumber);
            preparedStatement.setString(2, pin);
            ResultSet res = preparedStatement.executeQuery();
            if (res.next()) {
                return new Account(
                        res.getInt("id"),
                        res.getString("number"),
                        res.getString("pin"),
                        res.getInt("balance")
                );
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public void addToBalance(int amount, String card) {
        try (
            Connection connection = database.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(ADD_BALANCE)
        ) {
            preparedStatement.setInt(1, amount);
            preparedStatement.setString(2, card);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void withdrawFromBalance(int amountSend, String card) {
        try (
                Connection connection = database.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(MINUS_BALANCE)
        ) {
            preparedStatement.setInt(1, amountSend);
            preparedStatement.setString(2, card);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }


    public void deleteAccount(int id) {
        try (
            Connection connection = database.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_ACCOUNT)
        ) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}