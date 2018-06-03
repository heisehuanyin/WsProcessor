package ws.editor.schedule;

import java.io.File;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import ws.editor.ConfigItems;
import ws.editor.PluginFeature;
import ws.editor._plugin_define.ConfigUnit;
import ws.editor._plugin_define.ContentPort;
import ws.editor._plugin_define.FrontWindow;
import ws.editor._plugin_define.LogPort;
import ws.editor._plugin_define.PMenuBar;
import ws.editor.configservice.ConfigService;
import ws.editor.contentport.BinaryDiskFileAccess;
import ws.editor.logport.LogWriter;
import ws.editor.menubar.WMenuBar;
import ws.editor.window.WWindow;

public class WsProcessor {
	private PluginManage manager = new PluginManage();
	private String wsProcessor_logPath = "."+File.separator+"Software.wslog";
	private String wsProcessor_configPath = "."+File.separator+"Software.wscfg";
	
	public WsProcessor (){
		this.registerCompFectory(new LogWriter());
	}
	
	/**
	 * 注册各种工厂组件
	 * @param obj 组件的实例，这个实例作为工厂，用于派生新实例的，不能够用于工作
	 * */
	public void registerCompFectory(PluginFeature obj) {
		this.manager.registerFectory(obj);
	}
	
	/**
	 * 获取特定的注册了的工厂类
	 * @param f_id 工厂类的唯一标识
	 * */
	private PluginFeature getCompFactory(String f_id) {
		PluginFeature factory = this.manager.getRegisteredFactory(f_id);
		
		if(factory == null) {
			this.getLogPortInstance(wsProcessor_logPath).errorLog(this, "未注册"+ f_id +"实例");
			return null;
		}
		
		return factory;
	}
	
	/**
	 * 获取已经存在的实例，这是一个集合接口
	 * @param pluginMark 插件种类,定义域PluginFeature接口中
	 * @param id 插件的唯一标识
	 * @return 返回新的插件实例，如果不存在则返回null
	 * */
	public PluginFeature getExistsPlugin(int pluginMark, String id) {
		return this.manager.getRegisteredPluginInstance(pluginMark, id);
	}
	
	/**
	 * 综合配置文件和默认设置对提供的factory_id进行校验，获取合法的factory用于生成实例
	 * @param factory_id 指明的工厂类id
	 * @param configItems_Item 指明配置项条目作为默认配置项目
	 * @param defaultf_id 指名配置项目出现意外后的默认factory_id*/
	private PluginFeature getValidateFactory(String factory_id, String configItems_Item, String defaultf_id) {
		//获取合法的factory_id
		if(factory_id == null) {
			factory_id = this.getMainConfigUnit().getValue(configItems_Item, defaultf_id);
		}
		
		//该factory是否注册,未注册切换为基础窗口
		PluginFeature factory = this.getCompFactory(factory_id);
		if(factory == null) {
			factory = this.getCompFactory(defaultf_id);
		}
		return factory;
	}
	
	//插件实例获取接口===================================================================
	/**
	 * 获取logport用于输出log,如果已存在指定的log则获取，否则新建一个
	 * @param path log文件路径
	 * @return 实例*/
	public LogPort getLogPortInstance(String path) {
		PluginFeature one = this.getExistsPlugin(PluginFeature.Service_LogPort, path);
		if(one != null)
			return (LogPort) one;
		PluginFeature factory =  this.getCompFactory(LogPort.class.getName());
		
		LogPort writer = ((LogPort)factory).createNewPort(path);
		this.manager.registerPluginInstance(writer);
		return writer;
	}
	
	
	
	
	
	/**
	 * 获取configunit用于读取和输出配置，如果存在则获取，否则新建一个
	 * @param path 配置文件存放路径
	 * @return 实例*/
	public ConfigUnit getConfigUnitInstance(String path) {
		PluginFeature one = this.getExistsPlugin(PluginFeature.Service_ConfigUnit, path);
		if(one != null)
			return (ConfigUnit) one;
		PluginFeature factory = this.getCompFactory(ConfigUnit.class.getName());
		if(factory == null)
			return null;
		
		ConfigUnit config = ((ConfigUnit)factory).createNewPort(path);
		this.manager.registerPluginInstance(config);
		return config;
	}
	/**
	 * 获取主配置文件
	 * */
	public ConfigUnit getMainConfigUnit() {
		return this.getConfigUnitInstance(wsProcessor_configPath);
	}
	
	
	
	
	
	
	
	
	
	/**
	 * 获取frontwindow用于显示界面,可以指定frontwindow种类
	 * @param factory_id 指明window种类，值为null获取配置中控件或者默认控件
	 * @param id 窗口的id
	 * @param configUnit TODO
	 * @return 实例*/
	public FrontWindow getAppWindow(String factory_id, String id, ConfigUnit configUnit) {
		PluginFeature one = this.getExistsPlugin(PluginFeature.UI_Window, id);
		if(one != null)
			return (FrontWindow) one;
		
		//获取合法的factory
		PluginFeature factory = this.getValidateFactory(factory_id, ConfigItems.DefaultWindow, WWindow.class.getName());
		//获取一份实例
		FrontWindow window = ((FrontWindow)factory).getInstance(this, id);
		this.manager.registerPluginInstance(window);
		return window;
	}
	/**
	 * 获取menubar用于构建显示界面
	 * @param factory_id 获取指定类型控件,当为null的时候，返回配置控件
	 * @param id 菜单栏的id
	 * @return 返回实例*/
	public PMenuBar getAppMenubar(String factory_id, String id) {
		PluginFeature one = this.getExistsPlugin(PluginFeature.UI_MenuBar, id);
		if(one != null)
			return (PMenuBar) one;
		
		PluginFeature factory = this.getValidateFactory(factory_id, ConfigItems.DefaultMenuBar, WMenuBar.class.getName());
		
		PMenuBar menubar = ((PMenuBar)factory).getInstance(this, id);
		this.manager.registerPluginInstance(menubar);
		return menubar;
	}
	
	/**
	 * 获取binaryport用于访问数据,如果文件存在就返回连接，如果不存在，就创建文件，再返回连接
	 * @param factory_id 指定插件类型
	 * @param id port的id
	 * @return 返回实例*/
	public ContentPort getContentPort(String factory_id, String id) {
		PluginFeature one = this.getExistsPlugin(PluginFeature.IO_ChannelPort, id);
		if(one!=null)
			return (ContentPort)one;
		
		PluginFeature factory = this.getValidateFactory(factory_id, ConfigItems.DefaultBinaryPort,
				BinaryDiskFileAccess.class.getName());
		ContentPort b_port = ((ContentPort)factory).openExistsFile(this, id);
		this.manager.registerPluginInstance(b_port);
		return b_port;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//======================================================================================
	
	
	
	
	/**
	 * 开启图形模式：实例化Processor之后，注册各种组件之后，调用本函数可以打开图形界面*/
	public void openGraphicMode() {
		this.initDefaultGraphicPlugin();
		
		FrontWindow win = this.getAppWindow(null, "mainwindow", this.getMainConfigUnit());
		win.displayWindow();
	}
	
	/**
	 * 开启静默模式：实例化Processor之后，注册各种组件之后，调用本函数可以打开静默模式*/
	public void openSilentMode() {
		// TODO 默认模式的设计
	}
	/**
	 * 初始化静默模式默认的组件,能够保证最低限度的正常使用*/
	private void initDefaultSilentPlugin() {
		this.registerCompFectory(new LogWriter());
		this.registerCompFectory(new ConfigService());
	}
	/**
	 * 初始化静默模式插件和图形模式插件，能够保证最低限度的正常使用*/
	private void initDefaultGraphicPlugin() {
		this.initDefaultSilentPlugin();
		
		this.registerCompFectory(new WWindow());
		this.registerCompFectory(new WMenuBar());
	}
	
	
	
	
	
	
	
	/**
	 * 执行保存操作
	 */
	public void saveOperation() {
		this.manager.saveOperation();
	}
	/**
	 * 执行推出操作
	 * */
	public void exitOperation() {
		this.saveOperation();
		System.exit(0);
	}

	/**
	 * 两种启动方式
	 * -w:图形界面启动，可以撰写文件，功能丰富
	 * -s:静默启动，可以读取脚本执行，功能单一
	 * */
	public static void main(String[] args) {
		if(args.length > 1 && args[0].equals("-s")) {
			//TODO 程序的静默处理需要设计
			
			
			System.out.println("静默处理");
		}else {
			WsProcessor proc = new WsProcessor();
			
			if(System.getProperty("os.name").indexOf("Mac") != -1)
				System.setProperty("apple.laf.useScreenMenuBar", "true");
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
					| UnsupportedLookAndFeelException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			proc.openGraphicMode();
		}
	}

}
