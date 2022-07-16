package assignment.gamestate;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import javax.sound.sampled.Clip;

import assignment.audio.AudioPlayer;
import assignment.resource.ResourceManager;
import assignment.util.UI;

public class StartScreen extends GameState {
	
	public static final String GAME_TITLE = "Asteroid Blaster";
	private Clip menuSong;
	
	public StartScreen() {
		menuSong = ResourceManager.getAudioClip("menu.wav");
		menuSong.setFramePosition(0);
		AudioPlayer.playContinuous(menuSong);
	}
	
	@Override
	public void update() {}
	
	@Override
	public void draw(Graphics2D g2d) {
		g2d.setColor(Color.white);
		
		// Draw start screen text
		Font titleFont = new Font("Courier New", 0, 40);
		Font subTitleFont = new Font("Courier New", 0, 30);
		
		UI.drawCenteredText(GAME_TITLE, titleFont, g2d, -200);
		UI.drawCenteredText("Press ENTER to play", subTitleFont, g2d, 0);
	}
	
	public void terminate() {
		// Stop playing the menu audio
		AudioPlayer.stopContinuous(menuSong);
	}
}
