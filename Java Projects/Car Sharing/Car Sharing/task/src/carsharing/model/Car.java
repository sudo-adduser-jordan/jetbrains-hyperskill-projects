package carsharing.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@Data @AllArgsConstructor @NoArgsConstructor
public class Car {
    private int id;
    private String name;
    private int company_id;
    private HashMap<Integer, String> carList;
    private HashMap<Integer, String> availableCarList;
}
