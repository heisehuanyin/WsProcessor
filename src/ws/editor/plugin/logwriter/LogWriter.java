package ws.editor.plugin.logwriter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import javax.swing.JMenuItem;

import ws.editor.editor.PluginFeature;

public class LogWriter implements PluginFeature{
	private BufferedWriter port = null;
	
	/**
	 * 空的，只调用本方法无法构造
	 * 实例化方法new LogWriter().getInstance(path);*/
	public LogWriter() {}

	/**
	 * 写log信息接口,每次调用接口，将log信息按照规定格式写入log文件*/
	public void write(Object obj, String msg) {
		msg = obj.getClass().getName() + ":" + msg;
		
		try {
			this.port.write(msg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	/**
	 * 有参构造器用于构建定制实例,利用已有实例构建出新实例，不会初始化已有实例
	 * @param path 实际地址作为唯一参数
	 * @return 文件路径合法返回新实例，不合法返回null
	 */
	public LogWriter getInstance(String path) {
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
		
		LogWriter rtn = new LogWriter();
		
		try {
			rtn.port = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path),"UTF-8"));
		} catch (UnsupportedEncodingException | FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return rtn;
	}
	//====================================================================
	/**
	 * 无参构造器用于构建默认实例
	 * 默认生成log位于主目录之下*/
	@Override
	public PluginFeature getDefaultInstance() {
		return this.getInstance("."+ File.separator + "log.wslog");
	}

	/**
	 * 保存过程调用flush
	 * 在框架中被设计成自动调用*/
	@Override
	public void saveOperation() {
		try {
			this.port.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public int getCompMark() {
		return PluginFeature.Service_LogWriter;
	}

	@Override
	public JMenuItem getCustomMenu() {
		return new JMenuItem(this.getClass().getName());
	}
}
