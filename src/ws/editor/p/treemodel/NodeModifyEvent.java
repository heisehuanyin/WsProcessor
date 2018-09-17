package ws.editor.p.treemodel;

public class NodeModifyEvent extends NodeGeneralEvent{
	private String mKey;

	public NodeModifyEvent(String modifiedKey, NodeSymboFeature obj, String exmsg) {
		super(obj, exmsg);
		this.mKey = modifiedKey;
	}

	public String getModifiedKey() {
		return this.mKey;
	}
}
