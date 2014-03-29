package info.flowersoft.gameframe.touch;



import java.util.LinkedList;
import java.util.List;

import android.view.MotionEvent;

public class TouchMapper {

	private boolean useNewList;
	private boolean useRemovedList;
	private List<TouchPoint> activePoints;
	private List<TouchPoint> newPoints;
	private List<TouchPoint> removedPoints;
	
	public TouchMapper() {
		this(true, true);
	}
	
	public TouchMapper(boolean useNewList, boolean useRemovedList) {
		activePoints = new LinkedList<TouchPoint>();
		newPoints = new LinkedList<TouchPoint>();
		removedPoints = new LinkedList<TouchPoint>();
		
		this.useNewList = useNewList;
		this.useRemovedList = useRemovedList;
	}
	
	public List<TouchPoint> getActivePoints() {
		return activePoints;
	}
	
	public TouchPoint getNewPoint() {
		if (newPoints.size() > 0) {
			return newPoints.remove(0);
		} else {
			return null;
		}
	}
	
	public TouchPoint getRemovedPoint() {
		if (removedPoints.size() > 0) {
			return removedPoints.remove(0);
		} else {
			return null;
		}
	}
	
	public TouchPoint getPrimarayPoint() {
		for (TouchPoint tp:activePoints) {
			if (tp.isPrimary()) {
				return tp;
			}
		}
		
		return null;
	}
	
	public void flush() {
		activePoints.clear();
		newPoints.clear();
		removedPoints.clear();
	}
	
	public void map(MotionEvent e) {
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
				
				if (i == e.getActionIndex() &&
						(e.getActionMasked() == MotionEvent.ACTION_UP
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
	
	public TouchPoint getPoint(int id) {
		for (TouchPoint tp:activePoints) {
			if (tp.getID() == id) {
				return tp;
			}
		}
		
		return null;
	}
	
	private TouchPoint addTouchPoint(int id, MotionEvent e, int idx, boolean primary) {
		TouchPoint tp = new TouchPoint(id, primary, e.getX(idx), e.getY(idx));
		
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
	}
}