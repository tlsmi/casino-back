package com.liceu.casino.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.List;

@Entity
public class BetResult {
    @Id
    String[] emailUser;

    private BetQuantity quantity;
    int total;
    private List<Object[]> par;

    private List<String[]> color;

    private List<int[]> mitad;

    private List<int[]> columna;

    private List<int[]> docena;

    private List<int[]> number;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<int[]> getNumber() {
        return number;
    }

    public void setNumber(List<int[]> number) {
        this.number = number;
    }

    public String[] getEmailUser() {
        return emailUser;
    }

    public void setEmailUser(String[] emailUser) {
        this.emailUser = emailUser;
    }

    public BetQuantity getQuantity() {
        return quantity;
    }

    public void setQuantity(BetQuantity quantity) {
        this.quantity = quantity;
    }

    public List<Object[]> getPar() {
        return par;
    }

    public void setPar(List<Object[]> par) {
        this.par = par;
    }

    public List<String[]> getColor() {
        return color;
    }

    public void setColor(List<String[]> color) {
        this.color = color;
    }

    public List<int[]> getMitad() {
        return mitad;
    }

    public void setMitad(List<int[]> mitad) {
        this.mitad = mitad;
    }

    public List<int[]> getColumna() {
        return columna;
    }

    public void setColumna(List<int[]> columna) {
        this.columna = columna;
    }

    public List<int[]> getDocena() {
        return docena;
    }

    public void setDocena(List<int[]> docena) {
        this.docena = docena;
    }
}
