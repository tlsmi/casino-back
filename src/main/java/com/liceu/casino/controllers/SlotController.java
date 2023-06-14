package com.liceu.casino.controllers;

import com.liceu.casino.forms.SpinAll;
import com.liceu.casino.forms.SpinColumn;
import com.liceu.casino.model.Slot;
import com.liceu.casino.model.User;
import com.liceu.casino.services.SlotService;
import com.liceu.casino.services.TokenService;
import com.liceu.casino.services.UserService;
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

    @Autowired
    UserService userService;

    @Autowired
    TokenService tokenService;

    @GetMapping("/resultado")
    public Map<String, Object> getResultado() {
        Map<String, Object> map = new HashMap<>();
        map.put("resultado", slotService.returnResultado());
        return map;
    }

    @PostMapping("/spin")
    public Map<String, Object> spin(@RequestBody SpinAll request, @RequestHeader("Authorization") String token) {
        User user = userService.findByEmail(tokenService.getEmail(token));
        int apuesta = request.getApuesta();
        return slotService.spin(apuesta, user);
    }

}
