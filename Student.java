import java.util.List;

public class Student {
    // Declare the attributes of the Student class
    private String ID;
    private String FirstName;
    private String LastName;
    private String Gender;
    private Double GPA;
    private Integer Level;
    private String Address;

    // Default constructor
    public Student() {
        this.ID = "";
        this.FirstName = "";
        this.LastName = "";
        this.Gender = "";
        this.GPA = 0.0;
        this.Level = 0;
        this.Address = "";
    }

    // Parameterized constructors
    public Student(String ID, String FirstName, String LastName, String Gender, Double GPA, Integer Level, String Address) {
        this.ID = ID;
        this.FirstName = FirstName;
        this.LastName = LastName;
        this.Gender = Gender;
        this.GPA = GPA;
        this.Level = Level;
        this.Address = Address;
    }

    public Student(List<String> studentData) {
        this.ID = studentData.get(0);
        this.FirstName = studentData.get(1);
        this.LastName = studentData.get(2);
        this.Gender = studentData.get(3);
        this.GPA = Double.parseDouble(studentData.get(4));
        this.Level = Integer.parseInt(studentData.get(5));
        this.Address = studentData.get(6);
    }

    // Getters
    public String getID() {
        return ID;
    }

    public String getFirstName() {
        return FirstName;
    }

    public String getLastName() {
        return LastName;
    }

    public String getGender() {
        return Gender;
    }

    public Double getGPA() {
        return GPA;
    }

    public Integer getLevel() {
        return Level;
    }

    public String getAddress() {
        return Address;
    }

    // Setters
    public void setID(String ID) {
        this.ID = ID;
    }

    public void setFirstName(String FirstName) {
        this.FirstName = FirstName;
    }

    public void setLastName(String LastName) {
        this.LastName = LastName;
    }

    public void setGender(String Gender) {
        this.Gender = Gender;
    }

    public void setGPA(Double GPA) {
        this.GPA = GPA;
    }

    public void setLevel(Integer Level) {
        this.Level = Level;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    // Override the toString() method to return a string representation of the Student object
    @Override
    public String toString() {
        return "<" + ID + ", " + FirstName + ", " + LastName + ", " + Gender + ", " + GPA + ", " + Level + ", " + Address + ">";
    }
}