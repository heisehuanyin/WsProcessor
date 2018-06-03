package ws.editor.contentport;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JMenu;

import ws.editor.PluginFeature;
import ws.editor._plugin_define.ContentPort;
import ws.editor.schedule.WsProcessor;

public class BinaryDiskFileAccess implements ContentPort {
	private String file_id = this.getClass().getName();
	private WsProcessor sch = null;
	private FileInputStream b_port = null;

	@Override
	public int getCompMark() {
		return PluginFeature.IO_ChannelPort;
	}

	@Override
	public String getCompid() {
		return this.file_id;
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
		
		if(f.exists()) {
			try {
				String path = f.getCanonicalPath();
				String name = path.substring(p.getCanonicalPath().length() + 1);
				name = "other_" + name;
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
		
		try {
			port.b_port = new FileInputStream(one);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		port.file_id = f_path;
		port.sch = sch;
		
		return port;
	}
	
	@Override
	public InputStream getBinaryPort() {
		return this.b_port;
	}

	@Override
	public void saveOperation() {}

}
