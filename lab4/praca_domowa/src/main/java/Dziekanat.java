import com.arangodb.ArangoCollection;
import com.arangodb.ArangoCursor;
import com.arangodb.ArangoDB;
import com.arangodb.ArangoDBException;
import com.arangodb.entity.BaseDocument;
import com.arangodb.entity.CollectionEntity;
import com.arangodb.entity.IndexEntity;
import com.arangodb.internal.util.DefaultArangoSerialization;
import com.arangodb.util.MapBuilder;
import com.arangodb.velocypack.VPackSlice;
import org.apache.log4j.BasicConfigurator;

import java.util.Collection;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

public class Dziekanat {
    private static Scanner in = new Scanner( System.in );
    final static String dbName = "mydb";
    final static String studenci = "studenci";
    final static String wydzialy = "wydzialy";

    public static void main(String[] args) {
        BasicConfigurator.configure();
        final ArangoDB arangoDB = new ArangoDB.Builder().user("root").password("myszka312").build();

        // create database
        try {
            arangoDB.createDatabase(dbName);
            System.out.println("Database created: " + dbName);
        } catch (final ArangoDBException e) {
            System.err.println("Failed to create database: " + dbName + "; " + e.getMessage());
        }

        try {
            final CollectionEntity myArangoCollection = arangoDB.db(dbName).createCollection(studenci);
            System.out.println("Collection created: " + myArangoCollection.getName());
        } catch (final ArangoDBException e) {
            System.err.println("Failed to create collection: " + studenci + "; " + e.getMessage());
        }

        try {
            final CollectionEntity myArangoCollection = arangoDB.db(dbName).createCollection(wydzialy);
            System.out.println("Collection created: " + myArangoCollection.getName());
        } catch (final ArangoDBException e) {
            System.err.println("Failed to create collection: " + wydzialy + "; " + e.getMessage());
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
                break;
            }
            else if (choice == 1){
                add(arangoDB);
            }
            else if (choice == 2){
                delete(arangoDB);
            }
            else if (choice == 3){
                update(arangoDB);
            }
            else if (choice == 4){
                get_ar(arangoDB);
            }
            else {
                continue;
            }
        }
    }

    private static void get_ar(ArangoDB arangoDB) {
        int choice = -2;
        while (choice != 0){
            System.out.println("Wybierz:");
            System.out.println("0. COFNIJ");
            System.out.println("1. Znajdź studenta po id...");
            choice = Integer.parseInt(in.nextLine());
            if (choice == 0){
                return;
            }
            else if (choice == 1){
                System.out.println("Podaj ID: ");
                String id = in.nextLine();
                try {
                    final BaseDocument student = arangoDB.db(dbName).collection(studenci).getDocument(id,
                            BaseDocument.class);
                    System.out.println("Key: " + student.getKey());
                    System.out.println("Imie: " + student.getAttribute("name"));
                    System.out.println("Nazwisko: " + student.getAttribute("surname"));
                    System.out.println("Numer indeksu: " + student.getAttribute("index"));
                } catch (final ArangoDBException e) {
                    System.err.println("Failed to get document: myKey; " + e.getMessage());
                }
            }
            else {
                continue;
            }
        }
    }

    private static void update(ArangoDB arangoDB) {
        int choice = -2;
        while (choice != 0){
            System.out.println("Wybierz:");
            System.out.println("0. COFNIJ");
            System.out.println("1. Student...");
            choice = Integer.parseInt(in.nextLine());
            if (choice == 0){
                return;
            }
            else if (choice == 1){
                System.out.println("Podaj ID: ");
                String id = in.nextLine();
                System.out.println("Podaj imie: ");
                String name = in.nextLine();
                System.out.println("Podaj nazwisko: ");
                String surname = in.nextLine();
                System.out.println("Podaj numer indeksu: ");
                int index = choice = Integer.parseInt(in.nextLine());
                try {
                    final BaseDocument student = arangoDB.db(dbName).collection(studenci).getDocument(id,
                            BaseDocument.class);
                    final BaseDocument myObject = new BaseDocument();
                    myObject.addAttribute("name", name);
                    myObject.addAttribute("surname", surname);
                    myObject.addAttribute("index", index);
                    arangoDB.db(dbName).collection(studenci).updateDocument(id, myObject);
                } catch (final ArangoDBException e) {
                    System.err.println("Failed to get document: myKey; " + e.getMessage());
                }
            }
            else {
                continue;
            }
        }
    }

    private static void delete(ArangoDB arangoDB) {
        int choice = -2;
        while (choice != 0){
            System.out.println("Wybierz:");
            System.out.println("0. COFNIJ");
            System.out.println("1. Student...");
            choice = Integer.parseInt(in.nextLine());
            if (choice == 0){
                return;
            }
            else if (choice == 1){
                System.out.println("Podaj ID: ");
                String id = in.nextLine();
                try {
                    arangoDB.db(dbName).collection(studenci).deleteDocument(id);
                } catch (final ArangoDBException e) {
                    System.err.println("Failed to delete document. " + e.getMessage());
                }
            }
            else {
                continue;
            }
        }
    }

    private static void add(ArangoDB arangoDB) {
        int choice = -2;
        while (choice != 0){
            System.out.println("Wybierz:");
            System.out.println("0. COFNIJ");
            System.out.println("1. Student...");
            choice = Integer.parseInt(in.nextLine());
            if (choice == 0){
                return;
            }
            else if (choice == 1){
                System.out.println("Podaj imie: ");
                String name = in.nextLine();
                System.out.println("Podaj nazwisko: ");
                String surname = in.nextLine();
                System.out.println("Podaj numer indeksu: ");
                int index = choice = Integer.parseInt(in.nextLine());
                System.out.println("Dodawanie: "+name+" "+surname+" "+index);

                String uuid_s = UUID.randomUUID().toString();
                System.out.println(uuid_s);
                final BaseDocument myObject = new BaseDocument();
                myObject.setKey(uuid_s);
                myObject.addAttribute("name", name);
                myObject.addAttribute("surname", surname);
                myObject.addAttribute("index", index);
                try {
                    arangoDB.db(dbName).collection(studenci).insertDocument(myObject);
                    System.out.println("Document created");
                } catch (final ArangoDBException e) {
                    System.err.println("Failed to create document. " + e.getMessage());
                }
            }
            else {
                continue;
            }
        }
    }
}
