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
                System.out.println("Valid commands are: \"display all\", \"display <id>\", \"quit\"");
                System.out.print("Please enter a command: ");
                String userInput = consoleInput.nextLine();

                if(userInput.equalsIgnoreCase("quit")) {
                    continueRunning = false;
                } else if(userInput.toLowerCase().startsWith("display")) {
                    String[] parts = userInput.split(" ");
                    if(parts.length > 1) {
                        out.println("display " + parts[1]);  // Send command to server
                        String response = in.readLine();  // Read response from server
                        System.out.println("Received from server: " + response);
                    } else {
                        System.out.println("Invalid command. Usage: display <id>");
                    }
                } if (userInput.equalsIgnoreCase("display all")) {
                    out.println("display all"); // Send command to server
                    String response = in.readLine(); // Read JSON response from server
                    System.out.println("Received from server: ");
                    System.out.println(response); // Display JSON or further process it
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

//  LocalTime time = LocalTime.parse(timeString); // Parse String -> convert to LocalTime object if required LocalTime.parse(timeString); // Parse timeString -> convert to LocalTime object if required