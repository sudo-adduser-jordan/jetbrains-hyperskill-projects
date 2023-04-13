package carsharing.controller;

import carsharing.config.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class CarDAO {
    private static final String INSERT_INTO_COMPANY =
        """
        INSERT INTO CAR (NAME, COMPANY_ID)
        VALUES (?,?);
        """;
    private static final String SELECT_ONE_ID =
        """
        SELECT ID,NAME
        FROM CAR
        WHERE ID=?;
        """;
    private static final String SELECT_ALL_ID_NAME =
        """
        SELECT ID, NAME
        FROM CAR
        WHERE COMPANY_ID=?;
        """;
    private static final String SELECT_ALL_AVAILABLE_ID_NAME =
        """
        SELECT CAR.ID, CAR.NAME
        FROM CAR
        LEFT JOIN CUSTOMER
        ON CAR.ID = CUSTOMER.RENTED_CAR_ID
        WHERE COMPANY_ID=? AND CUSTOMER.ID IS NULL;
        """;


    private final Database database;

    public CarDAO(Database database) {
        this.database = database;
    }

    public void createCar(String name, int company_id) {
        try (
                Connection connection = database.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(INSERT_INTO_COMPANY)
        ) {
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, company_id);
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

            String name = null;
            if (resultSet.next()) {
                name = resultSet.getString("NAME");
            }
            if (name != null) {
                return name;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public HashMap<Integer, String> readAllByCOMPANY_ID(int company_id) {
        try (
                Connection connection = database.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_ID_NAME)
        ) {
            preparedStatement.setInt(1, company_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            HashMap<Integer, String> carList = new HashMap<>();

            int counter = 1;
            while (resultSet.next()) {
                carList.put(
                        counter,
                        resultSet.getString("NAME")
                );
                counter++;
            }

            return carList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public HashMap<Integer, String> readAllAvailableByCOMPANY_ID(int company_id) {
        try (
                Connection connection = database.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_AVAILABLE_ID_NAME)
        ) {
            preparedStatement.setInt(1, company_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            HashMap<Integer, String> availableCarList = new HashMap<>();

            while (resultSet.next()) {
                availableCarList.put(
                        resultSet.getInt("ID"),
                        resultSet.getString("NAME")
                );
            }

            return availableCarList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
