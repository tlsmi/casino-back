package com.liceu.casino.controllers;

import com.liceu.casino.forms.SpinAll;
import com.liceu.casino.forms.SpinColumn;
import com.liceu.casino.services.SlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/games/slot")
@CrossOrigin(origins = "http://localhost:3000")
public class SlotController {
    @Autowired
    SlotService slotService;

    @GetMapping("/credito")
    public int getCredito() {
        return slotService.returnCredito();
    }

    @GetMapping("/resultado")
    public Map<String, Object> getResultado() {
        Map<String, Object> map = new HashMap<>();
        map.put("resultado", slotService.returnResultado());
        return map;
    }

    @PostMapping("/spin")
    public Map<String, Object> spin(@RequestBody SpinAll request) {
        int apuesta = request.getApuesta();
        return SlotService.spin(apuesta);
    }

    @PostMapping("/spinColumn")
    public Map<String, Object> spinColumn(@RequestBody SpinColumn request) {
        Map<String, Object> map = new HashMap<>();
        int column = request.getColumn();
        String emoji = request.getEmoji();
        int apuesta = request.getApuesta();
        return SlotService.spinColumn(column, apuesta, emoji);
    }

    @PostMapping("/reset")
    public String reset() {
        slotService.reset();
        return "Reiniciado con Éxito!";
    }
}
