package ws.editor.plugin.pjt_manager;

import javax.swing.JMenu;

import ws.editor.common.GroupSymbo;
import ws.editor.common.NodeSymbo;
import ws.editor.plugin.ProjectManager;

public abstract class AbstractProjectManager implements ProjectManager {

	@Override
	public int pluginMark() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int upStreamMark() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public NodeSymbo getProjectDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSuffix() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	@Override
	public NodeSymbo createNewFile(String name, String url, String encoding) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GroupSymbo createNewGroup(String name, String url, String encoding) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean insertFileUnder(GroupSymbo dirNode, NodeSymbo fileNode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean insertFileBefore(NodeSymbo Node, NodeSymbo insertFile) {
		// TODO Auto-generated method stub
		return false;
	}

}
