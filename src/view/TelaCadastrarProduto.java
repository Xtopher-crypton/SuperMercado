package view;

import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import java.awt.Font;
import java.security.Principal;

import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.Color;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;

public class TelaCadastrarProduto extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTextField tfId;
	private JTextField tfNome;
	private JTextField tfPreco;
	private JTextField tfQuantidade;
	private JTable table;
	private Principal frame;

	public TelaCadastrarProduto(Principal frame) {
		this.frame = frame;
		setLayout(new MigLayout("", "[grow 20][grow][grow][grow][grow 20]", "[grow 10][grow][grow][grow][grow][grow][grow][grow][grow][grow][grow][grow 50]"));

		JLabel lblTitulo = new JLabel("Cadastro de Produtos");
		lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 24));
		add(lblTitulo, "cell 1 0 3 1,alignx center");

		add(new JLabel("ID:"), "cell 1 2,alignx trailing");
		tfId = new JTextField();
		tfId.setEditable(false);
		add(tfId, "cell 2 2 2 1,grow");

		add(new JLabel("Nome:"), "cell 1 4,alignx trailing");
		tfNome = new JTextField();
		add(tfNome, "cell 2 4 2 1,grow");

		add(new JLabel("Preço:"), "cell 1 6,alignx trailing");
		tfPreco = new JTextField();
		add(tfPreco, "cell 2 6 2 1,grow");

		add(new JLabel("Quantidade:"), "cell 1 8,alignx trailing");
		tfQuantidade = new JTextField();
		add(tfQuantidade, "cell 2 8 2 1,grow");

		JButton btnAdicionar = new JButton("Adicionar");
		btnAdicionar.setBackground(Color.BLACK);
		btnAdicionar.setForeground(Color.WHITE);
		add(btnAdicionar, "cell 1 10,grow");

		JButton btnAtualizar = new JButton("Atualizar");
		add(btnAtualizar, "cell 2 10,grow");

		JButton btnExcluir = new JButton("Excluir");
		add(btnExcluir, "cell 3 10,grow");

		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, "cell 0 11 5 1,grow");

		table = new JTable();
		table.setModel(new DefaultTableModel(
			new Object[][] {},
			new String[] {"ID", "Nome", "Preço", "Quantidade"}
		));
		scrollPane.setViewportView(table);
	}
}