package info.flowersoft.gameframe;

import info.flowersoft.gameframe.description.ScreenRect;
import info.flowersoft.gameframe.touch.TouchPoint;

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
	public boolean update(TouchPoint add, TouchPoint remove) {
		boolean result = false;
		
		if (tp == null && add != null) {
			if (rect.contains((int) add.getX(), (int) add.getY())) {
				tp = add;
				result = true;
			}
		}
		
		if (tp != null && tp == remove) {
			if (handler != null && rect.contains((int) tp.getX(), (int) tp.getY())) {
				handler.run();
			}
			tp = null;
		}
		
		return result;
	}

	@Override
	public void flush() {
		tp = null;
	}

}
