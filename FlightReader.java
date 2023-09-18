import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;


public class FlightReader implements FlightReaderInterface{

    @Override
    public void readFlightsFromFile(String filename) throws FileNotFoundException {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        try {
            String text;
            while ((text = br.readLine()) != null) {
                try {
                    String[] sections = text.split(",");
                    Airport origin = new Airport(sections[0]);
                    Airport destination = new Airport(sections[1]);
                    if (!airports.contains(origin)) {
                        airports.add(origin);
                    }
                    if (!airports.contains(destination)) {
                        airports.add(destination);
                    }
                    Flight flight = new Flight(origin, destination, Double.parseDouble(sections[2]),
                            Double.parseDouble(sections[3]));
                    if (!flights.contains(flight)) {
                        flights.add(flight);
                    }
                }catch (Exception ignored) {}
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<AirportInterface> getAirports() {
        return airports;
    };
    public List<FlightInterface> getFlights() {
        return flights;
    };

}

