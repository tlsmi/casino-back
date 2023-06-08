package com.liceu.casino.services;

import org.aspectj.weaver.patterns.ConcreteCflowPointcut;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SlotService {
    private static final String[] items = {"🍒", "🍇", "🍊", "🍋", "🔔", "💵", "💰", "💎"};
    private static final int[] valor = {1, 2, 3, 4, 5, 10, 20, 50};
    private static int credito = 100;
    private static String[] resultado;
    private static boolean[] isSpin = new boolean[]{false, false, false};
    private static int apuestaUsuario = 0;
    private static boolean firstInit = true;
    private static int groups = 1;
    private static int duration = 1;
    private static List<String> resultadoUlt = new ArrayList<>();

/*    public SlotService () {
        resultadoUlt.add("❓");
        resultadoUlt.add("❓");
        resultadoUlt.add("❓");
    }*/

    public static Map<String, Object> spin(Integer apuesta) {
        Map<String, Object> map = new HashMap<>();
        if (apuesta == 0) {
            map.put("message", "¡Tienes que apostar dinero!");
            return map;
        } else if (apuesta > 0) apuestaUsuario = apuesta;
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
        }
        resultado = new String[]{"❓", "❓", "❓"};
        resultadoUlt.clear();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                List<String> randomItem = new ArrayList<>(Arrays.asList(items));
                for (int k = 0; k < items.length; k++) {
                    int random = getRandomIndex(randomItem.size());
                    if (j == 2 && k == items.length - 1) resultadoUlt.add(randomItem.get(random));
                    resultado[i] += randomItem.get(random);
                    randomItem.remove(random);
                }
            }
        }

        // Resetea todos los valores del Array a FALSE
        Arrays.fill(isSpin, false);
        credito -= apuesta;
        int gains = checkwin(apuestaUsuario);
        map.put("resultado", resultado);
        map.put("ganancias", gains);
        credito += gains;
        map.put("creditos", credito);
        map.put("message", "");
        map.put("total", resultadoUlt);

        return map;
    }

    public static Map<String, Object> spinColumn(Integer column, Integer apuesta, String emoji) {
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
            if (isSpin[column]) {
                map.put("message", "Esta columna ya ha sido movida solitariamente una vez");
                return map;
            }
            while (true) {
                String anotherFruit = "";
                anotherFruit = items[getRandomIndex(items.length)];
                if (emoji.equals(anotherFruit)) {
                    isSpin[column] = true;
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
            Arrays.fill(isSpin, false);
            credito -= apuesta;
            resultado = new String[3];
            for (int i = 0; i < resultado.length; i++) {
                resultado[i] = items[getRandomIndex(items.length)].repeat(3);
            }
            map.put("resultado", resultado);
        }
        return map;
    }

    public int returnCredito() {
        return credito;
    }

    public List<String> returnResultado() {
        return resultadoUlt;
    }

    private static int getRandomIndex(int length) {
        return (int) (Math.random() * length);
    }

    private static int checkwin(int apuesta) {
        int gains = 0;
        String resultado1 = resultadoUlt.get(0);
        String resultado2 = resultadoUlt.get(1);
        String resultado3 = resultadoUlt.get(2);
        if (resultado1.equals(resultado2) && resultado1.equals(resultado3)&& !resultado1.equals("❓")) {
            for (int i = 0; i < items.length; i++) {
                if (items[i].equals(resultado1)) {
                    gains = apuesta * valor[i];
                    return gains;
                }
            }
        }
        return gains;
    }

    public void reset() {
        credito = 10;
        resultado = new String[3];
    }

    public String[] init() {
        if (firstInit) {
            credito = 10;
        }
        return new String[0];
    }
}
