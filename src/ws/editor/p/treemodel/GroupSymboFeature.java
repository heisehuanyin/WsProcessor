package ws.editor.p.treemodel;

/**
 * 本接口用于TreeModel，自定义类型，相对于 {@link NodeSymboFeature} 主要拥有以下功能<br>
 * 内部子节点的修改*/
public interface GroupSymboFeature extends NodeSymboFeature{
	/**
	 * 获取子节点数量
	 * @return 数量*/
	int getChildCount();
	
	/**
	 * 获取子节点
	 * @param index 子节点在此实例中的序号
	 * @return 子节点*/
	NodeSymboFeature getChildAtIndex(int index);

	/**
	 * 从父节点中移除该节点
	 * @param one 移除的指定节点
	 * */
	void removeChild(NodeSymboFeature one);
	
	/**
	 * 节点插入到指定的位置
	 * @param node 插入节点
	 * @param index 位置*/
	void insertChildAtIndex(NodeSymboFeature node, int index);
	
	/**
	 * 获取指定节点在group中的位置
	 * @param child 指定节点
	 * @return 位序，如果为 -1 ，表明该文件不是此group的child*/
	int getChildIndex(NodeSymboFeature child);
}
