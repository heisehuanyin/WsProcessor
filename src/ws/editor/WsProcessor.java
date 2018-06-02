package ws.editor;

import java.io.File;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import ws.editor.plugin.ConfigUnit;
import ws.editor.plugin.FrontWindow;
import ws.editor.plugin.LogPort;
import ws.editor.plugin.PMenuBar;
import ws.editor.plugin.configservice.ConfigService;
import ws.editor.plugin.logwriter.LogWriter;
import ws.editor.plugin.menubar.WMenuBar;
import ws.editor.plugin.window.WWindow;

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
	 * @param id 工厂类的唯一标识
	 * */
	private PluginFeature getCompFactory(String id) {
		return this.manager.getRegisteredFactory(id);
	}
	
	/**
	 * 获取已经存在的实例，这是一个集合接口
	 * @param pluginMark 插件种类,定义域PluginFeature接口中
	 * @param id 插件的唯一标识
	 * @return 返回新的插件实例，如果不存在则返回null
	 * */
	private PluginFeature getExistsPlugin(int pluginMark, String id) {
		return this.manager.getRegisteredPluginInstance(pluginMark, id);
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
		
		LogPort writer = ((LogPort)factory).getInstance(path);
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
			this.getLogPortInstance(wsProcessor_logPath).writeLog(this, "未注册"+ConfigUnit.class.getName()+"实例");
		ConfigUnit config = ((ConfigUnit)factory).getInstance(path);
		this.manager.registerPluginInstance(config);
		return config;
	}
	/**
	 * 获取frontwindow用于显示界面,可以指定frontwindow种类
	 * @param factory_id 指明window种类，用于获取多种控件，值为null获取配置中控件
	 * @param id 窗口的id
	 * @return 实例*/
	public FrontWindow getAppWindow(String factory_id, String id) {
		PluginFeature one = this.getExistsPlugin(PluginFeature.UI_Window, id);
		if(one != null)
			return (FrontWindow) one;
		
		String configStr;
		if(factory_id ==null)
			configStr = this.getMainConfigUnit().getValue(ConfigItems.WindowType, WWindow.class.getName());
		else
			configStr = factory_id;
		PluginFeature factory = this.getCompFactory(configStr);
		if(factory == null) {
			this.getLogPortInstance(wsProcessor_logPath).writeLog(this, "未注册"+configStr +"<"+FrontWindow.class.getName()+">实例");
			return null;
		}
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
		
		String configStr ;
		if(factory_id == null)
			configStr = this.getMainConfigUnit().getValue(ConfigItems.MenuBarType, WMenuBar.class.getName());
		else
			configStr = factory_id;
		PluginFeature factory = this.getCompFactory(configStr);
		if(factory == null)
			this.getLogPortInstance(wsProcessor_logPath).writeLog(this, "未注册"+configStr+"<"+PMenuBar.class.getName()+">实例");
		
		PMenuBar menubar = ((PMenuBar)factory).getInstance(this, id);
		this.manager.registerPluginInstance(menubar);
		return menubar;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//======================================================================================
	/**
	 * 获取主配置文件
	 * */
	public ConfigUnit getMainConfigUnit() {
		return this.getConfigUnitInstance(wsProcessor_configPath);
	}
	
	
	
	
	public void openGraphicMode() {
		FrontWindow win = this.getAppWindow(null, "mainwindow");
		win.displayWindow();
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
			proc.registerCompFectory(new LogWriter());
			proc.registerCompFectory(new ConfigService());
			proc.registerCompFectory(new WWindow());
			proc.registerCompFectory(new WMenuBar());
			
			
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
