package assignment.gamestate;

import java.awt.Color;
import java.awt.Graphics2D;

import assignment.game.Game;
import assignment.game.GameCamera;
import assignment.game.World;
import assignment.gameobjects.GameObject;
import assignment.util.UI;

public class Playing extends GameState{

	// Reference to the world instance
	private World world;
	
	public Playing(World world) {
		this.world = world;
	}

	@Override
	public void draw(Graphics2D g2d) {
		
		GameCamera cam = world.getGameCamera();
		
		int camX = cam.getXOffset();
		int camY = cam.getYOffset();
		
		world.draw(g2d, camX, camY);
		
		synchronized(Game.class) {
			for(GameObject obj : world.getGameObjects()) {
				obj.draw(g2d, camX, camY);
			}
		}

		// Draw UI
		g2d.setColor(Color.white);
		UI.drawCenteredText("Score: " + Game.getScore(), g2d.getFont(), g2d,-250);
		UI.drawCenteredText("Crystals: " + Game.getRocksCollected() + "/" + Game.TOTAL_ROCKS_GOAL, 
						    g2d.getFont(), g2d, -240);
		UI.drawCenteredText("Lives: " + Game.getLives(), g2d.getFont(), g2d,-230);
		UI.drawCenteredText("Power: " + world.getPlayer().getPower(), g2d.getFont(), g2d,-220);
	}

	@Override
	public void update() {
		world.update();
	}
	
	@Override
	public String toString() {
		return "playing";
	}
}
