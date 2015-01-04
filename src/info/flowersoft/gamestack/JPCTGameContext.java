package info.flowersoft.gamestack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import info.flowersoft.gameframe.key.KeyMapper;
import info.flowersoft.gameframe.touch.TouchMapper;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.threed.jpct.FrameBuffer;
import com.threed.jpct.GLSLShader;
import com.threed.jpct.RGBColor;
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
	 * Min deltaTime that will be allowed.
	 */
	public static final float MIN_DELTA_TIME = 0.01f;
	
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
	 * Touch mapper for touch input handling. Doesn't have to be used.
	 */
	private TouchMapper touch;
	
	/**
	 * Key mapper for key input handing. You don't have to use it.
	 */
	private KeyMapper key;
	
	/**
	 * Constructor to create new JPCTGameContext. See examples for how to use such objects. Feel free to use it for
	 * other purposes.
	 */
	public JPCTGameContext() {
		touch = new TouchMapper();
		key = new KeyMapper();
		
		deltaTime = MIN_DELTA_TIME;
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
	 * Returns the touch mapper of this context. Isn't specific for jPCT but useful.
	 * @return Touch mapper object.
	 */
	public TouchMapper getTouch() {
		return touch;
	}
	
	/**
	 * Returns the key mapper of this context. Isn't specific for jPCT but useful.
	 * @return Key mapper object.
	 */
	public KeyMapper getKey() {
		return key;
	}
	
	/**
	 * Returns the internal frame buffer for rendering. May be null and may change over time.
	 * @return Current frame buffer.
	 */
	public FrameBuffer getFrameBuffer() {
		return fb;
	}
	
	/**
	 * Loads a texture from the given resource id resId and applies a name to it.
	 * @param resId Resource id to load the texture from. Has to be a width and height of 2^x.
	 * @param name Name to set for the new texture. You will use this name for further texture access.
	 * @param useAlpha If true, also alpha information will be loaded and may be used.
	 * @return Texture object for the loaded texture.
	 */
	public Texture loadTexture(int resId, String name, boolean useAlpha) {
		TextureManager mgr = TextureManager.getInstance();
		Texture tex;
		if (mgr.containsTexture(name)) {
			tex = mgr.getTexture(name);
		} else {
			tex = new Texture(context.getResources().openRawResource(resId), useAlpha);
			mgr.addTexture(name, tex);
		}
		return tex;
	}
	
	/**
	 * Tries to load a bitmap from the given resource id.
	 * @param resId Resource id of the bitmap to laod.
	 * @return The loaded bitmap object.
	 */
	public Bitmap loadBitmap(int resId) {
		return BitmapFactory.decodeStream(context.getResources().openRawResource(resId));
	}
	
	/**
	 * Reads in a text file and returns it as a string. Lines are separated with \n.
	 * @param id Resource id of the text file
	 * @return String with the content of the text file
	 */
	public String loadTextFile(int id) {
		InputStream stream = context.getResources().openRawResource(id);
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		StringBuilder b = new StringBuilder(1024);
		try {
			String line = reader.readLine();
			while (line != null) {
				b.append(line);
				b.append('\n');
				line = reader.readLine();
			}
		} catch (IOException e) {
			b.append("!!! Error while reading string from text file: " + e.toString() + " !!!");
		}
		return b.toString();
	}
	
	/**
	 * Loads a shader from the given vertex shader and fragment shader source specified by their file ids.
	 * @param vertexShaderId Resource id of the vertex shader source.
	 * @param fragmentShaderId Resource id of the fragment shader source.
	 * @return The loaded shader of null if the shader could not be loaded.
	 */
	public GLSLShader loadShader(int vertexShaderId, int fragmentShaderId) {
		return new GLSLShader(loadTextFile(vertexShaderId), loadTextFile(fragmentShaderId));
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
	 * Renders the whole scene. The given world object will be used for rendering.
	 * @param bgColor Color for the background.
	 * @param world World object to render.
	 */
	public void render(RGBColor bgColor, World world) {
		renderWithoutDisplay(bgColor, world);
		fb.display();
	}
	
	/**
	 * Renders the whole scene. The given world object will be used for rendering. Won't display the framebuffer.
	 * @param bgColor Color for the background.
	 * @param world World object to render.
	 */
	public void renderWithoutDisplay(RGBColor bgColor, World world) {
		if (fb == null) {
			throw new IllegalStateException("You have to call onSizeChange in order to be able to render.");
		}
		
		fb.clear(bgColor);
		world.renderScene(fb);
		world.draw(fb);
		
		
		long ms = System.currentTimeMillis();
		deltaTime = (ms - lastRender) / 1000.0f;
		if (deltaTime > 1 || deltaTime < MIN_DELTA_TIME) {
			deltaTime = MIN_DELTA_TIME;
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
