/***
 * @author Xianchen Huang
 * @email xianchenh@student.unimelb.edu.au
 * @username xianchenh
 * @tutor Xunyun Liu
 * @studentID 858176
 */
package DictionaryServer;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.SpringLayout;
import javax.swing.JLabel;
import javax.swing.JScrollBar;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ServerWindow {

	private JFrame frmDictionaryserver;
	private JTextField textField;
	private JTextPane textPane;
	private JScrollPane scrollPane;
	/**
	 * Create the application.
	 */
	public ServerWindow() {
		initialize();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frmDictionaryserver.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmDictionaryserver = new JFrame();
		frmDictionaryserver.setTitle("DictionaryServer");
		frmDictionaryserver.setBounds(100, 100, 450, 300);
		frmDictionaryserver.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmDictionaryserver.getContentPane().setLayout(null);
		
		JLabel lblPort = new JLabel("Port:");
		lblPort.setBounds(106, 27, 46, 14);
		frmDictionaryserver.getContentPane().add(lblPort);
		
		textField = new JTextField();
		textField.setEditable(false);
		textField.setBounds(162, 24, 86, 20);
		frmDictionaryserver.getContentPane().add(textField);
		textField.setColumns(10);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(51, 52, 337, 180);
		frmDictionaryserver.getContentPane().add(scrollPane);
		
		textPane = new JTextPane();
		scrollPane.setViewportView(textPane);
		textPane.setEditable(false);
		
		JButton btnNewButton = new JButton("Stop server");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		btnNewButton.setBounds(275, 23, 113, 23);
		frmDictionaryserver.getContentPane().add(btnNewButton);
	}
	public void setPort(int port) {
		textField.setText(Integer.toString(port));
	}
	public void appendMessage(String m) {
		textPane.setText(textPane.getText() + m);
		JScrollBar sBar = scrollPane.getVerticalScrollBar();
		sBar.setValue(sBar.getMaximum());
	}
}
