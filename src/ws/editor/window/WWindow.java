package ws.editor.window;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.SplitPaneUI;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

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
	
	private JTabbedPane leftCollect = new JTabbedPane();
	private JTabbedPane centerCollect = new JTabbedPane();
	private JTabbedPane rightCollect = new JTabbedPane();
	private JTabbedPane bottomCollect = new JTabbedPane();
	
	private JSplitPane centerAndRightCollect = 
			new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,this.centerCollect,this.rightCollect);
	private JSplitPane topAndBottomCollect = 
			new JSplitPane(JSplitPane.VERTICAL_SPLIT,this.centerAndRightCollect,this.bottomCollect);
	private JSplitPane leftAndRightCollect = 
			new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,this.leftCollect,this.topAndBottomCollect);
	
	
	public WWindow() {}
	
	@Override
	public void displayWindow() {
		this.buildWindow(this.window);
		
		int width = Integer.parseInt(this.schedule.instance_GetMainConfigUnit().
				getValue(ConfigItems.WindowWidth, "800"));
		int height = Integer.parseInt(this.schedule.instance_GetMainConfigUnit().
				getValue(ConfigItems.WindowHeight, "600"));
		this.window.setSize(width,height);
		this.window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.window.setVisible(true);
	}
	
	private void buildWindow(JFrame window) {
		PMenuBar menubar = this.schedule.instance_GetNewDefaultMenubar("menubar");
		if(menubar != null)
			window.setJMenuBar((JMenuBar) menubar);
		// TODO add toolbar
		// TODO add statusbar
		
		window.add(this.leftAndRightCollect, BorderLayout.CENTER);
		SplitPaneUI spui = this.leftAndRightCollect.getUI();
		BasicSplitPaneDivider dr = ((BasicSplitPaneUI)spui).getDivider();
		dr.setBorder(new LineBorder(Color.gray, 1, false));
		this.leftAndRightCollect.setDividerSize(4);


		SplitPaneUI spui2 = this.topAndBottomCollect.getUI();
		BasicSplitPaneDivider dr2 = ((BasicSplitPaneUI)spui2).getDivider();
		dr2.setBorder(new LineBorder(Color.gray, 1, false));
		this.topAndBottomCollect.setDividerSize(4);
		this.topAndBottomCollect.setBorder(new EmptyBorder(0,0,0,0));
		
		SplitPaneUI spui3 = this.centerAndRightCollect.getUI();
		BasicSplitPaneDivider dr3 = ((BasicSplitPaneUI)spui3).getDivider();
		dr3.setBorder(new LineBorder(Color.gray, 1, false));
		this.centerAndRightCollect.setDividerSize(4);
		this.centerAndRightCollect.setBorder(new EmptyBorder(0,0,0,0));
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
			src.schedule.instance_GetMainConfigUnit().setKeyValue(ConfigItems.WindowWidth, 
					""+src.window.getWidth());
			src.schedule.instance_GetMainConfigUnit().setKeyValue(ConfigItems.WindowHeight, 
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
