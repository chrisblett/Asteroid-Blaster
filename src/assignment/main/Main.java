package assignment.main;

import assignment.display.View;
import assignment.game.Game;
import assignment.util.Constants;
import assignment.util.JEasyFrame;

public class Main {
	public static void main(String[] args) throws Exception {
		Game game = new Game();
		View view = new View(game);
		new JEasyFrame(view, "Asteroid Blaster").addKeyListener(game.getController());
	
		// Game loop
		while(true) {
			game.update();
			view.repaint();
			Thread.sleep(Constants.DELAY);
		}
	}
}
