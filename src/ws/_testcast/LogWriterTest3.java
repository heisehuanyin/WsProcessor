package ws._testcast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import ws.editor.plugin.logwriter.LogWriter;

public class LogWriterTest3 {	public void test_getDefaultInstance(LogWriter obj) {
	LogWriter alog = (LogWriter) obj.getDefaultInstance(null);
	alog.writeLog(alog, "test_DefaultInstance");
	alog.saveOperation();
	File other = new File(alog.getCompid());
	assert(other.exists());
	String str = null;
	try {
		str = new BufferedReader(new FileReader(alog.getCompid())).readLine();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	str = str.replace("\n", "");
	assert(this.getPureMsg(str).equals("test_DefaultInstance"));
}

public void test_getInstance(LogWriter obj) {
	LogWriter alog = obj.getInstance("./testdata/log.gz");
	alog.writeLog(alog, "test_DefaultInstance");
	alog.saveOperation();
	File other = new File(alog.getCompid());
	assert(other.exists());
	String str = null;
	try {
		str = new BufferedReader(new FileReader(alog.getCompid())).readLine();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	str = str.replace("\n", "");
	assert(this.getPureMsg(str).equals("test_DefaultInstance"));
}

public String getPureMsg(String msg) {
	return msg.split(":")[1];
}

public static void main(String[] args) {
	LogWriter one = new LogWriter().getInstance("./testdata/log.tz");
	LogWriter two = (LogWriter) one.getDefaultInstance(null);
	one.writeLog(one, "shishi1");
	two.writeLog(two, "shishi2");
	one.saveOperation();
	two.saveOperation();
	new LogWriterTest3().test_getDefaultInstance(one);
	new LogWriterTest3().test_getInstance(one);
	}
}
