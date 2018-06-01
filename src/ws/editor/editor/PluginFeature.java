package ws.editor.editor;

import javax.swing.JMenuItem;

public interface PluginFeature {
	static int UI_MenuBar = 0;
	static int UI_ToolBar = 1;
	static int UI_MainArea = 2;
	static int UI_StatusBar = 3;
	static int UI_TabviewFactory = 4;
	static int IO_ContentProcess = 5;
	static int IO_ChannelPort = 9;
	static int Tools_Plugin = 6;
	static int Service_Config = 7;
	static int Service_LogWriter = 8;
	
	/**
	 * 获取组件代码，标定组件类别
	 * @return 返回组件标记代码*/
	int getCompMark();
	/**
	 * 获取每个组件都拥有的配置菜单
	 * @return 返回每个组件自定义的定制菜单，供上级调用*/
	JMenuItem getCustomMenu();
	/**
	 * 获取一个默认新实例，然而每个类拥有自己的独特的构造方法
	 * @return 新实例*/
	PluginFeature getDefaultInstance();
	/**
	 * 组件自带保存操作，需要保存过程的组件需要实现*/
	void saveOperation();
}
