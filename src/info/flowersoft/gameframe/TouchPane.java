package info.flowersoft.gameframe;

import java.util.ArrayList;
import java.util.List;

import info.flowersoft.gameframe.description.ScreenShape;
import info.flowersoft.gameframe.touch.TouchPoint;
import info.flowersoft.gameframe.touch.TouchUpdate;

public class TouchPane implements Touchable {

	private ScreenShape shape;
	
	private int maxPoints;
	
	private List<TouchPoint> tps;
	
	public TouchPane(ScreenShape shape, int maxTouchPoints) {
		this.shape = shape;
		this.maxPoints = maxTouchPoints;
		
		tps = new ArrayList<TouchPoint>();
	}
	
	@Override
	public void update(TouchUpdate tpUpdate) {
		if (tpUpdate.getAddedTouchPoint() != null) {
			if (shape.contains((int) tpUpdate.getAddedTouchPoint().getX(),
					(int) tpUpdate.getAddedTouchPoint().getY())) {
				if (maxPoints > tps.size()) {
					tps.add(tpUpdate.getAddedTouchPoint());
					tpUpdate.clearAddedTouchPoint();
				}
			}
		}
		
		if (!tps.isEmpty()) {
			if (tpUpdate.getRemovedTouchPoint() != null) {
				tps.remove(tpUpdate.getRemovedTouchPoint());
			}
		}
	}

	@Override
	public void flush() {
		tps.clear();
	}
	
	public int countTouchPoints() {
		return tps.size();
	}
	
	public TouchPoint getTouchPoint(int i) {
		return tps.get(i);
	}
	
	public float[] getMovement() {
		if (tps.size() == 1) {
			return new float[] {tps.get(0).getXSpeed(), tps.get(0).getYSpeed()};
		} else {
			return new float[] {0f, 0f};
		}
	}
	
	public float getScale() {
		if (tps.size() == 2) {
			TouchPoint[] tp = {tps.get(0), tps.get(1)};
			
			float[] dx = new float[2];
			float[] dy = new float[2];
			
			dx[0] = tp[0].getX() - tp[0].getXSpeed() - tp[1].getX() + tp[1].getXSpeed();
			dy[0] = tp[0].getY() - tp[0].getYSpeed() - tp[1].getY() + tp[1].getYSpeed();
			dx[1] = tp[0].getX() - tp[1].getX();
			dy[1] = tp[0].getY() - tp[1].getY();
			
			float[] dist = new float[2];
			dist[0] = (float) Math.sqrt(dx[0] * dx[0] + dy[0] + dy[0]);
			dist[1] = (float) Math.sqrt(dx[1] * dx[1] + dy[1] + dy[1]);
			
			return dist[1] / dist[0];
		} else {
			return 1f;
		}
	}
	
}
