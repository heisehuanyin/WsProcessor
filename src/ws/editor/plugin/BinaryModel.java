package ws.editor.plugin;

import ws.editor.common.PluginFeature;

public interface BinaryModel extends PluginFeature {
	/**
	 * 创建新的 {@link BinaryModel} 实例
	 * @param filePath 文件路径
	 * @return 返回实例 */
	BinaryModel openBinaryModel(String filePath);
}
