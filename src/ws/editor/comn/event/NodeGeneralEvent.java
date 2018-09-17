package ws.editor.comn.event;

import java.util.ArrayList;

import ws.editor.plugin.Tree_NodeSymbo;

public class NodeGeneralEvent {

	private Tree_NodeSymbo source;
	private String msg;
	private ArrayList<Tree_NodeSymbo> ePath = new ArrayList<>();

	public NodeGeneralEvent(Tree_NodeSymbo obj, String msg) {
		this.source = obj;
		this.msg = msg;
	}

	public Tree_NodeSymbo getSource() {
		return this.source;
	}

	public String getMsg() {
		return this.msg;
	}

	public void recordTreePath(Tree_NodeSymbo obj) {
		this.ePath .add(0, obj);
	}

	public boolean nodeContain(Tree_NodeSymbo obj) {
		return this.ePath.contains(obj);
	}

}