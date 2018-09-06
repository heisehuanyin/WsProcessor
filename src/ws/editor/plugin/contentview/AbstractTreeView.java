package ws.editor.plugin.contentview;

import ws.editor.comn.PluginFeature;
import ws.editor.comn.event.NodeEventListener;
import ws.editor.plugin.ContentView;

public abstract class AbstractTreeView implements ContentView , NodeEventListener{

	@Override
	public int pluginMark() {
		return PluginFeature.UI_ContentView;
	}

	@Override
	public int upStreamMark() {
		return PluginFeature.IO_TreeModel;
	}

}
