package com.liceu.casino.services;

import com.liceu.casino.DAO.UserDAO;
import com.liceu.casino.model.Slot;
import com.liceu.casino.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SlotService {
    @Autowired
    UserService userService;
    private long credito;
    private String[] arResult;
    private int apuestaUsuario = 0;
    private List<String> resultSlot = new ArrayList<>();
    private Slot slot = new Slot();

    public Map<String, Object> spin(Integer apuesta, User user) {
        credito = user.getCoins();
        Map<String, Object> map = new HashMap<>();
        if (apuesta == 0) {
            map.put("message", "¡Tienes que apostar dinero!");
            return map;
        } else if (apuesta > 0) apuestaUsuario = apuesta;
        if (credito == 0) {
            map.put("message", "¡Se acabó el crédito!");
            return map;
        }
        if (apuesta > credito) {
            map.put("message", "La apuesta es más grande que los créditos que tienes");
            return map;
        }
        arResult = new String[]{"❓", "❓", "❓"};
        resultSlot.clear();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                List<String> randomItem = new ArrayList<>(Arrays.asList(slot.getItems()));
                for (int k = 0; k < slot.getItems().length; k++) {
                    int random = getRandomIndex(randomItem.size());
                    if (j == 2 && k == slot.getItems().length - 1) resultSlot.add(randomItem.get(random));
                    arResult[i] += randomItem.get(random);
                    randomItem.remove(random);
                }
            }
        }
        credito -= apuesta;
        int gains = checkwin(apuestaUsuario);
        if (gains > 0) map.put("message", "Has ganado " + gains + " créditos!");
        else map.put("message", "");
        map.put("resultado", arResult);
        map.put("ganancias", gains);
        credito += gains;
        map.put("creditos", credito);
        map.put("total", resultSlot);
        userService.setCredito(credito, user);

        return map;
    }

    /*public static Map<String, Object> spinColumn(Integer column, Integer apuesta, String emoji) {
        Map<String, Object> map = new HashMap<>();
        if (column != null) {
            if (arResult[0] == null) {
                map.put("message", "No puedes girar una columna sin haber girado las tres");
                return map;
            }
            if (column < 0 || column >= arResult.length) {
                map.put("message", "La columna especificada no es válida");
                return map;
            }
            while (true) {
                String anotherFruit = "";
                anotherFruit = slot.getItems()[getRandomIndex(slot.getItems().length)];
                if (emoji.equals(anotherFruit)) {
                    isSpin[column] = true;
                    break;
                }
            }
            arResult[column] = slot.getItems()[getRandomIndex(slot.getItems().length)];
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
            arResult = new String[3];
            for (int i = 0; i < arResult.length; i++) {
                arResult[i] = slot.getItems()[getRandomIndex(slot.getItems().length)].repeat(3);
            }
            map.put("resultado", arResult);
        }
        return map;
    }*/

    public long returnCredito(User user) {
        return user.getCoins();
    }

    public List<String> returnResultado() {
        return resultSlot;
    }

    private int getRandomIndex(int length) {
        return (int) (Math.random() * length);
    }

    private int checkwin(int apuesta) {
        int gains = 0;
        String resultado1 = resultSlot.get(0);
        String resultado2 = resultSlot.get(1);
        String resultado3 = resultSlot.get(2);
        if (resultado1.equals(resultado2) && resultado1.equals(resultado3)&& !resultado1.equals("❓")) {
            for (int i = 0; i < slot.getItems().length; i++) {
                if (slot.getItems()[i].equals(resultado1)) {
                    gains = apuesta * slot.getValor()[i];
                    return gains;
                }
            }
        }
        return gains;
    }
}
