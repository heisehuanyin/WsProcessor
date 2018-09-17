package ws.editor.window;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.SplitPaneUI;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

import ws.editor.WsProcessor;
import ws.editor.comn.ConfigItemsKey;
import ws.editor.p.ContentView;
import ws.editor.p.FrontWindow;

public class TwoViewWindow extends AbstractWindow {
	private String gId;
	private WsProcessor core;
	private ArrayList<ContentView> Views = new ArrayList<>();
	private JTabbedPane leftC = new JTabbedPane(JTabbedPane.SCROLL_TAB_LAYOUT);
	private JTabbedPane rightC = new JTabbedPane(JTabbedPane.SCROLL_TAB_LAYOUT);
	private JSplitPane jsp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftC, rightC);
	private JCheckBoxMenuItem leftVisible = new JCheckBoxMenuItem("LeftVisible");

	private String LEFTVIEW_WIDTH_KEY = ConfigItemsKey.get_CUSTOMSETTING_STRING(this, "LEFTVIEW_WIDTH");
	private String LEFTVISIBLE_CTRL = ConfigItemsKey.get_CUSTOMSETTING_STRING(this, "leftvisible");
	private String VIEWPOSITION_KEY = ConfigItemsKey.get_CUSTOMSETTING_STRING(this, "VIEW_POSITION");

	private static final int POSITION_LEFT = 0x0;
	private static final int POSITION_RIGHT = 0x02;

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
		// MenuBar=======================
		this.core.service_Refresh_MenuBar(this);

		// WindowSize====================
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		String wStr = this.core.instance_GetMainConfigUnit().getValue(ConfigItemsKey.WindowWidth, "800");
		String hStr = this.core.instance_GetMainConfigUnit().getValue(ConfigItemsKey.WindowHeight, "600");
		this.setSize(Integer.parseInt(wStr), Integer.parseInt(hStr));

		// CustomSplitPane==============
		this.customJSplitPane(this.jsp);
		String leftLocation = this.core.instance_GetMainConfigUnit().getValue(this.LEFTVIEW_WIDTH_KEY, "80");
		this.jsp.setDividerLocation(Integer.parseInt(leftLocation));

		// CustomTabbedPane============
		this.leftC.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				core.service_Refresh_MenuBar(TwoViewWindow.this);
			}
		});
		this.rightC.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				core.service_Refresh_MenuBar(TwoViewWindow.this);
			}
		});

		// LeftViewVisible=============
		String vctrl = this.core.instance_GetMainConfigUnit().getValue(this.LEFTVISIBLE_CTRL, "true");
		boolean vcb = Boolean.parseBoolean(vctrl);
		this.leftVisible.setState(vcb);
		if (!this.leftVisible.getState())
			this.getContentPane().add(rightC, BorderLayout.CENTER);
		else
			this.getContentPane().add(jsp, BorderLayout.CENTER);

		this.leftVisible
				.addItemListener(new ViewCubeControl(this.leftVisible, this.jsp, rightC, this.LEFTVIEW_WIDTH_KEY));

		// AddListener===================
		this.addComponentListener(new CommonComponentListener(this.core));
		this.addWindowListener(new CommonWindowsListener(this.core));

		this.setVisible(true);
	}

	/**
	 * 设置JSplitPane的定制细节：<br>
	 * 1.设置分割栏边框灰色，1px，宽4px；<br>
	 * 2.设置JSplitPane 无边框
	 * 
	 * @param splitp
	 *            目标
	 */
	private void customJSplitPane(JSplitPane splitp) {
		SplitPaneUI sp = splitp.getUI();
		BasicSplitPaneDivider dr = ((BasicSplitPaneUI) sp).getDivider();
		dr.setBorder(new LineBorder(Color.gray, 1, false));
		splitp.setDividerSize(4);
		splitp.setBorder(new EmptyBorder(0, 0, 0, 0));
	}

	@Override
	protected void placeView2(String viewTitle, ContentView comp) {
		if (!this.Views.contains(comp))
			this.Views.add(comp);

		String position = this.core.instance_GetMainConfigUnit()
				.getValue(this.VIEWPOSITION_KEY + "." + comp.getClass().getName(), "" + this.POSITION_RIGHT);

		if (Integer.parseInt(position) == this.POSITION_LEFT) {
			this.leftC.add(viewTitle, comp.getView());
			this.leftC.setTabComponentAt(this.leftC.getTabCount() - 1, new TabWithCloseButton(this, leftC, comp));
			this.leftC.setSelectedIndex(this.leftC.getTabCount() - 1);
		}
		if (Integer.parseInt(position) == this.POSITION_RIGHT) {
			this.rightC.add(viewTitle, comp.getView());
			this.rightC.setTabComponentAt(this.rightC.getTabCount() - 1, new TabWithCloseButton(this, rightC, comp));
			this.rightC.setSelectedIndex(this.rightC.getTabCount() - 1);
		}

	}

	@Override
	public void saveOperation() {
		int lw = this.jsp.getDividerLocation();
		this.core.instance_GetMainConfigUnit().setKeyValue(this.LEFTVIEW_WIDTH_KEY, "" + lw);
		this.core.instance_GetMainConfigUnit().setKeyValue(this.LEFTVISIBLE_CTRL, "" + this.leftVisible.getState());
	}

	@Override
	public String getGroupId() {
		return this.gId;
	}

	@Override
	public ArrayList<? extends ContentView> getActivedViews() {
		ArrayList<ContentView> list = new ArrayList<>();
		Component x;

		if (this.leftC.getTabCount() > 0) {
			x = this.leftC.getSelectedComponent();
			for (ContentView v : this.Views)
				if (v.getView() == x)
					list.add(v);
		}

		if (this.rightC.getTabCount() > 0) {
			x = this.rightC.getSelectedComponent();
			for (ContentView v : this.Views)
				if (v.getView() == x)
					list.add(v);
		}

		return list;
	}

	@Override
	public JMenu getCustomMenu() {
		JMenu cus = new JMenu("Window");

		cus.add(leftVisible);

		return cus;
	}

	@Override
	protected void service_ResetMenuBar2(JMenuBar mbar) {
		this.setJMenuBar(mbar);
	}

	@Override
	public void closeView(ContentView comp) {
		if (!this.Views.contains(comp))
			return;

		Component x = comp.getView();

		int i = this.leftC.indexOfComponent(x);
		if (i != -1)
			this.leftC.remove(i);
		i = this.rightC.indexOfComponent(x);
		if (i != -1)
			this.rightC.remove(i);
	}

	private class ViewCubeControl implements ItemListener {
		private JSplitPane split = null;
		private Component r = null;
		private JCheckBoxMenuItem t = null;
		private String widthCtrlKey = null;

		public ViewCubeControl(JCheckBoxMenuItem t, Component jsplit, Component right, String widthCtrl_Key) {
			this.t = t;
			this.split = (JSplitPane) jsplit;
			this.r = right;
			this.widthCtrlKey = widthCtrl_Key;
		}

		@Override
		public void itemStateChanged(ItemEvent e) {
			int state = e.getStateChange();

			int lw = split.getDividerLocation();
			TwoViewWindow.this.core.instance_GetMainConfigUnit().setKeyValue(widthCtrlKey, "" + lw);

			if (state == ItemEvent.SELECTED) {
				Component[] cons = TwoViewWindow.this.getContentPane().getComponents();
				for (Component c : cons) {
					if (c == r)
						TwoViewWindow.this.remove(c);
				}
				TwoViewWindow.this.getContentPane().add(split, BorderLayout.CENTER);

				split.setRightComponent(r);
				String leftLocation = TwoViewWindow.this.core.instance_GetMainConfigUnit().getValue(widthCtrlKey, "80");
				split.setDividerLocation(Integer.parseInt(leftLocation));

			} else {
				Component[] cons = TwoViewWindow.this.getContentPane().getComponents();
				for (Component c : cons) {
					if (c == split)
						TwoViewWindow.this.remove(c);
				}
				TwoViewWindow.this.getContentPane().add(r, BorderLayout.CENTER);

			}

			TwoViewWindow.this.validate();
		}
	}

}
