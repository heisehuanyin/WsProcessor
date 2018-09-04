package ws.editor.plugin;

import ws.editor.WsProcessor;
import ws.editor.common.PluginFeature;

public interface ContentView extends PluginFeature{
	ContentView openContentView(WsProcessor core, PluginFeature upStream);
	
}
