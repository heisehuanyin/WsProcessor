package ws.editor.plugin;

import java.awt.Component;

import javax.swing.JMenuBar;

import ws.editor.WsProcessor;
import ws.editor.common.PluginFeature;

public interface FrontWindow extends PluginFeature{

	/**
	 * 获取一个窗口实例
	 * @param schedule 主调度器，用于连接各种组件
	 * @param gId groupId，由窗口自动生成的插件全部集中与一条 "分组/通道" 中
	 * @return 返回的实例*/
	FrontWindow getInstance(WsProcessor schedule, String gId);

	/**
	 * 显示这个窗口实例*/
	void displayWindow();

	/**
	 * 将激活视图放置在本窗体上
	 * @param viewTitle 视图的标题，但不一定是标签页的标签
	 * @param comp 主视图*/
	void placeView(String viewTitle, Component comp);

	/**
	 * 刷新MenuBar，将额外菜单添加到菜单栏
	 * @param mbar 额外的菜单集合*/
	void service_ResetMenuBar(JMenuBar mbar);
}
