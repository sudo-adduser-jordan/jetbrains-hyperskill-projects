package contacts.contact;




import java.time.LocalDate;


public class Person extends Contact {
    private String surname;
    private String birthDate;
    private String gender;

    public Person() {
        super();
        setName();
        setSurname();
        setBirthDate();
        setGender();
        setNumber();
    }


    public void setSurname() {
        System.out.print("Enter the surname: ");
        this.surname = scanner.nextLine();
    }

    public void setGender() {
        System.out.print("Enter the gender: ");
        String gender = scanner.nextLine();
        if (!"M".equals(gender) && !"F".equals(gender)) {
            System.out.println("Bad gender!");
            this.gender = "";
            return;
        }
        this.gender = gender;
    }

    public void setBirthDate() {
        System.out.print("Enter the birth: ");
        String birthDate = scanner.nextLine();

        try {
            LocalDate localDate = LocalDate.parse(birthDate);
            this.birthDate = localDate.toString();
        } catch (Exception e) {
            System.out.println("Bad birth date!");
            this.birthDate = "";
        }
    }

    @Override
    public String getFullName() {
        return name + " " + surname;
    }


    @Override
    public String toString() {
        return "Name: " + name + "\n" +
                "Surname: " + surname + "\n" +
                "Birth date: " + (birthDate.isEmpty() ? "[no data]" : birthDate) + "\n" +
                "Gender: " + (gender.isEmpty() ? "[no data]" : gender) + "\n" +
                "Number: " + (number.isEmpty() ? "[no number]" : number) + "\n" +
                "Time created: " + timeCreated + "\n" +
                "Time last edit: " + timeLastEdit;
    }
}
