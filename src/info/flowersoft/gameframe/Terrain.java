package info.flowersoft.gameframe;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.threed.jpct.GLSLShader;
import com.threed.jpct.GenericVertexController;
import com.threed.jpct.Mesh;
import com.threed.jpct.Object3D;
import com.threed.jpct.PolygonManager;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.TextureInfo;
import com.threed.jpct.TextureManager;

/**
 * Subclass of Object3D to create and manage terrains in jpct-ae. Only rectangle terrains are supported (and therefore
 * no caves etc.).</br>
 * 
 * There are two coordinate systems used within methods of this class:</br>
 * <u>quad or vertex system</u>, here quads are adressed directly while the left vertex in negative z direction is 0|0
 * </br>
 * <u>terrain absolute system</u>, here the center of the terrain is 0|0 and intern scaling is considered</br>
 * If not specified you can assume that absolute system is used (such methods are normally prefixed with centered). You
 * will probably need the last one more often.
 * 
 * @author Lobby Divinus
 */
public class Terrain extends Object3D {

	private static final long serialVersionUID = 7329791217357939144L;
	
	private float[][] heightData;
	private int width;
	private int depth;
	
	private float size;
	private float height;
	
	/**
	 * A height extractor uses a bitmap to compute a height value for a x|y position.
	 * 
	 * @author Lobby Divinus
	 */
	public interface IBitmapHeightExtractor {
		/**
		 * At the beginning the extractor will get the bitmap to work on with this method.
		 * @param bitmap The bitmap to extract heights from.
		 */
		void init(Bitmap bitmap);
		
		/**
		 * Extract a height value for the given pixel position.
		 * @param x X pixel coordinate.
		 * @param y Y pixel coordinate.
		 * @return A height value, should be in [0..1].
		 */
		float extract(int x, int y);
		
	}
	
	/**
	 * Bitmap extractor that returns height values in [0..1], based on grey values in the bitmap.
	 * 
	 * @author Lobby Divinus
	 */
	public class BitmapGreyHeightExtractor implements IBitmapHeightExtractor {

		private Bitmap bitmap;
		
		@Override
		public void init(Bitmap bitmap) {
			this.bitmap = bitmap;
		}

		@Override
		public float extract(int x, int y) {
			int col = bitmap.getPixel(x, y);
			float grey = (Color.red(col) + Color.green(col) + Color.blue(col)) / 3f;
			return grey / 255f;
		}

	}
	
	/**
	 * Creates a terrain with given number of width and depth quads.
	 * @param width number of quads in width
	 * @param depth number of quads in depth
	 */
	public Terrain(int width, int depth) {
		this(width, depth, new float[width + 1][depth + 1], 1f, 1f);
	}
	
	/**
	 * Creates a terrain from grey values of a bitmap. Pay attention on that the actual number of quads in width and
	 * depth is 1 smaller than the width and height of the bitmap.
	 * @param bitmap to generate terrain from
	 */
	public Terrain(Bitmap bitmap) {
		this(bitmap, 1f, 1f);
	}
	
	/**
	 * Creates a terrain from grey values of a bitmap. Pay attention on that the actual number of quads in width and
	 * depth is 1 smaller than the width and height of the bitmap.
	 * @param bitmap to generate terrain from
	 * @param size size of a single quad
	 * @param height factor of height scale (default is 1)
	 */
	public Terrain(Bitmap bitmap, float size, float height) {
		super(2 * bitmap.getWidth() * bitmap.getHeight());
		
		IBitmapHeightExtractor extractor = new BitmapGreyHeightExtractor();
		
		this.size = size;
		this.height = height;
		
		width = bitmap.getWidth();
		depth = bitmap.getHeight();
		heightData = new float[width][depth];
		
		extractor.init(bitmap);
		for (int x = 0; x < width; x++) {
			for (int z = 0; z < depth; z++) {
				heightData[x][z] = extractor.extract(x, z);
			}
		}
		
		buildMesh();
	}
	
	/**
	 * Creates a terrain from values of a bitmap. Pay attention on that the actual number of quads in width and
	 * depth is 1 smaller than the width and height of the bitmap (if you don't get it, just ignore).
	 * @param bitmap to generate terrain from
	 * @param size size of a single quad
	 * @param height factor of height scale (default is 1)
	 * @param extractor to get height data from bitmap
	 */
	public Terrain(Bitmap bitmap, float size, float height, IBitmapHeightExtractor extractor) {
		super(2 * bitmap.getWidth() * bitmap.getHeight());
		
		this.size = size;
		this.height = height;
		
		width = bitmap.getWidth();
		depth = bitmap.getHeight();
		heightData = new float[width][depth];
		
		extractor.init(bitmap);
		for (int x = 0; x < width; x++) {
			for (int z = 0; z < depth; z++) {
				heightData[x][z] = extractor.extract(x, z);
			}
		}
		
		buildMesh();
	}
	
	/**
	 * Creates a terrain with given number of width and depth quads.
	 * @param width number of quads in width
	 * @param depth number of quads in depth
	 * @param heights array that contains height value for each vertex, must have size [width + 1][depth + 1]
	 * @param size size of a single quad
	 * @param height factor of height scale (default is 1)
	 */
	public Terrain(int width, int depth, float[][] heights, float size, float height) {
		super(2 * width * depth);
		this.heightData = heights;
		this.width = width + 1;
		this.depth = depth + 1;
		
		this.size = size;
		this.height = height;
		
		buildMesh();
	}
	
	/**
	 * Apply new height data which are stored as grey values in a bitmap. Bitmap must have size edge size + 1.
	 * @param bitmap image that contains height values
	 */
	public void setHeightData(Bitmap bitmap) {
		setHeightData(bitmap, new BitmapGreyHeightExtractor());
	}
	
	/**
	 * Apply new height data which are stored in a bitmap. Bitmap must have size edge size + 1.
	 * @param bitmap image that contains height value
	 * @param extractor extractor to get height values from bitmap
	 */
	public void setHeightData(Bitmap bitmap, IBitmapHeightExtractor extractor) {
		extractor.init(bitmap);
		for (int x = 0; x < width; x++) {
			for (int z = 0; z < depth; z++) {
				heightData[x][z] = extractor.extract(x, z);
			}
		}
		applyHeightData();
	}
	
	/**
	 * Apply new height data which are store in a float array array. The array array must have edge size + 1.
	 * @param heights new height data
	 */
	public void setHeightData(float[][] heights) {
		this.heightData = heights;
		applyHeightData();
	}
	
	/**
	 * Returns number of quads in width.
	 * @return number of quads
	 */
	public int getEdgeWidth() {
		return width - 1;
	}
	
	/**
	 * Returns number of quads in depth
	 * @return number of quads
	 */
	public int getEdgeDepth() {
		return depth - 1;
	}
	
	/**
	 * Returns height scale (not including influence from model martix).
	 * @return height
	 */
	public float getHeight() {
		return height;
	}
	
	/**
	 * Returns quad size
	 * @return quad size of a single quad
	 */
	public float getQuadSize() {
		return size;
	}
	
	/**
	 * Returns smoothed y height at position x|z.
	 * @param x coordinate
	 * @param z coordinate
	 * @param smooth use 1 as default, higher values mean more smoothing
	 * @return smoothed height
	 */
	public float getCenteredSmoothedY(float x, float z, float smooth) {
		return getSmoothedY(x / size - 0.5f + width / 2f, z / size - 0.5f + depth / 2f, smooth);
	}
	
	/**
	 * Returns smoothed normal vector at position x|z.
	 * @param x coordinate
	 * @param z coordinate
	 * @param smooth use 1 as default, higher values mean more smoothing
	 * @return smoothed normal vector
	 */
	public SimpleVector getCenteredSmoothedNormal(float x, float z, float smooth) {
		return getSmoothedNormal(x / size - 0.5f + width / 2f, z / size - 0.5f + depth / 2f, smooth);
	}
	
	/**
	 * Returns y height at position x|z.
	 * @param x coordinate
	 * @param z coordinate
	 * @return height
	 */
	public float getCenteredY(float x, float z) {
		return getY(x / size - 0.5f + width / 2f, z / size - 0.5f + depth / 2f);
	}
	
	/**
	 * Retruns normal vector at position x|z.
	 * @param x coordinate
	 * @param z coordinate
	 * @return normal vector
	 */
	public SimpleVector getCenteredNormal(float x, float z) {
		return getNormal(x / size - 0.5f + width / 2f, z / size - 0.5f + depth / 2f);
	}
	
	/**
	 * Returns smoothed y height at position x|z in quad coordinate system.
	 * @param x quad coordinate
	 * @param z quad coordinate
	 * @param smooth use 1 as default, higher values mean more smoothing
	 * @return smoothed height
	 */
	public float getSmoothedY(float x, float z, float smooth) {
		if (x < 0 || z < 0 || x > width - 1 || z > depth - 1) {
			return 0f;
		}
		
		float result = 0f;
		result += getY(x - smooth, z);
		result += getY(x + smooth, z);
		result += getY(x, z + smooth);
		result += getY(x, z - smooth);
		result /= 4;
		
		result += getY(x, z);
		result /= 2;
		
		return result;
	}
	
	/**
	 * Returns smoothed normal vector at position x|z in quad coordinate system.
	 * @param x quad coordinate
	 * @param z quad coordinate
	 * @param smooth use 1 as default, higher values mean more smoothing
	 * @return smoothed normal vector
	 */
	public SimpleVector getSmoothedNormal(float x, float z, float smooth) {
		if (x < 0 || z < 0 || x > width - 1 || z > depth - 1) {
			return new SimpleVector(0f, -1f, 0f);
		}
		
		SimpleVector result = new SimpleVector();
		
		float height = getY(x,z);
		SimpleVector v0 = new SimpleVector(smooth, getY(x + smooth, z) - height, 0);
		SimpleVector v1 = new SimpleVector(0, getY(x, z + smooth) - height, smooth);
		SimpleVector v2 = new SimpleVector(- smooth, getY(x - smooth, z) - height, 0);
		SimpleVector v3 = new SimpleVector(0, getY(x, z - smooth) - height, - smooth);
		v0 = v0.calcCross(v1);
		v2 = v2.calcCross(v3);
		
		result.add(v0);
		result.add(v2);
		result.scalarMul(0.5f);
		
		return result;
	}
	
	/**
	 * Returns smoothed normal vector within quad at position x|z in quad coordinate system.
	 * @param x quad coordinate
	 * @param z quad coordinate
	 * @return smoothed normal vector
	 */
	public SimpleVector getSmoothedQuadNormal(float x, float z) {
		if (x < 0 || z < 0 || x > width - 1 || z > depth - 1) {
			return new SimpleVector(0f, -1f, 0f);
		}
		
		int ix = (int) x;
		int iz = (int) z;
		float mx = x % 1;
		float mz = z % 1;
		
		SimpleVector normal1;
		SimpleVector normal2;

		normal1 = getNormal(ix, iz);
		normal1.scalarMul((1 - mx) + (1 - mz));
		
		normal2 = getNormal(ix + 0.8f, iz + 0.8f);
		normal2.scalarMul(mx + mz);
		
		normal1.add(normal2);
		
		return normal1;
	}
	
	/**
	 * Determines the height of a x, z position in quad coordinate system.
	 * @param x coordinate
	 * @param z coordinate
	 * @return height of x, z or 0 if x, z is out of terrain
	 */
	public float getY(float x, float z) {
		if (x < 0 || z < 0 || x > width - 1 || z > depth - 1) {
			return 0f;
		}
		
		float mx = x % 1;
		float mz = z % 1;
		
		if (Math.min(mx, 1 - mx) < 0.001f && Math.min(mz,  1 - mz) < 0.001f) {
			return getPointY((int) (x + 0.5f), (int) (z + 0.5f));
		}
		
		int lX;
		int lZ;
		int o1X;
		int o1Z;
		int o2X;
		int o2Z;
		
		if (mx + mz < 1) {
			lX = (int) x;
			lZ = (int) z;
			o1X = 0;
			o1Z = 1;
			o2X = 1;
			o2Z = 0;
		} else {
			lX = (int) x + 1;
			lZ = (int) z + 1;
			o1X = 0;
			o1Z = - 1;
			o2X = - 1;
			o2Z = 0;
		}
		
		float lH = - height * heightData[lX][depth - lZ - 1];
		float o1H = - height * heightData[lX + o1X][depth - lZ - o1Z - 1];
		float o2H = - height * heightData[lX + o2X][depth - lZ - o2Z - 1];
		
		SimpleVector o1V = SimpleVector.create(size * o1X, o1H - lH, size * o1Z);
		SimpleVector o2V = SimpleVector.create(size * o2X, o2H - lH, size * o2Z);
		o1V.scalarMul((1 - o1Z) / 2 + o1Z * (mz));
		o2V.scalarMul((1 - o2X) / 2 + o2X * (mx));
		return lH + o1V.y + o2V.y;
	}
	
	/**
	 * Determines height of a vertex at given position in quad coordinate system. If position is out of the terrain
	 * 0 will be returned.
	 * @param x coordinate
	 * @param z coordinate
	 * @return absolute height value
	 */
	public float getPointY(int x, int z) {
		if (x < 0 || z < 0 || x > width - 1 || z > depth - 1) {
			return 0f;
		}
		
		return this.height * (- this.heightData[x][depth - z - 1]);
	}
	
	/**
	 * Sets height for the vertex at the given position in quad coordinate system. If position is out of the terrain
	 * the call will be ignored.
	 * @param x coordinate in quad coordiante system
	 * @param h new height value, by convention this is in 0..1
	 * @param z coordinate in quad coordinate system
	 */
	public void setPointHeight(int x, float h, int z) {
		if (x < 0 || z < 0 || x > width - 1 || z > depth - 1) {
			return;
		}
		
		heightData[x][depth - z - 1] = h;
		applyHeightData();
	}
	
	/**
	 * Calculates normal vector for given x, z position in quad coordinate system. If position is out of the terrain
	 * null will be returned.
	 * @param x coordinate in quad coordinate system
	 * @param z coordinate in quad coordinate system
	 * @return normal vector
	 */
	public SimpleVector getNormal(float x, float z) {
		if (x < 0 || z < 0 || x > width - 1 || z > depth - 1) {
			return null;
		}
		
		int lX;
		int lZ;
		int o1X;
		int o1Z;
		int o2X;
		int o2Z;
		
		if (x % 1 + z % 1 <= 1) {
			lX = (int) x;
			lZ = (int) z;
			o1X = 0;
			o1Z = 1;
			o2X = 1;
			o2Z = 0;
		} else {
			lX = (int) x + 1;
			lZ = (int) z + 1;
			o1X = 0;
			o1Z = - 1;
			o2X = - 1;
			o2Z = 0;
		}
		
		float lH = -height * heightData[lX][lZ];
		float o1H = -height * heightData[lX + o1X][lZ + o1Z];
		float o2H = -height * heightData[lX + o2X][lZ + o2Z];
		
		SimpleVector o1V = SimpleVector.create(size * o1X, o1H - lH, size * o1Z);
		SimpleVector o2V = SimpleVector.create(size * o2X, o2H - lH, size * o2Z);
		return o2V.calcCross(o1V);
	}
	
	private void buildMesh() {
		// Prepare vertices
		SimpleVector[][] v = new SimpleVector[width][depth];
		for (int x = 0; x < width; x++) {
			for (int z = 0; z < depth; z++) {
				v[x][z] = new SimpleVector(size * (
						x + 0.5f - width / 2f),
						-height * heightData[x][depth - z - 1],
						size * (z + 0.5f - depth / 2f));
			}
		}
		
		
		// Create quads
		float uC = 0f;
		float vC = 1f;
		float uStep = 1f / (width - 1);
		float vStep = -1f / (depth - 1);
		for (int x = 0; x + 1 < width; x++) {
			vC = 1f;
			for (int z = 0; z + 1 < depth; z++) {
				addTriangle(v[x][z + 1], uC, vC + vStep, v[x][z], uC, vC, v[x + 1][z], uC + uStep, vC);
				addTriangle(
						v[x + 1][z],
						uC + uStep,
						vC,
						v[x + 1][z + 1],
						uC + uStep,
						vC + vStep,
						v[x][z + 1],
						uC,
						vC + vStep);
				
				
				// Use other diagonal for quads (made calculations more complex :/ )
				//addTriangle(v[x][z + 1], uC, vC + vStep, v[x][z], uC, vC, v[x + 1][z + 1], uC + uStep, vC + vStep);
				//addTriangle(v[x + 1][z + 1], uC + uStep, vC + vStep, v[x][z], uC, vC, v[x + 1][z], uC + uStep, vC);
				
				vC += vStep;
			}
			uC += uStep;
		}
	}
	
	private void applyHeightData() {
		Mesh mesh = getMesh();
		
		mesh.setVertexController(new GenericVertexController() {
			private static final long serialVersionUID = 1L;
			@Override
			public void apply() {
				SimpleVector[] source = getSourceMesh();
				SimpleVector[] dest = getDestinationMesh();
				for (int i = 0; i < source.length; i++) {
					int x = (int) Math.floor(source[i].x / size + width / 2.0);
					int z = (int) Math.floor(source[i].z / size + depth / 2.0);
					dest[i].y = -height * heightData[x][depth - z - 1];
				}
			}
		}, false);
		
		mesh.applyVertexController();
		mesh.removeVertexController();
	}
	
	/**
	 * Applies multiple textures on the terrain. A base texture, given with mask, will be used to select which of the
	 * other (up to three) textures should be shown at a specific position. For stage 1 color channel red will be used
	 * and so on.
	 * The base texture is stretched over the whole terrain. Other textures can be scaled using xScale and yScale.
	 * 
	 * In order to use more than two texture in total on the terrain you should modify Config.maxTextureLayers to a
	 * value of 3 or more (is 2 by default).
	 * 
	 * To use this method you should also apply a specific shader. You can use the default one by calling
	 * applayTerrainShader().
	 * 
	 * @param mask name of the ground texture to use
	 * @param maps other texture names
	 * @param modes blending modes, use TextureInfo.MODE_ADD to gain good results
	 * @param xScale x scale for textures
	 * @param yScale y scale for textures
	 */
	public void setTerrainTextures(String mask, String[] maps, int[] modes, float[] xScale, float[] yScale) {
		TextureManager mgr = TextureManager.getInstance();
		int[] texIds = new int[1 + maps.length];
		TextureInfo info = new TextureInfo(texIds[0]);
		texIds[0] = mgr.getTextureID(mask);
		for (int i = 0; i < maps.length; i++) {
			texIds[i + 1] = mgr.getTextureID(maps[i]);
			info.add(texIds[i + 1], modes[i]);
		}
		
		PolygonManager pmgr = getPolygonManager();
		for (int poly = 0; poly < getMesh().getTriangleCount(); poly++) {
			SimpleVector v0 = pmgr.getTextureUV(poly, 0);
			SimpleVector v1 = pmgr.getTextureUV(poly, 1);
			SimpleVector v2 = pmgr.getTextureUV(poly, 2);
			info.set(texIds[0], 0, v0.x, v0.y, v1.x, v1.y, v2.x, v2.y, TextureInfo.MODE_REPLACE);
			for (int i = 0; i < maps.length; i++) {
				info.set(texIds[i + 1], i + 1,
						v0.x * xScale[i], v0.y * yScale[i],
						v1.x * xScale[i], v1.y * xScale[i],
						v2.x * xScale[i], v2.y * xScale[i],
						modes[i]);
			}
			pmgr.setPolygonTexture(poly, info);
		}
	}
	
	/**
	 * Applies terrain texture shader on the mesh so that it can use up to 4 textures for multitexturing. The texture
	 * at stage 0 defines where the other textures should be drawn (red = state 1 etc.).
	 */
	public void applyTerrainShader() {
		
		setShader(terrainShader);
	}
	
	private static final String TERRAIN_VERTEX_SHADER = "uniform mat4 modelViewMatrix;" +
			"uniform mat4 modelViewProjectionMatrix;" +
			"uniform mat4 textureMatrix;" +
			"uniform vec4 additionalColor;" +
			"uniform vec4 ambientColor;" +
			"uniform float alpha;" +
			"uniform float shininess;" +
			"uniform bool useColors;" +
			"uniform float fogStart;" +
			"uniform float fogEnd;" +
			"uniform vec3 fogColor;" +
			"uniform int lightCount;" +
			"uniform vec3 lightPositions[8];" +
			"uniform vec3 diffuseColors[8];" +
			"uniform vec3 specularColors[8];" +
			"uniform float attenuation[8];" +
			"attribute vec4 position;" +
			"attribute vec3 normal;" +
			"attribute vec4 color;" +
			"attribute vec2 texture0;" +
			"attribute vec2 texture1;" +
			"attribute vec2 texture2;" +
			"attribute vec2 texture3;" +
			"varying vec2 texCoord[4];" +
			"varying vec4 vertexColor;" +
			"varying vec3 fogVertexColor;" +
			"varying float fogWeight;" +
			"const vec4 WHITE = vec4(1,1,1,1);" +
			"void main() {" +
			"	texCoord[0] = (textureMatrix * vec4(texture0, 0, 1)).xy;" +	
			"	texCoord[1] = texture1;" +
			"	texCoord[2] = texture2;" +
			"	texCoord[3] = texture3;" +
			"	vec4 vertexPos = modelViewMatrix * position;" +
			"	vertexColor = ambientColor + additionalColor;" +
			"	if (lightCount>0) {" +
			"		vec3 normalEye   = normalize(modelViewMatrix * vec4(normal, 0.0)).xyz;" +
			"		float angle = dot(normalEye, normalize(lightPositions[0] - vertexPos.xyz));" +
			"		if (angle > 0.0) {" +
			"			vertexColor += vec4((diffuseColors[0] * angle + specularColors[0] * pow(angle, shininess))*(1.0/(1.0+length(lightPositions[0] - vertexPos.xyz)*attenuation[0])), 1);" +
			"		}" +
			"		if (lightCount>1) {" +
			"			angle = dot(normalEye, normalize(lightPositions[1] - vertexPos.xyz));" +
			"			if (angle > 0.0) {" +
			"				vertexColor += vec4((diffuseColors[1] * angle + specularColors[1] * pow(angle, shininess))*(1.0/(1.0+length(lightPositions[1] - vertexPos.xyz)*attenuation[1])), 1);" +
			"			}" +			
			"			if (lightCount>2) {" +
			"				angle = dot(normalEye, normalize(lightPositions[2] - vertexPos.xyz));" +
			"				if (angle > 0.0) {" +
			"					vertexColor += vec4((diffuseColors[2] * angle + specularColors[2] * pow(angle, shininess))*(1.0/(1.0+length(lightPositions[2] - vertexPos.xyz)*attenuation[2])), 1);" +
			"				}" +
			"				if (lightCount>3) {" +
			"					angle = dot(normalEye, normalize(lightPositions[3] - vertexPos.xyz));" +
			"					if (angle > 0.0) {" +
			"						vertexColor += vec4((diffuseColors[3] * angle + specularColors[3] * pow(angle, shininess))*(1.0/(1.0+length(lightPositions[3] - vertexPos.xyz)*attenuation[3])), 1);" +
			"					}" +
			"					if (lightCount>4) {" +
			"						angle = dot(normalEye, normalize(lightPositions[4] - vertexPos.xyz));" +
			"						if (angle > 0.0) {" +
			"							vertexColor += vec4((diffuseColors[4] * angle + specularColors[4] * pow(angle, shininess))*(1.0/(1.0+length(lightPositions[4] - vertexPos.xyz)*attenuation[4])), 1);" +
			"						}" +
			"						if (lightCount>5) {" +
			"							angle = dot(normalEye, normalize(lightPositions[5] - vertexPos.xyz));" +
			"							if (angle > 0.0) {" +
			"								vertexColor += vec4((diffuseColors[5] * angle + specularColors[5] * pow(angle, shininess))*(1.0/(1.0+length(lightPositions[5] - vertexPos.xyz)*attenuation[5])), 1);" +
			"							}" +
			"							if (lightCount>6) {" +
			"								angle = dot(normalEye, normalize(lightPositions[6] - vertexPos.xyz));" +
			"								if (angle > 0.0) {" +
			"									vertexColor += vec4((diffuseColors[6] * angle + specularColors[6] * pow(angle, shininess))*(1.0/(1.0+length(lightPositions[6] - vertexPos.xyz)*attenuation[6])), 1);" +
			"								}" +
			"								if (lightCount>7) {" +
			"									angle = dot(normalEye, normalize(lightPositions[7] - vertexPos.xyz));" +
			"									if (angle > 0.0) {" +
			"										vertexColor += vec4((diffuseColors[7] * angle + specularColors[7] * pow(angle, shininess))*(1.0/(1.0+length(lightPositions[7] - vertexPos.xyz)*attenuation[7])), 1);" +
			"									}" +
			"								}" +
			"							}" +
			"						}" +
			"					}" +
			"				}" +
			"			}" +
			"		}" +
			"	}" +
			"	if (fogStart != -1.0) {" +
			"		fogWeight = clamp((-vertexPos.z - fogStart) / (fogEnd - fogStart), 0.0, 1.0);" +
			"		fogVertexColor = fogColor * fogWeight;" +
			"	} else {" +
			"		fogWeight = -1.0;" +
			"	}" +
			"	vertexColor=vec4(min(WHITE, vertexColor).xyz, alpha);" +
			"	if (useColors) {" +
			"		vertexColor *= color;" +
			"	}" +
			"	gl_Position = modelViewProjectionMatrix * position;" +
			"}";
	
	private final static String TERRAIN_FRAGMENT_SHADER = "precision mediump float;" +
			"uniform sampler2D textureUnit0;" +
			"uniform sampler2D textureUnit1;" +
			"uniform sampler2D textureUnit2;" +
			"uniform sampler2D textureUnit3;" +
			"uniform int textureCount;" +
			"uniform int blendingMode[4];" +
			"varying vec2 texCoord[4];" +
			"varying vec4 vertexColor;" +
			"varying float fogWeight;" +
			"varying vec3 fogVertexColor;" +
			"const vec4 WHITE = vec4(1,1,1,1);" +
			"void main() {" +
			"	vec4 mask = texture2D(textureUnit0, texCoord[0]) * vertexColor;" +
			"	vec4 col = vec4(0,0,0,0);" +
			"	if (textureCount>1) {" +
			"		if (blendingMode[1]==0) {" +
			"			col *= mask.x * texture2D(textureUnit1, texCoord[1]);" +
			"		} else if (blendingMode[1]==1) {" +
			"			col += mask.x * texture2D(textureUnit1, texCoord[1]);" +
			"		} else if (blendingMode[1]==3) {" +
			"			col *= mask.x * (WHITE - texture2D(textureUnit1, texCoord[1]));" +
			"		} else if (blendingMode[1]==2) {" +
			"			col = mask.x * texture2D(textureUnit1, texCoord[1]);" +
			"		}" +
			"		if (textureCount>2) {" +
			"			if (blendingMode[2]==0) {" +
			"				col *= mask.y * texture2D(textureUnit2, texCoord[2]);" +
			"			} else if (blendingMode[2]==1) {" +
			"				col += mask.y * texture2D(textureUnit2, texCoord[2]);" +
			"			} else if (blendingMode[2]==3) {" +
			"				col *= mask.y * (WHITE - texture2D(textureUnit2, texCoord[2]));" +
			"			} else if (blendingMode[2]==2) {" +
			"				col = mask.y * texture2D(textureUnit2, texCoord[2]);" +
			"			}" +
			"			if (textureCount>3) {" +
			"				if (blendingMode[3]==0) {" +
			"					col *= mask.z * texture2D(textureUnit3, texCoord[3]);" +
			"				} else if (blendingMode[3]==1) {" +
			"					col += mask.z * texture2D(textureUnit3, texCoord[3]);" +
			"				} else if (blendingMode[3]==3) {" +
			"					col *= mask.z * (WHITE - texture2D(textureUnit3, texCoord[3]));" +
			"				} else if (blendingMode[3]==2) {" +
			"					col = mask.z * texture2D(textureUnit3, texCoord[3]);" +
			"				}" +
			"			}" +
			"		}" +
			"	}" +
			"	if (fogWeight>-0.9) {" +
			"		col.xyz = (1.0-fogWeight) * col.xyz + fogVertexColor;" +
			"	}" +
			"	gl_FragColor=col;" +
			"}";
	
	private static GLSLShader terrainShader = new GLSLShader(TERRAIN_VERTEX_SHADER, TERRAIN_FRAGMENT_SHADER);
}
