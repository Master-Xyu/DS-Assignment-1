/***
 * @author Xianchen Huang
 * @email xianchenh@student.unimelb.edu.au
 * @username xianchenh
 * @tutor Xunyun Liu
 * @studentID 858176
 */
package DictionaryClient;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

public class QueryDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	private Client client;
	/**
	 * Create the dialog.
	 */
	public QueryDialog(Client client) {
		setTitle("Query");
		this.client = client;
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		textField = new JTextField();
		textField.setBounds(149, 24, 147, 20);
		contentPanel.add(textField);
		textField.setColumns(10);
		
		JLabel lblWord = new JLabel("word:");
		lblWord.setBounds(81, 27, 46, 14);
		contentPanel.add(lblWord);
		
		JLabel lblExplaination = new JLabel("Explaination:");
		lblExplaination.setBounds(52, 60, 75, 14);
		contentPanel.add(lblExplaination);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(149, 60, 211, 124);
		contentPanel.add(scrollPane);
		
		JTextPane textPane = new JTextPane();
		textPane.setBounds(149, 60, 211, 124);
		scrollPane.setViewportView(textPane);
		textPane.setEditable(false);
		{
			JPanel buttonPane = new JPanel();
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("Query");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(textField.getText().equals("")) {
							JOptionPane.showMessageDialog(null, "Vacant input!", "",  JOptionPane.ERROR_MESSAGE);
							return;
						}
						String exp = "";
						try {
							exp = query(textField.getText());
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						textPane.setText(exp);
					}
				});
				buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						setVisible(false);
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
	private String query(String word) throws Exception {
		String[] exp = client.query(word);
		switch(exp[0]) {
		case "alert":
			JOptionPane.showMessageDialog(null, exp[1], "",  JOptionPane.ERROR_MESSAGE);
			return "";
		default:
			return exp[1];
		}
	}
}
