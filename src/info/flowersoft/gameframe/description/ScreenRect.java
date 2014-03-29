package info.flowersoft.gameframe.description;


public class ScreenRect implements ScreenShape {

	private int x;
	private int y;
	private int width;
	private int height;
	
	public ScreenRect(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	@Override
	public boolean contains(int px, int py) {
		return px >= x && py >= y && px <= x + width && py <= y + height;
	}

}
