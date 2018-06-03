package ws.editor.configservice;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Properties;

import javax.swing.JMenu;

import ws.editor.PluginFeature;
import ws.editor._plugin_define.ConfigUnit;
import ws.editor.schedule.WsProcessor;
/**
 * 用于获取配置文件中的信息
 * ConfigUnit虽然也被设计成为插件模式，但是软件本身只需要一种配置文件格式，
 * 因此，不需要多种实例共存，软件中只保留一个config插件，factory类id恒为ConfigUnit.class.getName()*/
public class ConfigService implements ConfigUnit{
	private String cfgPath = ConfigUnit.class.getName();
	private Properties PropCollect = new Properties();
	
	public ConfigService() {}


	
	
	@Override
	public void setKeyValue(String key, String value) {
		this.PropCollect.setProperty(key, value);
	}
	
	@Override
	public String getValue(String key, String defaultValue) {
		String v = this.PropCollect.getProperty(key);
		if(v == null) {
			this.PropCollect.setProperty(key, defaultValue);
			return defaultValue;
		}
		
		return v;
	}
	
	
	
	
	
	/**
	 * 获取一个新实例，该实例与本实例没有关系
	 * @param path 配置文件位置
	 * @return 返回新的实例
	 * */
	@Override
	public ConfigService createNewPort(String path) {

		File log = new File(path);
		
		if(! log.getParentFile().exists()) {
			log.getParentFile().mkdirs();
		}
		
		if(! log.exists()) {
			try {
				log.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		ConfigService rtn = new ConfigService();
		rtn.cfgPath = path;
		
		try {
			rtn.PropCollect.load(new InputStreamReader(new FileInputStream(log),"UTF-8"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return rtn;
	}
	/**
	 * 保存操作*/
	@Override
	public void saveOperation() {
		try {
			this.PropCollect.store(new OutputStreamWriter(new FileOutputStream(this.cfgPath),"UTF-8"), "config items as list");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getCompid() {
		return ConfigUnit.class.getName()+this.cfgPath;
	}
	
	@Override
	public int getPluginMark() {
		return PluginFeature.Service_ConfigUnit;
	}

	@Override
	public JMenu getCustomMenu() {
		JMenu custom = new JMenu(this.getClass().getName());
		//TODO 详细定制菜单需要完善
		return custom;
	}
}
