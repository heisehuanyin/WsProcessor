package ws.editor.plugin.binarymodel;

import ws.editor.common.PluginFeature;
import ws.editor.plugin.BinaryModel;

public abstract class AbstractBinaryModel implements BinaryModel{

	@Override
	public int pluginMark() {
		return PluginFeature.IO_BinaryModel;
	}

	@Override
	public int upStreamMark() {
		return PluginFeature.IO_NoUpStream;
	}
	
}
