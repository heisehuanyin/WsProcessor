package ws.editor;

import java.util.Scanner;
/**
 * 参数处理模块，处理各种模式与系统的衔接，决定软件的运行模式：静默、交互、图形<br>
 * 通过本模块是在Terminal中运行的实体，通过本模块调用核心模块实现各种功能<br>
 * 外面还可以在包含一个启动器，处理操作系统提供的各种参数，配置固定参数，用于不同默认模式下的启动*/
public class WSP {
	private WsProcessor wsp = null;

	private void openCtlModel() {
		this.wsp = (this.wsp==null)?new WsProcessor():this.wsp;
		wsp.control_OpenSilentModel();

		Scanner s = new Scanner(System.in);
		for (;;) {
			String cmd = s.nextLine();
			if (cmd.equals("exit();"))
				return;
			if (cmd.equals("pluginList();"))
				this.wsp.service_GetPluginManager().service_printPluginList();
			if (cmd.equals("pluginListG();")) {
				this.wsp.control_OpenGraphicMode("MainWindow");
				this.wsp.service_GetPluginManager().service_printPluginList();
			}
		}
	}

	/**
	 * 打开图形模式*/
	private void openGraphicsModel() {
		this.wsp = (this.wsp==null)?new WsProcessor():this.wsp;
		wsp.control_OpenGraphicMode("MainWindow");
	}

	/**
	 * 打开静默模式*/
	private void openSilentModel() {
		this.wsp = (this.wsp==null)?new WsProcessor():this.wsp;
		wsp.control_OpenSilentModel();
	}
	
	private void runMacroSource(String string) {

	}

	/**
	 * 打开文件*/
	private void openFile(String args1, String args0) {
		this.wsp.service_OpenFile(args1, args0.indexOf('w')!=-1?
				this.wsp.service_GetPluginManager()
				.instance_GetNewDefaultWindow("MainWindow"):null);

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

		if (args[0].indexOf('w') != -1) {
			c.openGraphicsModel();
		} else {
			c.openSilentModel();
		}

		if (args.length >= 2) {
			if (args[0].indexOf('s') != -1) {
				c.openFile(args[1],args[0]);
			}

			if (args[0].indexOf('r') != -1) {
				c.runMacroSource(args[1]);
			}
		}
		
	}

}