package assignment.physics;

import assignment.gameobjects.Asteroid;
import assignment.gameobjects.BlackHole;
import assignment.gameobjects.Bullet;
import assignment.gameobjects.Puller;
import assignment.gameobjects.Ship;
import assignment.gameobjects.Crystal;

public interface Collidable {
	
	public void onCollision(Collidable col);
	
	public void onCollision(Ship ship);
	public void onCollision(Bullet b);
	public void onCollision(Asteroid a);
	public void onCollision(Crystal rock);
	public void onCollision(BlackHole bh);
	public void onCollision(Puller p);
}
