/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author hawks
 */
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

public class Despesa {
    private int id;
    private int idFornecedor;
    private BigDecimal valor;
    private LocalDate dataDeVenc;
    private LocalDate dataDePagto;
    private String descricao;
    
    public Despesa(int idFornecedor, BigDecimal valor, LocalDate dataDeVenc, String descricao) {
        this.idFornecedor = idFornecedor;
        this.valor = valor;
        this.dataDeVenc = dataDeVenc;
        this.descricao = descricao;
    }
    public Despesa(int id, int idFornecedor, BigDecimal valor, LocalDate dataDeVenc, LocalDate dataDePagto, String descricao) {
        this.id = id;
        this.idFornecedor = idFornecedor;
        this.valor = valor;
        this.dataDePagto = dataDePagto;
        this.dataDeVenc = dataDeVenc;
        this.descricao = descricao;
    }
    // Getters e Setters
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getIdFornecedor() {
        return idFornecedor;
    }
    
    public void setIdFornecedor(int idFornecedor) {
        this.idFornecedor = idFornecedor;
    }
    
    public BigDecimal getValor() {
        return valor;
    }
    
    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
    
    public LocalDate getDataDeVenc() {
        return dataDeVenc;
    }
    
    public void setDataDeVenc(LocalDate data) {
        this.dataDeVenc = data;
    }
    public LocalDate getDataDePagto() {
        return dataDePagto;
    }

    public void setDataDePagto(LocalDate dataDePagto) {
        this.dataDePagto = dataDePagto;
    }
    public String getDescricao() {
        return descricao;
    }
    
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}