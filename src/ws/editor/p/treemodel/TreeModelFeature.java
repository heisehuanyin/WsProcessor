package ws.editor.p.treemodel;

import ws.editor.WsProcessor;
import ws.editor.p.PluginFeature;

public interface TreeModelFeature extends PluginFeature {
	
	/**
	 * 打开新的{@link TreeModelFeature}
	 * @param core 核心模块
	 * @param upStream 上游模型
	 * @return 返回的实例模型*/
	TreeModelFeature openTreeModel(WsProcessor core, PluginFeature upStream);
	
	/**
	 * 获取本模型的节点表示
	 * @return 节点表示实例*/
	NodeSymboFeature getNodeSymbo();

}
