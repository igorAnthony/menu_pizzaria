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
import java.util.ArrayList;
import java.util.List;
import jdbc.Conexao;
import models.Pizza;

/**
 *
 * @author hawks
 */
public class PizzaDAO {
    public void inserirPizza(Pizza pizza) throws SQLException {
        Connection connection = new Conexao().getConexao();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO pizza (nome, ingredientes, tipo) VALUES (?, ?, ?)");

            preparedStatement.setString(1, pizza.getNome());
            preparedStatement.setString(2, pizza.getIngredientes());
            preparedStatement.setString(3, pizza.getTipo());
            preparedStatement.executeUpdate();
            connection.commit();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            connection.rollback();
            e.printStackTrace();
        }
    }
    public void editarPizza(Pizza pizza) throws SQLException {
        Connection connection = new Conexao().getConexao();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE pizza SET nome = ?, ingredientes = ?, tipo = ? WHERE id = ?");

            preparedStatement.setString(1, pizza.getNome());
            preparedStatement.setString(2, pizza.getIngredientes());
            preparedStatement.setString(3, pizza.getTipo());
            preparedStatement.setInt(4, pizza.getId()); // Supondo que 'getId()' retorna o ID da pizza

            preparedStatement.executeUpdate();
            connection.commit();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            connection.rollback();
            e.printStackTrace();
        }
    }
    public void removerPizza(int idPizza) throws SQLException {
        Connection connection = new Conexao().getConexao();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM pizza WHERE id = ?");
            preparedStatement.setInt(1, idPizza);
            preparedStatement.executeUpdate();
            connection.commit();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            connection.rollback();
            e.printStackTrace();
        }
    }

    public List<Pizza> buscarListaDePizzasPorId(int pedidoId) throws SQLException {
        Connection connection = new Conexao().getConexao();
        String sql = "SELECT * FROM pizza WHERE id = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, pedidoId);
        ResultSet rs = ps.executeQuery();
        List<Pizza> listaDePizzasDoPedido = new ArrayList<>();
        while (rs.next()) {
            int pizzaId = rs.getInt("id");
            String nomePizza = rs.getString("nome");
            String ingredientesPizza = rs.getString("ingredientes");
            String tipoPizza = rs.getString("tipo");
            Pizza pizza = new Pizza(pizzaId, nomePizza, ingredientesPizza, tipoPizza);
            listaDePizzasDoPedido.add(pizza);
        }
        rs.close();
        ps.close();
        return listaDePizzasDoPedido;
    }
    
    public List<Pizza> buscarListaDePizzas(String nomePizza, String tipoPizza) throws SQLException {
        Connection connection = new Conexao().getConexao();
        StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM pizza WHERE 1=1");

        List<Object> parameterValues = new ArrayList<>();

        if (nomePizza != null && !nomePizza.isEmpty()) {
            sqlBuilder.append(" AND nome = ?");
            parameterValues.add(nomePizza);
        }

        if (tipoPizza != null && !tipoPizza.isEmpty()) {
            sqlBuilder.append(" AND tipo = ?");
            parameterValues.add(tipoPizza);
        }

        PreparedStatement preparedStatement = connection.prepareStatement(sqlBuilder.toString());

        for (int i = 0; i < parameterValues.size(); i++) {
            Object parameterValue = parameterValues.get(i);
            preparedStatement.setObject(i + 1, parameterValue);
        }

        ResultSet resultSet = preparedStatement.executeQuery();
        List<Pizza> listaDePizzas = new ArrayList<>();

        while (resultSet.next()) {
            int pizzaId = resultSet.getInt("id");
            String nome = resultSet.getString("nome");
            String ingredientes = resultSet.getString("ingredientes");
            String tipo = resultSet.getString("tipo");
            Pizza pizza = new Pizza(pizzaId, nome, ingredientes, tipo);
            listaDePizzas.add(pizza);
        }

        resultSet.close();
        preparedStatement.close();
        connection.close();

        return listaDePizzas;
    }

    
    public List<Pizza> buscarListaDePizzas() throws SQLException {
        Connection connection = new Conexao().getConexao();
        List<Pizza> ListaDePizzas = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT id, nome, ingredientes, tipo FROM pizza");

            while (resultSet.next()) {
                int idPizza = resultSet.getInt("id");
                String nomePizza = resultSet.getString("nome");
                String ingredientesPizza = resultSet.getString("ingredientes");
                String tipoPizza = resultSet.getString("tipo");

                Pizza pizza = new Pizza(idPizza, nomePizza, ingredientesPizza, tipoPizza);
                ListaDePizzas.add(pizza);
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ListaDePizzas;
    }
}
