package com.liceu.casino.model;

import java.util.List;

public class GameResponse {
    private double balance;
    private int betAmount;
    private Player player;
    private Player dealer;

    // Constructor
    public GameResponse(double balance, int betAmount, Player player, Player dealer) {
        this.balance = balance;
        this.betAmount = betAmount;
        this.player = player;
        this.dealer = dealer;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public int getBetAmount() {
        return betAmount;
    }

    public void setBetAmount(int betAmount) {
        this.betAmount = betAmount;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getDealer() {
        return dealer;
    }

    public void setDealer(Player dealer) {
        this.dealer = dealer;
    }
}
