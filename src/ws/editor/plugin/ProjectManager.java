package ws.editor.plugin;

import ws.editor.WsProcessor;
import ws.editor.common.GroupSymbo;
import ws.editor.common.NodeSymbo;
import ws.editor.common.PluginFeature;

public interface ProjectManager extends PluginFeature {
	/**
	 * 获得新实例，传入sch和内容端口，依照设定，传入的p_port指向的文件必定存在，格式正确
	 * @param schedule 软件核心模块
	 * @param b_port 内容端口
	 * @return 返回的新实例*/
	ProjectManager openProject(WsProcessor schedule);

	/**
	 * 新建实例，传入schedule 和内容端口，依照设定，传入端口的内容会被清空，写进默认项目数据
	 * @param schedule 核心模块
	 * @param pport 内容端口
	 * @return 返回新实例*/
	ProjectManager createNewProject(WsProcessor schedule);

	/**
	 * 获取项目描述，返回的是由一个文件描述实例组成的项目树
	 * @return 返回的描述实例*/
	NodeSymbo getProjectDescription();

	/**
	 * 获取针对的文件后缀名，相当于注册key，当打开该格式文件的时候，可以检索到本manager
	 * @return 返回后缀名*/
	String getSuffix();
	
	/**关闭操作*/
	void close();
	
	/**
	 * 创建一个文件，利用参数信息
	 * @param name 文件标题名称，不是文件真实名称，而是文件在项目中的名称
	 * @param url 文件的真实路径
	 * @param encoding 设定的文件编码
	 * @return 返回的实例*/
	NodeSymbo createNewFile(String name, String url, String encoding);

	/**
	 * 利用参数信息,创建一个group
	 * @param name 项目标题名称
	 * @param url 文件的真实路径，每个group是仅仅逻辑上存在，实际上是一个文件，这个文件中可以记录一些group的信息
	 * @param encoding 设定的文件编码
	 * @return 返回的实例*/
	GroupSymbo createNewGroup(String name, String url, String encoding);

	/**
	 * 在group节点下插入文件节点，将文件插入子节点集合中
	 * @param dirNode group节点
	 * @param fileNode 被插入的节点
	 * @return boolean操作成功或失败*/
	boolean insertFileUnder(GroupSymbo dirNode, NodeSymbo fileNode);

	/**
	 * 在文件节点之上插入节点，两个节点同处于一个父节点的子集中
	 * @param Node 基准节点，新节点会被插入到此节点的位置
	 * @param insertFile 新文件节点，被插入节点
	 * @return boolean 操作成功或失败*/
	boolean insertFileBefore(NodeSymbo Node, NodeSymbo insertFile);



}
