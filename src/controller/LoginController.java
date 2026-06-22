package controller;

import java.awt.Component;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.Produto;
import model.ProdutoDAO;
import model.UsuarioDAO;
import model.SupermercadoException;
import view.TelaCompra;
import view.TelaLogin;

public class LoginController {

    private TelaLogin view;
    private Navegador navegador;
    private ProdutoController produtoController;
    private TelaCompra telaCompra;

    public LoginController(TelaLogin view, Navegador navegador, ProdutoController produtoController, TelaCompra telaCompra) {
        this.view = view;
        this.navegador = navegador;
        this.produtoController = produtoController;
        this.telaCompra = telaCompra;

        for (Component c : view.getComponents()) {
            if (c instanceof JButton && "Entrar".equals(((JButton) c).getText())) {
                ((JButton) c).addActionListener(e -> fazerLogin());
                break;
            }
        }

        view.acaoCadastrar(e -> {
            view.limpaCampos();
            navegador.navegarPara("CADASTRO");
        });
    }

    private void fazerLogin() {
        String nome = view.gettFNome().getText().trim();
        String cpfOriginal = view.gettFCpf().getText().trim();
        
        // Limpa possíveis pontos e traços na hora de consultar a base de dados
        String cpfLimpo = cpfOriginal.replace(".", "").replace("-", "");

        if (nome.isEmpty() || cpfLimpo.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Preencha todos os campos.");
            return;
        }

        // Verifica se o utilizador digitou letras no CPF de Login
        if (!cpfLimpo.matches("\\d+")) {
            JOptionPane.showMessageDialog(null, "O CPF deve conter apenas números.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            UsuarioDAO dao = new UsuarioDAO();
            // Valida utilizando o CPF limpo
            String perfil = dao.validarLogin(nome, cpfLimpo);

            if (perfil == null) {
                JOptionPane.showMessageDialog(null, "Utilizador ou CPF incorretos!");

            } else if (perfil.equalsIgnoreCase("Admin")) {
                JOptionPane.showMessageDialog(null, "Bem-vindo, Administrador!");
                view.limpaCampos();
                produtoController.carregarTabela();
                navegador.navegarPara("CADASTRO_PRODUTO");

            } else {
                JOptionPane.showMessageDialog(null, "Bem-vindo, Cliente!");
                view.limpaCampos();
                carregarProdutosCompra();
                navegador.navegarPara("COMPRA");
            }
        } catch (SupermercadoException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Erro no Sistema", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarProdutosCompra() {
        ProdutoDAO dao = new ProdutoDAO();
        DefaultTableModel modelo = (DefaultTableModel) telaCompra.getTabelaProdutos().getModel();
        modelo.setRowCount(0);

        for (Produto p : dao.listarTodos()) {
            modelo.addRow(new Object[]{
                p.getId(),
                p.getNome(),
                String.format("%.2f", p.getPreco()),
                p.getQtdEstoque()
            });
        }
    }
}