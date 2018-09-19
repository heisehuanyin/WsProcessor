package ws.editor.x.textmodel;

import ws.editor.x.PluginEvent;

public class TextContentEvent implements PluginEvent {

	private Object srouce;
	private String msg;

	public TextContentEvent(Object source, String msg) {
		this.srouce = source;
		this.msg = msg;
	}
	
	@Override
	public Object getSource() {
		return this.srouce;
	}

	@Override
	public String getMsg() {
		return msg;
	}

}
