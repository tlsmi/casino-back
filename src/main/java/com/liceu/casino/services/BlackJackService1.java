package com.liceu.casino.services;

import com.liceu.casino.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class BlackJackService1 {
    @Autowired
    UserService userService;

    private final int BLACKJACK_VALUE = 21;
    private long credito;
    private Player player;
    private Player dealer;
    private List<Card> deck;
    private Random random;
    private int rounds;

    public ResponseEntity<?> start(BetRequest betRequest, User user) {
        credito = user.getCoins();
        int betApuesta = betRequest.getApuesta();

        // Verificar si hay apuesta
        if (betApuesta <= 0) {
            return ResponseEntity.badRequest().body("El monto de la apuesta debe ser mayor que cero.");
        }

        // Verificar si el jugador tiene suficiente saldo para realizar la apuesta
        if (betApuesta > credito) {
            return ResponseEntity.badRequest().body("No tienes suficiente saldo para realizar esta apuesta.");
        }

        // Restar la apuesta al credito del usuario
        credito -= betApuesta;

        player = new Player(user.getName());
        dealer = new Player("Miguel");

        initializeDeck();
        random = new Random();

        List<Card> userCards = new ArrayList<>();
        List<Card> dealerCards = new ArrayList<>();

        if (rounds >= 4) initializeDeck();

        userCards.add(drawCard());
        userCards.add(drawCard());
        player.setHand(userCards);

        dealerCards.add(drawCard());
        dealerCards.add(drawCard());
        dealer.setHand(dealerCards);

        calculateScore(userCards, player);
        calculateScore(dealerCards, dealer);

        GameResponse gameResponse = new GameResponse(credito, betApuesta, player, dealer, GameState.USER_TURN, Message.HIT_STAND);

        //return null;
        return ResponseEntity.ok(gameResponse);
    }

    private void initializeDeck() {
        deck = new ArrayList<>();
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                deck.add(new Card(rank, suit));
            }
        }
    }

    private Card drawCard() {
        int index = random.nextInt(deck.size());
        Card card = deck.get(index);
        deck.remove(index);
        return card;
    }

    private void calculateScore(List<Card> hand, Player player) {
        int score = 0;
        int numAces = 0;

        for (Card card : hand) {
            if (card.getRank() == Rank.AS) {
                numAces++;
                score += 11;
            } else {
                score += card.getRank().getValue();
            }
        }

        int ace = numAces;

        while (score > BLACKJACK_VALUE && numAces > 0) {
            score -= 10;
            numAces--;
        }
        player.setScore(score);
    }
}
