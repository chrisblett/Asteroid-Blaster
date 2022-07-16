package assignment.ai;

import assignment.game.World;
import assignment.gameobjects.EnemyShip;
import assignment.gameobjects.GameObject;
import assignment.input.Action;
import assignment.input.Controller;
import assignment.math.Vector2D;

import static assignment.util.Constants.*;

public class AimNShoot implements Controller {

	private Action action;	
	private World world;
	private EnemyShip owner;

	public AimNShoot(World world, EnemyShip owner) {
		this.world = world;
		this.action = new Action();
		this.owner = owner;
	}
	
	// Perform the action when called
	@Override
	public Action getAction() {
		// Find a target
		GameObject target = AIController.findTarget(owner, world.getPlayer(), 
												    world.getAsteroids());
		owner.setCurrentTarget(target);
		
		if(target == null) {
			action.turn = 0;
			action.shoot = false;
			return action;
		}
		
		// We have a target, find the angle between us
		Vector2D targetPos = new Vector2D(target.position);
		
		// Add the target's velocity for prediction
		Vector2D prediction = new Vector2D(targetPos).addScaled(target.velocity, DT * 10);
		
		// Calculate the angle with the predicted position
		double angleToTarget = AIController.getAngleToTarget(prediction, owner);
		
		// Compute turn amount
		action.turn = AIController.aim(target, owner, angleToTarget);
		
		action.shoot = Math.abs(angleToTarget) < EnemyShip.MAX_ANGLE;

		return action;
	}
}
