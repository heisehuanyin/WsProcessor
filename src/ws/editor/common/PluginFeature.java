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
	 * 该模型特殊功能*/
	final static int IO_BinaryModel = 0x20;
	final static int IO_TextModel = 0x21;
	final static int IO_TreeModel = 0x22;
	final static int IO_StyleModel = 0x23;
	
	final static int Tools_Plugin = 0x30;
	final static int Tools_Convert = 0x31;
	
	final static int Service_ConfigUnit = 0x40;
	final static int Service_LogPort = 0x41;
	final static int Service_ProjectManage = 0x42;
	
	/**
	 * 获取组件代码，标定组件类别，详细定义看{@link PluginFeature}
	 * @return 返回的组件标记代码*/
	int getPluginMark();
	/**
	 * 获取组件id,组件id由本插件拓展的端口名称和字符串组成。例如PluginFeature.class.getName()+Str。
	 * 只传入Str，在获取插件的接口中重组id变为可能。
	 * @return 返回的组件id*/
	String getCompid();
	/**
	 * 获取每个组件都拥有的配置菜单
	 * @return 返回每个组件自定义的定制菜单，供上级调用*/
	JMenu getCustomMenu();
	/**
	 * 组件自带保存操作，需要保存过程的组件需要实现*/
	void saveOperation();
}
