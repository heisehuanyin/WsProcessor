package ws.editor.treemodel;

import ws.editor.WsProcessor;
import ws.editor.x.PluginFeature;
import ws.editor.x.filesymbo.FileSymbo;
import ws.editor.x.treemodel.TreeModel;

public abstract class AbstractProjectModel implements TreeModel {

	@Override
	public int pluginMark() {
		return PluginFeature.IO_TreeModel;
	}

	@Override
	public int upStreamMark() {
		return PluginFeature.IO_FileSymbo;
	}

	@Override
	public TreeModel openTreeModel(WsProcessor core, PluginFeature upStream) {
		return this.openProject(core, ((FileSymbo)upStream).getFilePath());
	}
	/**
	 * 打开项目模型*/
	protected abstract TreeModel openProject(WsProcessor core, String pjtPath);


}
