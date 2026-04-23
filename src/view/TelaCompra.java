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

    public TelaCompra() {
        setLayout(new MigLayout("fill", "[grow][grow]", "[][grow][]"));

        JLabel titulo = new JLabel("Compra de Produtos");
        titulo.setFont(new Font("Arial", Font.BOLD, 22));

        add(titulo, "span 2, center, wrap");

        String[] colunasProdutos = {"ID", "Produto", "Preço", "Estoque"};
        DefaultTableModel modeloProdutos = new DefaultTableModel(colunasProdutos, 0);

        tabelaProdutos = new JTable(modeloProdutos);
        JScrollPane scrollProdutos = new JScrollPane(tabelaProdutos);

        JPanel painelProdutos = new JPanel(new MigLayout("fill"));
        painelProdutos.setBorder(BorderFactory.createTitledBorder("Produtos"));
        painelProdutos.add(scrollProdutos, "grow");

        add(painelProdutos, "grow");

        String[] colunasCarrinho = {"Produto", "Qtd", "Subtotal"};
        DefaultTableModel modeloCarrinho = new DefaultTableModel(colunasCarrinho, 0);

        tabelaCarrinho = new JTable(modeloCarrinho);
        JScrollPane scrollCarrinho = new JScrollPane(tabelaCarrinho);

        JPanel painelCarrinho = new JPanel(new MigLayout("fill"));
        painelCarrinho.setBorder(BorderFactory.createTitledBorder("Carrinho"));
        painelCarrinho.add(scrollCarrinho, "grow");

        add(painelCarrinho, "grow, wrap");

        btnAdicionar = new JButton("Adicionar >>");
        btnRemover = new JButton("<< Remover");

        add(btnAdicionar, "split 2, center");
        add(btnRemover, "wrap");

        lblTotal = new JLabel("Total: R$ 0,00");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 18));

        btnFinalizar = new JButton("Finalizar Compra");
        btnNotaFiscal = new JButton("Emitir Nota Fiscal");

        add(lblTotal, "left");
        add(btnFinalizar, "split 2, right");
        add(btnNotaFiscal);
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