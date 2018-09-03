package ws.editor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import ws.editor.common.ItemsKey;
import ws.editor.common.PluginFeature;
import ws.editor.plugin.ConfigPort;
import ws.editor.plugin.LogPort;
import ws.editor.plugin.PMenuBar;
import ws.editor.plugin.ToolsBar;
import ws.editor.plugin.logport.AbstractLogPort;
import ws.editor.plugin.window.AbstractFrontWindow;

/**
 * 用于管理插件，本身不是插件，不需要两步实例化直接实例化得到的就是可用组件
 */
public class PluginManager {
	private Map<String, PluginFeature> factoryContainer = new HashMap<>();
	private Map<String, ArrayList<PluginFeature>> instances = new HashMap<>();
	private WsProcessor schedule;
	private String configUnitId;

	public PluginManager(WsProcessor sch) {
		this.schedule = sch;
	}

	/**
	 * 注册工厂类
	 * 
	 * @param obj
	 *            工厂类实例，用于获取新实例，除了占位本身无实际意义
	 */
	public void factory_RegisterPlugin(PluginFeature obj) {
		if (obj.pluginMark() == PluginFeature.Service_ConfigUnit)
			this.configUnitId = obj.getClass().getName();

		this.factoryContainer.put(obj.getClass().getName(), obj);
	}

	/**
	 * 获取注册工厂类
	 * 
	 * @param id_factory
	 *            唯一标志
	 * @return 工厂类，用于构建新实例
	 */
	private PluginFeature factory_GetExistsfactory(String id_factory) {
		PluginFeature factory = this.factoryContainer.get(id_factory);

		if (factory == null) {
			this.schedule.service_GetDefaultLogPort().errorLog(this, "未注册" + id_factory + "插件");
			return null;
		}
		return factory;
	}

	/**
	 * 对配置文件和默认设置对提供的factory_id进行校验，合法的id组成的factory。校验不通过返回默认Factory。
	 * 
	 * @param itemsKeyDefine
	 *            指明配置项条目作为默认配置项目
	 * @param defaultf_id
	 *            指名配置项目出现意外后的默认factory_id
	 * 
	 * @return 合适的合法的插件工厂
	 */
	public PluginFeature factory_GetConfigComp(String itemsKeyDefine, String defaultf_id) {
		String factory_id;
		// 获取合法的factory_id,未配置的话，启用dafault_id
		factory_id = this.schedule.service_GetMainConfigUnit().getValue(itemsKeyDefine, defaultf_id);

		// 该factory是否注册,未注册切换为default_id插件
		PluginFeature factory = this.factory_GetExistsfactory(factory_id);
		if (factory == null) {
			factory = this.factory_GetExistsfactory(defaultf_id);
		}
		return factory;
	}

	/**
	 * 用于一次执行所有插件的保存操作
	 */
	public void operate_SaveOperation() {
		Set<String> aset = this.instances.keySet();
		for (String url : aset) {
			ArrayList<PluginFeature> cList = this.instances.get(url);
			for (PluginFeature one : cList) {
				one.saveOperation();
			}
		}
	}

	/**
	 * 注册插件实例
	 * 
	 * @param url
	 *            内容来源地址
	 * @param obj
	 *            插件实例
	 */
	private void instance_RegisterPluginInstance(String url, PluginFeature obj) {

		ArrayList<PluginFeature> con1 = this.instances.get(url);
		if (con1 == null) {// 如果未注册过该类插件，连容器也不会有
			con1 = new ArrayList<PluginFeature>();
			this.instances.put(url, con1);
		}

		con1.add(0, obj);
	}

	/**
	 * 根据url获取关联的整条通道
	 * 
	 * @param url
	 *            内容来源标识
	 * @return 整个通道的插件组合
	 */
	private ArrayList<PluginFeature> instance_GetExistsChannelList(String url) {
		return this.instances.get(url);
	}

	// 插件实例获取接口:基础===================================================================

	/**
	 * 获取configunit用于读取和输出配置，如果存在则获取，否则新建一个<br>
	 * 该机制保证只有一种插件能够用来解析configfile，configunit不接受配置
	 * 
	 * @param path
	 *            配置文件存放路径
	 * @return 实例
	 */
	public ConfigPort instance_GetAvailableConfigUnit(String path) {
		ArrayList<PluginFeature> cList = this.instance_GetExistsChannelList(path);
		if (cList != null)
			return (ConfigPort) cList.get(0);

		PluginFeature factory = this.factory_GetExistsfactory(this.configUnitId);

		ConfigPort config = ((ConfigPort) factory).createNewPort(path);
		this.instance_RegisterPluginInstance(path, config);

		return config;
	}

	/**
	 * 获取logport用于输出log,如果已存在指定的log则获取，否则新建一个。
	 * 
	 * @param path
	 *            log文件路径
	 * @return 实例
	 */
	public LogPort instance_GetAvailableLogPort(String path) {
		ArrayList<PluginFeature> cList = this.instance_GetExistsChannelList(path);
		if (cList != null)
			return (AbstractLogPort) cList.get(0);

		PluginFeature factory = this.factory_GetConfigComp(ItemsKey.LogPortConfig, AbstractLogPort.class.getName());

		LogPort writer = ((LogPort) factory).createNewPort(path);
		this.instance_RegisterPluginInstance(path, writer);

		return writer;
	}

	/**
	 * */

	// UI
	// Component==================================================================

	public PMenuBar instance_GetNewDefaultMenubar(String string) {
		// TODO Auto-generated method stub
		return null;
	}

	public ToolsBar instance_getNewDefaultToolsBar(String string) {
		// TODO Auto-generated method stub
		return null;
	}

	public AbstractFrontWindow instance_GetNewDefaultWindow(String string) {
		// TODO Auto-generated method stub
		return null;
	}

	public void service_printPluginList() {
		Collection<PluginFeature> x = this.factoryContainer.values();
		ArrayList<PluginFeature> alist = new ArrayList<PluginFeature>(x);
		Collections.sort(alist, new SortByMark());
		System.out.println("PluginFeature.IO_NoUpStream[" + PluginFeature.IO_NoUpStream + "]");
		for (PluginFeature aplugin : alist) {
			switch (aplugin.pluginMark()) {
			case PluginFeature.Service_ConfigUnit:
				System.out.print("PluginFeature.Service_ConfigUnit[" + aplugin.pluginMark() + "]\\");
				break;
			case PluginFeature.Service_LogPort:
				System.out.print("PluginFeature.Service_LogPort[" + aplugin.pluginMark() + "]\\");
				break;
			case PluginFeature.IO_TextModel:
				System.out.print("PluginFeature.IO_TextModel[" + aplugin.pluginMark() + "]\\");
				break;

			default: {
				System.out.print("UnRecognizedPlugin[" + aplugin.pluginMark() + "]\\");
				break;
			}
			}
			System.out.print("Name:" + aplugin.getClass().getName());
			System.out.println("\\UpStream:" + aplugin.upStreamMark());
		}
	}

	private class SortByMark implements Comparator<PluginFeature> {
		@Override
		public int compare(PluginFeature o1, PluginFeature o2) {
			if (o1.pluginMark() > o2.pluginMark())
				return 1;
			if (o1.pluginMark() == o2.pluginMark())
				return 0;
			return -1;
		}
	}

}
