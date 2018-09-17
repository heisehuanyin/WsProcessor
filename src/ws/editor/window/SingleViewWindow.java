package ws.editor.window;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

import ws.editor.WsProcessor;
import ws.editor.x.configport.ConfigItemsKey;
import ws.editor.x.contentview.ContentView;
import ws.editor.x.menubar.MenuBar;
import ws.editor.x.window.AbstractWindow;
import ws.editor.x.window.FrontWindow;

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
		String wStr = this.core.instance_GetMainConfigUnit().getValue(ConfigItemsKey.WindowWidth, "800");
		String hStr = this.core.instance_GetMainConfigUnit().getValue(ConfigItemsKey.WindowHeight, "600");
		
		//MenuBar=======================
		this.core.service_Refresh_MenuBar(this);
		
		
		
		
		
		
		this.addComponentListener(new CommonComponentListener(this.core));
		this.addWindowListener(new CommonWindowsListener(this.core));
		
		this.setSize(Integer.parseInt(wStr), Integer.parseInt(hStr));
		this.setVisible(true);
	}
	
	@Override
	protected void placeView2(String viewTitle, ContentView comp) {
		if(this.view != null)
			this.remove(this.view.getView());
		
		this.setTitle("SingleViewWindow - " + this.g_id + " - " + viewTitle);
		this.getContentPane().add(comp.getView(), BorderLayout.CENTER);
		this.view = comp;
	}

	@Override
	protected void service_ResetMenuBar2(JMenuBar mbar) {
		this.setJMenuBar(mbar);
	}

	@Override
	public JMenu getCustomMenu() {
		return new JMenu("Window");
	}

	@Override
	public void saveOperation() {
		System.out.println(".......................");
	}

	@Override
	public ArrayList<? extends ContentView> getActivedViews() {
		ArrayList<ContentView> x = new ArrayList<>();
		
		if(this.view != null)
			x.add(view);
		
		return x;
	}

	@Override
	public String getGroupId() {
		return this.g_id;
	}

	@Override
	public void closeView(ContentView comp) {
		if(this.view == comp)
			this.remove(this.view.getView());
	}


}
