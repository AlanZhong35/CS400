import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public interface FlightReaderInterface {
    List<FlightInterface> flights = new ArrayList<>();
    List<AirportInterface> airports = new ArrayList<>();

    public void readFlightsFromFile(String filename) throws FileNotFoundException;

    public List<AirportInterface> getAirports();
    public List<FlightInterface> getFlights();
}

