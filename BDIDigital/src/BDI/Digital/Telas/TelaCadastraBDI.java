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
import java.sql.Statement;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JOptionPane;

/**
 *
 * @author Silvio
 */
public class TelaCadastraBDI extends javax.swing.JInternalFrame {
    
    //Variáveis/Objetos globais
    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    Double totalBDI = 0.00;
    Double valorD1 = 0.00;
    Double valorD9 = 0.00;
    Double valorFrete = 0.00;
    String BDIPesquisado;
    ArrayList<String> codigosBuscados = new ArrayList<>();
    int contadorCamposPreenchidos = 0;
    String numBDIBuscado = "";
    
    String formato = "#,##0.00";
    DecimalFormat formatadorDecimal = new DecimalFormat(formato);
    
    public TelaCadastraBDI() {
        initComponents();
        //Inicia conexao com o banco
        conexao = ModuloConexao.conector();
        //Desativa todos os botões ao iniciar a tela
        btnPesquisarBDI.setEnabled(false);
        btnSalvarBDI.setEnabled(false);
        btnAlterarBDI.setEnabled(false);
        btnDeletarBDI.setEnabled(false);
        
    }
    
    private boolean verificaIniciantes(String tabela, String inicianteConsultado){
        PreparedStatement pstVerificaIniciantes = null;
        ResultSet rsVerificaIniciantes = null;
        String sqlVerificaIniciantes = "";
        
        if(tabela.equals("FRETE")){
            sqlVerificaIniciantes = "SELECT * FROM FRETE WHERE inicianteFrete = " + inicianteConsultado;
        }
        else{
            if(tabela.equals("SERIED1")){
                sqlVerificaIniciantes = "SELECT * FROM SERIED1 WHERE iniciantePassagem = " + inicianteConsultado;
            }
            else{
                if(tabela.equals("SERIED9")){
                    sqlVerificaIniciantes = "SELECT * FROM SERIED9 WHERE iniciantePassagem = " + inicianteConsultado;
                }
            }
        }
        
        try {
            pstVerificaIniciantes = conexao.prepareStatement(sqlVerificaIniciantes);
            rsVerificaIniciantes = pstVerificaIniciantes.executeQuery();
            if(rsVerificaIniciantes.next()){
                JOptionPane.showMessageDialog(null, "Iniciante repetido na tabela " + tabela + " : " + rsVerificaIniciantes.getString(2));
                return true;
            }
            else{
                return false;
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            return false;
        }
    }
    
    //Função centraliza a tela dentro do componente Pai
    public void setPosicao(){
        Dimension d = this.getDesktopPane().getSize();
        this.setLocation((d.width - this.getSize().width) / 2, (d.height - this.getSize().height) / 2); 
    }
    
    //Limpa todos os campos e bloqueia os botões para iniciar um novo preenchimento/consulta
    private void limparTela(){
        btnPesquisarBDI.setEnabled(false);
        txtEncerranteD1.setText(null);
        txtEncerranteD9.setText(null);
        txtEncerranteFrete.setText(null);
        txtInicianteD1.setText(null);
        txtInicianteD9.setText(null);
        txtInicianteFrete.setText(null);
        txtItinerarioBDI.setText(null);
        txtNomeCobradorBDI.setText(null);
        txtNomeMotoristaBDI.setText(null);
        txtNumBDI.setText(null);
        txtValorD1.setText(null);
        txtValorD9.setText(null);
        txtValorFrete.setText(null);
        cboCobradorBDI.setSelectedItem("Selecione");
        cboHorarioBDI.setSelectedItem("Selecione");
        cboMotoristaBDI.setSelectedItem("Selecione");
        cboPrefixoBDI.setSelectedItem("Selecione");
        valorD1 = 0.00;
        valorD9 = 0.00;
        valorFrete = 0.00;
        btnAlterarBDI.setEnabled(false);
        btnDeletarBDI.setEnabled(false);
        BDIPesquisado = "nulo";

        //Mostra o valor total acumulado pelos campos valores do BDI na tela
        lblTotalBDI.setText(String.valueOf(formatadorDecimal.format(valorD1+valorD9+valorFrete)).replace('.', ','));
    }
    
    /*  Função para deletar registros das tabelas SERIED1, SERIED9 e FRETE
        Sempre que um BDI for deletado, essa função será acessada para deletar os registros */
    private void deletaRegistros(String tabela, String codigoRegistro){
        String sql = "";
        //Cria o comando delete com base na tabela e no campo passados como parâmetros
        if(tabela.equals("SERIED1")){
            sql = "DELETE FROM SERIED1 WHERE cod_registro = ?";
        }
        else{
            if(tabela.equals("SERIED9")){
                sql = "DELETE FROM SERIED9 WHERE cod_registro = ?";
            }
            else{
                if(tabela.equals("FRETE")){
                    sql = "DELETE FROM FRETE WHERE codFrete = ?";
                }
            }
        }
        
        //Após definido o comando SQL, prepara execução do comando no banco
        try {
            //Prepara o comando contido na variavel sql, que possui 1 Statement
            pst = conexao.prepareStatement(sql);
            //Define o Statement
            pst.setString(1, codigoRegistro);
            //Executa a operação no banco
            pst.executeUpdate();
            //JOptionPane.showMessageDialog(null, "Registro " + codigoRegistro + " da tabela " + tabela + " excluído com sucesso!");
        } catch (Exception e) {
            //Exibe mensagem de erro acompanhada da exceção lançada
            JOptionPane.showMessageDialog(null, "Falha na função deletarRegistros()\n" + e);
        }
    }
    
    //Função para deletar um BDI do sistema
    private void deletarBDI(){
        //Comando SQL a ser usado com 1 Statement
        String sql = "DELETE FROM BDI WHERE codBDI = ?";
            try {
                //Prepara o comando 
                pst = conexao.prepareStatement(sql);
                //Define o Statement 1
                pst.setString(1, txtNumBDI.getText());
                //Executa o comando no banco deletando o BDI desejado
                pst.executeUpdate();
                /*  Chama a função de deletar os registros dos talões, para cada um deles
                    OBS: codigosBuscados é abastecido com os códigos dos registros de cada talão do BDI
                    pesquisado para ser excluido  */
                deletaRegistros("SERIED1", codigosBuscados.get(0));
                deletaRegistros("SERIED9", codigosBuscados.get(1));
                deletaRegistros("FRETE", codigosBuscados.get(2));
                //Mensagem de sucesso
                JOptionPane.showMessageDialog(null, "BDI deletado com sucesso!");
                //Limpa a tela para nova operação
                limparTela();
            } catch (Exception e) {
                //Exibe exceção caso a operação falhe
                JOptionPane.showMessageDialog(null, e);
            }
    }
    
    //Função para alterar os dados do talão D1, quando o BDI for atualizado
    private void alterarTaloesD1(){
        //Comando SQL
        String sql = "UPDATE SERIED1 SET iniciantePassagem = ?, encerrantePassagem = ?, valorRegistro = ? WHERE cod_registro = ?";
        try {
            //Prepara comando
            pst = conexao.prepareStatement(sql);
            //As próximas 4 linhas definem quais valores serão passados para cada Statement(?)
            pst.setString(1, txtInicianteD1.getText());
            pst.setString(2, txtEncerranteD1.getText());
            pst.setString(3, txtValorD1.getText());
            pst.setString(4, codigosBuscados.get(0));
            //Executa comando no banco
            pst.executeUpdate();
            //JOptionPane.showMessageDialog(null, "Tabela de Talões D1 atualizada com sucesso!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    //Comando para atualizar os dados do talão D9
    private void alterarTaloesD9(){
        //Comando SQL
        String sql = "UPDATE SERIED9 SET iniciantePassagem = ?, encerrantePassagem = ?, valorRegistro = ? WHERE cod_registro = ?";
        try {
            //Prepara o comando 
            pst = conexao.prepareStatement(sql);
            //As 4 próximas linhas definem quais valores serão passados para cada Statement(?)
            pst.setString(1, txtInicianteD9.getText());
            pst.setString(2, txtEncerranteD9.getText());
            pst.setString(3, txtValorD9.getText());
            pst.setString(4, codigosBuscados.get(1));
            //Executa comando no banco
            pst.executeUpdate();
            //JOptionPane.showMessageDialog(null, "Tabela de Talões D9 atualizada com sucesso!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    //Função para atualizar os dados da tabela de Fretes quando o BDI for atualizado
    private void alterarFretes(){
        //Comando SQL
        String sql = "UPDATE FRETE SET inicianteFrete = ?, encerranteFrete = ?, valorFrete = ? WHERE codFrete = ?";
        try {
            //Prepara comando 
            pst = conexao.prepareStatement(sql);
            //As 4 próximas linhas definem quais valores serão passados para cada Statement(?)
            pst.setString(1, txtInicianteFrete.getText());
            pst.setString(2, txtEncerranteFrete.getText());
            pst.setString(3, txtValorFrete.getText());
            pst.setString(4, codigosBuscados.get(2));
            //Executa comando no banco
            pst.executeUpdate();
            //JOptionPane.showMessageDialog(null, "Tabela de Fretes atualizada com sucesso!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    //Função para alterar o BDI quando ele for atualizado
    private void alteraBDI(){
        //Comando SQL de atualização
        String sqlAlteraBDI = "UPDATE BDI SET codBDI = ?, dataBDI = ?, cod_horario = ?, cod_linha = ?, matriculaCobradorBDI = ?, "
                + "matriculaMotoristaBDI = ? WHERE codBDI = ?";
        PreparedStatement pstAlteraBDI = null;
                
        try {
            //formatador para a data que vem do calendário (tipo Date)
            DateFormat formatador = new SimpleDateFormat("dd-MM-yyyy");
            //Prepara o comando
            pstAlteraBDI = conexao.prepareStatement(sqlAlteraBDI);
            //As próximas 7 linhas definem os valores que serão passados para cada Statement(?)
            pstAlteraBDI.setString(1, txtNumBDI.getText());
            pstAlteraBDI.setString(2, formatador.format(calendarioBDI.getDate()));
            pstAlteraBDI.setString(3, getCodigoHorario());
            pstAlteraBDI.setString(4, getCodigoLinha());
            pstAlteraBDI.setString(5, cboCobradorBDI.getSelectedItem().toString());
            pstAlteraBDI.setString(6, cboMotoristaBDI.getSelectedItem().toString());
            pstAlteraBDI.setString(7, numBDIBuscado);
            //Executa comando no banco
            pstAlteraBDI.executeUpdate();
            //Limpa a tela para nova operação
            limparTela();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    /*  Função retorna true se todos os campos estiverem devidamente preenchidos e false se
    qualquer campo estiver sem preenchimento    */
    public boolean validaCampos(){
        
        if(!txtInicianteD1.getText().isEmpty() &&
                !txtEncerranteD1.getText().isEmpty() &&
                !txtValorD1.getText().isEmpty() &&
                !txtInicianteD9.getText().isEmpty() &&
                !txtEncerranteD9.getText().isEmpty() &&
                !txtValorD9.getText().isEmpty() &&
                !txtInicianteFrete.getText().isEmpty() &&
                !txtEncerranteFrete.getText().isEmpty() &&
                !txtValorFrete.getText().isEmpty() &&
                (calendarioBDI.getDate().before(new Date()) == true) &&
                !txtNumBDI.getText().isEmpty() &&
                !cboCobradorBDI.getSelectedItem().toString().equals("Selecione") &&
                !cboMotoristaBDI.getSelectedItem().toString().equals("Selecione") &&
                !cboPrefixoBDI.getSelectedItem().toString().equals("Selecione") &&
                !cboHorarioBDI.getSelectedItem().toString().equals("Vazio") &&
                !cboHorarioBDI.getSelectedItem().toString().isEmpty()){
            
            return true;
        }
        else{
            return false;
        }
    }
    
    public int cadastraFrete(){
        ResultSet rsFretes;
        //Comando SQL
        String sql = "INSERT INTO FRETE(inicianteFrete, encerranteFrete,"
                                + " valorFrete) VALUES(?,?,?)";
        try {
            //Prepara o comando, pedindo como retorno a KEY (PK) gerada pela inserção dos dados
            pst = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            //As próximas 3 linhas definem os valores que serão passados para cada Statement(?)
            pst.setString(1, txtInicianteFrete.getText());
            pst.setString(2, txtEncerranteFrete.getText());
            pst.setString(3, txtValorFrete.getText());
            //Executa o comando no banco
            pst.executeUpdate();
            //A linha abaixo atribui ao ResulSet rsFretes a KEY (PK) retornada pela execução acima
            rsFretes = pst.getGeneratedKeys();
            rsFretes.next();
            //JOptionPane.showMessageDialog(null, "Os dados foram cadastrados com sucesso na tabela de Fretes!");
            //Salva a KEY obtida na ArrayList codigosBuscados
            codigosBuscados.add(Integer.toString(rsFretes.getInt(1)));
            
            //Retorna a KEY gerada para quem chamou a função
            return rsFretes.getInt(1);
        } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
        }
        //Se algo der errado no processo acima, retorna -1 como sinal de erro
        return -1;
    }
    
    public int cadastraTalaoD1(){
        //Comando SQL de inserção
        String sql = "INSERT INTO SERIED1(iniciantePassagem, encerrantePassagem,"
                                + " valorRegistro) VALUES(?,?,?)";
        PreparedStatement pstCadastraTalaoD1 = null;
        ResultSet rsCadastraTalaoD1 = null;
        
        try {
            pstCadastraTalaoD1 = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstCadastraTalaoD1.setString(1, txtInicianteD1.getText());
            pstCadastraTalaoD1.setString(2, txtEncerranteD1.getText());
            pstCadastraTalaoD1.setString(3, txtValorD1.getText());
            pstCadastraTalaoD1.executeUpdate();
            rsCadastraTalaoD1 = pstCadastraTalaoD1.getGeneratedKeys();
            rsCadastraTalaoD1.next();
            //JOptionPane.showMessageDialog(null, "Os dados foram cadastrados com sucesso na tabelas de Talões D1!");
            //Armazena a chave gerada no Arraylist codigosBuscados
            codigosBuscados.add(Integer.toString(rsCadastraTalaoD1.getInt(1)));
            //Retorna a chave gerada pela inserção
            return rsCadastraTalaoD1.getInt(1);
        } catch (Exception e) {
            //Exibe a exceção caso o comando SQL apresente alguma
            JOptionPane.showMessageDialog(null, e);
        }
        //Se algo der errado, retorna -1 como sinal de erro
        return -1;
    }
    
    public int cadastraTalaoD9(){
        String sql = "INSERT INTO SERIED9(iniciantePassagem, encerrantePassagem,"
                                + " valorRegistro) VALUES(?,?,?)";
        try {
            pst = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, txtInicianteD9.getText());
            pst.setString(2, txtEncerranteD9.getText());
            pst.setString(3, txtValorD9.getText());
            pst.executeUpdate();
            rs = pst.getGeneratedKeys();
            rs.next();
            codigosBuscados.add(Integer.toString(rs.getInt(1)));
            return rs.getInt(1);
        } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Falha no Cadastra Talao D9" + e);
        }
        return -1;
    }
    
    public String getCodigoHorario(){
        String sqlGetCodigoHorario = "SELECT HORARIOS.cod_horario FROM HORARIOS WHERE horario = ?";
        PreparedStatement pstGetCodigoHorario = null;
        ResultSet rsGetCodigoHorario = null;
        
        try {
            pstGetCodigoHorario = conexao.prepareStatement(sqlGetCodigoHorario);
            pstGetCodigoHorario.setString(1, cboHorarioBDI.getSelectedItem().toString());
            rsGetCodigoHorario = pstGetCodigoHorario.executeQuery();
            if(rsGetCodigoHorario.next()){
                return rsGetCodigoHorario.getString(1);
            }
            else{
                return "failed";
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro na getCodigoHorario\n" + e);
        }
        
        return "failed";
    }
    
    //Função para obter o codigo da linha
    public String getCodigoLinha(){
        String sqlGetCodigoLinha = "SELECT LINHAS.cod_linha FROM LINHAS WHERE prefixoLinha = ?";
        PreparedStatement pstGetCodigoLinha = null;
        ResultSet rsGetCodigoLinha = null;
        
        try {
            pstGetCodigoLinha = conexao.prepareStatement(sqlGetCodigoLinha);
            pstGetCodigoLinha.setString(1, cboPrefixoBDI.getSelectedItem().toString());
            rsGetCodigoLinha = pstGetCodigoLinha.executeQuery();
            if(rsGetCodigoLinha.next()){
                return rsGetCodigoLinha.getString(1);
            }
            else{
                return "failed";
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro na getCodigoLinha\n" + e);
        }
        
        return "failed";
    }
    
    
    public void cadastraBDI(){
        String sqlBDI = "INSERT INTO BDI(codBDI, dataBDI, cod_horario, "
                + "cod_linha, matriculaCobradorBDI, matriculaMotoristaBDI, codRegistroD1,"
                + "codRegistroD9, codFrete) VALUES(?,?,?,?,?,?,?,?,?)";
        PreparedStatement pstCadastraBDI = null;
        
        
        try {
            DateFormat formatador = new SimpleDateFormat("dd-MM-yyyy");
            pstCadastraBDI = conexao.prepareStatement(sqlBDI);
            pstCadastraBDI.setString(1, txtNumBDI.getText());
            pstCadastraBDI.setString(2, formatador.format(calendarioBDI.getDate()));
            pstCadastraBDI.setString(3, getCodigoLinha());
            pstCadastraBDI.setString(4, getCodigoLinha());
            pstCadastraBDI.setString(5, cboCobradorBDI.getSelectedItem().toString());
            pstCadastraBDI.setString(6, cboMotoristaBDI.getSelectedItem().toString());
            pstCadastraBDI.setString(7, Integer.toString(cadastraTalaoD1()));
            pstCadastraBDI.setString(8, Integer.toString(cadastraTalaoD9()));
            pstCadastraBDI.setString(9, Integer.toString(cadastraFrete()));
            pstCadastraBDI.executeUpdate();
            JOptionPane.showMessageDialog(null, "BDI cadastrado com sucesso!");
            limparTela();
        } catch (Exception e) {
            if(!e.toString().contains("com.mysql.jdbc.MysqlDataTruncation: Data truncation: Out of range value for column ")){
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }
    
    //Função que consulta os horários da linha selecionada e os carrega na combo de Horários
    public void consultaItinerario(){ 
        ResultSet rsItinerario;
        //Remove todos os itens da combo de Horários
        cboHorarioBDI.removeAllItems();
        String sql = "SELECT HORARIOS.horario, LINHAS.itinerarioLinha FROM HORARIOS_POR_LINHA\n" +
                "INNER JOIN LINHAS ON LINHAS.cod_linha = HORARIOS_POR_LINHA.cod_linha\n" +
                "INNER JOIN HORARIOS ON HORARIOS.cod_horario = HORARIOS_POR_LINHA.cod_horario\n" +
                "WHERE LINHAS.cod_linha = (SELECT cod_linha FROM LINHAS WHERE prefixoLinha = ?);\n";
        if(!cboPrefixoBDI.getSelectedItem().toString().equals("Selecione")){
            try {
                //Prepara o comando SQL
                pst = conexao.prepareStatement(sql);
                //Define o que será passado para o Statement 1 (?)
                pst.setString(1, cboPrefixoBDI.getSelectedItem().toString());
                //Executa o comando
                rsItinerario = pst.executeQuery();
                if(rsItinerario.next()){
                    //Adiciona o primeiro item lido na combo de Horários
                    cboHorarioBDI.addItem(rsItinerario.getString(1));
                    //Adiciona o itinerário no campo 
                    txtItinerarioBDI.setText(rsItinerario.getString(2));
                    //Enquanto houver resultados da consulta
                    while(rsItinerario.next()){
                        //Adiciona cada horário lido na combo de Horários
                        cboHorarioBDI.addItem(rsItinerario.getString(1));
                        /*  Adiciona o itinerário que será o mesmo independente de quantos resultados
                            Sejam encontrados no campo de itinerário    */
                        txtItinerarioBDI.setText(rsItinerario.getString(2));
                    }
                }
                else{
                    //Caso o prefixo selecionado não possua horários cadastrados
                    cboHorarioBDI.addItem("Vazio");
                    JOptionPane.showMessageDialog(null, "Não existem horários cadastrados para esta Linha!");
                }
            } catch (Exception e) {
                //Caso dê alguma exceção na execução do comando SQL, exibe-a
                JOptionPane.showMessageDialog(null, "Falhou aqui na consulta itinerario\n" + e);
            }
        }
    }
    
    private void consultaHorarios(){
        rs = null;
        cboHorarioBDI.removeAllItems();
        cboHorarioBDI.addItem("Selecione");
        String sql = "SELECT horario FROM HORARIOS WHERE prefixoLinha = ?";
        if(!cboPrefixoBDI.getSelectedItem().toString().equals("Selecione")){
            try {
                pst = conexao.prepareStatement(sql);
                pst.setString(1, cboPrefixoBDI.getSelectedItem().toString());
                rs = pst.executeQuery();
                while(rs.next()){
                    cboHorarioBDI.addItem(rs.getString(1));
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }
    
    private void consultaFuncionarios(String comboBox){
        String sql = "SELECT nomeFuncionario, cargoFuncionario FROM funcionario WHERE matriculaFuncionario = ?";
        try {
            pst = conexao.prepareStatement(sql);
            if(comboBox.equals("cobrador")){
                pst.setString(1, cboCobradorBDI.getSelectedItem().toString());
            }
            else{
                if(comboBox.equals("motorista")){
                    pst.setString(1, cboMotoristaBDI.getSelectedItem().toString());
                }
            }
            rs = pst.executeQuery();
            if(rs.next()){
                if(rs.getString(2).equals("C")){
                    txtNomeCobradorBDI.setText(rs.getString(1));
                }
                else{
                    if(rs.getString(2).equals("M")){
                        txtNomeMotoristaBDI.setText(rs.getString(1));
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    private void consultaBDI(){
        ResultSet rs2;
        PreparedStatement pstConsultaBDI = null;
        
        String sqlConsultaBDI = "SELECT BDI.codBDI NºBDI, BDI.dataBDI DIA,HORARIOS.horario HORARIO,\n" +
"LINHAS.prefixoLinha PREFIXO, LINHAS.itinerarioLinha ITINERARIO,\n" +
"BDI.matriculaCobradorBDI MATCOBRADOR, F.nomeFuncionario NOMECOBRADOR,\n" +
"BDI.matriculaMotoristaBDI MATMOTORISTA, FUNCIONARIO.nomeFuncionario NOMEMOTORISTA,\n" +
"BDI.codRegistroD1 CD1, SERIED1.iniciantePassagem INICIOD1, SERIED1.encerrantePassagem FIMD1, SERIED1.valorRegistro VALORD1,\n" +
"BDI.codRegistroD9 CD9, SERIED9.iniciantePassagem INICIOD9, SERIED9.encerrantePassagem FIMD9, SERIED9.valorRegistro VALORD9,\n" +
"BDI.codFrete CDFRETE, FRETE.inicianteFrete INICIOFRETE, FRETE.encerranteFrete FIMFRETE, FRETE.valorFrete VALORFRETE FROM BDI\n" +
"INNER JOIN HORARIOS ON HORARIOS.cod_horario = BDI.cod_horario\n" +
"inner join horarios_por_linha\n" +
"INNER JOIN LINHAS ON LINHAS.cod_linha = HORARIOS_POR_LINHA.cod_linha\n" +
"INNER JOIN FUNCIONARIO F ON F.matriculaFuncionario = BDI.matriculaCobradorBDI\n" +
"INNER JOIN FUNCIONARIO ON FUNCIONARIO.matriculaFuncionario = BDI.matriculaMotoristaBDI\n" +
"INNER JOIN SERIED1 ON SERIED1.cod_registro = BDI.codRegistroD1\n" +
"INNER JOIN SERIED9 ON SERIED9.cod_registro = BDI.codRegistroD9\n" +
"INNER JOIN FRETE ON FRETE.codFrete = BDI.codFrete\n" +
"WHERE BDI.codBDI = ? and horarios_por_linha.cod_linha = BDI.cod_linha\n" +
"and horarios_por_linha.cod_horario = HORARIOS.cod_horario";
        
        try {
            pstConsultaBDI = conexao.prepareStatement(sqlConsultaBDI);
            pstConsultaBDI.setString(1, txtNumBDI.getText());
            rs2 = pstConsultaBDI.executeQuery();
            if(rs2.next()){
                SimpleDateFormat formatadorData = new SimpleDateFormat("dd-MM-yyyy");
                calendarioBDI.setDate(formatadorData.parse(rs2.getString(2)));
                numBDIBuscado = txtNumBDI.getText();
                System.out.println(rs2.getString(3));
                cboPrefixoBDI.setSelectedItem(rs2.getString(4));
                cboHorarioBDI.setSelectedItem(rs2.getString(3));
                System.out.println(rs2.getString(4));
                txtItinerarioBDI.setText(rs2.getString(5));
                cboCobradorBDI.setSelectedItem(rs2.getString(6));
                txtNomeCobradorBDI.setText(rs2.getString(7));
                cboMotoristaBDI.setSelectedItem(rs2.getString(8));
                txtNomeMotoristaBDI.setText(rs2.getString(9));
                txtInicianteD1.setText(rs2.getString(11));
                txtEncerranteD1.setText(rs2.getString(12));
                txtValorD1.setText(rs2.getString(13));
                txtInicianteD9.setText(rs2.getString(15));
                txtEncerranteD9.setText(rs2.getString(16));
                txtValorD9.setText(rs2.getString(17));
                txtInicianteFrete.setText(rs2.getString(19));
                txtEncerranteFrete.setText(rs2.getString(20));
                txtValorFrete.setText(rs2.getString(21));
                
                
                //Salvo os códigos buscados dos talões e fretes
                codigosBuscados.clear();
                codigosBuscados.add(rs2.getString(10));
                codigosBuscados.add(rs2.getString(14));
                codigosBuscados.add(rs2.getString(18));
                //Salvo os valores dos codigos do horario e da linha buscados
                codigosBuscados.add(getCodigoHorario());
                codigosBuscados.add(getCodigoLinha());
                
                /*  Atualiza o valor mostrado no Total BDI com os valores lidos somados e formatados com vírgula
                no lugar do ponto   */
                valorD1 = Double.parseDouble(rs2.getString(13));
                valorD9 = Double.parseDouble(rs2.getString(17));
                valorFrete = Double.parseDouble(rs2.getString(21));
                totalBDI = valorD1 + valorD9 + valorFrete;
                lblTotalBDI.setText(formatadorDecimal.format(totalBDI).toString());
                btnDeletarBDI.setEnabled(true);
                btnAlterarBDI.setEnabled(true);
                BDIPesquisado = txtNumBDI.getText();
            }
            else{
                JOptionPane.showMessageDialog(null, "BDI não cadastrado!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e + "\nFalhou aqui!");
        }
    }
    
    private void confirmaDecisao(String mensagemParaUsuario){
        if(JOptionPane.showConfirmDialog(null, mensagemParaUsuario, "Atenção", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == 0){
            JOptionPane.showMessageDialog(null, "Cadastro atualizado com sucesso");
        }
        else{
            JOptionPane.showMessageDialog(null, "Atualização cancelada!");
        }
    }
    
    private boolean checaCodBDI(){
        String sql = "SELECT * FROM BDI WHERE codBDI = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtNumBDI.getText());
            rs = pst.executeQuery();
            if(rs.next()){
                return true;
            }
            else{
                return false;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            return true;
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

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtNomeMotoristaBDI = new javax.swing.JTextField();
        txtItinerarioBDI = new javax.swing.JTextField();
        cboHorarioBDI = new javax.swing.JComboBox<>();
        jLabel20 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        txtNomeCobradorBDI = new javax.swing.JTextField();
        cboPrefixoBDI = new javax.swing.JComboBox<>();
        cboMotoristaBDI = new javax.swing.JComboBox<>();
        cboCobradorBDI = new javax.swing.JComboBox<>();
        calendarioBDI = new com.toedter.calendar.JCalendar();
        txtNumBDI = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        txtInicianteD1 = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        txtEncerranteD1 = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        txtInicianteD9 = new javax.swing.JTextField();
        txtEncerranteD9 = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        txtValorD1 = new javax.swing.JTextField();
        txtValorD9 = new javax.swing.JTextField();
        txtInicianteFrete = new javax.swing.JTextField();
        txtEncerranteFrete = new javax.swing.JTextField();
        txtValorFrete = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        lblTotalBDI = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        btnLimparFormBDI = new javax.swing.JButton();
        btnPesquisarBDI = new javax.swing.JButton();
        btnAlterarBDI = new javax.swing.JButton();
        btnDeletarBDI = new javax.swing.JButton();
        btnSalvarBDI = new javax.swing.JButton();

        setClosable(true);
        setTitle("Cadastrar B.D.I");
        setMaximumSize(new java.awt.Dimension(836, 497));
        setMinimumSize(new java.awt.Dimension(836, 497));
        setPreferredSize(new java.awt.Dimension(836, 497));

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel1.setPreferredSize(new java.awt.Dimension(940, 220));

        jLabel1.setText("Viação Goianésia Ltda.");

        jLabel2.setText("Administração Total de Transportes");

        jLabel3.setText("Infobus Comércio e Serviços Ltda.");

        txtNomeMotoristaBDI.setEditable(false);

        txtItinerarioBDI.setEditable(false);

        cboHorarioBDI.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecione" }));
        cboHorarioBDI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboHorarioBDIActionPerformed(evt);
            }
        });

        jLabel20.setText("Nº BDI");

        jLabel6.setText("Cobrador");

        jLabel7.setText("Motorista");

        jLabel8.setText("Linha");

        jLabel19.setText("Horário:");

        txtNomeCobradorBDI.setEditable(false);

        cboPrefixoBDI.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecione" }));
        cboPrefixoBDI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboPrefixoBDIActionPerformed(evt);
            }
        });

        cboMotoristaBDI.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecione" }));
        cboMotoristaBDI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboMotoristaBDIActionPerformed(evt);
            }
        });

        cboCobradorBDI.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecione" }));
        cboCobradorBDI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboCobradorBDIActionPerformed(evt);
            }
        });

        txtNumBDI.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txtNumBDICaretUpdate(evt);
            }
        });
        txtNumBDI.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtNumBDIFocusGained(evt);
            }
        });
        txtNumBDI.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNumBDIKeyTyped(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel12.setText("*Nº deve ser digitado EXATAMENTE IGUAL no documento.");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cboCobradorBDI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cboMotoristaBDI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addGap(24, 24, 24)
                                .addComponent(cboPrefixoBDI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtNumBDI, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtNomeCobradorBDI, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addComponent(txtItinerarioBDI, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cboHorarioBDI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(txtNomeMotoristaBDI)
                            .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(calendarioBDI, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(118, 118, 118)
                        .addComponent(jLabel15))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel20)
                            .addComponent(txtNumBDI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel15)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel8)
                                    .addComponent(cboPrefixoBDI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel19)
                                    .addComponent(cboHorarioBDI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtItinerarioBDI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel6)
                                    .addComponent(cboCobradorBDI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtNomeCobradorBDI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel7)
                                    .addComponent(cboMotoristaBDI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtNomeMotoristaBDI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addComponent(calendarioBDI, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 37, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Bilhetes", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 18))); // NOI18N
        jPanel2.setMaximumSize(new java.awt.Dimension(928, 207));
        jPanel2.setMinimumSize(new java.awt.Dimension(928, 207));

        txtInicianteD1.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txtInicianteD1CaretUpdate(evt);
            }
        });
        txtInicianteD1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtInicianteD1FocusGained(evt);
            }
        });
        txtInicianteD1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtInicianteD1KeyTyped(evt);
            }
        });

        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel25.setText("Início");

        txtEncerranteD1.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txtEncerranteD1CaretUpdate(evt);
            }
        });
        txtEncerranteD1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtEncerranteD1FocusGained(evt);
            }
        });
        txtEncerranteD1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtEncerranteD1KeyTyped(evt);
            }
        });

        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel26.setText("Final");

        txtInicianteD9.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txtInicianteD9CaretUpdate(evt);
            }
        });
        txtInicianteD9.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtInicianteD9FocusGained(evt);
            }
        });
        txtInicianteD9.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtInicianteD9KeyTyped(evt);
            }
        });

        txtEncerranteD9.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txtEncerranteD9CaretUpdate(evt);
            }
        });
        txtEncerranteD9.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtEncerranteD9FocusGained(evt);
            }
        });
        txtEncerranteD9.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtEncerranteD9KeyTyped(evt);
            }
        });

        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel27.setText("Valor acertado");

        txtValorD1.setText("0.00");
        txtValorD1.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txtValorD1CaretUpdate(evt);
            }
        });
        txtValorD1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtValorD1FocusGained(evt);
            }
        });
        txtValorD1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtValorD1ActionPerformed(evt);
            }
        });
        txtValorD1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtValorD1KeyTyped(evt);
            }
        });

        txtValorD9.setText("0.00");
        txtValorD9.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txtValorD9CaretUpdate(evt);
            }
        });
        txtValorD9.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtValorD9FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtValorD9FocusLost(evt);
            }
        });
        txtValorD9.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtValorD9KeyTyped(evt);
            }
        });

        txtInicianteFrete.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txtInicianteFreteCaretUpdate(evt);
            }
        });
        txtInicianteFrete.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtInicianteFreteFocusGained(evt);
            }
        });
        txtInicianteFrete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtInicianteFreteActionPerformed(evt);
            }
        });
        txtInicianteFrete.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtInicianteFreteKeyTyped(evt);
            }
        });

        txtEncerranteFrete.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txtEncerranteFreteCaretUpdate(evt);
            }
        });
        txtEncerranteFrete.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtEncerranteFreteFocusGained(evt);
            }
        });
        txtEncerranteFrete.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtEncerranteFreteKeyTyped(evt);
            }
        });

        txtValorFrete.setText("0.00");
        txtValorFrete.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txtValorFreteCaretUpdate(evt);
            }
        });
        txtValorFrete.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtValorFreteFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtValorFreteFocusLost(evt);
            }
        });
        txtValorFrete.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtValorFreteKeyTyped(evt);
            }
        });

        jLabel4.setText("Valor Total");

        jLabel14.setText("R$");

        lblTotalBDI.setText("0,00");

        jLabel5.setText("Série");

        jLabel9.setText("D - 1");

        jLabel10.setText("D - 9");

        jLabel11.setText("FRETE");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(141, 141, 141)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(txtInicianteFrete, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtEncerranteFrete, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtValorFrete, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(txtInicianteD9, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtEncerranteD9, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtValorD9, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(txtInicianteD1, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtEncerranteD1, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtValorD1, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(41, 41, 41)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel14)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblTotalBDI, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25)
                    .addComponent(jLabel26)
                    .addComponent(jLabel27)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtInicianteD1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtEncerranteD1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtValorD1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtInicianteD9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtEncerranteD9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtValorD9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(12, 12, 12)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel14)
                            .addComponent(lblTotalBDI))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtInicianteFrete, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtEncerranteFrete, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtValorFrete, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel11))
                .addGap(75, 75, 75))
        );

        btnLimparFormBDI.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BDI/Digital/Icones/if_CCleaner_33788.png"))); // NOI18N
        btnLimparFormBDI.setToolTipText("Limpar Formulário");
        btnLimparFormBDI.setBorderPainted(false);
        btnLimparFormBDI.setContentAreaFilled(false);
        btnLimparFormBDI.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLimparFormBDI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimparFormBDIActionPerformed(evt);
            }
        });

        btnPesquisarBDI.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BDI/Digital/Icones/if_file_search_48764.png"))); // NOI18N
        btnPesquisarBDI.setToolTipText("Pesquisar BDI");
        btnPesquisarBDI.setBorderPainted(false);
        btnPesquisarBDI.setContentAreaFilled(false);
        btnPesquisarBDI.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnPesquisarBDI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPesquisarBDIActionPerformed(evt);
            }
        });

        btnAlterarBDI.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BDI/Digital/Icones/if_file_edit_48763.png"))); // NOI18N
        btnAlterarBDI.setToolTipText("Salvar alterações no BDI");
        btnAlterarBDI.setBorderPainted(false);
        btnAlterarBDI.setContentAreaFilled(false);
        btnAlterarBDI.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAlterarBDI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAlterarBDIActionPerformed(evt);
            }
        });

        btnDeletarBDI.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BDI/Digital/Icones/if_file_delete_48762.png"))); // NOI18N
        btnDeletarBDI.setToolTipText("Deletar BDI");
        btnDeletarBDI.setBorderPainted(false);
        btnDeletarBDI.setContentAreaFilled(false);
        btnDeletarBDI.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnDeletarBDI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeletarBDIActionPerformed(evt);
            }
        });

        btnSalvarBDI.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BDI/Digital/Icones/if_file_add_48761.png"))); // NOI18N
        btnSalvarBDI.setToolTipText("Cadastrar BDI");
        btnSalvarBDI.setBorderPainted(false);
        btnSalvarBDI.setContentAreaFilled(false);
        btnSalvarBDI.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSalvarBDI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalvarBDIActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnSalvarBDI, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnPesquisarBDI, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAlterarBDI, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDeletarBDI, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnLimparFormBDI, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 536, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 800, Short.MAX_VALUE)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 800, Short.MAX_VALUE))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnSalvarBDI)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(btnAlterarBDI)
                        .addComponent(btnPesquisarBDI)
                        .addComponent(btnDeletarBDI)
                        .addComponent(btnLimparFormBDI)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setBounds(0, 0, 836, 497);
    }// </editor-fold>//GEN-END:initComponents

    private void cboHorarioBDIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboHorarioBDIActionPerformed
        
        /*if(txtNumBDI.getText().equals(BDIPesquisado) && validaCampos() == true){
            btnAlterarBDI.setEnabled(true);

        }
        else{
            btnAlterarBDI.setEnabled(false);

        }

        if(validaCampos() == true){
            btnSalvarBDI.setEnabled(true);
            //btnAlterarUsu.setEnabled(true);
        }
        else{
            btnSalvarBDI.setEnabled(false);
            //btnAlterarUsu.setEnabled(false);
        }*/
    }//GEN-LAST:event_cboHorarioBDIActionPerformed

    private void cboPrefixoBDIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboPrefixoBDIActionPerformed
        consultaItinerario();
        if(txtNumBDI.getText().equals(BDIPesquisado) && validaCampos() == true){
            btnAlterarBDI.setEnabled(true);
        }
        else{
            btnAlterarBDI.setEnabled(false);
        }

        if(cboPrefixoBDI.getSelectedItem().toString().equals("Selecione")){
            cboHorarioBDI.removeAllItems();
            cboHorarioBDI.addItem("Vazio");
        }
        if(validaCampos() == true){
            btnSalvarBDI.setEnabled(true);
        }
        else{
            btnSalvarBDI.setEnabled(false);
        }
        //consultaItinerario();
    }//GEN-LAST:event_cboPrefixoBDIActionPerformed

    private void cboMotoristaBDIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboMotoristaBDIActionPerformed
        
        if(txtNumBDI.getText().equals(BDIPesquisado) && validaCampos() == true){
            btnAlterarBDI.setEnabled(true);
        }
        else{
            btnAlterarBDI.setEnabled(false);
        }

        if(validaCampos() == true){
            btnSalvarBDI.setEnabled(true);
        }
        else{
            btnSalvarBDI.setEnabled(false);
        }
        consultaFuncionarios("motorista");
    }//GEN-LAST:event_cboMotoristaBDIActionPerformed

    private void cboCobradorBDIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboCobradorBDIActionPerformed
        
        if(txtNumBDI.getText().equals(BDIPesquisado) && validaCampos() == true){
            btnAlterarBDI.setEnabled(true);
        }
        else{
            btnAlterarBDI.setEnabled(false);
        }
        if(validaCampos() == true){
            btnSalvarBDI.setEnabled(true);
        }
        else{
            btnSalvarBDI.setEnabled(false);
        }
        consultaFuncionarios("cobrador");
    }//GEN-LAST:event_cboCobradorBDIActionPerformed

    private void txtNumBDICaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txtNumBDICaretUpdate
        
        if(txtNumBDI.getText().equals(BDIPesquisado)){
            btnDeletarBDI.setEnabled(true);
            if(validaCampos() == true){
                btnAlterarBDI.setEnabled(true);
            }
            else{
                btnAlterarBDI.setEnabled(false);
            }
        }
        else{
            btnAlterarBDI.setEnabled(false);
            btnDeletarBDI.setEnabled(false);
        }

        if(!txtNumBDI.getText().isEmpty()){
            btnPesquisarBDI.setEnabled(true);

        }
        else{
            btnPesquisarBDI.setEnabled(false);
        }

        if(validaCampos() == true){
            btnSalvarBDI.setEnabled(true);
            //btnAlterarUsu.setEnabled(true);
        }
        else{
            btnSalvarBDI.setEnabled(false);
            //btnAlterarUsu.setEnabled(false);
        }
    }//GEN-LAST:event_txtNumBDICaretUpdate

    private void txtNumBDIFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNumBDIFocusGained
        
        txtNumBDI.selectAll();
    }//GEN-LAST:event_txtNumBDIFocusGained

    private void txtNumBDIKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNumBDIKeyTyped
        
        String caracteresAceitos="0987654321";
        if(!caracteresAceitos.contains(evt.getKeyChar() + "")){
            evt.consume();
        }
        else{
            if(txtNumBDI.getText().length() == 10){
                JOptionPane.showMessageDialog(null, "Limite de caracteres atingido!");
                evt.consume();
            }
        }
    }//GEN-LAST:event_txtNumBDIKeyTyped

    private void txtInicianteD1CaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txtInicianteD1CaretUpdate
        
        if(txtNumBDI.getText().equals(BDIPesquisado) && validaCampos() == true){
            btnAlterarBDI.setEnabled(true);
        }
        else{
            btnAlterarBDI.setEnabled(false);
        }

        if(validaCampos() == true){
            btnSalvarBDI.setEnabled(true);
            //btnAlterarUsu.setEnabled(true);
        }
        else{
            btnSalvarBDI.setEnabled(false);
            //btnAlterarUsu.setEnabled(false);
        }
    }//GEN-LAST:event_txtInicianteD1CaretUpdate

    private void txtInicianteD1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtInicianteD1FocusGained
        
        txtInicianteD1.selectAll();
    }//GEN-LAST:event_txtInicianteD1FocusGained

    private void txtInicianteD1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtInicianteD1KeyTyped
        
        String caracteresAceitos="0987654321";
        if(!caracteresAceitos.contains(evt.getKeyChar() + "")){
            evt.consume();
        }
        else{
            if(txtInicianteD1.getText().length() == 10){
                JOptionPane.showMessageDialog(null, "Limite máximo de caracteres permitidos no campo Iniciante atingido!");
                evt.consume();
            }
        }
    }//GEN-LAST:event_txtInicianteD1KeyTyped

    private void txtEncerranteD1CaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txtEncerranteD1CaretUpdate
        
        if(txtNumBDI.getText().equals(BDIPesquisado) && validaCampos() == true){
            btnAlterarBDI.setEnabled(true);
        }
        else{
            btnAlterarBDI.setEnabled(false);
        }

        if(validaCampos() == true){
            btnSalvarBDI.setEnabled(true);
            //btnAlterarUsu.setEnabled(true);
        }
        else{
            btnSalvarBDI.setEnabled(false);
            //btnAlterarUsu.setEnabled(false);
        }
    }//GEN-LAST:event_txtEncerranteD1CaretUpdate

    private void txtEncerranteD1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtEncerranteD1FocusGained
        
        txtEncerranteD1.selectAll();
    }//GEN-LAST:event_txtEncerranteD1FocusGained

    private void txtEncerranteD1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEncerranteD1KeyTyped
        
        String caracteresAceitos="0987654321";
        if(!caracteresAceitos.contains(evt.getKeyChar() + "")){
            evt.consume();
        }
        else{
            if(txtEncerranteD1.getText().length() == 10){
                JOptionPane.showMessageDialog(null, "Limite máximo de caracteres permitidos no campo Encerrante atingido!");
                evt.consume();
            }
        }
    }//GEN-LAST:event_txtEncerranteD1KeyTyped

    private void txtInicianteD9CaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txtInicianteD9CaretUpdate
        
        if(txtNumBDI.getText().equals(BDIPesquisado) && validaCampos() == true){
            btnAlterarBDI.setEnabled(true);
        }
        else{
            btnAlterarBDI.setEnabled(false);
        }

        if(validaCampos() == true){
            btnSalvarBDI.setEnabled(true);
            //btnAlterarUsu.setEnabled(true);
        }
        else{
            btnSalvarBDI.setEnabled(false);
            //btnAlterarUsu.setEnabled(false);
        }

    }//GEN-LAST:event_txtInicianteD9CaretUpdate

    private void txtInicianteD9FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtInicianteD9FocusGained
        
        txtInicianteD9.selectAll();
    }//GEN-LAST:event_txtInicianteD9FocusGained

    private void txtInicianteD9KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtInicianteD9KeyTyped
        
        String caracteresAceitos="0987654321";
        if(!caracteresAceitos.contains(evt.getKeyChar() + "")){
            evt.consume();
        }
        else{
            if(txtInicianteD9.getText().length() == 10){
                JOptionPane.showMessageDialog(null, "Limite de caracteres atingido!");
                evt.consume();
            }
        }
    }//GEN-LAST:event_txtInicianteD9KeyTyped

    private void txtEncerranteD9CaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txtEncerranteD9CaretUpdate
        
        if(txtNumBDI.getText().equals(BDIPesquisado) && validaCampos() == true){
            btnAlterarBDI.setEnabled(true);
        }
        else{
            btnAlterarBDI.setEnabled(false);
        }
        if(validaCampos() == true){
            btnSalvarBDI.setEnabled(true);
            //btnAlterarUsu.setEnabled(true);
        }
        else{
            btnSalvarBDI.setEnabled(false);
            //btnAlterarUsu.setEnabled(false);
        }
    }//GEN-LAST:event_txtEncerranteD9CaretUpdate

    private void txtEncerranteD9FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtEncerranteD9FocusGained
        
        txtEncerranteD9.selectAll();
    }//GEN-LAST:event_txtEncerranteD9FocusGained

    private void txtEncerranteD9KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEncerranteD9KeyTyped
        
        String caracteresAceitos="0987654321";
        if(!caracteresAceitos.contains(evt.getKeyChar() + "")){
            evt.consume();
        }
        else{
            if(txtEncerranteD9.getText().length() == 10){
                JOptionPane.showMessageDialog(null, "Limite máximo de caracteres permitidos no campo Encerrante atingido!");
                evt.consume();
            }
        }
    }//GEN-LAST:event_txtEncerranteD9KeyTyped

    //A função abaixo valida os botões e atualiza o campo total da tela
    private void txtValorD1CaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txtValorD1CaretUpdate
        
        /*O botão alterar só poderá ser ativo se os campos não forem vazios, e o número do BDI
        ainda for o mesmo do pesquisado, uma vez que não é permitido alterar o número do BDI*/
        if(txtNumBDI.getText().equals(BDIPesquisado) && validaCampos() == true){
            btnAlterarBDI.setEnabled(true);
        }
        else{
            btnAlterarBDI.setEnabled(false);
        }
        //O botão de salvar só pode ser ativo se todos os campos estiverem preenchidos
        if(validaCampos() == true){
            btnSalvarBDI.setEnabled(true);
        }
        else{
            btnSalvarBDI.setEnabled(false);
        }
        //Atualiza o campo total do BDI
        if(!txtValorD1.getText().isEmpty()){
           valorD1 = Double.parseDouble(txtValorD1.getText());
        }
        else{
            valorD1 = Double.parseDouble("0.00");
        }
        
        try {
            totalBDI = valorD1 + valorD9 + valorFrete;
            lblTotalBDI.setText(formatadorDecimal.format(totalBDI).toString());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }//GEN-LAST:event_txtValorD1CaretUpdate

    private void txtValorD1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtValorD1FocusGained
        
        //Seleciona todo o conteudo do campo quando ele for selecionado para uso
        txtValorD1.selectAll();
    }//GEN-LAST:event_txtValorD1FocusGained

    private void txtValorD1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtValorD1ActionPerformed
        
    }//GEN-LAST:event_txtValorD1ActionPerformed

    private void txtValorD1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtValorD1KeyTyped
        
        String caracteresAceitos="0987654321.";
        if(!caracteresAceitos.contains(evt.getKeyChar() + "")){
            evt.consume();
        }
    }//GEN-LAST:event_txtValorD1KeyTyped

    private void txtValorD9CaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txtValorD9CaretUpdate
        
        if(txtNumBDI.getText().equals(BDIPesquisado) && validaCampos() == true){
            btnAlterarBDI.setEnabled(true);
        }
        else{
            btnAlterarBDI.setEnabled(false);
        }

        if(validaCampos() == true){
            btnSalvarBDI.setEnabled(true);
        }
        else{
            btnSalvarBDI.setEnabled(false);
        }
        
        //Atualiza o valor do total na tela sempre que algo for digitado neste campo
        if(!txtValorD9.getText().isEmpty()){
           valorD9 = Double.parseDouble(txtValorD9.getText());
        }
        else{
            valorD9 = Double.parseDouble("0.00");
        }
        
        if(!txtValorD9.getText().isEmpty()){
            try {
                totalBDI = valorD1 + valorD9 + valorFrete;
                lblTotalBDI.setText(formatadorDecimal.format(totalBDI).toString());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }//GEN-LAST:event_txtValorD9CaretUpdate

    private void txtValorD9FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtValorD9FocusGained
        
        //Seleciona todo o conteudo do campo quando ele for selecionado para uso
        txtValorD9.selectAll();
    }//GEN-LAST:event_txtValorD9FocusGained

    private void txtValorD9FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtValorD9FocusLost
        
        
        //JOptionPane.showMessageDialog(null, "Foco perdido!");
    }//GEN-LAST:event_txtValorD9FocusLost

    private void txtValorD9KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtValorD9KeyTyped
        
        String caracteresAceitos="0987654321.";
        if(!caracteresAceitos.contains(evt.getKeyChar() + "")){
            evt.consume();
        }
    }//GEN-LAST:event_txtValorD9KeyTyped

    private void txtInicianteFreteCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txtInicianteFreteCaretUpdate
        
        if(txtNumBDI.getText().equals(BDIPesquisado) && validaCampos() == true){
            btnAlterarBDI.setEnabled(true);
        }
        else{
            btnAlterarBDI.setEnabled(false);
        }

        if(validaCampos() == true){
            btnSalvarBDI.setEnabled(true);
            //btnAlterarUsu.setEnabled(true);
        }
        else{
            btnSalvarBDI.setEnabled(false);
            //btnAlterarUsu.setEnabled(false);
        }
    }//GEN-LAST:event_txtInicianteFreteCaretUpdate

    private void txtInicianteFreteFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtInicianteFreteFocusGained
        
        txtInicianteFrete.selectAll();
    }//GEN-LAST:event_txtInicianteFreteFocusGained

    private void txtInicianteFreteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtInicianteFreteActionPerformed
        
    }//GEN-LAST:event_txtInicianteFreteActionPerformed

    private void txtInicianteFreteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtInicianteFreteKeyTyped
        
        String caracteresAceitos="0987654321";
        if(!caracteresAceitos.contains(evt.getKeyChar() + "")){
            evt.consume();
        }
        else{
            if(txtInicianteFrete.getText().length() == 10){
                JOptionPane.showMessageDialog(null, "Limite de caracteres atingido!");
                evt.consume();
            }
        }
    }//GEN-LAST:event_txtInicianteFreteKeyTyped

    private void txtEncerranteFreteCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txtEncerranteFreteCaretUpdate
        
        if(txtNumBDI.getText().equals(BDIPesquisado) && validaCampos() == true){
            btnAlterarBDI.setEnabled(true);
        }
        else{
            btnAlterarBDI.setEnabled(false);
        }
        if(validaCampos() == true){
            btnSalvarBDI.setEnabled(true);
            //btnAlterarUsu.setEnabled(true);
        }
        else{
            btnSalvarBDI.setEnabled(false);
            //btnAlterarUsu.setEnabled(false);
        }
    }//GEN-LAST:event_txtEncerranteFreteCaretUpdate

    private void txtEncerranteFreteFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtEncerranteFreteFocusGained
        
        txtEncerranteFrete.selectAll();
    }//GEN-LAST:event_txtEncerranteFreteFocusGained

    private void txtEncerranteFreteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEncerranteFreteKeyTyped
        
        String caracteresAceitos="0987654321";
        if(!caracteresAceitos.contains(evt.getKeyChar() + "")){
            evt.consume();
        }
        else{
            if(txtEncerranteFrete.getText().length() == 10){
                JOptionPane.showMessageDialog(null, "Limite de caracteres atingido!");
                evt.consume();
            }
        }
    }//GEN-LAST:event_txtEncerranteFreteKeyTyped

    private void txtValorFreteCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txtValorFreteCaretUpdate
        
        if(txtNumBDI.getText().equals(BDIPesquisado) && validaCampos() == true){
            btnAlterarBDI.setEnabled(true);
        }
        else{
            btnAlterarBDI.setEnabled(false);
        }

        if(validaCampos() == true){
            btnSalvarBDI.setEnabled(true);
            //btnAlterarUsu.setEnabled(true);
        }
        else{
            btnSalvarBDI.setEnabled(false);
            //btnAlterarUsu.setEnabled(false);
        }
        
        if(!txtValorFrete.getText().isEmpty()){
           valorFrete = Double.parseDouble(txtValorFrete.getText());
        }
        else{
            valorFrete = Double.parseDouble("0.00");
        }
        
        try {
            totalBDI = valorD1 + valorD9 + valorFrete;
            lblTotalBDI.setText(formatadorDecimal.format(totalBDI).toString());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }//GEN-LAST:event_txtValorFreteCaretUpdate

    private void txtValorFreteFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtValorFreteFocusGained
        
        //Seleciona todo o conteudo do campo quando ele for selecionado para uso
        txtValorFrete.selectAll();
    }//GEN-LAST:event_txtValorFreteFocusGained

    private void txtValorFreteFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtValorFreteFocusLost
        
        
    }//GEN-LAST:event_txtValorFreteFocusLost

    private void txtValorFreteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtValorFreteKeyTyped
        
        String caracteresAceitos="0987654321.";
        if(!caracteresAceitos.contains(evt.getKeyChar() + "")){
            evt.consume();
        }
    }//GEN-LAST:event_txtValorFreteKeyTyped

    private void btnLimparFormBDIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimparFormBDIActionPerformed
        
        limparTela();
    }//GEN-LAST:event_btnLimparFormBDIActionPerformed

    private void btnPesquisarBDIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPesquisarBDIActionPerformed
        
        consultaBDI();
    }//GEN-LAST:event_btnPesquisarBDIActionPerformed

    private void btnAlterarBDIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlterarBDIActionPerformed
        
        //Verifica se o BDI já está cadastrado, pois precisa existir para ser alterado
        if(checaCodBDI() == true){ //Se for verdadeiro, existe e pode ser alterado
            if(validaCampos() == true){ //Verifica se os campos estão todos devidamente preenchidos
                //As linhas abaixo alteram os dados dos registros de Talões e fretes
                alterarTaloesD1();
                alterarTaloesD9();
                alterarFretes();
                alteraBDI();
                JOptionPane.showMessageDialog(null, "BDI Nº " + txtNumBDI.getText() + " atualizado com sucesso!");
            }
            else{ //Mensagem de erro caso os campos não estejam devidamente preenchidos
                JOptionPane.showMessageDialog(null, "Verifique se todos os campos estão preenchidos corretamente!");
            }
        }
    }//GEN-LAST:event_btnAlterarBDIActionPerformed

    private void btnDeletarBDIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeletarBDIActionPerformed
        
        deletarBDI();
        btnAlterarBDI.setEnabled(false);
    }//GEN-LAST:event_btnDeletarBDIActionPerformed

    private void btnSalvarBDIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarBDIActionPerformed
        
        //codigosBuscados.clear();
        if(checaCodBDI() == false){ //Se não existir BDI cadastrado com o número informado
            if(validaCampos() == true){ //Se os campos estiverem devidamente preenchidos
                if(!verificaIniciantes("SERIED1", txtInicianteD1.getText()) &&
                        !verificaIniciantes("SERIED9", txtInicianteD9.getText()) &&
                        !verificaIniciantes("FRETE", txtInicianteFrete.getText())){
                    cadastraBDI();
                }
                else{
                    JOptionPane.showMessageDialog(null, "Verifique os iniciantes!");
                }
            }
            else{ //Mensagem de erro caso os campos não estejam devidamente preenchidos
                JOptionPane.showMessageDialog(null, "Erro! Verifique se todos os campos estão devidamente preenchidos!");
            }
        }
        else{ //Mensagem de erro caso já exista um BDI com este número cadastrado
            JOptionPane.showMessageDialog(null, "Erro! BDI Nº " + txtNumBDI.getText() + " já está cadastrado!");
        }
    }//GEN-LAST:event_btnSalvarBDIActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAlterarBDI;
    private javax.swing.JButton btnDeletarBDI;
    private javax.swing.JButton btnLimparFormBDI;
    private javax.swing.JButton btnPesquisarBDI;
    private javax.swing.JButton btnSalvarBDI;
    private com.toedter.calendar.JCalendar calendarioBDI;
    public static javax.swing.JComboBox<String> cboCobradorBDI;
    private javax.swing.JComboBox<String> cboHorarioBDI;
    public static javax.swing.JComboBox<String> cboMotoristaBDI;
    public static javax.swing.JComboBox<String> cboPrefixoBDI;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel lblTotalBDI;
    private javax.swing.JTextField txtEncerranteD1;
    private javax.swing.JTextField txtEncerranteD9;
    private javax.swing.JTextField txtEncerranteFrete;
    private javax.swing.JTextField txtInicianteD1;
    private javax.swing.JTextField txtInicianteD9;
    private javax.swing.JTextField txtInicianteFrete;
    private javax.swing.JTextField txtItinerarioBDI;
    private javax.swing.JTextField txtNomeCobradorBDI;
    private javax.swing.JTextField txtNomeMotoristaBDI;
    private javax.swing.JTextField txtNumBDI;
    private javax.swing.JTextField txtValorD1;
    private javax.swing.JTextField txtValorD9;
    private javax.swing.JTextField txtValorFrete;
    // End of variables declaration//GEN-END:variables
}
