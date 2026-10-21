package vn.vti.sqljdbbc;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import vn.vti.DatabaseConnection;
import vn.vti.DatabaseType;

import java.sql.*;

public class JdbcMySqlTest {
    private static Connection sqlConnection;

    @BeforeAll
    public static void setUpEnvironment() throws SQLException {
        // Init database connection
        sqlConnection = (Connection) new DatabaseConnection().initConnectionToDb(DatabaseType.MYSQL, "school_management", "admin", "Admin@123");
    }

    @AfterAll
    static void tearDown() throws SQLException {
        // Close db connection
        sqlConnection.close();
    }

    @Test
    void test_create_table() throws SQLException{
        // Create statement
        Statement statement = sqlConnection.createStatement();
        statement.execute("CREATE TABLE schools (\n" +
                "    school_id INT AUTO_INCREMENT PRIMARY KEY,\n" +
                "    school_name VARCHAR(150) NOT NULL,\n" +
                "    address VARCHAR(255),\n" +
                "    city VARCHAR(100),\n" +
                "    state VARCHAR(50),\n" +
                "    postal_code VARCHAR(20),\n" +
                "    phone_number VARCHAR(20),\n" +
                "    email VARCHAR(100) UNIQUE,\n" +
                "    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP\n" +
                ");");
    }

    @Test
    void test_insert_into() throws SQLException {
        // User preparestatement to void SQL injection && increase performance
        String insertSql = "INSERT INTO schools (school_name, address, city, state, postal_code, phone_number, email)\n" +
                "VALUES (?, ?, ?, ?, ?, ?, ?);\n";
        PreparedStatement preparedStatement = sqlConnection.prepareStatement(insertSql);
        preparedStatement.setString(1, "Greenwood High School");
        preparedStatement.setString(2, "123 Education Lane");
        preparedStatement.setString(3, "Austin");
        preparedStatement.setString(4, "TX");
        preparedStatement.setString(5, "78701");
        preparedStatement.setString(6, "512-555-0199");
        preparedStatement.setString(7, "info@greenwoodhigh.edu");
        preparedStatement.executeUpdate();
    }

    @Test
    void test_delete() throws SQLException {
        String deleteSql = "DELETE FROM schools where city = ?";
        PreparedStatement statement = sqlConnection.prepareStatement(deleteSql);
        statement.setString(1, "Austin");
        statement.executeUpdate();
    }

    @Test
    void test_update() throws SQLException {
        String updateSql = "UPDATE schools SET school_name = ?, email = ?";
        PreparedStatement statement = sqlConnection.prepareStatement(updateSql);
        statement.setString(1, "Harvard");
        statement.setString(2, "info@harvard.com");
        statement.executeUpdate();
    }

    @Test
    void test_query() throws SQLException {
        String querySql = "SELECT s.first_name, s.last_name, s.email, g.points_earned from students as s\n" +
                "INNER JOIN grades as g  on g.student_id = s.student_id;";
        Statement statement = sqlConnection.createStatement();
        ResultSet resultSet = statement.executeQuery(querySql);
        while(resultSet.next()) {
            String firstname = resultSet.getString("first_name");
            String lastName = resultSet.getString("last_name");
            int points_earned = resultSet.getInt("points_earned");
            Assertions.assertAll(() -> {
                Assertions.assertFalse(firstname.isEmpty());
                Assertions.assertFalse(lastName.isEmpty());
                Assertions.assertTrue(points_earned < 0);
            });
        }
    }
    @Test
    void test_query_1 () throws  SQLException {
        // Call api to add new students
        // Verify api response
        // Verify database
        String query = "SELECT * FROM students limit 1";
        ResultSet set = sqlConnection.createStatement().executeQuery(query);
    }
}
