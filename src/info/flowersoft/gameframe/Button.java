package info.flowersoft.gameframe;

import info.flowersoft.gameframe.description.ScreenRect;
import info.flowersoft.gameframe.shape.ImageShape;
import info.flowersoft.gameframe.touch.TouchPoint;

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
	
	public boolean update(TouchPoint add, TouchPoint remove) {
		boolean result = false;
		
		if (tp == null && add != null) {
			ScreenRect rect = shape.getAbsoluteScreenRect();
			
			if (rect.contains((int) add.getX(), (int) add.getY())) {
				tp = add;
				result = true;
			}
		}
		
		if (tp != null && tp == remove) {
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
