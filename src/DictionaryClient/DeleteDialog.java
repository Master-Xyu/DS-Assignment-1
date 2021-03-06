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

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class DeleteDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	private Client client;

	/**
	 * Create the dialog.
	 */
	public DeleteDialog(Client client) {
		setTitle("Delete");
		this.client = client;
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			textField = new JTextField();
			textField.setBounds(213, 83, 134, 20);
			contentPanel.add(textField);
			textField.setColumns(10);
		}
		{
			JLabel lblNewLabel = new JLabel("Word:");
			lblNewLabel.setBounds(129, 86, 46, 14);
			contentPanel.add(lblNewLabel);
		}
		{
			JPanel buttonPane = new JPanel();
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(textField.getText().equals("")) {
							JOptionPane.showMessageDialog(null, "Vacant input!", "",  JOptionPane.ERROR_MESSAGE);
							return;
						}
						try {
							delete(textField.getText());
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
		}
	}
	private void delete(String word) throws Exception {
		String message[] = client.delete(word);
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
