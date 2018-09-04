package ws.editor.plugin.treemodel;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.SAXException;

import ws.editor.WsProcessor;
import ws.editor.common.GroupSymbo;
import ws.editor.common.NodeSymbo;
import ws.editor.plugin.TreeModel;

public class DefaultProjectModel extends AbstractProjectModel {

	public final static String XML_ATTR_NODENAME = "name";
	public static final String XML_ATTR_NODEFILEPATH = "filepath";
	public static final String XML_ATTR_NODEENCODING = "encoding";

	public static final String XML_NODE_TAGNAME = "Node";
	public static final String XML_GROUP_TAGNAME = "Group";

	private WsProcessor core;
	private String pPath;
	private Document doc;
	private SimpleGroupNode mNode ;
	/**
	 * 解析document节点，生成完整{@link NodeSymbo} 树内容
	 * @param doc 文档节点*/
	private void parseDom4Model(Document doc) {
		this.doc = doc;

		Element root = doc.getDocumentElement();
		this.mNode = new SimpleGroupNode(this, root);
		this.mNode.setKeyValue(NodeSymbo.NodeName_Key, 
				root.getAttribute(DefaultProjectModel.XML_ATTR_NODENAME));
		this.mNode.setKeyValue(SimpleFileNode.FILEPATH, 
				root.getAttribute(DefaultProjectModel.XML_ATTR_NODEFILEPATH));
		this.mNode.setKeyValue(SimpleFileNode.FILEENCODING,
				root.getAttribute(DefaultProjectModel.XML_ATTR_NODEENCODING));

		this.loop4ChildNodeAndContent(mNode, root);
	}

	/**
	 * 根据DOM内容信息，迭代添加母节点下每个节点下子节点
	 * @param node 母节点
	 * @param domNode 穆姐点对应{@link Element}节点*/
	private void loop4ChildNodeAndContent(NodeSymbo node, Element domNode) {
		if (node.kind() == NodeSymbo.KindNode)
			return;

		NodeList nList = domNode.getChildNodes();
		for (int i = 0; i < nList.getLength(); ++i) {
			Node one = nList.item(i);
			if (!(one instanceof Element)) {
				continue;
			}

			String tag = ((Element) one).getTagName();
			SimpleFileNode x = null;
			if (tag.equals(DefaultProjectModel.XML_GROUP_TAGNAME)) {
				x = new SimpleGroupNode(this, ((Element)one));
			}
			if (tag.equals(DefaultProjectModel.XML_NODE_TAGNAME)) {
				x = new SimpleFileNode(this, ((Element)one));
			}

			x.setKeyValue(NodeSymbo.NodeName_Key, 
					((Element) one).getAttribute(DefaultProjectModel.XML_ATTR_NODENAME));
			x.setKeyValue(SimpleFileNode.FILEPATH,
					((Element) one).getAttribute(DefaultProjectModel.XML_ATTR_NODEFILEPATH));
			x.setKeyValue(SimpleFileNode.FILEENCODING,
					((Element) one).getAttribute(DefaultProjectModel.XML_ATTR_NODEENCODING));

			((GroupSymbo) node).insertChildAtIndex(x, ((GroupSymbo) node).getChildCount());

			this.loop4ChildNodeAndContent(x, (Element) one);
		}
	}

	@Override
	protected TreeModel openProject(WsProcessor core, String pjtPath) {
		DefaultProjectModel rtn = new DefaultProjectModel();
		rtn.core = core;
		rtn.pPath = pjtPath;

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(pPath);

			rtn.parseDom4Model(doc);

		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		return rtn;
	}

	@Override
	public NodeSymbo getNodeSymbo() {
		return this.mNode;
	}

	@Override
	public JMenu getCustomMenu() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveOperation() {
		DOMImplementation impl = doc.getImplementation();
		DOMImplementationLS implLS = (DOMImplementationLS) impl.getFeature("LS", "3.0");
		LSSerializer ser = implLS.createLSSerializer();
		
		LSOutput out = implLS.createLSOutput();
		out.setEncoding("UTF-8");
		try {
			out.setByteStream(new FileOutputStream(this.pPath));
			ser.write(doc, out);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

class SimpleGroupNode extends SimpleFileNode implements GroupSymbo {
	private ArrayList<NodeSymbo> con = new ArrayList<>();

	public SimpleGroupNode(TreeModel owner, Element elm) {
		super(owner, elm);
	}
	
	@Override
	public int kind() {
		return NodeSymbo.KindGroup;
	}

	@Override
	public int getChildCount() {
		return this.con.size();
	}

	@Override
	public NodeSymbo getChildAtIndex(int index) {
		return this.con.get(index);
	}

	@Override
	public void removeChild(NodeSymbo one) {
		this.con.remove(one);
	}

	@Override
	public void insertChildAtIndex(NodeSymbo node, int index) {
		this.con.add(index, node);
	}

	@Override
	public int getChildIndex(NodeSymbo child) {
		return this.con.indexOf(child);
	}
}

class SimpleFileNode implements NodeSymbo {

	public static final String FILEENCODING = "Encoding";
	public static final String FILEPATH = "FilePath";
	
	private TreeModel model ;
	private Element elm;
	private Map<String,String> val = new HashMap<>();
	private GroupSymbo parent;
	
	public SimpleFileNode(TreeModel owner, Element elm) {
		this.model = owner;
		this.elm = elm;
	}

	@Override
	public TreeModel getModel() {
		return this.model;
	}

	@Override
	public void setKeyValue(String key, String value) {
		this.val.put(key, value);
		if(key.equals(NodeSymbo.NodeName_Key)) {
			this.elm.setAttribute(DefaultProjectModel.XML_ATTR_NODENAME,value);
		}
		if(key.equals(SimpleFileNode.FILEPATH)) {
			this.elm.setAttribute(DefaultProjectModel.XML_ATTR_NODEFILEPATH, value);
		}
		if(key.equals(SimpleFileNode.FILEENCODING)) {
			this.elm.setAttribute(DefaultProjectModel.XML_ATTR_NODEENCODING, value);
		}
	}

	@Override
	public String getKey(int i) {
		if(i >= this.val.size()) {
			return null;
		}
		Set<String> keyS = this.val.keySet();
		ArrayList<String> KA = new ArrayList<String>(keyS);
		
		return KA.get(i);
	}

	@Override
	public String getValue(String key) {
		return this.val.get(key);
	}

	@Override
	public int kind() {
		return NodeSymbo.KindNode;
	}

	@Override
	public void initParent(GroupSymbo parent) {
		this.parent = parent;
	}

	@Override
	public GroupSymbo getParent() {
		return this.parent;
	}

	@Override
	public JPopupMenu getPopupMenu() {
		// TODO Auto-generated method stub
		return null;
	}
}
