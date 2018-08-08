package cn.ipfile.main;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.util.Scanner;
import java.util.UUID;

public class Client {
	public static void main(String[] args) {
		try {
			final Scanner scanner=new Scanner(System.in);
			System.out.println("请输入上传文件:");
			String filename="/media/ww/新加卷/car/mydfs-3.wmv";
			final File file=new File(filename);
			final long filesize=file.length();
			int block=1024*1024*10;
			int times=(int)Math.ceil(filesize/(float)block);
			int size=(int) (filesize-((times-1)*block));
			final String uuid=UUID.randomUUID().toString();
			for (int i = 0; i < times; i++) {
				try {
					Socket socket = new Socket("192.168.1.5",9997);
					OutputStream out = socket.getOutputStream();
					InputStream in = socket.getInputStream();
					DataInputStream dataIn=new DataInputStream(in);
					DataOutputStream dataout = new DataOutputStream(out);
					String fix = file.getName().substring(file.getName().lastIndexOf("."));
					dataout.writeUTF(uuid+"-"+i);
					
					dataout.writeUTF(fix);
					if (i==(times-1)) {
						dataout.writeInt(size);
					}else {
						dataout.writeInt(block);
					}
					RandomAccessFile ra = new RandomAccessFile(file, "r");
					ra.seek(i*block);
					byte[] buf=new byte[block];
					int len=ra.read(buf);
					dataout.write(buf, 0, len);
					dataout.flush();
					scanner.close();
					dataIn.readUTF();
					dataout.close();
					socket.close();
					ra.close();
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("在你眼中我是谁");
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
