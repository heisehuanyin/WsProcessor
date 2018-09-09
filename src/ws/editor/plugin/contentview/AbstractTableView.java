package ws.editor.plugin.contentview;

import java.awt.Component;

import javax.swing.JMenu;

import ws.editor.WsProcessor;
import ws.editor.comn.PluginFeature;
import ws.editor.plugin.ContentView;

public abstract class AbstractTableView implements ContentView {

	@Override
	public int pluginMark() {
		return PluginFeature.UI_ContentView;
	}

	@Override
	public int upStreamMark() {
		return PluginFeature.IO_TableModel;
	}

}
