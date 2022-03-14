/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.cefsa.token;

/**
 *
 * @author losan
 */
public class Token {

    private ETipo tipo;
    private String simbolo;
    private int posicao;

    @Override
    public String toString() {
        return "Simbolo: " + simbolo + "  Index: " + index + "  Posicao: " + posicao + "  Tipo: " + tipo;
    }
    private int index;

    public void setPosicao(int posicao) {
        this.posicao = posicao;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Token(ETipo tipo, String simbolo, int posicao) {
        this.tipo = tipo;
        this.simbolo = simbolo;
        this.posicao = posicao;
    }

    public ETipo getTipo() {
        return tipo;
    }

    public void setTipo(ETipo tipo) {
        this.tipo = tipo;
    }

    public void setSimbolo(String simbolo) {
        this.simbolo = simbolo;
    }

    public String getSimbolo() {
        return simbolo;
    }

    public int getPosicao() {
        return posicao;
    }
}
