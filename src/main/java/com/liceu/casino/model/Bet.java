package com.liceu.casino.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.List;

@Entity
public class Bet {
    @Id
    Object[] emailUser;

    Object total;

    private List<Object[]> par;

    private List<Object[]> color;

    private List<Object[]> mitad;

    private List<Object[]> columna;

    private List<Object[]> docena;

    private List<Object[]> number;

    public Object getTotal() {
        return total;
    }

    public void setTotal(Object total) {
        this.total = total;
    }

    public List<Object[]> getNumber() {
        return number;
    }

    public void setNumber(List<Object[]> number) {
        this.number = number;
    }

    public Object[] getEmailUser() {
        return emailUser;
    }

    public void setEmailUser(Object[] emailUser) {
        this.emailUser = emailUser;
    }

    public List<Object[]> getPar() {
        return par;
    }

    public void setPar(List<Object[]> par) {
        this.par = par;
    }

    public List<Object[]> getColor() {
        return color;
    }

    public void setColor(List<Object[]> color) {
        this.color = color;
    }

    public List<Object[]> getMitad() {
        return mitad;
    }

    public void setMitad(List<Object[]> mitad) {
        this.mitad = mitad;
    }

    public List<Object[]> getColumna() {
        return columna;
    }

    public void setColumna(List<Object[]> columna) {
        this.columna = columna;
    }

    public List<Object[]> getDocena() {
        return docena;
    }

    public void setDocena(List<Object[]> docena) {
        this.docena = docena;
    }
}
