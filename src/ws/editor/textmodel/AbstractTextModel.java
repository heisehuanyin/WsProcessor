package ws.editor.textmodel;

import ws.editor.p.PluginFeature;
import ws.editor.p.textmodel.TextModel;

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
