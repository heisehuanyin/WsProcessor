package ws.editor.plugin.configservice;

import java.util.Properties;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import ws.editor.editor.PluginFeature;

public class ConfigService implements PluginFeature{
	private String tmptrans = null;
	private Properties PropCollect = new Properties();
	
	public ConfigService(String cfgPath) {
		
	}

	@Override
	public int getCompMark() {
		return PluginFeature.Service_Config;
	}

	@Override
	public JMenuItem getCustomMenu() {
		JMenu custom = new JMenu(this.getClass().getName());
		
		return custom;
	}

	@Override
	public PluginFeature getDefaultInstance() {
		// TODO Auto-generated method stub
		return new ConfigService("");
	}

	@Override
	public void saveOperation() {
		// TODO Auto-generated method stub
		
	}

}
