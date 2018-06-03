package ws.editor.contentport;

import java.io.InputStream;

import javax.swing.JMenu;

import ws.editor.PluginFeature;
import ws.editor._plugin_define.ContentPort;
import ws.editor.schedule.WsProcessor;

public class SimpleNetworkPort implements ContentPort {
	private String id = this.getClass().getName();

	@Override
	public int getPluginMark() {
		return PluginFeature.IO_ChannelPort;
	}

	@Override
	public String getCompid() {
		return ContentPort.class.getName()+ this.id;
	}

	@Override
	public JMenu getCustomMenu() {
		return new JMenu(this.getClass().getName());
	}

	@Override
	public void saveOperation() {
		// TODO 设计自动保存操作

	}

	@Override
	public int getContentPortType() {
		return ContentPort.NetworkPort;
	}

	@Override
	public ContentPort createNewFile(WsProcessor sch, String f_path) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ContentPort openExistsFile(WsProcessor sch, String f_path) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public InputStream getBinaryPort() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return this.id;
	}


}
