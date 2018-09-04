package ws.editor.plugin;

import ws.editor.WsProcessor;
import ws.editor.common.GroupSymbo;
import ws.editor.common.NodeSymbo;
import ws.editor.common.PluginFeature;

public interface TreeModel extends PluginFeature {
	/**
	 * 获取本模型的节点表示
	 * @return 节点表示实例*/
	NodeSymbo getNodeSymbo();

}
