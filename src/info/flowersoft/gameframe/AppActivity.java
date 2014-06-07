package info.flowersoft.gameframe;

import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

/**
 * Base Activity class you can extend to get a real simple class. You just have to call the super constructor
 * in your own constructor and override getRenderer() to return your preferred AppRenderer.
 * 
 * @author Lobby
 */
public abstract class AppActivity extends Activity {

	private static AppRenderer renderer;
	
	private GLSurfaceView view;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		onCreation();
		
		if (renderer == null) {
			renderer = getRenderer(savedInstanceState);
		} else {
			renderer.setContext(this);
		}
		
		view = new AppView(this, renderer);
		setContentView(view);
	}
	
	@Override
	protected void onPause() {
		renderer.onPause();
		super.onPause();
		view.onPause();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		view.onResume();
		
		renderer.onResume();
	}
	
	/**
	 * Call this method in onCreation() to hide the action bar alias title of the activity.
	 */
	@SuppressLint("NewApi")
	protected void hideActionBar() {
		if (Build.VERSION.SDK_INT >= 11) {
			getActionBar().hide();
		} else {
			requestWindowFeature(Window.FEATURE_NO_TITLE);
		}
	}
	
	/**
	 * Call this method in onCreation() to hide the status bar so that you app runs in fullscreen mode.
	 */
	protected void hideStatusBar() {
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}
	
	/**
	 * Override this method to return your preferred AppRenderer. If your renderer overrides the constructor of
	 * AppRenderer this may look like <b>return new MyRenderer(savedInstanceState, this);</b>.
	 * @param savedInstanceState instance state the app got on start
	 * @return own app renderer
	 */
	public abstract AppRenderer getRenderer(Bundle savedInstanceState);

	/**
	 * Override this method to do start up things like hiding the action bar. Nothing will be called by default.
	 */
	protected void onCreation() {
	}
}
