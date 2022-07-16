package assignment.gamestate;

import java.awt.Graphics2D;

public abstract class GameState {
	public abstract void update(); 
	public abstract void draw(Graphics2D g2d);
	
	// Any game state implementations that need to do something 
	// on termination can override this
	public void terminate() {}
}
