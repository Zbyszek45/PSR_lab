import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.db.OrientDB;
import com.orientechnologies.orient.core.db.OrientDBConfig;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.orientechnologies.orient.core.record.OVertex;
import com.orientechnologies.orient.core.sql.executor.OResult;
import com.orientechnologies.orient.core.sql.executor.OResultSet;

import java.util.Scanner;

public class Zoo {
    private static Scanner in = new Scanner( System.in );

    public static void main(String[] args) {
        OrientDB orient = new OrientDB("remote:localhost", OrientDBConfig.defaultConfig());

        ODatabaseSession db = orient.open("test", "test", "test");

        OClass zwierze = db.getClass("Zwierze");

        if (zwierze == null) {
            zwierze = db.createVertexClass("Zwierze");
        }

        if (zwierze.getProperty("rasa") == null) {
            zwierze.createProperty("rasa", OType.STRING);
            zwierze.createIndex("Zwierze_rasa_index", OClass.INDEX_TYPE.NOTUNIQUE, "rasa");
        }
        if (zwierze.getProperty("imie") == null) {
            zwierze.createProperty("imie", OType.STRING);
            zwierze.createIndex("Zwierze_imie_index", OClass.INDEX_TYPE.NOTUNIQUE, "imie");
        }

        int choice = -2;
        while (choice != 0){
            System.out.println("Wybierz:");
            System.out.println("0. ZAKONCZ");
            System.out.println("1. Dodaj...");
            System.out.println("2. Kasuj...");
            System.out.println("3. Aktualizacja...");
            System.out.println("4. Pobierz...");
            choice = Integer.parseInt(in.nextLine());
            if (choice == 0){
                db.close();
                orient.close();
                break;
            }
            else if (choice == 1){
                add(db);
            }
            else if (choice == 2){
                delete(db);
            }
            else if (choice == 3){
                update(db);
            }
            else if (choice == 4){
                get_hc(db);
            }
            else {
                continue;
            }
        }
    }

    private static void get_hc(ODatabaseSession db) {
        int choice = -2;
        while (choice != 0){
            System.out.println("Wybierz:");
            System.out.println("0. COFNIJ");
            System.out.println("1. Wyświetl wszystkie zwierzęta...");
            System.out.println("2. Znajdź zwierze po imieniu...");
            System.out.println("3. Znajdź zwierze po rasie...");
            choice = Integer.parseInt(in.nextLine());
            if (choice == 0){
                return;
            }
            else if (choice == 1){
                String query = "SELECT * from Zwierze";
                OResultSet rs = db.query(query);

                while (rs.hasNext()) {
                    OResult item = rs.next();
                    System.out.println("Zwierze: " + item.getProperty("imie") + " rasy: " + item.getProperty("rasa"));
                }
                rs.close();
            }
            else if (choice == 2){
                System.out.println("Podaj imie: ");
                String name = in.nextLine();

                String query = "SELECT * from Zwierze where imie = ?";
                OResultSet rs = db.query(query, name);

                while (rs.hasNext()) {
                    OResult item = rs.next();
                    System.out.println("Zwierze: " + item.getProperty("imie") + " rasy: " + item.getProperty("rasa"));
                }
                rs.close();
            }
            else if (choice == 3){
                System.out.println("Podaj rase: ");
                String name = in.nextLine();

                String query = "SELECT * from Zwierze where rasa = ?";
                OResultSet rs = db.query(query, name);

                while (rs.hasNext()) {
                    OResult item = rs.next();
                    System.out.println("Zwierze: " + item.getProperty("imie") + " rasy: " + item.getProperty("rasa"));
                }
                rs.close();
            }
            else {
                continue;
            }
        }
    }

    private static void update(ODatabaseSession db) {
        System.out.println("Podaj imie: ");
        String imie = in.nextLine();
        System.out.println("Podaj nowe imie: ");
        String new_imie = in.nextLine();
        System.out.println("Podaj rase: ");
        String rasa = in.nextLine();

        OResultSet rs = db.command("DELETE VERTEX Zwierze where imie = ?", imie);
        rs.close();
        createZwierze(db, rasa, new_imie);
    }

    private static void delete(ODatabaseSession db) {
        System.out.println("Podaj imie: ");
        String name = in.nextLine();

        OResultSet rs = db.command("DELETE VERTEX Zwierze where imie = ?", name);

        rs.close();
    }

    private static void add(ODatabaseSession db) {
        System.out.println("Podaj imie: ");
        String imie = in.nextLine();
        System.out.println("Podaj rase: ");
        String rasa = in.nextLine();
        createZwierze(db, rasa, imie);
    }

    private static OVertex createZwierze(ODatabaseSession db, String rasa, String imie) {
        OVertex result = db.newVertex("Zwierze");
        result.setProperty("rasa", rasa);
        result.setProperty("imie", imie);
        result.save();
        return result;
    }
}
