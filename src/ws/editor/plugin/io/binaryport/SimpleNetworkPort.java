package ws.editor.plugin.io.binaryport;

import java.io.InputStream;
import java.io.OutputStream;

import javax.swing.JMenu;

import ws.editor.WsProcessor;
import ws.editor.common.PluginFeature;
import ws.editor.plugin.BinaryPort;

public class SimpleNetworkPort implements BinaryPort {
	private String id = this.getClass().getName();

	@Override
	public int getPluginMark() {
		return PluginFeature.IO_BinaryPort;
	}

	@Override
	public String getCompid() {
		return BinaryPort.class.getName()+ this.id;
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
		return BinaryPort.NetworkPort;
	}

	@Override
	public BinaryPort createNewFile(WsProcessor sch, String f_path) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BinaryPort openExistsFile(WsProcessor sch, String f_path) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public InputStream getInputBinaryPort() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return this.id;
	}

	@Override
	public OutputStream getOutputBinaryPort() {
		// TODO Auto-generated method stub
		return null;
	}


}
