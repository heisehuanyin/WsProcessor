package ws.editor.projectmanager;

import ws.editor.FileSymbo;

public class SimpleFileSymbo implements FileSymbo{
	private String fileName = null;
	private String fileUrl = null;
	private String encoding = null;
	private FileSymbo parent = null;
	
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
		return FileSymbo.KindFile;
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
	public void initParent(FileSymbo p) {
		this.parent = p;
	}
	@Override
	public FileSymbo getParent() {
		return this.parent;
	}
}