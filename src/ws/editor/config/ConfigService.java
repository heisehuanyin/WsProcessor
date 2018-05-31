package ws.editor.config;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import ws.editor.CompFeature;

public class ConfigService implements CompFeature{
	private String tmptrans = null;
	
	public ConfigService(String cfgPath) {
		
	}

	@Override
	public int getCompMark() {
		return CompFeature.ConfigService;
	}

	@Override
	public JMenuItem getCustomMenu() {
		JMenu custom = new JMenu(this.getCompName());
		
		return custom;
	}
	public CompFeature getInstance(String cfgPath) {
		
	}

	@Override
	public CompFeature getInstance() {
		// TODO Auto-generated method stub
		return new ConfigService("");
	}

	@Override
	public String getCompName() {
		// TODO Auto-generated method stub
		return null;
	}

}
