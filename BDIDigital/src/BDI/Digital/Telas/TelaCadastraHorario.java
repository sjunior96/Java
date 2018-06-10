package BDI.Digital.Telas;


import BDI.Digital.DAL.ModuloConexao;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.text.MaskFormatter;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Silvio
 */
public class TelaCadastraHorario extends javax.swing.JInternalFrame {

    /**
     * Creates new form TelaCadastraHorario
     */
    
    ResultSet rsDadosPrefixos = null;
    Connection conexao = null;
    
    public TelaCadastraHorario() {
        initComponents();
        conexao = ModuloConexao.conector();
    }
    
    public void setPosicao(){
        Dimension d = this.getDesktopPane().getSize();
        this.setLocation((d.width - this.getSize().width) / 2, (d.height - this.getSize().height) / 2); 
    }
    
    
    public void cadastraHorario(){
        String sqlCadastraHorario = "INSERT INTO HORARIOS (horario) VALUES(?)";
        PreparedStatement pstCadastraHorario = null;
        
        try {
            pstCadastraHorario = conexao.prepareStatement(sqlCadastraHorario);
            pstCadastraHorario.setString(1, txtHorario.getText());
            pstCadastraHorario.executeUpdate();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    public int getHorarioId(){
        String sqlGetHorarioId = "SELECT * FROM HORARIOS WHERE HORARIOS.horario = ?";
        PreparedStatement pstGetHorarioId = null;
        ResultSet rsGetHorarioId = null;
        
        try {
            pstGetHorarioId = conexao.prepareStatement(sqlGetHorarioId);
            pstGetHorarioId.setString(1, txtHorario.getText());
            rsGetHorarioId = pstGetHorarioId.executeQuery();
            if(rsGetHorarioId.next()){
                JOptionPane.showMessageDialog(null, "Existe o horário!\nCodigo do horario: " + rsGetHorarioId.getString(1));
                return Integer.parseInt(rsGetHorarioId.getString(1));
            }
            else{
                return -1;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            return -1;
        }
    }
    
    public int addHorarioInHorarios(){
        String sqlAddHorarioInHorarios = "INSERT INTO HORARIOS(horario) VALUES(?)";
        PreparedStatement pstAddHorarioInHorarios = null;
        ResultSet rsAddHorarioInHorarios = null;
        
        try {
            pstAddHorarioInHorarios = conexao.prepareStatement(sqlAddHorarioInHorarios, Statement.RETURN_GENERATED_KEYS);
            pstAddHorarioInHorarios.setString(1, txtHorario.getText());
            pstAddHorarioInHorarios.executeUpdate();
            rsAddHorarioInHorarios = pstAddHorarioInHorarios.getGeneratedKeys();
            rsAddHorarioInHorarios.next();
            return rsAddHorarioInHorarios.getInt(1);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            return -1;
        }
    }
    
    public void addHorarioInHorariosPorLinhas(){
        String sqlAddHorarioInHorariosPorLinhas = "INSERT INTO HORARIOS_POR_LINHA(cod_horario, cod_linha) VALUES(?,?)";
        PreparedStatement pstAddHorarioInHorariosPorLinhas = null;
        //ResultSet rsAddHorarioInHorariosPorLinhas = null;
        
        try {
            pstAddHorarioInHorariosPorLinhas = conexao.prepareStatement(sqlAddHorarioInHorariosPorLinhas);
            if(getHorarioId() == -1){
                pstAddHorarioInHorariosPorLinhas.setString(1, Integer.toString(addHorarioInHorarios()));
            }
            else{
                if(getHorarioId() != -1){
                    pstAddHorarioInHorariosPorLinhas.setString(1, Integer.toString(getHorarioId()));
                }
            }
            pstAddHorarioInHorariosPorLinhas.setString(2, Integer.toString(consultaItinerario()));
            pstAddHorarioInHorariosPorLinhas.executeUpdate();
            JOptionPane.showMessageDialog(null, "Horário cadastrado com sucesso!");
        } catch (Exception e) {
            if(e.toString().contains("com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException: Duplicate entry ")){
                JOptionPane.showMessageDialog(null, "Este horário já está cadastrado com este prefixo!");
            }
            else{
                JOptionPane.showMessageDialog(null, "Falha na addHorarioInHorariosPorLinhas()\n" + e);
            }
        }
    }
    
    public int consultaItinerario(){
        String sqlConsultaItinerario = "SELECT cod_linha, itinerarioLinha FROM LINHAS WHERE prefixoLinha = ?";
        PreparedStatement pstItinerario = null;
        ResultSet rsItinerario = null;
        
        try {
            pstItinerario = conexao.prepareStatement(sqlConsultaItinerario);
            pstItinerario.setString(1, cboPrefixo.getSelectedItem().toString());
            rsItinerario = pstItinerario.executeQuery();
            if(rsItinerario.next()){
                txtItinerarioPrefixo.setText(rsItinerario.getString(2));
                return Integer.parseInt(rsItinerario.getString(1));
            }else{
                return -1;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            return -1;
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

        jLabel7 = new javax.swing.JLabel();
        txtItinerarioPrefixo = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtHorario = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        cboPrefixo = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setClosable(true);
        setTitle("Cadastrar Horários");

        jLabel7.setText("Prefixo: ");

        txtItinerarioPrefixo.setEditable(false);

        jLabel8.setText("Itinerário:");

        try{
            javax.swing.text.MaskFormatter formatHorario = new javax.swing.text.MaskFormatter("##:##");
            txtHorario = new javax.swing.JFormattedTextField(formatHorario);
        }  catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
        txtHorario.setMaximumSize(new java.awt.Dimension(36, 20));
        txtHorario.setMinimumSize(new java.awt.Dimension(36, 36));
        txtHorario.setPreferredSize(new java.awt.Dimension(36, 20));
        txtHorario.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtHorarioKeyTyped(evt);
            }
        });

        jLabel9.setText("Horário: ");

        cboPrefixo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecione" }));
        cboPrefixo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboPrefixoActionPerformed(evt);
            }
        });

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BDI/Digital/Icones/if_file_add_48761.png"))); // NOI18N
        jButton1.setContentAreaFilled(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BDI/Digital/Icones/if_CCleaner_33788.png"))); // NOI18N
        jButton2.setBorderPainted(false);
        jButton2.setContentAreaFilled(false);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton2))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtItinerarioPrefixo)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtHorario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cboPrefixo, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap(19, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(cboPrefixo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txtItinerarioPrefixo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addComponent(txtHorario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cboPrefixoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboPrefixoActionPerformed
        // TODO add your handling code here:
        consultaItinerario();
    }//GEN-LAST:event_cboPrefixoActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        if(!txtHorario.getText().equals("  :  ") &&
                !cboPrefixo.getSelectedItem().toString().equals("Selecione")){
            
            String hora = txtHorario.getText().substring(0,2);
            String min = txtHorario.getText().substring(3,5);

            if(Integer.parseInt(hora) < 24 && Integer.parseInt(min) < 60){
                addHorarioInHorariosPorLinhas();
            }
            else{
                JOptionPane.showMessageDialog(null, "Hora inválida!");
            }
        }
        else{
            //CASO DE ERRO NO PREENCHIMENTO DO CAMPO HORARIO
            JOptionPane.showMessageDialog(null, "O preenchimento dos campos Prefixo e Horário é obrigatório!", "ERRO!", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        //txtHorario = new javax.swing.JTextField();
        txtHorario.setText(null);
        txtItinerarioPrefixo.setText(null);
        cboPrefixo.setSelectedItem("Selecione");
    }//GEN-LAST:event_jButton2ActionPerformed

    private void txtHorarioKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtHorarioKeyTyped
        // TODO add your handling code here:
        
    }//GEN-LAST:event_txtHorarioKeyTyped


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JComboBox<String> cboPrefixo;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JTextField txtHorario;
    private javax.swing.JTextField txtItinerarioPrefixo;
    // End of variables declaration//GEN-END:variables
}
