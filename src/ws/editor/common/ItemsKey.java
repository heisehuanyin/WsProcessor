package ws.editor.common;

import java.io.File;

public class ItemsKey {
	public final static String DefaultProjectPath_Value = "."+File.separator+"project.wspjt";
	public final static String DefaultFileEncoding_Value = "UTF-8";
	public static final String VoidFilePath_Value = "voidfile_wsdifined";

	
	public final static String LogPortConfig = "editor.logport.default";
	
	
	
	public final static String WindowWidth = "editor.window.width";
	public final static String WindowHeight = "editor.window.height";
	
	public final static String DefaultToolsBar = "editor.toolsbar.description";
	public final static String DefaultMenuBar = "editor.menubar.description";
	public final static String DefaultWindow = "editor.window.description";
	public final static String DefaultStatusBar = "editor.statusbar.description";
	
	public final static String DefaultLocalPort = "editor.binaryport.description";
	public final static String DefaultNetworkPort = "editor.networkport.description";
	
	
	
	/**
	 * 根据传入的文件格式后缀，动态生成属性键，用于之后从属性文件中获取ProjectManager配置
	 * @param formatStr 格式后缀名
	 * @return 配置属性索引键名*/
	public static String getKey_ProjectManager_FOR(String formatStr) {
		return "editor.projectparse.for."+formatStr;
	}
	
	/**
	 * 根据传入的文件格式后缀，动态生成属性键，用于之后从属性文件中获取ContentProcess序列串
	 * @param formatStr 格式后缀名
	 * @return 配置属性链索引键名*/
	public static String getKey_ContentProcessList_For(String formatStr) {
		return "editor.contentprocesslist.for."+formatStr;
	}
	
	
	public static String getPosition_WindowActiveViewManager(String windowID, String viewID) {
		return "editor."+windowID+"."+ viewID + ".position";
	}
}
