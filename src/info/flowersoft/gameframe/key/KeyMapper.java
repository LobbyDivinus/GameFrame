package info.flowersoft.gameframe.key;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.view.KeyEvent;

/**
 * KeyMapper provides a simple interface for key handling within games. To work you just have to give it all KeyEvents
 * your app retrieves.
 * 
 * @author Lobby Divinus
 */
public class KeyMapper {

	private int keyCount;
	
	private boolean[] keyDown;
	
	private boolean[] ghostKeyDown;
	
	private List<Integer> changeList;
	
	private List<Integer> keyHit;
	
	private List<Integer> ghostKeyHit;
	
	private List<Integer> keyRelease;
	
	private List<Integer> ghostKeyRelease;
	
	/**
	 * Constructs a new KeyMapper object.
	 */
	public KeyMapper() {
		keyCount = KeyEvent.getMaxKeyCode() + 1;
		
		keyDown = new boolean[keyCount];
		ghostKeyDown = new boolean[keyCount];
		
		changeList = new ArrayList<Integer>(4);
		
		keyHit = new LinkedList<Integer>();
		ghostKeyHit = new LinkedList<Integer>();
		
		keyRelease = new LinkedList<Integer>();
		ghostKeyRelease = new LinkedList<Integer>();
	}
	
	/**
	 * Call this method for every arrived KeyEvent so the KeyMapper can interprete them.
	 * @param event Retrieved KeyEvent to handle.
	 */
	public synchronized void map(KeyEvent event) {
		int code = event.getKeyCode();
		changeList.add(code);
		
		switch (event.getAction()) {
		case KeyEvent.ACTION_DOWN:
			if (!keyDown[code]) {
				keyDown[code] = true;
				keyHit.add(code);
			}
			break;
		case KeyEvent.ACTION_UP:
			if (keyDown[code]) {
				keyDown[code] = false;
				keyRelease.add(code);
			}
			break;
		default:
			
			break;
		}
	}
	
	/**
	 * Call this method in order to inform the mapper that a new frame begins.
	 */
	public synchronized void update() {
		for (int code : changeList) {
			ghostKeyDown[code] = keyDown[code];
		}
		
		List<Integer> tmp;
		
		tmp = keyHit;
		keyHit = ghostKeyHit;
		ghostKeyHit = tmp;
		keyHit.clear();
		
		tmp = keyRelease;
		keyRelease = ghostKeyRelease;
		ghostKeyRelease = tmp;
		keyRelease.clear();
	}
	
	/**
	 * Determines whether the key code has been hit since last frame.
	 * @param code Code of the key.
	 * @return True if the key has been hit, false otherwise.
	 */
	public synchronized boolean keyHit(int code) {
		return ghostKeyHit.contains(code);
	}
	
	/**
	 * Determines whether the key code is currently pressed down.
	 * @param code Code of the key.
	 * @return True if the key currently pressed, false otherwise.
	 */
	public synchronized boolean keyDown(int code) {
		return ghostKeyDown[code];
	}
	
	/**
	 * Determines whether the key code has been released since the last frame.
	 * @param code Code of the key.
	 * @return True if the has been released, false otherwise.
	 */
	public synchronized boolean keyReleased(int code) {
		return ghostKeyRelease.contains(code);
	}
	
	/**
	 * Flushes all handled keys.
	 */
	public synchronized void flush() {
		for (int i = 0; i < keyCount; i++) {
			keyDown[i] = false;
			ghostKeyDown[i] = false;
		}
		
		changeList.clear();
		keyHit.clear();
		ghostKeyHit.clear();
		keyRelease.clear();
		ghostKeyRelease.clear();
	}
}
