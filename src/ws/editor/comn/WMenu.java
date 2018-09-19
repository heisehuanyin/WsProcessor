package ws.editor.comn;

import java.util.ArrayList;

public abstract class WMenu {
	private String name;
	private ArrayList<WMenu> children = new ArrayList<>();
	
	public WMenu(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	public abstract void doAction();
	
	
	public void addSperator() {
		
	}
	
	public void addChild(WMenu childMenu) {
		if(children.contains(childMenu))
			return;
		children.add(childMenu);
	}
	
	public void removeChild(WMenu childMenu) {
		if(children.contains(childMenu))
			children.remove(childMenu);
	}
	
	public int getChildCount() {
		return this.children.size();
	}
	
	public WMenu getChildAt(int index) {
		return this.children.get(index);
	}
	
	public int getChildIndex(WMenu child) {
		return this.children.indexOf(child);
	}
	
}

