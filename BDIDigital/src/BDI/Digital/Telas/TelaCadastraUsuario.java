package BDI.Digital.Telas;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import BDI.Digital.Telas.TelaPrincipal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import BDI.Digital.DAL.ModuloConexao;
import java.awt.Dimension;

/**
 *
 * @author aluno.lab04
 */
public class TelaCadastraUsuario extends javax.swing.JInternalFrame {

    /**
     * Creates new form TelaCadastraUsuario
     */
    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    String matriculaPesquisada = "";
    String usuarioPesquisado = "";
    String usuarioLogado;
    
    public TelaCadastraUsuario() {
        initComponents();
        conexao = ModuloConexao.conector();
        this.requestFocusInWindow();
        btnCadastrarUsu.setEnabled(false);
        btnPesquisarUsu.setEnabled(false);
        btnAlterarUsu.setEnabled(false);
        btnDeletarUsu.setEnabled(false);
        lblMsgAlterarSenha.setVisible(false);
    }
    
    public void setPosicao(){
        Dimension d = this.getDesktopPane().getSize();
        this.setLocation((d.width - this.getSize().width) / 2, (d.height - this.getSize().height) / 2); 
    }
    
    private void limparTela(){
        txtUsuMatricula.setText(null);
        txtUsuNome.setText(null);
        txtUsuLogin.setText(null);
        txtUsuSenha.setText(null);
        txtUsuTelefone.setText(null);
        
        btnCadastrarUsu.setEnabled(false);
        btnPesquisarUsu.setEnabled(false);
        btnAlterarUsu.setEnabled(false);
        btnDeletarUsu.setEnabled(false);
        lblMsgAlterarSenha.setVisible(false);
        matriculaPesquisada = "";
        usuarioPesquisado = "";
        lblMsgAlterarSenha.setVisible(false);
    }
    
    private boolean validaCampos(String modo){
        if(modo.equals("ALTERAR")){
            if(!txtUsuMatricula.getText().isEmpty() &&
                !txtUsuNome.getText().isEmpty() &&
                !txtUsuTelefone.getText().isEmpty() &&
                !txtUsuLogin.getText().isEmpty()){
                return true;
            }
            else{
                return false;
            }
        }
        else{
            if(modo.equals("SALVAR")){
                if(!txtUsuMatricula.getText().isEmpty() &&
                    !txtUsuNome.getText().isEmpty() &&
                    !txtUsuTelefone.getText().isEmpty() &&
                    !txtUsuLogin.getText().isEmpty() &&
                    !txtUsuSenha.getText().isEmpty()){
                    return true;
                }
                else{
                    return false;
                }
            }
            else{
                return false;
            }
        }
    }
    
    private void consultar(){
        String sql = "SELECT * FROM usuario WHERE matriculaUsuario = ?";
            if(!txtUsuMatricula.getText().isEmpty()){
                matriculaPesquisada = txtUsuMatricula.getText();
                try {
                    pst = conexao.prepareStatement(sql);
                    pst.setString(1,txtUsuMatricula.getText());
                    rs = pst.executeQuery();
                    if (rs.next()) {
                        txtUsuMatricula.setText(rs.getString(1));
                        matriculaPesquisada = rs.getString(1);
                        txtUsuNome.setText(rs.getString(2));
                        usuarioPesquisado = rs.getString(1);
                        txtUsuTelefone.setText(rs.getString(3));
                        txtUsuLogin.setText(rs.getString(4));
                        btnAlterarUsu.setEnabled(true);
                        btnDeletarUsu.setEnabled(true);
                    } else {
                        JOptionPane.showMessageDialog(null, "Usuário não cadastrado");
                        limparTela();
                        lblMsgAlterarSenha.setVisible(false);
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e);
                }
            }
            else{
                JOptionPane.showMessageDialog(null, "Por favor digite uma matrícula!");
            }    
    }
    
    private String consultaPorNome(){
        PreparedStatement pstConsultaPorNome = null;
        ResultSet rsConsultaPorNome = null;
        String sqlConsultaPorNome = "SELECT * FROM USUARIO WHERE nomeUsuario = ?";
        try {
            pstConsultaPorNome = conexao.prepareStatement(sqlConsultaPorNome);
            pstConsultaPorNome.setString(1, txtUsuNome.getText());
            rsConsultaPorNome = pstConsultaPorNome.executeQuery();
            if(rsConsultaPorNome.next()){
                return "VERDADEIRO";
            }
            else{
                return "FALSO";
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            return "FALHA";
        }
    }
    
    private void adicionar(){
        String sql = "INSERT INTO usuario (matriculaUsuario, nomeUsuario, telefoneUsuario, login, senha) VALUES (?,?,?,?,?)";
        if(!txtUsuSenha.getText().isEmpty() &&
            !txtUsuNome.getText().isEmpty() &&
            !txtUsuLogin.getText().isEmpty() &&
            !txtUsuMatricula.getText().isEmpty() &&
            !txtUsuLogin.getText().isEmpty()){
            
            try {
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtUsuMatricula.getText().toUpperCase());
                pst.setString(2, txtUsuNome.getText().toUpperCase());
                pst.setString(3, txtUsuTelefone.getText().toUpperCase());
                pst.setString(4, txtUsuLogin.getText().toUpperCase());
                pst.setString(5, txtUsuSenha.getText().toUpperCase());
                //A linha abaixo atualiza a tabela usuario com os dados do formulário
                pst.executeUpdate();
                JOptionPane.showMessageDialog(null, "Usuário cadastrado com sucesso!");
                //Limpa a tela após finalizar cadastro
                limparTela();
            
            } catch (Exception e) {
                if(e.toString().contains("com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException: Duplicate entry")){
                    JOptionPane.showMessageDialog(null, "Já existe um usuário com esta matrícula ou login!");
                }
                else{
                    JOptionPane.showMessageDialog(null, e);
                }
                
                
            }
        }
        else{
            JOptionPane.showMessageDialog(null, "Todos os campos são obrigatórios!", "Atenção", JOptionPane.OK_OPTION);
        }
    }
    
    private void deletar(){ 
        if(usuarioLogado.equals(usuarioPesquisado)){
            JOptionPane.showMessageDialog(null, "Não é permitido deletar o usuário que está logado!");
        }
        else{
            String sql = "DELETE FROM usuario WHERE matriculaUsuario = ?";
                try {
                    pst = conexao.prepareStatement(sql);
                    pst.setString(1, txtUsuMatricula.getText());
                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Usuário deletado com sucesso!");
                    matriculaPesquisada = "";
                limparTela();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }
    
    private void alterar(){
        //String sql = "UPDATE usuario SET matriculaUsuario = ?, nomeUsuario = ?, telefoneUsuario = ?, login = ?, senha = ? WHERE matriculaUsuario = ?";
        if(!txtUsuMatricula.getText().isEmpty() &&
                !txtUsuNome.getText().isEmpty() &&
                !txtUsuLogin.getText().isEmpty() &&
                !txtUsuTelefone.getText().isEmpty()){
            
            if(!txtUsuSenha.getText().isEmpty()){
                try {
                String sql = "UPDATE usuario SET matriculaUsuario = ?, nomeUsuario = ?, telefoneUsuario = ?, login = ?, senha = ? WHERE matriculaUsuario = ?";
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtUsuMatricula.getText().toUpperCase());
                pst.setString(2, txtUsuNome.getText().toUpperCase());
                pst.setString(3, txtUsuTelefone.getText().toUpperCase());
                pst.setString(4, txtUsuLogin.getText().toUpperCase());
                pst.setString(5, txtUsuSenha.getText().toUpperCase());
                pst.setString(6, matriculaPesquisada);
                pst.executeUpdate();
                JOptionPane.showMessageDialog(null, "Cadastro atualizado com sucesso!");
                matriculaPesquisada = "";
                limparTela();
                } catch (Exception e) {
                    if(e.toString().contains("com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException: Duplicate entry")){
                        JOptionPane.showMessageDialog(null, "Já existe um usuário com esta matrícula ou login!");
                    }
                    else{
                        JOptionPane.showMessageDialog(null, e);
                    }
                }
            }
            else{
                try {
                String sql = "UPDATE usuario SET matriculaUsuario = ?, nomeUsuario = ?, telefoneUsuario = ?, login = ? WHERE matriculaUsuario = ?";
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtUsuMatricula.getText().toUpperCase());
                pst.setString(2, txtUsuNome.getText().toUpperCase());
                pst.setString(3, txtUsuTelefone.getText().toUpperCase());
                pst.setString(4, txtUsuLogin.getText().toUpperCase());
                pst.setString(5, matriculaPesquisada);
                pst.executeUpdate();
                JOptionPane.showMessageDialog(null, "Cadastro atualizado com sucesso!");
                matriculaPesquisada = "";
                limparTela();
                } catch (Exception e) {
                    if(e.toString().contains("com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException: Duplicate entry")){
                        JOptionPane.showMessageDialog(null, "Já existe um usuário com esta matrícula ou login!");
                    }
                    else{
                        JOptionPane.showMessageDialog(null, e);
                    }
                }
            }
            
            
        }
        else{
            JOptionPane.showMessageDialog(null, "Todos os campos são obrigatórios!", "Atenção", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtUsuLogin = new javax.swing.JTextField();
        txtUsuSenha = new javax.swing.JTextField();
        btnCadastrarUsu = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        btnLimparFormUsu = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtUsuMatricula = new javax.swing.JTextField();
        txtUsuNome = new javax.swing.JTextField();
        btnPesquisarUsu = new javax.swing.JButton();
        btnAlterarUsu = new javax.swing.JButton();
        btnDeletarUsu = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        txtUsuTelefone = new javax.swing.JTextField();
        lblMsgAlterarSenha = new javax.swing.JLabel();

        setClosable(true);
        setTitle("Cadastrar Usuário");
        setMaximumSize(new java.awt.Dimension(577, 400));
        setMinimumSize(new java.awt.Dimension(577, 400));
        setPreferredSize(new java.awt.Dimension(577, 400));

        txtUsuLogin.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txtUsuLoginCaretUpdate(evt);
            }
        });
        txtUsuLogin.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtUsuLoginKeyTyped(evt);
            }
        });

        txtUsuSenha.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txtUsuSenhaCaretUpdate(evt);
            }
        });
        txtUsuSenha.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtUsuSenhaKeyTyped(evt);
            }
        });

        btnCadastrarUsu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BDI/Digital/Icones/if_file_add_48761.png"))); // NOI18N
        btnCadastrarUsu.setToolTipText("Cadastrar Usuário");
        btnCadastrarUsu.setBorderPainted(false);
        btnCadastrarUsu.setContentAreaFilled(false);
        btnCadastrarUsu.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCadastrarUsu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCadastrarUsuActionPerformed(evt);
            }
        });

        jLabel1.setText("Matrícula");

        btnLimparFormUsu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BDI/Digital/Icones/if_CCleaner_33788.png"))); // NOI18N
        btnLimparFormUsu.setToolTipText("Limpar Formulário");
        btnLimparFormUsu.setBorderPainted(false);
        btnLimparFormUsu.setContentAreaFilled(false);
        btnLimparFormUsu.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLimparFormUsu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimparFormUsuActionPerformed(evt);
            }
        });

        jLabel2.setText("Nome");

        jLabel4.setText("Login");

        jLabel5.setText("Senha");

        txtUsuMatricula.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txtUsuMatriculaCaretUpdate(evt);
            }
        });
        txtUsuMatricula.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtUsuMatriculaKeyTyped(evt);
            }
        });

        txtUsuNome.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txtUsuNomeCaretUpdate(evt);
            }
        });
        txtUsuNome.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtUsuNomeKeyTyped(evt);
            }
        });

        btnPesquisarUsu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BDI/Digital/Icones/if_file_search_48764.png"))); // NOI18N
        btnPesquisarUsu.setToolTipText("Pesquisar Usuário");
        btnPesquisarUsu.setBorderPainted(false);
        btnPesquisarUsu.setContentAreaFilled(false);
        btnPesquisarUsu.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnPesquisarUsu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPesquisarUsuActionPerformed(evt);
            }
        });

        btnAlterarUsu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BDI/Digital/Icones/if_file_edit_48763.png"))); // NOI18N
        btnAlterarUsu.setToolTipText("Salvar alterações no Cadastro do Usuário");
        btnAlterarUsu.setBorderPainted(false);
        btnAlterarUsu.setContentAreaFilled(false);
        btnAlterarUsu.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAlterarUsu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAlterarUsuActionPerformed(evt);
            }
        });

        btnDeletarUsu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BDI/Digital/Icones/if_file_delete_48762.png"))); // NOI18N
        btnDeletarUsu.setToolTipText("Deletar Usuário");
        btnDeletarUsu.setBorderPainted(false);
        btnDeletarUsu.setContentAreaFilled(false);
        btnDeletarUsu.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnDeletarUsu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeletarUsuActionPerformed(evt);
            }
        });

        jLabel3.setText("Telefone");

        txtUsuTelefone.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txtUsuTelefoneCaretUpdate(evt);
            }
        });
        txtUsuTelefone.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtUsuTelefoneKeyTyped(evt);
            }
        });

        lblMsgAlterarSenha.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblMsgAlterarSenha.setText("*Deixe o campo senha em branco para manter a senha atual.");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addGap(142, 142, 142)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel1)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtUsuMatricula, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
                                .addComponent(txtUsuNome)
                                .addComponent(txtUsuTelefone)))
                        .addGroup(layout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtUsuLogin)
                                .addComponent(txtUsuSenha, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(143, 143, 143)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addComponent(btnCadastrarUsu, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnPesquisarUsu, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnAlterarUsu, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(56, 56, 56))
                                    .addComponent(btnDeletarUsu, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnLimparFormUsu, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(lblMsgAlterarSenha))))
                .addContainerGap(72, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtUsuMatricula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtUsuNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtUsuTelefone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtUsuLogin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtUsuSenha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblMsgAlterarSenha)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(btnCadastrarUsu)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnAlterarUsu)
                            .addComponent(btnPesquisarUsu)))
                    .addComponent(btnDeletarUsu)
                    .addComponent(btnLimparFormUsu))
                .addContainerGap(93, Short.MAX_VALUE))
        );

        setBounds(0, 0, 577, 400);
    }// </editor-fold>//GEN-END:initComponents

    private void btnCadastrarUsuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCadastrarUsuActionPerformed
        // TODO add your handling code here:
        adicionar();
    }//GEN-LAST:event_btnCadastrarUsuActionPerformed

    private void btnLimparFormUsuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimparFormUsuActionPerformed
        // TODO add your handling code here:
        limparTela();
    }//GEN-LAST:event_btnLimparFormUsuActionPerformed

    private void txtNroPasseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNroPasseActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNroPasseActionPerformed

    private void btnPesquisarUsuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPesquisarUsuActionPerformed
        // TODO add your handling code here:
        consultar();
        lblMsgAlterarSenha.setVisible(true);
        txtUsuSenha.setText(null);
    }//GEN-LAST:event_btnPesquisarUsuActionPerformed

    private void btnAlterarUsuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlterarUsuActionPerformed
        // TODO add your handling code here:
        alterar();
    }//GEN-LAST:event_btnAlterarUsuActionPerformed

    private void btnDeletarUsuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeletarUsuActionPerformed
        // TODO add your handling code here:
        deletar();
    }//GEN-LAST:event_btnDeletarUsuActionPerformed

    private void txtUsuMatriculaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUsuMatriculaKeyTyped
        // TODO add your handling code here:
        //Define que o campo só aceitará números inteiros;
        String caracteresAceitos="0987654321";
        if(!caracteresAceitos.contains(evt.getKeyChar() + "")){
            evt.consume();
        }
        else{
            if(txtUsuMatricula.getText().length() == 10){
                JOptionPane.showMessageDialog(null, "Limite máximo de caracteres permitidos no campo Matrícula atingido!");
                evt.consume();
            }
        }
    }//GEN-LAST:event_txtUsuMatriculaKeyTyped

    private void txtUsuMatriculaCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txtUsuMatriculaCaretUpdate
        // TODO add your handling code here:
        
        //Bloqueia ou libera o botão pesquisar
        if(!txtUsuMatricula.getText().isEmpty()){
            btnPesquisarUsu.setEnabled(true);
        }
        else{
            btnPesquisarUsu.setEnabled(false);
        }
        
        //Bloqueia ou libera o botão alterar
        if(validaCampos("ALTERAR") == true && txtUsuMatricula.getText().equals(matriculaPesquisada)){
            btnAlterarUsu.setEnabled(true);
        }
        else{
            btnAlterarUsu.setEnabled(false);
        }
        
        //Bloqueia ou libera o botão Cadastrar
        if(validaCampos("SALVAR")){
            btnCadastrarUsu.setEnabled(true);
        }
        else{
            btnCadastrarUsu.setEnabled(false);
        }
        
        //Bloqueia ou libera o botão deletar
        if(!txtUsuMatricula.getText().isEmpty() && txtUsuMatricula.getText().equals(matriculaPesquisada)){
            btnDeletarUsu.setEnabled(true);
        }
        else{
            btnDeletarUsu.setEnabled(false);
        }
        
        
        
    }//GEN-LAST:event_txtUsuMatriculaCaretUpdate

    private void txtUsuNomeCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txtUsuNomeCaretUpdate
        // TODO add your handling code here:
        //Bloqueia ou libera o botão Cadastrar
        if(validaCampos("SALVAR")){
            btnCadastrarUsu.setEnabled(true);
        }
        else{
            btnCadastrarUsu.setEnabled(false);
        }
        
        //Bloqueia ou libera o botão alterar
        if(validaCampos("ALTERAR") == true && txtUsuMatricula.getText().equals(matriculaPesquisada)){
            btnAlterarUsu.setEnabled(true);
        }
        else{
            btnAlterarUsu.setEnabled(false);
        }
    }//GEN-LAST:event_txtUsuNomeCaretUpdate

    private void txtUsuTelefoneCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txtUsuTelefoneCaretUpdate
        // TODO add your handling code here:
        //Bloqueia ou libera o botão Cadastrar
        if(validaCampos("SALVAR")){
            btnCadastrarUsu.setEnabled(true);
        }
        else{
            btnCadastrarUsu.setEnabled(false);
        }
        
        //Bloqueia ou libera o botão alterar
        if(validaCampos("ALTERAR") == true && txtUsuMatricula.getText().equals(matriculaPesquisada)){
            btnAlterarUsu.setEnabled(true);
        }
        else{
            btnAlterarUsu.setEnabled(false);
        }
    }//GEN-LAST:event_txtUsuTelefoneCaretUpdate

    private void txtUsuLoginCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txtUsuLoginCaretUpdate
        // TODO add your handling code here:
        //Bloqueia ou libera o botão Cadastrar
        if(validaCampos("SALVAR")){
            btnCadastrarUsu.setEnabled(true);
        }
        else{
            btnCadastrarUsu.setEnabled(false);
        }
        
        if(validaCampos("ALTERAR") == true && txtUsuMatricula.getText().equals(matriculaPesquisada)){
            btnAlterarUsu.setEnabled(true);
        }
        else{
            btnAlterarUsu.setEnabled(false);
        }
    }//GEN-LAST:event_txtUsuLoginCaretUpdate

    private void txtUsuSenhaCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txtUsuSenhaCaretUpdate
        // TODO add your handling code here:
        //Bloqueia ou libera o botão Cadastrar
        if(validaCampos("SALVAR")){
            btnCadastrarUsu.setEnabled(true);
        }
        else{
            btnCadastrarUsu.setEnabled(false);
        }
    }//GEN-LAST:event_txtUsuSenhaCaretUpdate

    private void txtUsuNomeKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUsuNomeKeyTyped
        // TODO add your handling code here:
        String caracteresAceitos="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZáãÁÃéÉíÍóõÓÕúÚ ";
        if(!caracteresAceitos.contains(evt.getKeyChar() + "")){
            evt.consume();
        }
        else{
            if(txtUsuNome.getText().length() == 80){
                JOptionPane.showMessageDialog(null, "Limite de caracteres permitidos no campo Nome atingido!");
                evt.consume();
            }
        }
    }//GEN-LAST:event_txtUsuNomeKeyTyped

    private void txtUsuTelefoneKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUsuTelefoneKeyTyped
        // TODO add your handling code here:
        String caracteresAceitos="0987654321";
        if(!caracteresAceitos.contains(evt.getKeyChar() + "")){
            evt.consume();
        }
        else{
            if(txtUsuTelefone.getText().length() == 16){
                JOptionPane.showMessageDialog(null, "Limite máximo de caracteres permitidos no campo Telefone atingido!");
                evt.consume();
            }
        }
    }//GEN-LAST:event_txtUsuTelefoneKeyTyped

    private void txtUsuLoginKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUsuLoginKeyTyped
        // TODO add your handling code here:
        String caracteresAceitos="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        if(!caracteresAceitos.contains(evt.getKeyChar() + "")){
            evt.consume();
        }
        else{
            if(txtUsuLogin.getText().length() == 15){
                JOptionPane.showMessageDialog(null, "Limite máximo de caracteres permitidos no campo Login atingido!");
                evt.consume();
            }
        }
    }//GEN-LAST:event_txtUsuLoginKeyTyped

    private void txtUsuSenhaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUsuSenhaKeyTyped
        // TODO add your handling code here:
        String caracteresAceitos="0987654321abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        if(!caracteresAceitos.contains(evt.getKeyChar() + "")){
            evt.consume();
        }
        else{
            if(txtUsuSenha.getText().length() == 15){
                JOptionPane.showMessageDialog(null, "Limite máximo de caracteres permitidos no campo Senha atingido!");
                evt.consume();
            }
        }
    }//GEN-LAST:event_txtUsuSenhaKeyTyped


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAlterarUsu;
    private javax.swing.JButton btnCadastrarUsu;
    private javax.swing.JButton btnDeletarUsu;
    private javax.swing.JButton btnLimparFormUsu;
    private javax.swing.JButton btnPesquisarUsu;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel lblMsgAlterarSenha;
    private javax.swing.JTextField txtUsuLogin;
    public static javax.swing.JTextField txtUsuMatricula;
    private javax.swing.JTextField txtUsuNome;
    private javax.swing.JTextField txtUsuSenha;
    private javax.swing.JTextField txtUsuTelefone;
    // End of variables declaration//GEN-END:variables
}
