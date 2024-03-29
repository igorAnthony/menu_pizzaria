package models;


import java.math.BigDecimal;
import java.util.List;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author igora
 */
public class Pedido {
    private int id;
    private int notaFiscalId;
    private String tamanho;
    private List<Pizza> sabores;

    public void setSabores(List<Pizza> sabores) {
        this.sabores = sabores;
    }
    private List<Bebida> bebidas;
    private String borda;
    private BigDecimal valorTotal;
    private String nomeCliente;
   
    public Pedido(int id, int notaFiscalId, String tamanho, List<Pizza> pizzas, String borda, BigDecimal valorTotal) {
        this.id = id;
        this.notaFiscalId = notaFiscalId;
        this.tamanho = tamanho;
        this.sabores = pizzas;
        this.borda = borda;
        this.valorTotal = valorTotal;
    }
    public Pedido(String tamanho, List<Pizza> pizzas, String borda, BigDecimal valorTotal) {
        this.tamanho = tamanho;
        this.sabores = pizzas;
        this.borda = borda;
        this.valorTotal = valorTotal;
    }
    public Pedido(int id, int notaFiscalId, List<Bebida> bebidas, BigDecimal valorTotal) {
        this.id = id;
        this.notaFiscalId = notaFiscalId;
        this.bebidas = bebidas;
        this.valorTotal = valorTotal;
    }
    public Pedido(List<Bebida> bebidas, BigDecimal valorTotal) {
        this.id = id;
        this.notaFiscalId = notaFiscalId;
        this.bebidas = bebidas;
        this.valorTotal = valorTotal;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNotaFiscalId() {
        return notaFiscalId;
    }

    public void setNotaFiscalId(int notaFiscalId) {
        this.notaFiscalId = notaFiscalId;
    }

    public String getTamanho() {
        return tamanho;
    }

    public void setTamanho(String tamanho) {
        this.tamanho = tamanho;
    }

    public List<Pizza> getSabores() {
        return this.sabores;
    }

    public String concatenaPizzas() {
        StringBuilder sb = new StringBuilder();
        for (Pizza sabor : sabores) {
            sb.append(sabor.getNome()).append("; ");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1); // Remove a última vírgula
        }
        return sb.toString();
    }
    public String concatenarBebidas() {
        StringBuilder sb = new StringBuilder();
        for (Bebida bebida : bebidas) {
            sb.append(bebida.getQuantidade()).append("x | ").append(bebida.getNome()).append("; ");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1); // Remove a última vírgula
        }
        return sb.toString();
    }
    public List<Bebida> getBebidas() {
        return bebidas;
    }

    public void setBebidas(List<Bebida> bebidas) {
        this.bebidas = bebidas;
    }

    public String getBorda() {
        return borda;
    }

    public void setBorda(String borda) {
        this.borda = borda;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }
    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }
}


