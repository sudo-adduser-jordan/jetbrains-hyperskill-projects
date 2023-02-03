package contacts.contact;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public abstract class Contact implements Serializable {
    protected String name;
    protected String number;
    protected String timeCreated;
    protected String timeLastEdit;
    protected transient Scanner scanner;

    Contact() {
        scanner = new Scanner(System.in);
        timeCreated = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        timeLastEdit = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    public void setName() {
        System.out.print("Enter the name: ");
        this.name = scanner.nextLine();
    }

    public String getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    protected Field[] getEditableFields() throws NoSuchFieldException {
        List<Field> fields = new ArrayList<>(List.of(this.getClass().getDeclaredFields()));
        fields.add(0, this.getClass().getSuperclass().getDeclaredField("name"));
        fields.add(this.getClass().getSuperclass().getDeclaredField("number"));
        Field[] fields1 = new Field[fields.size()];
        return fields.toArray(fields1);
    }


    public abstract String getFullName();

    public void edit() throws NoSuchFieldException, InvocationTargetException, IllegalAccessException {
        List<String> fields = Arrays.stream(getEditableFields())
                .map(Field::getName)
                .collect(Collectors.toList());
        String stringFields = String.join(", ", fields);
        System.out.print("Select a field (" + stringFields.toLowerCase() + "): ");
        String field = scanner.nextLine().toLowerCase();

        if (fields.contains(field)) {
            this.setMethod(field);
            timeLastEdit = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            System.out.println("Saved");
        }
    }

    private Method[] getThisSuperClassMethods() {
        List<Method> methods = new ArrayList<>(List.of(this.getClass().getMethods()));
        methods.addAll(Arrays.asList(this.getClass().getSuperclass().getMethods()));
        Method[] method1 = new Method[methods.size()];
        return methods.toArray(method1);
    }

    protected void setMethod(String input) throws InvocationTargetException, IllegalAccessException {
        Method setMethod = Arrays.stream(getThisSuperClassMethods())
                .filter(method -> method.getName().toLowerCase().equals("set" + input))
                .findFirst()
                .orElse(null);
        assert setMethod != null;
        setMethod.invoke(this);
    }

    protected boolean validateNumber(String number) {
        Pattern pattern = Pattern.compile("\\+?" +
                "(\\w+[-\\s]\\(\\w{2,}\\)" +
                "|\\(\\w+\\)[-\\s]\\w{2,}" +
                "|\\w+" +
                "|\\(\\w+\\))" +
                "([-\\s]\\w{2,})*");
        Matcher matcher = pattern.matcher(number);
        return matcher.matches();
    }

    public void setNumber() {
        System.out.print("Enter the number: ");
        String number = scanner.nextLine();
        if (validateNumber(number)) {
            this.number = number;
            return;
        }
        System.out.println("Wrong number format!");
        this.number = "";
    }


}
