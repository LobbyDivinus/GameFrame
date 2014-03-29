package info.flowersoft.gameframe.description;

import com.threed.jpct.Matrix;
import com.threed.jpct.Object3D;
import com.threed.jpct.RGBColor;

/**
 * A brush is a description for different properties of an Object3D instance. It contains information about texture,
 * texture matrix, additive color, transparency and the transparency mode.
 * 
 * @author Fabian Miltenberger
 */
public class Brush implements Cloneable {
	
	/**
	 * A red brush.
	 */
	public final static Brush RED = new Brush(255, 0, 0);
	
	/**
	 * A green brush.
	 */
	public final static Brush GREEN = new Brush(0, 255, 0);
	
	/**
	 * A blue brush.
	 */
	public final static Brush BLUE = new Brush(0, 0, 255);
	
	/**
	 * A white brush.
	 */
	public final static Brush WHITE = new Brush(255, 255, 255);
	
	/**
	 * A black brush.
	 */
	public final static Brush BLACK = new Brush(0, 0, 0);
	
	/**
	 * A grey brush.
	 */
	public final static Brush GREY = new Brush(127, 127, 127);
	
	/**
	 * A yellow brush.
	 */
	public final static Brush YELLOW = new Brush(255, 255, 0);
	
	/**
	 * An orange brush.
	 */
	public final static Brush ORANGE = new Brush(255, 127, 0);

	/**
	 * A cyan brush.
	 */
	public final static Brush CYAN = new Brush(0, 255, 255);
	
	private String texture;
	
	private Matrix textureMatrix;
	
	private RGBColor addColor;
	
	private int transparency;
	
	private int transparencyMode;
	
	/**
	 * Creates a new brush based on properties of an existing one.
	 * @param b existing brush from which properties should be applied
	 */
	public Brush(Brush b) {
		texture = b.texture;
		textureMatrix = b.textureMatrix;
		addColor = new RGBColor(b.addColor.getRed(), b.addColor.getGreen(), b.addColor.getBlue());
		transparency = b.transparency;
		transparencyMode = b.transparencyMode;
	}
	
	/**
	 * Creates a brush that just contains a texture.
	 * @param tex texture to use
	 */
	public Brush(String tex) {
		this(tex, 255, 255, 255);
	}
	
	/**
	 * Creates a brush that has just one color.
	 * @param r red
	 * @param g green
	 * @param b blue
	 */
	public Brush(int r, int g, int b) {
		this(null, r, g, b);
	}
	
	/**
	 * Creates a brush with a texture and a color
	 * @param tex texture
	 * @param r red
	 * @param g green
	 * @param b blue
	 */
	public Brush(String tex, int r, int g, int b) {
		this(tex, new RGBColor(r, g, b));
	}
	
	/**
	 * Creates a brush with a texture and a color
	 * @param tex texture
	 * @param color color
	 */
	public Brush(String tex, RGBColor color) {
		texture = tex;
		addColor = color;
		transparency = 15;
		transparencyMode = Object3D.TRANSPARENCY_MODE_DEFAULT;
	}
	
	/**
	 * Returns texture of the brush or null if not texture has been set
	 * @return texture name of null
	 */
	public String getTexture() {
		return texture;
	}
	
	/**
	 * Sets a new texture for the brush
	 * @param tex texture
	 */
	public void setTexture(String tex) {
		texture = tex;
	}
	
	/**
	 * Returns current color of the brush
	 * @return color
	 */
	public RGBColor getColor() {
		return addColor;
	}
	
	/**
	 * Sets a new color for the brush
	 * @param r red
	 * @param g green
	 * @param b blue
	 */
	public void setColor(int r, int g, int b) {
		setColor(new RGBColor(r, g, b));
	}
	
	/**
	 * Sets a new color for the brush
	 * @param color color
	 */
	public void setColor(RGBColor color) {
		addColor = color;
	}
	
	/**
	 * Returns current texture matrix of the brush or null if no texture matrix has been set
	 * @return texture matrix or null
	 */
	public Matrix getTextureMatrix() {
		return textureMatrix;
	}
	
	/**
	 * Sets a new texture matrix for the brush
	 * @param mat texture matrix
	 */
	public void setTextureMatrix(Matrix mat) {
		textureMatrix = mat;
	}
	
	/**
	 * Returns transparency of the brush
	 * @return transparency
	 */
	public int getTransparency() {
		return transparency;
	}
	
	/**
	 * Sets a new transpareny for the brush, use values in 0..15 or -1 to use no alpha at all
	 * @param trans transparency value
	 */
	public void setTransparency(int trans) {
		transparency = trans;
	}
	
	/**
	 * Returns current transparency mode of the brush
	 * @return transparency mode
	 */
	public int getTranspacencyMode() {
		return transparencyMode;
	}
	
	/**
	 * Sets a new transparency mode for the brush. Should be Object3D.TRANSPARENCY_MODE_DEFAULT or
	 * Object3D.TRANSPARENCY_MODE_ADDITIVE.
	 * @param mode transparency mode
	 */
	public void setTransparencyMode(int mode) {
		transparencyMode = mode;
	}
	
	/**
	 * Applies properties of the brush to an Object3D instance. You have to do this whenever you changed something in
	 * the brush and want it to take effect.
	 * @param obj Object3D instance
	 */
	public void apply(Object3D obj) {
		if (texture != null) {
			obj.setTexture(texture);
		}
		
		obj.setTextureMatrix(textureMatrix);
		
		obj.setAdditionalColor(addColor);
		
		obj.setTransparency(transparency);
		obj.setTransparencyMode(transparencyMode);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Brush clone() {
		return new Brush(this);
	}
}
