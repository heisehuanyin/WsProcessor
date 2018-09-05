package ws._testcast;

import java.util.Scanner;

import ws.editor.WsProcessor;

public class WSP {
	private WsProcessor wsp = null;

	private void openCtlModel() {
		wsp = new WsProcessor();
		wsp.control_OpenSilentModel();

		Scanner s = new Scanner(System.in);
		for (;;) {
			String cmd = s.nextLine();
			if (cmd.equals("exit();"))
				return;
			if (cmd.equals("pluginList();"))
				this.wsp.service_GetPluginManager().service_printPluginList();
			if (cmd.equals("pluginListG();")) {
				this.wsp.control_OpenGraphicMode();
				this.wsp.service_GetPluginManager().service_printPluginList();
			}
		}
	}

	private void openGraphicsModel() {
		wsp = new WsProcessor();
		wsp.control_OpenGraphicMode();
	}

	private void openSilentModel() {
		wsp = new WsProcessor();
		wsp.control_OpenSilentModel();
	}

	public static void main(String[] args) {
		System.out.println("==print args============");
		System.out.println("长度：" + args.length);
		int index = 0;
		for (String a : args) {
			System.out.println("[" + index + "]" + a);
			index++;
		}
		System.out.println("==proc work=============");

		if (args.length == 0) {
			new WSP().openCtlModel();
		}

		if (args.length == 1 && args[0].equals("-help")) {
			System.out.println("WSP 是WsProcessor的前端工具");
			System.out.println("WSP命令格式：  WSP [operation] [src_path]");
			System.out.println("OPERATION LIST:");
			System.out.println("无参数：终端模式，进入程序后一条条命令键入执行；");
			System.out.println("-s：外源文件模式，传入参数，打开文件");
			System.out.println("-w：图形界面模式：打开图形界面编辑文件；");
			System.out.println("-r: 立即执行模式，将传入的文件作为脚本执行");
		}

		if (args.length < 1 || (args[0].indexOf('-') == -1))
			return;

		WSP c = new WSP();

		if (args.length >= 2) {
			if (args[0].indexOf('s') != -1) {
				c.openFile(args[1]);
			}

			if (args[0].indexOf('r') != -1) {
				c.runMacroSource(args[1]);
			}
		}
		
		if (args[0].indexOf('w') != -1) {
			c.openGraphicsModel();
		} else {
			c.openSilentModel();
		}

	}

	private void runMacroSource(String string) {
		// TODO Auto-generated method stub

	}

	private void openFile(String string) {
		this.openSilentModel();
		this.wsp.service_OpenFile(string, null);

	}

}
