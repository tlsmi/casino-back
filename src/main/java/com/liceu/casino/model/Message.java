package com.liceu.casino.model;

public enum Message {
    BET("Place a Bet!"),
    HIT_STAND("Hit or Stand?"),
    BUST("Bust!"),
    USER_WIN("You Win!"),
    DEALER_WIN("Dealer Wins!"),
    TIE("Tie!");

    private final String message;

    private Message(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
