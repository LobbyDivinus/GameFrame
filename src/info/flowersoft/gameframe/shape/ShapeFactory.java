package info.flowersoft.gameframe.shape;

import info.flowersoft.gameframe.description.Brush;
import info.flowersoft.gameframe.description.FontDescription;
import info.flowersoft.gameframe.description.ImageDescription;

import java.util.ArrayList;
import java.util.List;

import com.threed.jpct.Camera;
import com.threed.jpct.FrameBuffer;
import com.threed.jpct.Object3D;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.World;

/**
 * Factory for two dimensional shapes. Every factory is bound to a specific camera.
 * 
 * @author Fabian Miltenberger
 */
public class ShapeFactory {

	private static final float Z_DISTANCE = 2f;
	
	private World world;
	
	private Camera camera;
	
	private FrameBuffer buffer;
	
	private Brush brush;
	
	private Object3D master;
	
	private List<Shape> shapeList;
	
	private float moveX;
	
	private float moveY;
	
	private float scaleX;
	
	private float scaleY;
	
	private float angle;
	
	private float lineWidth;
	
	private float virtX;
	
	private float virtY;
	
	private float virtW;
	
	private float virtH;
	
	/**
	 * Creates a new shape factory for given world and frame buffer.
	 * @param w world in which shapes should be created
	 * @param buf frame buffer on which objects will be drawn
	 */
	public ShapeFactory(World w, FrameBuffer buf) {
		world = w;
		camera = w.getCamera();
		buffer = buf;
		brush = Brush.WHITE;
		master = Object3D.createDummyObj();
		shapeList = new ArrayList<Shape>();
		moveX = 0f;
		moveY = 0f;
		scaleX = 1f;
		scaleY = 1f;
		angle = 0f;
		lineWidth = 1f;
		virtX = 0;
		virtY = 0;
		virtW = buf.getWidth();
		virtH = buf.getHeight();
	}
	
	/**
	 * Sets a virtual resolution for shape creation and modifying.
	 * @param x coordinate where viewport should begin
	 * @param y coordinate wher viewport should begin
	 * @param w width for the viewport
	 * @param h height for the viewport
	 */
	public void setVirtualResolution(float x, float y, float w, float h) {
		virtX = x;
		virtY = y;
		virtW = w;
		virtH = h;
	}
	
	/**
	 * Sets a new camera for the factory.
	 * @param cam new camera
	 */
	public void setCamera(Camera cam) {
		camera = cam;
	}
	
	/**
	 * Get camera the factory is currently based on.
	 * @return camera
	 */
	public Camera getCamera() {
		return camera;
	}
	
	/**
	 * Sets a new frame buffer for the world. Resets virtual resolution.
	 * @param buf new frame buffer
	 */
	public void setBuffer(FrameBuffer buf) {
		buffer = buf;
		virtX = 0;
		virtY = 0;
		virtW = buf.getWidth();
		virtH = buf.getHeight();
	}
	
	/**
	 * Get frame buffer factory is currently working on.
	 * @return frame buffer
	 */
	public FrameBuffer getBuffer() {
		return buffer;
	}
	
	/**
	 * Sets a new general brush. Will be used for newly created shapes.
	 * @param b new brush
	 */
	public void setBrush(Brush b) {
		brush = b;
	}
	
	/**
	 * Return current default brush.
	 * @return brush
	 */
	public Brush getBrush() {
		return brush;
	}
	
	public void setMovement(float x, float y) {
		moveX = x;
		moveY = y;
	}
	
	public float getMoveX() {
		return moveX;
	}
	
	public float getMoveY() {
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
	
	public void setRotation(float rot) {
		angle = rot;
	}
	
	public float getRotation() {
		return angle;
	}
	
	public void setLineWidth(float w) {
		lineWidth = w;
	}
	
	public float getLineWidth() {
		return lineWidth;
	}
	
	public Object3D getMaster() {
		return master;
	}
	
	public Shape createUnfilledOval(float x, float y, float width, float height, float quality) {
		return createUnfilledOval(x, y, width, height, (int) (2 * quality * Math.sqrt(width + height)));
	}
	
	public Shape createUnfilledOval(float x, float y, float width, float height, int quads) {
		Shape s = new Shape(this, brush.clone(), 2 * quads);
		
		float angleStep = (float) (2.0 * Math.PI / quads);
		float angle = angleStep;
		
		float preOuterX = width;
		float preOuterY = height / 2f;
		float preInnerX = width - lineWidth;
		float preInnerY = height / 2f;
		
		for (int i = 1; i < quads + 1; i++) {
			float curOuterX = (float) (width * (0.5f + 0.5f * Math.cos(angle)));
			float curOuterY = (float) (height * (0.5f + 0.5f * Math.sin(angle)));
			float curInnerX = (float) (width * 0.5f + (width * 0.5f - lineWidth) * Math.cos(angle));
			float curInnerY = (float) (height * 0.5f + (height * 0.5f - lineWidth) * Math.sin(angle));
			
			s.addTriangle(curInnerX, curInnerY, curOuterX, curOuterY, preOuterX, preOuterY, x, y);
			s.addTriangle(curInnerX, curInnerY, preOuterX, preOuterY, preInnerX, preInnerY, x, y);
			
			preOuterX = curOuterX;
			preOuterY = curOuterY;
			preInnerX = curInnerX;
			preInnerY = curInnerY;
			angle += angleStep;
		}
		
		s.width = width;
		s.height = height;
		
		finalizeShape(s, x, y);
		return s;
	}
	
	public Shape createOval(float x, float y, float width, float height, float quality) {
		return createOval(x, y, width, height, (int) (2 * quality * Math.sqrt(width + height)));
	}
	
	public Shape createOval(float x, float y, float width, float height, int triangles) {
		Shape s = new Shape(this, brush.clone(), triangles);
		
		float angleStep = (float) (2.0 * Math.PI / (triangles + 2));
		float angle = angleStep;
		
		float preX = 0f;
		float preY = 0f;
		
		for (int i = 1; i < triangles + 2; i++) {
			float curX = (float) (width * (0.5f + 0.5f * Math.cos(angle)));
			float curY = (float) (height * (0.5f + 0.5f * Math.sin(angle)));
			
			if (i >= 2) {
				s.addTriangle(curX, curY, preX, preY, width, height / 2f, x, y);
			}
			
			preX = curX;
			preY = curY;
			angle += angleStep;
		}
		
		s.width = width;
		s.height = height;
		
		finalizeShape(s, x, y);
		return s;
	}
	
	public Shape createUnfilledRect(float x, float y, float width, float height) {
		Shape s = new Shape(this, brush.clone(), 8);
		
		// Top
		s.addTriangle(0, 0, 0, lineWidth, width, lineWidth, x, y);
		s.addTriangle(0, 0, width, lineWidth, width, 0, x, y);
		
		// Bottom
		s.addTriangle(0, height - lineWidth, 0, height, width, height, x, y);
		s.addTriangle(0, height - lineWidth, width, height, width, height - lineWidth, x, y);
		
		// Left
		s.addTriangle(0, lineWidth, 0, height - lineWidth, lineWidth, height - lineWidth, x, y);
		s.addTriangle(0, lineWidth, lineWidth, height - lineWidth, lineWidth, lineWidth, x, y);
		
		// Right
		s.addTriangle(width - lineWidth, lineWidth, width - lineWidth, height - lineWidth, width, height - lineWidth,
				x, y);
		s.addTriangle(width - lineWidth, lineWidth, width, height - lineWidth, width, lineWidth, x, y);
		
		s.width = width;
		s.height = height;
		
		finalizeShape(s, x, y);
		return s;
	}
	
	public Shape createRect(float x, float y, float width, float height) {
		Shape s = new Shape(this, brush.clone(), 2);
		
		s.addTriangle(0, 0, 0, height, width, 0, x, y);
		s.addTriangle(width, 0, 0, height, width, height, x, y);
		
		s.width = width;
		s.height = height;
		
		finalizeShape(s, x, y);
		return s;
	}
	
	public Shape createPolygon(float[] xy) {
		Shape s = new Shape(this, brush.clone(), xy.length - 2);
		
		float minX = Math.min(xy[0], xy[2]);
		float maxX = Math.max(xy[0], xy[2]);
		float minY = Math.min(xy[3], xy[3]);
		float maxY = Math.max(xy[1], xy[3]);
		
		for (int i = 4; i < xy.length; i += 2) {
			s.addTriangle(0, 0, xy[i - 2] - xy[0], xy[i - 1] - xy[1], xy[i] - xy[0], xy[i + 1] - xy[1], xy[0], xy[1]);
			minX = Math.min(minX, xy[i]);
			maxX = Math.max(maxX, xy[i]);
			minY = Math.min(minY, xy[i + 1]);
			maxY = Math.max(maxY, xy[i + 1]);
		}
		
		s.width = maxX - minX;
		s.height = maxY - minY;
		
		finalizeShape(s, xy[0], xy[1]);
		return s;
	}
	
	public Shape createPolygon(float[] xy, float[] uv) {
		Shape s = new Shape(this, brush.clone(), xy.length - 2);
		
		float minX = Math.min(xy[0], xy[2]);
		float maxX = Math.max(xy[0], xy[2]);
		float minY = Math.min(xy[3], xy[3]);
		float maxY = Math.max(xy[1], xy[3]);
		
		for (int i = 4; i < xy.length; i += 2) {
			s.addTriangle(0, 0, uv[0], uv[1],
					xy[i - 2] - xy[0], xy[i - 1] - xy[1], uv[i - 2], uv[i - 1],
					xy[i] - xy[0], xy[i + 1] - xy[1], uv[i], uv[i + 1]);
			minX = Math.min(minX, xy[i]);
			maxX = Math.max(maxX, xy[i]);
			minY = Math.min(minY, xy[i + 1]);
			maxY = Math.max(maxY, xy[i + 1]);
		}
		
		s.width = maxX - minX;
		s.height = maxY - minY;
		
		finalizeShape(s, xy[0], xy[1]);
		return s;
	}
	
	/**
	 * Creates a shape of multiple convex polygons. Coordinates are used absolutely, origin of the created shape will
	 * be 0,0.
	 * @param xy array of arrays of polygon coordinates in format [x0,y0,x1,y1,...],...
	 * @param uv array of arrays of uv coordinates in format [u0,v0,u1,v1,...],...
	 * @return shape
	 */
	public Shape createPolygons(float[][] xy, float[][] uv) {
		int count = 0;
		for (int i = 0; i < xy.length; i++) {
			count += xy[i].length - 2;
		}
		
		Shape s = new Shape(this, brush.clone(), count);
		
		float minX = Float.MAX_VALUE;
		float maxX = -Float.MAX_VALUE;
		float minY = Float.MAX_VALUE;
		float maxY = -Float.MAX_VALUE;
		
		for (int i = 0; i < xy.length; i++) {
			for (int j = 4; j < xy[i].length; j += 2) {
				s.addTriangle(xy[i][0], xy[i][1], uv[i][0], uv[i][1],
						xy[i][j - 2], xy[i][j - 1], uv[i][j - 2], uv[i][j - 1],
						xy[i][j], xy[i][j + 1], uv[i][j], uv[i][j + 1]);
			}
			for (int j = 0; j < xy[i].length; j += 2) {
				minX = Math.min(minX, xy[i][j]);
				maxX = Math.max(maxX, xy[i][j]);
				minY = Math.min(minY, xy[i][j + 1]);
				maxY = Math.max(maxY, xy[i][j + 1]);
			}
		}
		
		s.width = maxX - minX;
		s.height = maxY - minY;
		
		finalizeShape(s, 0, 0);
		return s;
	}
	
	public ImageShape createImage(ImageDescription img, float x, float y) {
		return createImage(img, x, y, 0);
	}
	
	public ImageShape createImage(ImageDescription img, float x, float y, int frame) {
		return createImage(img, x, y, img.getWidth(frame), img.getHeight(frame), frame);
	}
	
	/**
	 * Creates an image shape from the frame of an image description that will fill the given rectangle.
	 * @param img image to use
	 * @param x position
	 * @param y position
	 * @param w width
	 * @param h height
	 * @param frame within the image description to use
	 * @return image shape
	 */
	public ImageShape createImage(ImageDescription img, float x, float y, float w, float h, int frame) {
		Brush imageBrush = brush.clone();
		imageBrush.setTexture(img.getTexture());
		
		ImageShape s = new ImageShape(this, imageBrush, 2, img, frame);
		
		float width = w;
		float height = h;
		float[] uv = img.getUVCoords(frame);
		
		s.addTriangle(0, 0, uv[0], uv[1], 0, height, uv[0], uv[3], width, 0, uv[2], uv[1]);
		s.addTriangle(width, 0, uv[2], uv[1], 0, height, uv[0], uv[3], width, height, uv[2], uv[3]);
		
		s.width = width;
		s.height = height;
		
		finalizeShape(s, x, y);
		return s;
	}
	
	/**
	 * Creates a line of text shape starting at a specific position.
	 * @param font that should be used
	 * @param text that should be used
	 * @param x position
	 * @param y position
	 * @return text shape
	 */
	public TextShape createTextLine(FontDescription font, String text, float x, float y) {
		Brush textBrush = brush.clone();
		textBrush.setTexture(font.getTexture());
		
		TextShape s = new TextShape(this, textBrush, 2 * text.length(), font, text);
		
		float px = 0;
		float py = 0;
		float width = 0;
		float height = 0;
		
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			float[] uv = font.getUVCoords(c);
			
			if (uv != null) {
				float gw = font.getGlyphWidth(c);
				float gh = font.getGlyphHeight(c);
				
				s.addTriangle(px, py, uv[0], uv[1], px, py + gh, uv[0], uv[3], px + gw, py, uv[2], uv[1]);
				s.addTriangle(px + gw, py, uv[2], uv[1], px, py + gh, uv[0], uv[3], px + gw, py + gh, uv[2], uv[3]);
				
				px += gw;
				px += font.getHorizontalSpacing();
			}
		}
		
		s.width = width;
		s.height = height;
		
		finalizeShape(s, x, y);
		return s;
	}
	
	private void finalizeShape(Shape s, float x, float y) {
		Object3D obj = s.getObject();
		
		obj.addParent(master);
		world.addObject(obj);
		obj.build(false);
		obj.setLighting(Object3D.LIGHTING_NO_LIGHTS);
		
		s.applyBrush();
		s.setPivot(0, 0);
		s.move(x + moveX, y + moveY);
		
		s.scale(scaleX, scaleY);
		s.turn(angle);
		
		shapeList.add(s);
	}
	
	void finalizeShape(Shape s) {
		Object3D obj = s.getObject();
		
		obj.addParent(master);
		world.addObject(obj);
		obj.build();
		obj.setLighting(Object3D.LIGHTING_NO_LIGHTS);
		
		obj.setCulling(false);
		
		s.applyBrush();
		
		shapeList.add(s);
	}
	
	SimpleVector getVertex(float x, float y) {
		float px = ((x - virtX) * (buffer.getWidth() / virtW));
		float py = ((y - virtY) * (buffer.getHeight() / virtH));
		
		float fac = Z_DISTANCE * camera.getFOV() / buffer.getWidth();
		return new SimpleVector(
				(px - buffer.getWidth() / 2) * fac,
				(py - buffer.getHeight() / 2) * fac,
				Z_DISTANCE);
	}
	
	void moveObject(Object3D obj, float x, float y) {
		float px = (x - virtX) * (buffer.getWidth() / virtW);
		float py = (y - virtY) * (buffer.getHeight() / virtH);
		
		float fac = Z_DISTANCE * camera.getFOV() / buffer.getWidth();
		obj.translate(px * fac, py * fac, 0);
	}
	
	void removeShape(Shape s) {
		world.removeObject(s.obj);
		s.obj = null;
		
		shapeList.remove(s);
	}
	
	float xToAbsolute(float x) {
		return x * (virtW / buffer.getWidth()) + virtX;
	}
	
	float yToAbsolute(float y) {
		return y * (virtH / buffer.getHeight()) + virtY;
	}
	
	float widthToAbsolute(float w) {
		return w * (virtW / buffer.getWidth());
	}
	
	float heightToAbsolute(float h) {
		return h * (virtH / buffer.getHeight());
	}
	
	/**
	 * Moves and turns all shapes (in fact only the master of them) in front of the current camera. Call this method
	 * whenever you move or rotate your cam and want the shapes to behold their position on screen.
	 * Control the master object by yourself if you want to make special effects (like earthquakes).
	 */
	public void updateToCameraView() {
		master.clearTranslation();
		master.translate(camera.getPosition());
		
		master.setRotationMatrix(camera.getBack().transpose());
	}
	
	/**
	 * Removes all shapes created by this factory. You can use the factory after calling this method. If you want to
	 * remove a specific shape you have to call it's dispose() method.
	 */
	public void clear() {
		for (Shape s:shapeList) {
			removeShape(s);
		}
	}
	
	/**
	 * Removes all shapes and objects created by this factory. The factory shall not be used after a call of this
	 * method. In general you won't have to call it.
	 */
	public void dispose() {
		clear();
		
		world.removeObject(master);
		master = null;
	}
}
