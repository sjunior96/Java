/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BDI.Digital.Telas;

import BDI.Digital.DAL.ModuloConexao;
import java.awt.Dimension;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author Silvio
 */
public class TelaCadastraFuncionario extends javax.swing.JInternalFrame {

    /**
     * Creates new form TelaCadastraFuncionario
     */
    
    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    String matriculaPesquisada = "";
    
    public TelaCadastraFuncionario() {
        initComponents();
        conexao = ModuloConexao.conector();
        btnCadastraFunc.setEnabled(false);
        btnPesquisaFunc.setEnabled(false);
        btnAlterarFunc.setEnabled(false);
        btnDeletarFunc.setEnabled(false);
    }
    
    public void setPosicao(){
        Dimension d = this.getDesktopPane().getSize();
        this.setLocation((d.width - this.getSize().width) / 2, (d.height - this.getSize().height) / 2); 
    }
    
    private void limparTela(){
        txtFuncMatricula.setText(null);
        txtFuncNome.setText(null);
        txtFuncMatricula.setText(null);
        txtFuncTelefone.setText(null);
        matriculaPesquisada = "";
    }
    
    private boolean validaCampos(){
        if(!txtFuncMatricula.getText().isEmpty() &&
                !txtFuncNome.getText().isEmpty() &&
                !txtFuncTelefone.getText().isEmpty()){
            return true;
        }
        else{
            return false;
        }
    }
    
    private void consultar(){
        String sql = "SELECT * FROM funcionario WHERE matriculaFuncionario = ?";
        if(!txtFuncMatricula.getText().isEmpty()){
            try {
                pst = conexao.prepareStatement(sql);
                pst.setString(1,txtFuncMatricula.getText());
                rs = pst.executeQuery();
                if (rs.next()) {
                    txtFuncMatricula.setText(rs.getString(1));
                    matriculaPesquisada = rs.getString(1);
                    txtFuncNome.setText(rs.getString(2));
                    txtFuncTelefone.setText(rs.getString(3));
                    if(rs.getString(4).equals("M")){
                        cboFuncCargo.setSelectedItem("Motorista");
                    }
                    else{
                        cboFuncCargo.setSelectedItem("Cobrador");
                    }
                    //txtUsuSenha.setText(rs.getString(5));
                    btnAlterarFunc.setEnabled(true);
                    btnDeletarFunc.setEnabled(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Usuário não cadastrado");
                    txtFuncMatricula.setText(null);
                    //cboFuncCargo.setSelectedItem(null);
                    txtFuncMatricula.setText(null);
                    txtFuncNome.setText(null);
                    txtFuncTelefone.setText(null);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
        else{
            JOptionPane.showMessageDialog(null, "Por favor digite uma matrícula!");
        }
    }
    
    private void adicionar(){
        String sql = "INSERT INTO funcionario (matriculaFuncionario, nomeFuncionario, telefoneFuncionario, cargoFuncionario) VALUES (?,?,?,?)";
        if(!txtFuncNome.getText().isEmpty() &&
            !txtFuncTelefone.getText().isEmpty() &&
            !txtFuncMatricula.getText().isEmpty()){
            
            try {
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtFuncMatricula.getText().toUpperCase());
                pst.setString(2, txtFuncNome.getText().toUpperCase());
                pst.setString(3, txtFuncTelefone.getText().toUpperCase());
                if(cboFuncCargo.getSelectedItem().toString().toUpperCase().equals("MOTORISTA")){
                    pst.setString(4, "M");
                }
                else{
                    pst.setString(4, "C");
                }
                //A linha abaixo atualiza a tabela usuario com os dados do formulário
                pst.executeUpdate();
                JOptionPane.showMessageDialog(null, "Funcionário cadastrado com sucesso!");
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
    
    private void alterarFuncionario(){
        if(!txtFuncMatricula.getText().isEmpty() &&
                !txtFuncNome.getText().isEmpty() &&
                !txtFuncTelefone.getText().isEmpty()){
            String sql = "UPDATE FUNCIONARIO SET matriculaFuncionario = ?, nomeFuncionario = ?, telefoneFuncionario = ?, cargoFuncionario = ? WHERE matriculaFuncionario = ?";
            try {
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtFuncMatricula.getText());
                pst.setString(2, txtFuncNome.getText());
                pst.setString(3, txtFuncTelefone.getText());
                if(cboFuncCargo.getSelectedItem().toString().equals("Motorista")){
                    pst.setString(4, "M");
                }
                else{
                    pst.setString(4, "C");
                }
                pst.setString(5, matriculaPesquisada);
                pst.executeUpdate();
                JOptionPane.showMessageDialog(null, "Dados do funcionário atualizados com sucesso!");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
        
    }
    
    private void deletarFuncionario(){
        String sql = "DELETE FROM FUNCIONARIO WHERE matriculaFuncionario = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtFuncMatricula.getText());
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Funcionário deletado com sucesso!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
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

        txtFuncMatricula = new javax.swing.JTextField();
        txtFuncNome = new javax.swing.JTextField();
        btnPesquisaFunc = new javax.swing.JButton();
        btnAlterarFunc = new javax.swing.JButton();
        btnDeletarFunc = new javax.swing.JButton();
        btnCadastraFunc = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        txtFuncTelefone = new javax.swing.JTextField();
        btnLimparFormFunc = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        cboFuncCargo = new javax.swing.JComboBox<>();

        setClosable(true);
        setTitle("Cadastrar Motoristas/Cobradores");
        setMaximumSize(new java.awt.Dimension(588, 400));
        setMinimumSize(new java.awt.Dimension(588, 400));
        setPreferredSize(new java.awt.Dimension(588, 400));

        txtFuncMatricula.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txtFuncMatriculaCaretUpdate(evt);
            }
        });
        txtFuncMatricula.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtFuncMatriculaKeyTyped(evt);
            }
        });

        txtFuncNome.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txtFuncNomeCaretUpdate(evt);
            }
        });
        txtFuncNome.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtFuncNomeKeyTyped(evt);
            }
        });

        btnPesquisaFunc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BDI/Digital/Icones/if_file_search_48764.png"))); // NOI18N
        btnPesquisaFunc.setToolTipText("Pesquisar Funcionário");
        btnPesquisaFunc.setBorderPainted(false);
        btnPesquisaFunc.setContentAreaFilled(false);
        btnPesquisaFunc.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnPesquisaFunc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPesquisaFuncActionPerformed(evt);
            }
        });

        btnAlterarFunc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BDI/Digital/Icones/if_file_edit_48763.png"))); // NOI18N
        btnAlterarFunc.setToolTipText("Salvar alterações no Cadastro do Funcionário");
        btnAlterarFunc.setBorderPainted(false);
        btnAlterarFunc.setContentAreaFilled(false);
        btnAlterarFunc.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAlterarFunc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAlterarFuncActionPerformed(evt);
            }
        });

        btnDeletarFunc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BDI/Digital/Icones/if_file_delete_48762.png"))); // NOI18N
        btnDeletarFunc.setToolTipText("Deletar Funcionário");
        btnDeletarFunc.setBorderPainted(false);
        btnDeletarFunc.setContentAreaFilled(false);
        btnDeletarFunc.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnDeletarFunc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeletarFuncActionPerformed(evt);
            }
        });

        btnCadastraFunc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BDI/Digital/Icones/if_file_add_48761.png"))); // NOI18N
        btnCadastraFunc.setToolTipText("Cadastrar Funcionário");
        btnCadastraFunc.setBorderPainted(false);
        btnCadastraFunc.setContentAreaFilled(false);
        btnCadastraFunc.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCadastraFunc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCadastraFuncActionPerformed(evt);
            }
        });

        jLabel3.setText("Telefone");

        jLabel1.setText("Matrícula");

        txtFuncTelefone.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txtFuncTelefoneCaretUpdate(evt);
            }
        });
        txtFuncTelefone.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtFuncTelefoneKeyTyped(evt);
            }
        });

        btnLimparFormFunc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BDI/Digital/Icones/if_CCleaner_33788.png"))); // NOI18N
        btnLimparFormFunc.setToolTipText("Limpar Formulário");
        btnLimparFormFunc.setBorderPainted(false);
        btnLimparFormFunc.setContentAreaFilled(false);
        btnLimparFormFunc.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLimparFormFunc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimparFormFuncActionPerformed(evt);
            }
        });

        jLabel2.setText("Nome");

        jLabel4.setText("Cargo");

        cboFuncCargo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Motorista", "Cobrador" }));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(142, 142, 142)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel1)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtFuncMatricula, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
                                    .addComponent(txtFuncNome)
                                    .addComponent(txtFuncTelefone)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cboFuncCargo, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(156, 156, 156)
                        .addComponent(btnCadastraFunc, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnPesquisaFunc, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAlterarFunc, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDeletarFunc, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnLimparFormFunc, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(142, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtFuncMatricula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtFuncNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtFuncTelefone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(cboFuncCargo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(btnCadastraFunc)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnAlterarFunc)
                            .addComponent(btnPesquisaFunc)
                            .addComponent(btnDeletarFunc)))
                    .addComponent(btnLimparFormFunc))
                .addContainerGap(151, Short.MAX_VALUE))
        );

        setBounds(0, 0, 588, 400);
    }// </editor-fold>//GEN-END:initComponents

    private void btnPesquisaFuncActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPesquisaFuncActionPerformed
        // TODO add your handling code here:
        consultar();
    }//GEN-LAST:event_btnPesquisaFuncActionPerformed

    private void btnAlterarFuncActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlterarFuncActionPerformed
        // TODO add your handling code here:
        alterarFuncionario();
        limparTela();
    }//GEN-LAST:event_btnAlterarFuncActionPerformed

    private void btnDeletarFuncActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeletarFuncActionPerformed
        // TODO add your handling code here:
        deletarFuncionario();
        limparTela();
    }//GEN-LAST:event_btnDeletarFuncActionPerformed

    private void btnCadastraFuncActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCadastraFuncActionPerformed
        // TODO add your handling code here:
        adicionar();
        limparTela();
    }//GEN-LAST:event_btnCadastraFuncActionPerformed

    private void btnLimparFormFuncActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimparFormFuncActionPerformed
        // TODO add your handling code here:
        limparTela();
    }//GEN-LAST:event_btnLimparFormFuncActionPerformed

    private void txtFuncMatriculaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFuncMatriculaKeyTyped
        // TODO add your handling code here:
        String caracteresAceitos="0987654321";
        
        //Verifica se o caractere digitado é um caractere aceito
        if(!caracteresAceitos.contains(evt.getKeyChar() + "")){
            evt.consume(); //Se não for, consome o evento que inclui esse caractere no campo
        }
        else{ //Se for verifica se atingiu o limite de caracteres do campo
            if(txtFuncMatricula.getText().length() == 10){ //Se atingiu, não permite mais caracteres naquele campo
                JOptionPane.showMessageDialog(null, "Limite máximo de caracteres permitidos no campo Matrícula atingido!");
                evt.consume();
            }
        }
    }//GEN-LAST:event_txtFuncMatriculaKeyTyped

    private void txtFuncMatriculaCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txtFuncMatriculaCaretUpdate

        // TODO add your handling code here:
        if(validaCampos() == true){
            if(txtFuncMatricula.getText().equals(matriculaPesquisada)){
                btnAlterarFunc.setEnabled(true);
            }
            else{
                btnAlterarFunc.setEnabled(false);
            }
           
            btnCadastraFunc.setEnabled(true);
        }
        else{
            btnAlterarFunc.setEnabled(false);
            btnCadastraFunc.setEnabled(false);
        }
        
        if(!txtFuncMatricula.getText().isEmpty()){
            btnPesquisaFunc.setEnabled(true);
        }
        else{
            btnPesquisaFunc.setEnabled(false);
        }
        
    }//GEN-LAST:event_txtFuncMatriculaCaretUpdate

    private void txtFuncNomeCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txtFuncNomeCaretUpdate
        // TODO add your handling code here:
        if(validaCampos() == true){
            if(txtFuncMatricula.getText().equals(matriculaPesquisada)){
                btnAlterarFunc.setEnabled(true);
            }
            else{
                btnAlterarFunc.setEnabled(false);
            }
            
            btnCadastraFunc.setEnabled(true);
        }
        else{
            btnAlterarFunc.setEnabled(false);
            btnCadastraFunc.setEnabled(false);
        }
    }//GEN-LAST:event_txtFuncNomeCaretUpdate

    private void txtFuncTelefoneCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txtFuncTelefoneCaretUpdate
        // TODO add your handling code here:
        if(validaCampos() == true){
            if(txtFuncMatricula.getText().equals(matriculaPesquisada)){
                btnAlterarFunc.setEnabled(true);
            }
            else{
                btnAlterarFunc.setEnabled(false);
            }
            
            btnCadastraFunc.setEnabled(true);
        }
        else{
            btnAlterarFunc.setEnabled(false);
            btnCadastraFunc.setEnabled(false);
        }
    }//GEN-LAST:event_txtFuncTelefoneCaretUpdate

    private void txtFuncNomeKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFuncNomeKeyTyped
        // TODO add your handling code here:
        String caracteresAceitos = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZáãÁÃéÉíÍóõÓÕúÚ ";
        
        if(!caracteresAceitos.contains(evt.getKeyChar() + "")){
            evt.consume();
        }
        else{
            if(txtFuncNome.getText().length() == 80){
                JOptionPane.showMessageDialog(null, "Limite de caracteres permitidos no campo Nome atingido!");
                evt.consume();
            }
        }
    }//GEN-LAST:event_txtFuncNomeKeyTyped

    private void txtFuncTelefoneKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFuncTelefoneKeyTyped
        // TODO add your handling code here:
        String caracteresAceitos="0987654321";
        if(!caracteresAceitos.contains(evt.getKeyChar() + "")){
            evt.consume();
        }
        else{
            if(txtFuncTelefone.getText().length() == 16){
                JOptionPane.showMessageDialog(null, "Limite máximo de caracteres permitidos no campo Telefone atingido!");
                evt.consume();
            }
        }
    }//GEN-LAST:event_txtFuncTelefoneKeyTyped


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAlterarFunc;
    private javax.swing.JButton btnCadastraFunc;
    private javax.swing.JButton btnDeletarFunc;
    private javax.swing.JButton btnLimparFormFunc;
    private javax.swing.JButton btnPesquisaFunc;
    private javax.swing.JComboBox<String> cboFuncCargo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    public static javax.swing.JTextField txtFuncMatricula;
    private javax.swing.JTextField txtFuncNome;
    private javax.swing.JTextField txtFuncTelefone;
    // End of variables declaration//GEN-END:variables
}
