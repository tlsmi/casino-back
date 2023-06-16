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

    private int apuestaUsuario = 0;
    private List<String> resultSlot = new ArrayList<>();
    private Slot slot = new Slot();

    public Map<String, Object> spin(Integer apuesta, User user) {
        long credito = user.getCoins();
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
        String[] arResult = new String[]{"❓", "❓", "❓"};
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
