package assignment.resource;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;

import assignment.math.Vector2D;

public class Sprite {

	private Image image;
	private Vector2D position;
	private Vector2D direction;
	private double w;
	private double h;
	
	public Sprite(Image image, Vector2D pos, Vector2D dir, double w, double h) {
		this.image = image;
		this.position = pos;
		this.direction = dir;
		this.w = w;
		this.h = h;
	}
	
	public void draw(Graphics2D g2d, int xOffset, int yOffset) {
		
		Vector2D relativeCamPos = new Vector2D(position.x - xOffset,
				position.y - yOffset);
		
		double imW = image.getWidth(null);
		double imH = image.getHeight(null);
		
		AffineTransform t = new AffineTransform();
		t.rotate(direction.angle() + Math.PI /2 , 0, 0);
		t.scale(w / imW, h / imH);
		t.translate(-imW / 2.0, -imH / 2.0);
		
		AffineTransform t0 = g2d.getTransform();
		g2d.translate(relativeCamPos.x, relativeCamPos.y);
		g2d.drawImage(image, t, null);
		g2d.setTransform(t0);
	}
}
