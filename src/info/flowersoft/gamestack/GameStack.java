package info.flowersoft.gamestack;

import java.util.LinkedList;
import java.util.Stack;

public class GameStack {

	private Stack<GameStage> stages;
	
	private LinkedList<GameStage> droppedStages;
	
	private GameStage lastStage;
	
	public GameStack() {
		stages = new Stack<GameStage>();
		droppedStages = new LinkedList<GameStage>();
	}
	
	public void push(GameStage stage) {
		stages.push(stage);
	}
	
	public GameStage pop() {
		GameStage stage = stages.pop();
		droppedStages.addFirst(stage);
		return stage;
	}
	
	public void popAll() {
		while (!isEmpty() && pop() != null);
	}
	
	public void update() {
		if (!isEmpty()) {
			GameStage stage = stages.lastElement();
			
			if (lastStage != stage && lastStage != null) {
				lastStage.leave();
			}
			
			if (!stage.isBound()) {
				stage.bind(this);
				stage.prepare();
				stage.enter();
			} else if (lastStage != stage) {
				stage.enter();
			}
			
			stage.update();
			
			lastStage = stage;
		} else {
			if (lastStage != null) {
				lastStage.leave();
			}
			lastStage = null;
		}
		
		while (!droppedStages.isEmpty()) {
			GameStage drop = droppedStages.removeLast();
			if (drop.isBound()) {
				if (drop == lastStage) {
					lastStage.leave();
					lastStage = null;
				}
				drop.drop();
			}
		}
	}
	
	public boolean isEmpty() {
		return stages.isEmpty();
	}
	
	public void onPause() {
		if (!isEmpty()) {
			stages.lastElement().leave();
		}
	}
	
	public void onResume() {
		if (!isEmpty()) {
			stages.lastElement().enter();
		}
	}
}
