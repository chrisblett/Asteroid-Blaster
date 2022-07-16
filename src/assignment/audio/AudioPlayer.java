package assignment.audio;

import javax.sound.sampled.Clip;

public class AudioPlayer {
	
	public static void play(Clip clip) {
		clip.setFramePosition(0);
		clip.start();
	}
	
	// Loops a clip continuously
	public static void playContinuous(Clip clip) {
		clip.loop(-1);
	}
	
	// Immediately ends a clip without waiting for it to finish
	public static void stopContinuous(Clip clip) {
		clip.stop();
	}	
}
