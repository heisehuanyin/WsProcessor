package ws.editor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import ws.editor.common.ConfigItemsKey;
import ws.editor.common.PluginFeature;
import ws.editor.plugin.ConfigUnit;
import ws.editor.plugin.ContentPort;
import ws.editor.plugin.FrontWindow;
import ws.editor.plugin.LogPort;
import ws.editor.plugin.PMenuBar;
import ws.editor.plugin.ProjectManager;
import ws.editor.plugin.ToolsBar;
import ws.editor.plugin.contentport.BinaryDiskFileAccess;
import ws.editor.plugin.contentport.SimpleNetworkPort;
import ws.editor.plugin.menubar.WMenuBar;
import ws.editor.plugin.pjt_manager.SimpleProjectMake;
import ws.editor.plugin.toolsbar.WToolsBar;
import ws.editor.plugin.window.WWindow;

/**
 * 用于管理插件，本身不是插件，不需要两步实例化直接实例化得到的就是可用组件
 */
public class PluginManager {
	private Map<String, PluginFeature> factoryContainer = new HashMap<>();
	private Map<Integer, Map<String, PluginFeature>> instances = new HashMap<>();
	private WsProcessor schedule;

	public PluginManager(WsProcessor sch) {
		this.schedule = sch;
	}

	/**
	 * 注册工厂类
	 * 
	 * @param obj
	 *            工厂类实例，用于获取新实例
	 */
	public void factory_RegisterPluginComponent(PluginFeature obj) {
		this.factoryContainer.put(obj.getCompid(), obj);
	}

	/**
	 * 获取注册工厂类
	 * 
	 * @param id_factory
	 *            唯一标志
	 * @return 工厂类，用于构建新实例
	 */
	public PluginFeature factory_GetExistsComponent(String id_factory) {
		PluginFeature factory = this.factoryContainer.get(id_factory);

		if (factory == null) {
			this.schedule.service_GetDefaultLogPort().errorLog(this, "未注册" + id_factory + "实例");
			return null;
		}
		return factory;
	}

	/**
	 * 综合配置文件和默认设置对提供的factory_id进行校验，获取由三者中合法的id组成的factory。 如果factory_id ==
	 * null，意味着从配置文件中读取配置，进行校验，否则对factory_id进行校验，校验不通过都返回默认Factory。
	 * 
	 * @param factory_id
	 *            指明的工厂类id
	 * @param configItems_Item
	 *            指明配置项条目作为默认配置项目
	 * @param defaultf_id
	 *            指名配置项目出现意外后的默认factory_id
	 * @return 合适的合法的插件工厂
	 */
	public PluginFeature factory_GetValidateComponent(String factory_id, String configItems_Item, String defaultf_id) {
		// 获取合法的factory_id
		if (factory_id == null) {
			factory_id = this.schedule.service_GetMainConfigUnit().getValue(configItems_Item, defaultf_id);
		}

		// 该factory是否注册,未注册切换为基础插件
		PluginFeature factory = this.factory_GetExistsComponent(factory_id);
		if (factory == null) {
			factory = this.factory_GetExistsComponent(defaultf_id);
		}
		return factory;
	}

	

	/**
	 * 获取可用的ProjectManager列表信息，用于新建项目菜单和打开项目菜单，
	 * 意味着，每个ProjectManager实现就不需要在自定义菜单上添加新建项目和打开项目两个选项了
	 * 
	 * @return 返回的是当前软件注册后的所有项目插件的id
	 */
	public ArrayList<String> service_GetAvailableProjectManagerList() {
		ArrayList<String> rtnlist = new ArrayList<>();
		Set<String> idlist = this.factoryContainer.keySet();
		for (String idone : idlist) {
			if (idone.startsWith(ProjectManager.class.getName())) {
				rtnlist.add(idone);
			}
		}
		return rtnlist;
	}

	/**
	 * 获取当前项目视图，此时图包含所有活动项目
	 * 
	 * @return 活动Manager视图
	 */
	public ArrayList<ProjectManager> service_GetActiveProjectManagerView() {
		ArrayList<ProjectManager> mview = new ArrayList<ProjectManager>();
		Map<String, PluginFeature> activeProjects = this.instances.get(PluginFeature.Service_ProjectManage);
		
		if(activeProjects != null) {
			Set<String> instanceID = activeProjects.keySet();

			for (String id : instanceID) {
				mview.add((ProjectManager) activeProjects.get(id));
			}
		}

		return mview;
	}

	/**
	 * 用于执行所有插件的保存操作
	 */
	public void operate_SaveOperation() {
		Map<String, PluginFeature> array;
		array = this.instances.get(PluginFeature.Service_ConfigUnit);
		this.saveOperateOnebyOne(array);
		array = this.instances.get(PluginFeature.Service_LogPort);
		this.saveOperateOnebyOne(array);
	}

	/**
	 * 迭次保存操作
	 */
	private void saveOperateOnebyOne(Map<String, PluginFeature> array) {
		if (array == null)
			return;

		Set<String> aset = array.keySet();
		for (String str : aset) {
			PluginFeature plugin = array.get(str);
			plugin.saveOperation();
		}
	}

	/**
	 * 注册插件实例
	 * 
	 * @param obj
	 *            插件实例
	 */
	private void instance_RegisterPluginInstance(PluginFeature obj) {

		Map<String, PluginFeature> con1 = this.instances.get(obj.getPluginMark());
		if (con1 == null) {// 如果未注册过该类插件，连容器也不会有
			con1 = new HashMap<String, PluginFeature>();
			this.instances.put(obj.getPluginMark(), con1);
		}

		con1.put(obj.getCompid(), obj);
	}

	/**
	 * 获取已经注册好的实例
	 * 
	 * @param pMark
	 *            插件类型，见{@link PluginFeature}
	 * @return 注册好的插件实例
	 */
	private PluginFeature instance_GetExistsPluginInstance(int pMark, String id) {
		Map<String, PluginFeature> con = this.instances.get(pMark);
		if (con == null)
			return null;

		return con.get(id);
	}
	// 插件实例获取接口:基础===================================================================

	/**
	 * 获取configunit用于读取和输出配置，如果存在则获取，否则新建一个
	 * 
	 * @param path
	 *            配置文件存放路径
	 * @return 实例
	 */
	public ConfigUnit instance_GetAvailableConfigUnit(String path) {
		PluginFeature one = this.instance_GetExistsPluginInstance(PluginFeature.Service_ConfigUnit,
				ConfigUnit.class.getName() + path);
		if (one != null)
			return (ConfigUnit) one;
		PluginFeature factory = this
				.factory_GetExistsComponent(ConfigUnit.class.getName() + ConfigUnit.class.getName());
		if (factory == null)
			return null;

		ConfigUnit config = ((ConfigUnit) factory).createNewPort(path);
		this.instance_RegisterPluginInstance(config);

		return config;
	}

	/**
	 * 获取logport用于输出log,如果已存在指定的log则获取，否则新建一个。 由于此接口在配置接口建立之前使用，因此无法根据配置文件动态获取，
	 * 根据PluginManager的机制，只能获取最后加载进入的logport插件。 规定每次启动相关位置只能放置一个logport插件。
	 * 
	 * @param path
	 *            log文件路径
	 * @return 实例
	 */
	public LogPort instance_GetAvailableLogPort(String path) {
		PluginFeature one = this.instance_GetExistsPluginInstance(PluginFeature.Service_LogPort,
				LogPort.class.getName() + path);
		if (one != null)
			return (LogPort) one;
		PluginFeature factory = this.factory_GetExistsComponent(LogPort.class.getName() + LogPort.class.getName());

		LogPort writer = ((LogPort) factory).createNewPort(path);
		this.instance_RegisterPluginInstance(writer);
		return writer;
	}

	/**
	 * 获取binaryport用于访问数据,如果文件存在就返回连接，如果不存在，返回null
	 * 根据配置文件设定，每次启动只能使用统一的一套ContentPort组件
	 * 
	 * @param fpath
	 *            文件路径
	 * @return 返回实例
	 */
	public ContentPort instance_GetContentPortFromExistsFile(String fpath) {
		PluginFeature one = this.instance_GetExistsPluginInstance(PluginFeature.IO_ChannelPort,
				ContentPort.class.getName() + fpath);
		if (one != null)
			return (ContentPort) one;

		String ConfigStr = ConfigItemsKey.DefaultLocalPort;
		String DefaultStr = ContentPort.class.getName() + BinaryDiskFileAccess.class.getName();
		if (fpath.startsWith("http")) {
			ConfigStr = ConfigItemsKey.DefaultNetworkPort;
			DefaultStr = ContentPort.class.getName() + SimpleNetworkPort.class.getName();
		}

		PluginFeature factory = this.factory_GetValidateComponent(null, ConfigStr, DefaultStr);
		ContentPort b_port = ((ContentPort) factory).openExistsFile(this.schedule, fpath);
		if (b_port != null)
			this.instance_RegisterPluginInstance(b_port);
		return b_port;
	}

	/**
	 * 获取binaryport用于访问数据，操作必然创建一个新文件，而且是与之前所有文件都不同的新文件
	 * 根据配置文件设定，每次启动智能使用统一的一套ContentPort组件
	 * 
	 * @param fpath
	 *            文件路径
	 * @return 返回实例
	 */
	public ContentPort instance_GetContentPortFromCreateNewFile(String fpath) {
		String ConfigStr = ConfigItemsKey.DefaultLocalPort;
		String DefaultStr = ContentPort.class.getName() + BinaryDiskFileAccess.class.getName();
		if (fpath.startsWith("http")) {
			ConfigStr = ConfigItemsKey.DefaultNetworkPort;
			DefaultStr = ContentPort.class.getName() + SimpleNetworkPort.class.getName();
		}

		PluginFeature factory = this.factory_GetValidateComponent(null, ConfigStr, DefaultStr);
		ContentPort one = ((ContentPort) factory).createNewFile(this.schedule, fpath);
		this.instance_RegisterPluginInstance(one);
		return one;
	}

	/**
	 * 打开项目模块，并返回此模块，如果项目已经被打开，那么返回打开的Manager，确保只有一个能够解析的实例
	 * 
	 * @param factory_id
	 *            插件种类
	 * @param b_port
	 *            项目文件接口
	 * @return 打开的项目实例
	 */
	public ProjectManager instance_OpenProjectFromFormatFile(String factory_id, ContentPort b_port) {
		String p_path = b_port.getPath();
		PluginFeature one = this.instance_GetExistsPluginInstance(PluginFeature.Service_ProjectManage,
				ProjectManager.class.getName() + p_path);
		if (one != null)
			return (ProjectManager) one;

		String formatStr = p_path.substring(p_path.lastIndexOf(".") + 1);
		PluginFeature factory = this.factory_GetValidateComponent(factory_id,
				ConfigItemsKey.getKey_ProjectManager_FOR(formatStr),
				ProjectManager.class.getName() + SimpleProjectMake.class.getName());

		ProjectManager pmake = ((ProjectManager) factory).openProject(this.schedule, b_port);
		this.instance_RegisterPluginInstance(pmake);

		return pmake;
	}

	/**
	 * 打开空白的项目文件，如果格式文件传入，会被覆盖，如果项目已经打开，那么返回null
	 * 
	 * @param factory_id
	 *            插件种类
	 * @param b_port
	 *            项目文件接口
	 * @return 打开的项目实例
	 */
	public ProjectManager instance_OpenProjectFromEmptyFile(String factory_id, ContentPort b_port) {
		String path = b_port.getPath();
		PluginFeature one = this.instance_GetExistsPluginInstance(PluginFeature.Service_ProjectManage,
				ProjectManager.class.getName() + path);
		if (one != null)
			return null;

		String formatStr = path.substring(path.lastIndexOf(".") + 1);
		PluginFeature factory = this.factory_GetValidateComponent(factory_id,
				ConfigItemsKey.getKey_ProjectManager_FOR(formatStr),
				ProjectManager.class.getName() + SimpleProjectMake.class.getName());

		ProjectManager pmake = ((ProjectManager) factory).createNewProject(this.schedule, b_port);
		this.instance_RegisterPluginInstance(pmake);

		return pmake;
	}

	// 插件实例获取接口：图形===========================================================================
	/**
	 * 获取frontwindow用于配置界面，获取何种插件有配置文件决定，配置文件错误可以返回默认实例
	 * 如果此id已经存在，那么会返回一个新id组成的实例，因此获取实例后需要更新插件id
	 * 
	 * @param id
	 *            预设窗口的id
	 * @return 新实例
	 */
	public FrontWindow instance_GetNewDefaultWindow(String id) {
		PluginFeature one = this.instance_GetExistsPluginInstance(PluginFeature.UI_Window,
				FrontWindow.class.getName() + id);
		if (one != null)
			id = "other" + id;

		// 获取合法的factory
		PluginFeature factory = this.factory_GetValidateComponent(null, ConfigItemsKey.DefaultWindow,
				FrontWindow.class.getName() + WWindow.class.getName());
		// 获取一份实例
		FrontWindow window = ((FrontWindow) factory).getInstance(this.schedule, id);
		this.instance_RegisterPluginInstance(window);

		return window;
	}

	/**
	 * 获取menubar用于构建显示界面,种类由配置文件和默认值共同指定，如果id存在，则返回新id实例，记得及时更新id
	 * 
	 * @param id
	 *            菜单栏的id
	 * @return 返回实例
	 */
	public PMenuBar instance_GetNewDefaultMenubar(String id) {
		PluginFeature one = this.instance_GetExistsPluginInstance(PluginFeature.UI_MenuBar,
				PMenuBar.class.getName() + id);
		if (one != null)
			id = "other" + id;

		PluginFeature factory = this.factory_GetValidateComponent(null, ConfigItemsKey.DefaultMenuBar,
				PMenuBar.class.getName() + WMenuBar.class.getName());

		PMenuBar menubar = ((PMenuBar) factory).getInstance(this.schedule, id);
		this.instance_RegisterPluginInstance(menubar);

		return menubar;
	}

	/**
	 * 获取toolsbar用于构建显示界面,种类由配置文件和默认值共同指定，如果id存在，则返回新id实例，记得及时更新id
	 * 
	 * @param id
	 *            实例id
	 * @return 返回实例
	 */
	public ToolsBar instance_getNewDefaultToolsBar(String string) {
		PluginFeature one = this.instance_GetExistsPluginInstance(PluginFeature.Tools_Plugin,
				ToolsBar.class.getName() + string);
		if (one != null)
			string = "other" + string;

		PluginFeature factory = this.factory_GetValidateComponent(null, ConfigItemsKey.DefaultToolsBar,
				ToolsBar.class.getName() + WToolsBar.class.getName());

		ToolsBar toolsbar = ((ToolsBar) factory).getInstance(this.schedule, string);
		this.instance_RegisterPluginInstance(toolsbar);

		return toolsbar;
	}

}
