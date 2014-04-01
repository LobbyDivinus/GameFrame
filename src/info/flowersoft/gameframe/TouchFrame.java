package info.flowersoft.gameframe;

import info.flowersoft.gameframe.description.ScreenRect;
import info.flowersoft.gameframe.touch.TouchPoint;

public class TouchFrame implements Touchable {

	private ScreenRect rect;
	
	private TouchPoint tp;
	
	public TouchFrame(ScreenRect frame) {
		rect = frame;
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
			tp = null;
		}
		
		return result;
	}

	@Override
	public void flush() {
		tp = null;
	}

}
