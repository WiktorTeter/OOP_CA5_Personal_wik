package org.example;
import java.util.List;
import java.util.Scanner;

import DAOs.UserDaoInterface;
import DAOs.MySqlBooksDao;
import DTOs.Book;
import Exceptions.DaoException;


public class BookStore {
    private static UserDaoInterface userDao = new MySqlBooksDao();
    private static Scanner input = new Scanner(System.in);
    public static void main(String[] args) throws DaoException {


        int choice;
        do {
            System.out.println("1. View All Books");
            System.out.println("2. Get Book by ID");
            System.out.println("3. Delete Book by ID");
            System.out.println("4. Insert Book");
            System.out.println("5. Update Book By ID");
            System.out.println("6. Find Book By Filter");
            System.out.println("7. Convert all books to JSON");
            System.out.println("8. Convert book by ID to JSON");
            System.out.println("0. Exit");
            choice = input.nextInt();
            switch (choice) {
                case 1:
                    findAllBooks();
                    break;
                case 2:
                    System.out.println("PLease enter the book ID: ");
                    int bookID = input.nextInt();
                    getBookByID(bookID);
                    break;
                case 3:
                    System.out.println("PLease enter the book ID: ");
                    int bookID2 = input.nextInt();
                    deleteBookByID(bookID2);

                    break;
                case 4:
                    System.out.println("Please enter the book ID: ");
                    int bookID3 = input.nextInt();
                    input.nextLine(); // Consume newline character
                    System.out.println("Please enter the book title: ");
                    String title = input.nextLine();
                    System.out.println("Please enter the book author: ");
                    String author = input.nextLine();
                    System.out.println("Please enter the book price: ");
                    float price = input.nextFloat();
                    insertBook(bookID3, title, author, price);
                    break;
                case 5:
                    System.out.println("Please enter the book ID: ");
                    int bookID4 = input.nextInt();
                    input.nextLine(); // Consume newline character
                    System.out.println("Please enter the book title: ");
                    String title2 = input.nextLine();
                    System.out.println("Please enter the book author: ");
                    String author2 = input.nextLine();
                    System.out.println("Please enter the book price: ");
                    float price2 = input.nextFloat();
                    updateBookByID(bookID4, title2, author2, price2);
                    break;

                case 6:
                    System.out.println("Please enter the book ID: ");
                    int bookID5 = input.nextInt();
                    input.nextLine(); // Consume newline character
                    System.out.println("Please enter the book title: ");
                    String title3 = input.nextLine();
                    System.out.println("Please enter the book author: ");
                    String author3 = input.nextLine();
                    System.out.println("Please enter the book price: ");
                    float price3 = input.nextFloat();
                    Book filter = new Book(bookID5, title3, author3, price3);
                    getBookByFilter(filter);
                    break;
                case 7:
                    findAllBooksAndConvertToJSON();
                    break;
                case 8:
                    System.out.println("Please enter the book ID for JSON conversion: ");
                    int bookIDJson = input.nextInt();
                    convertBookByIDToJson(bookIDJson);
                    break;
                case 0:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }
        } while (choice != 0);
    }




    private static void findAllBooks() throws DaoException {
        List<Book> books = userDao.findAllBooks();
        for (Book book : books) {
            System.out.println(book);
        }



    }
    private static void getBookByID(int id) throws DaoException {
        Book book = userDao.getBookByID(id);
        System.out.println(book);
    }
    private static void deleteBookByID(int id)  {
        Book book = null;
        try {
            book = userDao.deleteBookByID(id);
        } catch (DaoException e) {
            throw new RuntimeException(e);
        }
        System.out.println(book);
    }
    private static void insertBook(int id, String title, String author, float price) {
        try {
            userDao.insertBook(id, title, author, price);
        } catch (DaoException e) {
            throw new RuntimeException(e);
        }
    }
    private static void updateBookByID(int id, String title, String author, float price) {
        try {
            userDao.updateBookByID(id, title, author, price);
        } catch (DaoException e) {
            throw new RuntimeException(e);
        }
    }
    private static void getBookByFilter(Book filter) {
        List<Book> books = userDao.getBookByFilter(filter);
        for (Book book : books) {
            System.out.println(book);
        }
    }

    private static void findAllBooksAndConvertToJSON() throws DaoException {
        List<Book> books = userDao.findAllBooks();
        String json = ((MySqlBooksDao) userDao).convertListToJSON(books);
        System.out.println("JSON representation of all books:");
        System.out.println(json);
    }

    private static void convertBookByIDToJson(int id) throws DaoException {
        Book book = userDao.getBookByID(id);
        if (book != null) {
            String json = ((MySqlBooksDao) userDao).convertEntityToJSON(book);
            System.out.println("JSON representation of the book:");
            System.out.println(json);
        } else {
            System.out.println("No book found with ID: " + id);
        }
    }
}



