package info.flowersoft.gameframe;

import info.flowersoft.gameframe.AppRenderer;
import info.flowersoft.gameframe.AppView;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.app.Activity;

public abstract class AppActivity extends Activity {

	private static AppRenderer renderer;
	
	private GLSurfaceView view;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getActionBar().hide();
		
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
	
	public abstract AppRenderer getRenderer(Bundle savedInstanceState);

}
