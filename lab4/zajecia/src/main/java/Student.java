import java.io.Serializable;

public class Student implements Serializable {
    private String name;
    private String surname;
    private int index_number;

    public Student(String name, String surname, int index_number) {
        this.name = name;
        this.surname = surname;
        this.index_number = index_number;
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", index_number=" + index_number +
                '}';
    }
}
