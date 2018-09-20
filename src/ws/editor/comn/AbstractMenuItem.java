package ws.editor.comn;

public abstract class AbstractMenuItem {
	private String name;

	public AbstractMenuItem(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public abstract void doAction();
}
