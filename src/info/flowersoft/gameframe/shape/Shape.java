package info.flowersoft.gameframe.shape;

import info.flowersoft.gameframe.description.Brush;
import info.flowersoft.gameframe.description.ScreenRect;

import com.threed.jpct.Matrix;
import com.threed.jpct.Object3D;
import com.threed.jpct.SimpleVector;

public class Shape implements Cloneable {

	ShapeFactory factory;
	
	Brush brush;
	
	Object3D obj;
	
	float order;
	
	float posX;
	
	float posY;
	
	float width;
	
	float height;
	
	float pivotX;
	
	float pivotY;
	
	float scaleX;
	
	float scaleY;
	
	float angle;
	
	protected Shape() {
	}
	
	public Shape(ShapeFactory fac, Brush b, int maxTriangles) {
		factory = fac;
		brush = b;
		obj = new Object3D(maxTriangles);
		scaleX = 1;
		scaleY = 1;
	}
	
	public Object3D getObject() {
		return obj;
	}
	
	public boolean isVisible() {
		return obj.getVisibility();
	}
	
	public void setVisible(boolean state) {
		obj.setVisibility(state);
	}
	
	public void show() {
		setVisible(true);
	}
	
	public void hide() {
		setVisible(false);
	}
	
	public void setOrder(float offset) {
		order = offset;
		obj.setSortOffset(offset);
	}
	
	public float getOrder() {
		return order;
	}
	
	public void setBrush(Shape s ) {
		setBrush(s.brush);
	}
	
	public void setBrush(Brush b) {
		brush = b;
		brush.apply(obj);
	}
	
	public void applyBrush() {
		brush.apply(obj);
	}
	
	public Brush getBrush() {
		return brush;
	}
	
	public void setPosition(Shape s) {
		setPosition(s.posX, s.posY);
	}
	
	public void setPosition(float x, float y) {
		factory.moveObject(obj, x - posX, y - posY);
		posX = x;
		posY = y;
	}
	
	public float getX() {
		return posX;
	}
	
	public float getY() {
		return posY;
	}
	
	public float getWidth() {
		return width;
	}
	
	public float getHeight() {
		return height;
	}
	
	public void move(float x, float y) {
		factory.moveObject(obj, x, y);
		posX += x;
		posY += y;
	}
	
	public void setPivot(Shape s) {
		setPivot(s.pivotX, s.pivotY);
	}
	
	public void setPivot(float x, float y) {
		float rot = this.angle;
		setRotation(0);
		factory.moveObject(obj, pivotX, pivotY);
		obj.setRotationPivot(factory.getVertex(x, y));
		factory.moveObject(obj, -x, -y);
		setRotation(rot);
		pivotX = x;
		pivotY = y;
	}
	
	public void movePivot(float x, float y) {
		setPivot(pivotX + x, pivotY + y);
	}
	
	public void midPivot() {
		setPivot(width / 2, height / 2);
	}
	
	public void setScale(Shape s) {
		setScale(s.scaleX, s.scaleY);
	}
	
	public void setScale(float x, float y) {
		scale(x / scaleX, y / scaleY);
	}
	
	public void scale(float x, float y) {
		float sx = x;
		float sy = y;
		
		if (sx == 0f) {
			sx = 0.0001f;
		}
		if (sy == 0f) {
			sy = 0.0001f;
		}
		
		Matrix rot = obj.getRotationMatrix();
		Matrix scl = new Matrix();
		scl.set(0, 0, sx);
		scl.set(1, 1, sy);
		scl.matMul(rot);
		obj.setRotationMatrix(scl);
		
		scaleX *= sx;
		scaleY *= sy;
	}
	
	public void setRotation(Shape s) {
		setRotation(s.angle);
	}
	
	public void setRotation(float a) {
		obj.rotateZ(-(a - angle));
		angle = a;
	}
	
	public void turn(float a) {
		obj.rotateZ(a);
		angle += a;
	}
	
	public void addTriangle(float x0, float y0, float x1, float y1, float x2, float y2, float ux, float vy) {
		addTriangle(x0, y0, ux + x0, vy + y0, x1, y1, ux + x1, vy + y1, x2, y2, ux + x2, vy + y2);
	}
	
	public void addTriangle(float x0, float y0, float u0, float v0, float x1, float y1, float u1, float v1,
			float x2, float y2, float u2, float v2) {
		
		SimpleVector vert0 = factory.getVertex(x0, y0);
		SimpleVector vert1 = factory.getVertex(x1, y1);
		SimpleVector vert2 = factory.getVertex(x2, y2);
		
		obj.addTriangle(vert0, u0, v0, vert1, u1, v1, vert2, u2, v2);
	}
	
	public void dispose() {
		factory.removeShape(this);
	}
	
	protected void copyAttributesTo(Shape s) {
		s.factory = factory;
		s.brush = brush;
		obj = new Object3D(s.obj);
		s.posX = posX;
		s.posY = posY;
		s.width = width;
		s.height = height;
		s.pivotX = pivotX;
		s.pivotY = pivotY;
		s.scaleX = scaleX;
		s.scaleY = scaleY;
		s.angle = angle;
	}
	
	public Shape clone() {
		Shape s = new Shape();
		
		copyAttributesTo(s);
		
		factory.finalizeShape(s);
		
		return s;
	}
	
	public ScreenRect getAbsoluteScreenRect() {
		return factory.calculateAbsoluteScreenRect(posX, posY, width, height);
	}
}
