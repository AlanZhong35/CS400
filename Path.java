import java.util.ArrayList;
import java.util.List;

/**
 * Resembles an airplane path. It includes the starting node(resembling the airplane departure \
 * location), the last node(the airplane landing location), and any other nodes in the path representing
 * layovers. 
 * 
 * @author akils
 *
 */
public class Path {
		public List<String> nodeList; //List represeting all airports in path
		public Path(List<String> nodeList) {
			this.nodeList = nodeList;
		}
	}