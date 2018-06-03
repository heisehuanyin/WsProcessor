package ws.editor.window;

import java.awt.Component;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import ws.editor.ConfigItems;
import ws.editor.PluginFeature;
import ws.editor._plugin_define.FrontWindow;
import ws.editor._plugin_define.PMenuBar;
import ws.editor.schedule.WsProcessor;

/**
 * 界面窗口，用于摆放各种控件和面板*/
public class WWindow implements FrontWindow{
	private String w_id = this.getClass().getName();
	private JFrame window = new JFrame();
	private WsProcessor schedule = null;
	
	public WWindow() {}
	
	@Override
	public void displayWindow() {
		this.buildWindow(this.window);
		
		int width = Integer.parseInt(this.schedule.getMainConfigUnit().
				getValue(ConfigItems.WindowWidth, "800"));
		int height = Integer.parseInt(this.schedule.getMainConfigUnit().
				getValue(ConfigItems.WindowHeight, "600"));
		this.window.setSize(width,height);
		this.window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.window.setVisible(true);
	}
	
	private void buildWindow(JFrame window) {
		PMenuBar menubar = this.schedule.getNewDefaultMenubar("menubar");
		if(menubar != null)
			window.setJMenuBar((JMenuBar) menubar);
		
	}


	@Override
	public int getPluginMark() {
		return PluginFeature.UI_Window;
	}


	@Override
	public String getCompid() {
		return FrontWindow.class.getName()+this.w_id;
	}


	@Override
	public JMenu getCustomMenu() {
		return new JMenu(this.getClass().getName());
	}



	@Override
	public void saveOperation() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public FrontWindow getInstance(WsProcessor schedule, String id) {
		
		WWindow rtn = new WWindow();
		rtn.w_id = id;
		rtn.schedule = schedule;
		SrcWindowListener ear = new SrcWindowListener(rtn);
		rtn.window.addWindowListener(ear);
		rtn.window.addComponentListener(ear);
				
		return rtn;
	}
	
	class SrcWindowListener implements WindowListener, ComponentListener{
		private WWindow src = null;
		public SrcWindowListener(WWindow srcobj) {
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
			src.schedule.saveOperation();
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
			src.schedule.getMainConfigUnit().setKeyValue(ConfigItems.WindowWidth, 
					""+src.window.getWidth());
			src.schedule.getMainConfigUnit().setKeyValue(ConfigItems.WindowHeight, 
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
}
