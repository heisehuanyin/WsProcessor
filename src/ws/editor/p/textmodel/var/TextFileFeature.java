package ws.editor.p.textmodel.var;

import ws.editor.p.FileFeature;

public interface TextFileFeature extends FileFeature {
	/**
	 * 获取文件编码
	 * @return 文件编码*/
	String encoding();
}
