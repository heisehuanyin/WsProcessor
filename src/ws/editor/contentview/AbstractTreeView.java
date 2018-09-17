package ws.editor.contentview;

import ws.editor.WsProcessor;
import ws.editor.comn.PluginFeature;
import ws.editor.p.ContentView;
import ws.editor.p.treemodel.NodeEventListener;

public abstract class AbstractTreeView implements ContentView , NodeEventListener{

	private String gid;

	@Override
	public int pluginMark() {
		return PluginFeature.UI_ContentView;
	}

	@Override
	public int upStreamMark() {
		return PluginFeature.IO_TreeModel;
	}

	@Override
	public void __setGroupId(String gId) {
		this.gid = gId;
	}
	
	@Override
	public String getGroupId() {
		return this.gid;
	}
}
