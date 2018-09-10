package ws.editor.plugin.window;

import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

import ws.editor.WsProcessor;
import ws.editor.comn.ItemsKey;
import ws.editor.plugin.ContentView;
import ws.editor.plugin.FrontWindow;

public class TwoViewWindow extends AbstractWindow{
	private String gId;
	private WsProcessor core;
	private ContentView leftV = null;
	private ContentView rightV = null;
	private JTabbedPane leftC = new JTabbedPane(JTabbedPane.SCROLL_TAB_LAYOUT);
	private JTabbedPane rightC= new JTabbedPane(JTabbedPane.SCROLL_TAB_LAYOUT);
	private JSplitPane jsp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftC, rightC);

	@Override
	public FrontWindow openWindow(WsProcessor schedule, String gId) {
		TwoViewWindow x = new TwoViewWindow();
		x.core = schedule;
		x.gId = gId;
		
		x.customWindow();
		
		return x;
	}

	private void customWindow() {
		this.setTitle("TwoViewWindow - " + this.gId + " ");
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		String wStr = this.core.instance_GetMainConfigUnit().getValue(ItemsKey.WindowWidth, "800");
		String hStr = this.core.instance_GetMainConfigUnit().getValue(ItemsKey.WindowHeight, "600");
		
		//MenuBar=======================
		this.core.service_Refresh_MenuBar(this);
		this.getContentPane().add(jsp,BorderLayout.CENTER);
		
		
		
		
		
		this.addComponentListener(new BridgeWindowListener(this.core));
		
		this.setSize(Integer.parseInt(wStr), Integer.parseInt(hStr));
		this.setVisible(true);
	}

	@Override
	protected void placeView2(String viewTitle, ContentView comp) {
		
		
	}
	
	@Override
	public void saveOperation() {
		
	}

	@Override
	public String getGroupId() {
		return this.gId;
	}

	@Override
	public ArrayList<? extends JMenu> getActivedViewsMenus() {
		ArrayList<JMenu> list = new ArrayList<>();
		
		if(this.leftV != null)
			list.add(this.leftV.getCustomMenu());
		
		if(this.rightV != null)
			list.add(this.rightV.getCustomMenu());
		
		return list;
	}

	@Override
	public JMenu getCustomMenu() {
		return new JMenu("Window");
	}

	@Override
	protected void service_ResetMenuBar2(JMenuBar mbar) {
		this.setJMenuBar(mbar);
	}


}
