package ws.editor.plugin;

import java.io.InputStream;
import java.io.OutputStream;

import ws.editor.WsProcessor;
import ws.editor.common.PluginFeature;

public abstract class BinaryPort implements PluginFeature{
	
	private String file_id;

	public BinaryPort() {
		this.file_id = BinaryPort.class.getName();
	}
	
	protected BinaryPort(String idstr) {
		this.file_id = idstr;
	}

	@Override
	public int getPluginMark() {
		return PluginFeature.IO_BinaryPort;
	}

	@Override
	public String getCompid() {
		return BinaryPort.class.getName() + this.file_id;
	}

	/**
	 * 创建一个实例，创建磁盘文件，如果文件已经存在，换一个名称创建新文件
	 * @param sch 软件控制核心实例
	 * @param f_path 文件路径
	 * @return 返回插件实例*/
	public abstract BinaryPort createNewFile(WsProcessor sch, String f_path);
	
	/**
	 * 查询文件是否存在，存在返回true，不存在返回false
	 * @return 测试结果*/
	public abstract boolean FileIsExists();
	
	/**
	 * 创建一个实例，连接磁盘文件,如果文件不存在，返回null
	 * @param sch 软件控制核心实例
	 * @param f_path 文件路径
	 * @return 返回插件实例*/
	public abstract BinaryPort openExistsFile(WsProcessor sch, String f_path);
	/**
	 * 获取一个二进制的输入内容接口
	 * @return 返回InputStream是一个二进制的接口*/
	public abstract InputStream getInputBinaryPort();
	/**
	 * 获取一个二进制的输出内容接口
	 * @return 返回OutputStream是一个二进制的接口*/
	public abstract OutputStream getOutputBinaryPort();
	
	/**
	 * 获取指向目标文件的路径
	 * @return 目标URL*/
	public abstract String getPath();
}
