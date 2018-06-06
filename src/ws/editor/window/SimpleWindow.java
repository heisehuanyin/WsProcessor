package ws.editor.window;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JToolBar;

import ws.editor.ConfigItems;
import ws.editor.PluginFeature;
import ws.editor._plugin_define.FrontWindow;
import ws.editor._plugin_define.TabView;
import ws.editor.schedule.WsProcessor;

public class SimpleWindow implements FrontWindow {
	private String w_id = this.getClass().getName();
	private WsProcessor schedule = null;
	private JFrame window = new JFrame();
	private JMenuBar menubar = new JMenuBar();
	private JToolBar toolsbar = new JToolBar();
	private TabView mainview = null;

	@Override
	public int getPluginMark() {
		return PluginFeature.UI_Window;
	}

	@Override
	public String getCompid() {
		return FrontWindow.class.getName()+w_id;
	}

	@Override
	public JMenu getCustomMenu() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveOperation() {
		// TODO Auto-generated method stub

	}

	@Override
	public FrontWindow getInstance(WsProcessor schedule, String id) {
		SimpleWindow rtn = new SimpleWindow();
		rtn.w_id = id;
		rtn.schedule = schedule;
		SrcWindowListener2 ear = new SrcWindowListener2(rtn);
		rtn.window.addWindowListener(ear);
		rtn.window.addComponentListener(ear);
		return rtn;
	}
	class SrcWindowListener2 implements WindowListener, ComponentListener{
		private SimpleWindow src = null;
		public SrcWindowListener2(SimpleWindow srcobj) {
			this.src = srcobj;
		}

		@Override
		public void windowOpened(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowClosing(WindowEvent e) {
			// TODO Closing can operate
		}

		@Override
		public void windowClosed(WindowEvent e) {
			src.schedule.operate_SaveOperation();
		}

		@Override
		public void windowIconified(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowDeiconified(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowActivated(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowDeactivated(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void componentResized(ComponentEvent e) {
			src.schedule.service_GetMainConfigUnit().setKeyValue(ConfigItems.WindowWidth, 
					""+src.window.getWidth());
			src.schedule.service_GetMainConfigUnit().setKeyValue(ConfigItems.WindowHeight, 
					""+src.window.getHeight());
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

	@Override
	public void displayWindow() {
		this.window.setJMenuBar(this.menubar);
		this.window.add(toolsbar, BorderLayout.NORTH);
		
		int width = Integer.parseInt(this.schedule.service_GetMainConfigUnit()
				.getValue(ConfigItems.WindowWidth,"800"));
		int height = Integer.parseInt(this.schedule.service_GetMainConfigUnit()
				.getValue(ConfigItems.WindowHeight, "600"));
		
		this.window.setSize(width, height);
		this.window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.window.setVisible(true);
	}

	@Override
	public void placeView(String viewTitle, Component comp) {
		this.window.setTitle(viewTitle);
		this.mainview = (TabView) comp;
		this.window.add(comp, BorderLayout.CENTER);
	}

	@Override
	public void service_RefreshMenuBar(ArrayList<JMenu> exterl) {
		this.menubar.removeAll();
		exterl.add(this.mainview.getCustomMenu());
		exterl.add(this.getCustomMenu());
		for(JMenu one:exterl) {
			this.menubar.add(one);
		}
	}

}
