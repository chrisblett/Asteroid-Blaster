package assignment.input;

public class Action {
	// 0 = off, 1 = on
	public int thrust;
	// -1 = left turn, 0 = no turn, 1 = right turn
	public int turn;
	public boolean shoot;
	public boolean shield;
	
	public String toString() {
		String s = new String();
		s = "thrust: " + thrust + "\n" 
		+   "turn:   " + turn   + "\n"
		+   "shoot:  " + shoot  + "\n";
		return s;
	}
}
