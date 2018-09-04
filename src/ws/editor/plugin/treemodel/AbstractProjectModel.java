package ws.editor.plugin.treemodel;

import ws.editor.common.PluginFeature;
import ws.editor.plugin.TreeModel;

public abstract class AbstractProjectModel implements TreeModel {

	@Override
	public int pluginMark() {
		return PluginFeature.IO_TreeModel;
	}

	@Override
	public int upStreamMark() {
		return PluginFeature.IO_NoUpStream;
	}
	
	

}
