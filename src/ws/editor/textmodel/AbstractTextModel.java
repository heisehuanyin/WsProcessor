package ws.editor.textmodel;

import ws.editor.x.PluginFeature;
import ws.editor.x.textmodel.TextModel;

public abstract class AbstractTextModel implements TextModel {
	
	@Override
	public int pluginMark() {
		return PluginFeature.IO_TextModel;
	}

	@Override
	public int upStreamMark() {
		return PluginFeature.IO_FileSymbo;
	}
	
}
