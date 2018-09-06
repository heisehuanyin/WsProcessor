package ws.editor.plugin;

import java.awt.Component;

import ws.editor.WsProcessor;
import ws.editor.comn.PluginFeature;

public interface ContentView extends PluginFeature{
	/**
	 * 获取一个新的实例
	 * @param core 核心模块
	 * @param upStream 上游模块*/
	ContentView openContentView(WsProcessor core, PluginFeature upStream);
	
	/**
	 * */
	Component getView();
}
