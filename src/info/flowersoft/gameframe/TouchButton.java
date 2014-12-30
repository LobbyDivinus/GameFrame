package info.flowersoft.gameframe;

import info.flowersoft.gameframe.touch.TouchPoint;

import com.threed.jpct.World;
import com.threed.jpct.util.Overlay;


public class TouchButton {
	
	private World world;
	
	private TouchPoint tp;
	
	private Image defaultTex;
	
	private Image pressedTex;
	
	private int shapeX;
	
	private int shapeY;
	
	private int shapeW;
	
	private int shapeH;
	
	public class Image {
		private Overlay img;
		private int sourceX;
		private int sourceY;
		private int sourceW;
		private int sourceH;
		private boolean show;
		private void set(String tex, int x, int y, int w, int h) {
			if (img != null) {
				img.dispose();
				img = null;
			}
			
			if (!tex.equals("")) {
				sourceX = x;
				sourceY = y;
				sourceW = w;
				sourceH = h;
				img = new Overlay(world, shapeX, shapeY, shapeX + shapeW, shapeY + shapeH, tex, true);
				img.setSourceCoordinates(sourceX, sourceY, sourceW, sourceH);
				img.setTransparency(15);
				if (!show) {
					img.setVisibility(false);
				}
			}
		}
		private void updateShape() {
			if (img != null) {
				img.setNewCoordinates(shapeX, shapeY, shapeX + shapeW, shapeY + shapeH);
			}
		}
		private void show() {
			show = true;
			if (img != null) {
				img.setVisibility(show);
			}
		}
		private void hide() {
			show = false;
			if (img != null) {
				img.setVisibility(show);
			}
		}
	}
	
	public TouchButton(World world) {
		this.world = world;
		defaultTex = new Image();
		pressedTex = new Image();
		defaultTex.show();
	}
	
	public void setTexture(String tex, int sourceX, int sourceY, int sourceW, int sourceH) {
		defaultTex.set(tex, sourceX, sourceY, sourceW, sourceH);
	}
	
	public void setPressedTexture(String tex, int sourceX, int sourceY, int sourceW, int sourceH) {
		pressedTex.set(tex, sourceX, sourceY, sourceW, sourceH);
	}
	
	public Overlay getOverlay() {
		return defaultTex.img;
	}
	
	public Overlay getPressedOverlay() {
		return pressedTex.img;
	}
	
	public void setShape(int x, int y, int w, int h) {
		shapeX = x;
		shapeY = y;
		shapeW = w;
		shapeH = h;
		defaultTex.updateShape();
		pressedTex.updateShape();
	}
	
	public boolean isPressed() {
		return tp != null;
	}
	
	public TouchPoint getTouchPoint() {
		return tp;
	}
	
	public void release() {
		tp = null;
	}
	
	public void update(TouchPoint newPoint, TouchPoint removedPoint) {
		if (tp == null && newPoint != null) {
			int x = (int) newPoint.getX();
			int y = (int) newPoint.getY();
			boolean in = (x >= shapeX && y >= shapeY && x <= shapeX + shapeW && y <= shapeY + shapeH);
			if (in) {
				tp = newPoint;
				defaultTex.hide();
				pressedTex.show();
			}
		}
		if (tp == removedPoint) {
			tp = null;
			defaultTex.show();
			pressedTex.hide();
		}
	}
}
