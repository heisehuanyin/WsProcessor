package ws.editor.p.treemodel;

import java.util.ArrayList;

public class NodeGeneralEvent {

	private NodeSymboFeature source;
	private String msg;
	private ArrayList<NodeSymboFeature> ePath = new ArrayList<>();

	public NodeGeneralEvent(NodeSymboFeature obj, String msg) {
		this.source = obj;
		this.msg = msg;
	}

	public NodeSymboFeature getSource() {
		return this.source;
	}

	public String getMsg() {
		return this.msg;
	}

	public void recordTreePath(NodeSymboFeature obj) {
		this.ePath .add(0, obj);
	}

	public boolean nodeContain(NodeSymboFeature obj) {
		return this.ePath.contains(obj);
	}

}