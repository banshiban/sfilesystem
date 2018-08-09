package cn.box.main;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.util.Scanner;
import java.util.UUID;

public class XClient {
	public static void main(String[] args) {
		try {
			final Scanner scanner = new Scanner(System.in);
			System.out.print("请输入上传文件目录:");
			String filename = "/media/ww/新加卷/car/mydfs-3.wmv";
			final File file = new File(filename);
			final long filesize = file.length();
			int block = 1024 * 1024 * 10;
			int times = (int)Math.ceil(filesize / (float) block);
			//计算出最后一块的大小
			int endsize=(int)(filesize-((times-1)*block));
			
			final String fileId = UUID.randomUUID().toString(); 
			// 100M 10M
			for (int i = 0; i < times; i++) {
				try {
					Socket socket = new Socket("192.168.1.5", 9998);
					OutputStream o = socket.getOutputStream();
					InputStream in = socket.getInputStream();
					DataInputStream datain = new DataInputStream(in);
					// 加密流
					DataOutputStream dataout = new DataOutputStream(o);
					// 截取文件后缀
					String stuffix = file.getName().substring(file.getName().lastIndexOf(".") + 1);// /
					// 把文件名传输过去
					dataout.writeUTF(fileId + "-" + i);
					// 1.把文件后缀放到服务器
					dataout.writeUTF(stuffix);
					// 2.把每次读到大小写到服务端,最后一节文件大小<=block
					if(i==(times-1)){
						dataout.writeInt(endsize);
					}else {
						dataout.writeInt(block);
					}
					RandomAccessFile raFile = new RandomAccessFile(file, "r");
					raFile.seek(i * block);
					byte[] buf = new byte[block];
					int len = raFile.read(buf);
					// 我们每次发送到服务器的数据是block大小
					// 问题:这边没有传输完成!也就是说因为网络的问题
					//该数据不是一次性传输过去的,或者传输的时候是不均段传输的
					dataout.write(buf, 0, len);
					dataout.flush();
					scanner.close();
					//必须等到服务器接收完毕,所有的流才关闭
					datain.readUTF();
					dataout.close();
					socket.close();
					raFile.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
