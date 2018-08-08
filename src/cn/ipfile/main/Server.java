package cn.ipfile.main;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
	public static void main(String[] args) {
		try {
			final int readsize=1024*1024;
			ServerSocket server=new ServerSocket(9997);
			ExecutorService pool = Executors.newFixedThreadPool(3);
			ResourceBundle bundle = ResourceBundle.getBundle("project");
			final String basepath = bundle.getString("basepath");
			new File(basepath).mkdir();
			while (true) {
				final Socket socket=server.accept();
				InputStream in = socket.getInputStream();
				OutputStream out = socket.getOutputStream();
				final DataOutputStream dataOut=new DataOutputStream(out);
				final DataInputStream datain=new DataInputStream(in);
				pool.execute(new Runnable() {
					
					@Override
					public void run() {
						try {
							String Id=datain.readUTF();
							String fix=datain.readUTF();
							int block=datain.readInt();
							String name=basepath+Id+"."+fix;
							FileOutputStream out = new FileOutputStream(name);
							byte[] buf=new byte[readsize];
							int boxsize=0;
							do {
								if (boxsize==block)break; 
								int len=datain.read(buf);
								out.write(buf, 0, len);
								out.flush();
								boxsize+=len;
								
							} while (true);
							out.close();
							dataOut.writeUTF("over");
							datain.close();
							socket.close();
							System.out.println(name);
							
						} catch (Exception e) {
							e.printStackTrace();						}
					}
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
