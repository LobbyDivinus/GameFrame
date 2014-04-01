package info.flowersoft.gameframe;

import info.flowersoft.gameframe.touch.TouchPoint;

public interface Touchable {

	public void update(TouchPoint add, TouchPoint remove);
	
	public void flush();
	
}
