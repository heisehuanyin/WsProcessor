package ws.editor.plugin.contentview;

import java.awt.Component;

import javax.swing.JMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import ws.editor.WsProcessor;
import ws.editor.comn.GroupSymbo;
import ws.editor.comn.NodeSymbo;
import ws.editor.comn.PluginFeature;
import ws.editor.comn.event.NodeGeneralEvent;
import ws.editor.comn.event.NodeModifyEvent;
import ws.editor.plugin.ContentView;
import ws.editor.plugin.TreeModel;

public class DefaultTreeView extends AbstractTreeView {
	
	private TreeModel upstream = null;
	private WsProcessor core=null;
	private DefaultMutableTreeNode root = new DefaultMutableTreeNode("预设为不可见");
	private DefaultTreeModel tm = new DefaultTreeModel(root);
	private JTree tree = new JTree(tm);
	private JScrollPane sview = new JScrollPane(tree);
	

	private void initTreeView(NodeSymbo nodeSymbo) {
		nodeSymbo.addNodeEventListener(this);
		
		root.setAllowsChildren(true);
		tm.setAsksAllowsChildren(true);
		
		this.loopInsert(root, nodeSymbo);
	}
	
	private void loopInsert(DefaultMutableTreeNode parent, NodeSymbo dnode) {
		
		CustomNode xa = new CustomNode(dnode);
		this.tm.insertNodeInto(xa , parent, parent.getChildCount());
		
		if(dnode.kind() == NodeSymbo.KindGroup) {
			GroupSymbo gnode = (GroupSymbo) dnode;
			for(int i=0; i<gnode.getChildCount(); ++i) {
				NodeSymbo xx = gnode.getChildAtIndex(i);
				this.loopInsert(xa, xx);
			}
		}
	}
	
	@Override
	public ContentView openContentView(WsProcessor core, PluginFeature upStream) {
		DefaultTreeView rtn = new DefaultTreeView();
		
		rtn.upstream = (TreeModel) upStream;
		rtn.core = core;
		
		rtn.initTreeView(rtn.upstream.getNodeSymbo());
		
		return rtn;
	}


	@Override
	public Component getView() {
		// TODO Auto-generated method stub
		return sview;
	}

	@Override
	public JMenu getCustomMenu() {
		return new JMenu(this.getClass().getName());
	}

	@Override
	public void saveOperation() {

	}

	@Override
	public void nodeInsert(NodeGeneralEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void nodeRemove(NodeGeneralEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void nodeMidified(NodeModifyEvent e) {
		// TODO Auto-generated method stub

	}
	
	private class CustomNode extends DefaultMutableTreeNode{
		
		private NodeSymbo node;
		
		public CustomNode(NodeSymbo node) {
			super(node.getValue(NodeSymbo.NODENAME_KEY));
			this.node = node;
			this.setAllowsChildren((node.kind() == NodeSymbo.KindGroup)?true:false);
		}
		public NodeSymbo getNodeSymbo() {
			return this.node;
		}
	}

}
