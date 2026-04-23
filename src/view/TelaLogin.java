package view;

import javax.swing.JPanel;
import javax.swing.JRadioButton;

import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.Color;

public class TelaLogin extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTextField tFNome;
	private JTextField tFCpf;
	private JButton btnEntrar;
	private JButton btnCadastrar;

	public TelaLogin() {
		setLayout(new MigLayout("", "[grow 20][grow 0][grow 9][grow 15][grow 11][grow 20]", "[grow 9][grow 5][grow 1][grow 3][grow 1][grow 3][grow 1][][][grow 1][grow 15]"));
		
		JLabel lblNewLabel = new JLabel("Faça seu Login");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
		add(lblNewLabel, "cell 1 0 4 1,alignx center,aligny center");
		
		JRadioButton rdbtnAdministrador = new JRadioButton("Administrador");
		rdbtnAdministrador.setFont(new Font("Tahoma", Font.PLAIN, 15));
		add(rdbtnAdministrador, "cell 1 1 4 1,alignx center,aligny center");
		
		JLabel lblNewLabel_1 = new JLabel("Nome:");
		add(lblNewLabel_1, "cell 1 2,alignx trailing");
		
		tFNome = new JTextField();
		add(tFNome, "cell 2 2 3 1,grow");
		tFNome.setColumns(10);
		
		
		JLabel lblNewLabel_2 = new JLabel("CPF:");
		add(lblNewLabel_2, "cell 1 4,alignx trailing");
		
		tFCpf = new JTextField();
		add(tFCpf, "cell 2 4 3 1,grow");
		tFCpf.setColumns(10);
		
		
		JButton btnLogin = new JButton("Entrar");
		btnLogin.setForeground(new Color(255, 255, 255));
		btnLogin.setBackground(new Color(0, 0, 0));
		add(btnLogin, "cell 3 6,grow");
		
		JLabel lblNewLabel_3 = new JLabel("Não Possui Conta?");
		lblNewLabel_3.setFont(new Font("Tahoma", Font.PLAIN, 13));
		add(lblNewLabel_3, "cell 3 7,alignx center,aligny center");
		
		btnCadastrar = new JButton("Cadastrar");
		add(btnCadastrar, "cell 3 8,alignx center,aligny top");
	}
	
	public JTextField gettFNome() {
		return tFNome;
	}

	public JTextField gettFCpf() {
		return tFCpf;
	}

	public void acaoEntrar(ActionListener acao) {
		this.btnEntrar.addActionListener(acao);
	}
	
	public void acaoCadastrar(ActionListener acao) {
		this.btnCadastrar.addActionListener(acao);
	}

	public void limpaCampos() {
		tFNome.setText("");
	    tFCpf.setText("");
	}
}