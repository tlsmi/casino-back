package com.liceu.casino.services;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
public class SlotService {
    private static final String[] items = {"🍒", "🍇", "🍊", "🍋", "🔔", "💵", "💰", "💎"};
    private static final int[] valor = {1, 2, 3, 4, 5, 10, 20, 50};
    private static int credito = 10;
    private static String[] resultado = new String[3];
    private static boolean[] spinColumn = new boolean[]{false, false, false};
    private static int apuestaUsuario = 0;

    public static Map<String, Object> spin(Integer column, Integer apuesta) {
        Map<String, Object> map = new HashMap<>();
        if (column != null) {
            if (resultado[0] == null) {
                map.put("message", "No puedes girar una columna sin haber girado las tres");
                return map;
            }

            if (column < 0 || column >= resultado.length) {
                map.put("message", "La columna especificada no es válida");
                return map;
            }
            if (spinColumn[column]) {
                map.put("message", "Esta columna ya ha sido movida solitariamente una vez");
                return map;
            }
            while (true) {
                String anotherFruit = "";
                anotherFruit = items[getRandomIndex(items.length)];
                if (resultado[column].equals(anotherFruit)) {
                    spinColumn[column] = true;
                    break;
                }
            }
            resultado[column] = items[getRandomIndex(items.length)];
        } else {
            if (apuesta != null && apuesta > 0) apuestaUsuario = apuesta;
            else {
                map.put("message", "Necesitas apostar creditos para participar!");
                return map;
            }
            if (credito == 0) {
                map.put("message", "¡Se acabó el crédito!");
                return map;
            }
            if (apuesta > credito) {
                map.put("message", "La apuesta es más grande que los créditos que tienes");
                return map;
            } else if (apuesta == 0) {
                map.put("message", "¡Tienes que apostar dinero!");
                return map;
            }
            // Resetea todos los valores del Array a FALSE
            Arrays.fill(spinColumn, false);
            credito -= apuesta;
            resultado = new String[3];
            for (int i = 0; i < resultado.length; i++) {
                resultado[i] = items[getRandomIndex(items.length)];
            }
        }
        int gains = checkwin(apuestaUsuario);
        map.put("resultado", resultado);
        map.put("ganancias", gains);
        credito += gains;
        map.put("creditos", credito);

        return map;
    }

    public int returnCredito() {
        return credito;
    }

    public String[] returnResultado() {
        return resultado;
    }

    private static int getRandomIndex(int length) {
        return (int) (Math.random() * length);
    }

    private static int checkwin(int apuesta) {
        int gains = 0;
        if (resultado[0].equals(resultado[1]) && resultado[0].equals(resultado[2]) && !resultado[0].equals("❓")) {
            for (int i = 0; i < items.length; i++) {
                if (items[i].equals(resultado[0])) gains = apuesta * valor[i];
            }
        }
        return gains;
    }

    public void reset() {
        credito = 10;
        resultado = new String[3];
    }
}
