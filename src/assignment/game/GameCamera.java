package assignment.game;

import assignment.util.Constants;

public class GameCamera {
	
	private int xOffset;
	private int yOffset;
	
	public GameCamera(int xOff, int yOff) {
		xOffset = xOff;
		yOffset = yOff;
	}
	
	public void keepWithinBounds() {
	
		if(xOffset < 0) {
			xOffset = 0;
		}
		if(xOffset + Constants.FRAME_WIDTH > World.WORLD_SIZE) {
			xOffset = World.WORLD_SIZE - Constants.FRAME_WIDTH;
		}
		
		if(yOffset < 0) {
			yOffset = 0;
		}
		
		if(yOffset + Constants.FRAME_HEIGHT > World.WORLD_SIZE) {
			yOffset = World.WORLD_SIZE - Constants.FRAME_HEIGHT;
		}
	}
	
	public int getXOffset() {
		return xOffset;
	}
	
	public int getYOffset() {
		return yOffset;
	}
	
	public void setXOffset(int x) {
		xOffset = x;
	}
	
	public void setYOffset(int y) {
		yOffset = y;
	}
	
	@Override
	public String toString() {
		return "[" + xOffset + ", " + yOffset + "]";
	}

}
