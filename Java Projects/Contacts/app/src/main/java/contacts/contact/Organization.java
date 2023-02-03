package contacts.contact;


public class Organization extends Contact {
    private String address;

    public Organization() {
        super();
        setName();
        setAddress();
        setNumber();
    }

    public void setAddress() {
        System.out.print("Enter the address: ");
        this.address = scanner.nextLine();
    }

    @Override
    public String getFullName() {
        return name;
    }

    @Override
    public String toString() {
        return "Organization name: " + name + "\n" +
                "Address: " + address + "\n" +
                "Number: " + (number.isEmpty() ? "[no number]" : number) + "\n" +
                "Time created: " + timeCreated + "\n" +
                "Time last edit: " + timeLastEdit;
    }


}
