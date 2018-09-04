package ws.editor;

import java.awt.Component;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import ws.editor.common.PluginFeature;
import ws.editor.plugin.ConfigPort;
import ws.editor.plugin.LogPort;
import ws.editor.plugin.bak.PMenuBar;
import ws.editor.plugin.configport.DefaultConfigPort;
import ws.editor.plugin.filesymbo.DefaultFileSymbo;
import ws.editor.plugin.logport.DefaultLogPort;
import ws.editor.plugin.menubar.WMenuBar;
import ws.editor.plugin.textmodel.DefaultTextModel;
import ws.editor.plugin.toolsbar.WToolsBar;
import ws.editor.plugin.window.AbstractFrontWindow;
import ws.editor.plugin.window.DefaultFrontWindow;

public class WsProcessor {
	private PluginManager manager = new PluginManager(this);
	private String wsProcessor_logPath = "."+File.separator+"Software.wslog";
	private String wsProcessor_configPath = "."+File.separator+"Software.wscfg";
	private AbstractFrontWindow fwin = null;
	private JFileChooser chooser = new JFileChooser();
	
	public WsProcessor (){}
	
	
	//=service========================================================================
	/**
	 * 获取插件管理器服务，供外部使用
	 * @return 正在使用的插件管理器*/
	public PluginManager service_GetPluginManager() {
		return this.manager;
	}
	
	/**
	 * 注册各种工厂组件,在管理器中的id格式：接口名+类名
	 * @param obj 组件的实例，这个实例作为工厂，用于派生新实例的，不能够用于工作
	 * */
	public void service_RegisterPlugin(PluginFeature obj) {
		this.manager.factory_RegisterPlugin(obj);
	}

	
	/**
	 * 获取默认的log输出接口,每次启动加载的{@link LogPort}都是最后加载的同一种插件，输出格式相同。
	 * @return 默认的log输出接口*/
	public LogPort service_GetDefaultLogPort() {
		return this.manager.instance_GetLogPort(this.wsProcessor_logPath);
	}
	
	/**
	 * 获取主配置文件，每次启动加载的 {@link ConfigPort } 都是最后加载的同一种插件，输入输出的格式相同。
	 * @return 连接向程序的主配置文件的配置端口*/
	public ConfigPort service_GetMainConfigUnit() {
		return this.manager.instance_GetConfigUnit(this.wsProcessor_configPath);
	}
	
	
	/**
	 * 重构菜单栏服务，通常调用者：1.view视图插件，视图变化菜单栏变化
	 * @param win TODO*/
	public void service_Refresh_MenuBar(AbstractFrontWindow win) {
		ArrayList<JMenu> exterl = new ArrayList<>();
		JMenu wspace = this.rebuildMenu_WSpace();
		exterl.add(wspace);
		
		exterl.addAll(this.fwin.getActivedViewsMenus());
		
		exterl.add(this.fwin.getCustomMenu());
		
		PMenuBar x = this.manager.instance_GetNewDefaultMenubar("menubar").refreshMenuBar(exterl);
		
		
		
		this.fwin.service_ResetMenuBar(x);
	}
	
	/**
	 * 操作文件选择对话框
	 * @param target_type 针对的文件目标类型：
	 * File（JFileChooser.FILE_ONLY）,
	 * Directory(JFileChooser.DIRECTORY_ONLY)
	 * File&Directory(JFileChooser.FILE_AND_DIRECTORY)
	 * @param dialog_type 对话框类型：
	 * 保存对话框（JFileChooser.SAVE_DIALOG）
	 * 选择对话框（JFileChooser.OPEN_DIALOG）
	 * @return 选中的文件*/
	public File service_FileChooserOperate(int target_type, int dialog_type) {
		this.chooser.setFileSelectionMode(target_type);
		int ret = 0;
		if(dialog_type == JFileChooser.SAVE_DIALOG) {
			ret = chooser.showSaveDialog((Component) null);
		}else {
			ret = chooser.showOpenDialog((Component) null);
		}
		if(ret != JFileChooser.APPROVE_OPTION)
			return null;
		return chooser.getSelectedFile();
	}
	
	private JMenu rebuildMenu_WSpace(){
		JMenu rtn = new JMenu("WSpace");
		return rtn;
	}
	
	
	
	
	
	
	
	//=operate==========================================================================
	private void operate_LoadAllPlugins() {
		//TODO 载入特定路径的所有插件
		
	}
	
	/**
	 * 初始化静默模式默认的组件,能够保证最低限度的正常使用*/
	private void operate_InitDefaultSilentPlugin() {
		this.addShutDownHook();
		this.service_RegisterPlugin(new DefaultFileSymbo());
		this.service_RegisterPlugin(new DefaultLogPort());
		this.service_RegisterPlugin(new DefaultConfigPort());
		this.service_RegisterPlugin(new DefaultTextModel());
	}

	/**
	 * 开启静默模式：实例化Processor之后，注册各种组件之后，调用本函数可以打开静默模式*/
	public void operate_OpenSilentModel() {
		// TODO 默认模式的设计
		this.operate_InitDefaultSilentPlugin();
		this.operate_LoadAllPlugins();
	}

	/**
	 * 初始化静默模式插件和图形模式插件，能够保证最低限度的正常使用*/
	private void operate_InitDefaultGraphicPlugin() {
		this.operate_InitDefaultSilentPlugin();
		
		this.service_RegisterPlugin(new DefaultFrontWindow());
		this.service_RegisterPlugin(new WMenuBar());
		this.service_RegisterPlugin(new WToolsBar());
	}

	/**
	 * 开启图形模式：实例化Processor之后，注册各种组件之后，调用本函数可以打开图形界面*/
	public void operate_OpenGraphicMode() {
		this.operate_InitDefaultGraphicPlugin();
		this.operate_LoadAllPlugins();
		
		/*if(System.getProperty("os.name").indexOf("Mac") != -1)
			System.setProperty("apple.laf.useScreenMenuBar", "true");*/
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		AbstractFrontWindow win = this.manager.instance_GetNewDefaultWindow("mainwindow");
		this.fwin = win;
		win.displayWindow();
	}
	
	/**
	 * 执行保存操作
	 */
	public void operate_SaveOperation() {
		this.manager.operate_SaveOperation();
	}

	
	
	
	
	
	
	
	//=other==========================================================================
	/**
	 * 截获系统关闭事件，添加处理程序*/
	private void addShutDownHook() {
		Runtime.getRuntime().addShutdownHook(new shutdownHook(this));
	}
	private class shutdownHook extends Thread{
		private WsProcessor s = null;
		public shutdownHook(WsProcessor s) {
			this.s = s;
		}
		@Override
		public void run() {
			this.s.operate_SaveOperation();
		}
		
	}

	/**
	 * 两种启动方式
	 * -w:图形界面启动，可以撰写文件，功能丰富
	 * -s:静默启动，可以读取脚本执行，功能单一
	 * */
	public static void main(String[] args) {
		if(args.length > 1 && args[0].equals("-s")) {
			//TODO 程序的静默处理需要设计
			WsProcessor proc = new WsProcessor();
			proc.operate_OpenSilentModel();
			
			System.out.println("静默处理");
		}else {
			WsProcessor proc = new WsProcessor();
			
			proc.operate_OpenGraphicMode();
		}
	}


}
