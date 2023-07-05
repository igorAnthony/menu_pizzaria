/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import jdbc.Conexao;
import models.User;

/**
 *
 * @author hawks
 */
public class UsuarioDAO {
    public User login(String email, String senha) throws SQLException {
        Connection connection = new Conexao().getConexao();
        String sql = "SELECT * FROM usuario WHERE email = ? AND senha = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, email);
        ps.setString(2, senha);
        ResultSet rs =ps.executeQuery();
        User user = null;
        
        while (rs.next()) {
            user = new User();
            user.setId(rs.getInt("id"));
            user.setNome(rs.getString("nome"));
            user.setEmail(rs.getString("email"));
        }

        rs.close();
        ps.close();        
        return user;
    }
    public void registrarUsuario(String nome, String email, String senha) {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = new Conexao().getConexao();
            ps = connection.prepareStatement("INSERT INTO usuario (nome, email, senha) VALUES (?, ?, ?)");
            ps.setString(1, nome);
            ps.setString(2, email);
            ps.setString(3, senha);
            ps.executeUpdate();
            connection.commit();
            JOptionPane.showMessageDialog(null, "Usuário registrado com sucesso!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocorreu um erro ao registrar o usuário: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Erro ao fechar o PreparedStatement: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Erro ao fechar a conexão com o banco de dados: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

}
