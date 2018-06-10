/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BDI.Digital.DAL;

import java.sql.*;
import javax.swing.JOptionPane;

/**
 *
 * @author Silvio
 */
public class ModuloConexao {
    //Método responsável por estabelecer a conexao com o banco
    public static Connection conector(){
        java.sql.Connection conexao = null;
        //A linha abaixo "chama" o driver
        String driver = "com.mysql.jdbc.Driver";
        //Armazenando informações referentes ao banco
        String url = "jdbc:mysql://localhost:3306/goianesia";
        String user = "root";
        String password = "root";
        //Estabelecendo a conexao com o banco
        try {
            Class.forName(driver);
            conexao = DriverManager.getConnection(url, user, password);
            return conexao;
        } catch (Exception e) {
            //A linha abaixo serve de apoio para esclarecer o erro
            JOptionPane.showMessageDialog(null, e);
            return null;
        }
    }
    
}
