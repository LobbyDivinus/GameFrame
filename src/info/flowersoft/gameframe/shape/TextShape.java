package info.flowersoft.gameframe.shape;

import info.flowersoft.gameframe.description.Brush;
import info.flowersoft.gameframe.description.FontDescription;

public class TextShape extends Shape {

	FontDescription font;
	
	String text;
	
	TextShape() {
	}
	
	TextShape(ShapeFactory fac, Brush b, int maxTriangles, FontDescription font, String text) {
		super(fac, b, maxTriangles);
		
		this.font = font;
		this.text = text;
	}
	
	public FontDescription getFont() {
		return font;
	}
	
	/**
	 * Not implemented yet. Changes the text of a text line.
	 * @param line new text
	 */
	public void setText(String line) {
		if (!text.equals(line)) {
			throw new UnsupportedOperationException();
		}
	}
	
	/**
	 * Returns current text of the text shape.
	 * @return
	 */
	public String getText() {
		return text;
	}

	@Override
	public TextShape clone() {
		TextShape s = new TextShape();
		
		copyAttributesTo(s);
		
		s.font = font;
		s.text = text;
		
		factory.finalizeShape(s);
		
		return s;
	}
}
