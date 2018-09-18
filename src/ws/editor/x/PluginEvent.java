package ws.editor.x;

public interface PluginEvent {
	/**
	 * 获取事件源
	 * @return 事件源对象*/
	Object getSource();
	/**
	 * 获取事件消息
	 * @return 消息*/
	String getMsg();
}
