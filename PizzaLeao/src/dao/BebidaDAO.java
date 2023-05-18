/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import jdbc.Conexao;
import models.Bebida;

/**
 *
 * @author hawks
 */
public class BebidaDAO {
    public void adicionarBebida(Bebida novaBebida) throws SQLException {
        Connection connection = new Conexao().getConexao();
        PreparedStatement ps = null;
        try {
            String sql = "INSERT INTO bebida(nome, preco) VALUES (?, ?)";
            ps = connection.prepareStatement(sql);
            ps.setString(1, novaBebida.getNome());
            ps.setBigDecimal(2, novaBebida.getPreco());
            ps.executeUpdate();

            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            System.out.println("Erro ao tentar inserir no banco de dados");
            throw e;
        } finally {
            if (ps != null) {
                ps.close();
            }
            connection.close();
        }
    }
    public void removerBebida(int idBebida) throws SQLException {
        Connection connection = new Conexao().getConexao();
        PreparedStatement ps = null;
        try {
            String sql = "DELETE FROM bebida WHERE id = ?";
            ps = connection.prepareStatement(sql);
            ps.setInt(1, idBebida);
            ps.executeUpdate();

            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            System.out.println("Erro ao tentar remover do banco de dados");
            throw e;
        } finally {
            if (ps != null) {
                ps.close();
            }
            connection.close();
        }
    }
    public void atualizarBebida(Bebida bebida) throws SQLException {
        Connection connection = new Conexao().getConexao();
        PreparedStatement ps = null;
        try {
            String sql = "UPDATE bebida SET nome = ?, preco = ? WHERE id = ?";
            ps = connection.prepareStatement(sql);
            ps.setString(1, bebida.getNome());
            ps.setBigDecimal(2, bebida.getPreco());
            ps.setInt(3, bebida.getId());
            ps.executeUpdate();

            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            System.out.println("Erro ao tentar atualizar no banco de dados");
            throw e;
        } finally {
            if (ps != null) {
                ps.close();
            }
            connection.close();
        }
    }
    public List<Bebida> buscarBebida(String nome) throws SQLException {
        Connection connection = new Conexao().getConexao();
        List<Bebida> listaBebidas = new ArrayList<>();

        try {
            StringBuilder sqlBuilder = new StringBuilder("SELECT id, nome, preco FROM bebida WHERE 1=1");

            if (nome != null && !nome.isEmpty()) {
                sqlBuilder.append(" AND nome LIKE ?");
            }

            PreparedStatement ps = connection.prepareStatement(sqlBuilder.toString());

            if (nome != null && !nome.isEmpty()) {
                ps.setString(1, "%" + nome + "%");
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int idBebida = rs.getInt("id");
                String nomeBebida = rs.getString("nome");
                BigDecimal precoBebida = rs.getBigDecimal("preco");

                Bebida bebida = new Bebida(idBebida, nomeBebida, precoBebida);
                listaBebidas.add(bebida);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }

        return listaBebidas;
    }
    public Bebida buscarBebidaPorNomeExato(String nome) throws SQLException {
        Connection connection = new Conexao().getConexao();
        Bebida bebida = null;

        try {
            String sql = "SELECT id, nome, preco FROM bebida WHERE nome = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, nome);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int idBebida = rs.getInt("id");
                String nomeBebida = rs.getString("nome");
                BigDecimal precoBebida = rs.getBigDecimal("preco");

                bebida = new Bebida(idBebida, nomeBebida, precoBebida);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }

        return bebida;
    }

    public Bebida buscarBebidaPorId(int id) throws SQLException {
        Connection connection = new Conexao().getConexao();
        Bebida bebida = null;

        try {
            String sql = "SELECT id, nome, preco FROM bebida WHERE id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int idBebida = rs.getInt("id");
                String nomeBebida = rs.getString("nome");
                BigDecimal precoBebida = rs.getBigDecimal("preco");

                bebida = new Bebida(idBebida, nomeBebida, precoBebida);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }

        return bebida;
    }


}
