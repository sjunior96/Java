/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BDI.Digital.Telas;

import BDI.Digital.DAL.ModuloConexao;
import com.sun.javafx.fxml.expression.Expression;
import java.awt.Dimension;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Silvio
 */
public class TelaDeletarLinhas extends javax.swing.JInternalFrame {

    /**
     * Creates new form TelaDeletarLinhas
     */
    DefaultTableModel dtmLinhas;
    Connection conexao = null;
    
    public TelaDeletarLinhas() {
        initComponents();
        conexao = ModuloConexao.conector();
        createTable();
    }
    
    public void createTable(){
        dtmLinhas = (DefaultTableModel)tabelaLinhas.getModel();
        dtmLinhas.setNumRows(0);
        tabelaLinhas.getColumnModel().getColumn(0).setMaxWidth(100);
        tabelaLinhas.getColumnModel().getColumn(0).setPreferredWidth(90);
        consultaLinhas();
    }
    
    public void consultaLinhas(){
        String sql = "SELECT * FROM LINHAS";
        PreparedStatement pstConsultaLinhas = null;
        ResultSet rsConsultaLinhas = null;
        
        try {
            pstConsultaLinhas = conexao.prepareStatement(sql);
            rsConsultaLinhas = pstConsultaLinhas.executeQuery();
            
            while(rsConsultaLinhas.next()){
                Object[] dados = {rsConsultaLinhas.getString(1), rsConsultaLinhas.getString(2),
                rsConsultaLinhas.getString(3)};
                dtmLinhas.addRow(dados);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    //O código abaixo deleta o registro do selecionado do banco e atualza a tabela na tela
    public void deletarLinha(){
        //Se existir linha selecionada faça
        if(tabelaLinhas.getSelectedRow() != -1){
            //Variável k receberá o índice da linha selecionada na tabela
            int k = tabelaLinhas.getSelectedRow();
            /*  A variável idLinha recebe o valor lido na linha K, coluna 0, onde a coluna 0
            representa o campo cod_linha da tabela LINHAS do banco de dados*/
            String idLinha = (String) tabelaLinhas.getValueAt(k, 0);
            //Variável "sqlDeletarHorario" armazena o que será consultado no banco de dados
            String sqlDeletarHorario = "DELETE FROM LINHAS WHERE cod_linha = ?";
            PreparedStatement pstDeletarlinha = null;
            
            try {
                //A variável "pstDeletarLinha" vai preparar a consulta com a string "sqlDeletarHorario"
                pstDeletarlinha = conexao.prepareStatement(sqlDeletarHorario);
                //Substitui o "?" da string pela id que se deseja deletar 
                pstDeletarlinha.setString(1, idLinha);
                //Executa a exclusão
                pstDeletarlinha.executeUpdate();
                //Atualiza a tabela na tela, recriando-a
                createTable();
                //Mensagem de sucesso
                JOptionPane.showMessageDialog(null, "Registro deletado com sucesso!");
            } catch (Exception e) {//Exibe a exceção caso não seja possível fazer a consulta do try
                //Verifica se a exceção dada diz respeito à tentativa de deletar "pai" com "filhos" 
                if(e.toString().contains("com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException: Cannot delete or update a parent row: a foreign key constraint fails")){
                    JOptionPane.showMessageDialog(null, "Você não pode deletar este prefixo porque existem horários associados à ele!");
                }
                else{
                    //Exibe a exceção caso ela não contenha a string de erro acima
                    JOptionPane.showMessageDialog(null, e);
                }
            }
            
        }
    }
    
    //O código abaixo centraliza a tela no painel "Pai"
    public void setPosicao(){
        Dimension d = this.getDesktopPane().getSize();//Obtém o tamanho do painel pai
        //A linha abaixo define a posiçao da tela
        this.setLocation((d.width - this.getSize().width) / 2, (d.height - this.getSize().height) / 2); 
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tabelaLinhas = new javax.swing.JTable();
        btnDeletarLinha = new javax.swing.JButton();

        setClosable(true);
        setTitle("Deletar Linhas");

        tabelaLinhas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Codigo Linha", "Prefixo", "Itinerário"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabelaLinhas.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tabelaLinhas);

        btnDeletarLinha.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BDI/Digital/Icones/if_file_delete_48762.png"))); // NOI18N
        btnDeletarLinha.setToolTipText("Deletar Linha");
        btnDeletarLinha.setBorderPainted(false);
        btnDeletarLinha.setContentAreaFilled(false);
        btnDeletarLinha.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnDeletarLinha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeletarLinhaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 820, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnDeletarLinha)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnDeletarLinha)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnDeletarLinhaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeletarLinhaActionPerformed
        // TODO add your handling code here:
        deletarLinha();
    }//GEN-LAST:event_btnDeletarLinhaActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDeletarLinha;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tabelaLinhas;
    // End of variables declaration//GEN-END:variables
}
