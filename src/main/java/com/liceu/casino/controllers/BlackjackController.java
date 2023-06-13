package com.liceu.casino.controllers;

import com.liceu.casino.model.GameResponse;
import com.liceu.casino.services.BlackjackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/games/blackjack")

public class BlackjackController {
    @Autowired
    BlackjackService blackjackService;

    @PostMapping("/start")
    public String startGame() {
        return blackjackService.start();
    }

    @PostMapping("/hit")
    public String hit() {
        return blackjackService.hit();
    }

    @PostMapping("/stay")
    public String stay() {
        return blackjackService.stay();
    }

    @PostMapping("/double")
    public String doblar () {
        return blackjackService.doblar();
    }
}
