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
import model.SupermercadoException;
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

        List<JTextField> campos = new ArrayList<>();

        for (Component c : view.getComponents()) {
            if (c instanceof JTextField) {
                campos.add((JTextField) c);
            } else if (c instanceof JScrollPane) {
                Component v = ((JScrollPane) c).getViewport().getView();
                if (v instanceof JTable) {
                    table = (JTable) v;
                }
            }
        }

        if (campos.size() >= 4) {
            tfId = campos.get(0);
            tfNome = campos.get(1);
            tfPreco = campos.get(2);
            tfQuantidade = campos.get(3);
        }

        for (Component c : view.getComponents()) {
            if (c instanceof JButton) {
                JButton btn = (JButton) c;
                String textoBotao = btn.getText();

                if ("Adicionar".equalsIgnoreCase(textoBotao)) {
                    btn.addActionListener(e -> adicionarProduto());
                } else if ("Atualizar".equalsIgnoreCase(textoBotao)) {
                    btn.addActionListener(e -> atualizarProduto());
                } else if ("Excluir".equalsIgnoreCase(textoBotao)) {
                    btn.addActionListener(e -> excluirProduto());
                } else if ("Voltar".equalsIgnoreCase(textoBotao)) {
                    btn.addActionListener(e -> voltar());
                }
            }
        }

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
    }

    private void voltar() {
        this.navegador.navegarPara("LOGIN");
    }

    public void carregarTabela() {
        if (table == null) return;

        try {
            ProdutoDAO dao = new ProdutoDAO();
            DefaultTableModel modelo = (DefaultTableModel) table.getModel();
            modelo.setRowCount(0);

            for (Produto p : dao.listarTodos()) {
                modelo.addRow(new Object[]{p.getId(), p.getNome(), String.format("%.2f", p.getPreco()), p.getQtdEstoque()});
            }
            limparCampos();
        } catch (SupermercadoException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void adicionarProduto() {
        String nome = tfNome.getText().trim();
        String precoTxt = tfPreco.getText().trim().replace(",", ".");
        String qtdTxt = tfQuantidade.getText().trim();

        if (nome.isEmpty() || precoTxt.isEmpty() || qtdTxt.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Preencha Nome, Preço e Quantidade.");
            return;
        }

        try {
            double preco = Double.parseDouble(precoTxt);
            int qtd = Integer.parseInt(qtdTxt);

            // VALIDAÇÃO 1: Impede quantidade (e preço) menor que zero
            if (qtd < 0 || preco < 0) {
                JOptionPane.showMessageDialog(null, "A quantidade em stock e o preço não podem ser menores que zero.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            ProdutoDAO dao = new ProdutoDAO();
            
            // VALIDAÇÃO 2: Impede produto com o mesmo nome (Passa 0 como ID pois é um novo registo)
            if (dao.existeProdutoPorNome(nome, 0)) {
                JOptionPane.showMessageDialog(null, "Já existe um produto registado com o nome '" + nome + "'.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            dao.cadastrar(new Produto(0, nome, preco, qtd));
            JOptionPane.showMessageDialog(null, "Produto '" + nome + "' registado com sucesso!");
            carregarTabela();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Preço e Quantidade devem ser números válidos.");
        } catch (SupermercadoException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Erro na Base de Dados", JOptionPane.ERROR_MESSAGE);
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

            // VALIDAÇÃO 1: Impede quantidade (e preço) menor que zero
            if (qtd < 0 || preco < 0) {
                JOptionPane.showMessageDialog(null, "A quantidade em stock e o preço não podem ser menores que zero.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            ProdutoDAO dao = new ProdutoDAO();
            
            // VALIDAÇÃO 2: Impede que o novo nome já pertença a outro produto (Passa o ID atual para ser ignorado)
            if (dao.existeProdutoPorNome(nome, id)) {
                JOptionPane.showMessageDialog(null, "Já existe OUTRO produto registado com o nome '" + nome + "'.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            dao.atualizar(new Produto(id, nome, preco, qtd));
            JOptionPane.showMessageDialog(null, "Produto atualizado com sucesso!");
            carregarTabela();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Preço e Quantidade devem ser números válidos.");
        } catch (SupermercadoException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Erro na Base de Dados", JOptionPane.ERROR_MESSAGE);
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
            try {
                new ProdutoDAO().excluir(Integer.parseInt(idTxt));
                JOptionPane.showMessageDialog(null, "Produto excluído com sucesso!");
                carregarTabela();
            } catch (SupermercadoException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Erro na Base de Dados", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void limparCampos() {
        tfId.setText("");
        tfNome.setText("");
        tfPreco.setText("");
        tfQuantidade.setText("");
    }
}