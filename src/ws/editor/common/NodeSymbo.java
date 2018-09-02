package ws.editor.common;
/**
 * 本接口用于TreeModel，自定义类型，主要拥有以下功能<br>
 * 1.构建树模型，提供图标和标题<br>
 * 2.对内部所有内容（几乎全部是键值对）提供迭代器<br>
 * 3.如果需要将修改行为与其他行为进行关联，需要自行实现*/
public interface NodeSymbo{
	public final static int KindGroup = 0;
	public final static int KindNode = 1;
	
	/**
	 * 设置键值对,所有的内容都是由键值对存储的
	 * 标题提供的提供由一个特殊的键关联 "NODE_TITLE"
	 * 设置标题的时候也是设置 "NODE_TITLE" 关联值*/
	void setKeyValue(String key, String value);
	
	/**
	 * 获取该索引上绑定的键名，如果超出内容范围，返回NULL
	 * @param i 索引值
	 * @return 返回键名，如果超出范围，返回NULL*/
	String getKey(int i);
	
	/**
	 * 获取绑定在key上的索引值
	 * @param key 键名
	 * @return 返回的值*/
	String getValue(String key);

	/**
	 * 获取实例种类
	 * @return 种类*/
	int kind();

	/**
	 * 文件被添加到集合节点之后，可以用它初始化父节点
	 * @param parent 父节点*/
	void initParent(GroupSymbo parent);
	
	/**
	 * 文件被添加到集合节点之后，可以用它获取父节点
	 * @reture 父节点*/
	GroupSymbo getParent();
}
