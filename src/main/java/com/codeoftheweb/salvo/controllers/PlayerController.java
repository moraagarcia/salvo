package com.codeoftheweb.salvo.controllers;

import com.codeoftheweb.salvo.models.Player;
import com.codeoftheweb.salvo.models.Util;
import com.codeoftheweb.salvo.repositories.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class PlayerController {

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @RequestMapping(path = "/players",method = RequestMethod.POST)
    public ResponseEntity<Map<String,Object>> createPlayer(@RequestParam String email, @RequestParam String password){
        if (email.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>(Util.makeMap("error","Missing parameters"), HttpStatus.FORBIDDEN);
        }

        Player player = playerRepository.findByUserName(email);
        if (player != null) {
            return new ResponseEntity<>(Util.makeMap("error","UserName already in use"), HttpStatus.CONFLICT);
        }

        playerRepository.save(new Player(email,passwordEncoder.encode(password)));
        return new ResponseEntity<>(Util.makeMap("error","Player added"), HttpStatus.CREATED);
    }


}
