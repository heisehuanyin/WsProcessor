package ws.editor.comn.event;

import ws.editor.plugin.Tree_NodeSymbo;

public class NodeModifyEvent extends NodeGeneralEvent{
	private String mKey;

	public NodeModifyEvent(String modifiedKey, Tree_NodeSymbo obj, String exmsg) {
		super(obj, exmsg);
		this.mKey = modifiedKey;
	}

	public String getModifiedKey() {
		return this.mKey;
	}
}
