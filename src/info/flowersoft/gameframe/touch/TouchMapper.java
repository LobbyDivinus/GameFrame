package info.flowersoft.gameframe.touch;



import java.util.LinkedList;
import java.util.List;

import android.view.MotionEvent;

/**
 * The touch mapper allows you to handle touch input frame wise instead per event. This is useful for games.
 * 
 * @author Lobby Divinus
 */
public class TouchMapper {

	private boolean useNewList;
	private boolean useRemovedList;
	private List<TouchPoint> activePoints;
	private List<TouchPoint> newPoints;
	private List<TouchPoint> removedPoints;
	private List<TouchPoint> publicActivePoints;
	private TouchPoint newPoint;
	private TouchPoint removedPoint;
	
	/**
	 * Construct a new touch mapper.
	 */
	public TouchMapper() {
		this(true, true);
	}
	
	/**
	 * Construct a new touch mapper. Use this constructor if you won't call getTouchUpdate every frame.
	 * @param useNewList New points should be collected in a list.
	 * @param useRemovedList Removed points should be collected in a list.
	 */
	public TouchMapper(boolean useNewList, boolean useRemovedList) {
		activePoints = new LinkedList<TouchPoint>();
		newPoints = new LinkedList<TouchPoint>();
		removedPoints = new LinkedList<TouchPoint>();
		publicActivePoints = new LinkedList<TouchPoint>();
		
		this.useNewList = useNewList;
		this.useRemovedList = useRemovedList;
	}
	
	/**
	 * Gets a list of all active touch points. Don't modify this list!
	 * @return A list of all currently active touch points.
	 */
	public List<TouchPoint> getActivePoints() {
		return activePoints;
	}
	
	/**
	 * Just don't use this method.
	 * @return Oldest touchpoint in the list of new touch points, or null if the list is empty.
	 */
	@Deprecated
	public TouchPoint getNewPoint() {
		return removedPoint;
	}
	
	/**
	 * Just don't use this method.
	 * @return Oldest touchpoint in the list of removed touch point or null, if the list is empty.
	 */
	@Deprecated
	public TouchPoint getRemovedPoint() {
		return newPoint;
	}
	
	/**
	 * Call this method every frame to get a touch update.
	 * @return The current touch update to handle for this frame.
	 */
	public synchronized TouchUpdate getTouchUpdate() {
		newPoint = newPoints.isEmpty() ? null : newPoints.remove(0).getGhost();
		removedPoint = removedPoints.isEmpty() ? null : removedPoints.remove(0).getGhost();
		
		publicActivePoints.clear();
		for (TouchPoint tp : activePoints) {
			TouchPoint ghost = tp.getGhost();
			ghost.fillWith(tp);
			tp.setLastPosition(tp.getX(), tp.getY());
			publicActivePoints.add(ghost);
		}
		
		return new TouchUpdate(newPoint, removedPoint);
	}
	
	/**
	 * Gets the primary touch point. The primary touch point is the touch point that occurred at first after a pase
	 * without active touch points.
	 * @return The primary touch point or null if none exists.
	 */
	public synchronized TouchPoint getPrimarayPoint() {
		for (TouchPoint tp:publicActivePoints) {
			if (tp.isPrimary()) {
				return tp;
			}
		}
		
		return null;
	}
	
	/**
	 * Flushes all handled touch points.
	 */
	public synchronized void flush() {
		activePoints.clear();
		newPoints.clear();
		removedPoints.clear();
	}
	
	/**
	 * Call this method on every MotionEvent your app receives.
	 * @param e MotionEvent to handle.
	 */
	public synchronized void map(MotionEvent e) {
		
		if (e == null) {
			return;
		}
		
		for (int i = 0; i < e.getPointerCount(); i++) {
			TouchPoint tp = getPoint(e.getPointerId(i));
			if (tp == null) {
				if (i == e.getActionIndex()) {
					if (e.getActionMasked() == MotionEvent.ACTION_DOWN) {
						addTouchPoint(e.getPointerId(i), e, i, true);
					} else if (e.getActionMasked() == MotionEvent.ACTION_POINTER_DOWN) {
						addTouchPoint(e.getPointerId(i), e, i, false);
					}
				}
			} else {
				fillTouchPoint(tp, e, i);
				
				if (i == e.getActionIndex()
						&& (e.getActionMasked() == MotionEvent.ACTION_UP
						|| e.getActionMasked() == MotionEvent.ACTION_POINTER_UP)) {
					removeTouchPoint(tp);
				}
			}
		}
		
		for (TouchPoint tp:activePoints) {
			boolean inUse = false;
			for (int i = 0; i < e.getPointerCount() && !inUse; i++) {
				if (e.getPointerId(i) == tp.getID()) {
					inUse = true;
				}
			}
			
			if (!inUse) {
				removeTouchPoint(tp);
			}
		}
	}
	
	/**
	 * Finds the touchpoint with the given id and returns it.
	 * @param id Id of a touch point.
	 * @return The touchpoint with the fiven id, or null, if no such touch point has been found.
	 */
	public synchronized TouchPoint getPoint(int id) {
		for (TouchPoint tp:activePoints) {
			if (tp.getID() == id) {
				return tp;
			}
		}
		
		return null;
	}
	
	
	private  TouchPoint addTouchPoint(int id, MotionEvent e, int idx, boolean primary) {
		TouchPoint tp = new TouchPoint(id, primary, e.getX(idx), e.getY(idx));
		tp.setGhost(tp.clone());
		
		if (useNewList) {
			newPoints.add(tp);
		}
		activePoints.add(tp);
		
		return tp;
	}
	
	private void fillTouchPoint(TouchPoint tp, MotionEvent e, int idx) {
		tp.setPosition(e.getX(idx), e.getY(idx));
	}
	
	private void removeTouchPoint(TouchPoint tp) {
		newPoints.remove(tp);
		activePoints.remove(tp);
		if (useRemovedList) {
			removedPoints.add(tp);
		}
		tp.getGhost().fillWith(tp);
	}
}