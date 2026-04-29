package view;

import java.awt.Font;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import net.miginfocom.swing.MigLayout;

public class TelaCompra extends JPanel {

    private JTable tabelaProdutos;
    private JTable tabelaCarrinho;
    private JLabel lblTotal;
    private JButton btnAdicionar;
    private JButton btnRemover;
    private JButton btnFinalizar;
    private JButton btnNotaFiscal;
    private JButton btnVoltar;

    public TelaCompra() {
        setLayout(new MigLayout("fill", "[grow][grow]", "[][grow][]"));

        JLabel titulo = new JLabel("Compra de Produtos");
        titulo.setFont(new Font("Arial", Font.BOLD, 22));

        add(titulo, "cell 0 0 2 1,alignx center");

        String[] colunasProdutos = {"ID", "Produto", "Preço", "Estoque"};
        DefaultTableModel modeloProdutos = new DefaultTableModel(colunasProdutos, 0);

        tabelaProdutos = new JTable(modeloProdutos);
        JScrollPane scrollProdutos = new JScrollPane(tabelaProdutos);

        JPanel painelProdutos = new JPanel(new MigLayout("fill"));
        painelProdutos.setBorder(BorderFactory.createTitledBorder("Produtos"));
        painelProdutos.add(scrollProdutos, "grow");

        add(painelProdutos, "cell 0 1,grow");

        String[] colunasCarrinho = {"Produto", "Qtd", "Subtotal"};
        DefaultTableModel modeloCarrinho = new DefaultTableModel(colunasCarrinho, 0);

        tabelaCarrinho = new JTable(modeloCarrinho);
        JScrollPane scrollCarrinho = new JScrollPane(tabelaCarrinho);

        JPanel painelCarrinho = new JPanel(new MigLayout("fill"));
        painelCarrinho.setBorder(BorderFactory.createTitledBorder("Carrinho"));
        painelCarrinho.add(scrollCarrinho, "grow");

        add(painelCarrinho, "cell 1 1,grow");

        btnAdicionar = new JButton("Adicionar >>");
        btnRemover = new JButton("<< Remover");

        add(btnAdicionar, "cell 0 2,alignx center");
        add(btnRemover, "cell 0 2");

        lblTotal = new JLabel("Total: R$ 0,00");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 18));

        btnFinalizar = new JButton("Finalizar Compra");
        btnNotaFiscal = new JButton("Emitir Nota Fiscal");
        
        btnVoltar = new JButton("Voltar");
        add(btnVoltar, "cell 1 2,alignx right");

        add(lblTotal, "cell 0 3,alignx left");
        add(btnFinalizar, "cell 1 3,alignx right");
        add(btnNotaFiscal, "cell 1 3");
    }

    public JTable getTabelaProdutos() {
        return tabelaProdutos;
    }

    public JTable getTabelaCarrinho() {
        return tabelaCarrinho;
    }

    public JLabel getLblTotal() {
        return lblTotal;
    }

    public JButton getBtnAdicionar() {
        return btnAdicionar;
    }

    public JButton getBtnRemover() {
        return btnRemover;
    }

    public JButton getBtnFinalizar() {
        return btnFinalizar;
    }

    public JButton getBtnNotaFiscal() {
        return btnNotaFiscal;
    }
}