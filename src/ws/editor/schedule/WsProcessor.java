package ws.editor.schedule;

import java.io.File;
import java.util.ArrayList;

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
import ws.editor._plugin_define.ToolsBar;
import ws.editor.configservice.ConfigService;
import ws.editor.contentport.BinaryDiskFileAccess;
import ws.editor.contentport.SimpleNetworkPort;
import ws.editor.logport.LogWriter;
import ws.editor.menubar.WMenuBar;
import ws.editor.projectmanager.SimpleProjectMake;
import ws.editor.toolsbar.WToolsBar;
import ws.editor.window.WWindow;

public class WsProcessor {
	private PluginManage manager = new PluginManage(this);
	private String wsProcessor_logPath = "."+File.separator+"Software.wslog";
	private String wsProcessor_configPath = "."+File.separator+"Software.wscfg";
	private ProjectManager projectSymbo = null;
	
	public WsProcessor (){
		this.service_RegisterNewComponent(new LogWriter());
	}
	
	/**
	 * 注册各种工厂组件,在管理器中的id格式：接口名+类名
	 * @param obj 组件的实例，这个实例作为工厂，用于派生新实例的，不能够用于工作
	 * */
	public void service_RegisterNewComponent(PluginFeature obj) {
		this.manager.factory_RegisterPluginComponent(obj);
	}
	
	
	/**
	 * 开启图形模式：实例化Processor之后，注册各种组件之后，调用本函数可以打开图形界面*/
	public void operate_OpenGraphicMode() {
		this.operate_InitDefaultGraphicPlugin();
		
		if(System.getProperty("os.name").indexOf("Mac") != -1)
			System.setProperty("apple.laf.useScreenMenuBar", "true");
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		FrontWindow win = this.manager.instance_GetNewDefaultWindow("mainwindow");
		win.displayWindow();
	}
	
	/**
	 * 开启静默模式：实例化Processor之后，注册各种组件之后，调用本函数可以打开静默模式*/
	public void operate_OpenSilentMode() {
		// TODO 默认模式的设计
	}
	
	
	
	
	/**
	 * 初始化静默模式默认的组件,能够保证最低限度的正常使用*/
	private void operate_InitDefaultSilentPlugin() {
		this.addShutDownHook();
		this.service_RegisterNewComponent(new LogWriter());
		this.service_RegisterNewComponent(new ConfigService());
		this.service_RegisterNewComponent(new BinaryDiskFileAccess());
		this.service_RegisterNewComponent(new SimpleNetworkPort());
		this.service_RegisterNewComponent(new SimpleProjectMake());
	}
	/**
	 * 初始化静默模式插件和图形模式插件，能够保证最低限度的正常使用*/
	private void operate_InitDefaultGraphicPlugin() {
		this.operate_InitDefaultSilentPlugin();
		
		this.service_RegisterNewComponent(new WWindow());
		this.service_RegisterNewComponent(new WMenuBar());
		this.service_RegisterNewComponent(new WToolsBar());
	}

	
	/**
	 * 执行保存操作
	 */
	public void operate_SaveOperation() {
		this.manager.operate_SaveOperation();
	}

	
	
	public LogPort service_GetDefaultLogPort() {
		return this.service_GetPluginManager().
				instance_GetAvailableLogPort(this.wsProcessor_logPath);
	}
	
	
	/**
	 * 获取主配置文件
	 * */
	public ConfigUnit service_GetMainConfigUnit() {
		return this.manager.instance_GetAvailableConfigUnit(wsProcessor_configPath);
	}
	
	
	
	

		
	
	
	
	
	
	
	
	
	
	
	
	
	public PluginManage service_GetPluginManager() {
		return this.manager;
	}
	
	
	
	
	
	
	
	//======================================================================================
	
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
			
			
			System.out.println("静默处理");
		}else {
			WsProcessor proc = new WsProcessor();
			
			
			
			proc.operate_OpenGraphicMode();
		}
	}


}
