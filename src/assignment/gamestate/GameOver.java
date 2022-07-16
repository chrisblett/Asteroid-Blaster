package assignment.gamestate;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import assignment.game.Game;
import assignment.util.UI;

public class GameOver extends GameState {
	
	// Ticks to wait before starting the restart process
	public static final int WAIT_BEFORE_RESTART = 200;
	
	private Game game;
	private int waitTime;
	
	public GameOver(Game game) {
		this.game = game;
	}

	public void update() {
		waitTime++;
		if(waitTime >= WAIT_BEFORE_RESTART) {
			game.restart();
			System.out.println("restarting game");
		}
	}

	@Override
	public void draw(Graphics2D g2d) {
		g2d.setColor(Color.white);
		g2d.setFont(new Font("Courier New", 0, 40));
		
		String text = "Game over!";
		if(game.hasPlayerWon()) {
			text = "You win!";
		}
		UI.drawCenteredText(text, g2d.getFont(), g2d, 0);
	}
}
