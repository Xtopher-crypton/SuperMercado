package controller;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.ProdutoDAO;
import view.TelaCompra;

public class CompraController {

    private TelaCompra view;
    private double valorTotal = 0.0;
    private List<Integer> idsCarrinho = new ArrayList<>();

    public CompraController(TelaCompra view) {
        this.view = view;

        view.getBtnAdicionar().addActionListener(e -> adicionarAoCarrinho());
        view.getBtnRemover().addActionListener(e -> removerDoCarrinho());
        view.getBtnFinalizar().addActionListener(e -> finalizarCompra());
        view.getBtnNotaFiscal().addActionListener(e -> emitirNotaFiscal());
    }

    private void adicionarAoCarrinho() {
        int linha = view.getTabelaProdutos().getSelectedRow();

        if (linha == -1) {
            JOptionPane.showMessageDialog(null, "Selecione um produto para adicionar.");
            return;
        }

        int id = Integer.parseInt(view.getTabelaProdutos().getValueAt(linha, 0).toString());
        String nome = view.getTabelaProdutos().getValueAt(linha, 1).toString();
        String precoStr = view.getTabelaProdutos().getValueAt(linha, 2).toString();
        int estoque = Integer.parseInt(view.getTabelaProdutos().getValueAt(linha, 3).toString());

        if (estoque <= 0) {
            JOptionPane.showMessageDialog(null, "Produto sem estoque!");
            return;
        }

        DefaultTableModel modeloCarrinho = (DefaultTableModel) view.getTabelaCarrinho().getModel();
        modeloCarrinho.addRow(new Object[]{nome, 1, precoStr});
        idsCarrinho.add(id);

        valorTotal += Double.parseDouble(precoStr.replace(",", "."));
        view.getLblTotal().setText(String.format("Total: R$ %.2f", valorTotal));
    }

    private void removerDoCarrinho() {
        int linha = view.getTabelaCarrinho().getSelectedRow();

        if (linha == -1) {
            JOptionPane.showMessageDialog(null, "Selecione um item do carrinho para remover.");
            return;
        }

        DefaultTableModel modelo = (DefaultTableModel) view.getTabelaCarrinho().getModel();
        String precoStr = modelo.getValueAt(linha, 2).toString();

        valorTotal -= Double.parseDouble(precoStr.replace(",", "."));
        view.getLblTotal().setText(String.format("Total: R$ %.2f", valorTotal));
        modelo.removeRow(linha);
        idsCarrinho.remove(linha);
    }

    private void finalizarCompra() {
        DefaultTableModel modelo = (DefaultTableModel) view.getTabelaCarrinho().getModel();

        if (modelo.getRowCount() == 0) {
            JOptionPane.showMessageDialog(null, "O carrinho esta vazio!");
            return;
        }

        ProdutoDAO dao = new ProdutoDAO();
        for (int id : idsCarrinho) {
            dao.baixarEstoque(id, 1);
        }

        JOptionPane.showMessageDialog(null, String.format("Compra finalizada! Total: R$ %.2f", valorTotal));
        modelo.setRowCount(0);
        idsCarrinho.clear();
        valorTotal = 0.0;
        view.getLblTotal().setText("Total: R$ 0,00");
    }

    private void emitirNotaFiscal() {
        DefaultTableModel modelo = (DefaultTableModel) view.getTabelaCarrinho().getModel();

        if (modelo.getRowCount() == 0) {
            JOptionPane.showMessageDialog(null, "O carrinho esta vazio!");
            return;
        }

        StringBuilder nota = new StringBuilder("======= NOTA FISCAL =======\n");

        for (int i = 0; i < modelo.getRowCount(); i++) {
            String produto = modelo.getValueAt(i, 0).toString();
            String qtd = modelo.getValueAt(i, 1).toString();
            String subtotal = modelo.getValueAt(i, 2).toString();
            nota.append(String.format("- %s (x%s)  R$ %s\n", produto, qtd, subtotal));
        }

        nota.append("---------------------------\n");
        nota.append(String.format("TOTAL: R$ %.2f", valorTotal));

        JOptionPane.showMessageDialog(null, nota.toString(), "Nota Fiscal", JOptionPane.INFORMATION_MESSAGE);
    }
}
