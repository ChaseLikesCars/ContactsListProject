package src;

import util.Input;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class contactList {
    static Input input = new Input();
    static String directory = "src/data";
    static String filename = "contacts.txt";

    public static void main(String[] args) {
        start();
    }
    public static void start() {
        System.out.println("Welcome to your Contacts List");
        menu();
    }
    public static void menu() {
        System.out.println("Please Select One:");
        System.out.println("1 - Show all your contacts");
        System.out.println("2 - Add Contact");
        System.out.println("3 - Search by Name");
        System.out.println("4 - Delete existing Contact");
        System.out.println("5 - Exit");
        int selection = input.getInt(1, 5);

        if (selection == 1) {
            try {
                showContactList();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(selection == 2) {
            try {
                addContact();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if (selection == 3) {
            try {
                searchContact();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if (selection == 4) {
            try {
                deleteContact();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            System.out.println("Thank You for using Our Application");
            System.exit(0);
        }
    }
    public static void showContactList() throws IOException {
        Path contactListPath = Paths.get(directory, filename);

        List<String> contactList = Files.readAllLines(contactListPath);

        System.out.println("Name | Phone number\n" + "---------------");
        for (String person: contactList) {
            String user = person;
            String [] userSplit = user.split("(?<!\\G\\S+)\\s");
            String names = userSplit[0];
            String numbers = userSplit[1];
            System.out.println(names + " | "+  numbers + " |");
        }
        System.out.println();
        menu();
    }

    public static void addContact() throws IOException {
        Path contactListPath = Paths.get(directory, filename);

        if (Files.notExists(contactListPath)){
            Files.createFile(contactListPath);
        }

        List<String> contactLine = Files.readAllLines(contactListPath);
        String person = input.getString("Please Enter a New Contact:\nUsing Format: FirstName LastName Phonenumber\nExample: Bob Sterling 210-222-2222 or 2102222222 or 2222222");

        if (person.matches("(.*)(\\s)(.*)(\\s)(\\d{10})")) {
            person = person.replaceFirst("(.*)(.*)(\\d{3})(\\d{3})(\\d{4})", "$1$2$3-$4-$5");
            contactLine.add(person);
            Files.write(contactListPath, contactLine);
            menu();
        }else if (person.matches("(.*)(\\s)(.*)(\\s)(\\d{7})")){
            person = person.replaceFirst("(.*)(.*)(\\d{3})(\\d{4})(\\s{4})", "$1$2$3-$4");
            contactLine.add(person);
            Files.write(contactListPath, contactLine);
            menu();
        } else if (person.matches("(.*)(\\s)(.*)(\\s)(\\d{3}(\\W))(\\d{3})(\\W)(\\d{4})")){
            contactLine.add(person);
            Files.write(contactListPath, contactLine);
            menu();
        } else if (person.matches("(.*)(\\s)(.*)(\\s)(\\d{3})(\\W)(\\d{4})")) {
            contactLine.add(person);
            Files.write(contactListPath, contactLine);
            menu();
        } else {
            System.out.println("Next time follow the rules!");
            addContact();
        }
    }

    public static void searchContact() throws IOException {
        Path contactListPath = Paths.get(directory, filename);

        if (Files.notExists(contactListPath)){
            Files.createFile(contactListPath);
        }

        List<String> contactList = Files.readAllLines(contactListPath);

        String userInput = input.getString("Please Enter Name to Search: ").toLowerCase().replaceAll("\\s+","");

        for (String person: contactList) {
            String user = person;
            String [] userSplit = user.split("(?<!\\G\\S+)\\s");
            String name = userSplit[0];
            String nameEdit = name.replaceAll("\\s+","").toLowerCase();
            String dude = userSplit[1];
            if (userInput.equals(nameEdit)) {
                System.out.println(name + " | " + dude + " |");
            }
        }

        menu();
    }
    public static void deleteContact() throws IOException {
        File inputFile = new File ("src/data/contacts.txt");
        File tempFile = new File("tempFile.txt");

        BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

        String userInput = input.getString("Enter the Full Name of the contact to Delete: ").toLowerCase().replaceAll("\\s+","");
        String currentLine;

        while((currentLine = reader.readLine()) != null) {
           String [] currentLineArray = currentLine.split("(?<!\\G\\S+)\\s");
           String currentLineName = currentLineArray[0].replaceAll("\\s+","").toLowerCase();
            System.out.println(currentLineName);

           if (currentLineName.contains(userInput)) continue;

            writer.write(currentLine);
            writer.newLine();
        }
        writer.close();
        boolean successful = tempFile.renameTo(inputFile);
        menu();
    }
}
