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

    final int SERVER_PORT_NUMBER = 8888;
    public static void main(String[] args) {
        menu client = new menu();
        client.start();
    }

    public void start() {

        try (
                Socket socket = new Socket("localhost", SERVER_PORT_NUMBER);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        ) {
            System.out.println("Client message: The Client is running and has connected to the server");

            Scanner consoleInput = new Scanner(System.in);
            boolean continueRunning = true;
            while(continueRunning) {
                System.out.println("Valid commands are: \"display all\", \"display <id>\", \"add book\" ,\"quit\"");
                System.out.print("Please enter a command: ");
                String userInput = consoleInput.nextLine();

                if(userInput.equalsIgnoreCase("quit")) {
                    continueRunning = false;
                }

                else if(userInput.toLowerCase().startsWith("display")) {
                    String[] parts = userInput.split(" ");
                    if(parts.length > 1) {
                        out.println("display " + parts[1]);  // Send command to server
                        String response = in.readLine();  // Read response from server
                        System.out.println("Received from server: " + response);
                    } else {
                        System.out.println("Invalid command. Usage: display <id>");
                    }
                }

                if (userInput.equalsIgnoreCase("display all")) {
                    out.println("display all"); // Send command to server
                    String response = in.readLine(); // Read JSON response from server
                    System.out.println("Received from server: ");
                    System.out.println(response); // Display JSON or further process it
                }
                if (userInput.equalsIgnoreCase("add book")) {
                    System.out.println("Enter book details to add:");
                    System.out.print("ID: ");
                    int id = consoleInput.nextInt(); consoleInput.nextLine(); // Consumes newline
                    System.out.print("Title: ");
                    String title = consoleInput.nextLine().replace("\"", "\\\"");
                    System.out.print("Author: ");
                    String author = consoleInput.nextLine().replace("\"", "\\\"");
                    System.out.print("Price: ");
                    float price = consoleInput.nextFloat(); consoleInput.nextLine(); // Consumes newline

                    String jsonRequest = String.format("{\"id\": %d, \"title\": \"%s\", \"author\": \"%s\", \"price\": %.2f}", id, title, author, price);
                    out.println("add book " + jsonRequest);  // Send the JSON to the server

                    String response = in.readLine();  // Read the response from the server
                    System.out.println("Response from server: " + response);
                }

                else {
                    out.println(userInput);
                    String serverResponse = in.readLine();
                    System.out.println("Server response: " + serverResponse);
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }
}
