package ws.editor._plugin_define;

import ws.editor.PluginFeature;
import ws.editor.schedule.WsProcessor;

public interface ProjectManager extends PluginFeature {
	/**
	 * 获得新实例，传入sch和项目路径
	 * @param schedule 软件核心模块
	 * @param b_port 项目路径
	 * @return 返回的新实例*/
	PluginFeature openProject(WsProcessor schedule, ContentPort b_port);

}
