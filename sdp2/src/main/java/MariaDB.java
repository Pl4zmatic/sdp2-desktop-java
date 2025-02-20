import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MariaDB {
    public static void main(String[] args) {

        String url = "jdbc:mariadb://vichogent.be:40058/SDP2_2425_DB_G02";
        String user = "SDP2-2425-G02";
        String password = "admin";

        // Try catch die verbinding maakt met db
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            System.out.println("Verbinding met de database is gelukt!");

        } catch (SQLException e) {
            System.err.println("Fout bij het verbinden met de database: " + e.getMessage());
        }
    }
}