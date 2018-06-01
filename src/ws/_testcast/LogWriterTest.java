package ws._testcast;

import ws.editor.plugin.logwriter.*;

class LogWriterTest {
	public static void main(String[] args) {
		LogWriter one = new LogWriter().getInstance("./testdata/log.tz");
		LogWriter two = (LogWriter) one.getDefaultInstance();
		one.write(one, "shishi1");
		two.write(two, "shishi2");
		one.saveOperation();
		two.saveOperation();
	}
}
