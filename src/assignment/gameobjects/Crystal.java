package assignment.gameobjects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;

import assignment.game.Game;
import assignment.math.Vector2D;
import assignment.physics.Collidable;
import assignment.resource.ResourceManager;
import assignment.resource.Sprite;

public class Crystal extends GameObject {
	
	public static final double CRYSTAL_RADIUS = 5;
	public static final Color CRYSTAL_DEBUG_COLOR = Color.yellow;
	
	// Between 0.0 and 1.0
	public static final double DROP_CHANCE = 0.2;
	
	private Sprite sprite;
	
	public Crystal(Vector2D pos) {
		super(pos, new Vector2D(0,0), CRYSTAL_RADIUS);
		
		Image img = ResourceManager.getTexture("crystal.png");
		sprite = new Sprite(img, position, new Vector2D(0, -1), 2*radius, 2*radius);
	}

	@Override
	public void draw(Graphics2D g2d, int xOffset, int yOffset) {
		Vector2D relativeCamPos = new Vector2D(position.x - xOffset, position.y - yOffset);
		
		sprite.draw(g2d, xOffset, yOffset);
		
		if(Game.DEBUG) {
			g2d.setColor(CRYSTAL_DEBUG_COLOR);
			g2d.drawOval((int)(relativeCamPos.x - radius), (int)(relativeCamPos.y - radius), (int)(2*radius), (int)(2*radius));
		}
	}

	@Override
	public void onCollision(Collidable col) {
		col.onCollision(this);
	}
	
	@Override
	public void onCollision(Ship ship) {
		System.out.println("crystal collected by ship, removing from world");
		isDead = true;
	}
}
