package info.flowersoft.gamestack;

import android.content.Context;

import com.threed.jpct.FrameBuffer;
import com.threed.jpct.Texture;
import com.threed.jpct.TextureManager;
import com.threed.jpct.World;

/**
 * Simple game context for GameStages using jPCT. Stores world, framebuffer, app context, display size and fps.
 * @author Lobby
 *
 */
public class JPCTGameContext {

	/**
	 * jPCT world object.
	 */
	private World world;
	
	/**
	 * FrameBuffer object for rendering. May be null and can change over time.
	 */
	private FrameBuffer fb;
	
	/**
	 * Current app context. Used for resource loading for instance.
	 */
	private Context context;
	
	/**
	 * Current display width.
	 */
	private int width;
	
	/**
	 * Current display height.
	 */
	private int height;
	
	/**
	 * Time when last rendered.
	 */
	private long lastRender;
	
	/**
	 * Time between last rendering and pre last rendering. Will also be used for FPS calculation.
	 */
	private float deltaTime;
	
	/**
	 * Constructor to create new JPCTGameContext. See examples for how to use such objects. Feel free to use it for
	 * other purposes.
	 */
	public JPCTGameContext() {
		world = new World();
	}
	
	/**
	 * Apply a new app context. Make sure you have always a valid context set.
	 * @param context New context to apply.
	 */
	public void setContext(Context context) {
		this.context = context;
	}
	
	/**
	 * Returns the current app context. Used for resource loading for instance.
	 * @return Current app context object.
	 */
	public Context getContext() {
		return context;
	}
	
	/**
	 * Loads a texture from the given resource id resId and applies a name to it.
	 * @param resId Resource id to load the texture from. Has to be a width and height of 2^x.
	 * @param name Name to set for the new texture. You will use this name for further texture access.
	 * @param useAlpha If true, also alpha information will be loaded and may be used.
	 * @return Texture object for the loaded texture.
	 */
	public Texture loadTexture(int resId, String name, boolean useAlpha) {
		Texture tex = new Texture(context.getResources().openRawResource(resId), useAlpha);
		TextureManager.getInstance().addTexture(name, tex);
		return tex;
	}
	
	/**
	 * Current display width. Zero if onSizeChanged has never been called before.
	 * @return Display width in pixels.
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * Current display height. Zero if onSizeChanged has never been called before.
	 * @return Display height in pixels.
	 */
	public int getHeight() {
		return height;
	}
	
	/**
	 * Call this method on resolution changes. You have also to call it at least once before rendering.
	 * @param w New display width in pixels.
	 * @param h New display height in pixels.
	 */
	public void onSizeChange(int w, int h) {
		width = w;
		height = h;
		
		if (fb != null) {
			fb.dispose();
		}
		
		fb = new FrameBuffer(w, h);
	}
	
	/**
	 * Render the whole scene. The internal world object will be used for rendering.
	 */
	public void render() {
		if (fb == null) {
			throw new IllegalStateException("You have to call onSizeChange in order to be able to render.");
		}
		
		fb.clear();
		world.renderScene(fb);
		world.draw(fb);
		fb.display();
		
		long ms = System.currentTimeMillis();
		deltaTime = (ms - lastRender) / 1000.0f;
		if (deltaTime > 0) {
			deltaTime = 0;
		}
		lastRender = ms;
	}
	
	/**
	 * Gives you the time between last rendering and the rendering before.
	 * @return Delta time between two last renderings or 0 instead. Delta time in seconds.
	 */
	public float getDeltaTime() {
		return deltaTime;
	}
	
	/**
	 * Calculates the framerate frames per second based on delta time for rendering.
	 * @return Framerate in frames per second. May be #INF.
	 */
	public float getFPS() {
		return 1 / deltaTime;
	}
}
