package ws.editor._plugin_define;

import ws.editor.PluginFeature;
import ws.editor.schedule.WsProcessor;

public interface ProjectManager extends PluginFeature {
	/**
	 * 获得新实例，传入sch和内容端口，依照设定，传入的p_port指向的文件必定存在，格式正确
	 * @param schedule 软件核心模块
	 * @param b_port 内容端口
	 * @return 返回的新实例*/
	ProjectManager openProject(WsProcessor schedule, ContentPort p_port);

	/**关闭操作*/
	void close();
	/**
	 * 新建实例，传入schedule 和内容端口，依照设定，传入端口的内容会被清空，写进默认项目数据
	 * @param schedule 核心模块
	 * @param pport 内容端口
	 * @return 返回新实例*/
	ProjectManager createNewProject(WsProcessor schedule, ContentPort pport);

}
