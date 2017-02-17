package tools;

public class Map {
	
	public static Object[][] map = new Object[5][5];
	
	public Map(){
		
	}
	
	public static void init() {
		map[0][0] = new Rock("v1");
		map[2][1] = new Rock(false);
		//map[2][4] = new Rock();
		map[3][3] = new Rock(false);
		map[4][1] = new Rock("v2");
		//map[4][4] = new Rock(false);		
	}
	
	public static void initRandom() {
		// Randomly initializes our Map.
		// ...
	}
	
	public static Object[][] getMap() {
		return map;
	}
	
	public static String getCell(Coordonnee coord){
		return map[coord.getPositionX()][coord.getPositionY()].toString();
	}
	
	// Performs some tests...
	public static void infoOnMap() {
		for (int i=0; i<5; i++) {
			for (int j=0; j<5; j++) {
				if (map[i][j] != null) {
	 				if (map[i][j].toString().equals("F")) {
						System.out.println("F: " + "(" + i + "," + j + ")");
					}
				}
			}
		}
	}
}
