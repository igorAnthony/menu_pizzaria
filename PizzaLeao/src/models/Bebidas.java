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
public class Bebidas {
    private String nome;
    private int quantidade;
    private BigDecimal preco;

    @Override
    public String toString() {
        return quantidade+"x - " + nome + " R$"+preco;
    }

    public Bebidas(String nome, int quantidade, BigDecimal preco) {
        this.nome = nome;
        this.quantidade = quantidade;
        this.preco = preco;
    }

    public String getNome() {
        return nome;
    }

    public int getQuantidade() {
        return quantidade;
    }
    
    public BigDecimal getPreco(){
        return preco;
    }
    
    public BigDecimal getTotal(){
        return preco.multiply(new BigDecimal(quantidade));
    }

}