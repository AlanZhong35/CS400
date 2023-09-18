
public interface AirportFrontendInterface {
  //public AirportFrontend(Scanner userInput, AirportBackendInterface backend);
  public void runCommandLoop();
  public char mainMenuPrompt();
  public void loadDataCommand();
  public String chooseAirportDestination();
  public String chooseAirportDeparture();
  public int chooseOptimalFlightRoute();
  public int chooseGetStatisticsPrompt();

  public void getStats();
  public void getPrice();
  public void getMilesTraveled(Path p);
  public void getNumLayovers(Path a);
  public void getAirplaneLocations();
  public void getMinimumCostFlight();
  public void getMaximumCostFlight();
  public void getMinimumDistanceFlight();
  public void getMaximumDistanceFlight();
  
  public void quit();


}
