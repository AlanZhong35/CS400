import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class BackendBD implements BackendInterface {

  private GraphADT<String, FlightInterface> graph; // graph that this class uses
  private FlightReaderInterface reader; // reader that this class uses
  private List<AirportInterface> nodeList; // list of airport nodes to insert
  private List<FlightInterface> edgeList; // list of edges to insert

  public BackendBD(GraphADT<String, FlightInterface> graph, FlightReaderInterface reader) {
    this.graph = graph; // initialize graph
    this.reader = reader; // initialize flight reader
  }

  /**
   * This method passes in a path of flights and gets the total distance traveled
   * 
   * @param - path of airport flights
   * @return int - total miles
   */
  @Override
  public int getDistanceTraveled(Path path) {
    int total = 0;
    for (int i = 0; i < path.nodeList.size() - 1; i++) { // traverse the path and sum the miles from
                                                         // the edges to each node
      FlightInterface edge = graph.getEdge(path.nodeList.get(i), path.nodeList.get(i + 1));
      total += edge.getMiles(); // sum the miles data from each edge
    }
    return total;
  }

  /**
   * This method gets the top three shortest paths between two airports
   * 
   * @author Alex Martens
   * @param - AirportInterface a: origin - AirportInterface b: destination
   * @return - A list of three paths with shortest path being at index 0 and 3rd shortest at index 2
   */
  @Override
  public List<Path> getShortestPaths(String a, String b) {
    ArrayList<Path> paths = new ArrayList<Path>();
    List<String> path = graph.shortestPathData(a, b);
    paths.add(new Path(path));// Adds the shortest path to paths
    double lowestCost = Double.MAX_VALUE;
    List<String> lowestPath = null; // Makes the path with the lowest cost
    for (int i = 0; i < path.size() - 1; i++) { // Finds the second shortest path
      FlightInterface edge = graph.getEdge(path.get(i), path.get(i + 1));
      graph.removeEdge(edge.getOriginAirport(), edge.getDestinationAirport());
      if (graph.shortestPathCost(a, b) < lowestCost) {
        lowestCost = graph.shortestPathCost(a, b);
        lowestPath = graph.shortestPathData(a, b);
      }
      graph.insertEdge(edge.getOriginAirport(), edge.getDestinationAirport(), edge);
    }
    if (lowestPath == null) { // If there is no second path, return paths
      return paths;
    }
    paths.add(new Path(lowestPath));
    lowestCost = Double.MAX_VALUE;
    lowestPath = null; // Resets the lowest path
    for (int i = 0; i < paths.get(0).nodeList.size() - 1; i++) { // Finds the third shortest path by
                                                                 // iterating through the first two
                                                                 // and removing edges
      FlightInterface edge1 =
          graph.getEdge(paths.get(0).nodeList.get(i), paths.get(0).nodeList.get(i + 1));
      graph.removeEdge(edge1.getOriginAirport(), edge1.getDestinationAirport());
      for (int j = 0; j < paths.get(1).nodeList.size() - 1; j++) {
        try {
          FlightInterface edge2 = (FlightInterface) graph.getEdge(paths.get(1).nodeList.get(j),
              paths.get(1).nodeList.get(j + 1));
          graph.removeEdge(edge2.getOriginAirport(), edge2.getDestinationAirport());
          if (graph.shortestPathCost(a, b) < lowestCost) {
            lowestCost = graph.shortestPathCost(a, b);
            lowestPath = graph.shortestPathData(a, b);
          }
          graph.insertEdge(edge2.getOriginAirport(), edge2.getDestinationAirport(), edge2);
        } catch (Exception e) {

        }
      }
      graph.insertEdge(edge1.getOriginAirport(), edge1.getDestinationAirport(), edge1);
    }
    paths.add(new Path(lowestPath)); // Adds the third shortest path
    return paths; // this method is from Algorithm Engineer
  }

  /**
   * This method returns the total price of a certain path of flights
   * 
   * @param - path of airport flights
   * @return double - the total price of the path
   */
  @Override
  public double getFinalPrice(Path path) {
    int total = 0;
    for (int i = 0; i < path.nodeList.size() - 1; i++) { // traverse the path and sum the miles from
      // the edges to each node
      FlightInterface edge = graph.getEdge(path.nodeList.get(i), path.nodeList.get(i + 1));
      total += edge.getPrice(); // sum the price data from each edge
    }
    return total;
  }


  /**
   * This method returns the number of layovers of a flight(airports reached that are not the start
   * and end destinations)
   * 
   * @param - path of airport flights
   * @return - int - number of layovers
   */
  @Override
  public int getNumLayovers(Path path) {
    return path.nodeList.size() - 2; // minus 2 since layover does not count the start and end
                                     // destinations
  }

  /**
   * This method returns a list (no duplicates) of all airport locations loaded into the app
   * 
   * @return List of type string of all unique airport locations
   */
  @Override
  public List<String> getAirportLocations() {
    ArrayList<String> ret = new ArrayList<String>();
    for (AirportInterface a : this.nodeList) { // nodelist is created in loadData() from
                                               // DataWrangler's getAirports method
      if (!ret.contains(a.toString())) { // want to return non-duplicate airport locations
        ret.add(a.toString());
      }
    }
    return ret;
  }

  /**
   * This method returns the flight that has the lowest cost in the entire dataset
   * 
   * @return flight with the lowest cost
   */
  @Override
  public FlightInterface getMinimumCostFlight() {
    FlightInterface min = this.edgeList.get(0); // set min to first flight in edgelist
    for (FlightInterface a : this.edgeList) { // edgelist is created in loadData() from
                                              // DataWrangler's getFlights method
      if (a.getPrice() < min.getPrice()) {
        min = a; // change the minimum if lower price is found
      }
    }
    return min;
  }

  /**
   * This method returns the flight that has the highest cost in the entire dataset
   * 
   * @return flight with the highest cost
   */
  @Override
  public FlightInterface getMaximumCostFlight() {
    FlightInterface max = this.edgeList.get(0); // set max to first flight in edgelist
    for (FlightInterface a : this.edgeList) { // edgelist is created in loadData() from
                                              // DataWrangler's getFlights method
      if (a.getPrice() > max.getPrice()) {
        max = a; // change the maximum if higher price is found
      }
    }
    return max;
  }

  /**
   * This method returns the flight that has the lowest distance in miles in the entire dataset
   * 
   * @return flight with the lowest distance
   */
  @Override
  public FlightInterface getMinimumDistanceFlight() {
    FlightInterface min = this.edgeList.get(0); // set min to first flight in edgelist
    for (FlightInterface a : this.edgeList) { // edgelist is created in loadData() from
                                              // DataWrangler's getFlights method
      if (a.getMiles() < min.getMiles()) {
        min = a; // change the minimum if lower distance is found
      }
    }
    return min;
  }

  /**
   * This method returns the flight that has the highest distance in miles in the entire dataset
   * 
   * @return flight with the highest distance
   */
  @Override
  public FlightInterface getMaximumDistanceFlight() {
    FlightInterface max = this.edgeList.get(0); // set max to first flight in edgelist
    for (FlightInterface a : this.edgeList) { // edgelist is created in loadData() from
                                              // DataWrangler's getFlights method
      if (a.getMiles() > max.getMiles()) {
        max = a; // change the maximum if higher distance is found
      }
    }
    return max;
  }

  /**
   * This method loads data from a dotgraph file and uses Datawrangler methods to add edges and
   * nodes to the graph
   * 
   * @param fileName - name of dotgraph file
   */
  @Override
  public void loadData(String fileName) throws FileNotFoundException {
    this.reader.readFlightsFromFile(fileName); // parse the file

    // access the list of airports and flights
    edgeList = this.reader.getFlights();
    nodeList = this.reader.getAirports();

    // insert the nodes (String - Airports) into the graph, this loop must be done before edges are
    // inserted
    for (AirportInterface a : this.nodeList) {
      if (!graph.containsNode(a.toString())) {
        graph.insertNode(a.toString()); // graph takes in string, while reader uses Airport
                                        // Interface, use toString to resolve
      }
    }

    // insert the edges (Flights) into the graph, do this after the nodes or else errors...
    for (FlightInterface f : this.edgeList) {
      graph.insertEdge(f.getOriginAirport(), f.getDestinationAirport(), f);
    }
  }

}
