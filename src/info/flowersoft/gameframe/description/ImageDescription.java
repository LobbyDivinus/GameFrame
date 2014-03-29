package info.flowersoft.gameframe.description;

import java.util.ArrayList;
import java.util.List;

import com.threed.jpct.Texture;
import com.threed.jpct.TextureManager;

/**
 * An image description is a set of rectangles within one texture. Different rectangles are adresses by an index called
 * frame.
 * 
 * @author Lobby Divinus
 */
public class ImageDescription {

	/**
	 * Texture name of the texture the image description is based on.
	 */
	private String texture;
	
	/**
	 * Texture object the image description is based on.
	 */
	private Texture textureObj;
	
	/**
	 * True if alpha should be used for the image description.
	 */
	private boolean alpha;
	
	/**
	 * List of all frames in the image description.
	 */
	private List<Frame> frameList;
	
	/**
	 * Frame within the image description.
	 * 
	 * @author Lobby Divinus
	 */
	private class Frame {
		private float actualWidth;
		private float actualHeight;
		private float[] uv;
	}
	
	/**
	 * Creates a new image description for a texture. Also creates a frame containing the whole texture if singleFrame
	 * is set to true.
	 * @param tex texture the image description should base on, the texture has to be loaded yet
	 * @param hasAlpha set this to true if your texture contains alpha you want to use
	 * @param singleFrame if true a frame for the whole texture will be created (for convenience)
	 */
	public ImageDescription(String tex, boolean hasAlpha, boolean singleFrame) {
		texture = tex;
		alpha = hasAlpha;
		
		textureObj = TextureManager.getInstance().getTexture(tex);
		
		frameList = new ArrayList<Frame>();
		
		if (singleFrame) {
			addFrame(0, 0, textureObj.getWidth(), textureObj.getHeight());
		}
	}
	
	/**
	 * Creates a new image description for a texture. Also adds a frame with the given rectangle x, y, w, h in pixels.
	 * @param tex texture the image description should base on, the texture has to be loaded yet
	 * @param x coordinate of the frame
	 * @param y coordinate of the frame
	 * @param w width of the frame
	 * @param h height of the frame
	 * @param hasAlpha set this to true if your texture contains alpha you want to use
	 */
	public ImageDescription(String tex, float x, float y, float w, float h, boolean hasAlpha) {
		this(tex, hasAlpha, false);
		
		addFrame(x, y, w, h);
	}
	
	/**
	 * Creates a new image description for a texture. Also adds a strip defined by frame width and height over the
	 * whole texture with at maximum frameCount frames. Start index will be 0.
	 * @param tex texture the image description should base on, the texture has to be loaded yet
	 * @param frameWidth width of a single frame in the strip
	 * @param frameHeight height of a single frame in the strip
	 * @param frameCount maximum number of frames in the strip
	 * @param hasAlpha set this to true if your texture contains alpha you want to use
	 */
	public ImageDescription(String tex, float frameWidth, float frameHeight, int frameCount, boolean hasAlpha) {
		this(tex, hasAlpha, false);
		
		addStrip(frameWidth, frameHeight, frameCount);
	}
	
	/**
	 * Creates a new image description for a texture. Also adds all frames of a given image description. UV coords are
	 * copied instead of absolute pixel values so texture size doesn't matter. Frame indices will be the same.
	 * @param tex texture the image description should base on, the texture has to be loaded yet
	 * @param des image description to copy frames from
	 * @param hasAlpha set this to true if your texture contains alpha you want to use
	 */
	public ImageDescription(String tex, ImageDescription des, boolean hasAlpha) {
		this(tex, hasAlpha, false);
		
		for (int i = 0; i < des.frameList.size(); i++) {
			Frame other = des.frameList.get(i);
			Frame frame = new Frame();
			
			frame.actualWidth = other.actualWidth;
			frame.actualHeight = other.actualHeight;
			frame.uv = other.uv.clone();
			
			frameList.add(frame);
		}
	}
	
	/**
	 * Adds a frame to the image description with the given rectangle x, y, w, h in pixels.
	 * @param x coordinate of the frame
	 * @param y coordinate of the frame
	 * @param w height of the frame
	 * @param h height of the frame
	 * @return index of the frame
	 */
	public int addFrame(float x, float y, float w, float h) {
		Frame frame = new Frame();
		frame.actualWidth = w;
		frame.actualHeight = h;
		
		float facX = 1f / textureObj.getWidth();
		float facY = 1f / textureObj.getHeight();
		
		float[] uv = new float[4];
		uv[0] = facX * x;
		uv[1] = facY * y;
		uv[2] = facX * (x + w);
		uv[3] = facY * (y + h);
		
		frame.uv = uv;
		
		frameList.add(frame);
		
		return frameList.size() - 1;
	}
	
	/**
	 * Adds as most frames of a specific size as possible within the whole texture to the image description.
	 * @param frameWidth single frame width
	 * @param frameHeight single frame height
	 * @return frame index of the first added frame even if no frames have been added, so compare it with countFrames()
	 */
	public int addStrip(float frameWidth, float frameHeight) {
		return addStrip(frameWidth, frameHeight, Integer.MAX_VALUE);
	}
	
	/**
	 * Adds count frames of a specific size within the whole texture to the image description.
	 * @param frameWidth single frame width
	 * @param frameHeight single frame height
	 * @param count number of frames that should be added at the maximum, less frames could be added
	 * @return frame index of the first added frame even if no frames have been added, so compare it with countFrames()
	 */
	public int addStrip(float frameWidth, float frameHeight, int count) {
		return addStrip(0f, 0f, textureObj.getWidth(), textureObj.getHeight(), frameWidth, frameHeight, count);
	}
	
	/**
	 * Adds count frames of a specific size within a specific rectangle area to the image description.
	 * @param x coordinate for the area
	 * @param y coordinate for the area
	 * @param w width for the area
	 * @param h height for the area
	 * @param frameWidth single frame width
	 * @param frameHeight single frame height
	 * @param count number of frames that should be added at the maximum, less frames could be added
	 * @return frame index of the first added frame even if no frames have been added, so compare it with countFrames()
	 */
	public int addStrip(float x, float y, float w, float h, float frameWidth, float frameHeight, int count) {
		int frameIndex = frameList.size();
		
		int idx = 0;
		for (float py = y; py + frameHeight <= y + h && idx < count; py += frameHeight) {
			for (float px = x; px + frameWidth <= x + w && idx < count; px += frameWidth) {
				addFrame(px, py, frameWidth, frameHeight);
				idx++;
			}
		}
		
		return frameIndex;
	}
	
	/**
	 * Returns texture name the image description is based on.
	 * @return texture name
	 */
	public String getTexture() {
		return texture;
	}
	
	/**
	 * Returns texture object the image description is based on. This method should be faster than getting it from
	 * texture name manually.
	 * @return texture object
	 */
	public Texture getTextureObj() {
		return textureObj;
	}
	
	/**
	 * Sets another size for a specific frame. By default frame size is the same as the size of the rectangle within
	 * the texture.
	 * @param width of the frame
	 * @param height of the frame
	 * @param frame index of the frame to modify
	 */
	public void setSize(float width, float height, int frame) {
		Frame f = frameList.get(frame);
		f.actualWidth = width;
		f.actualHeight = height;
	}
	
	/**
	 * Returns width of a specific frame. Can be different from actual frame rectangle width.
	 * @param frame index of the frame
	 * @return width of the frame
	 */
	public float getWidth(int frame) {
		return frameList.get(frame).actualWidth;
	}
	
	/**
	 * Returns height of a specific frame. Can be different from actual frame rectangle height.
	 * @param frame index of the frame
	 * @return height of the frame
	 */
	public float getHeight(int frame) {
		return frameList.get(frame).actualHeight;
	}
	
	/**
	 * Determines whether alpha should be used for the image description
	 * @return true if alpha should be used, otherwise false
	 */
	public boolean hasAlpha() {
		return alpha;
	}
	
	/**
	 * Gets the total number of frames within the image description. Can be 0 if no frames are defined at all.
	 * @return number of frames
	 */
	public int countFrames() {
		return frameList.size();
	}
	
	/**
	 * Gets the uv coords for a specific frame. The uv coords are an array of four entries, where uv[0], uv[1] is the
	 * left upper corner and uv[2], uv[3] is the right lower corner.
	 * @param frame index of the frame
	 * @return uv coords of the frame
	 */
	public float[] getUVCoords(int frame) {
		return frameList.get(frame).uv;
	}
}
