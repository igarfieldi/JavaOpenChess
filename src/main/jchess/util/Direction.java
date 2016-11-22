package jchess.util;

public class Direction
{
	private int x;
	private int y;
	
	public Direction(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Direction(Direction dir) {
		this.x = dir.x;
		this.y = dir.y;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public Direction add(Direction dir) {
		this.x += dir.x;
		this.y += dir.y;
		return this;
	}
	
	public Direction subtract(Direction dir) {
		this.x += dir.x;
		this.y += dir.y;
		return this;
	}
	
	public Direction multiply(int factor) {
		this.x *= factor;
		this.y *= factor;
		return this;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) {
			return true;
		} else if((obj == null) || !(obj instanceof Direction)) {
			return false;
		} else {
			return (((Direction) obj).x == x) && (((Direction) obj).y == y);
		}
	}
}
