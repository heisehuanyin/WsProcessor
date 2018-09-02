package ws.editor.plugin;

import ws.editor.common.PluginFeature;
/**
 * ConfigUnit虽然也被设计成为插件模式，但是软件本身只需要一种配置文件格式，
 * 因此，不需要多种实例共存，软件中只保留一个config插件，factory类id恒为ConfigUnit.class.getName()*/
public abstract class ConfigPort implements PluginFeature{

	@Override
	public int pluginMark() {
		return PluginFeature.Service_ConfigUnit;
	}
	
	@Override
	public int upStreamMark() {
		return PluginFeature.IO_NoUpStream;
	}

	
	/**
	 * 获取一个新实例，该实例与本实例没有关系
	 * @param path 配置文件位置
	 * @return 返回新的实例
	 * */
	public abstract ConfigPort createNewPort(String path);

	/**
	 * 设置一个配置属性
	 * @param key 键
	 * @param value 值*/
	public abstract void setKeyValue(String key, String value);

	/**
	 * 获取一个配置属性,如果获取不到指定键值，那么就返回默认属性，并将默认属性写入配置文件
	 * @param key 通过此键获取属性
	 * @param defaultValue 默认属性
	 * @return 返回的配置属性*/
	public abstract String getValue(String key, String defaultValue);

}
