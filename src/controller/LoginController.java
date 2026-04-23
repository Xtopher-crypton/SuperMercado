package controller;

import java.awt.Component;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.Produto;
import model.ProdutoDAO;
import model.UsuarioDAO;
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

        // Bug na view: btnEntrar nunca foi atribuido ao campo, existe so como variavel local.
        // Solucao: buscar o botao pelos componentes do painel pelo texto "Entrar".
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
        String cpf = view.gettFCpf().getText().trim();

        if (nome.isEmpty() || cpf.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Preencha todos os campos.");
            return;
        }

        UsuarioDAO dao = new UsuarioDAO();
        String perfil = dao.validarLogin(nome, cpf);

        if (perfil == null) {
            JOptionPane.showMessageDialog(null, "Usuario ou CPF incorretos!");

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
