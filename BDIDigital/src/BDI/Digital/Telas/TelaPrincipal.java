/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BDI.Digital.Telas;

import BDI.Digital.DAL.ModuloConexao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import BDI.Digital.Telas.TelaCadastraFuncionario;
import BDI.Digital.Telas.TelaCadastraUsuario;
import BDI.Digital.Telas.TelaCadastraBDI;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.font.NumericShaper;
import javax.management.JMException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class TelaPrincipal extends javax.swing.JFrame {

    /**
     * Creates new form TelaPrincipal
     */
    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    TelaCadastraUsuario usuario = null;
    TelaCadastraFuncionario funcionario = null;
    TelaCadastraBDI BDI = null;
    TelaCadastraHorario horarioPrefixo = null;
    TelaDeletarHorarios deletaHorario = null;
    TelaDeletarLinhas deletaLinha = null;
    TelaCadastraLinha cadastraLinha = null;
    TelaSobre telaSobre = null;
    String usuarioLogado = "";
    
    
    public TelaPrincipal() {
        initComponents();
        conexao = ModuloConexao.conector();
    }
    
    
    private void consultaFuncionarios(){
        String sql = "SELECT matriculaFuncionario, nomeFuncionario, cargoFuncionario FROM funcionario";
        try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            while(rs.next()){
                if(rs.getString(3).equals("C")){
                    BDI.cboCobradorBDI.addItem(rs.getString(1));
                }
                else{
                    BDI.cboMotoristaBDI.addItem(rs.getString(1));
                }
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        
    }
    
    private void consultaPrefixos(String telaQueChamou){
        String sql = "SELECT prefixoLinha FROM LINHAS";
        ResultSet rsPrefixosParaHorarios = null;
        PreparedStatement pstPrefixosHorarios = null;
        
        if(telaQueChamou.equals("BDI")){
            try {
                pstPrefixosHorarios = conexao.prepareStatement(sql);
                rsPrefixosParaHorarios = pstPrefixosHorarios.executeQuery();
                while(rsPrefixosParaHorarios.next()){
                    BDI.cboPrefixoBDI.addItem(rsPrefixosParaHorarios.getString(1));
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
        else{
            if(telaQueChamou.equals("CADHORARIO")){
                try {
                    pst = conexao.prepareStatement(sql);
                    rs = pst.executeQuery();
                    while(rs.next()){
                        horarioPrefixo.cboPrefixo.addItem(rs.getString(1));
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e);
                }
            }
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

        ImageIcon icon = new ImageIcon(getClass().getResource("/BDI/Digital/Icones/logo_principal.jpg"));
        Image image = icon.getImage();
        desktopPane = new javax.swing.JDesktopPane(){
            public void paintComponent(Graphics g){
                g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
            }
        };
        jMenuBar1 = new javax.swing.JMenuBar();
        MenCad = new javax.swing.JMenu();
        MenCadUsu = new javax.swing.JMenuItem();
        MenCadFunc = new javax.swing.JMenuItem();
        MenCadBDI = new javax.swing.JMenuItem();
        MenCadHorarios = new javax.swing.JMenuItem();
        MenCadLinha = new javax.swing.JMenuItem();
        MenDeletaHorario = new javax.swing.JMenuItem();
        MenDeletaLinha = new javax.swing.JMenuItem();
        MenSobre = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        MenTrocarUsuario = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Digital B.D.I - Auto Viação Goianésia");
        setMaximumSize(new java.awt.Dimension(968, 600));
        setMinimumSize(new java.awt.Dimension(968, 600));
        setResizable(false);

        javax.swing.GroupLayout desktopPaneLayout = new javax.swing.GroupLayout(desktopPane);
        desktopPane.setLayout(desktopPaneLayout);
        desktopPaneLayout.setHorizontalGroup(
            desktopPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 952, Short.MAX_VALUE)
        );
        desktopPaneLayout.setVerticalGroup(
            desktopPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 541, Short.MAX_VALUE)
        );

        MenCad.setText("Cadastros");

        MenCadUsu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_U, java.awt.event.InputEvent.CTRL_MASK));
        MenCadUsu.setText("Usuários");
        MenCadUsu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenCadUsuActionPerformed(evt);
            }
        });
        MenCad.add(MenCadUsu);

        MenCadFunc.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.event.InputEvent.CTRL_MASK));
        MenCadFunc.setText("Funcionários");
        MenCadFunc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenCadFuncActionPerformed(evt);
            }
        });
        MenCad.add(MenCadFunc);

        MenCadBDI.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_B, java.awt.event.InputEvent.CTRL_MASK));
        MenCadBDI.setText("B.D.I's");
        MenCadBDI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenCadBDIActionPerformed(evt);
            }
        });
        MenCad.add(MenCadBDI);

        MenCadHorarios.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_H, java.awt.event.InputEvent.CTRL_MASK));
        MenCadHorarios.setText("Cadastrar Horários");
        MenCadHorarios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenCadHorariosActionPerformed(evt);
            }
        });
        MenCad.add(MenCadHorarios);

        MenCadLinha.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.CTRL_MASK));
        MenCadLinha.setText("Cadastrar Linhas");
        MenCadLinha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenCadLinhaActionPerformed(evt);
            }
        });
        MenCad.add(MenCadLinha);

        MenDeletaHorario.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_H, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        MenDeletaHorario.setText("Deletar Horários");
        MenDeletaHorario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenDeletaHorarioActionPerformed(evt);
            }
        });
        MenCad.add(MenDeletaHorario);

        MenDeletaLinha.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        MenDeletaLinha.setText("Deletar Linhas");
        MenDeletaLinha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenDeletaLinhaActionPerformed(evt);
            }
        });
        MenCad.add(MenDeletaLinha);

        jMenuBar1.add(MenCad);

        MenSobre.setText("Sobre");
        MenSobre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenSobreActionPerformed(evt);
            }
        });

        jMenuItem1.setText("Sobre");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        MenSobre.add(jMenuItem1);

        jMenuBar1.add(MenSobre);

        MenTrocarUsuario.setText("Trocar Usuário");

        jMenuItem2.setText("Trocar Usuário");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        MenTrocarUsuario.add(jMenuItem2);

        jMenuItem3.setText("Sair");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        MenTrocarUsuario.add(jMenuItem3);

        jMenuBar1.add(MenTrocarUsuario);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(desktopPane)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(desktopPane)
        );

        setBounds(0, 0, 968, 600);
    }// </editor-fold>//GEN-END:initComponents

    private void MenCadBDIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenCadBDIActionPerformed
        // TODO add your handling code here:
        if(BDI == null){ //Se a tela não tiver sido criada ainda
            BDI = new TelaCadastraBDI(); //Cria nova instância da tela
            BDI.setVisible(true); //Deixa tela criada visível
            desktopPane.add(BDI); //Adiciona tela criada ao Pane
            consultaFuncionarios();
            consultaPrefixos("BDI");
            BDI.setPosicao();
            BDI.toFront();
        }
        else{
            if(BDI.isVisible()){ //Se a tela já tiver sido criada
                JOptionPane.showMessageDialog(null, "Já existe uma tela Cadastrar BDI aberta!");
                BDI.pack(); //Restaura tela ao estado de maximizada caso esteja minimizada e não cria outra
                BDI.toFront();
            }
            else{
                BDI = new TelaCadastraBDI(); //Cria nova instância da tela
                desktopPane.add(BDI); //Adiciona tela ao Pane
                BDI.setVisible(true); //Deixa tela visível
                consultaFuncionarios();
                consultaPrefixos("BDI");
                BDI.setPosicao();
                BDI.toFront();
                       
            }
        }
    }//GEN-LAST:event_MenCadBDIActionPerformed

    private void MenCadUsuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenCadUsuActionPerformed
        // TODO add your handling code here:
        if(usuario == null){ //Se a tela não tiver sido criada ainda
            usuario = new TelaCadastraUsuario(); //Cria nova instância da tela
            usuario.setVisible(true); //Deixa tela criada visível
            desktopPane.add(usuario); //Adiciona tela criada ao Pane
            usuario.usuarioLogado = this.usuarioLogado;
            usuario.setPosicao();
            usuario.toFront();
        }
        else{
            
            if(usuario.isVisible()){ //Se a tela já tiver sido criada
                JOptionPane.showMessageDialog(null, "Já existe uma tela Cadastrar Usuário aberta!");
                usuario.pack(); //Restaura ela ao estado de maximizada caso esteja minimizada e não cria outra
                usuario.toFront();
                usuario.usuarioLogado = this.usuarioLogado;
            }
            else{
                usuario = new TelaCadastraUsuario(); //Cria nova instância da tela
                desktopPane.add(usuario); //Adiciona tela ao Pane
                usuario.setVisible(true); //Deixa tela visível
                usuario.usuarioLogado = this.usuarioLogado;
                usuario.setPosicao();
                usuario.toFront();
            }
        }
    }//GEN-LAST:event_MenCadUsuActionPerformed

    private void MenCadFuncActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenCadFuncActionPerformed
        // TODO add your handling code here:
        
        if(funcionario == null){
            funcionario = new TelaCadastraFuncionario(); //Cria nova instância da tela
            funcionario.setVisible(true); //Deixa tela criada visível
            desktopPane.add(funcionario); //Adiciona tela criada ao Pane
            funcionario.setPosicao();
            funcionario.toFront();
        }
        else{
            if(funcionario.isVisible()){ //Se a tela já tiver sido criada
                JOptionPane.showMessageDialog(null, "Já existe uma tela Cadastrar Funcionário aberta!");
                funcionario.pack(); //Restaura tela ao estado de maximizada caso esteja minimizada e não cria outra
                funcionario.toFront();
            }
            else{
                funcionario = new TelaCadastraFuncionario(); //Cria nova instância da tela
                desktopPane.add(funcionario); //Adiciona tela ao Pane
                funcionario.setVisible(true); //Deixa tela visível
                funcionario.setPosicao();
                funcionario.toFront();
            }
        }
    }//GEN-LAST:event_MenCadFuncActionPerformed

    private void MenSobreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenSobreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_MenSobreActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
        if(telaSobre == null){
            telaSobre = new TelaSobre();
            telaSobre.setVisible(true);
            desktopPane.add(telaSobre);
            telaSobre.toFront();
            telaSobre.setPosicao();
        }
        else{
            if(telaSobre.isVisible()){
                JOptionPane.showMessageDialog(null, "Já existe uma tela Sobre aberta!");
                telaSobre.pack();
                telaSobre.toFront();
            }
            else{
                telaSobre = new TelaSobre();
                telaSobre.setVisible(true);
                desktopPane.add(telaSobre);
                telaSobre.setPosicao();
                telaSobre.toFront();
            }
        }
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void MenCadHorariosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenCadHorariosActionPerformed
        // TODO add your handling code here:
        if(horarioPrefixo == null){ //Se a tela não tiver sido criada ainda
            horarioPrefixo = new TelaCadastraHorario(); //Cria nova instância da tela
            horarioPrefixo.setVisible(true); //Deixa tela criada visível
            desktopPane.add(horarioPrefixo); //Adiciona tela criada ao Pane
            consultaPrefixos("CADHORARIO");
            horarioPrefixo.setPosicao();
            horarioPrefixo.toFront();
        }
        else{
            
            if(horarioPrefixo.isVisible()){ //Se a tela já tiver sido criada
                JOptionPane.showMessageDialog(null, "Já existe uma tela Cadastrar Horários aberta!");
                horarioPrefixo.pack(); //Restaura ela ao estado de maximizada caso esteja minimizada e não cria outra
                horarioPrefixo.toFront();
            }
            else{
                horarioPrefixo = new TelaCadastraHorario(); //Cria nova instância da tela
                desktopPane.add(horarioPrefixo); //Adiciona tela ao Pane
                horarioPrefixo.setVisible(true); //Deixa tela visível
                consultaPrefixos("CADHORARIO");
                horarioPrefixo.setPosicao();
                horarioPrefixo.toFront();
            }
        }
        
    }//GEN-LAST:event_MenCadHorariosActionPerformed

    private void MenDeletaHorarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenDeletaHorarioActionPerformed
        // TODO add your handling code here:
        if(deletaHorario == null){ //Se a tela não tiver sido criada ainda
            deletaHorario = new TelaDeletarHorarios(); //Cria nova instância da tela
            deletaHorario.setVisible(true); //Deixa tela criada visível
            desktopPane.add(deletaHorario); //Adiciona tela criada ao Pane
            deletaHorario.setPosicao();
            deletaHorario.toFront();
        }
        else{
            
            if(deletaHorario.isVisible()){ //Se a tela já tiver sido criada
                JOptionPane.showMessageDialog(null, "Já existe uma tela Deletar Horários aberta!");
                deletaHorario.pack(); //Restaura ela ao estado de maximizada caso esteja minimizada e não cria outra
                deletaHorario.toFront();
            }
            else{
                deletaHorario = new TelaDeletarHorarios(); //Cria nova instância da tela
                desktopPane.add(deletaHorario); //Adiciona tela ao Pane
                deletaHorario.setVisible(true); //Deixa tela visível
                deletaHorario.setPosicao();
                deletaHorario.toFront();
            }
        }
    }//GEN-LAST:event_MenDeletaHorarioActionPerformed

    private void MenCadLinhaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenCadLinhaActionPerformed
        // TODO add your handling code here:
        if(cadastraLinha == null){ //Se a tela não tiver sido criada ainda
            cadastraLinha = new TelaCadastraLinha();//Cria nova instância da tela
            cadastraLinha.setVisible(true); //Deixa tela criada visível
            desktopPane.add(cadastraLinha); //Adiciona tela criada ao Pane
            cadastraLinha.setPosicao();
            cadastraLinha.toFront();
        }
        else{
            
            if(cadastraLinha.isVisible()){ //Se a tela já tiver sido criada
                JOptionPane.showMessageDialog(null, "Já existe uma tela Cadastrar Linhas aberta!");
                cadastraLinha.pack(); //Restaura ela ao estado de maximizada caso esteja minimizada e não cria outra
                cadastraLinha.toFront();
            }
            else{
                cadastraLinha = new TelaCadastraLinha(); //Cria nova instância da tela
                desktopPane.add(cadastraLinha); //Adiciona tela ao Pane
                cadastraLinha.setVisible(true); //Deixa tela visível
                cadastraLinha.setPosicao();
                cadastraLinha.toFront();
            }
        }
    }//GEN-LAST:event_MenCadLinhaActionPerformed

    private void MenDeletaLinhaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenDeletaLinhaActionPerformed
        // TODO add your handling code here:
        if(deletaLinha == null){ //Se a tela não tiver sido criada ainda
            deletaLinha = new TelaDeletarLinhas(); //Cria nova instância da tela
            deletaLinha.setVisible(true); //Deixa tela criada visível
            desktopPane.add(deletaLinha); //Adiciona tela criada ao Pane
            deletaLinha.setPosicao();
            deletaLinha.setTitle("Deletar Linhas");
            deletaLinha.toFront();
        }
        else{
            
            if(deletaLinha.isVisible()){ //Se a tela já tiver sido criada
                JOptionPane.showMessageDialog(null, "Já existe uma tela Deletar Linhas aberta!");
                deletaLinha.pack(); //Restaura ela ao estado de maximizada caso esteja minimizada e não cria outra
                deletaLinha.toFront();
            }
            else{
                deletaLinha = new TelaDeletarLinhas(); //Cria nova instância da tela
                desktopPane.add(deletaLinha); //Adiciona tela ao Pane
                deletaLinha.setVisible(true); //Deixa tela visível
                deletaLinha.setTitle("Deletar Linhas");
                deletaLinha.setPosicao();
                deletaLinha.toFront();
            }
        }
        
    }//GEN-LAST:event_MenDeletaLinhaActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        // TODO add your handling code here:
        new Login().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TelaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TelaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TelaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TelaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TelaPrincipal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu MenCad;
    private javax.swing.JMenuItem MenCadBDI;
    private javax.swing.JMenuItem MenCadFunc;
    private javax.swing.JMenuItem MenCadHorarios;
    private javax.swing.JMenuItem MenCadLinha;
    private javax.swing.JMenuItem MenCadUsu;
    private javax.swing.JMenuItem MenDeletaHorario;
    private javax.swing.JMenuItem MenDeletaLinha;
    private javax.swing.JMenu MenSobre;
    private javax.swing.JMenu MenTrocarUsuario;
    private javax.swing.JDesktopPane desktopPane;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    // End of variables declaration//GEN-END:variables
}