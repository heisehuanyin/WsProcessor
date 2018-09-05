package ws.editor.plugin.window;

import javax.swing.JFrame;
import javax.swing.JMenuBar;

import ws.editor.common.PluginFeature;
import ws.editor.plugin.ContentView;
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
	
	@Override
	public void service_ResetMenuBar(JMenuBar mbar) {
		this.service_ResetMenuBar2(mbar);
		this.validate();
	}
	
	protected abstract void service_ResetMenuBar2(JMenuBar mbar);
	
	@Override
	public void placeView(String viewTitle, ContentView comp) {
		this.placeView2(viewTitle, comp);
		this.validate();
	}
	protected abstract void placeView2(String viewTitle, ContentView comp);

}
