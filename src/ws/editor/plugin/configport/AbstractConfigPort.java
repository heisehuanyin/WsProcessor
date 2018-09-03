package ws.editor.plugin.configport;

import ws.editor.common.PluginFeature;
import ws.editor.plugin.ConfigPort;
/**
 * ConfigPort 无法被配置，只能加载最终加载的类型*/
public abstract class AbstractConfigPort implements ConfigPort{

	@Override
	public int pluginMark() {
		return PluginFeature.Service_ConfigUnit;
	}
	
	@Override
	public int upStreamMark() {
		return PluginFeature.IO_NoUpStream;
	}

}
