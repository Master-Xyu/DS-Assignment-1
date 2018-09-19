/***
 * @author Xianchen Huang
 * @email xianchenh@student.unimelb.edu.au
 * @username xianchenh
 * @tutor Xunyun Liu
 * @studentID 858176
 */
package DictionaryClient;

import java.net.*;
import java.util.*;

import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.*;

public class Client {
	private static Scanner sc = new Scanner(System.in);
	private DataInputStream in;
	private DataOutputStream out;
	private String ip;
	private int port;
	public static void main(String[] args) {
		if((args.length == 0)|(args.length>2)) {
			JOptionPane.showMessageDialog(null, "Invalid amount of parameters inputed!", "",  JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		String ip = args[0];
		int port = -9999;
		try {
			port = Integer.parseInt(args[1]);
		}catch(Exception e) {
			JOptionPane.showMessageDialog(null, "Invalid port format!", "",  JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		if((port>65535) | (port<0)) {
			JOptionPane.showMessageDialog(null, "Invalid port!", "",  JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		Client client = new Client(ip,port);
		ClientWindow c = new ClientWindow(client);
	}
	public Client(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}
	public boolean connect() throws Exception{
		Socket client = new Socket(ip, port);
		in = new DataInputStream(client.getInputStream());
		out = new DataOutputStream(client.getOutputStream());
		return true;
	}
	public String[] query(String word) throws Exception {
		String[] output = new String[3];
		output[0] = "query";
		output[1] = word;
		output[2] = "";
		outputXML(output);
		return inputXML();
	}
	public String[] insert(String word, String exp) throws Exception {
		String[] output = new String[3];
		output[0] = "insert";
		output[1] = word;
		output[2] = exp;
		outputXML(output);
		return inputXML();
	}
	public String[] delete(String word) throws Exception {
		String[] output = new String[3];
		output[0] = "delete";
		output[1] = word;
		output[2] = "";
		outputXML(output);
		return inputXML();
	}
	public void disconnect() throws Exception {
		String[] output = new String[3];
		output[0] = "exit";
		output[1] = "";
		output[2] = "";
		outputXML(output);
	}
	private String[] inputXML() throws Exception {
		String XML = "";
		try{
			XML = in.readUTF();
		}catch(Exception e) {
			JOptionPane.showMessageDialog(null, "Connection has been lost!", "",  JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		DocumentBuilderFactory df = DocumentBuilderFactory.newInstance();
		df.setNamespaceAware(true);
		DocumentBuilder db = df.newDocumentBuilder();
		Document message = db.parse(new ByteArrayInputStream(XML.getBytes()));
		
		NodeList type = message.getElementsByTagName("Type");
		String[] input = new String[2];
		input[0] = type.item(0).getAttributes().item(0).getNodeValue();
		input[1] = type.item(0).getFirstChild().getTextContent();
		return input;
	}
	private void outputXML(String[] output) throws Exception {
		Document message = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		message.setXmlStandalone(true);
		Element transport = message.createElement("Transport");
		Element action = message.createElement("Action");
		action.setAttribute("Action", output[0]);
		Element word = message.createElement("Word");
		word.setTextContent(output[1]);
		Element exp = message.createElement("Explaination");
		exp.setTextContent(output[2]);
		action.appendChild(word);
		action.appendChild(exp);
		transport.appendChild(action);
		message.appendChild(transport);
		
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		StringWriter writer = new StringWriter();
		transformer.transform(new DOMSource(message), new StreamResult(writer));
		String outputMessage = writer.getBuffer().toString();
		
		try{
			out.writeUTF(outputMessage);
		}catch(Exception e) {
			JOptionPane.showMessageDialog(null, "Connection has been lost!", "",  JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		out.flush();
	}
}
