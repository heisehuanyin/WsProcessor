package ws.editor.plugin.textmodel;

import ws.editor.common.PluginFeature;
import ws.editor.plugin.TextModel;

public abstract class AbstractTextModel implements TextModel {
	
	@Override
	public int pluginMark() {
		return PluginFeature.IO_TextModel;
	}

	@Override
	public int upStreamMark() {
		return PluginFeature.IO_NoUpStream;
	}
	
}
