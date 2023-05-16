/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import jdbc.Conexao;
import models.NotaFiscal;
import models.Pedido;
import models.Pizza;

/**
 *
 * @author hawks
 */
public class PedidoDAO {
    public List<NotaFiscal> buscarNotasFiscais() throws SQLException {
        Connection connection = new Conexao().getConexao();
        String sql = "SELECT * FROM nota_fiscal";
        PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        List<NotaFiscal> listaDeNotasFiscais = new ArrayList<>();
        while (rs.next()) {
            NotaFiscal notaFiscal = new NotaFiscal(
                    rs.getInt("id_usuario"), rs.getInt("id_endereco"), rs.getBigDecimal("total"));
            listaDeNotasFiscais.add(notaFiscal);
        }
        rs.close();
        ps.close();
        return listaDeNotasFiscais;
    }
    
    public List<Pedido> buscarDetalhesPedido(int notaFiscalId) throws SQLException {
        Connection connection = new Conexao().getConexao();
        String sql = "SELECT * FROM pedido WHERE nota_fiscal_id = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, notaFiscalId);
        ResultSet rs = ps.executeQuery();
        List<Pedido> listaDePedidos = new ArrayList<>();
        while (rs.next()) {
            int pedidoId = rs.getInt("id");
            String tamanho = rs.getString("tamanho");
            String pizzas = rs.getString("pizzas");
            String bebidas = rs.getString("bebidas");
            String borda = rs.getString("borda");
            BigDecimal valorTotal = rs.getBigDecimal("valor_total");
            
            String[] nomesPizzas = pizzas.split(";");

            List<Pizza> listaDePizzas = new ArrayList<>();

            for (String nomePizza : nomesPizzas) {
                List<Pizza> pizzasEncontradas = new PizzaDAO().buscarListaDePizzas(nomePizza.trim(),"");
                listaDePizzas.addAll(pizzasEncontradas);
            }
            // Obter sabores do pedido
            Pedido pedido = new Pedido(tamanho,listaDePizzas, bebidas, borda, valorTotal);
            pedido.setId(pedidoId);
            pedido.setNotaFiscalId(notaFiscalId);
            listaDePedidos.add(pedido);
        }
        rs.close();
        ps.close();
        return listaDePedidos;
    }
    

    public void inserirNotaFiscal(NotaFiscal notaFiscal, List<Pedido> listaDePedidos) throws SQLException {
        PreparedStatement psNotaFiscal = null;
        PreparedStatement psPedido = null;
        Connection connection = new Conexao().getConexao();

        try {
            // Inserir a nota fiscal
            String sqlNotaFiscal = "INSERT INTO nota_fiscal (id_usuario, id_endereco, total) VALUES (?, ?, ?)";
            psNotaFiscal = connection.prepareStatement(sqlNotaFiscal, Statement.RETURN_GENERATED_KEYS);
            psNotaFiscal.setInt(1, notaFiscal.getIdCliente());
            psNotaFiscal.setInt(2, notaFiscal.getIdEndereco());
            psNotaFiscal.setBigDecimal(3, notaFiscal.getTotal());
            psNotaFiscal.executeUpdate();

            // Recuperar o id gerado pelo banco de dados
            ResultSet rs = psNotaFiscal.getGeneratedKeys();
            if (rs.next()) {
                int notaFiscalId = rs.getInt(1);

                // Inserir os detalhes do pedido
                String sqlPedido = "INSERT INTO pedido (nota_fiscal_id, tamanho, sabores, bebidas, borda, valor_total) VALUES (?, ?, ?, ?, ?, ?)";
                psPedido = connection.prepareStatement(sqlPedido);
                for (Pedido pedido : listaDePedidos) {
                    psPedido.setInt(1, notaFiscalId);
                    psPedido.setString(2, pedido.getTamanho());
                    psPedido.setString(3, pedido.concatenaPizzas()); // Concatena os IDs dos sabores
                    psPedido.setString(4, pedido.getBebidas());
                    psPedido.setString(5, pedido.getBorda());
                    psPedido.setBigDecimal(6, pedido.getValorTotal());
                    psPedido.executeUpdate();
                }
            }

            connection.commit();
        } catch (SQLException e) {
            if (connection != null) {
                connection.rollback();
            }
            throw e;
        } finally {
            if (psNotaFiscal != null) {
                psNotaFiscal.close();
            }
            if (psPedido != null) {
                psPedido.close();
            }
        }
    }
    public List<NotaFiscal> buscaTodosPedidosComNome(String nomeCliente, String nomePizza, Date dataInicio, Date dataFim) throws SQLException {
        Connection connection = new Conexao().getConexao();
        StringBuilder sqlBuilder = new StringBuilder("SELECT p.*, nf.id_cliente, nf.id_endereco, nf.data_venda, nf.total, nf.id, u.nome AS nome_cliente ")
                .append("FROM pedido p ")
                .append("INNER JOIN nota_fiscal nf ON nf.id = p.nota_fiscal_id ")
                .append("INNER JOIN cliente u ON u.id = nf.id_cliente ")
                .append("WHERE 1=1");

        List<Object> parameterValues = new ArrayList<>();

        if (nomeCliente != null && !nomeCliente.isEmpty()) {
            sqlBuilder.append(" AND u.nome = ?");
            parameterValues.add(nomeCliente);
        }

        if (nomePizza != null && !nomePizza.isEmpty()) {
            sqlBuilder.append(" AND p.nome_pizza = ?");
            parameterValues.add(nomePizza);
        }

        if (dataInicio != null) {
            sqlBuilder.append(" AND nf.data_venda >= ?");
            parameterValues.add(dataInicio);
        }

        if (dataFim != null) {
            sqlBuilder.append(" AND nf.data_venda <= ?");
            parameterValues.add(dataFim);
        }

        PreparedStatement preparedStatement = connection.prepareStatement(sqlBuilder.toString());

        for (int i = 0; i < parameterValues.size(); i++) {
            Object parameterValue = parameterValues.get(i);
            preparedStatement.setObject(i + 1, parameterValue);
        }

        ResultSet resultSet = preparedStatement.executeQuery();
        List<NotaFiscal> listaDeNotas = new ArrayList<>();

        while (resultSet.next()) {
            List<Pedido> listaDePedidos = buscarDetalhesPedido(resultSet.getInt("nota_fiscal_id"));
            int idCliente = resultSet.getInt("id_cliente");
            int idEndereco = resultSet.getInt("id_endereco");
            BigDecimal total = resultSet.getBigDecimal("total");
            int id = resultSet.getInt("id");
            NotaFiscal nf = new NotaFiscal(id, idEndereco, idCliente, listaDePedidos, total);
            listaDeNotas.add(nf);
        }

        resultSet.close();
        preparedStatement.close();
        connection.close();

        return listaDeNotas;
    }
}
