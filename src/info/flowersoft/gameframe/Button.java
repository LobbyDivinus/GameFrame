package info.flowersoft.gameframe;

import info.flowersoft.gameframe.description.ScreenRect;
import info.flowersoft.gameframe.shape.ImageShape;
import info.flowersoft.gameframe.touch.TouchPoint;

public class Button {

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
	
	public void update(TouchPoint add, TouchPoint remove) {
		ScreenRect rect = shape.getAbsoluteScreenRect();
		
		if (tp == null) {
			if (rect.contains((int) add.getX(), (int) add.getY())) {
				tp = add;
			}
		}
		
		if (tp != null && tp == remove) {
			tp = null;
		}
		
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
