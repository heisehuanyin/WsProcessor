package ws.editor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ws.editor.common.PluginFeature;
import ws.editor.plugin.ConfigPort;
import ws.editor.plugin.FileSymbo;
import ws.editor.plugin.FrontWindow;
import ws.editor.plugin.LogPort;
import ws.editor.plugin.TextModel;
import ws.editor.plugin.TreeModel;
import ws.editor.plugin.bak.PMenuBar;
import ws.editor.plugin.bak.ToolsBar;
import ws.editor.plugin.filesymbo.DefaultFileSymbo;

/**
 * 用于管理插件，本身不是插件，不需要两步实例化直接实例化得到的就是可用组件
 */
public class PluginManager {
	private Map<String, PluginFeature> factoryContainer = new HashMap<>();
	private Map<String, ArrayList<PluginFeature>> instances = new HashMap<>();
	private WsProcessor schedule;
	private String onlyoneCfgportF_id;
	private String onlyoneLogportF_id;

	public PluginManager(WsProcessor sch) {
		this.schedule = sch;
	}

	/**
	 * 注册插件
	 * 
	 * @param obj
	 *            工厂类实例，用于获取新实例，除了占位本身无实际意义
	 */
	public void factory_RegisterPlugin(PluginFeature obj) {
		if (obj.pluginMark() == PluginFeature.Service_ConfigPort)
			this.onlyoneCfgportF_id = obj.getClass().getName();
		if(obj.pluginMark() == PluginFeature.Service_LogPort)
			this.onlyoneLogportF_id = obj.getClass().getName();

		this.factoryContainer.put(obj.getClass().getName(), obj);
	}

	/**
	 * 获取注册工厂类,如果指定工厂类未注册，返回null
	 * 
	 * @param id_factory
	 *            唯一标志
	 * @return 工厂类，用于构建新实例，未注册则返回null
	 */
	private PluginFeature factory_GetExistsfactory(String id_factory) {
		PluginFeature factory = this.factoryContainer.get(id_factory);
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
			this.schedule.service_GetDefaultLogPort().errorLog(this, "配置文件错误，插件"+factory_id+"不存在");
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
	 * 自动注册插件实例
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
	 * 获取{@link ConfigPort}用于读取和输出配置，如果存在则获取，否则新建一个<br>
	 * 该机制保证只有一种插件能够用来解析configfile，{@link ConfigPort}不接受配置
	 * 
	 * @param path
	 *            配置文件存放路径，插件分组依据
	 * @return 实例
	 */
	public ConfigPort instance_GetConfigUnit(String path) {
		ArrayList<PluginFeature> cList = this.instance_GetExistsChannelList(path);
		if (cList != null)
			return (ConfigPort) cList.get(0);

		PluginFeature factory = this.factory_GetExistsfactory(this.onlyoneCfgportF_id);

		ConfigPort config = ((ConfigPort) factory).createNewPort(path);
		this.instance_RegisterPluginInstance(path, config);

		return config;
	}

	/**
	 * 获取{@link LogPort}用于输出log,如果已存在指定的{@link LogPort}则获取，否则新建一个。<br>
	 * 该机制保证只有一种插件能够输出log，保证每次启动输出的log文件格式相同，{@link LogPort}不接受配置
	 * @param path
	 *            log文件路径，插件分组依据
	 * @return 实例
	 */
	public LogPort instance_GetLogPort(String path) {
		ArrayList<PluginFeature> cList = this.instance_GetExistsChannelList(path);
		if (cList != null)
			return (LogPort) cList.get(0);

		PluginFeature factory = this.factory_GetExistsfactory(this.onlyoneLogportF_id);

		LogPort writer = ((LogPort) factory).createNewPort(path);
		this.instance_RegisterPluginInstance(path, writer);

		return writer;
	}
	public FileSymbo instance_GetFileSymbo(String path) {
		ArrayList<PluginFeature> cList = this.instance_GetExistsChannelList(path);
		if (cList != null)
			for(PluginFeature x:cList) {
				if(x.getClass().getName().equals(DefaultFileSymbo.class.getName()))
					return (FileSymbo) x;
			}
		
		PluginFeature f = this.factory_GetExistsfactory(DefaultFileSymbo.class.getName());
		FileSymbo rtn = ((FileSymbo)f).openFileModel(path);
		this.instance_RegisterPluginInstance(path, rtn);
		
		return rtn;
	}

	/**
	 * 根据提供的factory_id，获取插件实例<br>
	 * 首先搜寻是否存在实例，如果存在返回实例，不存在的话注册新实例
	 * @param f_id 插件id-插件类名
	 * @param url 通道标识，插件分组标识
	 * @param upStream 上游插件
	 * @return 正确的插件实例*/
	public TextModel instance_GetTextModelAsDescription(String f_id, String url, PluginFeature upStream) {
		ArrayList<PluginFeature> cList = this.instance_GetExistsChannelList(url);
		
		if(cList != null)
			for(PluginFeature x:cList) {
				if(x.getClass().getName().equals(f_id))
					return (TextModel) x;
			}
		
		PluginFeature f = this.factory_GetExistsfactory(f_id);
		if(f == null) {
			this.schedule.service_GetDefaultLogPort().errorLog(this, "参数f_id错误，未能找到注册插件----"+f_id );
			System.exit(0);
		}
		
		TextModel rtn = ((TextModel)f).openTextModel(schedule, upStream);
		this.instance_RegisterPluginInstance(url, rtn);
		
		return rtn;
	}
	
	public TreeModel instance_GetTreeModelAsDescription(String f_id, String url, PluginFeature upStream) {
		List<PluginFeature> cList = this.instance_GetExistsChannelList(url);
		if(cList != null)
			for(PluginFeature x:cList) {
				if(x.getClass().getName().equals(f_id)) 
					return (TreeModel) x;
			}
		PluginFeature f = this.factory_GetExistsfactory(f_id);
		if(f == null) {
			this.schedule.service_GetDefaultLogPort().errorLog(this, "参数f_id错误，未能找到注册插件----"+f_id );
			System.exit(0);
		}
		
		TreeModel rtn = ((TreeModel)f).openTreeModel(this.schedule, upStream);
		this.instance_RegisterPluginInstance(url, rtn);
		
		return rtn;
	}

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

	public FrontWindow instance_GetNewDefaultWindow(String string) {
		// TODO Auto-generated method stub
		return null;
	}

	
	// Service
	// =============================================================================

	public void service_printPluginList() {
		Collection<PluginFeature> x = this.factoryContainer.values();
		ArrayList<PluginFeature> alist = new ArrayList<PluginFeature>(x);
		Collections.sort(alist, new SortByMark());
		System.out.println("已载入插件如下============");
		System.out.println("空插件标识：PluginFeature.IO_NoUpStream:" + PluginFeature.IO_NoUpStream );
		for (PluginFeature aplugin : alist) {
			switch (aplugin.pluginMark()) {
			case PluginFeature.Service_ConfigPort:
				this.printInfo(aplugin, "PluginFeature.Service_ConfigPort");
				break;
			case PluginFeature.Service_LogPort:
				this.printInfo(aplugin, "PluginFeature.Service_LogPort");
				break;
			case PluginFeature.IO_TextModel:
				this.printInfo(aplugin, "PluginFeature.IO_TextModel");
				break;
			case PluginFeature.IO_FileSymbo:
				this.printInfo(aplugin, "PluginFeature.IO_FileSymbo");
				break;
			case PluginFeature.IO_TreeModel:
				this.printInfo(aplugin, "PluginFeature.IO_TreeModel");
				break;

			default:
				this.printInfo(aplugin, "UnrecognizedPlugin");
				break;
			
			}
		}
	}
	/**
	 * 输出插件简要信息上屏，入Log，用于简化代码，增强复用效果*/
	private void printInfo(PluginFeature plg, String typeName) {
		String msg = typeName + ":" + plg.pluginMark() + 
				"\tName:" + plg.getClass().getName() +
				"\tUpstream:" + plg.upStreamMark();
		this.schedule.service_GetDefaultLogPort().echoLog(this, msg);
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
