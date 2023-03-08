package WeatherData.UserInterface;


import WeatherData.Logic.SearchLogic;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * This class runs the command line user interface, functionality reduced for
 * usability for code-sample purposes - See Main project for full functionality.
 * In this code-sample version hands over responsibility for search to
 * SearchLogic
 *
 * @author PalMar
 */
public class UserInterface {

    private final SearchLogic logic;
    private final BufferedReader reader;
    private String connectionStatus;
    private String errorStatus;

    /**
     * Constructor for UserInterface. Nothing fancy, initializes Reader and
     * Logic for UserInterface to use in running of program.
     *
     * @param reader Instance of BufferedReader
     * @param logic Instance of Logic
     */
    public UserInterface(BufferedReader reader, SearchLogic logic) {
        this.reader = reader;
        this.logic = logic;
        this.connectionStatus = "";
        this.errorStatus = "";
    }

    /**
     * Starts user interface. Prints user welcome-message with any needed info
     * and then hands over running of user interface to
     * handleFunctionality-method
     */
    public void start() {
        printIntroText();
        runMainMenu();
    }

    /**
     * Prints information for user on startup, includes cautions.
     */
    private void printIntroText() {
        System.out.println("Welcome to the WeatherApp! (Code Sample version)");
        System.out.println("This program allows you to access the FMI"
                + " Open weather data");
        System.out.println("\n!!! CAUTION! Ensure you have a working internet connection,"
                + " otherwise you will not be able to use this program. !!!");
        System.out.println("\nNow let's get started!");
    }

    /**
     * This method handles main loop of user interaction, sending off any
     * search-requests to SearchLogic for further action returns to main
     * run-method when user types 'quit'
     *
     * @return error message to main function or "" empty String if no errors
     * occurred during run
     */
    private void runMainMenu() {
        String command = "";
        while (true) {
            printOptions(); // search or quit
            command = readInput(); // search, quit or error
            if (command.equalsIgnoreCase("quit")) {
                break;
            } else if (command.equalsIgnoreCase("error")) {
                this.connectionStatus = "error";
                break;
            } else if (command.equalsIgnoreCase("search")) {
                runSearchOptionMenu();
                errorStatus = logic.getConnectionStatus();
                runParsingOptionMenu();
            }
        }
    }

    /**
     * Prints what options user has. In this version of the program user can
     * choose between a Search or Quit.
     */
    private void printOptions() {
        System.out.println("\nHere are your options:");
        System.out.println(" search - searches for data from FMI");
        System.out.println(" quit - ends this program");
    }

    /**
     * Handles search function, giving in this version three valid options:
     * Helsinki, Hanko and Lohja. More options are perfectly valid, but are not
     * implemented in this truncated version.
     */
    public void runSearchOptionMenu() {
        String place = "";
        while (true) {
            System.out.println("\nSearch for data from Helsinki, Hanko or Lohja");
            place = readInput();
            if (place.equalsIgnoreCase("lohja")) {
                performRequest(place);
                break;
            } else if (place.equalsIgnoreCase("helsinki")) {
                performRequest(place);
                break;
            } else if (place.equalsIgnoreCase("hanko")) {
                performRequest(place);
                break;
            } 
        }
    }

    /**
     * Gets requested data from SearchLogic -> FMI.
     * @param place Parameter given for query, here limited to three known valid
     * locations Helsinki, Hanko and Lohja.
     */
    private void performRequest(String place) {
        logic.getDataFromFMI(place);
        this.connectionStatus = logic.getConnectionStatus();
        System.out.println(logic.getBodyOfMessage());
    }

    /**
     * Runs menu for selecting whether to parse input. If no, returns to main
     * menu.
     */
    private void runParsingOptionMenu() {
        System.out.println("\nHere is the data in XML-format - "
                + " do you wish to parse it? Yes / No");
        String command = "";
        while (true) {
            command = readInput();
            if (command.equalsIgnoreCase("yes")) {
                logic.parseInput();
                System.out.println(logic.getParsedMessage());
                break;
            } else if (command.equalsIgnoreCase("no")) {
                break;
            }
        }
    }

    /**
     * Reads user input from command line.
     *
     * @return User input in String or error if occurred
     */
    private String readInput() {
        String input;
        try {
            input = reader.readLine();
        } catch (IOException ex) {
            input = "error";
        }
        return input;
    }
}
