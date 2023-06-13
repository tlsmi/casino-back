package com.liceu.casino.controllers;

import com.liceu.casino.model.BetRequest;
import com.liceu.casino.model.GameResponse;
import com.liceu.casino.model.User;
import com.liceu.casino.services.BlackJackService1;
import com.liceu.casino.services.TokenService;
import com.liceu.casino.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/games/blackjack1")
@CrossOrigin
public class BlackJackController1 {
    @Autowired
    BlackJackService1 blackJackService;

    @Autowired
    UserService userService;

    @Autowired
    TokenService tokenService;

    @PostMapping("/start")
    public ResponseEntity<?> startGame(@RequestBody BetRequest betRequest, @RequestHeader("Authorization") String token) {
        User user = userService.findByEmail(tokenService.getUser(token));
        return blackJackService.start(betRequest, user);
    }
}
