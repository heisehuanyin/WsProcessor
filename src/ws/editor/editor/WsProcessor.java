package ws.editor.editor;

public class WsProcessor {
	public WsProcessor (){}
	
	//注册各种组件
	public void registerComp(PluginFeature obj) {
		
	}
	
	//相应Save操作
	public void SaveOperation() {
		
	}
	//相应Exit操作
	public void ExitOperation() {
		this.SaveOperation();
		System.exit(0);
	}

	//两种启动方式
	//-w:图形界面启动，可以撰写文件，功能丰富
	//-s:静默启动，可以读取脚本执行，功能单一
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
