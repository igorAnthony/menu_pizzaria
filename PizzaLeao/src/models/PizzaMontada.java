/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author hawks
 */
public class PizzaMontada {
    private String tamanho;
    private List<Pizza> sabores;
    private String borda;
    private BigDecimal valorTotal;

    
    
    public PizzaMontada(String tamanho, List<Pizza> sabores, String borda, BigDecimal valorTotal) {
        this.tamanho = tamanho;
        this.sabores = sabores;
        this.borda = borda;
        this.valorTotal = valorTotal;
    }
    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }
    public String getTamanho() {
        return tamanho;
    }

    public void setTamanho(String tamanho) {
        this.tamanho = tamanho;
    }

    public List<Pizza> getSabores() {
        return sabores;
    }

    public void setSabores(List<Pizza> sabores) {
        this.sabores = sabores;
    }

    public String getBorda() {
        return borda;
    }

    public void setBorda(String borda) {
        this.borda = borda;
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
}
