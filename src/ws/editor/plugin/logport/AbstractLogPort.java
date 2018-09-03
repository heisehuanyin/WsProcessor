package ws.editor.plugin.logport;

import ws.editor.common.PluginFeature;
import ws.editor.plugin.LogPort;
/**
 * 用于输出log到文件中
 * LogPort虽然也被设计成为插件模式，但是软件本身只需要一种log文件格式，可以利用配置文件进行配置*/
public abstract class AbstractLogPort  implements LogPort{

	@Override
	public int pluginMark() {
		return PluginFeature.Service_LogPort;
	}

	@Override
	public int upStreamMark() {
		return PluginFeature.IO_NoUpStream;
	}

	
	/**
	 * 有参构造器用于构建定制实例,利用已有实例构建出新实例，不会初始化已有实例
	 * @param path 实际地址作为唯一参数
	 * @return 文件路径合法返回新实例，不合法返回null
	 */
	@Override
	public abstract LogPort createNewPort(String path);

	/**
	 * 写log信息接口,每次调用接口，将log信息按照规定格式写入log文件
	 * @param obj 写信息的对象
	 * @param msg 具体信息*/
	@Override
	public abstract void writeLog(Object obj, String msg);
	
	/**
	 * 写errorlog到文件，格式化错误信息
	 * @param obj 写信息的对象
	 * @param msg 具体信息*/
	@Override
	public abstract void errorLog(Object obj, String msg);

}
