/***
 * @author Xianchen Huang
 * @email xianchenh@student.unimelb.edu.au
 * @username xianchenh
 * @tutor Xunyun Liu
 * @studentID 858176
 */
package DictionaryServer;
import java.net.*;

import javax.swing.JOptionPane;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.w3c.dom.*;

import java.io.*;

public class Server {
	private static int port;
	public static void main(String[] args){
		if((args.length>2)|args.length == 0) {
			System.out.println("Invalid amount of parameter inputed!");
			System.exit(0);
		}
		try{
			port = Integer.parseInt(args[0]);
		}catch(Exception e) {
			System.out.println("Invalid port format!");
			System.exit(0);
		}
		if((port>65535) | (port<0)) {
			System.out.println("Invalid port!");
			System.exit(0);
		}
		String dicPath = args[1];
		int clientNum = 0;
		ServerWindow window = new ServerWindow();
		window.setPort(port);
		try{
			ServerSocket server = new ServerSocket(port); 
			window.appendMessage("Server is online.\r");
			while(true) {
				Socket client = server.accept();
				clientNum++;
				DicThread t = new DicThread(client, dicPath, clientNum, window);
				t.start();
			}
		}catch(Exception e) {
			JOptionPane.showMessageDialog(null, "Address already in use!", "",  JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
	}

}
