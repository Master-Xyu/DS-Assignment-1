/***
 * @author Xianchen Huang
 * @email xianchenh@student.unimelb.edu.au
 * @username xianchenh
 * @tutor Xunyun Liu
 * @studentID 858176
 */
package DictionaryClient;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JSplitPane;
import javax.swing.JScrollPane;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ClientWindow {

	private JFrame frmDictionaryClient;
	private QueryDialog q;
	private InsertDialog i;
	private DeleteDialog d;
	private Client client;
	/**
	 * Launch the application.
	 */
	//public static void main(String[] args) {

	//}

	/**
	 * Create the application.
	 */
	public ClientWindow(Client c) {
		initialize();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				client = c;
				try {
					frmDictionaryClient.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		try {
			c.connect();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Connection refused!", "",  JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		frmDictionaryClient.addWindowListener(new WindowAdapter() {
		      public void windowClosing(WindowEvent e) {
		        try {
					client.disconnect();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
		      }
		    });
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmDictionaryClient = new JFrame();
		frmDictionaryClient.setTitle("Dictionary Client");
		frmDictionaryClient.setBounds(100, 100, 450, 300);
		frmDictionaryClient.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmDictionaryClient.getContentPane().setLayout(null);
		
		JButton btnNewButton = new JButton("Query");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				q = new QueryDialog(client);
				q.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				q.setVisible(true);
			}
		});
		btnNewButton.setBounds(152, 47, 107, 44);
		frmDictionaryClient.getContentPane().add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Insert");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				i = new InsertDialog(client);
				i.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				i.setVisible(true);
			}
		});
		btnNewButton_1.setBounds(152, 102, 107, 44);
		frmDictionaryClient.getContentPane().add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("Delete");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				d = new DeleteDialog(client);
				d.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				d.setVisible(true);
			}
		});
		btnNewButton_2.setBounds(152, 157, 107, 44);
		frmDictionaryClient.getContentPane().add(btnNewButton_2);
	}
}
