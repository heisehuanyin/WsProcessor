package ws._testcast;

import java.util.Scanner;

import ws.editor.WsProcessor;

public class WSP {
	private WsProcessor wsp = null;

	private void openCtlModel() {
		wsp = new WsProcessor();
		wsp.operate_SilentModel();
		
		
		Scanner s = new Scanner(System.in);
		for (;;) {
			String cmd = s.nextLine();
			if (cmd.equals("exit();"))
				return;
			if(cmd.equals("pluginList();"))
				this.printPlugin();
		}
	}
	private void printPlugin() {
		this.wsp.service_GetPluginManager().service_printPluginList();
	}


	private void openSourceModel() {
		// TODO 程序的外源程序处理需要设计

	}

	private void openGraphicsModel() {
		wsp = new WsProcessor();

		wsp.operate_OpenGraphicMode();
	}


	public static void main(String[] args) {
		System.out.println("==print args============");
		System.out.println("长度：" + args.length);
		int index = 1;
		for (String a : args) {
			System.out.println("[" + index + "]" + a);
			index++;
		}
		System.out.println("==proc work=============");

		if (args.length == 0) {
			new WSP().openCtlModel();
		} else if (args.length == 1 && args[0].equals("-help")) {
			System.out.println("WSP 是WsProcessor的前端工具");
			System.out.println("WSP命令格式：  WSP [operation] [src_path]");
			System.out.println("OPERATION LIST:");
			System.out.println("无参数：终端模式，进入程序后一条条命令键入执行；");
			System.out.println("-s：外源文件模式，传入参数，程序静默执行，抛出错误；");
			System.out.println("-w：图形界面模式：打开图形界面编辑文件；");

		} else if (args.length > 1) {
			if (args[0].equals("-s")) {
				new WSP().openSourceModel();
				System.out.println("......静默处理结束");
			} else if (args[0].equals("-w")) {
				new WSP().openGraphicsModel();
			}
		} else {
			System.out.println("参数错误");
		}

	}

}
