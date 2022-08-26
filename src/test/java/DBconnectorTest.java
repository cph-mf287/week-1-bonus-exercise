import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

class DBconnectorTest {

    private Connection con = null;

    @BeforeEach
    void setUp() {
        System.out.println("TESTINNNNGGGG");
        try {
            con = DBconnector.connection();
            String createTable = "CREATE TABLE IF NOT EXISTS `startcode_test`.`usertable` (\n" +
                    "  `id` INT NOT NULL AUTO_INCREMENT,\n" +
                    "  `fname` VARCHAR(45) NULL,\n" +
                    "  `lname` VARCHAR(45) NULL,\n" +
                    "  `pw` VARCHAR(45) NULL,\n" +
                    "  `phone` VARCHAR(45) NULL,\n" +
                    "  `address` VARCHAR(45) NULL,\n" +
                    "  PRIMARY KEY (`id`));";
            con.prepareStatement(createTable).executeUpdate();
            String SQL = "INSERT INTO startcode_test.usertable (fname, lname, pw, phone, address) VALUES (?,?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, "Hans");
            ps.setString(2, "Hansen");
            ps.setString(3, "Hemmelig123");
            ps.setString(4, "40404040");
            ps.setString(5, "Rolighedsvej 3");
            ps.executeUpdate();
            SQL = "INSERT INTO startcode_test.usertable (fname, lname, pw, phone, address) VALUES (?,?,?,?,?)";
            ps = con.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, "Jens");
            ps.setString(2, "Jensen");
            ps.setString(3, "Hemmelig123");
            ps.setString(4, "40404040");
            ps.setString(5, "Rolighedsvej 3");
            ps.executeUpdate();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    void tearDown() {
        try {
            con = DBconnector.connection();
            String createTable = "DROP TABLE IF EXISTS `startcode_test`.`usertable`";
            con.prepareStatement(createTable).executeUpdate();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Table dropped.");
    }

    @Test
    void US1() throws SQLException {
        System.out.println("List of all user on the system by their names only");
        //String expected = "1 Hans Hansen";
        StringBuilder actual = new StringBuilder();
        ResultSet rs = con.prepareStatement("SELECT * FROM `startcode_test`.`usertable`").executeQuery();
        while (rs.next()) {
            int id = rs.getInt("id");
            String fname = rs.getString("fname");
            String lname = rs.getString("lname");
            actual.append(id).append(" ").append(fname).append(" ").append(lname).append("\n");
        }
        //assertEquals(expected, actual.toString());
        System.out.println(actual.toString());
    }

    // @org.junit.jupiter.api.Test
    // void connection() throws SQLException, ClassNotFoundException {
    //     System.out.println("Testing connection to db...");
    //     boolean expected = true;
    //     boolean actual = new DBconnector().connection().isValid(10);
    //     assertEquals(expected, actual);
    //     System.out.println(actual);
    // }
}