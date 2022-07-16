package assignment.gameobjects;

import java.awt.Color;
import java.awt.Graphics2D;

import assignment.game.Game;
import assignment.math.Vector2D;
import assignment.physics.Collidable;
import assignment.physics.Physics;

import static assignment.util.Constants.*;

public class Puller extends GameObject {

	public static final Color PULLER_DEBUG_COLOUR = Color.orange;
	public static final Color PULLER_DEBUG_TARGET_VECTOR_COLOUR = Color.red;
	
	public static final double PULL_FORCE_SCALAR = 1.75;
	
	private GameObject target;
	private Vector2D pullDir;
	
	private double pullForce;
	private boolean pulling;
	
	public Puller(Vector2D pos, double radius) {
		super(pos, new Vector2D(), radius);
		target = null;
		pullDir = null;
		pulling = false;
		pullForce = radius * PULL_FORCE_SCALAR;
	}
	
	public void update() {
		if(target != null) {
			//System.out.println("target pos: " + target.position);
			//System.out.println("target vector: " + pullDir);
			if(!Physics.overlaps(this, target)) {
				stop();
			}
		}
	}

	@Override
	public void onCollision(Collidable col) {
		col.onCollision(this);
	}
	
	@Override
	public void onCollision(Ship ship) {
		if(!ship.isDead()) {
			pulling = true;
			pullTowards(ship);
		}
	}

	public void pullTowards(GameObject obj) {
		target = obj;
		pullDir = new Vector2D(position).subtract(target.position);
		pullDir.normalise();
		target.velocity.addScaled(pullDir, pullForce * DT);
	}
	
	// Releases the target
	public void stop() {
		//System.out.println("stopping pulling");
		pulling = false;
		target = null;
		pullDir = null;
	}
	
	public void increaseRadius(double amount) {
		radius += amount;
	}
	
	@Override
	public void draw(Graphics2D g2d, int xOffset, int yOffset) {
		Vector2D relativeCamPos = new Vector2D(position.x - xOffset, position.y - yOffset);
		
		if(Game.DEBUG) {
			g2d.setColor(PULLER_DEBUG_COLOUR);
			g2d.drawOval((int)(relativeCamPos.x - radius), (int)(relativeCamPos.y - radius), (int)(2*radius), (int)(2*radius));
			
			if(pulling) {
				g2d.setColor(PULLER_DEBUG_TARGET_VECTOR_COLOUR);
				g2d.drawLine((int)relativeCamPos.x, (int)relativeCamPos.y, (int)target.position.x - xOffset, (int)target.position.y - yOffset);
				//System.out.println("pulling");
			}
		}
	}
}
