package com.codeoftheweb.salvo.controllers;

import com.codeoftheweb.salvo.models.GamePlayer;
import com.codeoftheweb.salvo.models.Ship;
import com.codeoftheweb.salvo.models.Util;
import com.codeoftheweb.salvo.repositories.GamePlayerRepository;
import com.codeoftheweb.salvo.repositories.PlayerRepository;
import com.codeoftheweb.salvo.repositories.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ShipController {

    @Autowired
    GamePlayerRepository gamePlayerRepository;

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    ShipRepository shipRepository;

    @RequestMapping( path = "/games/players/{gamePlayerId}/ships", method = RequestMethod.POST)
    public ResponseEntity<Map<String,Object>> saveShips(@PathVariable long gamePlayerId, @RequestBody List<Ship> ships, Authentication authentication){
        if (Util.isGuest(authentication)) return new ResponseEntity<>(Util.makeMap("error", "You're not logged in"), HttpStatus.UNAUTHORIZED);
        Optional<GamePlayer> optionalGamePlayer = gamePlayerRepository.findById(gamePlayerId);
        if (!optionalGamePlayer.isPresent()) return new ResponseEntity<>(Util.makeMap("error", "No such gamePlayer"),HttpStatus.UNAUTHORIZED);
        GamePlayer gamePlayer = optionalGamePlayer.get();
        if (playerRepository.findByUserName(authentication.getName()).getId() != gamePlayer.getPlayer().getId())
            return new ResponseEntity<>(Util.makeMap("error", "Wrong player"),HttpStatus.UNAUTHORIZED);
        if (!gamePlayer.getShips().isEmpty())
            return new ResponseEntity<>(Util.makeMap("error", "You've already placed your ships"),HttpStatus.FORBIDDEN);
        if (!Util.shipLocationsAreValid(ships)) return new ResponseEntity<>(Util.makeMap("error", "Ship locations out of range"),HttpStatus.FORBIDDEN);
        ships.stream().forEach(ship -> ship.setGamePlayer(gamePlayer));
        shipRepository.saveAll(ships);
        return new ResponseEntity<>(Util.makeMap("OK", "Ships added"),HttpStatus.CREATED);
    }
}
