package ws.editor._plugin_define;

import java.util.ArrayList;

import javax.swing.JMenu;

import ws.editor.PluginFeature;
import ws.editor.schedule.WsProcessor;

public interface PMenuBar extends PluginFeature{

	/**
	 * 获取特定的实例
	 * @param schedule 调度模块
	 * @param id 唯一标识
	 * @return 返回新实例*/
	PMenuBar getInstance(WsProcessor schedule, String id);

	/**
	 * 刷新MenuBar实例
	 * @param elseMenus 刷新过程中将额外菜单假如MenuBar
	 * @return 返回合适的实例
	 * */
	PMenuBar refreshMenuBar(ArrayList<JMenu> elseMenus);

}
