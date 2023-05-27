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
                result.setMitad(Collections.singletonList(new int[]{2}));
            }

            //Mira si esta en la columna 1
            for (int n:columna1) {
                if (n == num) {
                    result.setColumna(Collections.singletonList(new int[]{1}));
                }
            }


            //Mira si esta en la columna 2 y sino lo pone en la 3
            for (int n:columna1) {
                if (n == num) {
                    result.setColumna(Collections.singletonList(new int[]{1}));
                    isCol1 = true;
                    break;
                }
            }

            if (!isCol1) {
                for (int n : columna2) {
                    if (n == num) {
                        result.setColumna(Collections.singletonList(new int[]{2}));
                        break;
                    }
                }
                result.setColumna(Collections.singletonList(new int[]{3}));
            }

            //Mira en que docena esta el numero
            if (num < 13) {
                result.setDocena(Collections.singletonList(new int[]{1}));
            } else if (num > 24){
                result.setDocena(Collections.singletonList(new int[]{3}));
            }else{
                result.setDocena(Collections.singletonList(new int[]{2}));
            }

        }else{
            result.setColor(Collections.singletonList(new String[]{"verde"}));
            result.setMitad(Collections.singletonList(new int[]{0}));
            result.setColumna(Collections.singletonList(new int[]{0}));
            result.setDocena(Collections.singletonList(new int[]{0}));
        }
        return result;
    }

    public int getCoinsByResult(Bet result,Bet apuesta) {

        int totalLeft = apuesta.getTotal();

        totalLeft += compareParity(result.getPar(),apuesta.getPar());
        totalLeft += compareColor(result.getColor(),apuesta.getColor());
        totalLeft += compareHalf(result.getMitad(),apuesta.getMitad());
        totalLeft += compareColumn(result.getColumna(),apuesta.getColumna());
        totalLeft += compareDozen(result.getDocena(),apuesta.getDocena());

        return totalLeft;
    }

    private int compareDozen(List<int[]> docena, List<int[]> apuestaDocena) {
        for (int[] bet: apuestaDocena) {
            if (bet[1] == docena.get(0)[0]){

            }
        }
        return 0;
    }

    private int compareColumn(List<int[]> columna, List<int[]> apuestaColumna) {
        return 0;
    }

    private int compareHalf(List<int[]> mitad, List<int[]> apuestaMitad) {
        return 0;
    }

    private int compareColor(List<String[]> color, List<String[]> apuestaColor) {
        return 0;
    }

    private int compareParity(List<Object[]> par, List<Object[]> apuestaPar) {
        return 0;
    }
}
