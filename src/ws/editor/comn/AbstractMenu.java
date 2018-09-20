package ws.editor.comn;

import java.util.ArrayList;

public abstract class AbstractMenu extends AbstractMenuItem{
	private ArrayList<AbstractMenuItem> children = new ArrayList<>();
	
	public AbstractMenu(String name) {
		super(name);
	}
	
	
	public void addSperator() {
		
	}
	
	public void addChild(AbstractMenuItem childMenu) {
		if(children.contains(childMenu))
			return;
		children.add(childMenu);
	}
	
	public void removeChild(AbstractMenuItem childMenu) {
		if(children.contains(childMenu))
			children.remove(childMenu);
	}
	
	public int getChildCount() {
		return this.children.size();
	}
	
	public AbstractMenuItem getChildAt(int index) {
		return this.children.get(index);
	}
	
	public int getChildIndex(AbstractMenuItem child) {
		return this.children.indexOf(child);
	}
	
}

