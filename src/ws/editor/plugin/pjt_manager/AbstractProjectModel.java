package ws.editor.plugin.pjt_manager;

import ws.editor.common.PluginFeature;
import ws.editor.plugin.ProjectModel;

public abstract class AbstractProjectModel implements ProjectModel {

	@Override
	public int pluginMark() {
		return PluginFeature.IO_TreeModel;
	}

	@Override
	public int upStreamMark() {
		return PluginFeature.IO_NoUpStream;
	}

}
