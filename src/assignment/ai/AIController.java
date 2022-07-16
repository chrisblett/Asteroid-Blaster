package assignment.ai;

import assignment.gameobjects.Asteroid;
import assignment.gameobjects.EnemyShip;
import assignment.gameobjects.GameObject;
import assignment.gameobjects.PlayerShip;
import assignment.gameobjects.Ship;
import assignment.math.Vector2D;

public class AIController {
	
	public static GameObject findTarget(EnemyShip subject, PlayerShip player, Iterable<Asteroid> asteroids) {
		double minDistance = subject.getDetectionRadius();
		double dist;
		
		GameObject closest = null;
		
		for(Asteroid ast : asteroids) {
			dist = subject.position.dist(ast.position);
			if(dist < minDistance) {
				minDistance = dist;
				closest = ast;
			}
		}
		
		// Compare with the player, just in case they're closer
		dist = subject.position.dist(player.position);
		if(dist < minDistance) {
			minDistance = dist;
			closest = player;
		}
		return closest;
	}
	
	public static int aim(GameObject target, EnemyShip subject, double angle) {
		double EPSILON = 0;

		// If we have reached the target, stop turning
		if(Math.abs(angle) < EPSILON) {
			return 0;
		} else {
			// If the angle is positive, turn right, otherwise turn left
			return angle > 0 ? 1 : -1;
		}
	}
	
	public static double getAngleToTarget(Vector2D targetPos, Ship subject) {
		return subject.getDirection().angle(targetPos.subtract(subject.position));
	}
}
