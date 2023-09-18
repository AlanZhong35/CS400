import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

/**
 * Class contains the UI for the airplane app, allowing the user to interact
 * with the app. The UI would give users the choice to load a flight dataset, to
 * find the shortest three paths between two airports(lowest cost) along with
 * additional dataset statistics
 * 
 * @author akils
 * @implements AirportFrontendInterface - all the methods in frontend
 */
public class AirportFrontendFD implements AirportFrontendInterface {

	private Scanner userInput; // Useful for frontend tester
	private BackendInterface backend; // Useful for calling backend methods for getting
										// statistics for flight dataset as well as the shortest
										// three paths.

	/**
	 * Creates a new AirportFrontendFD() object, allows other classes to interact
	 * with UI
	 * 
	 * @param userInput the scanner used for UI testing
	 * @param backend   the backend object used to call backend methods(see above)
	 */
	public AirportFrontendFD(Scanner userInput, BackendInterface backend) {
		this.userInput = userInput;
		this.backend = backend;
	}

	/**
	 * Helper method to display a 79 column wide row of dashes: a horizontal rule.
	 */
	private void hr() {
		System.out.println("-------------------------------------------------------------------------------");
	}

	/**
	 * Gets the users input from the main menu prompt. Based on the option chosen by
	 * the user, the appropriate function is called to display the subsequent
	 * prompts, or to send a quit message. Also shows the welcome message to the app
	 *
	 */
	public void runCommandLoop() {
		hr(); // display welcome message
		System.out.println("Welcome to our Plane Flight Calculator! Please choose one of the" + 
		" following options:");
		hr();

		char command = '\0';
		while (command != 'Q') { // main loop continues until user chooses to quit
			// Shows user the list of options
			command = this.mainMenuPrompt();

			// Different options the user could chose
			// Calls respective methods
			switch (command) {
			case 'L': // [L]oad data of flights from file
				loadDataCommand();
				break;
			case 'G': // [G]et flight dataset statistics
				getStats();
				break;
			case 'F': // [F]ind shortest path between two destinations
				getPrice();
				break;
			case 'Q': // [Q]uit
				// Prints quit message and quits
				hr();
				quit();
				hr();
				break;
			default: // Default error message printed if a valid choice was not chosen
				System.out.println(
						"Didn't recognize that command.  Please type one of the letters "
						+ "presented within []s to identify the command you would like to choose.");
			}

			// Breaks out of loops if user chose to quit
			if (command == 'Q') {
				break;
			}
		}

	}

	/**
	 * Displays the main menu options which the user could choose. This inlcudes
	 * loading a new flight dataset, getting flight dataset statistics, finding the
	 * shortest path between two destinations, or quitting. Retrieves the user input
	 * chosen by the command.
	 */
	public char mainMenuPrompt() {
		// display menu of choices
		System.out.println("    [L]oad data of flights from file");
		System.out.println("    [G]et flight dataset statistics");
		System.out.println("    [F]ind shortest path between two destinations");
		System.out.println("    [Q]uit");

		// read in user's choice, and trim away any leading or trailing whitespace
		System.out.print("Choose command: ");
		String input = userInput.nextLine().trim();
		if (input.length() == 0) // if user's choice is blank, return null character
			return '\0';
		// otherwise, return an uppercase version of the first character in input
		return Character.toUpperCase(input.charAt(0));
	}

	/**
	 * Prompt user to enter filename, and display error message when loading fails.
	 * This is if the user chose the [L] option in the main menu prompt
	 */
	@Override
	public void loadDataCommand() {
		System.out.println("Please input the name of the dataset you want to load ");
		String filename = userInput.nextLine().trim();
		try {
			// Tries to load file in file path provided by user
			backend.loadData(filename);
			System.out.println("Data loaded successfully!");
		} catch (FileNotFoundException e) {
			// Takes user back to home page if file could not be loaded
			System.out.println("Error: Could not find or load file: " + filename);
		}
	}

	/**
	 * If the user selected the option [G] in the main menu prompt, then this prompt
	 * would show up, giving the user five options on which statistics to retrieve,
	 * shown below. Retrieves a valid input from the user and returns this the user
	 * input
	 */
	public int chooseGetStatisticsPrompt() {
		int output; // Contains the user's input parsed as an integer

		// Repeatedly asks user the prompt, until a valid option is given
		while (true) {
			System.out.println("These are the following statistics you can get:");
			System.out.println("    [1]: Get number of airport locations");
			System.out.println("    [2]: Get the minimum cost flight");
			System.out.println("    [3]: Get the maximum cost flight");
			System.out.println("    [4]: Get the flight with lowest distance traveled");
			System.out.println("    [5]: Get the flight with highest distance traveled");
			System.out.println("Choose option: ");
			String input = userInput.nextLine().trim();
			try {
				// Checks to see if user input is a integer
				output = Integer.parseInt(input);
				// Checks to see if number is either 1,2,3,4, or 5
				if (output > 5 || output <= 0) {
					System.out.println("Valid option not given");
				} else {
					break;
				}
			} catch (Exception e) {
				// Case when user did not give a number(numberFormatException).
				// Repeatdely asks user for valid input
				System.out.println("Valid option not given");
			}
		}
		// Returns users choice
		return output;

	}

	/**
	 * Gets the user input from the choose statistics prompt. Based on the choice
	 * selected by the user, the relevant backend methods are called to display the
	 * appropriate statistics in the dataset
	 * 
	 * @param a - an airport interface to pass to the backend methods
	 */
	public void getStats() {
		while (true) {
			// Gets the user selection on which statistc to see
			int input = chooseGetStatisticsPrompt();

			// Keeps track of whether the user entered a valid input
			int valid = 1;

			switch (input) {

			case 1: // User chose [1] Get number of airport locations
				getAirplaneLocations();
				break;
			case 2: // User chose [2] Get minimum cost flight
				getMinimumCostFlight();
				break;

			case 3: // User chose [3] Get maximum cost flight
				getMaximumCostFlight();
				break;

			case 4: // User chose [4] Get flight with lowest distance traveled
				getMinimumDistanceFlight();
				break;

			case 5: // User chose [5] Get flight with highest distance traveled
				getMaximumDistanceFlight();
				break;

			// Case when user does not choose a valid option
			default:
				System.out.println("Invalid option entered");
				valid = 0;
				break;
			}

			// Breaks only if user chose a valid option
			if (valid == 1) {
				break;
			}

		}

	}

	/**
	 * Displays the number of airport locations in dataset through calling the
	 * backend method getAirplaneLocations(). This function would be called if the
	 * user chose [1] in the get statistics prompt
	 */
	@Override
	public void getAirplaneLocations() {
		System.out.print("Displayed are the airplane locations: ");
		// Returns the number of flight locations through calling backend
		System.out.println(backend.getAirportLocations());
	}

	/**
	 * Displays the flight with the minimum cost between two locations. Calls the
	 * backend method to get the shortest cost flight in the dataset between two
	 * locations. This method is called if the user chose [2] on the getStatistics()
	 * prompt.
	 * 
	 * @param a - The airport interface to pass to backend
	 */
	@Override
	public void getMinimumCostFlight() {
		System.out.print("Displayed is the minimum cost flight: ");
		// Retrieves the path of the minimum cost flight
		FlightInterface p = backend.getMinimumCostFlight();
		// Gets the departure flight airport location
		System.out.print(p.getOriginAirport());
		System.out.print(" -> ");
		// Gets the arrival flight airport location
		System.out.println(p.getDestinationAirport());
		// Prints out the cost of the flight
		System.out.println("Cost of flight: " + p.getPrice() + "\n");
	}

	/**
	 * Displays the flight with the maximum cost between two locations. Calls the
	 * backend method to get the shortest cost flight in the dataset between two
	 * locations. This method is called if the user chose [3] on the getStatistics()
	 * prompt.
	 * 
	 * @param a - The airport interface to pass to backend
	 */
	@Override
	public void getMaximumCostFlight() {
		System.out.print("Displayed is the maximum cost flight: ");
		// Retrieves the path of the maximum cost flight in the dataset
		FlightInterface p = backend.getMaximumCostFlight();
		// Retrieves departure airport location
		System.out.print(p.getOriginAirport());
		System.out.print(" -> ");
		// Retrieves arrival airport location
		System.out.println(p.getDestinationAirport());
		// Prints out the cost of the flight
		System.out.println("Cost of flight: " + p.getPrice() + "\n");
	}

	/**
	 * Displays the flight with the minimum distance traveled. Calls the backend
	 * method to get the shortest distance flight. This method is called if the user
	 * chose [4] on the get statistics prompt
	 * 
	 * @param a - The airport interface to pass to backend
	 */
	@Override
	public void getMinimumDistanceFlight() {
		System.out.print("Displayed is the flight with lowest distance traveled: ");
		// Retrieves the minimum distance flight path from backend
		FlightInterface p = backend.getMinimumDistanceFlight();

		// Gets departing flight
		System.out.print(p.getOriginAirport());
		System.out.print(" -> ");
		// Gets arrival flight
		System.out.println(p.getDestinationAirport());
		// Prints out cost of flight
		System.out.println("Distance of flight: " + p.getMiles() + "\n");

	}

	/**
	 * Displays the flight with the maximum distance traveled. Calls the backend
	 * method to get the longest distance flight. This method is called if user
	 * chose [5] on the get statistics prompt
	 * 
	 * @param a - The airport interface to pass to backend
	 */
	@Override
	public void getMaximumDistanceFlight() {
		System.out.print("Displayed is the flight with highest distance traveled: ");
		// Retrieves path of maximum distance flight
		FlightInterface p = backend.getMaximumDistanceFlight();
		// Prints out the departure airport
		System.out.print(p.getOriginAirport());
		System.out.print(" -> ");
		// Prints out the arrival airport
		System.out.println(p.getDestinationAirport());
		// Prints out distance traveled between two airports
		System.out.println("Distance of flight: " + p.getMiles() + "\n");

	}

	/**
	 * If the user selected [L] on the main menu prompt, then this method would be
	 * called. The user would be asked for the departure and destination airports
	 * where the flight will take off and land. Then the backend method
	 * getShortestPaths() would be called such that using Dijkstra's algorithm, the
	 * shortest three paths with lowest cost would be found and displayed to the
	 * user
	 * 
	 * @param a - The airport interface to be sent to backend
	 */
	@Override
	public void getPrice() {
		// Gets the departure flight from user
		String departure = chooseAirportDeparture();
		// Gets the destination flight from the user
		String destination = chooseAirportDestination();
		// Gets the three shortest paths given destination and departure locations
		List<Path> results = backend.getShortestPaths(departure, 
				             destination);

		// Controls which flight path to show the user
		// 1 means the most shortest cost flight path would be shown
		// 2 means the second shortest cost flight path would be shown
		// 3 means the third shortest cost flight path would be shown
		// 4 means the user wants to return to main menu
		// Assumes the user initially wants to see the shortest cost flight path
		int optimalChosen = 1;
		while (true) {
			// Displays the appropriate heading based on the flight path the user
			// is about to see
			if (optimalChosen == 1) {
				System.out.println("To minimize the number of miles traveled and the "
						+ "price of your flight, the most optimal route to take" + " is: ");
			} else if (optimalChosen == 2) {
				if(results.get(1) == null) {
					System.out.println("There is no second most optimal route");
					break;
				}
				System.out.println("The second most optimal route to take is:");
			}

			else if (optimalChosen == 3) {
				if(results.get(2) == null) {
					System.out.println("There is no third most optimal route");
					break;
				}
				System.out.println("The third most optimal route to take is:");
			}

			// Breaks out of the loop and returns user to main menu if 4 is chosen
			else if (optimalChosen == 4) {
				break;
			}

			int nodeNum = 0; // Keeps track of number of airport location traversed.
								// If last airport, dont add arrow

			// Iterates through all the airports in the path which the user
			// is about to see
			for (String node : results.get(optimalChosen - 1).nodeList) {
				// Displays the path to the user
				System.out.print(node);
				if (results.get(optimalChosen - 1).nodeList.size() != nodeNum + 1) {
					System.out.print(" -> ");
				} else {
					System.out.println("\n");
				}
				nodeNum++; // Increments the count of number of airport locations

			}

			// Prints the price of the path, the number of miles traveled via the flight
			// path
			// and the number of layovers
			System.out.println("The price of the flight is: " + 
			                   backend.getFinalPrice(results.get(optimalChosen - 1)));
			getMilesTraveled(results.get(optimalChosen - 1));
			getNumLayovers(results.get(optimalChosen - 1));

			// Displays appropriate options based on the flight path the user selected. The
			// user should
			// have the option to either see the other two paths or to return to main menu
			// through
			// selecting M
			if (optimalChosen == 1) {
				System.out.println("\nClick:");
				System.out.println("	[2] to view the second most optimal flight route");
				System.out.println("	[3] to view the third most optimal flight route");
				System.out.println("	[M] to return back to the main menu");
			}

			// What would be displayed if user saw the second optimal flight path
			if (optimalChosen == 2) {
				System.out.println("\nClick:");
				System.out.println("	[1] to view the most most optimal flight route");
				System.out.println("	[3] to view the third most optimal flight route");
				System.out.println("	[M] to return back to the main menu");
			}

			// What would be displayed if user saw the third optimal flight path
			if (optimalChosen == 3) {
				System.out.println("\nClick:");
				System.out.println("	[1] to view the most optimal flight route");
				System.out.println("	[2] to view the second most optimal flight route");
				System.out.println("	[M] to return back to the main menu");
			}

			// Stores the users choice to the above prompt and either shows another flight
			// path or returns user to main menu
			optimalChosen = chooseOptimalFlightRoute();

		}
	}

	/**
	 * Gets the user input on which flight path to see, indicated by a number from 1
	 * through 4
	 * 
	 * @return a number indicating the flight path the user wants to see 1 means the
	 *         most shortest cost flight path would be shown 2 means the second
	 *         shortest cost flight path would be shown 3 means the third shortest
	 *         cost flight path would be shown 4 means the user wants to return to
	 *         main menu
	 */
	@Override
	public int chooseOptimalFlightRoute() {
		int pathChosen = 0; // The value which would store the path the user chose

		while (true) {
			// Gets input from user
			System.out.println("Choose option: ");
			String input = userInput.nextLine().trim();

			// Returns 4 if the user wants to return to the main menu
			if (input.equals("M")) {
				return 4;
			}
			try {
				// Checks to see if the user returned a number between 1 and 3 inclusive
				pathChosen = Integer.parseInt(input);
				if (pathChosen > 3 || pathChosen <= 0) {
					System.out.println("Valid integer not given");
				} else {
					break;
				}
			} catch (Exception e) {
				// If user does not return an integer
				System.out.println("Integer nor M charecter was given");
			}
		}

		// Returns the path number that the user chose
		return pathChosen;
	}

	/**
	 * Gets the departure flight 3 digit code for finding the least cost flight path
	 * using Dijkstras algorithim
	 * 
	 * @return the 3 digit code for the departure flight
	 */
	@Override
	public String chooseAirportDeparture() {
		// Gets 3 digit code for departure
		System.out.println("Please input the airport code of the destination location:");
		String input = userInput.nextLine().trim();
		return input;
	}

	/**
	 * Gets the destination flight 3 digit code for finding the least cost flight
	 * path using Dijkstras algorithim
	 * 
	 * @return the 3 digit code for the destination flight
	 **/
	@Override
	public String chooseAirportDestination() {
		// Gets three digit code for destination
		System.out.println("Please input the airport code of the departure location:");
		String input = userInput.nextLine().trim();
		return input;
	}

	/**
	 * Gets the number of miles traveled along a certain path through calling the
	 * backend method.
	 * 
	 * @param p - The path of the distance which needs to be calculated Usually the
	 *          path of the first, second, or third lowest cost flight between two
	 *          locations
	 */
	@Override
	public void getMilesTraveled(Path p) {
		// Calls backend method getDistanceTraveled() to get miles of flight traveled
		System.out.println("The number of miles traveled is: " + backend.getDistanceTraveled(p));

	}

	/**
	 * Gets the number of layovers along a certain path through calling the backend
	 * method.
	 * 
	 * @param p - The path of the number of layovers which needs to be calculated
	 *          Usually the path of the first, second, or third lowest cost flight
	 *          between two locations
	 */
	@Override
	public void getNumLayovers(Path a) {
		// Calls the backend method getNumLayovers() to get number of layovers
		
		System.out.println("The number of layovers are: " + backend.getNumLayovers(a));
	}

	/**
	 * If the user selected [Q], then the below message would be displayed, and the
	 * flight app would terminate
	 */
	@Override
	public void quit() {
		System.out.println("Thank you for using our flight simulator app!");
	}

	public static void main(String[] args) {
		// AirportFrontendFD airport = new AirportFrontendFD(new Scanner(System.in), new
		// AirportBackendFD(new BaseGraph(), new AirplaneReaderFD()));
		// airport.runCommandLoop();
	}

}
