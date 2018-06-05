package ws.editor;

public interface DirSymbo extends FileSymbo {
	/**
	 * 添加一个文件符号
	 * @param one 文件符号*/
	void addChild(FileSymbo one);
	/**
	 * 获取子文件数量
	 * @return 数量*/
	int getChildCount();
	/**
	 * 获取子文件
	 * @param index 子文件在此实例中的序号
	 * @return 子文件*/
	FileSymbo getChildAtIndex(int index);

	/**
	 * 从父文件中移除该文件
	 * @param one 移除的指定文件
	 * */
	void removeChild(FileSymbo one);
	/**
	 * 文件插入到指定的位置
	 * @param node 插入文件
	 * @param index 位置*/
	void insertChildAtIndex(FileSymbo node, int index);
	/**
	 * 获取指定文件在group中的位置
	 * @param child 指定文件
	 * @param 位序，如果为 -1 ，表明该文件不是此group的child*/
	int getChildIndex(FileSymbo child);
}
