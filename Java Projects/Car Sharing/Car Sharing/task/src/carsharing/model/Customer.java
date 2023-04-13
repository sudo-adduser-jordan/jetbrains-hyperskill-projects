package carsharing.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@Data @AllArgsConstructor @NoArgsConstructor
public class Customer {
    private int id;
    private String name;
    private int rented_car_id;
    private HashMap<Integer, String> customerList;
}
