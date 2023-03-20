
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.JCheckBox;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author igora
 */
public class Pedido {
    private String tamanho;
    List<String> sabores;
    List<Bebidas> bebidas;

    public List<Bebidas> getBebidas() {
        return bebidas;
    }

    private String borda;
    double valorTotal = 0;

    public double getValorTotal() {
        return valorTotal;
    }
    public Pedido(String tamanho, List<String> sabores, List<Bebidas> bebidas, String borda, double total) {
        this.tamanho = tamanho;
        this.sabores = sabores;
        this.bebidas = bebidas;
        this.borda = borda;
        this.valorTotal = total;
    }

    public String getTamanho() {
        return tamanho;
    }

    public List<String> getSabores() {
        return sabores;
    }

    public String getBorda() {
        return borda;
    }
    
    
}
