package ws.editor.plugin.pjt_manager;

import ws.editor.common.GroupSymbo;
import ws.editor.common.NodeSymbo;

public class SimpleFileSymbo implements NodeSymbo{
	private String fileName = null;
	private String fileUrl = null;
	private String encoding = null;
	private GroupSymbo parent = null;
	
	public SimpleFileSymbo() {
	}
	@Override
	public SimpleFileSymbo initFileSymbo(String fileName, String fileurl, String encoding) {
		this.fileName = fileName;
		this.fileUrl = fileurl;
		this.encoding = encoding;
		
		return this;
	}
	@Override
	public int kind() {
		return NodeSymbo.KindFile;
	}
	@Override
	public String fileName() {
		return this.fileName;
	}
	@Override
	public String fileURL() {
		return this.fileUrl;
	}
	@Override
	public String Encoding() {
		return this.encoding;
	}
	@Override
	public void initParent(GroupSymbo p) {
		this.parent = p;
	}
	@Override
	public GroupSymbo getParent() {
		return this.parent;
	}
	
}