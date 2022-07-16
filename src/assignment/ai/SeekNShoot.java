package assignment.ai;

import static assignment.util.Constants.DT;

import assignment.game.World;
import assignment.gameobjects.EnemyShip;
import assignment.gameobjects.GameObject;
import assignment.input.Action;
import assignment.input.Controller;
import assignment.math.Vector2D;

public class SeekNShoot implements Controller {
	
	public static final double STOPPING_FACTOR = 5;
	
	private double stoppingDistance;
	
	private Action action;	
	private World world;
	private EnemyShip subject;
	
	private boolean chasing;
	private GameObject target;

	public SeekNShoot(World world, EnemyShip subject) {
		this.world = world;
		this.action = new Action();
		this.subject = subject;
		
		target = null;
	}
	
	@Override
	public Action getAction() {
	
		// Find a target if we are not in the chasing state, as we want to prioritise
		// the current target over new targets.
		if(!chasing) {
			target = AIController.findTarget(subject, world.getPlayer(), world.getAsteroids());
			subject.setCurrentTarget(target);
			
			if(target == null) {
				action.turn = 0;
				action.thrust = 0;
				action.shoot = false;
				subject.setDetectionRadius(EnemyShip.ENEMY_DETECTION_RADIUS);
				return action;
				
			  // Prioritise chasing a player, so increase the detection radius
			} else if(target == world.getPlayer()) {
				chasing = true;
				subject.setDetectionRadius(EnemyShip.ENEMY_DETECTION_RADIUS * 2);
			} 
		}
		
		if(target.isDead()) {
			stop();
			return action;
		}
		
		//System.out.println("target: " + target);
		
		//System.out.println("owner velocity mag: " + subject.velocity.mag());
		// Adjust the stopping distance according to the subject's velocity
		stoppingDistance = subject.getDetectionRadius() * (subject.velocity.mag() * DT) / STOPPING_FACTOR;
		//System.out.println("stopping distance: " + stoppingDistance);
		
		// Add the target's velocity for prediction
		Vector2D prediction = new Vector2D(target.position).addScaled(target.velocity, DT * 10);
		
		// Calculate the angle with the predicted position
		double angleToTarget = AIController.getAngleToTarget(prediction, subject);
		
		// Compute turn amount
		action.turn = AIController.aim(target, subject, angleToTarget);
		
		if(Math.abs(angleToTarget) < EnemyShip.MAX_ANGLE) {
			action.shoot = true;
			
			if(chasing) {
				System.out.println("CHASING: " + target);
				// If we get too close, slow down, otherwise accelerate
				if(subject.dist(target) <= stoppingDistance) {
					action.thrust = 0;
				} else {
					action.thrust = 1;
				}
				
				// Stop chasing if the target is out of range
				if(subject.dist(target) > subject.getDetectionRadius()) {
					stop();
				}
				
				System.out.println("target dead: " + target.isDead());
				
				if(target.isDead()) {
					stop();
				}
			} 
		}
	
		return action;
	}
	
	private void stop() {
		chasing = false;
		action.turn = 0;
		action.thrust = 0;
		action.shoot = false;
		// Reset the enemy's detection radius back to the default value
		subject.setDetectionRadius(EnemyShip.ENEMY_DETECTION_RADIUS);	
	}
}
