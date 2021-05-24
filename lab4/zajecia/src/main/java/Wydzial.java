import java.io.Serializable;

public class Wydzial implements Serializable {
    private String name;

    public Wydzial(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Wydzial{" +
                "name='" + name + '\'' +
                '}';
    }
}
