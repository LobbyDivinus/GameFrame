package info.flowersoft.gameframe;

import info.flowersoft.gameframe.touch.TouchPoint;

public interface Touchable {

	public boolean update(TouchPoint add, TouchPoint remove);
	
	public void flush();
	
}
