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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import jdbc.Conexao;
import models.Despesa;

/**
 *
 * @author hawks
 */
public class DespesaDAO {

        public void inserirDespesa(int idFornecedor, String descricao, String valor, Date data) {
        Connection connection = new Conexao().getConexao();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO despesa (id_fornecedor, valor, data_vencimento, descricao) VALUES (?, ?, ?, ?)");

            preparedStatement.setInt(1, idFornecedor);
            preparedStatement.setBigDecimal(2, new BigDecimal(valor));
            preparedStatement.setDate(3, data);
            preparedStatement.setString(4, descricao);

            preparedStatement.executeUpdate();

            preparedStatement.close();
            connection.commit(); // Commit da transação
            connection.close();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            JOptionPane.showMessageDialog(null, "Ocorreu um erro ao inserir a despesa. Por favor, tente novamente mais tarde.", "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
//    public void editarDespesa(Despesa despesa) throws SQLException {
//        Connection connection = new Conexao().getConexao();
//
//        try {
//            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE despesa SET id_fornecedor = ?, valor = ?, data = ?, descricao = ? WHERE id = ?");
//
//            preparedStatement.setInt(1, despesa.getIdFornecedor());
//            preparedStatement.setBigDecimal(2, despesa.getValor());
//            preparedStatement.setDate(3, Date.valueOf(despesa.getData()));
//            preparedStatement.setString(4, despesa.getDescricao());
//            preparedStatement.setInt(5, despesa.getId());
//
//            preparedStatement.executeUpdate();
//
//            preparedStatement.close();
//            connection.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
    public List<Despesa> buscarListaDeDespesas(
            Date dataPagtoInicio, 
            Date dataPagtoFim, 
            Date dataVencimentoInicio, 
            Date dataVencimentoFim, 
            String nomeFornecedor, 
            String valorMin, 
            String valorMax) throws SQLException { 
        Connection connection = new Conexao().getConexao();
        List<Despesa> listaDespesas = new ArrayList<>();

        try {
            StringBuilder queryBuilder = new StringBuilder("SELECT id, id_fornecedor, valor, data_pagto, data_vencimento, descricao FROM despesa WHERE 1=1");

            // Adicionar filtros opcionais à consulta
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            
            List<Object> parameterValues = new ArrayList<>();
            //System.out.println(dataPagtoInicio);
            if (dataPagtoInicio != null) {
                queryBuilder.append(" AND data_pagto >= ?");
                parameterValues.add(dataPagtoInicio);
            }
            //System.out.println(dataPagtoFim);
            if (dataPagtoFim != null) {
                queryBuilder.append(" AND data_pagto <= ?"); 
                parameterValues.add(dataPagtoFim);
            }

            if (dataVencimentoInicio != null) {
                queryBuilder.append(" AND data_vencimento >= ?"); 
                parameterValues.add(dataVencimentoInicio);
            }

            if (dataVencimentoFim != null) {
                queryBuilder.append(" AND data_vencimento <= ?");
                parameterValues.add(dataVencimentoFim);
            }

            if (!nomeFornecedor.isEmpty()) {
                queryBuilder.append(" AND id_fornecedor IN (SELECT id FROM fornecedor WHERE nome = ?)");
                parameterValues.add(nomeFornecedor);
            }

            if (!valorMin.isEmpty()) {
                queryBuilder.append(" AND valor >= ?");
                parameterValues.add(new BigDecimal(valorMin));
            }

            if (!valorMax.isEmpty()) {
                queryBuilder.append(" AND valor <= ?");
                parameterValues.add(new BigDecimal(valorMax));
            }

            if (!nomeFornecedor.isEmpty()) {
                queryBuilder.append(" AND id_fornecedor IN (SELECT id FROM fornecedor WHERE nome = ?)");
                parameterValues.add(nomeFornecedor);
            }

            if (!valorMin.isEmpty()) {
                queryBuilder.append(" AND valor >= ?");
                parameterValues.add(new BigDecimal(valorMin));
            }

            if (!valorMax.isEmpty()) {
                queryBuilder.append(" AND valor <= ?");
                parameterValues.add(new BigDecimal(valorMax));
            }

            PreparedStatement preparedStatement = connection.prepareStatement(queryBuilder.toString());

            for (int i = 0; i < parameterValues.size(); i++) {
                Object parameterValue = parameterValues.get(i);
                preparedStatement.setObject(i + 1, parameterValue);
            }

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int idDespesa = resultSet.getInt("id");
                int idFornecedor = resultSet.getInt("id_fornecedor");
                BigDecimal valor = resultSet.getBigDecimal("valor");
                LocalDate dataPagto = resultSet.getDate("data_pagto").toLocalDate();
                LocalDate dataVencimento = resultSet.getDate("data_vencimento").toLocalDate();
                String descricao = resultSet.getString("descricao");

                Despesa despesa = new Despesa(idDespesa, idFornecedor, valor, dataVencimento, dataPagto, descricao);
                listaDespesas.add(despesa);
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Ocorreu um erro ao buscar a despesa. Por favor, tente novamente mais tarde.", "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        return listaDespesas;

    }



    public void alterarDataPagtoDespesa(int idDespesa, LocalDate novaDataPagto) throws SQLException {
        Connection connection = new Conexao().getConexao();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE despesa SET data_pagto = ? WHERE id = ?");
            preparedStatement.setDate(1, java.sql.Date.valueOf(novaDataPagto));
            preparedStatement.setInt(2, idDespesa);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("A data de pagamento da despesa foi alterada com sucesso.");
            } else {
                System.out.println("Nenhuma despesa encontrada com o ID fornecido.");
            }

            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    
}
