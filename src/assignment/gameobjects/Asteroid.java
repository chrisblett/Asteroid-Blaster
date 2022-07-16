package assignment.gameobjects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import javax.sound.sampled.Clip;

import assignment.controllers.AsteroidController;
import assignment.game.Game;
import assignment.math.Vector2D;
import assignment.physics.Collidable;
import assignment.resource.ResourceManager;
import assignment.resource.Sprite;

public class Asteroid extends GameObject {

	public static final int LARGE_RADIUS = 30;
	public static final int MEDIUM_RADIUS = 20;
	public static final int SMALL_RADIUS = 10;
	
	public static final Color ASTEROID_DEBUG_COLOUR = Color.magenta;
	
	public static final int MAX_SPEED = 100;
	
	public static final Clip SMALL_SFX = ResourceManager.getAudioClip("ast_small.wav");
	public static final Clip MED_SFX = ResourceManager.getAudioClip("ast_med.wav");
	public static final Clip LARGE_SFX = ResourceManager.getAudioClip("ast_large.wav");
	
	// The speed increase that a new fragment has compared with it's 'parent' asteroid
	public static final double FRAGMENT_SPEED_FACTOR = 1.25;
	
	// How many pieces an large enough asteroid will create when destroyed
	private final int fragments = 2;
	
	// Stores how many points an asteroid awards when destroyed
	private int points;
	
	private Sprite sprite;

	private Clip explodeSFX;
	
	public Asteroid(Vector2D pos, Vector2D vel, double radius) {
		super(pos, vel, radius);
		
		Image img = ResourceManager.getTexture("asteroid.png");
		sprite = new Sprite(img, position, vel, 2*radius, 2*radius);
	
		// Set up the points and explode sfx depending on the asteroid size
		if(radius >= LARGE_RADIUS) {
			radius = LARGE_RADIUS;
			explodeSFX = Asteroid.LARGE_SFX;
			
			points = AsteroidController.LARGE_ASTEROID_POINTS;
			
		} else if(radius >= MEDIUM_RADIUS) {
			radius = MEDIUM_RADIUS;
			explodeSFX = Asteroid.MED_SFX;
			
			points = AsteroidController.MEDIUM_ASTEROID_POINTS;
		} else {
			radius = SMALL_RADIUS;
			explodeSFX = Asteroid.SMALL_SFX;
			
			points = AsteroidController.SMALL_ASTEROID_POINTS;
		}
	}
	
	@Override
	public void onHit() {
		super.onHit();
	
		if(radius > SMALL_RADIUS) {
			double fragRadius = 0;
			
			switch( (int)radius ) {
			case LARGE_RADIUS:
				fragRadius = MEDIUM_RADIUS;
	
				break;
			case MEDIUM_RADIUS:
				fragRadius = SMALL_RADIUS;
				break;
			}
			
			// Generate the fragments
			for(int i = 0; i < fragments; i++) {
				System.out.println("creating asteroid");
				AsteroidController.makeNewAsteroid(new Vector2D(position), 
												   new Vector2D(Vector2D.generateRandomDirection()).mult(MAX_SPEED * FRAGMENT_SPEED_FACTOR), 
									               fragRadius); 
			}
		} 
	}
	
	@Override
	public void draw(Graphics2D g2d, int xOffset, int yOffset) {
		Vector2D relativeCamPos = new Vector2D(position.x - xOffset, position.y - yOffset);
		
		sprite.draw(g2d, xOffset, yOffset);
		
		if(Game.DEBUG) {
			g2d.setColor(ASTEROID_DEBUG_COLOUR);
			g2d.drawOval((int)(relativeCamPos.x - radius), (int)(relativeCamPos.y - radius), (int)(2*radius), (int)(2*radius));
		}
	}
	
	@Override
	public void onCollision(Collidable col) {
		col.onCollision(this);
	}

	@Override
	public void onCollision(Ship ship) {
		System.out.println("asteroid collided with ship");		
	}

	@Override
	public void onCollision(Bullet bullet) {
		onHit();
		System.out.println("asteroid collided with bullet");
		AsteroidController.onAsteroidDestroyed(this,bullet);
	}
	
	public int getPoints() {
		return points;
	}
	
	public Clip getDeathSFX() {
		return explodeSFX;
	}
}
