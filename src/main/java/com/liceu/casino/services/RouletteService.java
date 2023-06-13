package com.liceu.casino.services;

import com.liceu.casino.DAO.UserDAO;
import com.liceu.casino.model.Bet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RouletteService {

    @Autowired
    UserDAO userDAO;

    public Bet getResult(int num) {

        Bet result = new Bet();
        int[] numAux = {num};
        List<int[]> lista = new ArrayList<>();
        lista.add(numAux);
        result.setNumber(lista);
        boolean isCol1 = false;

        System.out.println(num);
        int[] rojos = {1, 3, 5, 7, 9, 12, 14, 16, 18, 19, 21, 23, 25, 27, 30, 32, 34, 36};
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
            } else {
                result.setMitad(Collections.singletonList(new int[]{1}));
            }

            //Mira si esta en la columna 1
            for (int n : columna1) {
                if (n == num) {
                    result.setColumna(Collections.singletonList(new int[]{1}));
                }
            }


            //Mira si esta en la columna 2 y sino lo pone en la 3
            for (int n : columna1) {
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
            } else if (num > 24) {
                result.setDocena(Collections.singletonList(new int[]{3}));
            } else {
                result.setDocena(Collections.singletonList(new int[]{2}));
            }

        } else {
            result.setColor(Collections.singletonList(new String[]{"verde"}));
            result.setMitad(Collections.singletonList(new int[]{0}));
            result.setColumna(Collections.singletonList(new int[]{0}));
            result.setDocena(Collections.singletonList(new int[]{0}));
        }
        return result;
    }

    public Map<String, Object> getBalance(Bet result, Bet bet, String emailUser) {

        Map<String, Object> map = new HashMap<>();

        int totalLeft = 0;

        if (bet.getPar().size() > 0) {
            totalLeft += comparePar(result.getPar().get(0)[0], bet.getPar());
        }
        if (bet.getColor().size() > 0) {
            totalLeft += compareColor((String) result.getColor().get(0)[0], bet.getColor());
        }
        if (bet.getMitad().size() > 0) {
            totalLeft += compareInts2(result.getMitad().get(0)[0], bet.getMitad());
        }
        if (bet.getColumna().size() > 0) {
            totalLeft += compareInts3(result.getColumna().get(0)[0], bet.getColumna());
        }
        if (bet.getDocena().size() > 0) {
            totalLeft += compareInts3(result.getDocena().get(0)[0], bet.getDocena());
        }
        if (bet.getNumber().size() > 0) {
            totalLeft += compareInts36(result.getNumber().get(0)[0], bet.getNumber());
        }
        map.put("balance", totalLeft);
        map.put("message", "ok");
        userDAO.updateUser(userDAO.findByEmail(emailUser).getCoins() + totalLeft, emailUser);
        return map;
    }

    private int comparePar(Object b, List<Object[]> b1) {
        int result = 0;
        for (Object[] objects : b1) {
            if (b == objects[0]) {
                result += (int) objects[1] * 2 - (int) objects[1];
            } else {
                result -= (int) objects[1];
            }
        }

        return result;
    }

    private int compareColor(String s, List<Object[]> s1) {
        int result = 0;
        for (Object[] strings : s1) {
            if (s.equals(strings[0])) {
                result += (int) strings[1] * 2 - (int) strings[1];
            } else {
                result -= (int) strings[1];
            }
        }

        return result;
    }

    private int compareInts2(int i, List<int[]> i1) {
        int result = 0;
        for (int[] ints : i1) {
            if (i == ints[0]) {
                result += ints[1] * 2 - ints[1];
            } else {
                result -= ints[1];
            }
        }

        return result;
    }

    private int compareInts3(int i, List<int[]> i1) {
        int result = 0;
        for (int[] ints : i1) {
            if (i == ints[0]) {
                result += ints[1] * 3 - ints[1];
            } else {
                result -= ints[1];
            }
        }

        return result;
    }

    private int compareInts36(int i, List<int[]> i1) {
        int result = 0;
        for (int[] ints : i1) {
            if (i == ints[0]) {
                result += ints[1] * 36 - ints[1];
            } else {
                result -= ints[1];
            }
        }

        return result;
    }
}