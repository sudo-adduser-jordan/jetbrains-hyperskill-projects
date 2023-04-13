package carsharing;

import carsharing.config.Database;
import carsharing.controller.CarDAO;
import carsharing.controller.CompanyDAO;
import carsharing.controller.CustomerDAO;
import carsharing.view.CarSharingView;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        if (args.length == 2 && args[0].equals("-databaseFileName")) {

        Database database = new Database(args[1]);

        CompanyDAO companyDAO = new CompanyDAO(database);
        CarDAO carDAO = new CarDAO(database);
        CustomerDAO customerDAO = new CustomerDAO(database);

        CarSharingView carSharingView = new CarSharingView(new Scanner(System.in), companyDAO, carDAO, customerDAO);
        carSharingView.startSystem();

        } else {
            throw new IllegalArgumentException("Enter the '-fileName' and H2 db name");
        }

//            Database database = new Database("carsharing");
//
//            CompanyDAO companyDAO = new CompanyDAO(database);
//            CarDAO carDAO = new CarDAO(database);
//            CustomerDAO customerDAO = new CustomerDAO(database);
//
//        CarSharingView carSharingView = new CarSharingView(new Scanner(System.in), companyDAO, carDAO, customerDAO);
//            carSharingView.startSystem();
//
    }
}
