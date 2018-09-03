package ws.editor.plugin.window;

import java.awt.Component;
import java.util.Collection;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

import ws.editor.WsProcessor;
import ws.editor.common.PluginFeature;
import ws.editor.plugin.FrontWindow;

public abstract class AbstractFrontWindow extends JFrame implements FrontWindow{

	/**
	 * 获取一个窗口实例
	 * @param schedule 主调度器，用于连接各种组件
	 * @return 返回的实例*/
	@Override
	public abstract FrontWindow getInstance(WsProcessor schedule, String gId);

	/**
	 * 显示这个窗口实例*/
	@Override
	public abstract void displayWindow();

	/**
	 * 将激活视图放置在本窗体上
	 * @param viewTitle 视图的标题，但不一定是标签页的标签
	 * @param comp 主视图*/
	@Override
	public abstract void placeView(String viewTitle, Component comp);

	/**
	 * 刷新MenuBar，将额外菜单添加到菜单栏
	 * @param mbar 额外的菜单集合*/
	@Override
	public abstract void service_ResetMenuBar(JMenuBar mbar);

	@Override
	public int pluginMark() {
		return PluginFeature.UI_Window;
	}

	@Override
	public int upStreamMark() {
		return PluginFeature.IO_NoUpStream;
	}

	public Collection<? extends JMenu> getActivedViewsMenus() {
		// TODO Auto-generated method stub
		return null;
	}

}
