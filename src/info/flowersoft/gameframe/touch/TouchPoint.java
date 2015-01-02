package info.flowersoft.gameframe.touch;

import android.os.SystemClock;

/**
 * A TouchPoint objects represents a touch point on the screen of your device. TouchPoints are used by TouchMappers.
 * 
 * @author Lobby Divinus
 */
public class TouchPoint implements Cloneable {

	private int id;
	private boolean primary;
	private float firstX;
	private float firstY;
	private float x;
	private float y;
	private float lastX;
	private float lastY;
	private long timestamp;
	private TouchPoint ghost;
	
	private TouchPoint() {
	}
	
	/**
	 * Constructor to construct a new TouchPoint object.
	 * @param id Id of the new touch point.
	 * @param primary True if the touch point is a primary one.
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 */
	TouchPoint(int id, boolean primary, float x, float y) {
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
	
	/**
	 * Returns the id of the TouchPoint. Every TouchPoint has a unique id.
	 * @return Id of the TouchPoint.
	 */
	public int getID() {
		return id;
	}
	
	/**
	 * Determines whether the TouchPoint is a primary one.
	 * @return True if the TouchPoint is primary.
	 */
	public boolean isPrimary() {
		return primary;
	}
	
	/**
	 * Sets a new position for the TouchPoint.
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 */
	void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Sets a last position for the TouchPoint.
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 */
	void setLastPosition(float x, float y) {
		this.lastX = x;
		this.lastY = y;
	}
	
	/**
	 * Returns the x coordinate this TouchPoint had on it's creation.
	 * @return X coordinate.
	 */
	public float getFirstX() {
		return firstX;
	}
	
	/**
	 * Returns the y coordinate this TouchPoint had on it's creation.
	 * @return Y coordinate.
	 */
	public float getFirstY() {
		return firstY;
	}
	
	/**
	 * Returns the current x coordinate of this TouchPoint.
	 * @return X coordinate in pixels.
	 */
	public float getX() {
		return x;
	}
	
	/**
	 * Returns the current y coordinate of this TouchPoint.
	 * @return Y coordinate in pixels.
	 */
	public float getY() {
		return y;
	}
	
	/**
	 * Returns the x speed of this TouchPoint since the last frame.
	 * @return X speed in pixels.
	 */
	public float getXSpeed() {
		return x - lastX;
	}
	
	/**
	 * Returns the y speed of this TouchPoint since the last frame.
	 * @return Y speed in pixels.
	 */
	public float getYSpeed() {
		return y - lastY;
	}
	
	/**
	 * Returns the timestamp of creation for this TouchPoint.
	 * @return Timestamp in millisecs since boot.
	 */
	public long getTimestamp() {
		return timestamp;
	}
	
	/**
	 * Fills the TouchPoint with the values of the given TouchPoint tp so that will be equal.
	 * @param tp The TouchPoint to get values from.
	 */
	void fillWith(TouchPoint tp) {
		id = tp.id;
		primary = tp.primary;
		firstX = tp.firstX;
		firstY = tp.firstY;
		x = tp.x;
		y = tp.y;
		lastX = tp.lastX;
		lastY = tp.lastY;
		timestamp = tp.timestamp;
	}
	
	@Override
	public TouchPoint clone() {
		TouchPoint tp = new TouchPoint();
		
		tp.fillWith(this);
		
		return tp;
	}
	
	@Override
	public boolean equals(Object other) {
		boolean result = false;
		
		if (other instanceof TouchPoint) {
			TouchPoint tp = (TouchPoint) other;
			result = tp.getID() == id && tp.getTimestamp() == timestamp;
		}
		
		return result;
	}
	
	@Override
	public String toString() {
		String result = "";
		
		result += "TouchPoint " + id + " at ";
		result += "(" + x + ", " + y + ") ";
		result += "which is " + (primary ? "primary" : "not primary");
		
		return result;
	}
	
	/**
	 * Sets a ghost TouchPoint g.
	 * @param g The ghost TouchPoint.
	 */
	void setGhost(TouchPoint g) {
		ghost = g;
	}
	
	/**
	 * Returns the ghost TouchPoint.
	 * @return Ghost TouchPoint or Null if no one has been set.
	 */
	TouchPoint getGhost() {
		return ghost;
	}
}
