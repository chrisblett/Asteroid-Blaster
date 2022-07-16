package assignment.display;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JComponent;
import assignment.game.Game;
import assignment.game.GameCamera;
import assignment.game.World;
import assignment.levelgen.WorldGenerator;
import assignment.util.Constants;

public class View extends JComponent{
	
	private Game game;
	
	public View(Game game) {
		this.game = game;
	}

	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		
		// Improves the rendering quality
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		
		World world = game.getWorld();
		GameCamera cam = world.getGameCamera();
		int camX = cam.getXOffset();
		int camY = cam.getYOffset();
		
		// Paint background
		g2d.setColor(Color.black);
		g2d.fillRect(-camX, -camY, World.WORLD_SIZE, World.WORLD_SIZE);
		
		synchronized(Game.class) {
			game.getCurrentGameState().draw(g2d);
		}
		
		// Draw debug related
		if(Game.DEBUG) {
			if(world != null) {
				WorldGenerator.drawWorldRegions(g2d, camX, camY);
				if(world.spawnArea != null)
					world.spawnArea.draw(g2d, camX, camY);
			}
			
			// Draw debug UI
		    g2d.setFont(new Font("Courier New", 0, 10));
		    g2d.setColor(Color.white);
		
			g2d.drawString("Debug", 0, 10);
			g2d.drawString("Player:", 0, 40);
			g2d.drawString("pos:" + game.getWorld().getPlayer().position.toString(), 0, 55);
			g2d.drawString("vel:" + game.getWorld().getPlayer().velocity.toString(), 0, 70);
			g2d.drawString("World:", 0, 95);
			g2d.drawString("cam:" + cam.toString(),0, 110);
			g2d.drawString("current objects: " + world.getGameObjects().size(), 0, 140);
			g2d.drawString("current asteroids: " + world.getTotalAsteroids(), 0, 155);
		}
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(Constants.FRAME_WIDTH, Constants.FRAME_HEIGHT);
	}
}
