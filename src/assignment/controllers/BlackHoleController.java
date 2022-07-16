package assignment.controllers;

import java.util.ArrayList;
import java.util.List;

import assignment.game.World;
import assignment.gameobjects.BlackHole;

public class BlackHoleController extends GameObjectController {
	
	private List<BlackHole> holes;
	
	public BlackHoleController(World world) {
		GameObjectController.world = world;
		holes = new ArrayList<BlackHole>();
	}
	
	public void addBlackHole(BlackHole bh) {
		holes.add(bh);
		world.addObjectToWorld(bh);
		world.addObjectToWorld(bh.getPuller());
	}
	
	public void increaseAllPullRadius() {
		for(BlackHole bh : holes) {
			bh.getPuller().increaseRadius(1);
		}
	}
}
