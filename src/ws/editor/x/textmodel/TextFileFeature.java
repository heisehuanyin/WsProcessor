package ws.editor.x.textmodel;

import ws.editor.x.FileFeature;

public interface TextFileFeature extends FileFeature {
	/**
	 * 获取文件编码
	 * @return 文件编码*/
	String encoding();
}
