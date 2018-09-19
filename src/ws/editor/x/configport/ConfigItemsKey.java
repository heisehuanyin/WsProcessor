package ws.editor.x.configport;

import ws.editor.x.PluginFeature;
/**
 * 软件拥有很多可配置项目，为了便于统一，将配置文件中的键名集中起来*/
public class ConfigItemsKey {
	public final static String DefaultTextProcList = "ws.editor.filesymbo.DefaultFileSymbo=>" + "ws.editor.textmodel.DefaultTextModel=>"
			+ "ws.editor.contentview.DefaultTextView";
	
	
	public final static String WindowWidth = "editor.window.width";
	public final static String WindowHeight = "editor.window.height";
	
	public final static String DefaultToolsBar = "editor.toolsbar.description";
	public final static String DefaultMenuBar = "editor.menubar.description";
	public final static String DefaultWindow = "editor.window.description";
	public final static String DefaultStatusBar = "editor.statusbar.description";
	
	public final static String DefaultLocalPort = "editor.binaryport.description";
	public final static String DefaultNetworkPort = "editor.networkport.description";
	
	public static String get_MODULELIST_AS_SUFFIX(String suffix) {
		return "editor.modulelist.suffix" + suffix;
	}

	public static String get_CUSTOMSETTING_STRING(PluginFeature plugin, String string) {
		return "editor.module.custom_setting." + plugin.getClass().getName()+"."+ string;
	}
}
