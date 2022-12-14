import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.Scanner;

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
            ps.setString(3, "EndnuMereHemmelig123");
            ps.setString(4, "50505050");
            ps.setString(5, "Urolighedsvej 3");
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
        String expected = "1 Hans Hansen\n2 Jens Jensen\n";
        StringBuilder actual = new StringBuilder();
        ResultSet rs = con.prepareStatement("SELECT * FROM `startcode_test`.`usertable`").executeQuery();
        while (rs.next()) {
            int id = rs.getInt("id");
            String fname = rs.getString("fname");
            String lname = rs.getString("lname");
            actual.append(id).append(" ").append(fname).append(" ").append(lname).append("\n");
        }
        assertEquals(expected, actual.toString());
        System.out.println(actual.toString());
    }

    @Test
    void US2() throws SQLException {
        System.out.println("Details of a specific user from the list");
        String expected =
                "ID: 1\n" +
                "First Name: Hans\n" +
                "Last Name: Hansen\n" +
                "Password: Hemmelig123\n" +
                "Phone: 40404040\n" +
                "Address: Rolighedsvej 3\n";
        StringBuilder actual = new StringBuilder();
        PreparedStatement ps = con.prepareStatement("SELECT * FROM `startcode_test`.`usertable` WHERE id = ?");
        ps.setInt(1, 1);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            actual.append("ID: ").append(rs.getInt("id")).append("\n");
            actual.append("First Name: ").append(rs.getString("fname")).append("\n");
            actual.append("Last Name: ").append(rs.getString("lname")).append("\n");
            actual.append("Password: ").append(rs.getString("pw")).append("\n");
            actual.append("Phone: ").append(rs.getString("phone")).append("\n");
            actual.append("Address: ").append(rs.getString("address")).append("\n");
        }
        assertEquals(expected, actual.toString());
        System.out.println(actual.toString());
    }

    @Test
    void US3() throws SQLException {
        String expected =
                "ID: 1\n" +
                "First Name: Johan\n" +
                "Last Name: Hansen\n" +
                "Password: Hemmelig123\n" +
                "Phone: 40404040\n" +
                "Address: Rolighedsvej 3\n";
        StringBuilder actual = new StringBuilder();
        PreparedStatement ps = con.prepareStatement("UPDATE `startcode_test`.`usertable` SET fname = ? WHERE id = ?");
        ps.setString(1, "Johan");
        ps.setInt(2, 1);
        ps.executeUpdate();

        ps = con.prepareStatement("SELECT * FROM `startcode_test`.`usertable` WHERE id = ?");
        ps.setInt(1, 1);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            actual.append("ID: ").append(rs.getInt("id")).append("\n");
            actual.append("First Name: ").append(rs.getString("fname")).append("\n");
            actual.append("Last Name: ").append(rs.getString("lname")).append("\n");
            actual.append("Password: ").append(rs.getString("pw")).append("\n");
            actual.append("Phone: ").append(rs.getString("phone")).append("\n");
            actual.append("Address: ").append(rs.getString("address")).append("\n");
        }
        assertEquals(expected, actual.toString());
        System.out.println(actual.toString());
    }

    //@Test
    //void US3() throws SQLException {
    //    System.out.println("Edit your own details");
    //    Scanner sc = new Scanner(System.in);
    //    System.out.println(
    //            "What do you wish to change?\n" +
    //            "1. First Name" +
    //            "2. Last Name" +
    //            "3. Password" +
    //            "4. Phone" +
    //            "5. Address");
    //    int detail = sc.nextInt();
    //    switch (detail) {
    //        case 1:
    //            String column = "fname";
    //            String value = sc.nextLine();
    //            break;
    //        case 2:
    //            break;
    //        case 3:
    //            break;
    //        case 4:
    //            break;
    //        case 5:
    //            break;
    //    }
    //    String SQL = "UPDATE startcode_test.usertable (?) VALUES (?)";
    //    //assertEquals(expected, actual.toString());
    //    System.out.println(actual.toString());
    //}
}