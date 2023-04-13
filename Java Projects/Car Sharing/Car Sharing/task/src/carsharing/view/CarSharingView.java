package carsharing.view;

import carsharing.controller.CarDAO;
import carsharing.controller.CompanyDAO;
import carsharing.controller.CustomerDAO;
import carsharing.model.Car;
import carsharing.model.Company;
import carsharing.model.Customer;

import java.util.Scanner;

public class CarSharingView {
    String MAIN_MENU =
            """
            1. Log in as a manager
            2. Log in as a customer
            3. Create a customer
            0. Exit
            """;
    String MANAGER_MENU =
            """
            1. Company list
            2. Create a company
            0. Back
            """;

    String COMPANY_MENU =
            """
            1. Car list
            2. Create a car
            0. Back
            """;

    String CUSTOMER_MENU =
            """
            1. Rent a car
            2. Return a rented car
            3. My rented car
            0. Back
            """;

    private final Scanner scanner;
    private final CompanyDAO companyDAO;
    private final CarDAO carDAO;
    private final CustomerDAO customerDAO;

    public CarSharingView(Scanner scanner,CompanyDAO companyDAO, CarDAO carDAO, CustomerDAO customerDAO) {
        this.companyDAO = companyDAO;
        this.carDAO = carDAO;
        this.customerDAO = customerDAO;
        this.scanner = scanner;
    }

    boolean menu;
    private final Company company = new Company();
    private final Car car = new Car();
    private final Customer customer = new Customer();

    public void startSystem() {

        company.setCompanyList(companyDAO.readAllIDAndNAME());
        customer.setCustomerList(customerDAO.readAllIDAndNAME());

        menu = true;
        while (menu) {
            System.out.println();
            System.out.println(MAIN_MENU);
            switch (scanner.nextInt()) {
                case 1 -> loginAsManager();
                case 2 -> loginAsCustomer();
                case 3 -> createCustomer();
                case 0 -> System.exit(0);
                default -> System.out.println("Invalid input");
            }
        }
    }

    private void loginAsManager() {
        while (menu) {
            System.out.println();
            System.out.println(MANAGER_MENU);
            switch (scanner.nextInt()) {
                case 1 -> chooseCompany();
                case 2 -> createCompany();
                case 0 -> {
                    startSystem();
                    menu = false;
                }
                default -> System.out.println("Invalid input");
            }
        }
    }

    private void loginToCompany() {
        System.out.println();
        System.out.println("'" + company.getName() + "'" + " company:");
        while (menu) {
            System.out.println();
            System.out.println(COMPANY_MENU);
            switch (scanner.nextInt()) {
                case 1 -> printCarList();
                case 2 -> createCar();
                case 0 -> {
                    loginAsManager();
                    menu = false;
                }
                default -> System.out.println("Invalid input");
            }
        }
    }

    private void loginAsCustomer() {

        customer.setCustomerList(customerDAO.readAllIDAndNAME());

        if (customer.getName() == null) {
            chooseCustomer();
            }
            if (!customer.getCustomerList().isEmpty()) {
                System.out.println();
                System.out.println("Logged in as '" + customer.getName() + "':");
                while (menu) {
                    System.out.println();
                    System.out.println(CUSTOMER_MENU);
                    switch (scanner.nextInt()) {
                        case 1 -> rentCar();
                        case 2 -> returnCar();
                        case 3 -> rentedCar();
                        case 0 -> {
                            customer.setId(0);
                            customer.setName(null);
                            customer.setRented_car_id(0);
                            menu = false;
                            startSystem();
                        }
                        default -> System.out.println("Invalid input");
                    }
                }
            }
    }

    private void chooseCompany() {
        System.out.println();
        System.out.println("Choose the company:");
        printCompanyList();
        if (company.getCompanyList().isEmpty()) {
            backToManager();
        } else {
            int id = scanner.nextInt();
            if (id == 0) {
                backToManager();
            } else {
                company.setId(id);
                company.setName(companyDAO.readNameByID(id));
                car.setCarList(carDAO.readAllByCOMPANY_ID(id));
                loginToCompany();
            }
        }
    }

    private void chooseCompanyAsCustomer() {
        System.out.println();
        System.out.println("Choose the company:");
        printCompanyList();
        if (company.getCompanyList().isEmpty()) {
            backToManager();
        } else {
            int id = scanner.nextInt();
            if (id == 0) {
                backToCustomer();
            } else {
                company.setId(id);
                company.setName(companyDAO.readNameByID(id));
                car.setCarList(carDAO.readAllByCOMPANY_ID(id));
                chooseCar();
            }
        }
    }

    private void chooseCar() {
        System.out.println();
        System.out.println("Choose car from '" + company.getName() + "':");

        car.setAvailableCarList(carDAO.readAllAvailableByCOMPANY_ID(company.getId()));
        printAvailableCarList();

        int id = scanner.nextInt();
        if (id == 0) {
            backToCustomer();
        } else {
            customerDAO.updateRentedCar(id, customer.getName());
            customer.setRented_car_id(id);
            System.out.println("You rented '" + carDAO.readNameByID(id) + "'");
            backToCustomer();
        }
    }

    private void chooseCustomer() {
        System.out.println();
        System.out.println("Choose the customer:");
        printCustomerList();
        if (customer.getCustomerList().isEmpty()) {
            System.out.println("The customer list is empty!");
            startSystem();
        } else {
            int id = scanner.nextInt();
            if (id == 0) {
                startSystem();
            } else {
                customer.setId(id);
                customer.setName(customerDAO.readNameByID(id));
                customer.setRented_car_id(customerDAO.readRentedCarID(customer.getName()));
            }
        }
    }

    private void createCompany() {
        System.out.println();
        System.out.println("Enter the company name:");
        scanner.nextLine();
        companyDAO.createCompany(scanner.nextLine());
        company.setCompanyList(companyDAO.readAllIDAndNAME());
        System.out.println("Company created!");
        loginAsManager();
    }

    private void createCar() {
        System.out.println();
        System.out.println("Enter the car name:");
        scanner.nextLine();
        carDAO.createCar(scanner.nextLine(), company.getId());
        car.setCarList(carDAO.readAllByCOMPANY_ID(company.getId()));
        System.out.println("Car created!");
        loginToCompany();
    }

    private void createCustomer() {
        System.out.println();
        System.out.println("Enter the customer name:");
        scanner.nextLine();
        customerDAO.createCustomer(scanner.nextLine());
        customer.setCustomerList(customerDAO.readAllIDAndNAME());
        System.out.println("Customer created!");
        startSystem();
    }

    private void rentCar() {

        if (customer.getRented_car_id() != 0) {
            System.out.println("You've already rented a car!");
        } else {
            chooseCompanyAsCustomer();

            if (company.getCompanyList().isEmpty()) {
                System.out.println("Company list is empty!");
                backToCustomer();
            } else {

                if (car.getCarList().isEmpty()) {
                    System.out.println("No cars available!");
                } else {
                    chooseCar();
                }
            }
        }
    }

    private void returnCar()  {
        if (customerDAO.readRentedCarID(customer.getName()) == 0) {
            System.out.println("You didn't rent a car!");
        } else {
            customerDAO.deletedRentedCar(customer.getName());
            System.out.println("You've returned a rented car!");
        }
    }

    private void rentedCar() {
        System.out.println();
        int rentedCarID = customerDAO.readRentedCarID(customer.getName());
        if (rentedCarID == 0) {
            System.out.println("You didn't rent a car!");
        } else {
            System.out.println("Your rented car:");
            System.out.println(carDAO.readNameByID(rentedCarID));
            System.out.println("Company:");
            System.out.println(company.getName());
        }
    }

    private void printCompanyList() {
        if (company.getCompanyList().isEmpty()) {
            System.out.println("The company list is empty!");
        } else {
            company.getCompanyList()
                    .forEach((key, value) ->
                            System.out.println(key + ". " + value));
            System.out.println("0. Back");
        }
    }

    private void printCarList() {
        if (car.getCarList().isEmpty()) {
            System.out.println("The car list is empty!");
            System.out.println();
        } else {
            car.getCarList()
                    .forEach((key, value) ->
                            System.out.println(key + ". " + value));
            System.out.println();
        }
    }
    private void printAvailableCarList() {
        if (car.getAvailableCarList().isEmpty()) {
            System.out.println("The car list is empty!");
            System.out.println();
        } else {
            car.getAvailableCarList()
                    .forEach((key, value) ->
                            System.out.println(key + ". " + value));
            System.out.println();
        }
    }
    private void printCustomerList() {
        if (customer.getCustomerList().isEmpty()) {
            System.out.println();
        } else {
            customer.getCustomerList()
                    .forEach((key, value) ->
                            System.out.println(key + ". " + value));
            System.out.println("0. Back");
            System.out.println();
        }
    }

    private void backToManager() {
        loginAsManager();
    }

    private void backToCustomer() {
        loginAsCustomer();
    }


}
