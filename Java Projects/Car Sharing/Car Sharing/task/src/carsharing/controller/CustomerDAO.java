package carsharing.controller;

import carsharing.config.Database;
import carsharing.model.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class CustomerDAO {
    private static final String INSERT_INTO_CUSTOMER =
            """
            INSERT INTO CUSTOMER (NAME)
            VALUES (?);
            """;
    private static final String SELECT_ONE_ID =
            """
            SELECT ID,NAME
            FROM CUSTOMER
            WHERE ID=?;
            """;
    private static final String SELECT_ALL_ID_NAME =
            """
            SELECT ID, NAME
            FROM CUSTOMER;
            """;
    private static final String SELECT_RENTED_CAR_ID =
            """
            SELECT RENTED_CAR_ID
            FROM CUSTOMER
            WHERE NAME = ?;
            """;
    private static final String UPDATE_RENTED_CAR_ID =
            """
            UPDATE CUSTOMER
            SET RENTED_CAR_ID = ?
            WHERE NAME = ? ;
            """;
    private static final String DELETE_RENTED_CAR_ID =
            """
            UPDATE CUSTOMER
            SET RENTED_CAR_ID = NULL
            WHERE NAME=?;
            """;

    private final Database database;

    public CustomerDAO(Database database) {
        this.database = database;
    }

    public void createCustomer(String name) {
        try (
                Connection connection = database.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(INSERT_INTO_CUSTOMER)
        ) {
            preparedStatement.setString(1, name);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String readNameByID(int id) {
        try (
                Connection connection = database.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ONE_ID)
        ) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
               return resultSet.getString("NAME");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public HashMap<Integer, String> readAllIDAndNAME() {
        try (
                Connection connection = database.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_ID_NAME)
        ) {
            ResultSet resultSet = preparedStatement.executeQuery();
            HashMap<Integer, String> customerList = new HashMap<>();
            while (resultSet.next()) {
                customerList.put(
                        resultSet.getInt("ID"),
                        resultSet.getString("NAME")
                );
            }
            return customerList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int readRentedCarID(String name) {
        try (
                Connection connection = database.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(SELECT_RENTED_CAR_ID)
        ) {
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("RENTED_CAR_ID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void updateRentedCar(int rented_car_id, String name) {
        try (
                Connection connection = database.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_RENTED_CAR_ID)
        ) {
            preparedStatement.setInt(1, rented_car_id);
            preparedStatement.setString(2, name);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletedRentedCar(String name) {
        try (
                Connection connection = database.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(DELETE_RENTED_CAR_ID)
        ) {
            preparedStatement.setString(1, name);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
