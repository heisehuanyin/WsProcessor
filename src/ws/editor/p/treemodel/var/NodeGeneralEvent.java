package ws.editor.p.treemodel.var;

import java.util.ArrayList;

public class NodeGeneralEvent {

	private FeatureNodeSymbo source;
	private String msg;
	private ArrayList<FeatureNodeSymbo> ePath = new ArrayList<>();

	public NodeGeneralEvent(FeatureNodeSymbo obj, String msg) {
		this.source = obj;
		this.msg = msg;
	}

	public FeatureNodeSymbo getSource() {
		return this.source;
	}

	public String getMsg() {
		return this.msg;
	}

	public void recordTreePath(FeatureNodeSymbo obj) {
		this.ePath .add(0, obj);
	}

	public boolean nodeContain(FeatureNodeSymbo obj) {
		return this.ePath.contains(obj);
	}

}