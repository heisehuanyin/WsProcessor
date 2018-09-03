package ws.editor.plugin.window;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JToolBar;

import ws.editor.WsProcessor;
import ws.editor.common.ItemsKey;
import ws.editor.common.PluginFeature;
import ws.editor.plugin.TabView;

public class SimpleWindow extends AbstractFrontWindow {
	private String w_id = this.getClass().getName();
	private WsProcessor schedule = null;
	private JFrame window = new JFrame();
	private JMenuBar menubar = new JMenuBar();
	private JToolBar toolsbar = new JToolBar();
	private TabView mainview = null;

	@Override
	public int pluginMark() {
		return PluginFeature.UI_Window;
	}

	@Override
	public String getCompid() {
		return AbstractFrontWindow.class.getName()+w_id;
	}

	@Override
	public JMenu getCustomMenu() {
		// TODO Auto-generated method stub
		return new JMenu("SimpleWindow");
	}

	@Override
	public void saveOperation() {
		// TODO Auto-generated method stub

	}

	@Override
	public AbstractFrontWindow getInstance(WsProcessor schedule, String gId) {
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
			src.schedule.service_GetMainConfigUnit().setKeyValue(ItemsKey.WindowWidth, 
					""+src.window.getWidth());
			src.schedule.service_GetMainConfigUnit().setKeyValue(ItemsKey.WindowHeight, 
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
				.getValue(ItemsKey.WindowWidth,"800"));
		int height = Integer.parseInt(this.schedule.service_GetMainConfigUnit()
				.getValue(ItemsKey.WindowHeight, "600"));
		
		this.window.setSize(width, height);
		this.window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.window.setVisible(true);
		this.schedule.service_Refresh_MenuBar(null);
	}

	@Override
	public void placeView(String viewTitle, Component comp) {
		this.window.setTitle(viewTitle);
		this.mainview = (TabView) comp;
		this.window.add(comp, BorderLayout.CENTER);
	}

	@Override
	public void service_ResetMenuBar(JMenuBar mbar) {
		this.menubar.removeAll();
		if(this.mainview != null) {
			mbar.add(this.mainview.getCustomMenu());
		}
		mbar.add(this.getCustomMenu());
		for(JMenu one:mbar) {
			this.menubar.add(one);
		}
		this.window.validate();
	}

}
