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
import java.time.LocalDate;
import models.Bebida;
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
    
    public List<Pedido> retornaDetalhesDaNotaPeloId(int notaFiscalId) throws SQLException {
        Connection connection = new Conexao().getConexao();
        String sql = "SELECT * FROM pedido WHERE nota_fiscal_id = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, notaFiscalId);
        ResultSet rs = ps.executeQuery();
        List<Pedido> listaDePedidos = new ArrayList<>();
        Pedido pedido = null;
        while (rs.next()) {
            int pedidoId = rs.getInt("id");
            String tamanho = rs.getString("tamanho");
            String pizzas = rs.getString("pizzas");
            String bebidasEmString = rs.getString("bebidas");
            String borda = rs.getString("borda");
            BigDecimal valorTotal = rs.getBigDecimal("valor_total");

            pedido = null;

            if (pizzas != null) {
                
                List<Pizza> listaDePizzas = new ArrayList<>();

                String[] nomesPizzas = pizzas.split(";");

                for (String nomePizza : nomesPizzas) {
                    Pizza pizzaEncontrada = new PizzaDAO().buscarPizzaPorNomeExato(nomePizza.trim());
                    if(pizzaEncontrada != null){
                        listaDePizzas.add(pizzaEncontrada);
                    }
                }

                pedido = new Pedido(pedidoId, notaFiscalId, tamanho, listaDePizzas, borda, valorTotal);
                listaDePedidos.add(pedido);
            } else if (bebidasEmString != null) {
                List<Bebida> listaDeBebidas = new ArrayList<>();
                
                String[] bebidas = bebidasEmString.split(";");
               
                for (String bebida : bebidas) {
                    String[] partes = bebida.split("\\|");

                    String quantidadeString = partes[0].trim().replace("x", "");
                    int quantidade = Integer.parseInt(quantidadeString);

                    String nome = partes[1].trim();
                    Bebida bebidaEncontrada = new BebidaDAO().buscarBebidaPorNomeExato(nome.trim()); // Utilize a função buscarBebida(nome) para obter o objeto bebida correspondente
                     
                    if (bebidaEncontrada != null) {
                        //System.out.println("bebida encontrada:" + bebidaEncontrada.getNome());
                        bebidaEncontrada.setQuantidade(quantidade);
                        listaDeBebidas.add(bebidaEncontrada);
                    }
                }

                pedido = new Pedido(pedidoId, notaFiscalId, listaDeBebidas, valorTotal);
                System.out.println("valor do idnota:" + notaFiscalId);
                listaDePedidos.add(pedido);
            }            
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
            String sqlNotaFiscal = "INSERT INTO nota_fiscal (id_cliente, id_endereco, total) VALUES (?, ?, ?)";
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
                String sqlPedido = "INSERT INTO pedido (nota_fiscal_id, tamanho, pizzas, bebidas, borda, valor_total) VALUES (?, ?, ?, ?, ?, ?)";
                psPedido = connection.prepareStatement(sqlPedido);
                for (Pedido pedido : listaDePedidos) {
                    psPedido.setInt(1, notaFiscalId);
                    if(pedido.getSabores()!=null){
                        psPedido.setString(2, pedido.getTamanho());
                        psPedido.setString(3, pedido.concatenaPizzas());
                        psPedido.setString(5, pedido.getBorda());
                    }else {
                         psPedido.setString(2, null);
                        psPedido.setString(3, null);
                        psPedido.setString(5, null); // Campo de bebidas é nulo para pedido de pizza
                    }// Concatena os IDs dos sabores

                    if (pedido.getBebidas() != null) {
                        psPedido.setString(4, pedido.concatenarBebidas());
                    } else {
                        psPedido.setString(4, null); // Campo de bebidas é nulo para pedido de pizza
                    }
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
            List<Pedido> listaDePedidos = retornaDetalhesDaNotaPeloId(resultSet.getInt("nota_fiscal_id"));
            int idCliente = resultSet.getInt("id_cliente");
            int idEndereco = resultSet.getInt("id_endereco");
            BigDecimal total = resultSet.getBigDecimal("total");
            int id = resultSet.getInt("nota_fiscal_id");
            //LocalDate dataVenda = resultSet.getDate("data_venda").toLocalDate();
            NotaFiscal nf = new NotaFiscal(id, idEndereco, idCliente, listaDePedidos, total);
            listaDeNotas.add(nf);
        }

        resultSet.close();
        preparedStatement.close();
        connection.close();

        return listaDeNotas;
    }
    public List<NotaFiscal> getAllNotasFiscais() throws SQLException {
        Connection connection = new Conexao().getConexao();
        String sql = "SELECT * FROM nota_fiscal";
        PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        List<NotaFiscal> listaDeNotas = new ArrayList<>();

        while (rs.next()) {
            int notaFiscalId = rs.getInt("id");
            int idCliente = rs.getInt("id_cliente");
            int idEndereco = rs.getInt("id_endereco");
            BigDecimal total = rs.getBigDecimal("total");
            //java.sql.Date dataVenda = rs.getDate("data_venda");

            List<Pedido> listaDePedidos = retornaDetalhesDaNotaPeloId(notaFiscalId); // Passa o ID da nota fiscal corretamente
            System.out.println("valor do idnota:" + notaFiscalId);
            NotaFiscal nf = new NotaFiscal(notaFiscalId, idEndereco, idCliente, listaDePedidos, total);
            listaDeNotas.add(nf);
        }

        rs.close();
        ps.close();
        connection.close();

        return listaDeNotas;
    }


}
