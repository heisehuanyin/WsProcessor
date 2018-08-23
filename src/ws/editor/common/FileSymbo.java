package ws.editor.common;

public interface FileSymbo{
	public static int KindDir = 0;
	public static int KindFile = 0;
	
	/**
	 * 为实例赋予信息
	 * @param fileName 文件的名字
	 * @param fileurl 文件的URL
	 * @param encoding 文件的编码
	 * @return 赋值完成的实例*/
	FileSymbo initFileSymbo(String fileName, String fileurl, String encoding);

	/**
	 * 获取实例种类
	 * @return 种类*/
	int kind();

	/**
	 * 获取文件标题名称，非文件实际名称
	 * @return 标题名称*/
	String fileName();

	/**
	 * 获取文件的URL
	 * @return 文件URL*/
	String fileURL();

	/**
	 * 获取文件编码
	 * @return 文件编码*/
	String Encoding();

	/**
	 * 文件被添加到集合节点之后，可以用它初始化父节点
	 * @param parent 父节点*/
	void initParent(DirSymbo parent);
	
	/**
	 * 文件被添加到集合节点之后，可以用它获取父节点
	 * @reture 父节点*/
	DirSymbo getParent();
}
