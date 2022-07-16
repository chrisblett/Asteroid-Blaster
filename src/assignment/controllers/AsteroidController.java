package assignment.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import assignment.audio.AudioPlayer;
import assignment.game.Game;
import assignment.game.World;
import assignment.gameobjects.Asteroid;
import assignment.gameobjects.Bullet;
import assignment.gameobjects.Crystal;
import assignment.math.Vector2D;

public class AsteroidController extends GameObjectController {
	
	public static final int LARGE_ASTEROID_POINTS = 100;
	public static final int MEDIUM_ASTEROID_POINTS = 200;
	public static final int SMALL_ASTEROID_POINTS = 500;
	
	private List<Asteroid> asteroids;
	
	public AsteroidController(World world) {
		GameObjectController.world = world;
		asteroids = new ArrayList<Asteroid>();
	}
	
	public void addAsteroidToWorld(Asteroid ast) {
		asteroids.add(ast);
		world.addObjectToWorld(ast);
	}
	
	public void makeRandomAsteroid(double radius) {
		Random r = new Random();
		
		// Generate random position in the world
		double x = r.nextInt(World.WORLD_SIZE);
		double y = r.nextInt(World.WORLD_SIZE);
		
		Asteroid ast = new Asteroid(new Vector2D(x, y), 
									Vector2D.generateRandomDirection().mult(Asteroid.MAX_SPEED), 
									radius);
		addAsteroidToWorld(ast);
	}
	
	public static void makeNewAsteroid(Vector2D pos, Vector2D vel, double radius) {
		Asteroid a = new Asteroid(pos,vel,radius);
		
		Random r = new Random();
		double roll = r.nextDouble();
		
		//System.out.println("Rolled: " + roll);
		if(roll < Crystal.DROP_CHANCE) {
			world.addNewRock(new Crystal(pos));
		}
		world.addNewAsteroidToWorld(a);
	}
	
	public static void onAsteroidDestroyed(Asteroid victim, Bullet b) {
		if(b.getOwner() == world.getPlayer()) {
			Game.addScore(victim.getPoints());
		} 
		// Only play the death sound if in view of the player
		if(world.inCameraBounds(victim.position)) {
			AudioPlayer.play(victim.getDeathSFX());
		}
	}
	
	// Need to remove the references of asteroids that have been removed from the main collection
	// in this collection too.
	public void clearDead() {
		asteroids.removeIf(a -> a.isDead());
	}
	
	public void invertVelocity() {
		for(Asteroid a : asteroids) {
			a.velocity.x = -a.velocity.x;
		}
	}
	
	public void addRandomLargeAsteroidToWorld() {
		makeRandomAsteroid(Asteroid.LARGE_RADIUS);
	}
	
	public List<Asteroid> getAsteroids() {
		return asteroids;
	}
}
