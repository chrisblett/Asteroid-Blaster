package assignment.gameobjects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;

import assignment.game.World;
import assignment.math.Vector2D;
import assignment.physics.Collidable;
import assignment.physics.Physics;

public class SpawnArea extends GameObject {
	
	public static final int SPAWN_RADIUS = 100;
	
	public SpawnArea() {
		super(World.PLAYER_SPAWN, new Vector2D(0,0), SPAWN_RADIUS);
	}
	
	// TODO: Maybe improve the way we handle collisions, similar to how other collisions are handled
	public boolean isClear(List<? extends GameObject> objects) {
		boolean safe = true;
		for(GameObject obj : objects) {
			if(Physics.overlaps(this, obj)) {
				System.out.println("Unsafe spawn area");
				System.out.println("Spawn area: " + obj.toString() + " is inside");
				safe = false;
			}
		}
		return safe;
	}

	@Override
	public void draw(Graphics2D g2d, int xOffset, int yOffset) {
		Vector2D relativeCamPos = new Vector2D(position.x - xOffset, position.y - yOffset);
		g2d.setColor(Color.yellow);
		g2d.drawOval((int)(relativeCamPos.x - radius), (int)(relativeCamPos.y - radius), (int)(2*radius), (int)(2*radius));
	}

	@Override
	public void onCollision(Collidable col) {
		col.onCollision(this);
	}
}
