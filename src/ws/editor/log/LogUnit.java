package ws.editor.log;

import javax.swing.JMenuItem;

import ws.editor.CompFeature;

public class LogUnit implements CompFeature{
	private String path = null;
	
	public LogUnit(String path) {
		this.path = path;
	}

	@Override
	public String getCompName() {
		// TODO Auto-generated method stub
		return ;
	}

	@Override
	public int getCompMark() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public JMenuItem getCustomMenu() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CompFeature getInstance() {
		// TODO Auto-generated method stub
		return null;
	}

}
