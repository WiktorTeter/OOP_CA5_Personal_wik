package Server.example.org;

/**
 * MULTI-THREADED SERVER                                         March 2024
 * <p>
 * This Server accepts multiple client connections and manages a connection
 * with each client using a Thread per client. There is only one server.
 * For each connection accepted, a new ClientHandler object is created to handle
 * the communications with that Client. The ClientHandler is initialized on
 * construction with the socket created to communicate with the client.
 * The ClientHandler implements the Runnable interface, (it is a Runnable)
 * The server then passes the client handler into a new Thread, and ClientHandler
 * run() method runs in the thread and continues to independently communicate with
 * the client.
 * <p>
 * The server uses the client handler to process requests from clients, and
 * sends appropriate responses back to the client.
 * <p>
 * The following PROTOCOL is implemented:
 * If ( the Server receives the request "Time", from a Client ) then : the
 * server will send back the current time.
 * If ( the Server receives the request "Echo message", from a Client ) then :
 * the server will send back the message.
 * If ( the Server receives the request it does not recognize ) then : the
 * server will send back the message "Sorry, I don't understand".
 * This is an example of a simple protocol, where the server's response is based
 * only on  the client's request.
 * <p>
 * Each client is handled by a ClientHandler running in a separate worker Thread.
 * Because the thread runs independently, the server code continues to execute
 * and continually listens for new client requests and create handlers for those clients.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;

import Server.example.DAOs.MySqlBooksDao;
import Server.example.DAOs.UserDaoInterface;
import Server.example.DTOs.Book;
import Server.example.Exceptions.DaoException;


public class BookStore {
    public static UserDaoInterface userDao = new MySqlBooksDao();
    final int SERVER_PORT_NUMBER = 8888;  // could be any port from 1024 to 49151 (that doesn't clash with other Apps)

    public static void main(String[] args) throws DaoException {
        BookStore server = new BookStore();
        System.out.println("Press 1 to stay in server side or 2 to go to client side: ");
        Scanner input = new Scanner(System.in);
        int choice = input.nextInt();
        if (choice == 1) {
            server.runMenu();  // Run the server management menu directly
        } else {
            server.start();  // Start the normal server operation
        }
        input.close();
    }



    public void start() {

        ServerSocket serverSocket = null;
        Socket clientSocket = null;

        try {
            serverSocket = new ServerSocket(SERVER_PORT_NUMBER);
            System.out.println("Server has started.");
            int clientNumber = 0;  // a number sequentially allocated to each new client (for identification purposes here)

            while (true) {
                System.out.println("Server: Listening/waiting for connections on port ..." + SERVER_PORT_NUMBER);
                clientSocket = serverSocket.accept();
                clientNumber++;
                System.out.println("Server: Listening for connections on port ..." + SERVER_PORT_NUMBER);

                System.out.println("Server: Client " + clientNumber + " has connected.");
                System.out.println("Server: Port number of remote client: " + clientSocket.getPort());
                System.out.println("Server: Port number of the socket used to talk with client " + clientSocket.getLocalPort());

                // create a new ClientHandler for the requesting client, passing in the socket and client number,
                // pass the handler into a new thread, and start the handler running in the thread.
                Thread t = new Thread(new ClientHandler(clientSocket, clientNumber));
                t.start();

                System.out.println("Server: ClientHandler started in thread " + t.getName() + " for client " + clientNumber + ". ");

            }
        } catch (IOException ex) {
            System.out.println(ex);
        } finally {
            try {
                if (clientSocket != null)
                    clientSocket.close();
            } catch (IOException e) {
                System.out.println(e);
            }
            try {
                if (serverSocket != null)
                    serverSocket.close();
            } catch (IOException e) {
                System.out.println(e);
            }

        }
        System.out.println("Server: Server exiting, Goodbye!");
    }
    private void runMenu() throws DaoException {
        Scanner input = new Scanner(System.in);
        int choice;
        do {
            System.out.println("\n1. View All Books");
            System.out.println("2. Find Book by ID");
            System.out.println("3. Delete Book by ID");
            System.out.println("4. Insert New Book");
            System.out.println("5. Update Existing Book");
            System.out.println("6. Find Books by Filter");
            System.out.println("7. Display All Books in JSON Format");
            System.out.println("8. Convert Book by ID to JSON");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");

            choice = input.nextInt();
            switch (choice) {
                case 1:
                    findAllBooks();
                    break;
                case 2:
                    System.out.println("Please enter the book ID: ");
                    int bookID = input.nextInt();
                    getBookByID(bookID);
                    break;
                case 3:
                    System.out.println("Please enter the book ID: ");
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
                    input.close();
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }
        } while (choice != 0);
    }
    private static void findAllBooks() throws DaoException {
        List<Book> books = BookStore.userDao.findAllBooks();
        for (Book book : books) {
            System.out.println(book);
        }


    }

    private static void getBookByID(int id) throws DaoException {
        Book book = BookStore.userDao.getBookByID(id);
        System.out.println(book);
    }

    private static void deleteBookByID(int id) {
        Book book = null;
        try {
            book = BookStore.userDao.deleteBookByID(id);
        } catch (DaoException e) {
            throw new RuntimeException(e);
        }
        System.out.println(book);
    }

    private static void insertBook(int id, String title, String author, float price) {
        try {
            BookStore.userDao.insertBook(id, title, author, price);
        } catch (DaoException e) {
            throw new RuntimeException(e);
        }
    }

    private static void updateBookByID(int id, String title, String author, float price) {
        try {
            BookStore.userDao.updateBookByID(id, title, author, price);
        } catch (DaoException e) {
            throw new RuntimeException(e);
        }
    }

    private static void getBookByFilter(Book filter) {
        List<Book> books = BookStore.userDao.getBookByFilter(filter);
        for (Book book : books) {
            System.out.println(book);
        }
    }

    private static void findAllBooksAndConvertToJSON() throws DaoException {
        List<Book> books = BookStore.userDao.findAllBooks();
        String json = ((MySqlBooksDao) BookStore.userDao).convertListToJSON(books);
        System.out.println("JSON representation of all books:");
        System.out.println(json);
    }

    private static void convertBookByIDToJson(int id) throws DaoException {
        Book book = BookStore.userDao.getBookByID(id);
        if (book != null) {
            String json = ((MySqlBooksDao) BookStore.userDao).convertEntityToJSON(book);
            System.out.println("JSON representation of the book:");
            System.out.println(json);
        } else {
            System.out.println("No book found with ID: " + id);
        }
    }
}
    class ClientHandler implements Runnable {
        BufferedReader socketReader;
        PrintWriter socketWriter;
        Socket clientSocket;
        final int clientNumber;

        public ClientHandler(Socket clientSocket, int clientNumber) {
            this.clientSocket = clientSocket;
            this.clientNumber = clientNumber;
            try {
                this.socketWriter = new PrintWriter(clientSocket.getOutputStream(), true);
                this.socketReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void run() {
            String request;
            try {
                while ((request = socketReader.readLine()) != null) {
                    System.out.println("Server: (ClientHandler): Read command from client " + clientNumber + ": " + request);

                    if (request.startsWith("display ")) {
                        try {
                            int id = Integer.parseInt(request.substring(8).trim());
                            Book book = BookStore.userDao.getBookByID(id);
                            if (book != null) {
                                String json = ((MySqlBooksDao) BookStore.userDao).convertEntityToJSON(book);
                                socketWriter.println(json);
                            } else {
                                socketWriter.println("No book found with ID: " + id);
                            }
                        } catch (NumberFormatException e) {
                            socketWriter.println("Error: Invalid ID format");
                        } catch (DaoException e) {
                            socketWriter.println("Error accessing database");
                        }
                    } else if (request.startsWith("quit")) {
                        socketWriter.println("Goodbye.");
                        break;
                    } else {
                        socketWriter.println("Sorry, I don't understand");
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                try {
                    socketWriter.close();
                    socketReader.close();
                    clientSocket.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }

    }









