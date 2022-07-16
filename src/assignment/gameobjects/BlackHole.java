package assignment.gameobjects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;

import assignment.game.Game;
import assignment.math.Vector2D;
import assignment.physics.Collidable;
import assignment.resource.ResourceManager;
import assignment.resource.Sprite;

public class BlackHole extends GameObject {

	public static final double BLACK_HOLE_RADIUS = 50;
	public static final double PULL_RADIUS_FACTOR = 3;
	public static final Color BLACK_HOLE_DEBUG_COLOUR = Color.blue;
	
	private Puller puller;
	
	private Sprite sprite;
	
	public BlackHole(Vector2D pos) {
		super(pos, new Vector2D(0,0), BLACK_HOLE_RADIUS);
		
		Image img = ResourceManager.getTexture("bhole.png");
		sprite = new Sprite(img, position, new Vector2D(0, -1), 
				4 * radius, 4 * radius);
		
		puller = new Puller(new Vector2D(position), radius * PULL_RADIUS_FACTOR);
	}

	@Override
	public void draw(Graphics2D g2d, int xOffset, int yOffset) {
		Vector2D relativeCamPos = new Vector2D(position.x - xOffset, position.y - yOffset);
		
		sprite.draw(g2d, xOffset, yOffset);

		if(Game.DEBUG) {
			g2d.setColor(BLACK_HOLE_DEBUG_COLOUR);
			g2d.drawOval((int)(relativeCamPos.x - radius), (int)(relativeCamPos.y - radius), (int)(2*radius), (int)(2*radius));
			puller.draw(g2d, xOffset, yOffset);
		}
	}

	@Override
	public void onCollision(Collidable col) {
		col.onCollision(this);		
	}
	
	@Override
	public void onCollision(Ship s) {
		System.out.println("black hole has sucked ship");
		puller.stop();
	}
	
	public Puller getPuller() {
		return puller;
	}
}
