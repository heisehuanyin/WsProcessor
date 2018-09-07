package ws.editor.plugin;

import java.awt.Component;

import ws.editor.WsProcessor;
import ws.editor.comn.PluginFeature;

public interface ContentView extends PluginFeature{
	/**
	 * 获取一个新的实例
	 * @param core 核心模块
	 * @param upStream 上游模块
	 * @return 返回的视图实例*/
	ContentView openContentView(WsProcessor core, PluginFeature upStream);
	
	/**
	 * 返回一个实际工作的swing组件
	 * @return swing组件*/
	Component getView();
}
