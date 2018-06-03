package ws.editor._plugin_define;

import ws.editor.PluginFeature;
import ws.editor.schedule.WsProcessor;

public interface ProjectManager extends PluginFeature {
	/**
	 * 获得新实例，传入sch和项目路径
	 * @param schedule 软件核心模块
	 * @param b_port 项目路径
	 * @return 返回的新实例*/
	ProjectManager openProject(WsProcessor schedule, ContentPort p_port);

	/**关闭操作*/
	void close();

	/**
	 * 创建新工程实例，传入项目路径和工厂
	 * @param schedule 软件核心模块
	 * @param p_port 项目路径
	 * @return 返回的新实例*/
	ProjectManager newPorject(WsProcessor schedule, ContentPort p_port);

}
