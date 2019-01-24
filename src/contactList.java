package src;
// Cmports needed methods and classes
import util.Input;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

// Class defining contactList and its associated variables
public class contactList {
    static Input input = new Input();
    static String directory = "src/data";
    static String filename = "contacts.txt";
    // Defining the path to access our contacts.txt
    public static Path contactListPath = Paths.get(directory, filename);
    // Defining a List of Strings called contactList
    public static List<String> contactList;

    // Reading line by line and implementing them as individual items in the String List, on failure is caught
    static {
        try {
            contactList = Files.readAllLines(contactListPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Creating an ArrayList of objects(person) called contacts
    public static List<Person> contacts = new ArrayList<>();

    //main to start application
    public static void main(String[] args) throws IOException {
            start();
    }

    // Start method responsible for starting application
    public static void start() throws IOException {
        System.out.println("Welcome to your Contacts List");

        // For Loop that iterates over contactList String List
        for (String con: contactList) {
            // Defining an array of strings called userSplit and making it equal to the contact split on second space
            String [] userSplit = con.split("(?<!\\G\\S+)\\s");
            // Defining a new Person Object named contactP that takes in two parameters
            Person contactP = new Person("","");
            // Sets the value of the first set of key value pairs
            contactP.setName(userSplit[0]);
            // Sets the value of the second set of key value pairs
            contactP.setPhoneNumber(userSplit[1]);
            // Adding the object(contactP) to the ArrayList contacts
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
        boolean deleted = false;
        for (Person person: contacts){
            count++;
            if (userInput.equals(person.getName().toLowerCase().replaceAll("\\s+",""))) {
                contacts.remove(person);
                deleted = true;
                break;
            }
        }
        if (!deleted) {
            System.out.println("User not in Contacts, try again");
            deleteContact();
        }
        menu();
    }
}
