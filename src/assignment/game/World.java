package assignment.game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

import assignment.controllers.AsteroidController;
import assignment.controllers.BlackHoleController;
import assignment.gameobjects.Asteroid;
import assignment.gameobjects.Bullet;
import assignment.gameobjects.GameObject;
import assignment.gameobjects.PlayerShip;
import assignment.gameobjects.SpawnArea;
import assignment.gameobjects.Crystal;
import assignment.input.Controller;
import assignment.levelgen.WorldGenerator;
import assignment.math.Vector2D;
import assignment.physics.Physics;
import assignment.util.Constants;

public class World {
	
	public static final int WORLD_SIZE = 4000;
	public static final int TOTAL_STARS = 5000;
	public static final int INITIAL_ASTEROIDS = (int)(World.WORLD_SIZE / 100);
	public static final Vector2D PLAYER_SPAWN = new Vector2D(World.WORLD_SIZE / 2, 
															 World.WORLD_SIZE / 2);
	
	private List<GameObject> objects;
	// Keeps track of objects waiting to be added to the world
	private Queue<GameObject> waiting;
	
	private AsteroidController asteroidCtrl;
	private BlackHoleController bhCtrl;

	private Vector2D[] stars;
	
	private GameCamera cam;
	private Game game;

	public SpawnArea spawnArea = null;
	private PlayerShip ship;	
	private Controller ctrl;

	private int timeUntilSpawn;
	private boolean shipSpawned;
		
	public World(Game game, Controller ctrl) {
		this.game = game;
		this.ctrl = ctrl;
		
		stars = new Vector2D[TOTAL_STARS];
		
		waiting = new ConcurrentLinkedQueue<GameObject>();
		objects = new ArrayList<GameObject>();
		
		ship = new PlayerShip(ctrl, this);
		cam = new GameCamera(0,0);

		asteroidCtrl = new AsteroidController(this);
		bhCtrl = new BlackHoleController(this);
		
		new WorldGenerator(this);
		
		generateWorld();
	}
	
	private void generateWorld() {
		createStars();
		createInitialAsteroids();
	}
	
	private void createStars() {
		Random r = new Random();
		for(int i = 0; i < stars.length; i++) {
			stars[i] = new Vector2D(r.nextInt() % World.WORLD_SIZE, r.nextInt() % World.WORLD_SIZE);
		}
	}
	
	private void createInitialAsteroids() {
		for(int i = 0; i < INITIAL_ASTEROIDS; i++) {
			asteroidCtrl.makeRandomAsteroid(Asteroid.LARGE_RADIUS);
		}
	}
		
	public void update() {
		// Handle collisions
		for(int i = 0; i < objects.size(); i++) {
			for(int j = i + 1; j < objects.size(); j++) {
				if(Physics.overlaps(objects.get(i), objects.get(j))) {
					objects.get(i).onCollision(objects.get(j));
					objects.get(j).onCollision(objects.get(i));
				}
			}
		}
		
		// Keeps track of all game objects that are still alive in the world
		List<GameObject> alive = new ArrayList<GameObject>();
		for(GameObject obj : objects) {
			obj.update();
			if(!obj.isDead()) {
				alive.add(obj);
			}
		}
		
		// Handle ship spawning
		if(!shipSpawned) {
			if(timeUntilSpawn > 0) {
				timeUntilSpawn--;
				System.out.println("Time until player spawn starts: " + timeUntilSpawn);
			} else {
				if(canShipSpawn()) {
					spawnArea = null;
					shipSpawned = true;
					ship = new PlayerShip(ctrl, this);
					addToObjectQueue(ship);
				}
			}
		}
		
		// Position the camera so that the player is in the centre
		cam.setXOffset((int)(ship.position.x - (Constants.FRAME_WIDTH / 2)));
		cam.setYOffset((int)(ship.position.y - (Constants.FRAME_HEIGHT / 2)));
		
		// Keep the camera within the world boundaries
		cam.keepWithinBounds();
			
		synchronized (Game.class) {
			objects.clear();
			objects.addAll(alive);
			objects.addAll(waiting);
			waiting.clear();
		}		
		
		asteroidCtrl.clearDead();
	}
	
	public void addObjectToWorld(GameObject obj) {
		addToObjectQueue(obj);
	}
	
	public void addNewAsteroidToWorld(Asteroid a) {
		asteroidCtrl.addAsteroidToWorld(a);
	}
	
	public void addNewBulletToWorld(Bullet b) {
		addObjectToWorld(b);
	}
	
	public void addNewRock(Crystal rock) {
		addObjectToWorld(rock);
	}
	
	private void addToObjectQueue(GameObject obj) {
		waiting.add(obj);
		//System.out.println("Added: " + obj + " to queue");
	}
	
	public void onPlayerDeath() {	
		shipSpawned = false;
		timeUntilSpawn = PlayerShip.SPAWN_DELAY;
		game.onPlayerDeath();
	}
	
	public void onRockCollected() {
		game.onRockCollected();
	}
	
	public boolean canShipSpawn() {
		if(spawnArea == null) {
			spawnArea = new SpawnArea();
		}
		// Prevent the player from spawning on asteroids and dying immediately.
		return spawnArea.isClear(asteroidCtrl.getAsteroids());
	}
		
	public void onAsteroidDestroyed(Asteroid a, Bullet b) {
		if(b.getOwner() == ship) {
			Game.addScore(a.getPoints());
		}
	}
	
	public void draw(Graphics2D g2d, int xOffset, int yOffset) {
		drawStars(g2d, xOffset, yOffset);
	}
	
	private void drawStars(Graphics2D g2d, int xOffset, int yOffset) {
		g2d.setColor(Color.white);
		for(Vector2D s : stars) {
			g2d.fillOval((int)(s.x - xOffset), (int)(s.y - yOffset), 1, 1);
		}
	}
	
	// DEBUG - World draw
	public void drawWorldRegions(Graphics2D g2d, int xOffset, int yOffset) {
		g2d.setColor(Color.white);
		
		// NW
		g2d.drawRect(0 - xOffset, 
					 0 - yOffset , 
					 World.WORLD_SIZE / 2, 
					 World.WORLD_SIZE / 2);
		// NE
		g2d.drawRect(World.WORLD_SIZE / 2 - xOffset, 
				 	 0 - yOffset , 
				     World.WORLD_SIZE / 2, 
				     World.WORLD_SIZE / 2);

		// SE
		g2d.drawRect(World.WORLD_SIZE / 2 - xOffset, 
					 World.WORLD_SIZE / 2 - yOffset, 
					 World.WORLD_SIZE / 2,
					 World.WORLD_SIZE / 2);
		// SW
		g2d.drawRect(0 - xOffset, 
				 	 World.WORLD_SIZE / 2 - yOffset , 
				     World.WORLD_SIZE / 2, 
				     World.WORLD_SIZE / 2);
	}
	
	// Checks whether an position is in the camera view
	// Useful for playing audio, as we don't want to play audio
	// from an asteroid being destroyed on the other side of the world.
	public boolean inCameraBounds(Vector2D pos) {
		int frameW = Constants.FRAME_WIDTH;
		int frameH = Constants.FRAME_HEIGHT;
		
		int relativeX = (int) (pos.x - cam.getXOffset());
		int relativeY = (int) (pos.y - cam.getYOffset());
		
		return (relativeX >= 0 && relativeX <= frameW) && 
			   (relativeY >= 0 && relativeY <= frameH);	
	}
	
	public GameCamera getGameCamera() {
		return cam;
	}
	
	public List<GameObject> getGameObjects() {
		return objects;
	}
	
	public int getTotalAsteroids() {
		return asteroidCtrl.getAsteroids().size();
	}
	
	public BlackHoleController getBlackHoleController() {
		return bhCtrl;
	}
	
	public AsteroidController getAsteroidController() {
		return asteroidCtrl;
	}
	
	public List<Asteroid> getAsteroids() {
		return asteroidCtrl.getAsteroids();
	}
	
	public PlayerShip getPlayer() {
		return ship;
	}
}
