package assignment.gameobjects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import javax.sound.sampled.Clip;

import assignment.audio.AudioPlayer;
import assignment.game.Game;
import assignment.game.World;
import assignment.input.Action;
import assignment.input.Controller;
import assignment.math.Vector2D;
import assignment.physics.Collidable;
import assignment.resource.ResourceManager;
import assignment.resource.Sprite;

public class PlayerShip extends Ship {

	// Movement
	public static final double ACCEL_RATE = 200;
	public static final double PLAYER_STEER_RATE = 2 *Math.PI;
	
	// Firing
	public static final int PLAYER_FIRE_INTERVAL = 15;
	public static final Image PLAYER_BULLET_IMAGE = ResourceManager.getTexture("shotbig.png");
	
	// Power
	public static final double MAX_POWER = 100;
	
	// Spawn
	public static final int SPAWN_DELAY = 90;
	
	// How long (in timesteps) the player must wait for their shield to
	// start recharging if it reaches 0.
	public static final int POWER_RECHARGE_WAIT = 100;
	
	// Increase in power per tick when charging
	public static final double CHARGE_RATE = 1;
	
	private double power;
	private double timeUntilRecharge;
	private boolean canRechargePower;
	
	private int ticksSinceSpawn;
	private boolean hasSpawnProtection;
	
	private boolean thrusting;
	private boolean startedThrusting;
	
	private Shield shield;

	private Clip pickupSFX;
	private Clip thrustSFX;
	
	public PlayerShip(Controller ctrl, World world) {
		super(new Vector2D(World.PLAYER_SPAWN), new Vector2D(), SHIP_RADIUS, world);
		
		direction = new Vector2D(0,-1);
		
		bulletImage = PLAYER_BULLET_IMAGE;
		
		Image img = ResourceManager.getTexture("player.png");
		sprite = new Sprite(img, position, direction, 
				2*radius, 2 *radius);
		
		fireSFX = ResourceManager.getAudioClip("player_fire.wav");
		pickupSFX = ResourceManager.getAudioClip("pickup.wav");
		thrustSFX = ResourceManager.getAudioClip("thrust.wav");
		
		this.ctrl = ctrl;
		
		steerRate = PLAYER_STEER_RATE;
		fireInterval = PLAYER_FIRE_INTERVAL;
		shield = new Shield(this);
		power = MAX_POWER;
		
		hasSpawnProtection = true;
	}
	
	@Override
	public void update() {		
		ticksSinceSpawn++;
		if(ticksSinceSpawn >= Game.SPAWN_PROTECTION_TICKS) {
			hasSpawnProtection = false;
		} 
				
		// Keep track of the old position. This is the value we will
		// 'push' the player back to during boundary collisions.
		Vector2D oldPos = new Vector2D(position.x, position.y);

		Action action = ctrl.getAction();
		
		processMovement(action);
		processFiring(action);
		processPower(action);
		
		// Update the player before we check for collisions 
		// otherwise we won't be able to store an old position for this tick.
		super.update();
			
		keepWithinBounds(oldPos);
		
		// It is important to set the position of the shield
		// AFTER we have calculated the new player position, otherwise there will be
		// a misalignment.
		updateShield();
	}
		
	private void processPower(Action action) {
		if(hasPower() && action.shield) {
			shield.activate();	
		} else {
			shield.deactivate();
		}
		
		if(canRechargePower() && power < MAX_POWER && !shield.isActive()) {
			power += CHARGE_RATE;
		}		
		if(!canRechargePower()) {
			timeUntilRecharge--;
			if(timeUntilRecharge <= 0) {
				canRechargePower = true;
			}
		}
	}
	
	private void updateShield() {
		if(shield.isActive()) {
			radius = shield.radius;
			shield.update();
		} else {
			radius = SHIP_RADIUS;
		}
	}
	
	@Override
	public void onHit() {
		if(!hasSpawnProtection) {
			super.onHit();
			world.onPlayerDeath();
		}	
	}
	
	@Override
	public void draw(Graphics2D g2d, int xOffset, int yOffset) {
		Vector2D relativeCamPos = new Vector2D(position.x - xOffset, position.y - yOffset);
		
		// Create a flashing effect to inform the player
		if(hasSpawnProtection) {
			if(ticksSinceSpawn % 5 == 0) {
				sprite.draw(g2d, xOffset, yOffset);
			}
		} else {
			sprite.draw(g2d, xOffset, yOffset);
		} 
			
		if(shield.isActive()) 
			shield.draw(g2d, xOffset, yOffset);
		
		// Debug
		if(Game.DEBUG) {
			g2d.setColor(Color.magenta);
			g2d.drawOval((int)(relativeCamPos.x - radius), (int)(relativeCamPos.y - radius), (int)(2*radius), (int)(2*radius));
			g2d.drawLine((int)relativeCamPos.x, (int)relativeCamPos.y, 
					     (int)(relativeCamPos.x + direction.x * (3*SHIP_RADIUS)), 
					     (int)(relativeCamPos.y + direction.y * (3*SHIP_RADIUS)));
		}
	}
	
	public void usePower(double amount) {
		power -= amount;
		if(power <= 0) {
			power = 0;
			shield.deactivate();
			timeUntilRecharge = POWER_RECHARGE_WAIT;
			canRechargePower = false;
		}
	}
	
	@Override
	public void onCollision(Collidable col) {
		col.onCollision(this);		
	}
	
	@Override
	public void onCollision(Ship s) {
		if(!shield.isActive()) {
			System.out.println("player ship collided with another ship");
			onHit();
		} else {
			System.out.println("player ship collided with another ship, but shield was active");
		}
	}
	
	@Override
	public void onCollision(Asteroid ast) {
		if(!shield.isActive()) {
			System.out.println("ship collided with asteroid");
			onHit();
		} else {
			System.out.println("ship collided with asteroid, but shield was active");
		}
	}
	
	@Override
	public void onCollision(Bullet b) {
		if(!shield.isActive()) {
			System.out.println("ship collided with bullet");
			onHit();
		} else {
			System.out.println("ship collided with bullet, but shield was active");
		}
	}
	
	@Override
	public void onCollision(Crystal rock) {
		System.out.println("ship collected rock");
		AudioPlayer.play(pickupSFX);
		world.onRockCollected();
	}
		
	public boolean hasPower() {
		return power > 0;
	}
	
	public double getPower() {
		return power;
	}
	
	public boolean canRechargePower() {
		return canRechargePower;
	}
	public boolean isShieldActive() {
		return shield.isActive();
	}
	
	@Override
	public boolean doesWrap() {
		return false;
	}
}
