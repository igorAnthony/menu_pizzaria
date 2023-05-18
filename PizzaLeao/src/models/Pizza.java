/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author hawks
 */
public class Pizza {
    private int id;
    private String nome;
    private String ingredientes;
    private String tipo;
    
    public Pizza(int id, String nome, String ingredientes, String tipo) {
        this.id = id;
        this.nome = nome;
        this.ingredientes = ingredientes;
        this.tipo = tipo;
    }
    public Pizza(String nome, String ingredientes, String tipo) {
        this.nome = nome;
        this.ingredientes = ingredientes;
        this.tipo = tipo;
    }

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

    public String getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(String ingredientes) {
        this.ingredientes = ingredientes;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}

