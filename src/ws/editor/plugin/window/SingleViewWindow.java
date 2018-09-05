package ws.editor.plugin.window;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

import ws.editor.WsProcessor;
import ws.editor.plugin.ContentView;
import ws.editor.plugin.FrontWindow;

public class SingleViewWindow extends AbstractWindow{
	private WsProcessor core;
	private String g_id;
	private Component view ;

	@Override
	public FrontWindow openWindow(WsProcessor schedule, String gId) {
		SingleViewWindow x = new SingleViewWindow();
		x.core = schedule;
		x.g_id = gId;
		
		x.customWindow();
		
		return x;
	}

	private void customWindow() {
		this.setTitle("SingleViewWindow - " + this.g_id + " ");
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setSize(800, 600);
		this.setVisible(true);
	}
	
	@Override
	public void placeView(String viewTitle, Component comp) {
		this.setTitle("SingleViewWindow - " + this.g_id + " " + viewTitle);
		this.add(comp, BorderLayout.CENTER);
		this.view = comp;
	}

	@Override
	public void service_ResetMenuBar(JMenuBar mbar) {
		this.setJMenuBar(mbar);
	}

	@Override
	public JMenu getCustomMenu() {
		return new JMenu(this.getClass().getName());
	}

	@Override
	public void saveOperation() {
		
	}

	@Override
	public ArrayList<? extends JMenu> getActivedViewsMenus() {
		ArrayList<JMenu> x = new ArrayList<>();
		x.add(((ContentView)this.view).getCustomMenu()); 
		return x;
	}
}
