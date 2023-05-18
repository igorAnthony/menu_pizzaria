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
            connection.commit(); // Commit da transação
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            connection.rollback();
            e.printStackTrace();
        }
    }

    public void editarFornecedor(Fornecedor fornecedor) throws SQLException {
        Connection connection = new Conexao().getConexao();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE fornecedor SET nome = ?, telefone = ? WHERE id = ?");

            preparedStatement.setString(1, fornecedor.getNome());
            preparedStatement.setString(2, fornecedor.getTelefone());
            preparedStatement.setInt(3, fornecedor.getId());

            preparedStatement.executeUpdate();

            preparedStatement.close();
            connection.commit(); // Commit da transação
            connection.close();
        } catch (SQLException e) {
            connection.rollback();
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
            // Verificar se existem despesas vinculadas ao fornecedor
            PreparedStatement checkStatement = connection.prepareStatement("SELECT * FROM despesa WHERE id_fornecedor = ?");
            checkStatement.setInt(1, idFornecedor);
            ResultSet resultSet = checkStatement.executeQuery();

            if (resultSet.next()) {
                // Existem despesas vinculadas ao fornecedor, lançar uma exceção
                throw new SQLException("Não é possível remover o fornecedor enquanto houver despesas relacionadas a ele.");
            }

            // Remover o fornecedor
            PreparedStatement removeStatement = connection.prepareStatement("DELETE FROM fornecedor WHERE id = ?");
            removeStatement.setInt(1, idFornecedor);
            removeStatement.executeUpdate();

            // Commit da transação
            connection.commit();

            // Fechar as conexões
            resultSet.close();
            checkStatement.close();
            removeStatement.close();
            connection.close();
        } catch (SQLException e) { 
            connection.rollback(); 
            e.printStackTrace();
           // Desfazer a transação em caso de exceção
        }
    }

}
