import java.sql.*;
import java.util.Scanner;

public class StudentManager {
    private static final String URL = "jdbc:mysql://localhost:3306/?user=root";
    private static final String USER = "root"; // Replace with your MySQL username
    private static final String PASSWORD = "root"; // Replace with your MySQL password

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Connected to the database!");

            while (true) {
                System.out.println("\n=== Student Management ===");
                System.out.println("1. Add Student");
                System.out.println("2. View All Students");
                System.out.println("3. Update Student");
                System.out.println("4. Delete Student");
                System.out.println("5. Exit");
                System.out.print("Choose an option: ");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        addStudent(connection, scanner);
                        break;
                    case 2:
                        viewStudents(connection);
                        break;
                    case 3:
                        updateStudent(connection, scanner);
                        break;
                    case 4:
                        deleteStudent(connection, scanner);
                        break;
                    case 5:
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid choice! Try again.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void addStudent(Connection connection, Scanner scanner) throws SQLException {
        System.out.print("Enter name: ");
        String name = scanner.next();
        System.out.print("Enter age: ");
        int age = scanner.nextInt();
        System.out.print("Enter grade: ");
        String grade = scanner.next();

        String sql = "INSERT INTO students (name, age, grade) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            statement.setInt(2, age);
            statement.setString(3, grade);
            int rows = statement.executeUpdate();
            System.out.println(rows + " student(s) added.");
        }
    }

    private static void viewStudents(Connection connection) throws SQLException {
        String sql = "SELECT * FROM students";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            System.out.println("\n=== Student List ===");
            while (resultSet.next()) {
                System.out.printf("ID: %d, Name: %s, Age: %d, Grade: %s%n",
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getInt("age"),
                        resultSet.getString("grade"));
            }
        }
    }

    private static void updateStudent(Connection connection, Scanner scanner) throws SQLException {
        System.out.print("Enter student ID to update: ");
        int id = scanner.nextInt();
        System.out.print("Enter new name: ");
        String name = scanner.next();
        System.out.print("Enter new age: ");
        int age = scanner.nextInt();
        System.out.print("Enter new grade: ");
        String grade = scanner.next();

        String sql = "UPDATE students SET name = ?, age = ?, grade = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            statement.setInt(2, age);
            statement.setString(3, grade);
            statement.setInt(4, id);
            int rows = statement.executeUpdate();
            System.out.println(rows + " student(s) updated.");
        }
    }

    private static void deleteStudent(Connection connection, Scanner scanner) throws SQLException {
        System.out.print("Enter student ID to delete: ");
        int id = scanner.nextInt();

        String sql = "DELETE FROM students WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            int rows = statement.executeUpdate();
            System.out.println(rows + " student(s) deleted.");
        }
    }
}
