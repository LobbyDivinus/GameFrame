package info.flowersoft.gameframe;


import info.flowersoft.gameframe.touch.TouchMapper;
import info.flowersoft.gameframe.touch.TouchPoint;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.threed.jpct.FrameBuffer;
import com.threed.jpct.Texture;
import com.threed.jpct.TextureManager;
import com.threed.jpct.World;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Bundle;
import android.os.SystemClock;

/**
 * Extend this class to write a game or something else. Let your AppActivity extending class create a object of your
 * concrete AppRenderer in it's getRenderer() method.
 * 
 * @author Lobby
 */
public abstract class AppRenderer implements Renderer {

	/**
	 * App context from the AppActivity.
	 */
	private Context context;
	
	/**
	 * FrameBuffer to render objects. Will be erased with a new one on resize. May be null to indicate that no
	 * FrameBuffer has been created yet.
	 */
	private FrameBuffer buffer;
	
	/**
	 * jPCT World object that is managed by the AppRenderer.
	 */
	private World world;
	
	/**
	 * Time when last updated.
	 */
	private long lastUpdate;
	
	/**
	 * Wrapper class for touch point handling.
	 */
	private TouchMapper mapper;
	
	/**
	 * Current touch points.
	 */
	private List<TouchPoint> touchpoints;
	
	/**
	 * Latest added touch point, or null if not available.
	 */
	private TouchPoint newPoint;
	
	/**
	 * Latest removed touch point, or null if not available.
	 */
	private TouchPoint removedPoint;
	
	/**
	 * Current content's width in pixels.
	 */
	private int width;
	
	/**
	 * Current content's height in pixels.
	 */
	private int height;
	
	/**
	 * Current aspect ration, calculated by width / height.
	 */
	private float ratio;
	
	/**
	 * Current frames per second, also called framerate.
	 */
	private float fps;
	
	private List<Touchable> touchables;
	
	/**
	 * Override this constructor to create new instances.
	 * @param savedInstance
	 * @param con
	 */
	public AppRenderer(Bundle savedInstance, Context con) {
		context = con;
		world = new World();
		mapper = new TouchMapper();
		touchables = new ArrayList<Touchable>();
	}
	
	public void setContext(Context con) {
		context = con;
	}
	
	public Context getContext() {
		return context;
	}
	
	protected World getWorld() {
		return world;
	}
	
	protected FrameBuffer getBuffer() {
		return buffer;
	}
	
	public TouchMapper getTouchMapper() {
		return mapper;
	}
	
	protected List<TouchPoint> getActiveTouchPoints() {
		return touchpoints;
	}
	
	protected TouchPoint getNewTouchPoint() {
		return newPoint;
	}
	
	protected TouchPoint getRemovedTouchPoint() {
		return removedPoint;
	}
	
	/**
	 * Returns width of frame buffer in pixels.
	 * @return width
	 */
	protected int getWidth() {
		return width;
	}
	
	/**
	 * Returns height of frame buffer in pixels.
	 * @return height
	 */
	protected int getHeight() {
		return height;
	}
	
	/**
	 * Returns screen ratio of the frame buffer (width / height).
	 * @return ratio
	 */
	protected float getRatio() {
		return ratio;
	}
	
	/**
	 * Returns current fps of the app.
	 * @return fps
	 */
	protected float getFPS() {
		return fps;
	}
	
	/**
	 * Loads and adds a texture for you. The texture is loaded without alpha channel.
	 * @param name to use for the texture to add it to texture manager
	 * @param id resource id to load the texture from
	 * @return the loaded texture
	 */
	protected Texture loadTexture(String name, int id) {
		return loadTexture(name, id, false);
	}
	
	/**
	 * Loads and adds a texture for you.
	 * @param name to use for the texture to add it to texture manager
	 * @param id resource id to load the texture from
	 * @param alpha true if you want to use a alpha channel in your texture, otherwise false
	 * @return the loaded texture
	 */
	protected Texture loadTexture(String name, int id, boolean alpha) {
		Texture tex = new Texture(context.getResources().openRawResource(id), alpha);
		TextureManager.getInstance().addTexture(name, tex);
		return tex;
	}
	
	/**
	 * Loads a raw bitmap for you.
	 * @param id resource id to load the bitmap from
	 * @return the loaded bitmap
	 */
	protected Bitmap loadBitmap(int id) {
		return BitmapFactory.decodeStream(context.getResources().openRawResource(id));
	}
	
	/**
	 * Registers a touchable object. After that it will automatically receive all touch point events.
	 * @param t touchable to add
	 */
	protected void registerTouchable(Touchable t) {
		touchables.add(0, t);
	}
	
	/**
	 * Removes a touchable object.
	 * @param t touchable to remove
	 * @return true if removing was successful
	 */
	protected boolean removeTouchable(Touchable t) {
		return touchables.remove(t);
	}
	
	/**
	 * Creation method with garantee on existence of a frame buffer. Will be called before first call of
	 * onResolutionChange. Will only be called one time.
	 * @param width buffer width in pixels
	 * @param height buffer height in pixels
	 */
	protected abstract void onCreate(int width, int height);
	
	/**
	 * Always called when resolution changes (at start onCreate() will be called instead).
	 * @param width buffer width in pixels
	 * @param height buffer height in pixels
	 */
	protected abstract void onResolutionChange(int width, int height);
	
	/**
	 * Update method will be called every frame and should be used for non drawing only.
	 * @param time time since last call in seconds (useful for updates)
	 */
	protected abstract void update(double time);
	
	/**
	 * Called before world will be rendered into frame buffer. Should contain at least getBuffer().clear().
	 */
	protected abstract void beforeRendering();
	
	/**
	 * Called after world has been rendered and frame buffer has been displayed. You can do blitting here.
	 */
	protected abstract void afterRendering();
	
	/**
	 * Called when app goes into pause state.
	 */
	public abstract void onPause();
	
	/**
	 * Called when app resumes from pause state.
	 */
	public abstract void onResume();
	
	@Override
	public void onDrawFrame(GL10 gl) {
		long timeMS = SystemClock.uptimeMillis();
		double time = (timeMS - lastUpdate) / 1000.0;
		if (lastUpdate == 0) {
			time = 0;
		}
		if (time > 1) {
			time = 0;
		}
		lastUpdate = timeMS;
		
		if (time > 0) {
			fps = 0.9f * fps + 0.1f / (float) time;
		}
		
		synchronized (mapper) {
			touchpoints = new ArrayList<TouchPoint>(mapper.getActivePoints());
			newPoint = mapper.getNewPoint();
			removedPoint = mapper.getRemovedPoint();
		}
		
		for (Touchable t:touchables) {
			if (t.update(newPoint, removedPoint)) {
				newPoint = null;
			}
		}
		
		update(time);
		
		beforeRendering();
		
		world.renderScene(buffer);
		world.draw(buffer);
		
		afterRendering();
		
		buffer.display();
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int x, int y) {
		boolean firstRun = (buffer == null);
		
		if (buffer != null) {
			buffer.dispose();
		}
		
		buffer = new FrameBuffer(x, y);
		
		width = x;
		height = y;
		ratio = x / (float) y;
		
		if (firstRun) {
			onCreate(x, y);
		} else {
			onResolutionChange(x, y);
		}
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
	}

}
