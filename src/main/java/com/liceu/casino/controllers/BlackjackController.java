package com.liceu.casino.controllers;

import com.liceu.casino.model.Card;
import com.liceu.casino.model.Suit;
import com.liceu.casino.model.Rank;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/games/blackjack")
public class BlackjackController {
    private static final int BLACKJACK_VALUE = 21;
    private static final int DEALER_MIN_VALUE = 17;
    private static final int INITIAL_CARDS = 2;
    private List<Card> deck;
    private List<Card> usedDeck = new ArrayList<>();
    private int usedCards;
    private List<Card> playerHand;
    private List<Card> dealerHand;
    private Random random;

    public BlackjackController() {
        initializeDeck();
        random = new Random();
    }

    @PostMapping("/start")
    public String startGame() {
        playerHand = new ArrayList<>();
        dealerHand = new ArrayList<>();

        // Initial Cards
        for (int i = 0; i < INITIAL_CARDS; i++) {
            playerHand.add(drawCard());
            dealerHand.add(drawCard());
            usedCards += 2;
        }
        int[] parameters = getHandValue(playerHand);
        int numAs = parameters[1];
        int playerTotal = parameters[0];

        String message = "¿Bienvenido al juego de blackjack!\n\n" +
                "Tus cartas son " + getHandString(playerHand) + "\n";

        if (numAs > 0) message += "El valor de tu mano es igual a " + playerTotal + " o " + (playerTotal + 10 * numAs)  + "\n\n";
        else message += "El valor de tu mano es igual a " + playerTotal + "\n\n";

        message += "La carta visible del crupier es: " + dealerHand.get(0).toString() + "\n";

        int dealerTotal = dealerHand.get(0).getRank().getValue();
        if (dealerHand.get(0).getRank() == Rank.AS) message += "El valor de la mano del crupier es igual a " + dealerTotal + " o " + (dealerTotal + 10)  + "\n\n";
        else message += "El valor de la mano del crupier es igual a " + dealerTotal;

        return message;
    }

    @PostMapping("/hit")
    public String hit() {
        if (playerHand == null) {
            return "Primero debes iniciar el juego!";
        }

        Card newCard = drawCard();
        playerHand.add(newCard);

        int[] parameters = getHandValue(playerHand);
        int playerTotal = parameters[0];
        int numAs = parameters[1];
        if (playerTotal > BLACKJACK_VALUE) {
            return "Te has pasado de 21. ¡Has perdido!";
        } else if (playerTotal == BLACKJACK_VALUE) {
            return "Has conseguido 21. ¡Has ganado!!";
        }

        String message = "Has recibido una carta: " + newCard.toString() + "\n" +
                "Tus cartas son ahora: " + getHandString(playerHand);

        if (numAs > 0) message += "\nEl valor de tu mano es igual a " + playerTotal + " o " + playerTotal + 10;
        else message += "\nEl valor de mano es igual a " + playerTotal;

        return message;
    }

    @PostMapping("/stay")
    public String stay() {
        if (playerHand == null) {
            return "Primero debes iniciar el juego!";
        }
        int[] parameters = getHandValue(playerHand);
        int numAs = parameters[1];
        int playerTotal = parameters[0] + 10 * numAs;
        if (playerTotal > BLACKJACK_VALUE) {
            return "Te has pasado de 21. ¡Has perdido!";
        }

        String message = "Tu turno ha terminado. \n" +
                "Tus cartas son: " + getHandString(playerHand) + "\n" +
                "Las cartas del crupier son: " + getHandString(dealerHand) + "\n";


        parameters = getHandValue(dealerHand);
        numAs = parameters[1];
        int dealerTotal = parameters[0];
        while (dealerTotal < DEALER_MIN_VALUE) {
            Card newCard = drawCard();
            dealerHand.add(newCard);
            dealerTotal = getHandValue(dealerHand)[0];
            message += "El crupier ha recibido una carta: " + newCard.toString() + "\n";
        }
        message += "Las cartas del crupier son: " + getHandString(dealerHand) + "\n" +
                "El crupier tiene un total de " + dealerTotal + "\n";

        if (dealerTotal > BLACKJACK_VALUE) {
            message += "El crupier se ha pasado de 21. ¡Has ganado!";
        } else if (dealerTotal > playerTotal) {
            message += "¡El crupier ha ganado!";
        } else if (dealerTotal < playerTotal) {
            message += "¡Has ganado!";
        } else if (dealerTotal == playerTotal) {
            message += "Es un empate!";
        }

        return message;
    }

    //@PostMapping("/double")

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
        usedDeck.add(card);
        deck.remove(index);
        return card;
    }

    private String getHandString(List<Card> hand) {
        StringBuilder sb = new StringBuilder();
        for (Card card : hand) {
            sb.append(card.toString());
            sb.append(", ");
        }
        return sb.substring(0, sb.length() - 2);
    }

    private int[] getHandValue(List<Card> hand) {
        int value = 0;
        int numAs = 0;

        for (Card card : hand) {
            if (card.getRank() == Rank.AS) {
                numAs++;
                value += 11;
            } else {
                value += card.getRank().getValue();
            }
        }

        int as = numAs;

        while (value > BLACKJACK_VALUE && numAs > 0) {
            value -= 10;
            numAs--;
        }
        return new int[] {value, as};
    }
}
