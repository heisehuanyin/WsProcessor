package ws.editor.plugin.filesymbo;

import javax.swing.JMenu;

import ws.editor.comn.PluginFeature;
import ws.editor.plugin.FileSymbo;

public class DefaultFileSymbo implements FileSymbo{
	private String filePath=null;

	@Override
	public int pluginMark() {
		return PluginFeature.IO_FileSymbo;
	}

	@Override
	public int upStreamMark() {
		return PluginFeature.IO_NoUpStream;
	}

	@Override
	public FileSymbo openFileModel(String fPath) {
		DefaultFileSymbo x = new DefaultFileSymbo();
		x.filePath = fPath;
		return x;
	}

	@Override
	public String getFilePath() {
		return this.filePath;
	}

	@Override
	public JMenu getCustomMenu() {
		return null;
	}

	@Override
	public void saveOperation() {}

}
