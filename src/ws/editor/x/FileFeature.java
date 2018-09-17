package ws.editor.x;

public interface FileFeature {
	/**
	 * 获取文件名
	 * @return 文件名*/
	String fileName();
	
	/**
	 * 获取详细文件路径
	 * @return 文件路径*/
	String filePath();
	
	/**
	 * 是否只读
	 * @return 结果*/
	boolean isReadOnly();
	
	/**
	 * 是否可执行
	 * @return 结果*/
	boolean isExecutive();
}
