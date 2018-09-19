/***
 * @author Xianchen Huang
 * @email xianchenh@student.unimelb.edu.au
 * @username xianchenh
 * @tutor Xunyun Liu
 * @studentID 858176
 */
package DictionaryServer;
import java.io.*;
import java.net.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.*;

import java.util.regex.*;

public class DicThread extends Thread{
	private Socket c;
	private static Document d;
	private static NodeList list = null;
	DataInputStream in;
	DataOutputStream out;
	static String dicPath;
	int id;
	ServerWindow s;
	static String wordReg = "^[a-zA-Z\\s]*[a-zA-Z]+[a-zA-Z\\s]*$";
	
	DicThread(Socket client, String p, int id, ServerWindow s){
		c = client;
		dicPath = p;
		this.id = id;
		this.s = s;
	}
	static boolean syn = false;
	public void run() {
		String[] message = new String[3];
		message[0] = "";
		try {
			in = new DataInputStream(c.getInputStream());
			out = new DataOutputStream(c.getOutputStream());
			while(!message[0].equals("exit")) {
				message = inPutXML();
				while(syn == true) {}
				synControl(in, out, this, message);	
				s.appendMessage("Client " + id + ": " + message[0] +"\r");
				syn = false;
				sleep(5);
			}
			s.appendMessage("Client " + id + " offline" + "\r");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static synchronized String[] synControl(DataInputStream in, DataOutputStream out, DicThread dic, String[] input) throws IOException {
		syn = true;
		d = loadDic();
		list = d.getElementsByTagName("Word");
		String[] output = new String[2];
		switch(input[0]) {
		case "query":
			output = dic.query(input[1]);
			break;
		case "insert":
			output[0] = "alert";
			output[1] = dic.insert(input[1], input[2]);
			if(output[1].equals("Successful inserted!"))
				output[0] = "information";
			break;
		case "delete":
			output[0] = "alert";
			output[1] = dic.delete(input[1]);
			if(output[1].equals("Successful deleted!"))
				output[0] = "information";
			break;
		case "exit":
			return input;
		}	
		try {
			dic.outPutXML(output[1], output[0]);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return input;
	}
	
	public String insert(String nWord, String nExp) throws IOException {
		Element dictionary = d.getDocumentElement();
		String output;
		String[] qResult = query(nWord);
		if(!qResult[0].equals("alert")) {
			output  = "The word exists!";
			return output;
		}
		if(!Pattern.matches(wordReg, nWord)) {
			output = "Please input available word!";
			return output;
		}
		Element word = d.createElement("Word");
		word.setAttribute("Num",Integer.toString(list.getLength()+1));
		Element spelling = d.createElement("Spelling");
		spelling.setTextContent(nWord);
		Element exp = d.createElement("Explaination");
		try{
			if(nExp.equals(""))
				throw new Exception();
		}catch(Exception e) {
			output = "Vacant input!";
			return output;
		}
		exp.setTextContent(nExp);
		word.appendChild(spelling);
		word.appendChild(exp);
		dictionary.appendChild(word);
		this.saveXML();
		output = "Successful inserted!";
		return output;
	}
	
	public String[] query(String qWord) throws IOException {
		String[] output = new String[2];
		NodeList word;
		try {
			if(qWord.equals(""))
				throw new Exception();
		}catch(Exception e) {
			output[0] = "alert";
			output[1] = "Vacant input!";
			return output;
		}
		if(!Pattern.matches(wordReg, qWord)) {
			output[0] = "alert";
			output[1] = "Please input available word!";
			return output;
		}
		for(int i = 0; i < list.getLength(); i++) {
			word = list.item(i).getChildNodes();
			if(qWord.equals(word.item(1).getFirstChild().getTextContent())) {
				output[0] = "message";
				output[1] = word.item(3).getFirstChild().getTextContent();
				return output;
			}
		}
		output[0] = "alert";
		output[1] = "No result!";
		return output;
	}
	
	public String delete(String dWord) throws IOException{
		Element root = (Element) d.getFirstChild();
		NodeList word;
		String output;
		if(!Pattern.matches(wordReg, dWord)) {
			output = "Please input available word!";
			return output;
		}
		for(int i=0;i<list.getLength();i++) {
			word = list.item(i).getChildNodes();
			if(dWord.equals(word.item(1).getFirstChild().getTextContent())) {
				root.removeChild(list.item(i));
				saveXML();
				output = "Successful deleted!";
				return output;
			}
		}
		output = "The word is not in dictionary!";
		return output;
	}
	
	private void saveXML() {
		try {
			TransformerFactory tff = TransformerFactory.newInstance();
            Transformer tf = tff.newTransformer();
            tf.setOutputProperty(OutputKeys.INDENT, "yes");
            tf.transform(new DOMSource(d), new StreamResult("dic.xml"));
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	private static Document loadDic() {
		DocumentBuilderFactory df = DocumentBuilderFactory.newInstance();
		File file = null;
		Document dic = null;
		DocumentBuilder db = null;
		file = new File(dicPath);	
		if(!file.exists()) {
			try{
				db = df.newDocumentBuilder();
				dic = db.newDocument();
				dic.setXmlStandalone(true);		
				Element dictionary = dic.createElement("Dictionary");
				dic.appendChild(dictionary);
				TransformerFactory tff = TransformerFactory.newInstance();
	            Transformer tf = tff.newTransformer();
	            tf.setOutputProperty(OutputKeys.INDENT, "yes");
	            tf.transform(new DOMSource(dic), new StreamResult("dic.xml"));
	        } catch (Exception e1) {
	            e1.printStackTrace();
	        }
	        return dic;
		}
		try{
			db = df.newDocumentBuilder();
			dic = db.parse(dicPath);
		}catch(Exception e2) {
			e2.printStackTrace();
		}
		return dic;
	}
	private void outPutXML(String messages, String type) throws Exception {
		Document message = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		message.setXmlStandalone(true);
		Element transport = message.createElement("Transport");
		message.appendChild(transport);
		Element typeNode = message.createElement("Type");
		Element mNode = message.createElement("Message");
		typeNode.appendChild(mNode);
		transport.appendChild(typeNode);
		switch(type) {
		case "information":
		case "alert":
			typeNode.setAttribute("Type", type);
			mNode.setTextContent(messages);		
			break;
		case "message":
			typeNode.setAttribute("Type", "explanation");
			mNode.setTextContent(messages);
			break;
		}
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		StringWriter writer = new StringWriter();
		transformer.transform(new DOMSource(message), new StreamResult(writer));
		String output = writer.getBuffer().toString();
		out.writeUTF(output);
		out.flush();
	}
	private String[] inPutXML() throws Exception {
		String XML = in.readUTF();
		DocumentBuilderFactory df = DocumentBuilderFactory.newInstance();
		df.setNamespaceAware(true);
		DocumentBuilder db = df.newDocumentBuilder();
		Document message = db.parse(new ByteArrayInputStream(XML.getBytes()));
		
		NodeList n = message.getElementsByTagName("Action");
		String[] input = new String[3];
		input[0] = n.item(0).getAttributes().item(0).getNodeValue();
		switch(input[0]) {
		case "insert":
			input[1] = n.item(0).getChildNodes().item(0).getTextContent().trim();
			input[2] = n.item(0).getChildNodes().item(1).getTextContent().trim();
			break;
		case "delete":
			input[1] = n.item(0).getChildNodes().item(0).getTextContent().trim();
			break;
		case "query":
			input[1] = n.item(0).getChildNodes().item(0).getTextContent().trim();
		}
		return input;
	}
}
