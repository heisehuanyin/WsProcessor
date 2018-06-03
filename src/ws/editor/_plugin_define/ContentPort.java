package ws.editor._plugin_define;

import java.io.InputStream;

import ws.editor.PluginFeature;
import ws.editor.schedule.WsProcessor;

public interface ContentPort extends PluginFeature{
	
	/**
	 * 创建一个实例，创建磁盘文件
	 * @param sch 软件控制核心实例
	 * @param f_path 文件路径
	 * @return 返回插件实例*/
	ContentPort createNewFile(WsProcessor sch, String f_path);
	/**
	 * 获取一个二进制的内容接口
	 * @return 返回InputStream是一个二进制的接口*/
	InputStream getBinaryPort();
	/**
	 * 创建一个实例，连接磁盘文件
	 * @param sch 软件控制核心实例
	 * @param f_path 文件路径
	 * @return 返回插件实例*/
	ContentPort openExistsFile(WsProcessor sch, String f_path);
}
