package assignment.gameobjects;

import java.awt.Graphics2D;

import assignment.game.World;
import assignment.math.Vector2D;
import assignment.physics.Collidable;
import static assignment.util.Constants.*;

public abstract class GameObject implements Collidable {
		
	public Vector2D position;
	public Vector2D velocity;
		
	protected double radius;
	protected boolean isDead;
	
	public GameObject(Vector2D pos, Vector2D vel, double radius) {
		this.position = pos;
		this.velocity = vel;
		this.radius = radius;
		isDead = false;
	}
	
	public void onHit() {
		isDead = true;
	}
	
	public void update() {
		position.addScaled(velocity, DT);
		
		if(doesWrap()) {
			position.wrap(World.WORLD_SIZE, World.WORLD_SIZE);
		}
	}
	
	public abstract void onCollision(Collidable col);
	
	// Now each subclass can chose what methods to override depending on what it collides with.
	public void onCollision(Ship s) {}
	public void onCollision(PlayerShip player) {}
	public void onCollision(Bullet bullet) {}
	public void onCollision(Asteroid ast) {}
	public void onCollision(Crystal rock) {}
	public void onCollision(BlackHole hole) {}
	public void onCollision(Puller p) {}

	public abstract void draw(Graphics2D g2d, int xOffset, int yOffset);
	
	public boolean doesWrap() {
		return true;
	}
	
    // distance to other object in a wrapped world
    public double dist(GameObject other) {
        double dx = this.position.x - other.position.x;
        if (Math.abs(dx) > World.WORLD_SIZE / 2)
            dx = Math.abs(dx) - World.WORLD_SIZE;
        double dy = this.position.y - other.position.y;
        if (Math.abs(dy) > World.WORLD_SIZE / 2)
            dy = Math.abs(dy) - World.WORLD_SIZE;
        return Math.hypot(dx, dy);
    }
	
	public boolean isDead() {
		return isDead;
	}
	
	public double getRadius() {
		return radius;
	}
}
