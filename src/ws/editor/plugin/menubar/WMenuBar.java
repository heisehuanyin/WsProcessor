package ws.editor.plugin.menubar;

import java.util.ArrayList;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import ws.editor.PluginFeature;
import ws.editor.WsProcessor;
import ws.editor.plugin.PMenuBar;

public class WMenuBar extends JMenuBar implements PMenuBar{
	private String mbid = this.getClass().getName();
	private WsProcessor sch = null;

	@Override
	public int getCompMark() {
		return PluginFeature.UI_MenuBar;
	}

	@Override
	public String getCompid() {
		return this.mbid;
	}

	@Override
	public JMenuItem getCustomMenu() {
		// TODO 创建自定义菜单
		return new JMenuItem(this.getClass().getName());
	}

	@Override
	public PluginFeature getDefaultInstance(WsProcessor schedule) {
		return this.getInstance(schedule, "menubar");
	}
	
	@Override
	public PMenuBar getInstance(WsProcessor schedule, String id) {
		WMenuBar rtn = new WMenuBar();
		rtn.mbid = id;
		rtn.sch = schedule;
		rtn.refreshMenuBar(null);
		
		return rtn;
	}
	
	@Override
	public PMenuBar refreshMenuBar(ArrayList<JMenu> elseMenus) {
		this.add(new JMenu("File"));
		this.add(new JMenu("Edit"));
		return this;
	}

	@Override
	public void saveOperation() {
		// TODO Auto-generated method stub
		
	}
	
	

}
