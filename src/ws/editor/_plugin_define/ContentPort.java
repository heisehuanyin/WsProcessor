package ws.editor._plugin_define;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import ws.editor.PluginFeature;
import ws.editor.schedule.WsProcessor;

public interface ContentPort extends PluginFeature{
	public final int NetworkPort = 0;
	public final int LocalFilePort = 1;
	
	/**
	 * 获取此接口种类:ContentPort接口定义*/
	int getContentPortType();
	/**
	 * 创建一个实例，创建磁盘文件，如果文件已经存在，换一个名称创建新文件
	 * @param sch 软件控制核心实例
	 * @param f_path 文件路径
	 * @return 返回插件实例*/
	ContentPort createNewFile(WsProcessor sch, String f_path);
	/**
	 * 获取一个二进制的输入内容接口
	 * @return 返回InputStream是一个二进制的接口*/
	InputStream getInputBinaryPort();
	/**
	 * 获取一个二进制的输出内容接口
	 * @return 返回OutputStream是一个二进制的接口*/
	OutputStream getOutputBinaryPort();
	/**
	 * 创建一个实例，连接磁盘文件,如果文件不存在，返回null
	 * @param sch 软件控制核心实例
	 * @param f_path 文件路径
	 * @return 返回插件实例*/
	ContentPort openExistsFile(WsProcessor sch, String f_path);
	
	/**
	 * 获取指向目标文件的路径
	 * @return 目标URL*/
	String getPath();
}
