package view;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.security.Principal;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * Janela principal da aplicacao usando CardLayout para navegar entre telas.
 *
 * Implementa java.security.Principal porque TelaCadastrarProduto exige
 * um objeto Principal em seu construtor — esse e o contrato da view
 * original que nao pode ser alterada.
 */
public class Janela extends JFrame implements Principal {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private CardLayout cardLayout;

    public Janela() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("SuperMercado");
        setBounds(0, 0, 1000, 700);

        this.cardLayout  = new CardLayout();
        this.contentPane = new JPanel(this.cardLayout);
        this.contentPane.setPreferredSize(new Dimension(1000, 700));
        this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(this.contentPane);
    }

    /** Exigido por java.security.Principal */
    @Override
    public String getName() {
        return "SuperMercado";
    }

    public void adicionarTela(String nome, JPanel tela) {
        this.contentPane.add(tela, nome);
    }

    public void mostrarTela(String nome) {
        this.cardLayout.show(this.contentPane, nome);
        this.pack();
    }
}
