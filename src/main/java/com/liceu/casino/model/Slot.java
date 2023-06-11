package com.liceu.casino.model;

public class Slot {
    private final String[] items = {"🍒", "🍇", "🍊", "🍋", "🔔", "💵", "💰", "💎"};
    private final int[] valor = {1, 2, 3, 4, 5, 10, 20, 50};

    public Slot() {}

    public String[] getItems() {
        return items;
    }

    public int[] getValor() {
        return valor;
    }
}
