import com.rethinkdb.RethinkDB;
import com.rethinkdb.gen.exc.ReqlError;
import com.rethinkdb.gen.exc.ReqlQueryLogicError;
import com.rethinkdb.model.MapObject;
import com.rethinkdb.net.Connection;
import com.rethinkdb.net.Result;

import java.awt.*;
import java.util.Scanner;

import static java.lang.System.err;

public class FirmaPrzewozoa {
    public static final RethinkDB r = RethinkDB.r;

    private static Scanner in = new Scanner( System.in );
    private static int idGen = 0;

    public static void main(String[] args) {
        Connection conn = r.connection().hostname("localhost").port(28015).connect();

        r.db("test").tableDrop("car").run(conn);
        r.db("test").tableCreate("car").run(conn);
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
                r.db("test").tableDrop("car").run(conn);
                break;
            }
            else if (choice == 1){
                add(conn);
            }
            else if (choice == 2){
                delete(conn);
            }
            else if (choice == 3){
                update(conn);
            }
            else if (choice == 4){
                get_hc(conn);
            }
            else {
                continue;
            }
        }
    }

    private static void get_hc(Connection conn) {
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
                Result<Object> cursor = r.table("car").run(conn);
                for (Object doc : cursor) {
                    System.out.println(doc);
                }

            }
            else if (choice == 2){
                System.out.println("Podaj ID: ");
                String id = in.nextLine();
                Object doc = r.db("test").table("car").get(id).run(conn);
                System.out.println(doc);

            }
            else if (choice == 3){
                System.out.println("Podaj marke: ");
                String name = in.nextLine();

                Result<Object> cursor = r.table("car").filter(row -> row.g("marka").eq(name)).run(conn);
                for (Object doc : cursor) {
                    System.out.println(doc);
                }

            }
            else {
                continue;
            }
        }
    }

    private static void update(Connection conn) {
        System.out.println("Podaj ID: ");
        String id = in.nextLine();
        System.out.println("Podaj marke: ");
        String name = in.nextLine();
        System.out.println("Podaj przebieg: ");
        int number = Integer.parseInt(in.nextLine());
        r.table("car").get(id).update(r.hashMap("marka", name)
                .with("przebieg", number)
        ).run(conn);

    }

    private static void delete(Connection conn) {
        System.out.println("Podaj ID: ");
        String id = in.nextLine();
        r.table("car").get(id).delete().run(conn);
    }

    private static void add(Connection conn) {
        System.out.println("Podaj marke: ");
        String name = in.nextLine();
        System.out.println("Podaj przebieg: ");
        int number = Integer.parseInt(in.nextLine());

        r.table("car").insert(
                r.hashMap("marka", name)
                        .with("przebieg", number)
                        ).run(conn);

        idGen++;
    }
}
