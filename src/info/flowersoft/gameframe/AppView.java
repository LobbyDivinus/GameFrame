package info.flowersoft.gameframe;

import info.flowersoft.gameframe.touch.TouchMapper;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

public class AppView extends GLSurfaceView {

	private AppRenderer renderer;
	
	private TouchMapper mapper;
	
	public AppView(Context context) {
		super(context);
	}
	
	public AppView(Context context, AppRenderer renderer) {
		super(context);
		
		this.renderer = renderer;
		
		setEGLContextClientVersion(2);
		setRenderer(this.renderer);
		
		mapper = renderer.getTouchMapper();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		synchronized (mapper) {
			mapper.map(event);
		}
		
		return true;
	}
	
}
