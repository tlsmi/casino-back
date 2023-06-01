package com.liceu.casino.controllers;

import com.liceu.casino.model.Bet;
import com.liceu.casino.services.RouletteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RuletaController {

    @Autowired
    RouletteService ruletaService;
    @PostMapping("/games/roulette")
    @CrossOrigin
    public Object roulette(@RequestBody Bet bet){

        Bet result = ruletaService.getResult();
        int coins = ruletaService.getCoinsByResult(result,bet);
        return coins;
    }
}