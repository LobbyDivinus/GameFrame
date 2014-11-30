package info.flowersoft.gameframe;

import info.flowersoft.gameframe.touch.TouchPoint;

import java.util.ArrayList;

public class TouchableCollection extends ArrayList<Touchable> {

	/**
	 * Serial id.
	 */
	private static final long serialVersionUID = -5256962057074979788L;
	
	public void update(TouchPoint add, TouchPoint remove) {
		for	(Touchable t:this) {
			t.update(add, remove);
		}
	}
	
	public void flush() {
		for	(Touchable t:this) {
			t.flush();
		}
	}
}
