/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.sql.Date;

/**
 *
 * @author aluno
 */
public class Cliente {
    private int id;
    private String nome;
    private String telefone;
    private Date dataDeNascimento;  
    
    public Cliente(int id, String nome, String telefone, Date dataDeNascimento) {
        this.id = id;
        this.nome = nome;
        this.telefone = telefone;
        this.dataDeNascimento = dataDeNascimento;
    }
    public Cliente(){
        
    }
    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
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

    public Date getDataDeNascimento() {
        return dataDeNascimento;
    }

    public void setDataDeNascimento(Date dataDeNascimento) {
        this.dataDeNascimento = dataDeNascimento;
    }
}
