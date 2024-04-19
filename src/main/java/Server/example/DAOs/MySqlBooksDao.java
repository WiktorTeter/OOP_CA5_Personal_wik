package Server.example.DAOs;

/** OOP Feb 2024
 *
 * Data Access Object (DAO) for User table with MySQL-specific code
 * This 'concrete' class implements the 'UserDaoInterface'.
 *
 * The DAO will contain the SQL query code to interact with the database,
 * so, the code here is specific to a MySql database.
 * No SQL queries will be used in the Business logic layer of code, thus, it
 * will be independent of the database specifics. Changes to code related to
 * the database are all contained withing the DAO code base.
 *
 *
 * The Business Logic layer is only permitted to access the database by calling
 * methods provided in the Data Access Layer - i.e. by calling the DAO methods.
 * In this way, the Business Logic layer is seperated from the database specific code
 * in the DAO layer.
 */

import Server.example.DTOs.Book;
import Server.example.Exceptions.DaoException;
import Server.example.org.BookStore;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySqlBooksDao extends MySqlDao implements UserDaoInterface
{
    /**
     * Will access and return a List of all users in User database table
     * @return List of User objects
     * @throws DaoException
     */
    /**
     * Main author: Wiktor Teter
     * Other contributors: Eljesa Mesi
     *
     */
    public List<Book> findAllBooks() throws DaoException {
        List<Book> bookList = new ArrayList<>();
        try (Connection connection = this.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM books");
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                String author = resultSet.getString("author");
                float price = resultSet.getFloat("price");
                bookList.add(new Book(id, title, author, price));
            }
        } catch (SQLException e) {
            throw new DaoException("findAllBooks() " + e.getMessage());
        }
        return bookList;
    }

    /**
     * Given a username and password, find the corresponding User
     * @param id
     *
     * @return User object if found, or null otherwise
     * @throws DaoException
     */
    /**
     * Main author: Eljesa Mesi
     * Other contributors: Wiktor Teter
     *
     */
    //feature 2
    public Book getBookByID(int id) {
        String url = "jdbc:mysql://localhost:3306/bookshop";
        String userName = "root";
        String password = "";

        try (Connection conn = DriverManager.getConnection(url, userName, password)) {
            String sql = "SELECT * FROM books WHERE id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, id);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    int bookId = rs.getInt("id");
                    String title = rs.getString("title");
                    String author = rs.getString("author");
                    float price = rs.getFloat("price");
                    System.out.println("Book found:");
                    return new Book(bookId, title, author, price);
                } else {
                    System.out.println("No book found with ID: " + id);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }    /**
 * Main author: Eljesa Mesi
 * Other contributors: Wiktor Teter
 *
 */
    //feature 3
    public Book deleteBookByID(int id){
        String url = "jdbc:mysql://localhost:3306/bookshop";
        String userName = "root";
        String password = "";

        try (Connection conn = DriverManager.getConnection(url, userName, password)) {
            String sql = "DELETE FROM books WHERE id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, id);
                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Book with ID: " + id + " deleted successfully");
                } else {
                    System.out.println("No book found with ID: " + id);
                }
            }
        }
        catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace(); //

        }
        return null;
    }    /**
 * Main author: Eljesa Mesi
 * Other contributors: Wiktor Teter
 *
 */
    //feature 4
    public void insertBook(int id, String title, String author, float price){
        String url = "jdbc:mysql://localhost:3306/bookshop";
        String userName = "root";
        String password = "";

        try (Connection conn = DriverManager.getConnection(url, userName, password)) {
            String sql = "INSERT INTO books (id, title, author, price) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, id);
                pstmt.setString(2, title);
                pstmt.setString(3, author);
                pstmt.setFloat(4, price);
                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Book inserted successfully");
                } else {
                    System.out.println("Book not inserted");
                }
            }
        }
        catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace(); //

        }
    }    /**
 * Main author: Eljesa Mesi
 * Other contributors: Wiktor Teter
 *
 */
    //feature 5
    //update an existing entity by id
    public void updateBookByID(int id, String title, String author, float price){
        String url = "jdbc:mysql://localhost:3306/bookshop";
        String userName = "root";
        String password = "";

        try (Connection conn = DriverManager.getConnection(url, userName, password)) {
            String sql = "UPDATE books SET title = ?, author = ?, price = ? WHERE id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, title);
                pstmt.setString(2, author);
                pstmt.setFloat(3, price);
                pstmt.setInt(4, id);
                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Book updated successfully");
                } else {
                    System.out.println("No book found with ID: " + id);
                }
            }
        }
        catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace(); //

        }
    }
    /**
     * Main author: Eljesa Mesi
     * Other contributors: Wiktor Teter
     *
     */
    @Override
    public List<Book> getBookByFilter(Book filter) {
        List<Book> books = new ArrayList<>();
        String url = "jdbc:mysql://localhost:3306/bookshop";
        String userName = "root";
        String password = "";

        try (Connection conn = DriverManager.getConnection(url, userName, password)) {
            String sql = "SELECT * FROM books WHERE title = ? AND author = ? AND price = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, filter.getTitle());
                pstmt.setString(2, filter.getAuthor());
                pstmt.setFloat(3, filter.getPrice());
                ResultSet rs = pstmt.executeQuery();

                while (rs.next()) {
                    int bookId = rs.getInt("id");
                    String title = rs.getString("title");
                    String author = rs.getString("author");
                    float price = rs.getFloat("price");
                    Book book = new Book(bookId, title, author, price);
                    books.add(book);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
        return books;
    }
    /**
     * Main author: Eljesa Mesi
     * Other contributors: Wiktor Teter
     *
     */
    // Feature 7 - Convert List of Entities to a JSON String
    public String convertListToJSON(List<Book> books) {
        StringBuilder json = new StringBuilder();
        json.append("[");
        for (Book book : books) {
            json.append("{");
            json.append("\"id\": ").append(book.getId()).append(", ");
            json.append("\"title\": \"").append(book.getTitle().replace("\"", "\\\"")).append("\", ");
            json.append("\"author\": \"").append(book.getAuthor().replace("\"", "\\\"")).append("\", ");
            json.append("\"price\": ").append(book.getPrice());
            json.append("}, ");
        }
        if (books.size() > 0) {
            json.delete(json.length() - 2, json.length()); // Remove trailing comma only if books were added
        }
        json.append("]");
        return json.toString();
    }
    /**
     * Main author: Eljesa Mesi
     * Other contributors: Wiktor Teter
     *
     */
    // Feature 8 - Convert a single Entity by Key to a JSON String
    public String convertEntityToJSON(Book book) {
        if (book == null) {
            return "{}"; // Return an empty object if the book is null
        }
        StringBuilder json = new StringBuilder();
        json.append("{");
        json.append("\"id\": ").append(book.getId()).append(", ");
        json.append("\"title\": \"").append(book.getTitle().replace("\"", "\\\"")).append("\", ");
        json.append("\"author\": \"").append(book.getAuthor().replace("\"", "\\\"")).append("\", ");
        json.append("\"price\": ").append(book.getPrice());
        json.append("}");
        return json.toString();
    }

}

