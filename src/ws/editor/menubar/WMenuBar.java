package ws.editor.menubar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import ws.editor.FileSymbo;
import ws.editor.PluginFeature;
import ws.editor._plugin_define.PMenuBar;
import ws.editor._plugin_define.ProjectManager;
import ws.editor.schedule.WsProcessor;

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
		this.add(this.rebuildMenuP_M());
		
		if(elseMenus != null) {
			for(JMenu i:elseMenus) {
				this.add(i);
			}
		}
		return this;
	}
	
	private JMenu rebuildMenuP_M(){
		JMenu rtn = new JMenu("项目集");
		rtn.addMenuListener(new P_M_src_Listener(this.sch));
		return rtn;
	}
	private class P_M_src_Listener implements MenuListener{
		private WsProcessor schedule;
		public P_M_src_Listener(WsProcessor sch) {
			this.schedule = sch;
		}
		@Override
		public void menuSelected(MenuEvent e) {
			ArrayList<ProjectManager> v = this.schedule.operate_GetActiveProjectManagerView();
			ArrayList<String> avaPMList = this.schedule.operate_GetAvailableProjectManagerList();
			JMenu source = (JMenu) e.getSource();
			JMenu newProject = new JMenu("新建项目");
			source.add(newProject);
			JMenu openProject = new JMenu("打开项目");
			source.add(openProject);
			JMenu clearProject = new JMenu("清理项目");
			source.add(clearProject);
			
			for(String mitem:avaPMList) {
				
				newProject.add(mitem);
				openProject.add(mitem);
			}
			source.addSeparator();
			for(ProjectManager i:v) {
				FileSymbo pjt = i.getProjectDescription();
				JMenu itemcfg = i.getCustomMenu();
				itemcfg.setText(pjt.fileName());
				source.add(itemcfg);
			}
		}
		@Override
		public void menuDeselected(MenuEvent e) {
			JMenu source = (JMenu) e.getSource();
			source.removeAll();
		}
		@Override
		public void menuCanceled(MenuEvent e) {
			// TODO Auto-generated method stub
			
		}

	}

	@Override
	public void saveOperation() {
		// TODO Auto-generated method stub
		
	}
	
	

}
