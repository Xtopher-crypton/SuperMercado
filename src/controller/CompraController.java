package controller;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.Produto;
import model.ProdutoDAO;
import model.SupermercadoException;
import view.TelaCompra;

public class CompraController {

    private TelaCompra view;
    private Navegador navegador;
    private double valorTotal = 0.0;
    private List<Integer> idsCarrinho = new ArrayList<>();

    public CompraController(TelaCompra view, Navegador navegador) {
        this.view = view;
        this.navegador = navegador;

        view.getBtnAdicionar().addActionListener(e -> adicionarAoCarrinho());
        view.getBtnRemover().addActionListener(e -> removerDoCarrinho());
        view.getBtnFinalizar().addActionListener(e -> finalizarCompra());
        view.getBtnNotaFiscal().addActionListener(e -> emitirNotaFiscal());

        for (Component c : view.getComponents()) {
            if (c instanceof JButton) {
                JButton btn = (JButton) c;
                if ("Voltar".equalsIgnoreCase(btn.getText())) {
                    btn.addActionListener(e -> voltar());
                }
            }
        }
    }

    private void voltar() {
        DefaultTableModel modelo = (DefaultTableModel) view.getTabelaCarrinho().getModel();
        modelo.setRowCount(0);
        idsCarrinho.clear();
        valorTotal = 0.0;
        view.getLblTotal().setText("Total: R$ 0,00");
        this.navegador.navegarPara("LOGIN");
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

        DefaultTableModel modeloCarrinho = (DefaultTableModel) view.getTabelaCarrinho().getModel();
        
        // Verifica se o produto já existe no carrinho buscando pelo nome
        int linhaExistente = -1;
        for (int i = 0; i < modeloCarrinho.getRowCount(); i++) {
            if (modeloCarrinho.getValueAt(i, 0).toString().equals(nome)) {
                linhaExistente = i;
                break;
            }
        }

        int qtdNoCarrinho = 0;
        if (linhaExistente != -1) {
            qtdNoCarrinho = Integer.parseInt(modeloCarrinho.getValueAt(linhaExistente, 1).toString());
        }

        // Valida se a nova quantidade ultrapassa o estoque disponível na base de dados
        if (qtdNoCarrinho + 1 > estoque) {
            JOptionPane.showMessageDialog(null, "Quantidade indisponível em estoque!");
            return;
        }

        double preco = Double.parseDouble(precoStr.replace(",", "."));

        if (linhaExistente != -1) {
            // Se o produto já existe, mescla aumentando a quantidade e recalculando o subtotal
            int novaQtd = qtdNoCarrinho + 1;
            double novoSubtotal = preco * novaQtd;
            modeloCarrinho.setValueAt(novaQtd, linhaExistente, 1);
            modeloCarrinho.setValueAt(String.format("%.2f", novoSubtotal), linhaExistente, 2);
        } else {
            // Se for um item novo, cria a linha e alinha o ID correspondente
            modeloCarrinho.addRow(new Object[]{nome, 1, String.format("%.2f", preco)});
            idsCarrinho.add(id);
        }

        valorTotal += preco;
        view.getLblTotal().setText(String.format("Total: R$ %.2f", valorTotal));
    }

    private void removerDoCarrinho() {
        int linha = view.getTabelaCarrinho().getSelectedRow();

        if (linha == -1) {
            JOptionPane.showMessageDialog(null, "Selecione um item do carrinho para remover.");
            return;
        }

        DefaultTableModel modelo = (DefaultTableModel) view.getTabelaCarrinho().getModel();
        int qtd = Integer.parseInt(modelo.getValueAt(linha, 1).toString());
        double subtotal = Double.parseDouble(modelo.getValueAt(linha, 2).toString().replace(",", "."));
        double precoUnitario = subtotal / qtd;

        valorTotal -= precoUnitario;
        view.getLblTotal().setText(String.format("Total: R$ %.2f", valorTotal));

        if (qtd > 1) {
            // Se houver mais de uma unidade, reduz a quantidade e atualiza o subtotal
            int novaQtd = qtd - 1;
            modelo.setValueAt(novaQtd, linha, 1);
            modelo.setValueAt(String.format("%.2f", precoUnitario * novaQtd), linha, 2);
        } else {
            // Se for a última unidade restante, remove a linha completamente
            modelo.removeRow(linha);
            idsCarrinho.remove(linha);
        }
    }

    private void finalizarCompra() {
        DefaultTableModel modelo = (DefaultTableModel) view.getTabelaCarrinho().getModel();

        if (modelo.getRowCount() == 0) {
            JOptionPane.showMessageDialog(null, "O carrinho está vazio!");
            return;
        }

        try {
            ProdutoDAO dao = new ProdutoDAO();
            // Percorre a tabela para baixar a quantidade correta acumulada de cada produto
            for (int i = 0; i < modelo.getRowCount(); i++) {
                int id = idsCarrinho.get(i);
                int qtd = Integer.parseInt(modelo.getValueAt(i, 1).toString());
                dao.baixarEstoque(id, qtd);
            }

            JOptionPane.showMessageDialog(null, String.format("Compra finalizada! Total: R$ %.2f", valorTotal));
            modelo.setRowCount(0);
            idsCarrinho.clear();
            valorTotal = 0.0;
            view.getLblTotal().setText("Total: R$ 0,00");
            
            atualizarTabelaProdutos(); 
            
        } catch (SupermercadoException e) {
            JOptionPane.showMessageDialog(null, "Erro ao processar compra: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void atualizarTabelaProdutos() {
        try {
            DefaultTableModel modelo = (DefaultTableModel) view.getTabelaProdutos().getModel();
            modelo.setRowCount(0);
            ProdutoDAO dao = new ProdutoDAO();
            
            for (Produto p : dao.listarTodos()) {
                modelo.addRow(new Object[]{p.getId(), p.getNome(), String.format("%.2f", p.getPreco()), p.getQtdEstoque()});
            }
        } catch (SupermercadoException e) {
            System.err.println("Erro ao recarregar a tabela: " + e.getMessage());
        }
    }

    private void emitirNotaFiscal() {
        DefaultTableModel modelo = (DefaultTableModel) view.getTabelaCarrinho().getModel();

        if (modelo.getRowCount() == 0) {
            JOptionPane.showMessageDialog(null, "O carrinho está vazio!");
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