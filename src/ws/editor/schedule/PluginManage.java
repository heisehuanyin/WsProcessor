package ws.editor.schedule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import ws.editor.PluginFeature;
import ws.editor._plugin_define.ProjectManager;

/**
 * 用于管理插件，本身不是插件，不需要两步实例化直接实例化得到的就是可用组件
 * */
public class PluginManage {
	private Map<String, PluginFeature> factoryContainer = new HashMap<>();
	private Map<Integer, Map<String, PluginFeature>> instances = new HashMap<>();
	
	public PluginManage() {}

	/**
	 * 注册工厂类
	 * @param obj 工厂类实例，用于获取新实例
	 * */
	public void registerFectory(PluginFeature obj) {
		this.factoryContainer.put(obj.getCompid(), obj);
	}
	
	/**
	 * 获取注册工厂类
	 * @param id_factory 唯一标志
	 * @return 工厂类，用于构建新实例
	 * */
	public PluginFeature getRegisteredFactory(String id_factory) {
		return this.factoryContainer.get(id_factory);
	}

	/**
	 * 注册插件实例
	 * @param obj 插件实例
	 * */
	public void registerPluginInstance(PluginFeature obj) {
		
		Map<String, PluginFeature> con1 = this.instances.get(obj.getPluginMark());
		if(con1 == null) {//如果未注册过该类插件，连容器也不会有
			con1 = new HashMap<String, PluginFeature>();
			this.instances.put(obj.getPluginMark(), con1);
		}
		
		con1.put(obj.getCompid(), obj);
	}
	
	/**
	 * 获取已经注册好的实例
	 * @param pMark 插件类型，见{@link PluginFeature}
	 * @return 注册好的插件实例
	 * */
	public PluginFeature getRegisteredPluginInstance(int pMark, String id) {
		Map<String, PluginFeature> con = this.instances.get(pMark);
		if(con == null)
			return null;
		
		return con.get(id);
	}
	
	/**
	 * 用于执行所有插件的保存操作*/
	public void saveOperation() {
		Map<String, PluginFeature> array;
		array = this.instances.get(PluginFeature.Service_ConfigUnit);
		this.saveOperateOnebyOne(array);
		array = this.instances.get(PluginFeature.Service_LogPort);
		this.saveOperateOnebyOne(array);
	}
	/**
	 * 迭次保存操作*/
	private void saveOperateOnebyOne(Map<String, PluginFeature> array) {
		if(array == null)
			return;
		
		Set<String> aset = array.keySet();
		for(String str: aset) {
			PluginFeature plugin = array.get(str);
			plugin.saveOperation();
		}
	}
	
	public ArrayList<String> getProjectManagerFactoryList(){
		ArrayList<String> rtnlist = new ArrayList<>();
		Set<String> idlist = this.factoryContainer.keySet();
		for(String idone:idlist) {
			if(idone.startsWith(ProjectManager.class.getName())) {
				rtnlist.add(idone);
			}
		}
		return rtnlist;
	}

}
