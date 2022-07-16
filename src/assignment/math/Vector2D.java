package assignment.math;

import java.util.Random;

// This is a Mutable 2D vector
public final class Vector2D {
	public double x;
	public double y;
	
	public Vector2D() {
		this.x = 0.0;
		this.y = 0.0;
	}
	
	public Vector2D(Vector2D v) {
		x = v.x;
		y = v.y;
	}
	
	public Vector2D(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector2D set(double x, double y) {
		this.x = x;
		this.y = y;
		return this;
	}
	
	public Vector2D set(Vector2D v) {
		x = v.x;
		y = v.y;
		return this;
	}
	
	public double mag() {
		return Math.hypot(x, y);
	}
	
	// Angle between vector and horizontal axis in radians
	// In the range [-PI, PI]
	public double angle() {
		return Math.atan2(y,  x);
	}
	
	public double angle(Vector2D v) {
		double result = v.angle() - angle();
		if(result > Math.PI) {
			result -= 2*Math.PI;
		} else if(result < -Math.PI) {
			result += 2*Math.PI;
		}
		return result;
	}
	
	public Vector2D add(double x, double y) {
		this.x += x;
		this.y += y;
		return this;
	}
	
	public Vector2D add(Vector2D v) {
		x += v.x;
		y += v.y;
		return this;
	}
	
	public Vector2D addScaled(Vector2D v, double fac) {
		x += v.x * fac;
		y += v.y * fac;
		return this;
	}
	
	public Vector2D subtract(double x, double y) {
		this.x -= x;
		this.y -= y;
		return this;
	}
	
	public Vector2D subtract(Vector2D v) {
		x -= v.x;
		y -= v.y;
		return this;
	}
	
	public Vector2D mult(double fac) {
		x *= fac;
		y *= fac;
		return this;
	}
	
	// Rotate by a given angle in radians
	public Vector2D rotate(double angle) {
		double x = this.x * Math.cos(angle) - this.y * Math.sin(angle);
		double y = this.x * Math.sin(angle) + this.y * Math.cos(angle);
		
		set(x, y);
		return this;
	}
	
	public double dot(Vector2D v) {
		return x * v.x + y * v.y;
	}
	
	public double dist(Vector2D v) {
		return Math.hypot(x - v.x, y - v.y);
	}
	
	public Vector2D normalise() {
     double mag = this.mag();
     x /= mag;
     y /= mag;
     return this;
	}
	
	// Assumes w > 0 and h > 0
	public Vector2D wrap(double w, double h) {
		if(x < 0) {
			x += w;
		}
	
		if(x > w) {
			x-= w;
		}
		
		if(y < 0) {
			y += h;
		}
		
		if(y > h) {
			y-= h;
		}
		
		return this;
	}
	
	public static Vector2D polar(double angle, double mag) {
		return new Vector2D(mag * Math.cos(angle), mag * Math.sin(angle));
	}
	
	public static Vector2D generateRandomDirection() {
		Random r = new Random();		
		return new Vector2D(r.nextInt(), r.nextInt()).normalise();
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o) {
			return true;
		}
		
		if(o == null) {
			return false;
		}
		
		if(getClass() != o.getClass()) {
			return false;
		}
		
		Vector2D v = (Vector2D)o;
		
		// Cannot compare just using equality operator
		// due to floating-point rounding errors
		
		final double EPSILON = 0.01;
		
		double xDiff = Math.abs(x - v.x);
		double yDiff = Math.abs(y - v.y);
		
		return xDiff < EPSILON && yDiff < EPSILON;
	}
	
	@Override
	public String toString() {
		return new String("[" + x + "," + y + "]");
	}
}
