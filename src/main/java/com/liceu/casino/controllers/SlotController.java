package com.liceu.casino.controllers;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/games/slot")
public class SlotController {
    private String[] items = {"🍒", "🍇", "🍊", "🍋", "🔔", "💵", "💰", "💎"};
    private int[] valor = {1, 2, 3, 4, 5, 10, 20, 50};
    private int credito = 10;
    private String[] resultado = new String[3];

    @GetMapping("/credito")
    public int getCredito() {
        return credito;
    }

    @GetMapping("/resultado")
    public String[] getResultado() {
        return resultado;
    }

    @PostMapping("/spin")
    public String[] spin(@RequestParam(value = "column", required = false) Integer column,
                         @RequestParam(value = "apuesta", required = false) Integer apuesta) {
        if (column != null) {
            if (resultado[0] == null) {
                return new String[]{"No puedes girar una columna sin haber girado las tres"};
            }

            if (column < 0 || column >= resultado.length) {
                return new String[]{"La columna especificada no es válida"};
            }

            resultado[column] = items[getRandomIndex(items.length)];
            return resultado;
        } else {
            if (credito == 0) {
                return new String[]{"¡Se acabó el crédito!"};
            }
            if (apuesta > credito) {
                return new String[]{"La apuesta es más grande que los créditos que tienes"};
            } else if (apuesta == 0) {
                return new String[]{"Tienes que apostar fichas"};
            }

            credito -= apuesta;
            resultado = new String[3];
            for (int i = 0; i < resultado.length; i++) {
                resultado[i] = items[getRandomIndex(items.length)];
            }

            if (resultado[0].equals(resultado[1]) && resultado[0].equals(resultado[2]) && !resultado[0].equals("❓")) {
                int ganancia = 0;
                for (int i = 0; i < items.length; i++) {
                    if (items[i].equals(resultado[0])) {
                        ganancia = apuesta * valor[i];
                    }
                }
                credito += ganancia;
                return new String[]{"¡Ganaste un bono de " + ganancia + " créditos!"};
            }

            return resultado;
        }
    }

    @PostMapping("/spin-all")
    public String[] spinAll() {
        if (resultado[0] != null) {
            return new String[]{"Ya has girado todas las columnas"};
        }

        resultado = new String[3];
        for (int i = 0; i < resultado.length; i++) {
            resultado[i] = items[getRandomIndex(items.length)];
        }

        return resultado;
    }

    @PostMapping("/reset")
    public void reset() {
        credito = 10;
        resultado = new String[3];
    }

    private int getRandomIndex(int length) {
        return (int) (Math.random() * length);
    }

}
