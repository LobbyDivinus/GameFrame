package info.flowersoft.gameframe;

import com.threed.jpct.GenericVertexController;
import com.threed.jpct.IVertexController;
import com.threed.jpct.Object3D;
import com.threed.jpct.PolygonManager;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.TextureInfo;
import com.threed.jpct.TextureManager;

/**
 * This class provides some methods to create basic 3d objects that can be used within a jPCT world. All objects have
 * usable uv-coordinates and methods for default values (especially to create objects that fit into a 1*1*1 cube).
 * After creation don't forget to add the object to your world, otherwise you won't see it.
 * @author Lobby Divinus
 * @version 1.0
 */
public final class ExtendedPrimitives {

	private ExtendedPrimitives() {
	}
	
	/**
	 * A pivot object is just another name for a dummy object that can be used to position objects by setting
	 * the pivot as parent. A pivot will never be drawn and so you also shouldn't add it to a World.
	 * @return the pivot object
	 */
	public static Object3D createPivot() {
		return Object3D.createDummyObj();
	}
	
	/**
	 * Creates a cube with an edge length of 1.
	 * @return a cube object
	 */
	public static Object3D createCube() {
		return createCube(1f);
	}
	
	/**
	 * Creates a cube with a given edge length.
	 * @param size length of each edge
	 * @return a cube object
	 */
	public static Object3D createCube(float size) {
		return createBox(new SimpleVector(size, size, size));
	}
	
	/**
	 * Creates a cuboid with width, height and depth given as components of a SimpleVector.
	 * @param size width, height and depth for the cubiod that should be created
	 * @return the created cuboid
	 */
	public static Object3D createBox(SimpleVector size) {
		size.scalarMul(0.5f);
		
		float[] v = new float[6 * 4 * 3];
		float[] normals = new float[6 * 4 * 3];
		int c = 0;
		int d = 0;
		
		// Front face
		v[c + 0] = -size.x; v[c + 1] = -size.y; v[c + 2] = -size.z; c += 3;
		v[c + 0] = size.x; v[c + 1] = -size.y; v[c + 2] = -size.z; c += 3;
		v[c + 0] = size.x; v[c + 1] = size.y; v[c + 2] = -size.z; c += 3;
		v[c + 0] = -size.x; v[c + 1] = size.y; v[c + 2] = -size.z; c += 3;
		for (int i = 0; i < 4; i++) {
			normals[d + 0] = 0f; normals[d + 1] = 0f; normals[d + 2] = -1f; d += 3;
		}
		
		// Right face
		v[c + 0] = size.x; v[c + 1] = -size.y; v[c + 2] = -size.z; c += 3;
		v[c + 0] = size.x; v[c + 1] = -size.y; v[c + 2] = size.z; c += 3;
		v[c + 0] = size.x; v[c + 1] = size.y; v[c + 2] = size.z; c += 3;
		v[c + 0] = size.x; v[c + 1] = size.y; v[c + 2] = -size.z; c += 3;
		for (int i = 0; i < 4; i++) {
			normals[d + 0] = 1f; normals[d + 1] = 0f; normals[d + 2] = 0f; d += 3;
		}
		
		// Back face
		v[c + 0] = size.x; v[c + 1] = -size.y; v[c + 2] = size.z; c += 3;
		v[c + 0] = -size.x; v[c + 1] = -size.y; v[c + 2] = size.z; c += 3;
		v[c + 0] = -size.x; v[c + 1] = size.y; v[c + 2] = size.z; c += 3;
		v[c + 0] = size.x; v[c + 1] = size.y; v[c + 2] = size.z; c += 3;
		for (int i = 0; i < 4; i++) {
			normals[d + 0] = 0f; normals[d + 1] = 0f; normals[d + 2] = 1f; d += 3;
		}
		
		// Left face
		v[c + 0] = -size.x; v[c + 1] = -size.y; v[c + 2] = size.z; c += 3;
		v[c + 0] = -size.x; v[c + 1] = -size.y; v[c + 2] = -size.z; c += 3;
		v[c + 0] = -size.x; v[c + 1] = size.y; v[c + 2] = -size.z; c += 3;
		v[c + 0] = -size.x; v[c + 1] = size.y; v[c + 2] = size.z; c += 3;
		for (int i = 0; i < 4; i++) {
			normals[d + 0] = -1f; normals[d + 1] = 0f; normals[d + 2] = 0f; d += 3;
		}
		
		// Top face
		v[c + 0] = -size.x; v[c + 1] = -size.y; v[c + 2] = size.z; c += 3;
		v[c + 0] = size.x; v[c + 1] = -size.y; v[c + 2] = size.z; c += 3;
		v[c + 0] = size.x; v[c + 1] = -size.y; v[c + 2] = -size.z; c += 3;
		v[c + 0] = -size.x; v[c + 1] = -size.y; v[c + 2] = -size.z; c += 3;
		for (int i = 0; i < 4; i++) {
			normals[d + 0] = 0f; normals[d + 1] = -1f; normals[d + 2] = 0f; d += 3;
		}
		
		// Bottom face
		v[c + 0] = -size.x; v[c + 1] = size.y; v[c + 2] = -size.z; c += 3;
		v[c + 0] = size.x; v[c + 1] = size.y; v[c + 2] = -size.z; c += 3;
		v[c + 0] = size.x; v[c + 1] = size.y; v[c + 2] = size.z; c += 3;
		v[c + 0] = -size.x; v[c + 1] = size.y; v[c + 2] = size.z; c += 3;
		for (int i = 0; i < 4; i++) {
			normals[d + 0] = 0f; normals[d + 1] = 1f; normals[d + 2] = 0f; d += 3;
		}
		
		float[] uvs = new float[6 * 4 * 2];
		int[] indices = new int[6 * 2 * 3];
		
		for (int i = 0; i < 6; i++) {
			uvs[i * 4 * 2 + 0] = 0f;
			uvs[i * 4 * 2 + 1] = 0f;
			uvs[i * 4 * 2 + 2] = 1f;
			uvs[i * 4 * 2 + 3] = 0f;
			uvs[i * 4 * 2 + 4] = 1f;
			uvs[i * 4 * 2 + 5] = 1f;
			uvs[i * 4 * 2 + 6] = 0f;
			uvs[i * 4 * 2 + 7] = 1f;
			
			indices[i * 2 * 3 + 0] = i * 4 + 3;
			indices[i * 2 * 3 + 1] = i * 4 + 1;
			indices[i * 2 * 3 + 2] = i * 4 + 0;
			indices[i * 2 * 3 + 3] = i * 4 + 3;
			indices[i * 2 * 3 + 4] = i * 4 + 2;
			indices[i * 2 * 3 + 5] = i * 4 + 1;
		}
		
		return new Object3D(v, uvs, indices, TextureManager.TEXTURE_NOTFOUND);
	}
	
	/**
	 * Creates a cylinder which would perfectly fit into a 1*1*1 cube. It points towards the  Y-Axis.
	 * @param quads The number of quads that should be used for the cylinder. Don't use more quads than you need!
	 * @return the created cylinder
	 */
	public static Object3D createCylinder(int quads) {
		return createCylinder(0.5f, 1f, quads, true);
	}
	
	/**
	 * Creates a cylinder with specific radius and height. It points towards the  Y-Axis.
	 * @param radius of the cylinder
	 * @param height of the cylinder along Y-Axis
	 * @param quads The number of quads that should be used for the cylinder. Don't use more quads than you need!
	 * @param caps if false the cylinder won't have caps an the ends
	 * @return the created cylinder
	 */
	public static Object3D createCylinder(float radius, float height, int quads, boolean caps) {
		int triangles = 2 * quads;
		if (caps) {
			triangles += 2 * quads - 4;
		}
		Object3D obj = new Object3D(triangles);
		
		SimpleVector[] topV = new SimpleVector[quads];
		SimpleVector[] bottomV = new SimpleVector[quads];
		
		// Prepare vertices
		float halfHeight = height / 2;
		float angle = 0;
		float angleStep = (float) (2 * Math.PI / quads);
		float x;
		float y;
		for (int i = 0; i < quads; i++) {
			x = (float) Math.cos(angle);
			y = (float) Math.sin(angle);
			
			topV[i] = new SimpleVector(radius * x, -halfHeight, radius * y);
			bottomV[i] = new SimpleVector(radius * x, halfHeight, radius * y);
			
			angle += angleStep;
		}
		
		// Add caps if wanted
		if (caps) {
			angle = angleStep + angleStep;
			float u0 = 0.5f + 0.5f * (float) Math.cos(0);
			float v0 = 0.5f - 0.5f * (float) Math.sin(0);
			float u1 = 0.5f + 0.5f * (float) Math.cos(angleStep);
			float v1 = 0.5f - 0.5f * (float) Math.sin(angleStep);
			float u2 = 0.5f + 0.5f * (float) Math.cos(angle);
			float v2 = 0.5f - 0.5f * (float) Math.sin(angle);
			for (int i = 1; i + 1 < quads; i++) {
				u2 = 0.5f + 0.5f * (float) Math.cos(angle);
				v2 = 0.5f - 0.5f * (float) Math.sin(angle);
				
				obj.addTriangle(topV[0], u0, v0, topV[i], u1, v1, topV[i + 1], u2, v2);
				obj.addTriangle(bottomV[0], 1f - u0, v0, bottomV[i + 1], 1f - u2, v2, bottomV[i], 1f - u1, v1);
				
				u1 = u2;
				v1 = v2;
				
				angle += angleStep;
			}
		}
		
		// Add round faces
		float u = 0;
		float uStep = 2f / quads;
		for (int i = 0; i < quads; i++) {
			obj.addTriangle(topV[i], u, 0f, bottomV[i], u, 1f, topV[(i + 1) % quads], u + uStep, 0f);
			obj.addTriangle(
					topV[(i + 1) % quads], u + uStep, 0f, bottomV[i], u, 1f, bottomV[(i + 1) % quads], u + uStep, 1f);
			
			u += uStep;
		}
		
		return obj;
	}
	
	/**
	 * Creates a cone which would perfeclty fit into a 1*1*1 cube. The top points towards the negative Y-Axis.
	 * @param faces Number of faces that should be used. Don't use more faces than you need!
	 * @return the created cone
	 */
	public static Object3D createCone(int faces) {
		return createCone(0.5f, 1f, faces, true);
	}
	
	/**
	 * Creates a cone which would perfectly fit into a 1*1*1 cube. The top points towards the negative Y-Axis.
	 * @param radius of the cone
	 * @param height of the cone along the Y-Axis
	 * @param faces Number of faces that should be used. Don't use more faces than you need!
	 * @param cap If false the cone won't have a ground.
	 * @return the created cone
	 */
	public static Object3D createCone(float radius, float height, int faces, boolean cap) {
		int triangles = faces;
		if (cap) {
			triangles += faces - 2;
		}
		Object3D obj = new Object3D(triangles);
		
		float halfHeight = height / 2;
		
		SimpleVector topV = new SimpleVector(0, -halfHeight, 0);
		SimpleVector[] bottomV = new SimpleVector[faces];
		
		// Prepare vertices
		float angle = 0;
		float angleStep = (float) (2 * Math.PI / faces);
		float x;
		float y;
		for (int i = 0; i < faces; i++) {
			x = (float) Math.cos(angle);
			y = (float) Math.sin(angle);
			
			bottomV[i] = new SimpleVector(radius * x, halfHeight, radius * y);
			
			angle += angleStep;
		}
		
		// Add cap if wanted
		if (cap) {
			angle = angleStep + angleStep;
			float u0 = 0.5f + 0.5f * (float) Math.cos(0);
			float v0 = 0.5f - 0.5f * (float) Math.sin(0);
			float u1 = 0.5f + 0.5f * (float) Math.cos(angleStep);
			float v1 = 0.5f - 0.5f * (float) Math.sin(angleStep);
			float u2 = 0.5f + 0.5f * (float) Math.cos(angle);
			float v2 = 0.5f - 0.5f * (float) Math.sin(angle);
			for (int i = 1; i + 1 < faces; i++) {
				u2 = 0.5f + 0.5f * (float) Math.cos(angle);
				v2 = 0.5f - 0.5f * (float) Math.sin(angle);
				
				obj.addTriangle(bottomV[0], 1f - u0, v0, bottomV[i + 1], 1f - u2, v2, bottomV[i], 1f - u1, v1);
				
				u1 = u2;
				v1 = v2;
				
				angle += angleStep;
			}
		}
		
		// Add round faces
		float u = 0;
		float uStep = 2f / faces;
		for (int i = 0; i < faces; i++) {
			obj.addTriangle(topV, u + uStep / 2, 0f, bottomV[i], u, 1f, bottomV[(i + 1) % faces], u + uStep, 1f);
			
			u += uStep;
		}
		
		return obj;
	}
	
	/**
	 * Creates a sprite of size 1*1.
	 * 
	 * A sprite is just a surface of two triangles that will always point towards the camera. Sprites are especially
	 * used for particle effect. If you don't want the sprite pointing towards the camera you can disable it by using
	 * the setBillboarding method.
	 * @return created sprite
	 */
	public static Object3D createSprite() {
		return createSprite(1f);
	}
	
	/**
	 * Creates a sprite of a specific size.
	 * 
	 * A sprite is just a surface of two triangles that will always point towards the camera. Sprites are especially
	 * used for particle effect. If you don't want the sprite pointing towards the camera you can disable it by using
	 * the setBillboarding method.
	 * @param size of the sprite
	 * @return created sprite
	 */
	public static Object3D createSprite(float size) {
		return createSprite(size, size);
	}
	
	/**
	 * Creates a sprite of a specific size.
	 * 
	 * A sprite is just a surface of two triangles that will always point towards the camera. Sprites are especially
	 * used for particle effect. If you don't want the sprite pointing towards the camera you can disable it by using
	 * the setBillboarding method.
	 * @param width of the sprite
	 * @param height of the sprite
	 * @return created sprite
	 */
	public static Object3D createSprite(float width, float height) {
		Object3D obj = new Object3D(2);
		
		float halfWidth = width / 2;
		float halfHeight = height / 2;
		
		SimpleVector v0 = new SimpleVector(-halfWidth, -halfHeight, 0);
		SimpleVector v1 = new SimpleVector(halfWidth, -halfHeight, 0);
		SimpleVector v2 = new SimpleVector(-halfWidth, halfHeight, 0);
		SimpleVector v3 = new SimpleVector(halfWidth, halfHeight, 0);
		
		obj.addTriangle(v0, 0f, 0f, v2, 0f, 1f, v1, 1f, 0f);
		obj.addTriangle(v1, 1f, 0f, v2, 0f, 1f, v3, 1f, 1f);
		
		obj.setBillboarding(Object3D.BILLBOARDING_ENABLED);
		
		return obj; 
	}
	
	/**
	 * Creates a plane. A plane is a flat rectangle consisting of quads (smaller rectangles). By default each quad has
	 * a size of 1.
	 * @param quads Number of quads at each edge.
	 * @return the created plane
	 */
	public static Object3D createPlane(int quads) {
		return createPlane(1f, quads);
	}
	
	/**
	 * Creates a plane. A plane is a flat rectangle consisting of quads (smaller rectangles).
	 * @param size of the quads
	 * @param quads Number of quads at each edge.
	 * @return the created plane
	 */
	public static Object3D createPlane(float size, int quads) {
		Object3D obj = new Object3D(2 * quads * quads);
		
		// Prepare vertices
		SimpleVector[][] v = new SimpleVector[quads + 1][quads + 1];
		for (int x = 0; x <= quads; x++) {
			for (int y = 0; y <= quads; y++) {
				v[x][y] = new SimpleVector(size * (x - quads / 2f), 0f, size * (y - quads / 2f));
			}
		}
		
		// Create quads
		float scale = 1f / quads;
		for (int x = 0; x < quads; x++) {
			for (int y = 0; y < quads; y++) {
				obj.addTriangle(v[x][y + 1], scale * x, scale * (quads - y - 1),
						v[x][y], scale * x, scale * (quads - y),
						v[x + 1][y + 1], scale * (x + 1), scale * (quads - y - 1));
				obj.addTriangle(v[x + 1][y + 1], scale * (x + 1), scale * (quads - y - 1),
						v[x][y], scale * x, scale * (quads - y),
						v[x + 1][y], scale * (x + 1), scale * (quads - y));
			}
		}
		
		return obj;
	}
	
	/**
	 * Creates a torus (also known as donut) around the Y-Axis. This default torus would perfectly fit into a 1*1*1
	 * cube.
	 * @param lengthQuads Number of quads that should be used along the pipe.
	 * @param pipeQuads Number of quads that should be used the pipe round.
	 * @return the created torus
	 */
	public static Object3D createTorus(int lengthQuads, int pipeQuads) {
		return createTorus(0.35f, 0.15f, lengthQuads, pipeQuads);
	}
	
	/**
	 * Creates a torus (also known as donut) around the Y-Axis.
	 * @param radius Radius of a imaginary circle the torus should contain.
	 * @param pipeRadius Radius of the pipe.
	 * @param lengthQuads Number of quads that should be used along the pipe.
	 * @param pipeQuads Number of quads that should be used the pipe round.
	 * @return the created torus
	 */
	public static Object3D createTorus(float radius, float pipeRadius, int lengthQuads, int pipeQuads) {
		Object3D obj = new Object3D(2 * lengthQuads * pipeQuads);
		
		// Prepare vertices
		SimpleVector[][] v = new SimpleVector[lengthQuads][pipeQuads];
		for (int i = 0; i < pipeQuads; i++) {
			double ringAngle = 2 * Math.PI * i / pipeQuads + Math.PI;
			float ringRadius = (float) (radius + pipeRadius * Math.cos(ringAngle));
			float y = (float) (pipeRadius * Math.sin(ringAngle));
			float angle = 0f;
			float angleStep = (float) (2 * Math.PI / lengthQuads);
			for (int j = 0; j < lengthQuads; j++) {
				v[j][i] = new SimpleVector(ringRadius * Math.cos(angle), y, ringRadius * Math.sin(angle));
				angle += angleStep;
			}
		}
		
		// Create quads
		float uC = 0f;
		float vC = 0f;
		float uStep = 1f / lengthQuads;
		float vStep = 1f / pipeQuads;
		for (int i = 0; i < pipeQuads; i++) {
			uC = 0f;
			for (int j = 0; j < lengthQuads; j++) {
				obj.addTriangle(v[j][i], uC, vC,
						v[j][(i + 1) % pipeQuads], uC, vC + vStep,
						v[(j + 1) % lengthQuads][i], uC + uStep, vC);
				obj.addTriangle(v[(j + 1) % lengthQuads][i], uC + uStep, vC,
						v[j][(i + 1) % pipeQuads], uC, vC + vStep,
						v[(j + 1) % lengthQuads][(i + 1) % pipeQuads], uC + uStep, vC + vStep);
				uC += uStep;
			}
			vC += vStep;
		}
		
		return obj;
	}
	
	/**
	 * Creates a pyramid that would perfectly fit into a 1*1*1 cube. The top points towards the negative Y-Axis.
	 * @return the created pyramid
	 */
	public static Object3D createPyramid() {
		return createPyramid(1f);
	}
	
	/**
	 * Creates a pyramid that would perfectly fit into a specific cube. The top points towards the negative Y-Axis.
	 * @param size of the cube in which the pyramid should perfectly fit in
	 * @return the created pyramid
	 */
	public static Object3D createPyramid(float size) {
		return createPyramid(size, size);
	}
	
	/**
	 * Creates a pyramid with a specific ground size and height. The top points towards the negative Y-Axis.
	 * @param size of the ground of the pyramid
	 * @param height of the pyramid
	 * @return the created pyramid
	 */
	public static Object3D createPyramid(float size, float height) {
		Object3D obj = new Object3D(6);
		
		float halfSize = size / 2;
		float halfHeight = height / 2;
		
		// Prepare vertices
		SimpleVector v0 = new SimpleVector(halfSize, halfHeight, halfSize);
		SimpleVector v1 = new SimpleVector(-halfSize, halfHeight, halfSize);
		SimpleVector v2 = new SimpleVector(halfSize, halfHeight, -halfSize);
		SimpleVector v3 = new SimpleVector(-halfSize, halfHeight, -halfSize);
		SimpleVector v4 = new SimpleVector(0, -halfHeight, 0);
		
		// Create ground
		obj.addTriangle(v0, 0f, 0f, v2, 0f, 1f, v1, 1f, 0f);
		obj.addTriangle(v1, 1f, 0f, v2, 0f, 1f, v3, 1f, 1f);
		
		// Create faces
		obj.addTriangle(v4, 0.5f, 0f, v3, 0f, 1f, v2, 1f, 1f);
		obj.addTriangle(v4, 0.5f, 0f, v2, 0f, 1f, v0, 1f, 1f);
		obj.addTriangle(v4, 0.5f, 0f, v1, 0f, 1f, v3, 1f, 1f);
		obj.addTriangle(v4, 0.5f, 0f, v0, 0f, 1f, v1, 1f, 1f);
		
		return obj;
	}
	
	/**
	 * Creates a sphere with a radius of 0.5 (so it would perfectly fit into a 1*1*1 cube).
	 * @param quads Number of quads that should be used for the sphere. You should seriously not use more quads than
	 * you need!
	 * @return the created sphere
	 */
	public static Object3D createSphere(int quads) {
		return createSphere(0.5f, quads);
	}
	
	/**
	 * Creates a sphere with a specific radius.
	 * @param radius of the sphere
	 * @param quads Number of quads that should be used for the sphere. You should seriously not use more quads than
	 * you need!
	 * @return the created sphere
	 */
	public static Object3D createSphere(float radius, int quads) {
		float size = 2 * radius;
		return createEllipsoid(new SimpleVector(size, size, size), quads);
	}
	
	/**
	 * Creates an ellipsoid with given width, height and depth.
	 * @param size Vector that contains width, height and depth
	 * @param quads Number of quads that should be used for the sphere. You should seriously not use more quads than
	 * you need!
	 * @return the created ellipsoid
	 */
	public static Object3D createEllipsoid(SimpleVector size, int quads) {
		return createEllipsoid(size, quads, 2f, 1f);
	}
	
	/**
	 * Creates an ellipsoid with given width, height and depth.
	 * @param size Vector that contains width, height and depth
	 * @param quads Number of quads that should be used for the sphere. You should seriously not use more quads than
	 * you need!
	 * @param uScale texture u scale, default is 2f
	 * @param vScale texture v scale, default is 1f
	 * @return the created ellipsoid
	 */
	public static Object3D createEllipsoid(SimpleVector size, int quads, float uScale, float vScale) {
		int yQuads = Math.max(quads / 2 + 1, 3);
		Object3D obj = new Object3D(2 * quads * yQuads);
		
		// Prepare vertices
		SimpleVector[][] v = new SimpleVector[quads][yQuads];
		for (int y = 0; y < yQuads; y++) {
			float yAngle = (float) (Math.PI * y / (yQuads - 1));
			float yPos = -0.5f * size.y * (float) Math.cos(yAngle);
			float yRadius = (float) Math.sin(yAngle);
			for (int x = 0; x < quads; x++) {
				float xAngle = (float) (2 * Math.PI * x / quads);
				float xPos = 0.5f * size.x * (float) Math.cos(xAngle) * yRadius;
				float zPos = 0.5f * size.z * (float) Math.sin(xAngle) * yRadius;
				
				v[x][y] = new SimpleVector(xPos, yPos, zPos);
			}
		}
		
		// Create quads
		float v0 = 0;
		float v1 = vScale / (yQuads - 1);
		float vStep = v1;
		for (int y = 0; y + 1 < yQuads; y++) {
			for (int x = 0; x < quads; x++) {
				float u0 = uScale * (float) x / quads;
				float u1 = uScale * (float) (x + 1) / quads;
				if (y > 0) {
					obj.addTriangle(v[x][y], u0, v0,
							v[x][y + 1], u0, v1,
							v[(x + 1) % quads][y], u1, v0);
				}
				if (y + 2 < yQuads) {
					obj.addTriangle(v[(x + 1) % quads][y], u1, v0,
							v[x][y + 1], u0, v1,
							v[(x + 1) % quads][y + 1], u1, v1);
				}
			}
			
			v0 = v1;
			v1 += vStep;
		}
		
		return obj;
	}
	
	/**
	 * Creates a disc with a size of 1*1 (so it has radius 0.5). A disk is flat an can only seen from one side.
	 * The disk is created along the Z-Axis and should be visible from the negative Z-Axis.
	 * @param vertices Number of vertices that should be used.
	 * @return the created disk
	 */
	public static Object3D createDisc(int vertices) {
		return createDisc(0.5f, vertices);
	}
	
	/**
	 * Creates a disc with a specific radius. A disk is flat an can only seen from one side. The disk is created along
	 * the Z-Axis and should be visible from the negative Z-Axis.
	 * @param radius of the disk
	 * @param vertices Number of vertices that should be used.
	 * @return the created disk
	 */
	public static Object3D createDisc(float radius, int vertices) {
		return createDisc(radius, radius, vertices);
	}
	
	/**
	 * Creates a disc with a specific radius in X and in Y direction. A disk is flat an can only seen from one side.
	 * The disk is created along the Z-Axis and should be visible from the negative Z-Axis.
	 * @param xradius Radius along the X-Axis.
	 * @param yradius Radius along the Y-Axis.
	 * @param vertices Number of vertices that should be used.
	 * @return the created disk
	 */
	public static Object3D createDisc(float xradius, float yradius, int vertices) {
		Object3D obj = new Object3D(vertices - 2);
		
		// Prepare vertices
		SimpleVector[] v = new SimpleVector[vertices];
		float angle = 0f;
		float angleStep = (float) (2 * Math.PI / vertices);
		for (int i = 0; i < vertices; i++) {
			float x = xradius * (float) Math.cos(angle);
			float y = yradius * (float) Math.sin(angle);
			v[i] = new SimpleVector(x, y, 0);
			
			angle += angleStep;
		}
		
		// Create triangles
		angle = angleStep + angleStep;
		float u0 = 0.5f - 0.5f * (float) Math.cos(0);
		float v0 = 0.5f + 0.5f * (float) Math.sin(0);
		float u1 = 0.5f - 0.5f * (float) Math.cos(angleStep);
		float v1 = 0.5f + 0.5f * (float) Math.sin(angleStep);
		float u2 = 0.5f - 0.5f * (float) Math.cos(angle);
		float v2 = 0.5f + 0.5f * (float) Math.sin(angle);
		for (int i = 1; i + 1 < vertices; i++) {
			u2 = 0.5f - 0.5f * (float) Math.cos(angle);
			v2 = 0.5f + 0.5f * (float) Math.sin(angle);
			
			obj.addTriangle(v[0], 1f - u0, v0, v[i + 1], 1f - u2, v2, v[i], 1f - u1, v1);
			
			u1 = u2;
			v1 = v2;
			
			angle += angleStep;
		}
		
		return obj;
	}
	
	/**
	 * Creates a cog that fits into a 1*1*1 cube. A cog is created so that it would turn around the Y-Axis.
	 * @param teeth Number of teeth.
	 * @return the created cog
	 */
	public static Object3D createCog(int teeth) {
		return createCog(teeth, 0.1f, 0.4f, 0.5f, 1f, 1f, 0f, 0.1f);
	}
	
	/**
	 * Creates a cog. A cog is created so that it would turn around the Y-Axis.
	 * @param teeth Number of teeth
	 * @param holeRadius Radius of the hole within the cog.
	 * @param innerRadius Radius without the theeth.
	 * @param outerRadius Radius with the teeth.
	 * @param innerSpace Factor of the size between teeth. Default is 1.
	 * @param outerSpace Factor of the size of teeth. Default is 1.
	 * @param outerMovement Turning of the teeth in radian. Default is 0.
	 * @param height Heihgt of the cog.
	 * @return the created cog
	 */
	public static Object3D createCog(int teeth, float holeRadius, float innerRadius, float outerRadius,
			float innerSpace, float outerSpace, float outerMovement, float height) {
		Object3D obj = new Object3D(24 * teeth);
		
		// Prepare vertices
		int count = 2 * teeth;
		SimpleVector[] topHole = new SimpleVector[count];
		SimpleVector[] bottomHole = new SimpleVector[count];
		SimpleVector[] topInner = new SimpleVector[count];
		SimpleVector[] bottomInner = new SimpleVector[count];
		SimpleVector[] topOuter = new SimpleVector[count];
		SimpleVector[] bottomOuter = new SimpleVector[count];
		float halfHeight = height / 2;
		float angle = 0f;
		float angleStep = (float) (Math.PI / teeth);
		for (int i = 0; i < count; i++) {
			float x = holeRadius * (float) Math.cos(angle);
			float y = holeRadius * (float) Math.sin(angle);
			
			topHole[i] = new SimpleVector(x, -halfHeight, y);
			bottomHole[i] = new SimpleVector(x, halfHeight, y);
			
			angle += angleStep;
		}
		angle = 0.5f * (2 - innerSpace) * angleStep;
		for (int i = 0; i < count; i += 2) {
			float x = innerRadius * (float) Math.cos(angle);
			float y = innerRadius * (float) Math.sin(angle);
			topInner[i] = new SimpleVector(x, -halfHeight, y);
			bottomInner[i] = new SimpleVector(x, halfHeight, y);
			angle += innerSpace * angleStep;
			
			x = innerRadius * (float) Math.cos(angle);
			y = innerRadius * (float) Math.sin(angle);
			topInner[i + 1] = new SimpleVector(x, -halfHeight, y);
			bottomInner[i + 1] = new SimpleVector(x, halfHeight, y);
			angle += (2 - innerSpace) * angleStep;
		}
		angle = 0.5f * outerSpace * angleStep + outerMovement;
		for (int i = 0; i < count; i += 2) {
			float x = outerRadius * (float) Math.cos(angle);
			float y = outerRadius * (float) Math.sin(angle);
			topOuter[i] = new SimpleVector(x, -halfHeight, y);
			bottomOuter[i] = new SimpleVector(x, halfHeight, y);
			angle += (2 - outerSpace) * angleStep;
			
			x = outerRadius * (float) Math.cos(angle);
			y = outerRadius * (float) Math.sin(angle);
			topOuter[i + 1] = new SimpleVector(x, -halfHeight, y);
			bottomOuter[i + 1] = new SimpleVector(x, halfHeight, y);
			angle += outerSpace * angleStep;
		}
		
		// Create triangles
		for (int i = 0; i < count; i++) {
			float topHoleU0 = 0.5f + 0.5f * topHole[i].x / outerRadius;
			float topHoleU1 = 0.5f + 0.5f * topHole[(i + 1) % count].x / outerRadius;
			float topHoleV0 = 0.5f - 0.5f * topHole[i].z / outerRadius;
			float topHoleV1 = 0.5f - 0.5f * topHole[(i + 1) % count].z / outerRadius;
			float topInnerU0 = 0.5f + 0.5f * topInner[i].x / outerRadius;
			float topInnerU1 = 0.5f + 0.5f * topInner[(i + 1) % count].x / outerRadius;
			float topInnerV0 = 0.5f - 0.5f * topInner[i].z / outerRadius;
			float topInnerV1 = 0.5f - 0.5f * topInner[(i + 1) % count].z / outerRadius;
			float topOuterU0 = 0.5f + 0.5f * topOuter[i].x / outerRadius;
			float topOuterU1 = 0.5f + 0.5f * topOuter[(i + 1) % count].x / outerRadius;
			float topOuterV0 = 0.5f - 0.5f * topOuter[i].z / outerRadius;
			float topOuterV1 = 0.5f - 0.5f * topOuter[(i + 1) % count].z / outerRadius;
			
			// Hole
			obj.addTriangle(topHole[(i + 1) % count], topHoleU1, topHoleV1,
					bottomHole[(i + 1) % count], -topHoleU1, topHoleV1,
					topHole[i], topHoleU0, topHoleV0);
			obj.addTriangle(topHole[i], topHoleU0, topHoleV0,
					bottomHole[(i + 1) % count], -topHoleU1, topHoleV1,
					bottomHole[i], -topHoleU0, topHoleV0);
			
			// Top inner face
			obj.addTriangle(topInner[(i + 1) % count], topInnerU1, topInnerV1,
					topHole[(i + 1) % count], topHoleU1, topHoleV1,
					topInner[i], topInnerU0, topInnerV0);
			obj.addTriangle(topInner[i], topInnerU0, topInnerV0,
					topHole[(i + 1) % count], topHoleU1, topHoleV1,
					topHole[i], topHoleU0, topHoleV0);
			
			// Bottom inner face
			obj.addTriangle(bottomInner[i], -topInnerU0, topInnerV0,
					bottomHole[i], -topHoleU0, topHoleV0,
					bottomInner[(i + 1) % count], -topInnerU1, topInnerV1);
			obj.addTriangle(bottomInner[(i + 1) % count], -topInnerU1, topInnerV1,
					bottomHole[i], -topHoleU0, topHoleV0,
					bottomHole[(i + 1) % count], -topHoleU1, topHoleV1);
			
			if (i % 2 == 1) {
				// Top outer face
				obj.addTriangle(topOuter[(i + 1) % count], topOuterU1, topOuterV1,
						topInner[(i + 1) % count], topInnerU1, topOuterV1,
						topOuter[i], topOuterU0, topOuterV0);
				obj.addTriangle(topOuter[i], topOuterU0, topOuterV0,
						topInner[(i + 1) % count], topInnerU1, topInnerV1,
						topInner[i], topInnerU0, topInnerV0);
				
				// Bottom outer face
				obj.addTriangle(bottomOuter[i], -topOuterU0, topOuterV0, 
						bottomInner[i], -topInnerU0, topInnerV0,
						bottomOuter[(i + 1) % count], -topOuterU1, topOuterV1);
				obj.addTriangle(bottomOuter[(i + 1) % count], -topOuterU1, topOuterV1,
						bottomInner[i], -topInnerU0, topInnerV0,
						bottomInner[(i + 1) % count], -topInnerU1, topInnerV1);
				
				// Outer side face
				obj.addTriangle(topOuter[i], topOuterU0, topOuterV0,
						bottomOuter[i], topOuterU0, topOuterV0,
						topOuter[(i + 1) % count], topOuterU1, topOuterV1);
				obj.addTriangle(topOuter[(i + 1) % count], topOuterU1, topOuterV1,
						bottomOuter[i], topOuterU0, topOuterV0,
						bottomOuter[(i + 1) % count], topOuterU1, topOuterV1);
				obj.addTriangle(topInner[i], topInnerU0, topInnerV0,
						bottomInner[i], topInnerU0, topInnerV0,
						topOuter[i], topOuterU0, topOuterV0);
				obj.addTriangle(topOuter[i], topOuterU0, topOuterV0,
						bottomInner[i], topInnerU0, topInnerV0,
						bottomOuter[i], topOuterU0, topOuterV0);
			} else {
				// Inner side face
				obj.addTriangle(topInner[i], topInnerU0, topInnerV0,
						bottomInner[i], topInnerU0, topInnerV0,
						topInner[(i + 1) % count], topInnerU1, topInnerV1);
				obj.addTriangle(topInner[(i + 1) % count], topInnerU1, topInnerV1,
						bottomInner[i], topInnerU0, topInnerV0,
						bottomInner[(i + 1) % count], topInnerU1, topInnerV1);
				obj.addTriangle(topOuter[i], topOuterU0, topOuterV0,
						bottomOuter[i], topOuterU0, topOuterV0,
						topInner[i], topInnerU0, topInnerV0);
				obj.addTriangle(topInner[i], topInnerU0, topInnerV0,
						bottomOuter[i], topOuterU0, topOuterV0,
						bottomInner[i], topInnerU0, topInnerV0);
			}
		}
		
		return obj;
	}
	
	/**
	 * Creates a tube along the Y-Axis. A tube is nothing else than a cylinder with an hole in it. This default tube
	 * has a innerRadius of 0.3, outer radius of 0.5 and a height of 1 (so wow, it would fit into a 1*1*1 cube).
	 * @param quads Number of quads that should be used. Don't use more than you need!
	 * @return the created cube
	 */
	public static Object3D createTube(int quads) {
		return createTube(0.3f, 0.5f, 1f, quads);
	}
	
	/**
	 * Creates a tube along the Y-Axis. A tube is nothing else than a cylinder with an hole in it.
	 * @param innerRadius Radius of the cylinder.
	 * @param outerRadius Radius of the hole.
	 * @param height Heihgt of the cylinder.
	 * @param quads Number of quads that should be used. Don't use more than you need!
	 * @return the created cube.
	 */
	public static Object3D createTube(float innerRadius, float outerRadius, float height, int quads) {
		Object3D obj = new Object3D(8 * quads);
		
		// Prepare vertices
		SimpleVector[] topOuterV = new SimpleVector[quads];
		SimpleVector[] topInnerV = new SimpleVector[quads];
		SimpleVector[] bottomOuterV = new SimpleVector[quads];
		SimpleVector[] bottomInnerV = new SimpleVector[quads];
		float halfHeight = height / 2;
		float angle = 0f;
		float angleStep = 2 * (float) Math.PI / quads;
		float innerFac = innerRadius / outerRadius;
		for (int i = 0; i < quads; i++) {
			float outerX = outerRadius * (float) Math.cos(angle);
			float outerY = outerRadius * (float) Math.sin(angle);
			float innerX = innerFac * outerX;
			float innerY = innerFac * outerY;
			
			topOuterV[i] = new SimpleVector(outerX, -halfHeight, outerY);
			topInnerV[i] = new SimpleVector(innerX, -halfHeight, innerY);
			bottomOuterV[i] = new SimpleVector(outerX, halfHeight, outerY);
			bottomInnerV[i] = new SimpleVector(innerX, halfHeight, innerY);
			
			angle += angleStep;
		}
		
		// Create quads
		float uC = 0f;
		float uStep = 2f / quads;
		for (int i = 0; i < quads; i++) {
			float ou0 = 0.5f + 0.5f * topOuterV[i].x / outerRadius;
			float ou1 = 0.5f + 0.5f * topOuterV[(i + 1) % quads].x / outerRadius;
			float ov0 = 0.5f - 0.5f * topOuterV[i].z / outerRadius;
			float ov1 = 0.5f - 0.5f * topOuterV[(i + 1) % quads].z / outerRadius;
			float iu0 = 0.5f + 0.5f * topInnerV[i].x / outerRadius;
			float iu1 = 0.5f + 0.5f * topInnerV[(i + 1) % quads].x / outerRadius;
			float iv0 = 0.5f - 0.5f * topInnerV[i].z / outerRadius;
			float iv1 = 0.5f - 0.5f * topInnerV[(i + 1) % quads].z / outerRadius;
			
			// Top face
			obj.addTriangle(topOuterV[(i + 1) % quads], ou1, ov1,
					topInnerV[(i + 1) % quads], iu1, iv1,
					topOuterV[i], ou0, ov0);
			obj.addTriangle(topOuterV[i], ou0, ov0,
					topInnerV[(i + 1) % quads], iu1, iv1,
					topInnerV[i], iu0, iv0);
			
			// Bottom face
			obj.addTriangle(bottomOuterV[i], -ou0, ov0,
					bottomInnerV[i], -iu0, iv0,
					bottomOuterV[(i + 1) % quads], -ou1, ov1);
			obj.addTriangle(bottomOuterV[(i + 1) % quads], -ou1, ov1,
					bottomInnerV[i], -iu0, iv0,
					bottomInnerV[(i + 1) % quads], -iu1, iv1);
			
			// Outer face
			obj.addTriangle(topOuterV[i], uC, 0f,
					bottomOuterV[i], uC, 1f,
					topOuterV[(i + 1) % quads], uC + uStep, 0f);
			obj.addTriangle(topOuterV[(i + 1) % quads], uC + uStep, 0f,
					bottomOuterV[i], uC, 1f,
					bottomOuterV[(i + 1) % quads], uC + uStep, 1f);
			
			// Inner face
			obj.addTriangle(topInnerV[(i + 1) % quads], -uC - uStep, 0f,
					bottomInnerV[(i + 1) % quads], -uC - uStep, 1f,
					topInnerV[i], -uC, 0f);
			obj.addTriangle(topInnerV[i], -uC, 0f,
					bottomInnerV[(i + 1) % quads], -uC - uStep, 1f,
					bottomInnerV[i], -uC, 1f);
			
			uC += uStep;
		}
		
		return obj;
	}
	
	/**
	 * Moves, scales and again moves uv coords on an Object3D object. To let this method work properly you have to
	 * follow these rules:</br>
	 * 1. Never call strip() before</br>
	 * 2. Use build(false) instead of build() method (you have to call it manually)</br>
	 * 3. Call touch() on object to apply changes
	 * @param obj object whom uv coords should be scaled
	 * @param uMove1 u movement before scaling
	 * @param vMove1 v movement before scaling
	 * @param uScale scale factor for u coords
	 * @param vScale scale factor for v coords
	 * @param uMove2 u movement after scaling
	 * @param vMove2 v movement after scaling
	 */
	public static void transformUVCoords(Object3D obj, float uMove1, float vMove1, float uScale, float vScale,
			float uMove2, float vMove2) {
		PolygonManager mgr = obj.getPolygonManager();
		SimpleVector[] coords = {new SimpleVector(), new SimpleVector(), new SimpleVector()};
		
		for (int i = 0; i < mgr.getMaxPolygonID(); i++) {
			for (int j = 0; j < 3; j++) {
				mgr.getTextureUV(i, j, coords[j]);
				
				coords[j].x += uMove1;
				coords[j].x *= uScale;
				coords[j].x += uMove2;
				
				coords[j].y += vMove1;
				coords[j].y *= vScale;
				coords[j].y += vMove2;
			}
			
			TextureInfo info = new TextureInfo(
					mgr.getPolygonTexture(i),
					coords[0].x,
					coords[0].y,
					coords[1].x,
					coords[1].y,
					coords[2].x,
					coords[2].y);
			mgr.setPolygonTexture(i, info);
		}
	}
	
	private static void calculateFlatNormals(final Object3D obj) {
		IVertexController c = new GenericVertexController() {
			@Override
			public void apply() {
				SimpleVector[] sv = getSourceMesh();
				SimpleVector[] dn = getDestinationNormals();
				System.out.println(sv.length + " ............. " + dn.length);
				System.out.println(obj.getMesh().getUniqueVertexCount());
				for (int i = 0; i < sv.length; i += 3) {
					SimpleVector a = sv[i + 1].calcSub(sv[i]);
					SimpleVector b = sv[i + 2].calcSub(sv[i]);
					SimpleVector n = a.calcCross(b).normalize();
					dn[i] = n;
					dn[i + 1] = n;
					dn[i + 2] = n;
				}
			}
		};
		obj.getMesh().setVertexController(c, true);
		obj.getMesh().applyVertexController();
		obj.getMesh().removeVertexController();
		obj.getMesh().getUniqueVertexCount();
		obj.build();
	}
}




