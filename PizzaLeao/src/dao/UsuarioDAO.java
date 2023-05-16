/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
            user.setNivel(rs.getString("nivel"));

        }

        rs.close();
        ps.close();        
        return user;
    }
}
