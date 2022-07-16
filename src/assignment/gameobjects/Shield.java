package assignment.gameobjects;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;

import assignment.game.Game;
import assignment.math.Vector2D;
import assignment.physics.Collidable;
import assignment.resource.ResourceManager;
import assignment.resource.Sprite;

public class Shield extends GameObject{
	
	// Owner power loss per tick when the shield is active
	public static final double DRAIN_RATE = 2;
	
	// The shield's size shield compared to the owner's radius
	public static final double SHIELD_SIZE_FACTOR = 1.5;
	
	// The opacity applied to the shield image when being drawn
	public static final float SHIELD_ALPHA = 0.5f;
	
	private boolean isActive;
	
	private PlayerShip owner;
	
	private Sprite sprite;
	
	public Shield(PlayerShip owner) {
		super(new Vector2D(owner.position), new Vector2D(0,0), owner.radius * 		SHIELD_SIZE_FACTOR);
		this.owner = owner;
		
		Image img = ResourceManager.getTexture("shield.png");
		sprite = new Sprite(img, position, owner.direction, 2*radius, 2*radius);
	}
	
	@Override
	public void update() {
		System.out.println(toString());
		if(isActive) {
			owner.usePower(DRAIN_RATE);
		}
		position.set(owner.position);
	}
	
	public void activate() {
		isActive = true;
	}
	
	public void deactivate() {
		isActive = false;
	}
	
	@Override
	public void draw(Graphics2D g2d, int xOffset, int yOffset) {
		Vector2D relativeCamPos = new Vector2D(position.x - xOffset, position.y - yOffset);
		
		// Create a temp graphics object so we don't affect the original
		Graphics2D g2dTemp = (Graphics2D) g2d.create();
		float alpha = SHIELD_ALPHA;
		g2dTemp.setComposite(AlphaComposite.SrcOver.derive(alpha));
		sprite.draw(g2dTemp, xOffset, yOffset);
		// Destroy the temp after use.
		g2dTemp.dispose();
			
		if(Game.DEBUG) {
			g2d.setColor(Color.orange);
			g2d.drawOval((int)(relativeCamPos.x - radius), 
					     (int)(relativeCamPos.y - radius), 
					     (int)(2*radius), (int)(2*radius));
		}
	}

	@Override
	public void onCollision(Collidable col) {
		col.onCollision(this);
	}
	
	public boolean isActive() {
		return isActive;
	}
	
	@Override
	public String toString() {
		return ("is_active: " + isActive + "\n" +
			    "power: " + owner.getPower() + "\n");
	}
}
