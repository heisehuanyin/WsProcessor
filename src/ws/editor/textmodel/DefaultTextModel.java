package ws.editor.textmodel;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.swing.JMenu;

import ws.editor.WsProcessor;
import ws.editor.x.PluginFeature;
import ws.editor.x.filesymbo.FileSymbo;
import ws.editor.x.textmodel.AbstractTextModel;
import ws.editor.x.textmodel.TextModel;

public class DefaultTextModel extends AbstractTextModel {
	private ArrayList<String> content = new ArrayList<>();
	private String filePath;
	private String encoding;

	@Override
	public int pluginMark() {
		return PluginFeature.IO_TextModel;
	}

	@Override
	public int upStreamMark() {
		return PluginFeature.IO_FileSymbo;
	}
	
	public void popAllText(BufferedReader reader) {
		for (;;) {
			try {
				String line = reader.readLine();
				if (line == null)
					break;
				this.content.add(line);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public TextModel openTextModel(WsProcessor core, PluginFeature upStream, String encoding) {
		DefaultTextModel rtn = new DefaultTextModel();
		rtn.filePath = ((FileSymbo)upStream).getFilePath();
		rtn.encoding = encoding;

		try {
			rtn.popAllText(new BufferedReader(new InputStreamReader(new FileInputStream(rtn.filePath), encoding)));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return rtn;
	}

	@Override
	public int getRowsCount() {
		return this.content.size();
	}

	@Override
	public String getLine(int index) {
		return this.content.get(index);
	}

	@Override
	protected void updateLine_E(int index, String str)  {
		this.content.set(index, str);
	}

	@Override
	protected void insertLine_E(int index, String str) {
		this.content.add(index, str);
	}

	@Override
	protected void removeLines_E(int indexStart) {
		for(int i=this.content.size();i > indexStart; --i ) {
			this.content.remove(i-1);
		}
	}
	@Override
	public JMenu getCustomMenu() {
		return new JMenu(this.getClass().getName());
	}

	@Override
	public void saveOperation() {
		try {
			BufferedWriter writer = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(this.filePath), encoding));
			for (String line : this.content) {
				try {
					writer.write(line);
					writer.newLine();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			writer.flush();
			writer.close();
		} catch (UnsupportedEncodingException | FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
