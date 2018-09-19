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
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;

public class InsertDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	private Client client;
	private String exp = "";
	/**
	 * Create the dialog.
	 */
	public InsertDialog(Client client) {
		setTitle("Insert");
		this.client = client;
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		textField = new JTextField();
		textField.setBounds(149, 24, 147, 20);
		contentPanel.add(textField);
		textField.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Word:");
		lblNewLabel.setBounds(81, 27, 46, 14);
		contentPanel.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Explaination:");
		lblNewLabel_1.setBounds(52, 60, 75, 14);
		contentPanel.add(lblNewLabel_1);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(149, 60, 211, 124);
		contentPanel.add(scrollPane);
		
		JTextArea textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		{
			JPanel buttonPane = new JPanel();
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(textArea.getText().equals("")|textField.getText().equals("")) {
							JOptionPane.showMessageDialog(null, "Vacant input!", "",  JOptionPane.ERROR_MESSAGE);
							return;
						}
						try {
							if(exp.equals(""))
								exp = exp + textArea.getText();
							else
								exp = exp + "\n\n" + textArea.getText();
							insert(textField.getText(), exp);
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				});
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
			
			JButton btnAddAnotherExplanation = new JButton("Add another explanation");
			btnAddAnotherExplanation.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(textArea.getText().equals("")|textField.getText().equals("")) {
						JOptionPane.showMessageDialog(null, "Vacant input!", "",  JOptionPane.ERROR_MESSAGE);
						return;
					}
					if(exp.equals(""))
						exp = textArea.getText();
					else
						exp = exp + "\n\n" + textArea.getText();
					textArea.setText("");
					textField.setEditable(false);
				}
			});
			buttonPane.add(btnAddAnotherExplanation);
		}
	}
	private void insert(String word, String exp) throws Exception {
		String message[] = client.insert(word, exp);
		switch(message[0]) {
		case "alert":
			JOptionPane.showMessageDialog(null, message[1], "",  JOptionPane.ERROR_MESSAGE);
			return;
		default:
			JOptionPane.showMessageDialog(null, message[1], "",  JOptionPane.INFORMATION_MESSAGE);
			setVisible(false);
			return;
		}
	}
}
