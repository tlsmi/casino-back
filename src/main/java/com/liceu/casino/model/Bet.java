package com.liceu.casino.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Bet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "emailUser")
    private User emailUser;

    private int total;

    private List<Object[]> par;

    private List<Object[]> color;

    private List<int[]> mitad;

    private List<int[]> columna;

    private List<int[]> docena;

    private List<int[]> number;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getEmailUser() {
        return emailUser.getEmail();
    }

    public void setEmailUser(User emailUser) {
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
