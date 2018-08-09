package cn.box.main;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class Log {
	private static FileWriter logout; 
	private  static final String path=ResourceBundle.getBundle("project").getString("basepath");
	private static final String name=ResourceBundle.getBundle("project").getString("logname");
	private static String time=new SimpleDateFormat("yyyy-MM-dd_hh_mm_ss").format(new Date());
	public static final String log=path+"/"+time+"."+name;
	private Log(){
		
	}
	static{
		try {
			logout=new FileWriter(log);
			File file=new File(log);
			file.createNewFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void error(String msg) {
		try {
			String time=new SimpleDateFormat("yyyy-MM-dd_hh_mm_ss").format(new Date());
			logout.write("error:"+time+":"+msg);
			logout.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void fatal(String msg) {
		try {
			String time=new SimpleDateFormat("yyyy-MM-dd_hh_mm_ss").format(new Date());
			logout.write("fatal:"+time+":"+msg);
			logout.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void debug(String msg) {
		try {
			String time=new SimpleDateFormat("yyyy-MM-dd_hh_mm_ss").format(new Date());
			logout.write("debug:"+time+":"+msg);
			logout.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void info(String msg) {
		try {
			String time=new SimpleDateFormat("yyyy-MM-dd_hh_mm_ss").format(new Date());
			logout.write("info:"+time+":"+msg);
			logout.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void warn(String msg) {
		try {
			String time=new SimpleDateFormat("yyyy-MM-dd_hh_mm_ss").format(new Date());
			logout.write("warn:"+time+":"+msg);
			logout.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
	