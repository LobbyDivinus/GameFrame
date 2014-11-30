package info.flowersoft.gamestack;

public abstract class GameStage {

	private GameStack stack;
	
	protected void bind(GameStack stack) {
		this.stack = stack;
	}
	
	protected boolean isBound() {
		return stack != null;
	}
	
	public GameStack getGameStack() {
		return stack;
	}
	
	public abstract void prepare();
	
	public abstract void drop();
	
	public abstract void enter();
	
	public abstract void leave();
	
	public abstract void update();
	
}
