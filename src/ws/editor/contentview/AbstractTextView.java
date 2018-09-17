package ws.editor.contentview;

import ws.editor.WsProcessor;
import ws.editor.p.PluginFeature;
import ws.editor.p.contentview.ContentView;

public abstract class AbstractTextView implements ContentView{

	private String gid;

	@Override
	public int pluginMark() {
		return PluginFeature.UI_ContentView;
	}

	@Override
	public int upStreamMark() {
		return PluginFeature.IO_TextModel;
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
