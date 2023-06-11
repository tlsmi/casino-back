package com.liceu.casino.controllers;

import com.liceu.casino.model.Bet;
import com.liceu.casino.services.RouletteService;
import com.liceu.casino.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class RuletaController {

    @Autowired
    RouletteService ruletaService;

    @Autowired
    TokenService tokenService;

    @PostMapping("/games/roulette")
    @CrossOrigin
    public Object roulette(@RequestBody Bet bet){



        Map<String,Object> map;
        int num = (int) Math.floor(Math.random() * 36);
        Bet result = ruletaService.getResult(num);
        map = ruletaService.getBalance(result,bet,null);
        map.put("n",num);
        return map;
    }
}