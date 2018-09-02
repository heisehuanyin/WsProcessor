package ws.editor.plugin.pjt_manager;

import java.util.ArrayList;

import ws.editor.common.GroupSymbo;
import ws.editor.common.NodeSymbo;

public class SimpleDirSymbo extends SimpleFileSymbo implements GroupSymbo {
	private ArrayList<NodeSymbo> children = new ArrayList<>();

	public SimpleDirSymbo() {
	}

	@Override
	public int kind() {
		return NodeSymbo.KindDir;
	}

	@Override
	public void addChild(NodeSymbo one) {
		this.children.add(one);
		one.initParent(this);
	}

	@Override
	public int getChildCount() {
		return children.size();
	}

	@Override
	public NodeSymbo getChildAtIndex(int index) {
		return children.get(index);
	}

	@Override
	public void removeChild(NodeSymbo one) {
		this.children.remove(one);
	}

	@Override
	public void insertChildAtIndex(NodeSymbo node, int index) {
		this.children.add(index, node);
	}

	@Override
	public int getChildIndex(NodeSymbo child) {
		return this.children.indexOf(child);
	}

}
