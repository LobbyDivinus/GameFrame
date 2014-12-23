package info.flowersoft.gameframe;

import info.flowersoft.gameframe.description.ScreenRect;
import info.flowersoft.gameframe.touch.TouchPoint;
import info.flowersoft.gameframe.touch.TouchUpdate;

public class TouchFrame implements Touchable {

	private ScreenRect rect;
	
	private TouchPoint tp;
	
	private Runnable handler;
	
	public TouchFrame(ScreenRect frame) {
		this(frame, null);
	}
	
	public TouchFrame(ScreenRect frame, Runnable handler) {
		rect = frame;
		this.handler = handler;
	}
	
	public boolean isPressed() {
		return tp != null;
	}
	
	public TouchPoint getTouchPoint() {
		return tp;
	}
	
	@Override
	public void update(TouchUpdate tpUpdate) {
		if (tp == null && tpUpdate.getAddedTouchPoint() != null) {
			if (rect.contains((int) tpUpdate.getAddedTouchPoint().getX(), (int) tpUpdate.getAddedTouchPoint().getY())) {
				tp = tpUpdate.getAddedTouchPoint();
				tpUpdate.clearAddedTouchPoint();
			}
		}
		
		if (tp != null && tp.equals(tpUpdate.getRemovedTouchPoint())) {
			if (handler != null && rect.contains((int) tp.getX(), (int) tp.getY())) {
				handler.run();
			}
			tp = null;
		}
	}

	@Override
	public void flush() {
		tp = null;
	}

}
