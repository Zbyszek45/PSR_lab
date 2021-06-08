import com.datastax.oss.driver.api.core.CqlSession;
import java.sql.SQLOutput;
import java.util.Collection;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

public class Library {
    private static Scanner in = new Scanner( System.in );
    private static int idGen = 0;

    public static void main(String[] args) {
        try (CqlSession session = CqlSession.builder().build()) {
            KeyspaceManager keyspaceManager = new KeyspaceManager(session, "library");
            keyspaceManager.dropKeyspace();
            keyspaceManager.selectKeyspaces();
            keyspaceManager.createKeyspace();
            keyspaceManager.useKeyspace();

            BookTableManager bookManager = new BookTableManager(session);
            bookManager.createTable();
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
                    bookManager.dropTable();
                    break;
                }
                else if (choice == 1){
                    add(bookManager);
                }
                else if (choice == 2){
                    delete(bookManager);
                }
                else if (choice == 3){
                    update(bookManager);
                }
                else if (choice == 4){
                    get_cs(bookManager);
                }
                else {
                    continue;
                }
            }
        }
    }

    private static void get_cs(BookTableManager bookManager) {
        int choice = -2;
        while (choice != 0){
            System.out.println("Wybierz:");
            System.out.println("0. COFNIJ");
            System.out.println("1. Wyświetl wszystkie książki...");
            System.out.println("2. Znajdź książke po id...");
            System.out.println("3. Znajdź książki po autorze...");
            choice = Integer.parseInt(in.nextLine());
            if (choice == 0){
                return;
            }
            else if (choice == 1){
                bookManager.selectFromTable();
            }
            else if (choice == 2){
                System.out.println("Podaj ID: ");
                int id = Integer.parseInt(in.nextLine());
                bookManager.selectFromTable(id);
            }
            else if (choice == 3){
                System.out.println("Podaj autora: ");
                String author = in.nextLine();
                bookManager.selectFromTable(author);
            }
            else {
                continue;
            }
        }
    }

    private static void update(BookTableManager bookManager) {

                System.out.println("Podaj ID: ");
                String id = in.nextLine();
                System.out.println("Podaj tytuł: ");
                String title = in.nextLine();
                System.out.println("Podaj autora: ");
                String author = in.nextLine();
                System.out.println("Podaj numer isbn: ");
                int isbn = Integer.parseInt(in.nextLine());
    bookManager.updateIntoTable(id, title, author, isbn);
    }

    private static void delete(BookTableManager bookManager) {
        System.out.println("Podaj ID: ");
        int id = Integer.parseInt(in.nextLine());
        bookManager.deleteFromTable(id);
    }

    private static void add(BookTableManager bookManager) {
        int choice = -2;
        while (choice != 0){
            System.out.println("Wybierz:");
            System.out.println("0. COFNIJ");
            System.out.println("1. Książka...");
            choice = Integer.parseInt(in.nextLine());
            if (choice == 0){
                return;
            }
            else if (choice == 1){
                System.out.println("Podaj tytuł: ");
                String title = in.nextLine();
                System.out.println("Podaj autora: ");
                String author = in.nextLine();
                System.out.println("Podaj numer isbn: ");
                int isbn = choice = Integer.parseInt(in.nextLine());
                bookManager.insertIntoTable(idGen, title, author, isbn);
                idGen++;
            }
            else {
                continue;
            }
        }
    }
}
