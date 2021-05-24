import com.hazelcast.client.HazelcastClient;
import com.hazelcast.cluster.MembershipEvent;
import com.hazelcast.cluster.MembershipListener;
import com.hazelcast.core.*;
import com.hazelcast.map.IMap;
import com.hazelcast.map.listener.EntryAddedListener;
import com.hazelcast.partition.MigrationListener;
import com.hazelcast.partition.MigrationState;
import com.hazelcast.partition.PartitionService;
import com.hazelcast.partition.ReplicaMigrationEvent;
import com.hazelcast.query.Predicate;
import com.hazelcast.query.Predicates;

import java.net.UnknownHostException;
import java.util.Collection;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

public class Dziekanat {
    private static Scanner in = new Scanner( System.in );

    public static void main(String[] args) throws UnknownHostException {
        // kubełki: student, wydzial
        HazelcastInstance hc = Hazelcast.newHazelcastInstance();

        IMap<UUID, Student> studenci = hc.getMap("studenci");
        IMap<UUID, Wydzial> wydzialy = hc.getMap("wydzialy");

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
                hc.shutdown();
                break;
            }
            else if (choice == 1){
                add(studenci, wydzialy);
            }
            else if (choice == 2){
                delete(studenci, wydzialy);
            }
            else if (choice == 3){
                update(studenci, wydzialy);
            }
            else if (choice == 4){
                get_hc(studenci, wydzialy);
            }
            else {
                continue;
            }
        }
    }

    private static void get_hc(IMap<UUID,Student> studenci, IMap<UUID,Wydzial> wydzialy) {
        int choice = -2;
        while (choice != 0){
            System.out.println("Wybierz:");
            System.out.println("0. COFNIJ");
            System.out.println("1. Wyświetl wszystkich studentów...");
            System.out.println("2. Znajdź studenta po id...");
            System.out.println("3. Znajdź studenta po nazwisku...");
            System.out.println("4. Wyświetl wszystkie wydziały...");
            System.out.println("5. Znajdź wydział po id...");
            System.out.println("6. Znajdź wydział po nazwie...");
            choice = Integer.parseInt(in.nextLine());
            if (choice == 0){
                return;
            }
            else if (choice == 1){
                for(Map.Entry<UUID, Student> e : studenci.entrySet()){
                    System.out.println(e.getKey() + " => " + e.getValue());
                }
            }
            else if (choice == 2){
                System.out.println("Podaj ID: ");
                String id = in.nextLine();
                Student s = studenci.get(UUID.fromString(id));
                System.out.println(s);
            }
            else if (choice == 3){
                System.out.println("Podaj nazwisko: ");
                String surname = in.nextLine();

                Predicate<?,?> namePredicate = Predicates.equal( "surname", surname);

                Collection<Student> s = studenci.values(Predicates.and(namePredicate));
                for (Student st : s) {
                    System.out.println(st);
                }
            }
            else if (choice == 4){
                for(Map.Entry<UUID, Wydzial> e : wydzialy.entrySet()){
                    System.out.println(e.getKey() + " => " + e.getValue());
                }
            }
            else if (choice == 5){
                System.out.println("Podaj ID: ");
                String id = in.nextLine();
                Wydzial s = wydzialy.get(UUID.fromString(id));
                System.out.println(s);
            }
            else if (choice == 6){
                System.out.println("Podaj nazwe: ");
                String name = in.nextLine();

                Predicate<?,?> namePredicate = Predicates.equal( "name", name);

                Collection<Wydzial> w = wydzialy.values(Predicates.and(namePredicate));
                for (Wydzial wy : w) {
                    System.out.println(wy);
                }
            }
            else {
                continue;
            }
        }
    }

    private static void update(IMap<UUID, Student> studenci, IMap<UUID, Wydzial> wydzialy) {
        int choice = -2;
        while (choice != 0){
            System.out.println("Wybierz:");
            System.out.println("0. COFNIJ");
            System.out.println("1. Student...");
            System.out.println("2. Wydział...");
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
                studenci.replace(UUID.fromString(id), new Student(name, surname, index));
            }
            else if (choice == 2){
                System.out.println("Podaj ID: ");
                String id = in.nextLine();
                System.out.println("Podaj nazwe: ");
                String name = in.nextLine();
                wydzialy.replace(UUID.fromString(id), new Wydzial(name));
            }
            else {
                continue;
            }
        }
    }

    private static void delete(IMap<UUID, Student> studenci, IMap<UUID, Wydzial> wydzialy) {
        int choice = -2;
        while (choice != 0){
            System.out.println("Wybierz:");
            System.out.println("0. COFNIJ");
            System.out.println("1. Student...");
            System.out.println("2. Wydział...");
            choice = Integer.parseInt(in.nextLine());
            if (choice == 0){
                return;
            }
            else if (choice == 1){
                System.out.println("Podaj ID: ");
                String id = in.nextLine();
                studenci.remove(UUID.fromString(id));
            }
            else if (choice == 2){
                System.out.println("Podaj ID: ");
                String id = in.nextLine();
                wydzialy.remove(UUID.fromString(id));
            }
            else {
                continue;
            }
        }
    }

    private static void add(IMap<UUID, Student> studenci, IMap<UUID, Wydzial> wydzialy) {
        int choice = -2;
        while (choice != 0){
            System.out.println("Wybierz:");
            System.out.println("0. COFNIJ");
            System.out.println("1. Student...");
            System.out.println("2. Wydział...");
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
                studenci.put(UUID.randomUUID(), new Student(name, surname, index));
            }
            else if (choice == 2){
                System.out.println("Podaj nawe wydziału: ");
                String name = in.nextLine();
                System.out.println("Dodanie wydziału: "+name);
                wydzialy.put(UUID.randomUUID(), new Wydzial(name));
            }
            else {
                continue;
            }
        }
    }
}
