package assignment.game;

import assignment.audio.AudioPlayer;
import assignment.gamestate.GameOver;
import assignment.gamestate.GameState;
import assignment.gamestate.Playing;
import assignment.gamestate.StartScreen;
import assignment.input.Keyboard;
import assignment.resource.ResourceManager;

public class Game {
	
	// Debug 
	public static boolean DEBUG = false;
	
	public static final int STARTING_LIVES = 3;
	public static final int AWARD_LIFE_INTERVAL = (int)1e4;
	public static final int TOTAL_ROCKS_GOAL = 10;
	public static final int SPAWN_PROTECTION_TICKS = 30;
	public static final int ENEMY_KILL_POINTS = 1000;
	
	private static int totalScore;
	private static int scoreSinceLife;
	private static int lives;
	private static int rocksCollected;
	
	private boolean isGameOver;
	private boolean started;

	private World world;
	private Keyboard ctrl;
	private GameState currentGameState;
	
	public Game() {
		lives = STARTING_LIVES;
		totalScore = 0;
		
		initResources();
		
		ctrl = new Keyboard(this);
		world = new World(this,ctrl);
		currentGameState = new StartScreen();
	}
	
	
	private void initResources() {
		loadTextures();
		loadSounds();
	}
	
	private void loadTextures() {
		System.out.println("loading textures");
		try {
			// Player
			ResourceManager.loadTexture("shield.png");
			ResourceManager.loadTexture("player.png");
			ResourceManager.loadTexture("shotbig.png");
			
			// Enemy
			ResourceManager.loadTexture("enemy.png");
			ResourceManager.loadTexture("shotoval.png");
			
			// World
			ResourceManager.loadTexture("crystal.png");
			ResourceManager.loadTexture("asteroid.png");
			ResourceManager.loadTexture("bhole.png");
			
		} catch(Exception e) {
			System.out.println("Failed to load textures!");
			e.printStackTrace();
		}
	}
	
	private void loadSounds() {
		System.out.println("loading sounds");
		try {
			// Sound effects

			// Ship
			ResourceManager.loadClip("ship_death.wav");
			ResourceManager.loadClip("enemy_fire.wav");
			
			// Player
			ResourceManager.loadClip("player_fire.wav");
			ResourceManager.loadClip("pickup.wav");

			// Asteroid
			ResourceManager.loadClip("ast_small.wav");
			ResourceManager.loadClip("ast_med.wav");
			ResourceManager.loadClip("ast_large.wav");
			
			//Game
			ResourceManager.loadClip("life.wav");
			
			// Music
			ResourceManager.loadClip("menu.wav");
			
		} catch(Exception e) {
			System.out.println("Failed to load sounds!");
			e.printStackTrace();
		}
	}
	
	public void update() {
		if(currentGameState != null) {
			currentGameState.update();
		}
	}
	
	public static void addScore(int amount) {
		totalScore += amount;
		scoreSinceLife += amount;
		
		// Player earned enough, give them a life and play an audial cue
		if(scoreSinceLife >= AWARD_LIFE_INTERVAL) {
			lives++;
			AudioPlayer.play(ResourceManager.getAudioClip("life.wav"));
			scoreSinceLife -=  AWARD_LIFE_INTERVAL;
		}
	}
	
	public void start() {
		currentGameState.terminate();
		currentGameState = new Playing(world);
	}
	
	public void restart() {
		isGameOver = false;
		started = false;
		lives = 3;
		totalScore = 0;
		scoreSinceLife = 0;
		rocksCollected = 0;
		
		world = new World(this, ctrl);
		currentGameState = new StartScreen();
	}
	
	public void onPlayerDeath() {
		if(!Game.DEBUG) {
			lives--;
			if(lives == 0) {
				startGameOver();
			}
		}
	}
	
	public void onRockCollected() {
		rocksCollected++;
		System.out.println("You have: " + rocksCollected + "/" + TOTAL_ROCKS_GOAL + " rocks");
		if(rocksCollected == TOTAL_ROCKS_GOAL) {
			startGameOver();
		}
	}
			
	public void startGameOver() {
		isGameOver = true;
		currentGameState = new GameOver(this);
	}

	public World getWorld() {
		return world;
	}
	
	public Keyboard getController() {
		return ctrl;
	}
	
	public static int getScore() {
		return totalScore;
	}
	
	public static int getScoreSinceLife() {
		return scoreSinceLife;
	}
	
	public static int getLives() {
		return lives;
	}
	
	public static int getRocksCollected() {
		return rocksCollected;
	}
	
	public boolean isGameOver() {
		return isGameOver;
	}
	
	public GameState getCurrentGameState() {
		return currentGameState;
	}
	
	public boolean hasGameStarted() {
		return started;
	}
	
	public boolean hasPlayerWon() {
		return getRocksCollected() == TOTAL_ROCKS_GOAL;
	}
}
