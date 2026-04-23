package view;

import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JButton;


import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class TelaCadastro extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTextField textField;
	private JTextField textField_1;

	/**
	 * Create the panel.
	 */
	public TelaCadastro () {
		setLayout(new MigLayout("", "[grow 15][grow 0][grow 7][grow 8][grow 9][grow 15]", "[grow 25][grow 20][grow 4][grow 10][grow 4][grow 16][grow 3][grow 50]"));
		
		JLabel lblNewLabel = new JLabel("Cadastrar-se");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 36));
		add(lblNewLabel, "cell 1 0 4 1,alignx center,aligny center");
		
		JRadioButton rdbtnAdministrador = new JRadioButton("Administrador");
		rdbtnAdministrador.setFont(new Font("Tahoma", Font.PLAIN, 15));
		add(rdbtnAdministrador, "cell 1 1 4 1,alignx center,aligny center");
		
		JLabel lblNewLabel_1 = new JLabel("Nome:");
		add(lblNewLabel_1, "cell 1 2,alignx trailing");
		
		textField = new JTextField();
		textField.setColumns(10);
		add(textField, "cell 2 2 3 1,grow");
		
		JLabel lblNewLabel_2 = new JLabel("CPF:");
		add(lblNewLabel_2, "cell 1 4,alignx trailing");
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		add(textField_1, "cell 2 4 3 1,grow");
		
		JButton btnCadastrar = new JButton("Cadastrar-se");
		btnCadastrar.setForeground(new Color(255, 255, 255));
		btnCadastrar.setBackground(new Color(0, 0, 0));
		add(btnCadastrar, "cell 3 6,grow");

	}

}
