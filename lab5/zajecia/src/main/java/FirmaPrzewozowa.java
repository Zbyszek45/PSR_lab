import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.*;

import static com.mongodb.client.model.Filters.*;

public class FirmaPrzewozowa {
    private static Scanner in = new Scanner( System.in );
    private static int idGen = 0;

    public static void main(String[] args) {
        String user = "student01";
        String password = "student01";
        String host = "localhost";
        int port = 27017;
        String database = "database01";

        String clientURI = "mongodb://" + user + ":" + password + "@" + host + ":" + port + "/" + database;
        MongoClientURI uri = new MongoClientURI(clientURI);

        MongoClient mongoClient = new MongoClient(uri);

        MongoDatabase db = mongoClient.getDatabase(database);

        db.getCollection("car").drop();

        MongoCollection<Document> collection = db.getCollection("car");

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
                mongoClient.close();
                break;
            }
            else if (choice == 1){
                add(collection);
            }
            else if (choice == 2){
                delete(collection);
            }
            else if (choice == 3){
                update(collection);
            }
            else if (choice == 4){
                get_hc(collection);
            }
            else {
                continue;
            }
        }
    }

    private static void get_hc(MongoCollection<Document> collection) {
        int choice = -2;
        while (choice != 0){
            System.out.println("Wybierz:");
            System.out.println("0. COFNIJ");
            System.out.println("1. Wyświetl wszystkie auta...");
            System.out.println("2. Znajdź auto po id...");
            System.out.println("3. Znajdź auto po marce...");
            choice = Integer.parseInt(in.nextLine());
            if (choice == 0){
                return;
            }
            else if (choice == 1){
                for (Document doc : collection.find())
                    System.out.println("find() " + doc.toJson());
            }
            else if (choice == 2){
                System.out.println("Podaj ID: ");
                int number = Integer.parseInt(in.nextLine());

                Document myDoc = collection.find(eq("_id", number)).first();
                System.out.println(myDoc.toJson());
            }
            else if (choice == 3){
                System.out.println("Podaj marke: ");
                String name = in.nextLine();

                for (Document d : collection.find(eq("marka", name)))
                    System.out.println(d.toJson());
            }
            else {
                continue;
            }
        }
    }

    private static void update(MongoCollection<Document> collection) {
        System.out.println("Podaj ID: ");
        int id = Integer.parseInt(in.nextLine());
        System.out.println("Podaj marke: ");
        String name = in.nextLine();
        System.out.println("Podaj przebieg: ");
        int number = Integer.parseInt(in.nextLine());
        collection.updateOne(eq("_id", id), new Document("$set", new Document("marka", name).append("przebieg", number)));
    }

    private static void delete(MongoCollection<Document> collection) {
        System.out.println("Podaj ID: ");
        int number = Integer.parseInt(in.nextLine());
        collection.deleteOne(eq("_id", number));
    }

    private static void add(MongoCollection<Document> collection) {
        System.out.println("Podaj marke: ");
        String name = in.nextLine();
        System.out.println("Podaj przebieg: ");
        int number = Integer.parseInt(in.nextLine());
        Document newD = new Document("_id", idGen)
                .append("marka", name)
                .append("przebieg", number);
        collection.insertOne(newD);
        idGen++;
    }
}
