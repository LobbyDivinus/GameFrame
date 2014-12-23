package info.flowersoft.gameframe;

import info.flowersoft.gameframe.touch.TouchUpdate;

public interface Touchable {

	public void update(TouchUpdate tpUpdate);
	
	public void flush();
	
}
