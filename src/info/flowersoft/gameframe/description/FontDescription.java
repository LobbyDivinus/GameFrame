package info.flowersoft.gameframe.description;


import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Powerful font description that only can handle linear bitmap fonts for now.
 * The interface itself also offers other fonts. This is currently not in use to save memory.
 * 
 * @author Fabian Miltenberger
 */
public class FontDescription extends ImageDescription {
	
	Map<Character, Integer> mapping;
	
	float spacing;
	
	float lineSpacing;
	
	public FontDescription(String tex, boolean hasAlpha) {
		super(tex, hasAlpha, false);
		mapping = new HashMap<Character, Integer>();
	}
	
	public FontDescription(String tex, boolean alpha, float glyphWidth, float glyphHeight, char startChar, char endChar) {
		this(tex, alpha);
		
		defineCharStrip(glyphWidth, glyphHeight, startChar, endChar);
	}
	
	public void setSpacing(float horizontal, float vertical) {
		spacing = horizontal;
		lineSpacing = vertical;
	}
	
	public float getHorizontalSpacing() {
		return spacing;
	}
	
	public float getVerticalSpacing() {
		return lineSpacing;
	}
	
	public float getGlyphWidth(char c) {
		return getWidth(mapCharToFrame(c));
	}
	
	public float getGlyphHeight(char c) {
		return getHeight(mapCharToFrame(c));
	}
	
	public int countCharacters() {
		return mapping.size();
	}
	
	public Set<Character> getCharacters() {
		return mapping.keySet();
	}
	
	public void defineCharStrip(float glyph_w, float glyph_h, char startChar, char endChar) {
		int w = getTextureObj().getWidth();
		int h = getTextureObj().getHeight();
		defineCharStrip(0, 0, w, h, glyph_w, glyph_h, startChar, endChar);
	}
	
	public void defineCharStrip(float x, float y, float w, float h, float glyph_w, float glyph_h, char startChar, char endChar) {
		float px = x;
		float py = y;
		char curChar = startChar;
		
		while (curChar <= endChar && py + glyph_h <= y + h) {
			defineChar(curChar, px, py, glyph_w, glyph_h);
			
			px += glyph_w;
			if (px + glyph_w > x + w) {
				px = 0;
				py += glyph_h;
			}
			curChar++;
		}
		
		spacing = 0;
		lineSpacing = 0;
	}
	
	public int defineChar(char c, float sx, float sy, float sw, float sh) {
		int frame = super.addFrame(sx, sy, sw, sh);
		mapping.put(c, frame);
		return frame;
	}

	public float[] getUVCoords(char c) {
		float[] result = null;
		
		if (mapping.containsKey(c)) {
			result = this.getUVCoords(mapping.get(c));
		}
		
		return result;
	}
	
	public int mapCharToFrame(char c) {
		int result = -1;
		
		if (mapping.containsKey(c)) {
			result = mapping.get(c);
		}
		
		return result;
	}
	
}
