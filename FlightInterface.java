
/**
 * Interface for flights
 * @author amartens
 *
 */
public interface FlightInterface {
	//public FlightInterface(AirportInterface originAirport, AirportInterface destinationAirport, double price, double miles);
	public String getOriginAirport();
	public String getDestinationAirport();
	public double getPrice();
	public double getMiles();
	public abstract double value(); //Returns 0.05 * this.getMiles() + 0.95 * this.getPrice()
}
