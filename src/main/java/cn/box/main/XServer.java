package cn.box.main;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
public class XServer {
	public static void main(String[] args) {
		try {
			// 127.0.0.1:9999/192.168.3.20:9999
			final int readsize=1024*1024;
			ServerSocket serverSocket = new ServerSocket(9898);
			ExecutorService pool = Executors.newFixedThreadPool(5);
			ResourceBundle bundle = ResourceBundle.getBundle("project");
			final String basepath = bundle.getString("name");
			new File(basepath).mkdirs();
			while (true) {
				final Socket socket = serverSocket.accept();// 
				InputStream in = socket.getInputStream();
				OutputStream out = socket.getOutputStream();
				final DataOutputStream dataout=new DataOutputStream(out);
				final DataInputStream datain = new DataInputStream(in);
				pool.execute(new Runnable() {

					@Override
					public void run() {
						try {
							// 0.读到文件名
							String fileId = datain.readUTF();
							// 1.读到文件后缀
							String stuffix = datain.readUTF();
							// 2.读到每次大小
							int block = datain.readInt();
							// 拼接文件名字
							String fileName = basepath + fileId + "." + stuffix;
							FileOutputStream out = new FileOutputStream(fileName);
							byte[] buf = new byte[readsize];
//							int times=(int)Math.ceil(block/(float)(1024*1024));
							int boxsize=0;
							do {
								if(boxsize==block)break;
								int len = datain.read(buf);
								out.write(buf, 0, len);
								out.flush();
								boxsize+=len;
							} while (true);
							out.close();
							dataout.writeUTF("over");
							datain.close();
							socket.close();
							Log.debug("file save path:" + fileName);

						} catch (Exception e) {
							try {
								e.printStackTrace();
								e.printStackTrace(new PrintStream(Log.log));
							} catch (FileNotFoundException e1) {
								e1.printStackTrace();
							}Log.error(e.getMessage());
						}
					}
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				e.printStackTrace(new PrintStream(Log.log));
				Log.error(e.getMessage());
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
		}
	}
}