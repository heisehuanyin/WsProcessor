package ws.editor.plugin.io.binaryport;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JMenu;

import ws.editor.WsProcessor;
import ws.editor.common.PluginFeature;
import ws.editor.plugin.LocalFilePort;

public class SimpleFilePort extends LocalFilePort {
	private String file_id = this.getClass().getName();
	private WsProcessor sch = null;
	private FileInputStream b_portin = null;
	private FileOutputStream b_portout = null;
	private File realFile = null;

	@Override
	public JMenu getCustomMenu() {
		return new JMenu(this.getClass().getName());
	}

	
	@Override
	public LocalFilePort createNewFile(WsProcessor sch, String f_path) {
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
	public LocalFilePort openExistsFile(WsProcessor sch, String f_path) {
		File one = new File(f_path);
		if(! one.exists())
			return null;
		
		SimpleFilePort port = new SimpleFilePort();
		
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


	@Override
	public boolean FileIsExists() {
		// TODO Auto-generated method stub
		return false;
	}

}