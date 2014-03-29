package info.flowersoft.gameframe;

import info.flowersoft.gameframe.description.FontDescription;
import info.flowersoft.gameframe.description.ImageDescription;

import com.threed.jpct.FrameBuffer;
import com.threed.jpct.RGBColor;
import com.threed.jpct.Texture;

public class BlittingEngine {

	FrameBuffer buffer;
	
	int trans;
	
	boolean addivitve;
	
	RGBColor color;
	
	float moveX;
	
	float moveY;
	
	float scaleX;
	
	float scaleY;
	
	int virtX;
	
	int virtY;
	
	int virtW;
	
	int virtH;
	
	public BlittingEngine(FrameBuffer buf) {
		buffer = buf;
		trans = 15;
		addivitve = false;
		color = RGBColor.WHITE;
		scaleX = 1f;
		scaleY = 1f;
		virtX = 0;
		virtY = 0;
		virtW = buf.getWidth();
		virtH = buf.getHeight();
	}
	
	public void setBuffer(FrameBuffer buf) {
		buffer = buf;
		virtX = 0;
		virtY = 0;
		virtW = buf.getWidth();
		virtH = buf.getHeight();
	}
	
	public FrameBuffer getBuffer() {
		return buffer;
	}
	
	public void setTransparency(int alpha) {
		trans = alpha;
	}
	
	public int getTransparency() {
		return trans;
	}
	
	public void setAdditiveBlending(boolean enable) {
		addivitve = enable;
	}
	
	public boolean isAdditiveBlending() {
		return addivitve;
	}
	
	public void setColor(int r, int g, int b) {
		color = new RGBColor(r, g, b);
	}
	
	public void setColor(RGBColor col) {
		color = col;
	}
	
	public RGBColor getColor() {
		return color;
	}
	
	public void setMovement(float x, float y) {
		moveX = x;
		moveY = y;
	}
	
	public float getMovementX() {
		return moveX;
	}
	
	public float getMovementY() {
		return moveY;
	}
	
	public void setScale(float x, float y) {
		scaleX = x;
		scaleY = y;
	}
	
	public float getScaleX() {
		return scaleX;
	}
	
	public float getScaleY() {
		return scaleY;
	}
	
	public void setVirtualResolution(int x, int y, int w, int h) {
		virtX = x;
		virtY = y;
		virtW = w;
		virtH = h;
	}
	
	public void blitImage(ImageDescription img, int x, int y) {
		blitImage(img, x, y, 0);
	}
	
	public void blitImage(ImageDescription img, int x, int y, int frame) {
		Texture tex = img.getTextureObj();
		int w = tex.getWidth();
		int h = tex.getHeight();
		
		blitImage(img, x, y, w, h, frame);
	}
	
	public void blitImage(ImageDescription img, int x, int y, int width, int height, int frame) {
		Texture tex = img.getTextureObj();
		int w = tex.getWidth();
		int h = tex.getHeight();
		float[] uv = img.getUVCoords(frame);
		
		int px = (int) ((x - virtX + moveX) * (buffer.getWidth() / virtW));
		int py = (int) ((y - virtY + moveY) * (buffer.getHeight() / virtH));
		
		buffer.blit(tex,
				(int) (uv[0] * w),
				(int) (uv[1] * h),
				px,
				py,
				(int) ((uv[2] - uv[0]) * w),
				(int) ((uv[3] - uv[1]) * h),
				(int) (scaleX * width),
				(int) (scaleY * height),
				trans,
				addivitve,
				color);
	}
	
	public void blitTextLine(FontDescription font, String text, int x, int y) {
		Texture tex = font.getTextureObj();
		int w = tex.getWidth();
		int h = tex.getHeight();
		
		int px = x;
		int py = y;
		
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			float[] uv = font.getUVCoords(c);
			
			if (uv != null) {
				float gw = font.getGlyphWidth(c);
				float gh = font.getGlyphHeight(c);
				
				buffer.blit(tex,
						(int) (uv[0] * w),
						(int) (uv[1] * h),
						(int) ((px - virtX + moveX) * (buffer.getWidth() / virtW)),
						(int) ((py - virtY + moveY) * (buffer.getHeight() / virtH)),
						(int) ((uv[2] - uv[0]) * w),
						(int) ((uv[3] - uv[1]) * h),
						(int) (scaleX * gw * (buffer.getWidth() / virtW)),
						(int) (scaleY * gh * (buffer.getHeight() / virtH)),
						trans,
						addivitve,
						color);
				
				px += (int) (scaleX * (gw + font.getHorizontalSpacing()));
			}
		}
	}
}
