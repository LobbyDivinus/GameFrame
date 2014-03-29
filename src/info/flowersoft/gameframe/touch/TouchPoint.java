package info.flowersoft.gameframe.touch;

import android.os.SystemClock;

public class TouchPoint {

	private int id;
	private boolean primary;
	private float firstX;
	private float firstY;
	private float x;
	private float y;
	private float lastX;
	private float lastY;
	private long timestamp;
	
	public TouchPoint(int id, boolean primary, float x, float y) {
		this.id = id;
		this.primary = primary;
		firstX = x;
		firstY = y;
		this.x = x;
		this.y = y;
		lastX = x;
		lastY = y;
		timestamp = SystemClock.uptimeMillis();
	}
	
	public int getID() {
		return id;
	}
	
	public boolean isPrimary() {
		return primary;
	}
	
	public void setPosition(float x, float y) {
		lastX = this.x;
		lastY = this.y;
		this.x = x;
		this.y = y;
	}
	
	public float getFirstX() {
		return firstX;
	}
	
	public float getFirstY() {
		return firstY;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public float getXSpeed() {
		return x - lastX;
	}
	
	public float getYSpeed() {
		return y - lastY;
	}
	
	public long getTimestamp() {
		return timestamp;
	}
	
}
