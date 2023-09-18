import java.io.FileNotFoundException;
import java.util.Scanner;

public class FlightApp {

  public static void main(String[] args) throws FileNotFoundException {

 // Use data wrangler's code to load flight data
    FlightReaderInterface flightReader = new FlightReader();
    
    // Use algorithm engineer's code to store and search for data
    Graph<String, FlightInterface> graph = new Graph<String,FlightInterface>();
    
    // Use the backend developer's code to manage all app specific processing
    BackendInterface backend = new BackendBD(graph,flightReader);
    
    // Use the frontend developer's code to drive the text-base user interface
    Scanner scanner = new Scanner(System.in);
    AirportFrontendInterface frontend = new AirportFrontendFD(scanner, backend);
    
    
    
    frontend.runCommandLoop();
    
  }

}
