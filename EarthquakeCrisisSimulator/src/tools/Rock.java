package tools;

public class Rock {

	// attributes...
	public final String letter = "R";
	public boolean isSomeoneThere;
	public String victimName;
	
	public Rock() {
		isSomeoneThere = true;
		victimName = null;
	}
	
	public Rock(boolean is) {
		isSomeoneThere = is;
		victimName = null;
	}
	
	public Rock(String vName) {
		isSomeoneThere = true;
		victimName = vName;
	}
	
	public String toString() {
		return letter;
	}
	
	public String getVictimName() {
		return victimName;
	}
}
 