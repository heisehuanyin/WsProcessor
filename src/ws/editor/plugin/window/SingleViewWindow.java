package ws.editor.plugin.window;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

import ws.editor.WsProcessor;
import ws.editor.common.ItemsKey;
import ws.editor.plugin.ContentView;
import ws.editor.plugin.FrontWindow;
import ws.editor.plugin.MenuBar;

public class SingleViewWindow extends AbstractWindow{
	private WsProcessor core;
	private String g_id;
	private ContentView view ;

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
		String wStr = this.core.instance_GetMainConfigUnit().getValue(ItemsKey.WindowWidth, "800");
		String hStr = this.core.instance_GetMainConfigUnit().getValue(ItemsKey.WindowHeight, "600");
		
		//MenuBar=======================
		this.core.service_Refresh_MenuBar(this);
		
		
		
		
		
		
		this.addComponentListener(new BridgeWindowListener(this.core));
		
		this.setSize(Integer.parseInt(wStr), Integer.parseInt(hStr));
		this.setVisible(true);
	}
	
	@Override
	public void placeView(String viewTitle, ContentView comp) {
		this.setTitle("SingleViewWindow - " + this.g_id + " - " + viewTitle);
		this.getContentPane().add(comp.getView(), BorderLayout.CENTER);
		this.view = comp;
		this.validate();
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
		if(this.view!=null)
			x.add(((ContentView)this.view).getCustomMenu()); 
		return x;
	}

	@Override
	public String getGroupId() {
		return this.g_id;
	}
}

class BridgeWindowListener implements ComponentListener{
	private WsProcessor core;
	
	public BridgeWindowListener(WsProcessor core) {
		this.core = core;
	}
	
	@Override
	public void componentResized(ComponentEvent e) {
		JFrame win = (JFrame) e.getSource();
		int w = win.getWidth();
		int h = win.getHeight();
		this.core.instance_GetMainConfigUnit().setKeyValue(ItemsKey.WindowWidth, ""+w);
		this.core.instance_GetMainConfigUnit().setKeyValue(ItemsKey.WindowHeight, ""+h);
	}

	@Override
	public void componentMoved(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentShown(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentHidden(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}
