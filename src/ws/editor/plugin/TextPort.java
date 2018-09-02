package ws.editor.plugin;

import javax.swing.JMenu;

import ws.editor.common.PluginFeature;

public abstract class TextPort implements PluginFeature {
	

	@Override
	public int pluginMark() {
		// TODO Auto-generated method stub
		return PluginFeature.IO_TextPort;
	}

	@Override
	public String getCompid() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JMenu getCustomMenu() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveOperation() {
		// TODO Auto-generated method stub

	}

}
