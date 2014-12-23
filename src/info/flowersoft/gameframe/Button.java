package info.flowersoft.gameframe;

import info.flowersoft.gameframe.description.ScreenRect;
import info.flowersoft.gameframe.shape.ImageShape;
import info.flowersoft.gameframe.touch.TouchPoint;
import info.flowersoft.gameframe.touch.TouchUpdate;

public class Button implements Touchable {

	private ImageShape shape;
	
	private TouchPoint tp;
	
	private int defaultFrame;
	
	private int pressedFrame;
	
	public Button(ImageShape shape) {
		this(shape, 0, 1);
	}
	
	public Button(ImageShape shape, int defaultFrame, int pressedFrame) {
		this.shape = shape;
		
		this.defaultFrame = defaultFrame;
		this.pressedFrame = pressedFrame;
		
		updateShape();
	}
	
	public boolean update(TouchUpdate tpUpdate) {
		boolean result = false;
		
		if (tp == null && tpUpdate.getAddedTouchPoint() != null) {
			ScreenRect rect = shape.getAbsoluteScreenRect();
			
			if (rect.contains((int) tpUpdate.getAddedTouchPoint().getX(), (int) tpUpdate.getAddedTouchPoint().getY())) {
				tp = tpUpdate.getAddedTouchPoint();
				tpUpdate.clearAddedTouchPoint();
				result = true;
			}
		}
		
		if (tp != null && tp.equals(tpUpdate.getRemovedTouchPoint())) {
			tp = null;
		}
		
		updateShape();
		
		return result;
	}
	
	public void flush() {
		tp = null;
		
		updateShape();
	}
	
	public boolean isPressed() {
		return tp != null;
	}
	
	public TouchPoint getTouchPoint() {
		return tp;
	}
	
	private void updateShape() {
		if (isPressed()) {
			shape.setFrame(pressedFrame);
		} else {
			shape.setFrame(defaultFrame);
		}
	}
}
