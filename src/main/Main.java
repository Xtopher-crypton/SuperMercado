package main;

import controller.CadastroController;
import controller.CompraController;
import controller.LoginController;
import controller.Navegador;
import controller.ProdutoController;
import view.Janela;
import view.TelaCadastrarProduto;
import view.TelaCadastro;
import view.TelaCompra;
import view.TelaLogin;

public class Main {

    public static void main(String[] args) {
        Janela janela = new Janela();
        Navegador navegador = new Navegador(janela);

        TelaLogin telaLogin = new TelaLogin();
        TelaCadastro telaCadastro = new TelaCadastro();
        TelaCadastrarProduto telaProduto = new TelaCadastrarProduto(janela);
        TelaCompra telaCompra = new TelaCompra();

        navegador.adicionarPainel("LOGIN", telaLogin);
        navegador.adicionarPainel("CADASTRO", telaCadastro);
        navegador.adicionarPainel("CADASTRO_PRODUTO", telaProduto);
        navegador.adicionarPainel("COMPRA", telaCompra);

        ProdutoController produtoController = new ProdutoController(telaProduto, navegador);
        new CompraController(telaCompra);
        new CadastroController(telaCadastro, navegador);
        new LoginController(telaLogin, navegador, produtoController, telaCompra);

        janela.setLocationRelativeTo(null);
        janela.setVisible(true);
        navegador.navegarPara("LOGIN");
    }
}
