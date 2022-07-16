package assignment.levelgen;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

import assignment.game.World;
import assignment.gameobjects.BlackHole;
import assignment.gameobjects.EnemyShip;
import assignment.gameobjects.Ship;
import assignment.math.Vector2D;

public class WorldGenerator {
	
	private World world;
	
	public WorldGenerator(World world) {
		this.world = world;
		
		generateWorld();
	}
	
	private void generateWorld() {
		Random r = new Random();
		int regions = 4;
		
		int x = 0;
		int y = 0;
		
		// Loops through each quadrant of the world and each quadrant within that quadrant
		// generates a random number and depending on that number
		// it will decide what to spawn at that position.
		
		for(int i = 0; i < regions; i++) {
			int boundary = (x + World.WORLD_SIZE / 2);
			
			int innerX = x;
			int innerY = y;
			
			for(int j = 0; j < regions; j++) {
		
				// Generate random values
				int min = 0;
				int max = 2;
				int result = r.nextInt(max - min + 1) + min;
				
				int spawnOffset = (World.WORLD_SIZE / regions) / 2;
				switch(result) {
				case 0:
					EnemyShip enemy = new EnemyShip(new Vector2D(innerX + spawnOffset, innerY + spawnOffset), 
													new Vector2D(),
													Vector2D.generateRandomDirection(), 
													Ship.SHIP_RADIUS, world);
					world.addObjectToWorld(enemy);
					break;
					
				case 1:
					BlackHole bh = new BlackHole(new Vector2D(innerX + spawnOffset, innerY + spawnOffset));
					world.getBlackHoleController().addBlackHole(bh);
					break;
					
				default:
				}
								
				innerX += World.WORLD_SIZE / regions;
				if(innerX >= boundary) {
					innerX = x;
					innerY+= World.WORLD_SIZE / regions;
				}
			}
			
			x += World.WORLD_SIZE / 2;
			if(x >= World.WORLD_SIZE) {
				x = 0;
				y+= World.WORLD_SIZE / 2;
			}
		}
	}
	
	// DEBUG
	public static void drawWorldRegions(Graphics2D g2d, int xOffset, int yOffset) {
		g2d.setColor(Color.white);
		
		// NW
		g2d.drawRect(0 - xOffset, 
					 0 - yOffset , 
					 World.WORLD_SIZE / 2, 
					 World.WORLD_SIZE / 2);
		// NE
		g2d.drawRect(World.WORLD_SIZE / 2 - xOffset, 
				 	 0 - yOffset , 
				     World.WORLD_SIZE / 2, 
				     World.WORLD_SIZE / 2);

		// SE
		g2d.drawRect(World.WORLD_SIZE / 2 - xOffset, 
					 World.WORLD_SIZE / 2 - yOffset, 
					 World.WORLD_SIZE / 2,
					 World.WORLD_SIZE / 2);
		// SW
		g2d.drawRect(0 - xOffset, 
				 	 World.WORLD_SIZE / 2 - yOffset , 
				     World.WORLD_SIZE / 2, 
				     World.WORLD_SIZE / 2);
	}
}
