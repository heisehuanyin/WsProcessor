package ws.editor.projectmanager;

import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import ws.editor.ConfigItems;
import ws.editor.DirSymbo;
import ws.editor.FileSymbo;
import ws.editor.PluginFeature;
import ws.editor._plugin_define.ContentPort;
import ws.editor._plugin_define.ProjectManager;
import ws.editor.contentport.BinaryDiskFileAccess;
import ws.editor.schedule.WsProcessor;

public class SimpleProjectMake implements ProjectManager{
	private String pjt_id = this.getClass().getName();
	private WsProcessor sch = null;
	private FileSymbo p_tree = null;
	
	public SimpleProjectMake() {}

	@Override
	public int getPluginMark() {
		return PluginFeature.Service_ProjectManage;
	}

	@Override
	public String getCompid() {
		return ProjectManager.class.getName()+this.pjt_id;
	}

	@Override
	public JMenu getCustomMenu() {
		JMenu rtn = new JMenu(this.getClass().getName());
			JMenu project = new JMenu("项目管理");
			rtn.add(project);
				JMenuItem newP = new JMenuItem("新建项目");
				project.add(newP);
				JMenuItem switchP = new JMenuItem("切换项目");
				project.add(switchP);
				JMenuItem renameP = new JMenuItem("重命名本项目");
				project.add(renameP);
				JMenuItem clearP = new JMenuItem("清理本项目");
				project.add(clearP);
				JMenuItem closeP = new JMenuItem("关闭本项目");
				project.add(closeP);
				JMenuItem delP = new JMenuItem("删除本项目");
				project.add(delP);
			
		
			JMenu file = new JMenu("文件管理");
			rtn.add(file);
				JMenuItem newf = new JMenuItem("新建文件");
				file.add(newf);
				JMenuItem openf = new JMenuItem("打开文件");
				file.add(openf);
				JMenuItem renamef = new JMenuItem("重命名文件");
				file.add(renamef);
				JMenuItem savef = new JMenuItem("保存所有文件");
				file.add(savef);
				JMenuItem closef = new JMenuItem("关闭文件");
				file.add(closef);
		
		return rtn;
	}

	
	@Override
	public ProjectManager openProject(WsProcessor schedule, ContentPort p_file) {
		SimpleProjectMake rtn = new SimpleProjectMake();
		
		rtn.sch = schedule;
		rtn.p_tree = this.parseProject(p_file);
		rtn.pjt_id = p_file.getPath();
		
		return rtn;
	}
	@Override
	public ProjectManager newPorject(WsProcessor schedule, ContentPort p_port) {
		// TODO 新建项目需要设计
		
		return null;
	}
	
	
	/* xml.wspjt的格式如下
	 * <? xml version="1.0" ?>
	 * <project name="pjt_name" fileurl="./xxx.xx" encoding="utf-8">
	 * 		<group name="group_name" fileurl="./xxxx.xx" encoding="utf-8">
	 * 			<file name="file_name" fileurl="" encoding="utf-8" />
	 * 		</group>
	 * </project>
	 * */
	/**
	 * 打开项目文件，读取项目配置，返回项目描述
	 * @param b_port 项目文件管理端口
	 * @return 项目描述，第一个节点是项目整体描述节点*/
	private FileSymbo parseProject(ContentPort b_port) {
		FileSymbo pjt_s = null;
		XMLStreamReader reader;
		
		try {
			reader = XMLInputFactory.newInstance().
					createXMLStreamReader(new InputStreamReader(b_port.getBinaryPort(), ConfigItems.DefaultFileEncoding_Value));
			while(reader.hasNext()){
				int eventNum = reader.next();
				if(eventNum == XMLStreamConstants.START_ELEMENT){
					if(reader.getLocalName().equals("file")) {
						FileSymbo node = new SimpleFileSymbo();
						((DirSymbo)pjt_s).addChild(node);
						pjt_s = node;
					}else {
						if(pjt_s == null)
							pjt_s = new SimpleDirSymbo();
						else {
							FileSymbo node = new SimpleDirSymbo();
							((DirSymbo)pjt_s).addChild(node);
							pjt_s = node;
						}
					}
					
					String fileName = null,fileURL = null,fileEncoding = null;
					for(int i = reader.getAttributeCount()-1;i>=0;--i) {
						String key = reader.getAttributeLocalName(i);
						if(key.equals("name"))
							fileName = reader.getAttributeValue(i);
						if(key.equals("fileurl"))
							fileURL = reader.getAttributeValue(i);
						if(key.equals("encoding"))
							fileEncoding = reader.getAttributeValue(i);
					}
					
					pjt_s.initFileSymbo(fileName, fileURL, fileEncoding);
				}
				if(eventNum == XMLStreamConstants.END_ELEMENT){
					pjt_s = pjt_s.getParent();
				}
			}
		} catch (UnsupportedEncodingException | XMLStreamException | FactoryConfigurationError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pjt_s;
	}

	@Override
	public void saveOperation() {
		// TODO 保存操作需要设计
		
	}

	@Override
	public void close() {
		// TODO 关闭操作需要设计
		
	}
	
	
}
