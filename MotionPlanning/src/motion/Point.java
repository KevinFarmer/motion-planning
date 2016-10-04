package motion;

public class Point {

	public int x, y;
		
	public Point(int xCoord, int yCoord) {
		x = xCoord;
		y = yCoord;
	}
	
	public int getX() { return x; }
	public int getY() { return y; }
	
	public String toString() { return "("+x+","+y+")"; }
}
