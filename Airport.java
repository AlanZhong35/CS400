import java.util.Objects;

public class Airport implements AirportInterface{
    private String name;
    public Airport(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Airport airport = (Airport) o;
        return name.equals(airport.name);
    }
    
    public String toString() {
      return name;
  }
}

