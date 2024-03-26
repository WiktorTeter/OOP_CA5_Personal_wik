package org.example;
import java.util.Scanner;



//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class BookStore {
    public static void main(String[] args) {
        Books book = new Books();
        Scanner input = new Scanner(System.in);
        int choice;
        do {
            System.out.println("1. View All Books");
            System.out.println("2. Get Book by ID");
            System.out.println("3. Delete Book by ID");
            System.out.println("4. Insert Book");
            System.out.println("5. Update Book By ID");
            System.out.println("6. Find Book By Filter");
            choice = input.nextInt();
            switch (choice) {
                case 1:
                    book.start();
                    break;
                case 2:

                    break;
                case 3:

                    break;
                case 4:

                    break;
                case 5:

                    break;

                case 6:

                    break;

            }
        } while (choice != 0);









    }

}