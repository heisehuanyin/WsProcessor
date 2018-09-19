package ws.editor.x.textmodel;

public class TextLineEvent extends TextContentEvent {

	private int index;

	public TextLineEvent(int line, Object source, String msg) {
		super(source, msg);
		this.index=line;
	}
	
	public int getLineIndex() {
		return index;
	}

}
