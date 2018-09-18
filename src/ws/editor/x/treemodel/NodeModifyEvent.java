package ws.editor.x.treemodel;

public class NodeModifyEvent extends NodeGeneralEvent{
	private String mKey;

	public NodeModifyEvent(String modifiedKey, FeatureNodeSymbo obj, String exmsg) {
		super(obj, exmsg);
		this.mKey = modifiedKey;
	}

	public String getModifiedKey() {
		return this.mKey;
	}
}