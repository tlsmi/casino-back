package com.liceu.casino.services;

import com.liceu.casino.model.Bet;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class RouletteService {

    public Bet getResult() {

        Bet result = new Bet();

        boolean isCol1 = false;
        int num = (int) Math.floor(Math.random() * 36);
        result.setNumber(Collections.singletonList(new Object[]{num}));
        System.out.println(num);
        int[] rojos = {1, 3, 5, 7, 9, 10, 12, 14, 16, 18, 19, 21, 23, 25, 27, 28, 30, 32, 34, 36};
        int[] columna1 = {1, 4, 7, 10, 13, 16, 19, 22, 25, 28, 31, 34};
        int[] columna2 = {2, 5, 8, 11, 14, 17, 20, 23, 26, 29, 32, 35};

        if (num != 0) {

            //Mira si el num es par
            result.setPar(Collections.singletonList(new Object[]{num % 2 == 0}));

            //Mira si el numero esta entre los rojos
            for (int n : rojos) {
                if (n != num) {
                    result.setColor(Collections.singletonList(new String[]{"negro"}));
                } else {
                    result.setColor(Collections.singletonList(new String[]{"rojo"}));
                    break;
                }
            }

            //Mira si el num es de los 18 primeros o no
            if (num > 18) {
                result.setMitad(Collections.singletonList(new Object[]{2}));
            }

            //Mira si esta en la columna 1
            for (int n : columna1) {
                if (n == num) {
                    result.setColumna(Collections.singletonList(new Object[]{1}));
                }
            }


            //Mira si esta en la columna 2 y sino lo pone en la 3
            for (int n : columna1) {
                if (n == num) {
                    result.setColumna(Collections.singletonList(new Object[]{1}));
                    isCol1 = true;
                    break;
                }
            }

            if (!isCol1) {
                for (int n : columna2) {
                    if (n == num) {
                        result.setColumna(Collections.singletonList(new Object[]{2}));
                        break;
                    }
                }
                result.setColumna(Collections.singletonList(new Object[]{3}));
            }

            //Mira en que docena esta el numero
            if (num < 13) {
                result.setDocena(Collections.singletonList(new Object[]{1}));
            } else if (num > 24) {
                result.setDocena(Collections.singletonList(new Object[]{3}));
            } else {
                result.setDocena(Collections.singletonList(new Object[]{2}));
            }

        } else {
            result.setColor(Collections.singletonList(new String[]{"verde"}));
            result.setMitad(Collections.singletonList(new Object[]{0}));
            result.setColumna(Collections.singletonList(new Object[]{0}));
            result.setDocena(Collections.singletonList(new Object[]{0}));
        }
        return result;
    }

    public int getCoinsByResult(Bet result, Bet apuesta) {

        int totalLeft = (int) apuesta.getTotal();

        if (apuesta.getPar() != null){
            totalLeft += compare(result.getPar().get(0)[0], apuesta.getPar());
        }
        if (apuesta.getColor() != null){
            totalLeft += compare(result.getColor().get(0)[0], apuesta.getColor());
        }
        if (apuesta.getMitad() != null){
            totalLeft += compare(result.getMitad().get(0)[0], apuesta.getMitad());
        }
        if (apuesta.getColumna() != null){
            totalLeft += compare(result.getColumna().get(0)[0], apuesta.getColumna());
        }
        if (apuesta.getDocena() != null){
            totalLeft += compare(result.getDocena().get(0)[0], apuesta.getDocena());
        }
        if (apuesta.getNumber() != null){
            totalLeft += compare(result.getNumber().get(0)[0], apuesta.getNumber());
        }

        return totalLeft;
    }
    private int compare(Object par, List<Object[]> apuestaPar) {
        int result = 0;
        for (Object[] objects : apuestaPar) {
            if (par == objects[0]) {
                result += (int) objects[1];
            }else{
                result -= (int) objects[1];
            }
        }

        return result;
    }
}