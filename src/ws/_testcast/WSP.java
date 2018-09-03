package ws._testcast;

import ws.editor.WsProcessor;

public class WSP {
	

	public static void main(String[] args) {
		System.out.println("==print args============");
		System.out.println("长度："+args.length);
		int index = 1;
		for(String a:args) {
			System.out.println("["+ index + "]" + a);
			index++;
		}
		System.out.println("==proc work=============");
		
		if(args.length == 0) {
			new WSP().openCtlModel();
		}else if(args.length > 1 && args[0].equals("-s")) {
			//TODO 程序的外源程序处理需要设计
			WsProcessor proc = new WsProcessor();
			proc.operate_OpenSilentMode();
			
			System.out.println("......静默处理结束");
		}else if(args.length > 1 && args[0].equals("-w")){
			WsProcessor proc = new WsProcessor();
			
			proc.operate_OpenGraphicMode();
		}else {
			System.out.println("参数错误");
		}

	}

	private void openCtlModel() {
		
	}

}
