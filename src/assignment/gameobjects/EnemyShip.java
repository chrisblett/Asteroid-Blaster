package assignment.gameobjects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;

import assignment.ai.SeekNShoot;
import assignment.game.Game;
import assignment.game.World;
import assignment.input.Action;
import assignment.math.Vector2D;
import assignment.physics.Collidable;
import assignment.resource.ResourceManager;
import assignment.resource.Sprite;

public class EnemyShip extends Ship {
	
	public static final Color ENEMY_DEBUG_COLOUR = Color.pink;
	public static final Color ENEMY_DEBUG_TARGET_VECTOR_COLOUR = Color.red;
	public static final Color ENEMY_DEBUG_DETECTION_RADIUS_COLOUR = Color.green;
	public static final Color ENEMY_DEBUG_VIEW_ARC_COLOUR = Color.magenta;
	
	// The angle threshold at which it will start its target shooting at
	public static final double MAX_ANGLE = Math.PI/3;	
	
	// Movement
	public static final double ENEMY_STEER_RATE = Math.PI/2;
	
	// Firing
	public static final int ENEMY_FIRE_INTERVAL = 30;
	
	// AI
	public static final double ENEMY_DETECTION_RADIUS = 300;
	
	private GameObject currentTarget;
	private double angleToTarget;
	private double detectionRadius;
	
	public EnemyShip(Vector2D pos, Vector2D vel, Vector2D dir, double radius, World world) {
		super(pos, vel, radius, world);
		direction = dir;
		
		Image img = ResourceManager.getTexture("enemy.png");
		sprite = new Sprite(img, position, direction, 2*radius, 2*radius);
		
		bulletImage = Ship.DEFAULT_BULLET_IMAGE;
		
		fireSFX = ResourceManager.getAudioClip("enemy_fire.wav");
	
		ctrl = new SeekNShoot(this.world, this);
		
		steerRate = ENEMY_STEER_RATE;
		fireInterval = ENEMY_FIRE_INTERVAL;
		
		currentTarget = null;
		detectionRadius = ENEMY_DETECTION_RADIUS;	
	}
	
	public void update() {
		Vector2D oldPos = new Vector2D(position.x, position.y);
	
		Action action = ctrl.getAction();
		
		processMovement(action);
		processFiring(action);
		
		super.update();
		
		keepWithinBounds(oldPos);
	}

	@Override
	public void onCollision(Collidable col) {
		col.onCollision(this);
	}
	
	@Override
	public void onCollision(Ship s) {
		System.out.println("enemyship hit by another ship");
		onHit();
	}

	public void setCurrentTarget(GameObject target) {
		currentTarget = target;
	}
	
	public void setDetectionRadius(double amount) {
		detectionRadius = amount;
	}
	
	public GameObject getCurrentTarget() {
		return currentTarget;
	}
	
	public double getDetectionRadius() {
		return detectionRadius;
	}

	public double getAngleToTarget() {
		return angleToTarget;
	}

	@Override
	public void draw(Graphics2D g2d, int xOffset, int yOffset) {
		Vector2D relativeCamPos = new Vector2D(position.x - xOffset, position.y - yOffset);
		
		sprite.draw(g2d, xOffset, yOffset);
			
		if(Game.DEBUG) {
			g2d.setColor(ENEMY_DEBUG_COLOUR);
			g2d.drawOval((int)(relativeCamPos.x - radius), 
					     (int)(relativeCamPos.y - radius), (int)(2*radius), (int)(2*radius));
			
			g2d.drawLine((int)relativeCamPos.x, (int)relativeCamPos.y, 
					     (int)(relativeCamPos.x + direction.x * (3*SHIP_RADIUS)), 
					     (int)(relativeCamPos.y + direction.y * (3*SHIP_RADIUS)));
			
			// Target
			if(currentTarget != null) {
				g2d.setColor(ENEMY_DEBUG_TARGET_VECTOR_COLOUR);
				g2d.drawLine((int)relativeCamPos.x, 
							 (int)relativeCamPos.y, 
							 (int)currentTarget.position.x - xOffset, 
							 (int)currentTarget.position.y - yOffset);
				
				g2d.setColor( ENEMY_DEBUG_VIEW_ARC_COLOUR);
				g2d.drawArc((int)(relativeCamPos.x - detectionRadius), 
						    (int)(relativeCamPos.y -  detectionRadius), 
						    (int)(2*detectionRadius), (int)(2*detectionRadius),
						    (int)( Math.toDegrees(Math.atan2(-direction.y, direction.x) - EnemyShip.MAX_ANGLE / 2) ), 
						    (int)(Math.toDegrees(EnemyShip.MAX_ANGLE)));
			} else {
				// AI
				g2d.setColor(ENEMY_DEBUG_DETECTION_RADIUS_COLOUR);
				g2d.drawOval((int)(relativeCamPos.x - detectionRadius), 
					     	 (int)(relativeCamPos.y - detectionRadius), 
					     	 (int)(2*detectionRadius), (int)(2*detectionRadius));
			}
		}
	}
}
