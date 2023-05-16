/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author hawks
 */
public class Conexao {
    private final String database = "db_poo";
    private final String user = "root";
    private final String password = "";
    
    public Connection getConexao() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/"+database+"?userSSL=false",user, password);
            connection.setAutoCommit(false);
            return connection;
        } catch(SQLException ex) {
            System.out.println("Erro ao tentar realizar conexão com o banco de dados");
            throw new RuntimeException(ex);
        }
    }
}
