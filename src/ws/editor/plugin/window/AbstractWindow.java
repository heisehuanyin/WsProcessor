package ws.editor.plugin.window;

import javax.swing.JFrame;

import ws.editor.common.PluginFeature;
import ws.editor.plugin.FrontWindow;

public abstract class AbstractWindow extends JFrame implements FrontWindow{

	@Override
	public int pluginMark() {
		return PluginFeature.UI_Window;
	}

	@Override
	public int upStreamMark() {
		return PluginFeature.IO_NoUpStream;
	}
}
