package controller;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import model.UsuarioDAO;
import model.SupermercadoException;
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

        List<JTextField> campos = new ArrayList<>();
        List<JRadioButton> radios = new ArrayList<>();

        for (Component c : view.getComponents()) {
            if (c instanceof JTextField) campos.add((JTextField) c);
            else if (c instanceof JRadioButton) radios.add((JRadioButton) c);
            else if (c instanceof JButton) {
                JButton btn = (JButton) c;
                if ("Voltar".equalsIgnoreCase(btn.getText())) {
                    btn.addActionListener(e -> voltar());
                } else {
                    btn.addActionListener(e -> executarCadastro());
                }
            }
        }

        tfNome = campos.get(0);
        tfCpf = campos.get(1);
        if (!radios.isEmpty()) {
            rdbtnAdministrador = radios.get(0);
        }
    }

    private void voltar() {
        this.navegador.navegarPara("LOGIN");
    }

    private void executarCadastro() {
        String nome = tfNome.getText().trim();
        String cpfOriginal = tfCpf.getText().trim();
        
        // Proteção extra caso o rdbtn não exista
        boolean eAdmin = rdbtnAdministrador != null && rdbtnAdministrador.isSelected();

        // Limpa possíveis pontos e traços que o utilizador tenha digitado por hábito
        String cpfLimpo = cpfOriginal.replace(".", "").replace("-", "");

        if (nome.isEmpty() || cpfLimpo.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Preencha todos os campos.");
            return;
        }

        // VALIDAÇÃO 1: Verificar se contém caracteres que não são números (letras ou símbolos)
        if (!cpfLimpo.matches("\\d+")) {
            JOptionPane.showMessageDialog(null, "O CPF é inválido. Digite apenas números.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // VALIDAÇÃO 2: Verificar o limite exato de 11 caracteres
        if (cpfLimpo.length() != 11) {
            JOptionPane.showMessageDialog(null, "O CPF deve ter exatamente 11 dígitos.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String perfil = eAdmin ? "Admin" : "Cliente";

        try {
            UsuarioDAO dao = new UsuarioDAO();
            // Guarda na base de dados o CPF apenas com os números limpos
            dao.cadastrarUsuario(nome, cpfLimpo, perfil);

            JOptionPane.showMessageDialog(null, "Utilizador " + nome + " registado com sucesso!");
            limparCampos();
            navegador.navegarPara("LOGIN");
            
        } catch (SupermercadoException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Falha no Registo", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limparCampos() {
        tfNome.setText("");
        tfCpf.setText("");
        if (rdbtnAdministrador != null) {
            rdbtnAdministrador.setSelected(false);
        }
    }
}