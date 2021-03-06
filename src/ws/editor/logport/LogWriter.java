package ws.editor.logport;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import ws.editor.PluginFeature;
import ws.editor._plugin_define.LogPort;
import ws.editor.schedule.WsProcessor;
/**
 * 用于输出log到文件中
 * LogPort虽然也被设计成为插件模式，但是软件本身只需要一种log文件格式，
 * 因此，不需要多种实例共存，软件中只保留一个logport插件，factory类id恒为LogPort.class.getName()*/
public class LogWriter implements LogPort{
	private BufferedWriter port = null;
	private String id_path = LogPort.class.getName();//所有工厂类的默认
	
	/**
	 * 空的，只调用本方法无法构造
	 * 实例化方法new LogWriter().getInstance(path);*/
	public LogWriter() {}

	/**
	 * 写log信息接口,每次调用接口，将log信息按照规定格式写入log文件
	 * @param obj 写信息的对象
	 * @param msg 具体信息*/
	@Override
	public void writeLog(Object obj, String msg) {
		msg = "Log:"+obj.getClass().getName() + ":" + msg;
		
		try {
			this.port.write(msg);
			this.port.newLine();
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
	@Override
	public LogWriter createNewPort(String path) {
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
		rtn.id_path = path;
		
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
	public int getPluginMark() {
		return PluginFeature.Service_LogPort;
	}

	@Override
	public JMenu getCustomMenu() {
		//TODO 详细定制菜单需要完善
		return new JMenu(this.getClass().getName());
	}

	@Override
	public String getCompid() {
		return LogPort.class.getName() + this.id_path;
	}

	@Override
	public void errorLog(Object obj, String msg) {
		msg = "Error:"+obj.getClass().getName() + ":" + msg;
		
		try {
			this.port.write(msg);
			this.port.newLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
