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
import Server.example.DAOs.MySqlBooksDao;
import Server.example.DAOs.UserDaoInterface;
import Server.example.DTOs.Book;
import Server.example.Exceptions.DaoException;


public class BookStore {
    public static UserDaoInterface userDao = new MySqlBooksDao();
    final int SERVER_PORT_NUMBER = 8888;  // could be any port from 1024 to 49151 (that doesn't clash with other Apps)

    public static void main(String[] args) {
        BookStore server = new BookStore();
        server.start();
    }

    public void start() {

        ServerSocket serverSocket =null;
        Socket clientSocket =null;

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
        }
        finally{
            try {
                if(clientSocket!=null)
                    clientSocket.close();
            } catch (IOException e) {
                System.out.println(e);
            }
            try {
                if(serverSocket!=null)
                    serverSocket.close();
            } catch (IOException e) {
                System.out.println(e);
            }

        }
        System.out.println("Server: Server exiting, Goodbye!");
    }
}

class ClientHandler implements Runnable   // each ClientHandler communicates with one Client
{
    BufferedReader socketReader;
    PrintWriter socketWriter;
    Socket clientSocket;
    final int clientNumber;

    // Constructor
    public ClientHandler(Socket clientSocket, int clientNumber) {
        this.clientSocket = clientSocket;  // store socket for closing later
        this.clientNumber = clientNumber;  // ID number that we are assigning to this client
        try {
            // assign to fields
            this.socketWriter = new PrintWriter(clientSocket.getOutputStream(), true);
            this.socketReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * run() method is called by the Thread it is assigned to.
     * This code runs independently of all other threads.
     */
    @Override
    public void run() {
        String request;
        try {
            while ((request = socketReader.readLine()) != null) {
                System.out.println("Server: (ClientHandler): Read command from client " + clientNumber + ": " + request);

                // Implement our PROTOCOL
                // The protocol is the logic that determines the responses given based on requests received.
                //
                if (request.startsWith("time"))  // so, client wants the time !
                {
                    LocalTime time = LocalTime.now();  // get the time
                    socketWriter.println(time);  // send the time to client (as a string of characters)
                    System.out.println("Server message: time sent to client.");
                } else if (request.startsWith("echo")) {
                    String message = request.substring(5); // strip off the leading substring "echo "
                    socketWriter.println(message);   // send the received message back to the client
                    System.out.println("Server message: echo message sent to client.");
                } else if (request.startsWith("quit"))
                {
                    socketWriter.println("Sorry to see you leaving. Goodbye.");
                    System.out.println("Server message: Client has notified us that it is quitting.");
                }
                else{
                    socketWriter.println("error I'm sorry I don't understand your request");
                    System.out.println("Server message: Invalid request from client.");
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            this.socketWriter.close();
            try {
                this.socketReader.close();
                this.clientSocket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        System.out.println("Server: (ClientHandler): Handler for Client " + clientNumber + " is terminating .....");
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
    private static void deleteBookByID(int id)  {
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





