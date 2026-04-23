package controller;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import model.UsuarioDAO;
import view.TelaCadastro;

public class CadastroController {

    private TelaCadastro view;
    private Navegador navegador;
    private JTextField tfNome;
    private JTextField tfCpf;
    private JRadioButton rdbtnAdministrador;

    public CadastroController(TelaCadastro view, Navegador navegador) {
        this.view = view;
        this.navegador = navegador;

        // TelaCadastro nao tem getters, entao buscamos os componentes diretamente
        List<JTextField> campos = new ArrayList<>();
        List<JRadioButton> radios = new ArrayList<>();
        List<JButton> botoes = new ArrayList<>();

        for (Component c : view.getComponents()) {
            if (c instanceof JTextField) campos.add((JTextField) c);
            else if (c instanceof JRadioButton) radios.add((JRadioButton) c);
            else if (c instanceof JButton) botoes.add((JButton) c);
        }

        tfNome = campos.get(0);
        tfCpf = campos.get(1);
        rdbtnAdministrador = radios.get(0);

        if (!botoes.isEmpty()) {
            botoes.get(0).addActionListener(e -> executarCadastro());
        }
    }

    private void executarCadastro() {
        String nome = tfNome.getText().trim();
        String cpf = tfCpf.getText().trim();
        boolean eAdmin = rdbtnAdministrador.isSelected();

        if (nome.isEmpty() || cpf.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Preencha todos os campos.");
            return;
        }

        String perfil = eAdmin ? "Admin" : "Cliente";

        UsuarioDAO dao = new UsuarioDAO();
        dao.cadastrarUsuario(nome, cpf, perfil);

        JOptionPane.showMessageDialog(null, "Usuario " + nome + " cadastrado com sucesso!");
        limparCampos();
        navegador.navegarPara("LOGIN");
    }

    private void limparCampos() {
        tfNome.setText("");
        tfCpf.setText("");
        rdbtnAdministrador.setSelected(false);
    }
}
