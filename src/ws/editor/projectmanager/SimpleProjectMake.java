package ws.editor.projectmanager;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import ws.editor.ConfigItems;
import ws.editor.DirSymbo;
import ws.editor.FileSymbo;
import ws.editor.PluginFeature;
import ws.editor._plugin_define.ContentPort;
import ws.editor._plugin_define.ProjectManager;
import ws.editor.schedule.WsProcessor;

public class SimpleProjectMake implements ProjectManager {
	private String pjt_path = this.getClass().getName();
	private WsProcessor sch = null;
	private FileSymbo p_tree = null;
	private ContentPort cport = null;

	public SimpleProjectMake() {
	}

	@Override
	public int getPluginMark() {
		return PluginFeature.Service_ProjectManage;
	}

	@Override
	public String getCompid() {
		return ProjectManager.class.getName() + this.pjt_path;
	}

	@Override
	public JMenu getCustomMenu() {
		JMenu rtn = new JMenu(this.getClass().getName());
		JMenu project = new JMenu("项目管理");
		rtn.add(project);
		JMenuItem renameP = new JMenuItem("重命名项目");
		project.add(renameP);
		JMenuItem clearP = new JMenuItem("清理项目");
		project.add(clearP);
		JMenuItem closeP = new JMenuItem("关闭项目");
		project.add(closeP);
		JMenuItem delP = new JMenuItem("删除项目");
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
		rtn.pjt_path = p_file.getPath();
		rtn.cport = p_file;

		return rtn;
	}

	/*
	 * xml.wspjt的格式如下 
	 * <? xml version="1.0" ?> <project name="pjt_name"
	 * fileurl="./xxx.xx" encoding="utf-8"> <group name="group_name"
	 * fileurl="./xxxx.xx" encoding="utf-8"> <file name="file_name" fileurl=""
	 * encoding="utf-8" /> </group> </project>
	 */
	/**
	 * 打开项目文件，读取项目配置，返回项目描述,如果是空白项目，根据解析会生成一个项目根节点。
	 * 
	 * @param b_port
	 *            项目文件管理端口
	 * @return 项目描述，第一个节点是项目整体描述节点
	 */
	private FileSymbo parseProject(ContentPort b_port) {
		FileSymbo pjt_s = null;
		XMLStreamReader reader;

		try {
			reader = XMLInputFactory.newInstance().createXMLStreamReader(
					new InputStreamReader(b_port.getInputBinaryPort(), ConfigItems.DefaultFileEncoding_Value));
			while (reader.hasNext()) {
				int eventNum = reader.next();
				if (eventNum == XMLStreamConstants.START_ELEMENT) {
					if (reader.getLocalName().equals("file")) {
						FileSymbo node = new SimpleFileSymbo();
						((DirSymbo) pjt_s).addChild(node);
						pjt_s = node;
					} else {
						if (pjt_s == null)
							pjt_s = new SimpleDirSymbo();
						else {
							FileSymbo node = new SimpleDirSymbo();
							((DirSymbo) pjt_s).addChild(node);
							pjt_s = node;
						}
					}

					String fileName = null, fileURL = null, fileEncoding = null;
					for (int i = reader.getAttributeCount() - 1; i >= 0; --i) {
						String key = reader.getAttributeLocalName(i);
						if (key.equals("name"))
							fileName = reader.getAttributeValue(i);
						if (key.equals("fileurl"))
							fileURL = reader.getAttributeValue(i);
						if (key.equals("encoding"))
							fileEncoding = reader.getAttributeValue(i);
					}

					pjt_s.initFileSymbo(fileName, fileURL, fileEncoding);
				}
				if (eventNum == XMLStreamConstants.END_ELEMENT) {
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
	public ProjectManager createNewProject(WsProcessor schedule, ContentPort pport) {
		OutputStreamWriter out;
		try {
			out = new OutputStreamWriter(pport.getOutputBinaryPort(), "UTF-8");
			out.write("<?xml version=\"1.0\"?>\n"
					+ "<project name=\"pjt_name\" fileurl=\"./xxx.xx\" encoding=\"utf-8\">\n" + "</project>");
			out.flush();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return this.openProject(schedule, pport);
	}

	@Override
	public void saveOperation() {
		XMLOutputFactory factory = XMLOutputFactory.newInstance();
		XMLStreamWriter writer;
		try {
			writer = factory.createXMLStreamWriter(new OutputStreamWriter(this.cport.getOutputBinaryPort(), "UTF-8"));
			writer.writeStartDocument();
			this.saveProjectNode(this.p_tree, writer);
			writer.writeEndDocument();
		} catch (UnsupportedEncodingException | XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void saveProjectNode(FileSymbo node, XMLStreamWriter writer) {
		try {
			if (node.getParent() == null) {
				writer.writeStartElement("project");
			} else {
				if(node.kind() == FileSymbo.KindFile)
					writer.writeStartElement("file");
				else
					writer.writeStartElement("group");
			}
			
			writer.writeAttribute("name", node.fileName());
			writer.writeAttribute("fileurl", node.fileURL());
			writer.writeAttribute("encoding", node.Encoding());
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
		
		if(node.kind() != FileSymbo.KindFile) {
			int childCount = ((DirSymbo)node).getChildCount();
			for(int i=0;i<childCount;++i) {
				this.saveProjectNode(node, writer);
			}
		}
		
		try {
			writer.writeEndElement();
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void close() {
		// TODO 关闭操作需要设计

	}
	
	@Override
	public FileSymbo getProjectDescription() {
		return this.p_tree;
	}

}
