package com.liceu.casino.controllers;

import com.liceu.casino.model.Card;
import com.liceu.casino.model.Rank;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/games")
public class BlackjackController {
    private static final int BLACKJACK_VALUE = 21;
    private static final int DEALER_MIN_VALUE = 17;
    private static final int INITIAL_CARDS = 2;
    private static final String[] SUIT = {"PICAS", "DIAMANTES", "CORAZONES", "TREBOLES"};
    private static final String[] NUMBERS = {"AS", "DOS", "TRES", "CUATRO", "CINCO", "SEIS", "SIETE", "OCHO", "NUEVE", "DIEZ", "JOTA", "CU", "CA"};
    private static final Map<String, Integer> RANK;
    static {
        RANK = new HashMap<>();
        RANK.put("AS", 1);
        RANK.put("DOS", 2);
        RANK.put("TRES", 3);
        RANK.put("CUATRO", 4);
        RANK.put("CINCO", 5);
        RANK.put("SEIS", 6);
        RANK.put("SIETE", 7);
        RANK.put("OCHO", 8);
        RANK.put("NUEVE", 9);
        RANK.put("DIEZ", 10);
        RANK.put("J", 10);
        RANK.put("Q", 10);
        RANK.put("K", 10);
    }

    private List<Card> deck;
    private List<Card> playerHand;
    private List<Card> dealerHand;
    private Random random;

    public BlackjackController() {
        initializeDeck();
        random = new Random();
    }

    @PostMapping("/blackjack/start")
    public String startGame() {
        playerHand = new ArrayList<>();
        dealerHand = new ArrayList<>();

        // Deal initial cards
        for (int i = 0; i < INITIAL_CARDS; i++) {
            playerHand.add(drawCard());
            dealerHand.add(drawCard());
        }

        String message = "¡Bienvenido al juego de blackjack!\n";
        message += "Tus cartas son: " + getHandString(playerHand) + "\n";
        message += "La carta visible del crupier es: " + dealerHand.get(0).toString();
        return message;
    }

    @PostMapping("/blackjack/hit")
    public String hit() {
        if (playerHand == null) {
            return "Primero debes iniciar el juego.";
        }

        Card newCard = drawCard();
        playerHand.add(newCard);

        int playerTotal = getHandValue(playerHand);
        if (playerTotal > BLACKJACK_VALUE) {
            return "Te has pasado de 21. Has perdido.";
        }

        return "Has recibido una carta: " + newCard.toString() + "\n" +
                "Tus cartas son ahora: " + getHandString(playerHand);
    }

    @PostMapping("/blackjack/stand")
    public String stand() {
        if (playerHand == null) {
            return "Primero debes iniciar el juego.";
        }

        int playerTotal = getHandValue(playerHand);
        if (playerTotal > BLACKJACK_VALUE) {
            return "Te has pasado de 21. Has perdido.";
        }

        String message = "Tu turno ha terminado.\n";
        message += "Tus cartas son: " + getHandString(playerHand) + "\n";
        message += "Las cartas del crupier son: " + getHandString(dealerHand) + "\n";

        int dealerTotal = getHandValue(dealerHand);
        while (dealerTotal < DEALER_MIN_VALUE) {
            Card newCard = drawCard();
            dealerHand.add(newCard);
            dealerTotal = getHandValue(dealerHand);
            message += "El crupier ha recibido una carta: " + newCard.toString() + "\n";
        }

        message += "El crupier tiene un total de " + dealerTotal + "\n";

        if (dealerTotal > BLACKJACK_VALUE) {
            message += "El crupier se ha pasado de 21. ¡Has ganado!";
        } else if (dealerTotal > playerTotal) {
            message += "El crupier ha ganado.";
        } else if (dealerTotal < playerTotal) {
            message += "¡Has ganado!";
            message += "Es un empate.";
        }

        return message;
    }

    private void initializeDeck() {
        deck = new ArrayList<>();
        for (int i = 0; i < SUIT.length; i++) {
            for (int j = 0; j < NUMBERS.length; j++) {
                deck.add(new Card(SUIT[i], NUMBERS[j]));
            }
        }
    }

    private Card drawCard() {
        int index = random.nextInt(deck.size());
        Card card = deck.get(index);
        deck.remove(index);
        return card;
    }

    private int getHandValue(List<Card> hand) {
        int value = 0;
        int numAces = 0;

        for (Card card : hand) {
            if (card.getRank() == Rank.ACE) {
                numAces++;
                value += 11;
            } else {
                value += card.getRank().getValue();
            }
        }

        while (value > BLACKJACK_VALUE && numAces > 0) {
            value -= 10;
            numAces--;
        }

        return value;
    }

    private String getHandString(List<Card> hand) {
        StringBuilder sb = new StringBuilder();
        for (Card card : hand) {
            sb.append(card.toString());
            sb.append(", ");
        }
        return sb.substring(0, sb.length() - 2);
    }
}