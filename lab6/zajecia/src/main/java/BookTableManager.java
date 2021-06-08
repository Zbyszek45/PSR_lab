import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.data.UdtValue;

public class BookTableManager extends SimpleManager {

    public BookTableManager(CqlSession session) {
        super(session);
    }

    public void createTable() {
        executeSimpleStatement(
                "CREATE TABLE book (\n" +
                        "    id int PRIMARY KEY,\n" +
                        "    title text,\n" +
                        "    author text,\n" +
                        "    isbn int,\n" +
                        ");");
    }

    public void insertIntoTable(int id, String title, String author, int isbn) {
        StringBuilder sb = new StringBuilder("INSERT INTO book (id, title, author, isbn) ")
                .append("VALUES (")
                .append(String.valueOf(id) + ", ")
                .append("'" + title + "', ")
                .append("'" + author + "', ")
                .append(String.valueOf(isbn))
                .append(");");


        String query = sb.toString();
        //System.out.println(query);
        executeSimpleStatement(query);
    }

    public void updateIntoTable(String id, String title, String author, int isbn) {
        executeSimpleStatement("UPDATE book SET title = '" + title +"' WHERE id = "+ id + ";");
    }

    public void deleteFromTable(int id) {
        StringBuilder sb = new StringBuilder("DELETE FROM book WHERE id = ")
                .append(String.valueOf(id) + ";");

        String query = sb.toString();
        executeSimpleStatement(query);
    }

    public void selectFromTable() {
        String statement = "SELECT * FROM book;";
        ResultSet resultSet = session.execute(statement);
        for (Row row : resultSet) {
            System.out.print("książka: ");
            System.out.print(row.getInt("id") + ", ");
            System.out.print(row.getString("title") + ", ");
            System.out.print(row.getString("author") + ", ");
            System.out.print(row.getInt("isbn") + "\n");
        }
        System.out.println();
        System.out.println("Statement \"" + statement + "\" executed successfully");
    }

    public void selectFromTable(int id) {
        String statement = "SELECT * FROM book WHERE id = "+ String.valueOf(id) +";";
        ResultSet resultSet = session.execute(statement);
        for (Row row : resultSet) {
            System.out.print("książka: ");
            System.out.print(row.getInt("id") + ", ");
            System.out.print(row.getString("title") + ", ");
            System.out.print(row.getString("author") + ", ");
            System.out.print(row.getInt("isbn") + "\n");
        }
        System.out.println();
        System.out.println("Statement \"" + statement + "\" executed successfully");
    }

    public void selectFromTable(String author) {
        StringBuilder sb = new StringBuilder("SELECT * FROM book WHERE author = ")
                .append("'"+ author +"' ALLOW FILTERING;");

        String query = sb.toString();

        ResultSet resultSet = session.execute(query);
        for (Row row : resultSet) {
            System.out.print("książka: ");
            System.out.print(row.getInt("id") + ", ");
            System.out.print(row.getString("title") + ", ");
            System.out.print(row.getString("author") + ", ");
            System.out.print(row.getInt("isbn") + "\n");
        }
        System.out.println();
        System.out.println("Statement \"" + query + "\" executed successfully");
    }

    public void dropTable() {
        executeSimpleStatement("DROP TABLE book;");
    }
}
