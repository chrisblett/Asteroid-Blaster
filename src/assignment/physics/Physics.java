package assignment.physics;

import assignment.gameobjects.GameObject;

public class Physics {
	
	public static boolean overlaps(GameObject a, GameObject b) {
		double dist = a.position.dist(b.position);
		return dist < a.getRadius() + b.getRadius();
	}	
}
