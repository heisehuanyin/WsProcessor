package ws.editor.configport;

import ws.editor.comn.PluginFeature;
import ws.editor.p.ConfigPort;
/**
 * ConfigPort 无法被配置，只能加载最终加载的类型*/
public abstract class AbstractConfigPort implements ConfigPort{

	@Override
	public int pluginMark() {
		return PluginFeature.Service_ConfigPort;
	}
	
	@Override
	public int upStreamMark() {
		return PluginFeature.IO_NoUpStream;
	}

}
