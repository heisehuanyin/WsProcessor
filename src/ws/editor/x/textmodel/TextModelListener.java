package ws.editor.x.textmodel;

public interface TextModelListener {
	/**
	 * 内容被改变，具体改变不确定，需要重新载入全部内容比较稳妥
	 * @param e 事件*/
	void contentChanged(TextContentEvent e);
	
	/**
	 * 文本行插入
	 * @param e 事件*/
	void lineInserted(TextLineEvent e);
	
	/**
	 * 文本行更新
	 * @param e 事件*/
	void lineUpdated(TextLineEvent e);
	
	/**
	 * 从确定序号文本行开始，之后的文本行被完全移除
	 * @param e 事件*/
	void lineBelowsRemoved(TextLineEvent e);
}
