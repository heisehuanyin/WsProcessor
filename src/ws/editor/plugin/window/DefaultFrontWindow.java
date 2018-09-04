package ws.editor.plugin.window;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.SplitPaneUI;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

import ws.editor.WsProcessor;
import ws.editor.common.ItemsKey;
import ws.editor.common.PluginFeature;
import ws.editor.plugin.bak.PMenuBar;
import ws.editor.plugin.bak.StatusBar;
import ws.editor.plugin.bak.ToolsBar;

/**
 * 界面窗口，用于摆放各种控件和面板*/
public class DefaultFrontWindow extends AbstractFrontWindow{
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
	
	private ToolsBar toolsbar = null;
	
	public DefaultFrontWindow() {}
	
	@Override
	public void displayWindow() {
		this.buildWindow(this.window);
		
		int width = Integer.parseInt(this.schedule.service_GetMainConfigUnit().
				getValue(ItemsKey.WindowWidth, "800"));
		int height = Integer.parseInt(this.schedule.service_GetMainConfigUnit().
				getValue(ItemsKey.WindowHeight, "600"));
		this.window.setSize(width,height);
		this.window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.window.setVisible(true);
	}
	
	private void buildWindow(JFrame window) {
		PMenuBar menubar = this.schedule.service_GetPluginManager()
				.instance_GetNewDefaultMenubar("menubar");
		if(menubar != null) {
			window.setJMenuBar((JMenuBar) menubar);
		}
		
		ToolsBar toolbar = this.schedule.service_GetPluginManager()
				.instance_getNewDefaultToolsBar("toolsbar");
		if(toolbar !=null) {
			window.add((JToolBar)toolbar,BorderLayout.NORTH);
			this.toolsbar = toolbar;
		}
		// TODO add statusbar


		this.leftCollect.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		this.rightCollect.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		this.centerCollect.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		this.bottomCollect.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		
		window.getContentPane().add(this.leftAndRightCollect);
		this.UICustomOperate(this.leftAndRightCollect);
		this.UICustomOperate(this.topAndBottomCollect);
		this.UICustomOperate(this.centerAndRightCollect);
		
		this.schedule.service_Refresh_MenuBar(null);
		toolbar.buildToolsBarContent(menubar);
	}
	/**
	 * 设置JSplitPane的定制细节：1.设置分割栏边框灰色，1px，宽4px；2.设置JSplitPane 无边框
	 * @param splitp 目标*/
	private void UICustomOperate(JSplitPane splitp) {
		SplitPaneUI sp = splitp.getUI();
		BasicSplitPaneDivider dr = ((BasicSplitPaneUI)sp ).getDivider();
		dr.setBorder(new LineBorder(Color.gray, 1, false));
		splitp.setDividerSize(4);
		splitp.setBorder(new EmptyBorder(0, 0, 0, 0));
	}

	
	@Override
	public void placeView(String viewTitle, Component comp) {
		// TODO 配置文件参与设置
	}
	

	@Override
	public JMenu getCustomMenu() {
		JMenu viewM = new JMenu("视图配置");
		JCheckBoxMenuItem leftVisible = new JCheckBoxMenuItem("左视图可见");
		JCheckBoxMenuItem rightVisible = new JCheckBoxMenuItem("右视图可见");
		JCheckBoxMenuItem bottomVisible = new JCheckBoxMenuItem("底部视图可见",this.bottomCollect.isVisible());
		bottomVisible.addItemListener(new visibleManage(this.bottomCollect));
		JCheckBoxMenuItem toolsbarVisible = new JCheckBoxMenuItem("工具栏可见",((JComponent)this.toolsbar).isVisible());
		toolsbarVisible.addItemListener(new visibleManage((JComponent) this.toolsbar));
		
		viewM.add(leftVisible);
		viewM.add(rightVisible);
		viewM.add(bottomVisible);
		viewM.add(toolsbarVisible);
		
		return viewM;
	}
	private class visibleManage implements ItemListener{
		private JComponent s;
		public visibleManage(JComponent obj) {
			s = obj;
		}
		@Override
		public void itemStateChanged(ItemEvent e) {
			if(s.isVisible())
				s.setVisible(false);
			else
				s.setVisible(true);
		}
		
	}
	



	@Override
	public void saveOperation() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public AbstractFrontWindow getInstance(WsProcessor schedule, String gId) {
		
		DefaultFrontWindow rtn = new DefaultFrontWindow();
		rtn.schedule = schedule;
		SrcWindowListener ear = new SrcWindowListener(rtn);
		rtn.window.addWindowListener(ear);
		rtn.window.addComponentListener(ear);
				
		return rtn;
	}
	
	class SrcWindowListener implements WindowListener, ComponentListener{
		private DefaultFrontWindow src = null;
		public SrcWindowListener(DefaultFrontWindow srcobj) {
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
	public void service_ResetMenuBar(JMenuBar mbar) {
		this.setJMenuBar(mbar);
	}
}
