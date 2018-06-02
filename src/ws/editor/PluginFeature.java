package ws.editor;

import javax.swing.JMenuItem;
/**
 * 所有插件的共同接口，插件的实例既可以作为插件实体，也可以作为工厂类
 * 注册插件的时候使用的实例，被软件作为工厂类使用，获取新的实例
 * */
public interface PluginFeature {
	static int UI_MenuBar = 0;
	static int UI_ToolBar = 1;
	static int UI_Window = 2;
	static int UI_StatusBar = 3;
	static int UI_Tabview = 4;
	static int IO_ContentProcess = 5;
	static int IO_ChannelPort = 6;
	static int Tools_Plugin = 7;
	static int Service_ConfigUnit = 8;
	static int Service_LogPort = 9;
	static int Service_ProjectManage = 10;
	
	/**
	 * 获取组件代码，标定组件类别
	 * @return 返回的组件标记代码*/
	int getCompMark();
	/**
	 * 获取组件id
	 * @return 返回的组件id*/
	String getCompid();
	/**
	 * 获取每个组件都拥有的配置菜单
	 * @return 返回每个组件自定义的定制菜单，供上级调用*/
	JMenuItem getCustomMenu();
	/**
	 * 获取一个默认新实例，然而每个类拥有自己的独特的构造方法
	 * @param schedule 中心调度组件，需要什么自己获取
	 * @return 新实例*/
	PluginFeature getDefaultInstance(WsProcessor schedule);
	/**
	 * 组件自带保存操作，需要保存过程的组件需要实现*/
	void saveOperation();
}
