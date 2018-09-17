package ws._testcast;

import ws.editor.WsProcessor;
import ws.editor.p.FrontWindow;
import ws.editor.window.TwoViewWindow;

public class WindowTest {
	public static void main(String[] args) {
		WsProcessor p = new WsProcessor();
		p.control_OpenGraphicMode("gid");
		FrontWindow w = new TwoViewWindow().openWindow(p, "gid");
		System.out.println(w.getGroupId());
		System.out.println(w.pluginMark());
		System.out.println(w.upStreamMark());
	}
}
