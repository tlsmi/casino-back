package com.liceu.casino.model;

import java.util.List;

public class GameResponse {
    private double balance;
    private int betAmount;
    private Player player;
    private Player dealer;
    private GameState gameState;
    private Message message;

    // Constructor
    public GameResponse(double balance, int betAmount, Player player, Player dealer, GameState gameState, Message message) {
        this.balance = balance;
        this.betAmount = betAmount;
        this.player = player;
        this.dealer = dealer;
        this.gameState = gameState;
        this.message = message;
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

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
