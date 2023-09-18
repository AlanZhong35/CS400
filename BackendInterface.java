import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public interface BackendInterface {
   //public backendInterface(nodeGraph gp, airplaneReader airplaneReader);


  public int getDistanceTraveled(Path path);

  public List<Path> getShortestPaths(String a,String b);

  public double getFinalPrice(Path path);



  public int getNumLayovers(Path path);
  public List<String> getAirportLocations();
  public FlightInterface getMinimumCostFlight();
  public FlightInterface getMaximumCostFlight();
  public FlightInterface getMinimumDistanceFlight();
  public FlightInterface getMaximumDistanceFlight(); 





   public void loadData(String fileName) throws FileNotFoundException;
   
  


}

