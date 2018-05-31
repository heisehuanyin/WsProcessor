package ws.editor;

import javax.swing.JMenuItem;

public interface CompFeature {
	static int MenuBar = 0;
	static int ToolBar = 1;
	static int MainArea = 2;
	static int StatusBar = 3;
	static int TabviewFactory = 4;
	static int ContentProcessor = 5;
	static int ToolsPlugin = 6;
	static int ConfigService = 7;
	static int LogUnit = 8;
	
	//获取组件的简单名称
	public String getCompName();
	//获取组件代码，标定组件类别
	int getCompMark();
	//获取每个组件都拥有的配置菜单
	JMenuItem getCustomMenu();
	//获取实例
	CompFeature getInstance();
}
