package assignment.gameobjects;

import static assignment.util.Constants.DT;

import java.awt.Color;
import java.awt.Image;
import javax.sound.sampled.Clip;

import assignment.audio.AudioPlayer;
import assignment.game.Game;
import assignment.game.World;
import assignment.input.Action;
import assignment.input.Controller;
import assignment.math.Vector2D;
import assignment.resource.ResourceManager;
import assignment.resource.Sprite;

public abstract class Ship extends GameObject {
	
	public static final double SHIP_RADIUS = 15;

	public static final double ACCEL_RATE = 200;
	
	// Constant speed loss factor
	public static final double DRAG = 0.01;
	
	public static final double BULLET_RADIUS = 6;
	
	public static final Image DEFAULT_BULLET_IMAGE = ResourceManager.getTexture("shotoval.png");
	
	protected Controller ctrl;
	
	protected Vector2D direction;
	protected World world;
	
	protected int fireInterval;
	
	protected double steerRate;
	
	// Stores the amount of time steps that have to elapse until
	// the player can fire again.
	protected int nextFireTime;
	
	protected Bullet currentBullet;
	
	protected Image bulletImage;
	protected Sprite sprite;
	
	protected Clip fireSFX;
	protected Clip deathSFX;
	
	public Ship(Vector2D pos, Vector2D vel, double radius, World world) {
		super(pos, vel, radius);
		this.world = world;
		
		deathSFX = ResourceManager.getAudioClip("ship_death.wav");
	}
	
	@Override
	public void update() {
		super.update();
	}
	
	public void onHit() {
		super.onHit();
		if(world.inCameraBounds(position)) {
			AudioPlayer.play(deathSFX);
		}
	}
	
	public void processMovement(Action action) {
		// Movement
		direction.rotate(action.turn * steerRate * DT);
		velocity.addScaled(direction, action.thrust * ACCEL_RATE * DT);
		velocity.addScaled(velocity, -DRAG);
	}
	
	public void processFiring(Action action) {
		// Shooting
		if(nextFireTime > 0) {
			nextFireTime--;
		}
		if(action.shoot && nextFireTime <= 0) {
			makeBullet();
			
			if(world.inCameraBounds(this.position)) {
				AudioPlayer.play(fireSFX);
			}
			
			nextFireTime = fireInterval;
			action.shoot = false;
		}
	}
	
	protected void makeBullet() {
		// Spawn the bullet slightly in front to avoid collision on creation 
		Vector2D bulletPos = new Vector2D(position.x, position.y).addScaled(direction, SHIP_RADIUS*4);
		Vector2D bulletVel = new Vector2D(direction).mult(Bullet.SPEED);
		currentBullet = new Bullet(bulletPos, bulletVel, BULLET_RADIUS, this, bulletImage);
		world.addNewBulletToWorld(currentBullet);
		currentBullet = null;
	}
	
	protected void keepWithinBounds(Vector2D oldPos) {
		// Push the player away slightly to prevent them from getting 'stuck' to the boundary.
		if(position.x - radius <= 0 || position.x + radius > World.WORLD_SIZE) {
			velocity.x = -velocity.x;
			position.x = oldPos.x;
			position.y = oldPos.y;
		}
		if(position.y - radius <= 0 || position.y + radius > World.WORLD_SIZE) {
			velocity.y = -velocity.y;
			position.x = oldPos.x;
			position.y = oldPos.y;
		}
	}
	
	@Override
	public void onCollision(Asteroid a) {
		//System.out.println("ship hit an asteroid");
		onHit();
	}
	
	@Override
	public void onCollision(Bullet b) {
		//System.out.println("ship hit a bullet");
		// Reward the player if their bullet hit a ship
		if(b.getOwner() == world.getPlayer()) {
			Game.addScore(Game.ENEMY_KILL_POINTS);
		}
		onHit();
	}
	
	@Override
	public void onCollision(BlackHole hole) {
		//System.out.println("ship sucked into black hole");
		onHit();
	}
	
	@Override
	public void onCollision(Puller p) {
		//System.out.println("ship starting to get pulled into black hole");
	}
	
	public Vector2D getDirection() {
		return direction;
	}
}
