package com.liceu.casino.controllers;

import com.liceu.casino.services.SlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/games/slot")
public class SlotController {
    @Autowired
    SlotService slotService;

    @GetMapping("/credito")
    public int getCredito() {
        return slotService.returnCredito();
    }

    @GetMapping("/resultado")
    public String[] getResultado() {
        return slotService.returnResultado();
    }

    @PostMapping("/spin")
    public Map<String, Object> spin(@RequestParam(value = "column", required = false) Integer column,
                         @RequestParam(value = "apuesta", required = false) Integer apuesta) {
        return SlotService.spin(column, apuesta);
    }

    @PostMapping("/reset")
    public String reset() {
        slotService.reset();
        return "Reiniciado con Éxito!";
    }
}
