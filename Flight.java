import java.util.Objects;

public class Flight implements FlightInterface{
    private AirportInterface origin;
    private AirportInterface destination;
    private double price;
    private double miles;
    public Flight(AirportInterface originAirport, AirportInterface destinationAirport, double price,
                  double miles) {
        this.origin = originAirport;
        this.destination = destinationAirport;
        this.price = price;
        this.miles = miles;
    }

    @Override
    public String getOriginAirport() {
        return origin.getName();
    }

    @Override
    public String getDestinationAirport() {
        return destination.getName();
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public double getMiles() {
        return miles;
    }

    @Override
    public double value() {
        return 0.05 * miles + 0.95 * price;
    }

    public int compareTo(FlightInterface o) {
        if (value() > o.value())
            return 1;
        else if (value() < o.value())
            return -1;
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Flight flight = (Flight) o;
        return Double.compare(flight.price, price) == 0 && Double.compare(flight.miles, miles) == 0 && Objects.equals(origin, flight.origin) && Objects.equals(destination, flight.destination);
    }

    @Override
    public int hashCode() {
        return Objects.hash(origin, destination, price, miles);
    }
}

