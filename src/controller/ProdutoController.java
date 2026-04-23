package controller;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import model.Produto;
import model.ProdutoDAO;
import view.TelaCadastrarProduto;

public class ProdutoController {

    private TelaCadastrarProduto view;
    private Navegador navegador;
    private JTextField tfId;
    private JTextField tfNome;
    private JTextField tfPreco;
    private JTextField tfQuantidade;
    private JTable table;

    public ProdutoController(TelaCadastrarProduto view, Navegador navegador) {
        this.view = view;
        this.navegador = navegador;

        // TelaCadastrarProduto nao tem getters, buscamos os componentes diretamente
        List<JTextField> campos = new ArrayList<>();
        List<JButton> botoes = new ArrayList<>();

        for (Component c : view.getComponents()) {
            if (c instanceof JTextField) campos.add((JTextField) c);
            else if (c instanceof JButton) botoes.add((JButton) c);
            else if (c instanceof JScrollPane) {
                Component v = ((JScrollPane) c).getViewport().getView();
                if (v instanceof JTable) table = (JTable) v;
            }
        }

        tfId = campos.get(0);
        tfNome = campos.get(1);
        tfPreco = campos.get(2);
        tfQuantidade = campos.get(3);

        // Ao selecionar uma linha, preenche os campos para edicao
        if (table != null) {
            table.getSelectionModel().addListSelectionListener(e -> {
                if (!e.getValueIsAdjusting()) {
                    int linha = table.getSelectedRow();
                    if (linha != -1) {
                        tfId.setText(table.getValueAt(linha, 0).toString());
                        tfNome.setText(table.getValueAt(linha, 1).toString());
                        tfPreco.setText(table.getValueAt(linha, 2).toString());
                        tfQuantidade.setText(table.getValueAt(linha, 3).toString());
                    }
                }
            });
        }

        if (botoes.size() > 0) botoes.get(0).addActionListener(e -> adicionarProduto());
        if (botoes.size() > 1) botoes.get(1).addActionListener(e -> atualizarProduto());
        if (botoes.size() > 2) botoes.get(2).addActionListener(e -> excluirProduto());
    }

    public void carregarTabela() {
        if (table == null) return;

        ProdutoDAO dao = new ProdutoDAO();
        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
        modelo.setRowCount(0);

        for (Produto p : dao.listarTodos()) {
            modelo.addRow(new Object[]{p.getId(), p.getNome(), String.format("%.2f", p.getPreco()), p.getQtdEstoque()});
        }
        limparCampos();
    }

    private void adicionarProduto() {
        String nome = tfNome.getText().trim();
        String precoTxt = tfPreco.getText().trim().replace(",", ".");
        String qtdTxt = tfQuantidade.getText().trim();

        if (nome.isEmpty() || precoTxt.isEmpty() || qtdTxt.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Preencha Nome, Preco e Quantidade.");
            return;
        }

        try {
            double preco = Double.parseDouble(precoTxt);
            int qtd = Integer.parseInt(qtdTxt);

            new ProdutoDAO().cadastrar(new Produto(0, nome, preco, qtd));
            JOptionPane.showMessageDialog(null, "Produto '" + nome + "' cadastrado com sucesso!");
            carregarTabela();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Preco e Quantidade devem ser numeros validos.");
        }
    }

    private void atualizarProduto() {
        String idTxt = tfId.getText().trim();

        if (idTxt.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Selecione um produto na tabela para atualizar.");
            return;
        }

        String nome = tfNome.getText().trim();
        String precoTxt = tfPreco.getText().trim().replace(",", ".");
        String qtdTxt = tfQuantidade.getText().trim();

        try {
            int id = Integer.parseInt(idTxt);
            double preco = Double.parseDouble(precoTxt);
            int qtd = Integer.parseInt(qtdTxt);

            new ProdutoDAO().atualizar(new Produto(id, nome, preco, qtd));
            JOptionPane.showMessageDialog(null, "Produto atualizado com sucesso!");
            carregarTabela();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Preco e Quantidade devem ser numeros validos.");
        }
    }

    private void excluirProduto() {
        String idTxt = tfId.getText().trim();

        if (idTxt.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Selecione um produto na tabela para excluir.");
            return;
        }

        int confirma = JOptionPane.showConfirmDialog(null, "Deseja excluir este produto?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {
            new ProdutoDAO().excluir(Integer.parseInt(idTxt));
            JOptionPane.showMessageDialog(null, "Produto excluido com sucesso!");
            carregarTabela();
        }
    }

    private void limparCampos() {
        tfId.setText("");
        tfNome.setText("");
        tfPreco.setText("");
        tfQuantidade.setText("");
    }
}
