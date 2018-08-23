package ws.editor.plugin.menubar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import ws.editor.WsProcessor;
import ws.editor.common.FileSymbo;
import ws.editor.common.PluginFeature;
import ws.editor.plugin.PMenuBar;
import ws.editor.plugin.ProjectManager;

public class WMenuBar extends JMenuBar implements PMenuBar{
	private String mbid = this.getClass().getName();
	private WsProcessor sch = null;

	@Override
	public int getPluginMark() {
		return PluginFeature.UI_MenuBar;
	}

	@Override
	public String getCompid() {
		return PMenuBar.class.getName()+ this.mbid;
	}

	@Override
	public JMenu getCustomMenu() {
		// TODO 创建自定义菜单
		return new JMenu(this.getClass().getName());
	}
	
	@Override
	public PMenuBar getInstance(WsProcessor schedule, String id) {
		WMenuBar rtn = new WMenuBar();
		rtn.mbid = id;
		rtn.sch = schedule;
		
		return rtn;
	}
	
	@Override
	public PMenuBar refreshMenuBar(ArrayList<JMenu> elseMenus) {
		this.removeAll();
		if(elseMenus != null) {
			for(JMenu i:elseMenus) {
				this.add(i);
			}
		}
		return this;
	}
	
	

	@Override
	public void saveOperation() {
		// TODO Auto-generated method stub
		
	}
	
	

}
