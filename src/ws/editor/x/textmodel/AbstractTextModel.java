package ws.editor.x.textmodel;

import java.util.ArrayList;

import ws.editor.x.PluginFeature;

public abstract class AbstractTextModel implements TextModel {
	private ArrayList<TextModelListener> list = new ArrayList<>();

	@Override
	public void updateLine(PluginFeature source, int index, String str) {
		this.updateLine_E(index, str);
		this.pushUpdatedEvent(new TextLineEvent(index, this, "内容行更新"), source);
	}
	protected abstract void updateLine_E(int index, String str);

	@Override
	public void insertLine(PluginFeature source, int index, String str) {
		this.insertLine_E(index, str);
		this.pushInsertedEvent(new TextLineEvent(index, this, "内容行插入"), source);
	}
	protected abstract void insertLine_E(int index, String str);

	@Override
	public void removeLines(PluginFeature source, int indexStart) {
		this.removeLines_E(indexStart);
		this.pushRemovedEvent(new TextLineEvent(indexStart, this, "内容行及以后内容被移除"), source);
	}
	protected abstract void removeLines_E(int index);

	@Override
	public void addListener(TextModelListener l) {
		if(list.contains(l))
			return;
		list.add(l);
	}

	@Override
	public void removeListener(TextModelListener l) {
		if(list.contains(l))
			list.remove(l);
	}

	private void pushUpdatedEvent(TextLineEvent e, PluginFeature except) {
		for(TextModelListener l:list)
			if(except != l)
				l.lineUpdated(e);
	}
	
	private void pushInsertedEvent(TextLineEvent e, PluginFeature except) {
		for(TextModelListener l:list)
			if(except != l)
				l.lineInserted(e);
	}
	
	private void pushRemovedEvent(TextLineEvent e, PluginFeature except) {
		for(TextModelListener l:list)
			if(except != l)
				l.lineBelowsRemoved(e);
	}
	
	protected void pushContentChangedEvent(TextContentEvent e, PluginFeature except) {
		for(TextModelListener l:list)
			if(except != l)
				l.contentChanged(e);
	}
}
