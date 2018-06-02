package ws.editor.plugin;

import ws.editor.PluginFeature;
import ws.editor.WsProcessor;

public interface FrontWindow extends PluginFeature{

	/**
	 * 获取一个窗口实例
	 * @param schedule 主调度器，用于连接各种组件
	 * @param id 窗口的唯一标识
	 * @return 返回的实例*/
	FrontWindow getInstance(WsProcessor schedule, String id);

	/**
	 * 显示这个窗口实例*/
	void displayWindow();

}
