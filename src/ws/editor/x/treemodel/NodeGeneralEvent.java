package ws.editor.x.treemodel;

import java.util.ArrayList;

import ws.editor.x.PluginEvent;

public class NodeGeneralEvent implements PluginEvent{

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