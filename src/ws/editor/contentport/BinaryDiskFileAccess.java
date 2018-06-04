package ws.editor.contentport;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JMenu;

import ws.editor.PluginFeature;
import ws.editor._plugin_define.ContentPort;
import ws.editor.schedule.WsProcessor;

public class BinaryDiskFileAccess implements ContentPort {
	private String file_id = this.getClass().getName();
	private WsProcessor sch = null;
	private FileInputStream b_portin = null;
	private FileOutputStream b_portout = null;
	private File realFile = null;

	@Override
	public int getPluginMark() {
		return PluginFeature.IO_ChannelPort;
	}

	@Override
	public String getCompid() {
		return ContentPort.class.getName() + this.file_id;
	}

	@Override
	public JMenu getCustomMenu() {
		return new JMenu(this.getClass().getName());
	}

	
	@Override
	public ContentPort createNewFile(WsProcessor sch, String f_path) {
		File f = new File(f_path);
		
		File p = f.getParentFile();
		if(! p.exists())
			p.mkdirs();
		
		while(f.exists()) {
			try {
				String path = f.getCanonicalPath();
				String name = path.substring(p.getCanonicalPath().length() + 1);
				long number = (int) (Math.random() * 1000000000);
				name = "" + number + name;
				
				f = new File(p.getCanonicalPath() + File.separator + name);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			f.createNewFile();
			f_path = f.getCanonicalPath();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return this.openExistsFile(sch, f_path);
	}
	@Override
	public ContentPort openExistsFile(WsProcessor sch, String f_path) {
		File one = new File(f_path);
		if(! one.exists())
			return null;
		
		BinaryDiskFileAccess port = new BinaryDiskFileAccess();
		
		port.realFile = one;
		port.file_id = f_path;
		port.sch = sch;
		
		return port;
	}
	
	@Override
	public InputStream getInputBinaryPort() {
		if(this.b_portout != null) {
			try {
				this.b_portout.flush();
				this.b_portout.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(this.b_portin == null) {
			try {
				this.b_portin = new FileInputStream(this.realFile);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return this.b_portin;
	}

	@Override
	public void saveOperation() {}

	@Override
	public int getContentPortType() {
		return ContentPort.LocalFilePort;
	}

	@Override
	public String getPath() {
		return this.file_id;
	}

	@Override
	public FileOutputStream getOutputBinaryPort() {
		if(this.b_portin != null) {
			try {
				this.b_portin.close();
				this.b_portin = null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			if(this.b_portout == null)
				this.b_portout = new FileOutputStream(this.realFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return this.b_portout;
	}

}
