/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import jdbc.Conexao;
import models.Cliente;
import models.Endereco;

/**
 *
 * @author hawks
 */
public class ClienteDAO {
    public List<Cliente> buscaListaClientes(String nomeCliente, String telefoneCliente) throws SQLException {
        List<Cliente> clientes = new ArrayList<>();
        try {
            Connection connection = new Conexao().getConexao();
            StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM cliente WHERE 1=1");

            List<Object> parameterValues = new ArrayList<>();

            if (nomeCliente != null && !nomeCliente.isEmpty()) {
                sqlBuilder.append(" AND nome = ?");
                parameterValues.add(nomeCliente);
            }

            if (telefoneCliente != null && !telefoneCliente.isEmpty()) {
                sqlBuilder.append(" AND telefone = ?");
                parameterValues.add(telefoneCliente);
            }

            PreparedStatement preparedStatement = connection.prepareStatement(sqlBuilder.toString());

            for (int i = 0; i < parameterValues.size(); i++) {
                Object parameterValue = parameterValues.get(i);
                preparedStatement.setObject(i + 1, parameterValue);
            }

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nome = resultSet.getString("nome");
                String telefone = resultSet.getString("telefone");
                LocalDate dataDeNascimento = resultSet.getDate("dataDeNascimento").toLocalDate();
                Cliente cliente = new Cliente(id, nome, telefone, dataDeNascimento);
                clientes.add(cliente);
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clientes;
    }

    public Cliente buscaClientePorId(int clientId) throws SQLException {
        Cliente cliente = null;
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            connection = new Conexao().getConexao();
            String sql = "SELECT * FROM cliente WHERE id = ?";
            ps = connection.prepareStatement(sql);
            ps.setInt(1, clientId);
            rs = ps.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String telefone = rs.getString("telefone");
                LocalDate dataDeNascimento = rs.getDate("dataDeNascimento").toLocalDate();
                cliente = new Cliente(id, nome, telefone, dataDeNascimento);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (connection != null) {
                connection.close();
            }
        }

        return cliente;
    }

    public void adicionarEndereco(Endereco novoEndereco, int idUsuario) throws SQLException {
        Connection connection = new Conexao().getConexao();
        PreparedStatement ps = null;
        try {
            String sql = "INSERT INTO endereco(rua, cep, bairro, numero, id_usuario) VALUES (?, ?, ?, ?, ?)";
            ps = connection.prepareStatement(sql);
            ps.setString(1, novoEndereco.getRua());
            ps.setString(2, novoEndereco.getCep());
            ps.setString(3, novoEndereco.getBairro());
            ps.setInt(4, novoEndereco.getNumero());
            ps.setInt(5, idUsuario);
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
        }
    }
    
    public List<Endereco> buscaEnderecoPelaDescricao(String rua, int idCliente) throws SQLException {
        Connection connection = new Conexao().getConexao();
        String sql = "SELECT * FROM endereco WHERE rua LIKE ? AND id_cliente = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, "%" + rua + "%");
        ps.setInt(2, idCliente);
        ResultSet rs = ps.executeQuery();
        List<Endereco> listaDeEnderecos = new ArrayList<>();
        while (rs.next()) {
            Endereco endereco = new Endereco(rs.getString("rua"),
                    rs.getString("cep"), rs.getString("bairro"), rs.getInt("numero"),
                    rs.getInt("id"));
            listaDeEnderecos.add(endereco);
        }
        rs.close();
        ps.close();
        return listaDeEnderecos;
    }
    
    public void removerEndereco(int id) throws SQLException {
        Connection connection = new Conexao().getConexao();
        String sql = "DELETE FROM endereco WHERE id = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, id);
        ps.executeUpdate();

        try {
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            ps.close();
        }
    }
}
