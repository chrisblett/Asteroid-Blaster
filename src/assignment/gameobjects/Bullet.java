package assignment.gameobjects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;

import assignment.game.Game;
import assignment.math.Vector2D;
import assignment.physics.Collidable;
import assignment.resource.Sprite;

public class Bullet extends GameObject {

	public static final int SPEED = 500;
	public static final Color BULLET_DEBUG_COLOUR = Color.red;
	public static final int LIFETIME = 60;
	
	// Remaining time steps it is alive for
	private int timeToLive;

	private Ship owner;
		
	private Sprite sprite;
	
	public Bullet(Vector2D pos, Vector2D vel, double radius, Ship owner, Image image) {
		super(pos, vel, radius);
		this.owner = owner;
		
		int spriteW = 0;
		int spriteH = 0;
		spriteW = (int)(2*radius);
		
		if(image == PlayerShip.PLAYER_BULLET_IMAGE) {
			spriteH = (int)(4 * radius);
		} else {
			spriteH = spriteW;
		}
		
		sprite = new Sprite(image, position, vel, spriteW, spriteH);
		
		timeToLive = LIFETIME;
	}
	
	@Override
	public void update() {
		timeToLive--;
		if(timeToLive <= 0) {
			isDead = true;
		}
		super.update();
	}

	@Override
	public void draw(Graphics2D g2d, int xOffset, int yOffset) {
		Vector2D relativeCamPos = new Vector2D(position.x - xOffset, 
			       						       position.y - yOffset);
		
		sprite.draw(g2d, xOffset, yOffset);
		
		if(Game.DEBUG) {
			g2d.setColor(BULLET_DEBUG_COLOUR);
			g2d.drawOval((int)(relativeCamPos.x - radius), (int)(relativeCamPos.y - radius), (int)(2*radius), (int)(2*radius));
		}
	}

	@Override
	public void onCollision(Asteroid ast) {
		//System.out.println("bullet collided with asteroid");
		onHit();	
	}

	@Override
	public void onCollision(Ship ship) {
		//System.out.println("bullet collided with ship");
		onHit();
	}

	@Override
	public void onCollision(Collidable col) {
		col.onCollision(this);
	}
	
	public Ship getOwner() {
		return owner;
	}
}
