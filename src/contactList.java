package src;

import util.Input;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class contactList {
    static Input input = new Input();
    static String directory = "src/data";
    static String filename = "contacts.txt";
    public static Path contactListPath = Paths.get(directory, filename);
    public static List<String> contactList;

    static {
        try {
            contactList = Files.readAllLines(contactListPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Person> contacts = new ArrayList<>();

    public static void main(String[] args) throws IOException {
            start();
    }
    public static void start() throws IOException {
        System.out.println("Welcome to your Contacts List");
        for (String con: contactList) {
            String [] userSplit = con.split("(?<!\\G\\S+)\\s");
            String name = userSplit[0];
            String numbers = userSplit[1];
            Person contactP = new Person("","");
            contactP.setName(name);
            contactP.setPhoneNumber(numbers);
            contacts.add(contactP);
        }
        menu();
    }
    public static void menu() throws IOException {
        System.out.println("Please Select One:");
        System.out.println("1 - Show all your contacts");
        System.out.println("2 - Add Contact");
        System.out.println("3 - Search by Name");
        System.out.println("4 - Delete existing Contact");
        System.out.println("5 - Exit");
        int selection = Input.getInt(1, 5);

        if (selection == 1) {
                showContactList();
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
                deleteContact();
        }else {
            System.out.println("Thank You for using Our Application");
            File inputFile = new File ("src/data/contacts.txt");
            File tempFile = new File("tempFile.txt");

            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            for (Person person: contacts) {
                writer.write(person.getName() + " " + person.getPhoneNumber());
                writer.newLine();
            }
            writer.close();
            boolean successful = tempFile.renameTo(inputFile);
            System.exit(0);
        }
    }
    public static void showContactList() throws IOException {
        System.out.println("Name | Phone number\n" + "---------------");
        for (Person person: contacts) {
            String user = person.getName();
            String numb = person.getPhoneNumber();
            System.out.println(user + " | "+  numb + " |");
        }
        System.out.println();
        menu();
    }

    public static void addContact() throws IOException {

        if (Files.notExists(contactListPath)){
            Files.createFile(contactListPath);
        }

        String person = input.getString("Please Enter a New Contact:\nUsing Format: FirstName LastName Phonenumber\nExample: Bob Sterling 210-222-2222 or 2102222222 or 2222222");

        if (person.matches("(.*)(\\s)(.*)(\\s)(\\d{10})")) {
            person = person.replaceFirst("(.*)(.*)(\\d{3})(\\d{3})(\\d{4})", "$1$2$3-$4-$5");
            String [] temp = person.split("(?<!\\G\\S+)\\s");
            String name = temp[0];
            String numbers = temp[1];
            Person persons = new Person("","");
            persons.setName(name);
            persons.setPhoneNumber(numbers);
            contacts.add(persons);
            menu();
        }else if (person.matches("(.*)(\\s)(.*)(\\s)(\\d{7})")){
            person = person.replaceFirst("(.*)(.*)(\\d{3})(\\d{4})(\\s{4})", "$1$2$3-$4");
            String [] temp = person.split("(?<!\\G\\S+)\\s");
            String name = temp[0];
            String numbers = temp[1];
            Person persons = new Person("","");
            persons.setName(name);
            persons.setPhoneNumber(numbers);
            contacts.add(persons);
            menu();
        } else if (person.matches("(.*)(\\s)(.*)(\\s)(\\d{3}(\\W))(\\d{3})(\\W)(\\d{4})")){
            String [] temp = person.split("(?<!\\G\\S+)\\s");
            String name = temp[0];
            String numbers = temp[1];
            Person persons = new Person(" ","");
            persons.setName(name);
            persons.setPhoneNumber(numbers);
            contacts.add(persons);
            menu();
        } else if (person.matches("(.*)(\\s)(.*)(\\s)(\\d{3})(\\W)(\\d{4})")) {
            String [] temp = person.split("(?<!\\G\\S+)\\s");
            String name = temp[0];
            String numbers = temp[1];
            Person persons = new Person("","");
            persons.setName(name);
            persons.setPhoneNumber(numbers);
            contacts.add(persons);
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

        String userInput = input.getString("Please Enter Name to Search: ").toLowerCase().replaceAll("\\s+","");

        for (Person person: contacts) {
            String user = person.getName();
            String nameEdit = user.replaceAll("\\s+","").toLowerCase();
            String dude = person.getPhoneNumber();
            if (userInput.equals(nameEdit)) {
                System.out.println(user + " | " + dude + " |");
                System.out.println();
            }
        }
        menu();
    }
    public static void deleteContact() throws IOException {

        String userInput = input.getString("Enter the Full Name of the contact to Delete: ").toLowerCase().replaceAll("\\s+","");
        int count = -1;
        for (Person person: contacts){
            count++;
            System.out.println(person.getName());
            if (userInput.equals(person.getName().toLowerCase().replaceAll("\\s+",""))){
                break;
            }else {
                System.out.println("Sorry User is not in your contacts! Try again");
                deleteContact();
            }
        }
        menu();
    }
}
