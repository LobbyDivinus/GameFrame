package info.flowersoft.gameframe.shape;

import com.threed.jpct.PolygonManager;
import com.threed.jpct.TextureInfo;

import info.flowersoft.gameframe.description.Brush;
import info.flowersoft.gameframe.description.ImageDescription;

/**
 * Shapes that represent a single image. You can change the frame afterwards.
 * 
 * @author Lobby Divinus
 */
public class ImageShape extends Shape {

	ImageDescription image;
	
	int frame;
	
	/**
	 * Creates a new image shape.
	 */
	ImageShape() {
	}
	
	/**
	 * Creates a new image shape.
	 * @param fac factory
	 * @param b brush
	 * @param maxTriangles maximum of triangles, should be 2
	 * @param img image description
	 * @param frameIdx frame index
	 */
	ImageShape(ShapeFactory fac, Brush b, int maxTriangles, ImageDescription img, int frameIdx) {
		super(fac, b, maxTriangles);
		
		image = img;
		frame = frameIdx;
	}

	/**
	 * Gets the image description used for this ImageShape. You can not change it (yet).
	 * @return image description
	 */
	public ImageDescription getImage() {
		return image;
	}
	
	/**
	 * Changes the current shown frame of the image description. Will not resize the shape so you may have to do this
	 * manually. Try to change the frame as seldom as possible.
	 * @param frame new frame to show
	 */
	public void setFrame(int frame) {
		if (this.frame != frame) {
			this.frame = frame;
			
			float[] uv = image.getUVCoords(frame);
			
			PolygonManager mgr = obj.getPolygonManager();
			mgr.setPolygonTexture(0,
					new TextureInfo(mgr.getPolygonTexture(0), uv[0], uv[1], uv[0], uv[3], uv[2], uv[1]));
			mgr.setPolygonTexture(1,
					new TextureInfo(mgr.getPolygonTexture(1), uv[2], uv[1], uv[0], uv[3], uv[2], uv[3]));
			
			obj.touch();
		}
	}
	
	/**
	 * Returns the index of the currently used frame of the image description.
	 * @return frame index
	 */
	public int getFrame() {
		return frame;
	}
	
	@Override
	public ImageShape clone() {
		ImageShape s = new ImageShape();
		
		copyAttributesTo(s);
		
		s.image = image;
		s.frame = frame;
		
		factory.finalizeShape(s);
		
		return s;
	}
}
