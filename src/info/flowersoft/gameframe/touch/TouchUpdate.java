package info.flowersoft.gameframe.touch;

public class TouchUpdate {

	private TouchPoint add;
	
	private TouchPoint remove;
	
	public TouchUpdate(TouchPoint tpAdd, TouchPoint tpRemove) {
		add = tpAdd;
		remove = tpRemove;
	}
	
	public TouchPoint getAddedTouchPoint() {
		return add;
	}
	
	public TouchPoint getRemovedTouchPoint() {
		return remove;
	}
	
	public void clearAddedTouchPoint() {
		add = null;
	}
	
	public void clearRemovedTouchPoint() {
		remove = null;
	}
	
}
