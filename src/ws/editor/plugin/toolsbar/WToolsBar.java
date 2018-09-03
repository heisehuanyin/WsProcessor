package ws.editor.plugin.toolsbar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JToolBar;

import ws.editor.WsProcessor;
import ws.editor.common.PluginFeature;
import ws.editor.plugin.PMenuBar;
import ws.editor.plugin.ToolsBar;

public class WToolsBar extends JToolBar implements ToolsBar{
	private String t_id = this.getClass().getName();
	private WsProcessor sch = null;

	@Override
	public int pluginMark() {
		return PluginFeature.Tools_Plugin;
	}

	@Override
	public JMenu getCustomMenu() {
		return new JMenu(this.getClass().getName());
	}

	@Override
	public void saveOperation() {
		
	}

	@Override
	public ToolsBar getInstance(WsProcessor wsProcessor, String string) {
		WToolsBar rtn = new WToolsBar();
		
		rtn.t_id = string;
		rtn.sch = wsProcessor;
		
		return rtn;
	}

	@Override
	public void buildToolsBarContent(PMenuBar menubar) {
		// TODO 载入配置文件，重构工具栏界面
		
		this.add(new JButton("Test"));
		JMenuItem mt = new JMenuItem("mtest");
		mt.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JDialog a = new JDialog();
				a.setSize(80, 60);
				a.setVisible(true);
			}});
		this.add(mt);
		this.add(new JCheckBoxMenuItem("qp"));
	}

	@Override
	public int upStreamMark() {
		// TODO Auto-generated method stub
		return 0;
	}

}
