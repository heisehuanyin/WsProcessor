package ws.editor.plugin.textmodel;

import ws.editor.WsProcessor;
import ws.editor.common.PluginFeature;
import ws.editor.plugin.BinaryModel;
import ws.editor.plugin.TextModel;

public abstract class AbstractTextModel implements TextModel {
	private WsProcessor core ;
	
	@Override
	public int pluginMark() {
		return PluginFeature.IO_TextModel;
	}

	@Override
	public int upStreamMark() {
		return PluginFeature.IO_BinaryModel;
	}
	
	@Override
	public TextModel openTextModel(WsProcessor core, PluginFeature plugin) {
		this.core = core;
		if(plugin.upStreamMark() != this.upStreamMark()) {
			this.core.service_GetDefaultLogPort().errorLog(this, "TextModel上游插件组合错误");
			System.exit(0);
		}
		return openTextModel(core, (BinaryModel) plugin);
	}
	
	public abstract TextModel openTextModel(WsProcessor core, BinaryModel plugin);
	
}
