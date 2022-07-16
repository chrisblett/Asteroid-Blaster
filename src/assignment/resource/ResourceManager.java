package assignment.resource;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class ResourceManager {
	
	public static final String TEXTURE_PATH = "/textures/";
	public static final String SOUND_PATH = "res/sounds/";
	
	private static Map<String, Image> textures = new HashMap<String,Image>();
	private static Map<String, Clip> sounds = new HashMap<String, Clip>();
	
	public static void loadTexture(String fileName) throws IOException {
		String path = TEXTURE_PATH + fileName;
		
		Image img = ImageIO.read(ResourceManager.class.getResource(path));
		textures.put(path, img);
		System.out.println("successfully loaded texture: " + path);
	}
	
	public static void loadClip(String fileName) throws Exception {
		String path = SOUND_PATH + fileName;

		Clip clip = null;
		clip = AudioSystem.getClip();
		AudioInputStream sample = AudioSystem.getAudioInputStream(new File(path));
		clip.open(sample);
		sounds.put(path, clip);
		System.out.println("successfully loaded clip: " + path);
	}
	
	public static Image getTexture(String fileName) {
		return textures.get(TEXTURE_PATH + fileName);
	}
	
	public static Clip getAudioClip(String fileName) {
		return sounds.get(SOUND_PATH + fileName);
	}
}
