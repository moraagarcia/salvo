package com.codeoftheweb.salvo.controllers;

import com.codeoftheweb.salvo.models.GamePlayer;
import com.codeoftheweb.salvo.models.Salvo;
import com.codeoftheweb.salvo.models.Util;
import com.codeoftheweb.salvo.repositories.GamePlayerRepository;
import com.codeoftheweb.salvo.repositories.PlayerRepository;
import com.codeoftheweb.salvo.repositories.SalvoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class SalvoController {

    @Autowired
    GamePlayerRepository gamePlayerRepository;

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    SalvoRepository salvoRepository;

    @RequestMapping(path = "/games/players/{gamePlayerId}/salvoes", method = RequestMethod.POST)
    public ResponseEntity<Map<String,Object>> saveSalvoes(@PathVariable long gamePlayerId, @RequestBody Salvo salvo, Authentication authentication){
        if (Util.isGuest(authentication))
            return new ResponseEntity<>(Util.makeMap("error", "You're not logged in"), HttpStatus.UNAUTHORIZED);
        Optional<GamePlayer> optionalGamePlayer = gamePlayerRepository.findById(gamePlayerId);
        if (!optionalGamePlayer.isPresent())
            return new ResponseEntity<>(Util.makeMap("error", "No such gamePlayer"),HttpStatus.UNAUTHORIZED);
        GamePlayer gamePlayer = optionalGamePlayer.get();
        if (playerRepository.findByUserName(authentication.getName()).getId() != gamePlayer.getPlayer().getId())
            return new ResponseEntity<>(Util.makeMap("error", "Wrong player"),HttpStatus.UNAUTHORIZED);
        List<Salvo> salvoInThisTurn = gamePlayer.getSalvoes().stream().filter(salvo1 -> salvo1.getTurn() == salvo.getTurn()).collect(Collectors.toList());
        if (!salvoInThisTurn.isEmpty())
            return new ResponseEntity<>(Util.makeMap("error", "You've already played your turn"),HttpStatus.FORBIDDEN);
        if (salvo.getSalvoLocations().size() != 5)
            return new ResponseEntity<>(Util.makeMap("error", "You need to fire 5 shots"),HttpStatus.UNAUTHORIZED);
        salvo.setTurn(gamePlayer.getSalvoes().size()+1);
        salvo.setGamePlayer(gamePlayer);
        salvoRepository.save(salvo);
        return new ResponseEntity<>(Util.makeMap("OK", "Salvo added"),HttpStatus.CREATED);
    }
}
