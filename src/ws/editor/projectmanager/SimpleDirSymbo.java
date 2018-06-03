package ws.editor.projectmanager;

import java.util.ArrayList;

import ws.editor.DirSymbo;
import ws.editor.FileSymbo;

public class SimpleDirSymbo extends SimpleFileSymbo implements DirSymbo {
	private ArrayList<FileSymbo> children = new ArrayList<>();

	public SimpleDirSymbo() {
	}
	@Override
	public int kind() {
		return FileSymbo.KindDir;
	}

	@Override
	public void addChild(FileSymbo one) {
		this.children.add(one);
		one.initParent(this);
	}

	@Override
	public int getChildCount() {
		return children.size();
	}

	@Override
	public FileSymbo getChildAtIndex(int index) {
		return children.get(index);
	}

	@Override
	public void removeChild(FileSymbo one) {
		this.children.remove(one);
	}
}
