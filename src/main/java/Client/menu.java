package Client;

/**
 * CLIENT                                                  Mar 2024
 * <p>
 * This Client program asks the user to input commands to be sent to the server.
 * There are only two valid commands in the protocol: "Time" and "Echo"
 * If user types "time" then that string is sent to the server, and it will respond
 * by sending back the current time.
 * <p>
 * If the user types "echo" followed by a space and a message, the server will echo back the message.
 * e.g. "echo Nice to meet you".
 * If the user enters any other input, the server will not understand, and
 * will send back a message to the effect.
 * <p>
 * Note: You must run the server before running this the client.
 * (Both the server and the client will be running together on this computer)
 */

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Scanner;

public class menu {
    final static int SERVER_PORT_NUMBER = 8888;
    final static String SERVER_HOST = "localhost";
    /**
     * Main author: Wiktor Teter
     * Other contributors: Eljesa Mesi
     *
     */
    public static void main(String[] args) {
        new menu().start();
    }

    public void start() {
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT_NUMBER);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             Scanner consoleInput = new Scanner(System.in)) {

            System.out.println("Client message: The Client is running and has connected to the server");
            while (true) {
                System.out.println("Valid commands are: \"display all\", \"display <id>\", \"add book\", \"delete book <id>\", \"quit\"");
                System.out.print("Please enter a command: ");
                String userInput = consoleInput.nextLine().trim();

                if ("quit".equalsIgnoreCase(userInput)) {
                    out.println(userInput);
                    break;
                } else if (userInput.equalsIgnoreCase("display all")) {
                    out.println("display all");
                }
                else if (userInput.equalsIgnoreCase("add book")) {
                    System.out.println("Enter book details to add:");
                    System.out.print("ID: ");
                    int id = consoleInput.nextInt(); consoleInput.nextLine(); // Consumes newline
                    System.out.print("Title: ");
                    String title = consoleInput.nextLine().replace("\"", "\\\"");
                    System.out.print("Author: ");
                    String author = consoleInput.nextLine().replace("\"", "\\\"");
                    System.out.print("Price: ");
                    float price = consoleInput.nextFloat(); consoleInput.nextLine();

                    String jsonRequest = String.format("{\"id\": %d, \"title\": \"%s\", \"author\": \"%s\", \"price\": %.2f}", id, title, author, price);
                    out.println("add book " + jsonRequest);
                }
                else {
                    out.println(userInput);
                }

                String response = in.readLine();
                System.out.println("Server response: " + response);
            }
        } catch (IOException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

}