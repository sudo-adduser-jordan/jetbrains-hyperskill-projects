package carsharing.controller;

import carsharing.config.Database;
import carsharing.model.Company;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class CompanyDAO {
    private static final String INSERT_INTO_COMPANY =
        """
        INSERT INTO COMPANY (NAME)
        VALUES (?);
        """;
    private static final String SELECT_ONE_ID =
        """
        SELECT ID,NAME
        FROM COMPANY
        WHERE ID=?;
        """;
    private static final String SELECT_ALL_ID_NAME =
        """
        SELECT ID, NAME
        FROM COMPANY;
        """;
    private final Database database;

    public CompanyDAO(Database database) {
        this.database = database;
    }

    public void createCompany(String name) {
        try (
                Connection connection = database.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(INSERT_INTO_COMPANY)
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
            HashMap<Integer, String> companyList = new HashMap<>();

            while (resultSet.next()) {
                companyList.put(
                        resultSet.getInt("ID"),
                        resultSet.getString("NAME")
                );
            }

            return companyList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


}
