package ws.editor.contentview;

import java.awt.Component;

import javax.swing.JMenu;

import ws.editor.WsProcessor;
import ws.editor.x.PluginFeature;
import ws.editor.x.contentview.ContentView;

public abstract class AbstractTableView implements ContentView {
	private String gid;
	
	@Override
	public int pluginMark() {
		return PluginFeature.UI_ContentView;
	}

	@Override
	public int upStreamMark() {
		return PluginFeature.IO_TableModel;
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
