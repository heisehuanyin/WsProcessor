package ws.editor.plugin.contentview;

import java.awt.Component;

import javax.swing.JMenu;

import ws.editor.WsProcessor;
import ws.editor.comn.NodeSymbo;
import ws.editor.comn.PluginFeature;
import ws.editor.comn.event.NodeGeneralEvent;
import ws.editor.comn.event.NodeModifyEvent;
import ws.editor.plugin.ContentView;
import ws.editor.plugin.TreeModel;

public class DefaultTreeView extends AbstractTreeView {
	
	private TreeModel upstream = null;
	private WsProcessor core=null;
	

	private void initTreeView(NodeSymbo nodeSymbo) {
		
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
		return null;
	}

	@Override
	public JMenu getCustomMenu() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveOperation() {
		// TODO Auto-generated method stub

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

}
