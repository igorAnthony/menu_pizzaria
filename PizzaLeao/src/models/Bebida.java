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
public class Bebida {
    private int id;
    private String nome;
    private BigDecimal preco;
    private int quantidade;

    

    public Bebida(String nome, BigDecimal preco) {
        this.nome = nome;
        this.preco = preco;
    }
    public Bebida(int id, String nome, BigDecimal preco, int quantidade) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
        this.quantidade = quantidade;
    }
    public Bebida(int id, String nome, BigDecimal preco) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
    }
    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }
    
}
