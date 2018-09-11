package ws.editor;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import ws.editor.comn.ItemsKey;
import ws.editor.comn.PluginFeature;
import ws.editor.plugin.ConfigPort;
import ws.editor.plugin.ContentView;
import ws.editor.plugin.FrontWindow;
import ws.editor.plugin.LogPort;
import ws.editor.plugin.configport.DefaultConfigPort;
import ws.editor.plugin.contentview.DefaultTableView;
import ws.editor.plugin.contentview.DefaultTextView;
import ws.editor.plugin.contentview.DefaultTreeView;
import ws.editor.plugin.filesymbo.DefaultFileSymbo;
import ws.editor.plugin.logport.DefaultLogPort;
import ws.editor.plugin.menubar.DefaultMenuBar;
import ws.editor.plugin.tablemodel.DefaultCSVModel;
import ws.editor.plugin.textmodel.DefaultTextModel;
import ws.editor.plugin.treemodel.DefaultProjectModel;
import ws.editor.plugin.window.SingleViewWindow;
import ws.editor.plugin.window.TwoViewWindow;

/**
 * 核心模块
 */
public class WsProcessor {
	private PluginManager manager = new PluginManager(this);
	private String wsProcessor_logPath = "." + File.separator + "Software.wslog";
	private String wsProcessor_configPath = "." + File.separator + "Software.wscfg";
	private JFileChooser chooser = new JFileChooser();

	/**
	 * 构建一个默认核心模块
	 */
	public WsProcessor() {
	}

	// =service========================================================================
	/**
	 * 获取插件管理器服务，供外部使用
	 * 
	 * @return 正在使用的插件管理器
	 */
	public PluginManager service_GetPluginManager() {
		return this.manager;
	}

	/**
	 * 注册各种插件组件,插件在在管理器中的id格式：类名
	 * 
	 * @param obj
	 *            组件的实例，这个实例作为工厂，用于派生新实例的，不能够用于工作
	 */
	public void service_RegisterPlugin(PluginFeature obj) {
		this.manager.factory_RegisterPlugin(obj);
	}

	/**
	 * 重构菜单栏服务，通常调用者：1.view视图插件，视图变化菜单栏变化
	 * 
	 * @param win
	 *            发起刷新的窗体
	 */
	public void service_Refresh_MenuBar(FrontWindow win) {
		ArrayList<JMenu> exterl = new ArrayList<>();

		FileMenu one = new FileMenu(win);
		one.initMenus();
		exterl.add(one);

		for(ContentView itor:win.getActivedViews()) {
			exterl.add(itor.getCustomMenu());
		}
		exterl.add(win.getCustomMenu());
		
		exterl.add(new CustomMenu(this, "Custom"));

		JMenuBar x = this.manager.instance_GetNewDefaultMenubar(win.getGroupId()).rebuildMenuBar(exterl);

		win.service_ResetMenuBar(x);
	}

	/**
	 * 操作文件选择对话框
	 * 
	 * @param target_type
	 *            针对的文件目标类型： File（JFileChooser.FILE_ONLY），
	 *            Directory（JFileChooser.DIRECTORY_ONLY），
	 *            File&amp;Directory（JFileChooser.FILE_AND_DIRECTORY）。
	 * @param dialog_type
	 *            对话框类型： 保存对话框（JFileChooser.SAVE_DIALOG）
	 *            选择对话框（JFileChooser.OPEN_DIALOG）
	 * @param ff
	 *            指定的筛选器，用于筛选视图中的文件
	 * @return 选中的文件
	 */
	public File service_FileChooserOperate(int target_type, int dialog_type, FileFilter ff) {
		this.chooser.setFileSelectionMode(target_type);
		if (ff != null)
			this.chooser.setFileFilter(ff);

		int ret = 0;
		if (dialog_type == JFileChooser.SAVE_DIALOG) {
			ret = chooser.showSaveDialog((Component) null);
		} else {
			ret = chooser.showOpenDialog((Component) null);
		}
		if (ret != JFileChooser.APPROVE_OPTION)
			return null;
		return chooser.getSelectedFile();
	}

	/**
	 * 执行保存操作
	 */
	public void service_SaveOperation() {
		this.manager.operate_SaveOperation();
	}

	/**
	 * 打开指定文件，此操作将会调用工具组成链条通道，依次打开并获取内容
	 * 
	 * @param fpath
	 *            文件路径
	 * @param win
	 *            发生动作的窗口，最后一步视图会放置该窗口，如果win=null，视图不会放置
	 */
	public void service_OpenFile(String fpath, FrontWindow win) {
		File one = new File(fpath);
		if (!one.exists()) {
			this.instance_GetDefaultLogPort().errorLog(this, "文件不存在：" + fpath);
			return;
		}

		String cListStr = null;

		if (!one.isFile()) {
			this.instance_GetDefaultLogPort().errorLog(this, "指向目标不是文件：" + fpath);
		} else if (one.isFile()) {
			String regx = ".txt";

			regx = one.getName().substring(one.getName().lastIndexOf('.'));
			cListStr = this.instance_GetMainConfigUnit().getValue(ItemsKey.get_MODULELIST_AS_SUFFIX(regx),
					"ws.editor.plugin.filesymbo.DefaultFileSymbo=>" + "ws.editor.plugin.textmodel.DefaultTextModel=>"
							+ "ws.editor.plugin.contentview.DefaultTextView");
		}

		// source=>module1=>module2=>module3=>module4=>the last one module
		PluginFeature x = this.service_GetPluginManager().service_BuildInstanceList(cListStr, fpath);
		if (win != null && x.pluginMark() == PluginFeature.UI_ContentView) {
			win.placeView(one.getName(), ((ContentView) x));
			this.service_Refresh_MenuBar(win);
		}
	}

	// instance ========================================================
	/**
	 * 获取默认的log输出接口,每次启动加载的{@link LogPort}都是最后加载的同一种插件，输出格式相同。
	 * 
	 * @return 默认的log输出接口
	 */
	public LogPort instance_GetDefaultLogPort() {
		return this.manager.instance_GetLogPort(this.wsProcessor_logPath);
	}

	/**
	 * 获取主配置文件，每次启动加载的 {@link ConfigPort } 都是最后加载的同一种插件，输入输出的格式相同。
	 * 
	 * @return 连接向程序的主配置文件的配置端口
	 */
	public ConfigPort instance_GetMainConfigUnit() {
		return this.manager.instance_GetConfigUnit(this.wsProcessor_configPath);
	}

	// =operate==========================================================================
	private void control_LoadAllPlugins() {
		// TODO 载入特定路径的所有插件

	}

	/**
	 * 初始化静默模式默认的组件,能够保证最低限度的正常使用
	 */
	private void control_InitDefaultSilentPlugin() {
		this.addShutDownHook();
		this.service_RegisterPlugin(new DefaultFileSymbo());
		this.service_RegisterPlugin(new DefaultLogPort());
		this.service_RegisterPlugin(new DefaultConfigPort());
		this.service_RegisterPlugin(new DefaultTextModel());
		this.service_RegisterPlugin(new DefaultProjectModel());
		this.service_RegisterPlugin(new DefaultCSVModel());
	}

	/**
	 * 开启静默模式：实例化Processor之后，注册各种组件之后，调用本函数可以打开静默模式
	 */
	public void control_OpenSilentModel() {
		// TODO 默认模式的设计
		this.control_InitDefaultSilentPlugin();
		this.control_LoadAllPlugins();
	}

	/**
	 * 初始化静默模式插件和图形模式插件，能够保证最低限度的正常使用
	 */
	private void control_InitDefaultGraphicPlugin() {
		this.control_InitDefaultSilentPlugin();

		this.service_RegisterPlugin(new SingleViewWindow());
		this.service_RegisterPlugin(new DefaultMenuBar());
		this.service_RegisterPlugin(new DefaultTextView());
		this.service_RegisterPlugin(new DefaultTreeView());
		this.service_RegisterPlugin(new DefaultTableView());
		this.service_RegisterPlugin(new TwoViewWindow());
		// this.service_RegisterPlugin(new WToolsBar());
	}

	/**
	 * 开启图形模式：实例化Processor之后，注册各种组件之后，调用本函数可以打开图形界面
	 * 
	 * @param groupId
	 *            图形实例分组id
	 */
	public void control_OpenGraphicMode(String groupId) {
		this.control_InitDefaultGraphicPlugin();
		this.control_LoadAllPlugins();

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		this.service_GetPluginManager().instance_GetNewDefaultWindow(groupId == null ? "MainWindow" : groupId);
	}

	// =other==========================================================================
	/**
	 * 截获系统关闭事件，添加处理程序
	 */
	private void addShutDownHook() {
		Runtime.getRuntime().addShutdownHook(new ShutdownHook(this));
	}

	private class ShutdownHook extends Thread {
		private WsProcessor s = null;

		public ShutdownHook(WsProcessor s) {
			this.s = s;
		}

		@Override
		public void run() {
			this.s.service_SaveOperation();
		}

	}

	private class FileMenu extends JMenu implements MenuListener {
		private JMenu newMenu = new JMenu("New");
		private JMenu openMenu = new JMenu("Open");
		private FrontWindow w;

		public FileMenu(FrontWindow w) {
			super("File");
			this.w = w;
		}

		public void initMenus() {
			newMenu.addMenuListener(this);
			openMenu.addMenuListener(this);
			this.add(newMenu);
			this.add(openMenu);
			this.addSeparator();

			JMenuItem exitMenu = new JMenuItem("Exit");
			exitMenu.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					System.exit(0);
				}
			});
			this.add(exitMenu);
		}

		@Override
		public void menuSelected(MenuEvent e) {
			File ftemplate = new File("./FileTemplate");
			File[] children = ftemplate.listFiles();
			for (File onef : children) {
				String name = onef.getName();
				JMenuItem mitem = new JMenuItem(name);
				JMenuItem mitem2 = new JMenuItem(name);
				try {
					mitem.addActionListener(new FileMenu_Open(onef.getCanonicalPath(), w));
					mitem2.addActionListener(new FileMenu_New(onef.getCanonicalPath(), w));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				openMenu.add(mitem);
				newMenu.add(mitem2);
			}
		}

		@Override
		public void menuDeselected(MenuEvent e) {
			newMenu.removeAll();
			openMenu.removeAll();
		}

		@Override
		public void menuCanceled(MenuEvent e) {
			// TODO Auto-generated method stub

		}

	}

	private class FileMenu_Open implements ActionListener {
		private String target = null;
		private FrontWindow w;

		public FileMenu_Open(String target, FrontWindow owner) {
			this.target = target;
			this.w = owner;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			String suffix = this.target.substring(this.target.lastIndexOf('.')+1);
			
			File one = WsProcessor.this.service_FileChooserOperate(JFileChooser.FILES_ONLY, JFileChooser.OPEN_DIALOG,
					new FileNameExtensionFilter(suffix.toUpperCase()+"文件", suffix));
			if (one == null)
				return;
			
			WsProcessor.this.service_OpenFile(this.target, this.w);
		}

	}

	private class FileMenu_New implements ActionListener {
		private String target = null;
		private FrontWindow w = null;

		public FileMenu_New(String target, FrontWindow owner) {
			this.target = target;
			this.w = owner;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			File dst = WsProcessor.this.service_FileChooserOperate(JFileChooser.DIRECTORIES_ONLY,
					JFileChooser.SAVE_DIALOG, null);
			if (dst == null)
				return;
			try {
				String cpath = dst.getCanonicalPath();
				String suffixp = this.target.substring(this.target.lastIndexOf('.'));

				if (!cpath.endsWith(suffixp)) {
					dst = new File(cpath + suffixp);
				}

				if (!dst.exists())
					dst.createNewFile();

				File source = new File(this.target);

				InputStream bs = new FileInputStream(source);
				OutputStream bd = new FileOutputStream(dst);

				bd.write(bs.readAllBytes());
				bd.flush();
				bd.close();
				
				WsProcessor.this.service_OpenFile(this.target, this.w);

			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	
	private class CustomMenu extends JMenu implements ActionListener{
		private WsProcessor core;
		
		public CustomMenu(WsProcessor wsProcessor, String name) {
			super(name);
			this.core = wsProcessor;
			JMenuItem customPane = new JMenuItem("Perferences");
			customPane.addActionListener(this);
			this.add(customPane);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			CustomMenu_Dialog x = new CustomMenu_Dialog(core);
			x.setSize(600, 400);
			x.setVisible(true);
		}
		
	}
	private class CustomMenu_Dialog extends JDialog{
		private WsProcessor core;
		
		public CustomMenu_Dialog(WsProcessor core) {
			this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			this.core = core;
			
			JPanel base = new JPanel();
			this.setContentPane(new JScrollPane(base));
			base.setLayout(new BoxLayout(base, BoxLayout.Y_AXIS));
			
			
			//Config Default Window ================================
			JPanel configWindow = new JPanel();
			configWindow.setLayout(new FlowLayout(FlowLayout.LEFT));
			configWindow.add(new JLabel("选中窗口主类名："));
			String defaultWindow = this.core.instance_GetMainConfigUnit()
					.getValue(ItemsKey.DefaultWindow, SingleViewWindow.class.getName());
			ArrayList<String> windows = WsProcessor.this.service_GetPluginManager()
					.service_QueryFactoryList(PluginFeature.UI_Window);
			int index = windows.indexOf(defaultWindow);
			JComboBox<?> listSelect = new JComboBox<Object>(windows.toArray());
			listSelect.setSelectedIndex(index);
			configWindow.add(listSelect);
			listSelect.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String x = (String) listSelect.getSelectedItem();
					core.instance_GetMainConfigUnit().setKeyValue(ItemsKey.DefaultWindow, x);
				}});
			configWindow.setBorder(BorderFactory.createTitledBorder("默认Window"));
			base.add(configWindow);
			
			//Config ChannelList ==================================
			JPanel configCList = new JPanel();
			configCList.setBorder(BorderFactory.createTitledBorder("文件类型-处理链条"));
			base.add(configCList);
		}
		
		
	}
}
