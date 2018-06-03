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
import ws.editor._plugin_define.ProjectManager;
import ws.editor.configservice.ConfigService;
import ws.editor.contentport.BinaryDiskFileAccess;
import ws.editor.contentport.SimpleNetworkPort;
import ws.editor.logport.LogWriter;
import ws.editor.menubar.WMenuBar;
import ws.editor.projectmanager.SimpleProjectMake;
import ws.editor.window.WWindow;

public class WsProcessor {
	private PluginManage manager = new PluginManage();
	private String wsProcessor_logPath = "."+File.separator+"Software.wslog";
	private String wsProcessor_configPath = "."+File.separator+"Software.wscfg";
	private ProjectManager projectSymbo = null;
	
	public WsProcessor (){
		this.registerComponentFectory(new LogWriter());
	}
	
	/**
	 * 注册各种工厂组件,在管理器中的id格式：接口名+类名
	 * @param obj 组件的实例，这个实例作为工厂，用于派生新实例的，不能够用于工作
	 * */
	public void registerComponentFectory(PluginFeature obj) {
		this.manager.registerFectory(obj);
	}
	
	/**
	 * 获取特定的注册了的工厂类
	 * @param f_id 工厂类的唯一标识
	 * */
	private PluginFeature getComponentFactory(String f_id) {
		PluginFeature factory = this.manager.getRegisteredFactory(f_id);
		
		if(factory == null) {
			this.getLogPortInstance(wsProcessor_logPath).errorLog(this, "未注册"+ f_id +"实例");
			return null;
		}
		
		return factory;
	}
	
	/**
	 * 综合配置文件和默认设置对提供的factory_id进行校验，获取由三者中合法的id组成的factory。
	 * @param factory_id 指明的工厂类id
	 * @param configItems_Item 指明配置项条目作为默认配置项目
	 * @param defaultf_id 指名配置项目出现意外后的默认factory_id
	 * @return 合适的合法的插件工厂*/
	public PluginFeature getValidateFactory(String factory_id, String configItems_Item, String defaultf_id) {
		//获取合法的factory_id
		if(factory_id == null) {
			factory_id = this.getMainConfigUnit().getValue(configItems_Item, defaultf_id);
		}
		
		//该factory是否注册,未注册切换为基础窗口
		PluginFeature factory = this.getComponentFactory(factory_id);
		if(factory == null) {
			factory = this.getComponentFactory(defaultf_id);
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
	
	
	//插件实例获取接口:基础===================================================================
	/**
	 * 获取logport用于输出log,如果已存在指定的log则获取，否则新建一个。
	 * 由于此接口在配置接口建立之前使用，因此无法根据配置文件动态获取，
	 * 根据PluginManager的机制，只能获取最后加载进入的logport插件。
	 * 规定每次启动相关位置只能放置一个logport插件。
	 * @param path log文件路径
	 * @return 实例*/
	public LogPort getLogPortInstance(String path) {
		PluginFeature one = this.getExistsPlugin(PluginFeature.Service_LogPort, LogPort.class.getName()+path);
		if(one != null)
			return (LogPort) one;
		PluginFeature factory =  this.getComponentFactory(LogPort.class.getName()+LogPort.class.getName());
		
		LogPort writer = ((LogPort)factory).createNewPort(path);
		this.manager.registerPluginInstance(writer);
		return writer;
	}
	
	
	
	
	
	/**
	 * 获取configunit用于读取和输出配置，如果存在则获取，否则新建一个
	 * @param path 配置文件存放路径
	 * @return 实例*/
	public ConfigUnit getConfigUnitInstance(String path) {
		PluginFeature one = this.getExistsPlugin(PluginFeature.Service_ConfigUnit, ConfigUnit.class.getName()+path);
		if(one != null)
			return (ConfigUnit) one;
		PluginFeature factory = this.getComponentFactory(ConfigUnit.class.getName()+ConfigUnit.class.getName());
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
	 * 获取binaryport用于访问数据,如果文件存在就返回连接，如果不存在，就创建文件，再返回连接
	 * 根据配置文件设定，每次启动只能使用统一的一套ContentPort组件
	 * @param fpath port的id
	 * @return 返回实例*/
	public ContentPort getBinaryPort(String fpath) {
		PluginFeature one = this.getExistsPlugin(PluginFeature.IO_ChannelPort, ContentPort.class.getName() + fpath);
		if(one != null)
			return (ContentPort) one;
		
		String ConfigStr = ConfigItems.DefaultLocalPort;
		String DefaultStr = ContentPort.class.getName()+BinaryDiskFileAccess.class.getName();
		if(fpath.startsWith("http")) {
			ConfigStr = ConfigItems.DefaultNetworkPort;
			DefaultStr = ContentPort.class.getName()+SimpleNetworkPort.class.getName();
		}
		
		PluginFeature factory = this.getValidateFactory(null, ConfigStr, DefaultStr);
		ContentPort b_port = ((ContentPort)factory).openExistsFile(this, fpath);
		if(b_port == null)
			b_port = ((ContentPort)factory).createNewFile(this, fpath);
		this.manager.registerPluginInstance(b_port);
		return b_port;
	}

	/**
	 * 替换当前活动项目
	 * @param pjt_symbo 项目符号*/
	public void ReplaceProjectManager(ProjectManager pjt_symbo) {
		if(this.projectSymbo != null) {
			this.projectSymbo.saveOperation();
			this.projectSymbo.close();
		}
		this.projectSymbo = pjt_symbo;
	}
	
	/**
	 * 根据指定条件返回特定的项目模块
	 * @param factory_id 插件种类
	 * @param p_path 项目路径
	 * @return 打开的项目实例*/
	public ProjectManager getProjectManager(String factory_id, ContentPort b_port) {
		String p_path = b_port.getPath();
		PluginFeature one = this.getExistsPlugin(PluginFeature.Service_ProjectManage, 
				ProjectManager.class.getName()+p_path);
		if(one != null)
			return (ProjectManager) one;
		
		String formatStr = p_path.substring(p_path.lastIndexOf(".")+1);
		PluginFeature factory = this.getValidateFactory(factory_id,
				ConfigItems.getKey_ProjectManager_FOR(formatStr), SimpleProjectMake.class.getName());
		
		ProjectManager pmake = ((ProjectManager)factory).openProject(this, b_port);
		if(pmake == null)
			pmake = ((ProjectManager)factory).newPorject(this, b_port);
		
		
		return null;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//插件实例获取接口：图形===========================================================================
	/**
	 * 获取frontwindow用于配置界面，获取何种插件有配置文件决定，配置文件错误可以返回默认实例
	 * 如果此id已经存在，那么会返回一个新id组成的实例，因此获取实例后需要更新插件id
	 * @param id 预设窗口的id
	 * @return 新实例*/
	public FrontWindow getNewDefaultWindow(String id) {
		PluginFeature one = this.getExistsPlugin(PluginFeature.UI_Window, id);
		if(one != null)
			id = "other" + id;
		
		//获取合法的factory
		PluginFeature factory = this.getValidateFactory(null,ConfigItems.DefaultWindow, WWindow.class.getName());
		//获取一份实例
		FrontWindow window = ((FrontWindow)factory).getInstance(this, id);
		this.manager.registerPluginInstance(window);
		return window;
	}
	/**
	 * 获取menubar用于构建显示界面,种类由配置文件和默认值共同指定，如果id存在，则返回新id实例，记得及时更新id
	 * @param id 菜单栏的id
	 * @return 返回实例*/
	public PMenuBar getNewDefaultMenubar(String id) {
		PluginFeature one = this.getExistsPlugin(PluginFeature.UI_MenuBar, id);
		if(one != null)
			id = "other" + id;
		
		PluginFeature factory = this.getValidateFactory(null,ConfigItems.DefaultMenuBar, WMenuBar.class.getName());
		
		PMenuBar menubar = ((PMenuBar)factory).getInstance(this, id);
		this.manager.registerPluginInstance(menubar);
		return menubar;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//======================================================================================
	
	
	
	
	/**
	 * 开启图形模式：实例化Processor之后，注册各种组件之后，调用本函数可以打开图形界面*/
	public void openGraphicMode() {
		this.initDefaultGraphicPlugin();
		
		FrontWindow win = this.getNewDefaultWindow("mainwindow");
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
		this.registerComponentFectory(new LogWriter());
		this.registerComponentFectory(new ConfigService());
		this.registerComponentFectory(new SimpleProjectMake());
	}
	/**
	 * 初始化静默模式插件和图形模式插件，能够保证最低限度的正常使用*/
	private void initDefaultGraphicPlugin() {
		this.initDefaultSilentPlugin();
		
		this.registerComponentFectory(new WWindow());
		this.registerComponentFectory(new WMenuBar());
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