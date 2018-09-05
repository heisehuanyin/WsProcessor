package ws.editor.plugin.contentview;

import ws.editor.common.PluginFeature;
import ws.editor.plugin.ContentView;

public abstract class AbstractTextView implements ContentView{

	@Override
	public int pluginMark() {
		return PluginFeature.UI_ContentView;
	}

	@Override
	public int upStreamMark() {
		return PluginFeature.IO_TextModel;
	}


}
