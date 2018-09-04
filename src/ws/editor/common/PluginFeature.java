package ws.editor.common;

import javax.swing.JMenu;

import ws.editor.WsProcessor;
/**
 * 所有插件的共同接口，插件的实例既可以作为插件实体，也可以作为工厂类
 * 注册插件的时候使用的实例，被软件作为工厂类使用，获取新的实例
 * */
public interface PluginFeature {
	final static int UI_MenuBar = 0x10;
	final static int UI_ToolBar = 0x11;
	final static int UI_Window = 0x12;
	final static int UI_StatusBar = 0x13;
	final static int UI_Tabview = 0x14;
	
	/**
	 * 插件内部标识码
	 * 与源文件接触的第一个插件返回的上游代码就是 {@link #IO_NoUpStream} 
	 * */
	final static int IO_NoUpStream=0x24;
	final static int IO_TextModel = 0x21;
	final static int IO_TreeModel = 0x22;
	final static int IO_StyleModel = 0x23;
	
	final static int Tools_Plugin = 0x30;
	final static int Tools_Convert = 0x31;
	
	final static int Service_ConfigPort = 0x40;
	final static int Service_LogPort = 0x41;
	
	/**
	 * 获取组件代码，标定组件类别，详细定义看{@link PluginFeature}
	 * @return 返回的组件标记代码*/
	int pluginMark();
	/**
	 * 获取本插件上游插件类型
	 * @return 返回的上游插件类型*/
	int upStreamMark();
	/**
	 * 获取每个组件都拥有的配置菜单
	 * @return 返回每个组件自定义的定制菜单，供上级调用*/
	JMenu getCustomMenu();
	/**
	 * 组件自带保存操作，需要保存过程的组件需要实现*/
	void saveOperation();
}
