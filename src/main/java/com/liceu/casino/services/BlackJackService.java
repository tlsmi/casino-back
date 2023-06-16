package com.liceu.casino.services;

import com.liceu.casino.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class BlackJackService {
    @Autowired
    UserService userService;

    private long credito;
    private Player player;
    private Player dealer;
    private List<Card> deck;
    private Random random;

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
        userService.setCredito(credito, user);

        player = new Player(user.getName());
        dealer = new Player("Miguel");

        initializeDeck();
        random = new Random();

        List<Card> userCards = new ArrayList<>();
        List<Card> dealerCards = new ArrayList<>();

        userCards.add(drawCard());
        userCards.add(drawCard());
        player.setHand(userCards);

        dealerCards.add(drawCard());
        dealerCards.add(drawCard());
        dealer.setHand(dealerCards);

        GameResponse gameResponse = new GameResponse(credito, betApuesta, player, dealer);

        return ResponseEntity.ok(gameResponse);
    }

    // En cada ronda, se inicializará la baraja con todas las cartas
    private void initializeDeck() {
        deck = new ArrayList<>();
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                deck.add(new Card(rank, suit));
            }
        }
    }

    // Robar carta
    private Card drawCard() {
        int index = random.nextInt(deck.size());
        Card card = deck.get(index);
        deck.remove(index);
        return card;
    }

    public ResponseEntity<?> hit() {
        if (deck == null) return ResponseEntity.badRequest().body("Has de iniciar el juego antes!");

        Card newCard = drawCard();
        player.hit(newCard);

        return ResponseEntity.ok(player);
    }

    public ResponseEntity<?> stand() {
        if (deck == null) return ResponseEntity.badRequest().body("Has de iniciar el juego antes!");

        Card newCard = drawCard();
        dealer.hit(newCard);

        return ResponseEntity.ok(dealer);
    }

    public ResponseEntity<?> win(BetResponse response, User user) {
        if (response.getResultado().equals("WIN")) userService.setCredito(response.getApuesta() * 2L + credito, user);
        else if (response.getResultado().equals("TIE")) userService.setCredito(response.getApuesta() + credito, user);
        return ResponseEntity.ok(credito);
    }
}
