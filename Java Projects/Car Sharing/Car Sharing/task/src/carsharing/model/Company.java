package carsharing.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;


@Data @AllArgsConstructor @NoArgsConstructor
public class Company {
    private int id;
    private String name;
    private HashMap<Integer, String> companyList;
}
