package ws.editor.plugin;

import ws.editor.WsProcessor;
import ws.editor.common.GroupSymbo;
import ws.editor.common.NodeSymbo;
import ws.editor.common.PluginFeature;

public interface TreeModel extends PluginFeature {
	
	/**
	 * 打开新的{@link TreeModel}
	 * @param core 核心模块
	 * @param upStream 上游模型
	 * @return 返回的实例模型*/
	TreeModel openTreeModel(WsProcessor core, PluginFeature upStream);
	
	/**
	 * 获取本模型的节点表示
	 * @return 节点表示实例*/
	NodeSymbo getNodeSymbo();

}
