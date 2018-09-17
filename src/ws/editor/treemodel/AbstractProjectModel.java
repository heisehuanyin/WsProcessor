package ws.editor.treemodel;

import ws.editor.WsProcessor;
import ws.editor.p.PluginFeature;
import ws.editor.p.filesymbo.FileSymbo;
import ws.editor.p.treemodel.TreeModelFeature;

public abstract class AbstractProjectModel implements TreeModelFeature {

	@Override
	public int pluginMark() {
		return PluginFeature.IO_TreeModel;
	}

	@Override
	public int upStreamMark() {
		return PluginFeature.IO_FileSymbo;
	}

	@Override
	public TreeModelFeature openTreeModel(WsProcessor core, PluginFeature upStream) {
		return this.openProject(core, ((FileSymbo)upStream).getFilePath());
	}
	/**
	 * 打开项目模型*/
	protected abstract TreeModelFeature openProject(WsProcessor core, String pjtPath);


}
