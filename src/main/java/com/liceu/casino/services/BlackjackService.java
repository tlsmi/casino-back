package com.liceu.casino.services;

import com.liceu.casino.model.Card;
import com.liceu.casino.model.Rank;
import com.liceu.casino.model.Suit;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class BlackjackService {
    private static final int BLACKJACK_VALUE = 21;
    private static final int DEALER_MIN_VALUE = 17;
    private static final int INITIAL_CARDS = 2;
    private List<Card> deck;
    private int usedCards;
    private int rounds;
    private List<Card> playerHand;
    private List<Card> dealerHand;
    private Random random;
    private int apuesta;

    public String start() {
        initializeDeck();
        random = new Random();

        playerHand = new ArrayList<>();
        dealerHand = new ArrayList<>();

        if (rounds >= 4) initializeDeck();

        // Initial Cards
        for (int i = 0; i < INITIAL_CARDS; i++) {
            playerHand.add(drawCard());
            dealerHand.add(drawCard());
            usedCards += 2;
        }

        int[] parameters = getHandValue(playerHand);
        int numAs = parameters[1];
        int playerTotal = parameters[0];

        String message = "¡Bienvenido al juego de blackjack!\n\n" +
                "Tus cartas son " + getHandString(playerHand) + "\n";

        message += calculateHandValue(playerTotal, numAs);

        message += "La carta visible del crupier es: " + dealerHand.get(0).toString() + "\n";

        int dealerTotal = dealerHand.get(0).getRank().getValue();
        if (dealerHand.get(0).getRank() == Rank.AS) message += calculateHandValueDealerStart(dealerTotal, true);
        else message += calculateHandValue(dealerTotal, 0);

        return message;
    }

    private String calculateHandValueDealerStart(int dealerTotal, boolean hasAs) {
        if (hasAs) return "El valor de tu mano es igual a " + (dealerTotal) + " o " + (dealerTotal + 10);
        else return "El valor de tu mano es igual a " + dealerTotal;
    }

    private String calculateHandValue(int total, int numAs) {
        if (numAs > 0) {
            if (total - 10 <= BLACKJACK_VALUE) return ("El valor de tu mano es igual a " + (total - 10) + " o " + total + "\n\n");

        }
        return ("El valor de tu mano es igual a " + total + "\n\n");
    }

    public String hit() {
        if (playerHand == null) {
            return "Primero debes iniciar el juego!";
        }

        Card newCard = drawCard();
        playerHand.add(newCard);

        int[] parameters = getHandValue(playerHand);
        int playerTotal = parameters[0];
        int numAs = parameters[1];


        String message = "Has recibido una carta: " + newCard.toString() + "\n" +
                "Tus cartas son ahora: " + getHandString(playerHand) + "\n";

        message += calculateHandValue(playerTotal, numAs);

        if (playerTotal > BLACKJACK_VALUE) {
            message += "Te has pasado de 21. ¡Has perdido!\n";
        }

        return message;
    }

    public String stay() {
        if (playerHand == null) {
            return "Primero debes iniciar el juego!";
        }

        int[] parameters = getHandValue(playerHand);
        int numAs = parameters[1];
        int playerTotal = parameters[0] + 10 * numAs;

        String message = "Tu turno ha terminado. \n" +
                "Tus cartas son: " + getHandString(playerHand) + "\n" +
                "Las cartas del crupier son: " + getHandString(dealerHand) + "\n";


        parameters = getHandValue(dealerHand);
        int dealerTotal = parameters[0];
        boolean hasNewCard = false;
        while (dealerTotal < DEALER_MIN_VALUE) {
            Card newCard = drawCard();
            dealerHand.add(newCard);
            dealerTotal = getHandValue(dealerHand)[0];
            message += "El crupier ha recibido una carta: " + newCard.toString() + "\n";
            hasNewCard = true;
        }
        if (hasNewCard) {
            message += "Las cartas del crupier son: " + getHandString(dealerHand) + "\n" +
                    "El crupier tiene un total de " + dealerTotal + "\n";
        } else {
            message += "El crupier tiene un total de " + dealerTotal + "\n";
        }

        return message + checkwin(dealerTotal, playerTotal);
    }

    private String checkwin(int dealerTotal, int playerTotal) {
        if (dealerTotal > BLACKJACK_VALUE) {
            return "El crupier se ha pasado de 21. ¡Has ganado!";
        } else if (dealerTotal > playerTotal) {
            return "¡El crupier ha ganado!";
        } else if (dealerTotal < playerTotal) {
            return "¡Has ganado!";
        } else if (dealerTotal == playerTotal) {
            return "Es un empate!";
        }
        return null;
    }

    public String doblar() {
        if (playerHand == null) {
            return "Primero debes iniciar el juego!";
        }

        Card newCard = drawCard();
        playerHand.add(newCard);

        int[] parameters = getHandValue(playerHand);
        int playerTotal = parameters[0];
        int numAs = parameters[1];

        String message = "Has recibido una carta: " + newCard.toString() + "\n" +
                "Tus cartas son ahora: " + getHandString(playerHand);

        message += calculateHandValue(playerTotal, numAs);

        if (playerTotal > BLACKJACK_VALUE) {
            message += "Te has pasado de 21. ¡Has perdido!";
            return message;
        }

        parameters = getHandValue(playerHand);
        numAs = parameters[1];
        playerTotal = parameters[0] + 10 * numAs;

        message += "Tu turno ha terminado. \n" +
                "Tus cartas son: " + getHandString(playerHand) + "\n" +
                "Las cartas del crupier son: " + getHandString(dealerHand) + "\n";


        parameters = getHandValue(dealerHand);
        int dealerTotal = parameters[0];
        while (dealerTotal < DEALER_MIN_VALUE) {
            newCard = drawCard();
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

    private String getHandString(List<Card> hand) {
        StringBuilder sb = new StringBuilder();
        for (Card card : hand) {
            sb.append(card.toString());
            sb.append(", ");
        }
        return sb.substring(0, sb.length() - 2);
    }
}
