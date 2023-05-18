/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author hawks
 */
public class NotaFiscal {
    private int id;
    private int id_cliente;
    private int id_endereco;
    private List<Pedido> listaPedidos;

    private BigDecimal total;

    public NotaFiscal(int id_cliente, int id_endereco) {
        this.id_cliente = id_cliente;
        this.id_endereco = id_endereco;
    }
    public NotaFiscal(int id, int id_cliente, int id_endereco, List<Pedido> listaPedidos, BigDecimal total) {
        this.id_cliente = id_cliente;
        this.listaPedidos = listaPedidos;
        this.id_endereco = id_endereco;
        this.total = total;
    }
    public NotaFiscal(int id_cliente, int id_endereco, BigDecimal total) {
        this.id_cliente = id_cliente;
        this.id_endereco = id_endereco;
        this.total = total;
    }
    public List<Pedido> getListaPedidos() {
        return listaPedidos;
    }

    public void setListaPedidos(List<Pedido> listaPedidos) {
        this.listaPedidos = listaPedidos;
    }
    private LocalDate data_venda;

    public LocalDate getData_venda() {
        return data_venda;
    }

    public void setData_venda(LocalDate data_venda) {
        this.data_venda = data_venda;
    }
    public int getId() {
        return id;
    }

    public int getIdCliente() {
        return id_cliente;
    }

    public int getIdEndereco() {
        return id_endereco;
    }

    public BigDecimal getTotal() {
        return total;
    }
}
