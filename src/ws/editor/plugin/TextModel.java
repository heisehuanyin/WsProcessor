package ws.editor.plugin;

import ws.editor.WsProcessor;
import ws.editor.common.PluginFeature;

public interface TextModel extends PluginFeature {
	/**
	 * 获取内容，构建一个数据模型
	 * @param core TODO
	 * @param filePath 上游插件类型
	 * @return 数据模型*/
	TextModel openTextModel(WsProcessor core, String filePath);
	
	/**
	 * 获取当前行号
	 * @return 行号*/
	int getRowsIndex();
	
	/**
	 * 获取下一行
	 * @return 下一行内容*/
	String nextLine();
	
	/**
	 * 获取指定一行
	 * @param index 行序
	 * @return 指定行内容*/
	String getLine(int index);

	/**
	 * 更新指定行号的内容
	 * @param index 指名行序号
	 * @param str 指定内容*/
	void updateLine(int index, String str);

	
}
