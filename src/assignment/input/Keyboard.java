package assignment.input;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import assignment.game.Game;
import assignment.game.World;

public class Keyboard extends KeyAdapter implements Controller{
	
	private Action action;
	private Game game;
	
	public Keyboard(Game game) {
		this.game = game;
		action = new Action();
	}
	
	@Override
	public Action getAction() {
		return action;
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		
		switch(key) {
		case KeyEvent.VK_UP:
			action.thrust = 1;
			break;
			
		case KeyEvent.VK_LEFT:
			action.turn = -1;
			break;
			
		case KeyEvent.VK_RIGHT:
			action.turn = 1;
			break;
			
		case KeyEvent.VK_SPACE:
			action.shoot = true;
			break;
			
		case KeyEvent.VK_SHIFT:
			action.shield = true;
			break;
			
		// Controls the start screen
		case KeyEvent.VK_ENTER:
			if(!game.hasGameStarted() && !game.isGameOver()) {
				game.start();
			}
		}
		
		// Toggle debug UI
		if(key == KeyEvent.VK_1) {
			System.out.println("Debug toggle");
			Game.DEBUG = !Game.DEBUG;
		}
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		
		switch(key) {
		case KeyEvent.VK_UP:
			action.thrust = 0;
			break;
			
		case KeyEvent.VK_LEFT:
		case KeyEvent.VK_RIGHT:
			action.turn = 0;
			break;
			
		case KeyEvent.VK_SPACE:
			action.shoot = false;
			break;
		case KeyEvent.VK_SHIFT:
			action.shield = false;
		}
	}
}
