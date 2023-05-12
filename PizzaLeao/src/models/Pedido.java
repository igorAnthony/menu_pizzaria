package models;


import java.math.BigDecimal;

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
    private String sabores;
    private String bebidas;
    private String borda;
    private BigDecimal valorTotal;
    
    public Pedido(String tamanho, String sabores, String bebidas, String borda, BigDecimal valorTotal) {
        this.tamanho = tamanho;
        this.sabores = sabores;
        this.bebidas = bebidas;
        this.borda = borda;
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

    public String getSabores() {
        return sabores;
    }

    public void setSabores(String sabores) {
        this.sabores = sabores;
    }

    public String getBebidas() {
        return bebidas;
    }

    public void setBebidas(String bebidas) {
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
}


