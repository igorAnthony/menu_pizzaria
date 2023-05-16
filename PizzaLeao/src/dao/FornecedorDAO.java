/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import jdbc.Conexao;
import models.Fornecedor;

/**
 *
 * @author hawks
 */
public class FornecedorDAO {

    public void inserirFornecedor(String nome, String telefone) throws SQLException {
        Connection connection = new Conexao().getConexao();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO fornecedor (nome, telefone) VALUES (?, ?)");

            preparedStatement.setString(1, nome);
            preparedStatement.setString(2, telefone);

            preparedStatement.executeUpdate();

            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public Fornecedor buscarFornecedorPorId(int id) throws SQLException {
        Connection connection = new Conexao().getConexao();
        Fornecedor fornecedor = null;

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT nome, telefone FROM fornecedor WHERE id = ?");
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String nome = resultSet.getString("nome");
                String telefone = resultSet.getString("telefone");

                fornecedor = new Fornecedor(id, nome, telefone);
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return fornecedor;
    }
    public List<Fornecedor> buscarListaDeFornecedores(String nome, String telefone) throws SQLException {
        Connection connection = new Conexao().getConexao();
        List<Fornecedor> listaFornecedores = new ArrayList<>();

        try {
            StringBuilder queryBuilder = new StringBuilder("SELECT id, nome, telefone FROM fornecedor WHERE 1=1");

            if (!"".equals(nome)) {
                queryBuilder.append(" AND nome LIKE ?");
            }
            if (!"".equals(telefone)) {
                queryBuilder.append(" AND telefone LIKE ?");
            }

            PreparedStatement preparedStatement = connection.prepareStatement(queryBuilder.toString());

            int parameterIndex = 1;
            if (!"".equals(nome)) {
                preparedStatement.setString(parameterIndex++, "%" + nome + "%");
            }
            if (!"".equals(telefone)) {
                preparedStatement.setString(parameterIndex, "%" + telefone + "%");
            }

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int idFornecedor = resultSet.getInt("id");
                String nomeFornecedor = resultSet.getString("nome");
                String telefoneFornecedor = resultSet.getString("telefone");

                Fornecedor fornecedor = new Fornecedor(idFornecedor, nomeFornecedor, telefoneFornecedor);
                listaFornecedores.add(fornecedor);
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listaFornecedores;
    }
    public void removerFornecedor(int idFornecedor) throws SQLException {
        Connection connection = new Conexao().getConexao();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM fornecedor WHERE id = ?");

            preparedStatement.setInt(1, idFornecedor);

            preparedStatement.executeUpdate();

            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
