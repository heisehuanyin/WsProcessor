package ws.editor.textmodel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Set;
import java.util.SortedMap;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import ws.editor.WsProcessor;
import ws.editor.x.PluginFeature;
import ws.editor.x.filesymbo.FileSymbo;
import ws.editor.x.textmodel.AbstractTextModel;
import ws.editor.x.textmodel.TextContentEvent;
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
		JMenu xrtn = new JMenu("DefaultTextModel");
		
		JMenu encodeConfig = new JMenu("编码配置");
		encodeConfig.addMenuListener(new ChangeTextCharset(encodeConfig));
		
		
		
		JMenuItem reLoad = new JMenuItem("重新载入");
		reLoad.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					content.clear();
					popAllText(new BufferedReader(new InputStreamReader(
							new FileInputStream(filePath), encoding)));
					DefaultTextModel.this.pushContentChangedEvent(
							new TextContentEvent(this, "文字完全变化"), DefaultTextModel.this);
				} catch (UnsupportedEncodingException | FileNotFoundException e1) {
					e1.printStackTrace();
				}
			}});
		
		xrtn.add(encodeConfig);
		xrtn.add(reLoad);
		
		return xrtn;
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

	public class ChangeTextCharset implements MenuListener{
		private JMenu pMenu;

		public ChangeTextCharset(JMenu pMenu) {
			this.pMenu = pMenu;
		}

		@Override
		public void menuSelected(MenuEvent e) {
			SortedMap<String, Charset> xc = Charset.availableCharsets();
			
			Set<String> set = xc.keySet();
			JMenu target = pMenu;
			for(int i=0; i<set.size(); ++i) {
				if(i>39 && i%40 == 0) {
					JMenu f = new JMenu("其他");
					target.add(f);
					target = f;
				}
				
				String name = (String) set.toArray()[i];
				JCheckBoxMenuItem nameKey = new JCheckBoxMenuItem(name, encoding == name);
				nameKey.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						((JCheckBoxMenuItem)e.getSource()).setSelected(true);
						encoding = name;
					}});
				target.add(nameKey);
			}
		}

		@Override
		public void menuDeselected(MenuEvent e) {
			pMenu.removeAll();
		}

		@Override
		public void menuCanceled(MenuEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
