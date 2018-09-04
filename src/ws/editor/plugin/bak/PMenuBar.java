package ws.editor.plugin.bak;

import java.util.ArrayList;

import javax.swing.JMenu;
import javax.swing.JMenuBar;

import ws.editor.WsProcessor;
import ws.editor.common.PluginFeature;

public abstract class PMenuBar extends JMenuBar implements PluginFeature{

	/**
	 * 获取特定的空白实例
	 * @param schedule 调度模块
	 * @param id 唯一标识
	 * @return 返回新实例*/
	public abstract PMenuBar getInstance(WsProcessor schedule, String id);

	/**
	 * 构建MenuBar实例
	 * @param elseMenus 刷新过程中将额外菜单加入MenuBar
	 * @return 返回合适的实例
	 * */
	public abstract PMenuBar refreshMenuBar(ArrayList<JMenu> elseMenus);

}
