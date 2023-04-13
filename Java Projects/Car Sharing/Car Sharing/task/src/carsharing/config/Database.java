package carsharing.config;

import java.sql.*;

public class Database {
    private static final String CREATE_TABLE_COMPANY = """
            CREATE TABLE IF NOT EXISTS COMPANY (
                ID INT AUTO_INCREMENT,
                NAME VARCHAR_IGNORECASE(255) UNIQUE NOT NULL,
                PRIMARY KEY (ID)
            );
            """;
    private static final String CREATE_TABLE_CAR = """
            CREATE TABLE IF NOT EXISTS CAR (
                ID INT PRIMARY KEY AUTO_INCREMENT,
                NAME VARCHAR_IGNORECASE(255) NOT NULL UNIQUE,
                COMPANY_ID INT NOT NULL,
                PRIMARY KEY (ID),
                FOREIGN KEY (COMPANY_ID) REFERENCES COMPANY(ID)
            );
            """;
    private static final String CREATE_TABLE_CUSTOMER = """
            CREATE TABLE IF NOT EXISTS CUSTOMER (
                ID INT PRIMARY KEY AUTO_INCREMENT,
                NAME VARCHAR_IGNORECASE(255) NOT NULL UNIQUE,
                RENTED_CAR_ID INT DEFAULT NULL,
                PRIMARY KEY (ID),
                FOREIGN KEY (RENTED_CAR_ID) REFERENCES CAR(ID)
            );
            """;
    private static final String JDBC_DRIVER = "org.h2.Driver";
    private static final String DB_URL = "jdbc:h2:./src/carsharing/db/";
    private final String filename;

    public Database(String filename) {
        this.filename = filename;
        getConnection();
        createTableCompany();
        createTableCar();
        createTableCustomer();
    }

    private void createTableCompany() {
        try (
                Connection connection = getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(CREATE_TABLE_COMPANY)
        ) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void createTableCar() {
        try (
                Connection connection = getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(CREATE_TABLE_CAR)
        ) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void createTableCustomer() {
        try (
                Connection connection = getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(CREATE_TABLE_CUSTOMER)
        ) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public Connection getConnection() {
        try {
            Class.forName(JDBC_DRIVER);
            return DriverManager.getConnection(DB_URL + filename);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

}
