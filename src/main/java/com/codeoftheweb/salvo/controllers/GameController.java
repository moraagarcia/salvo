package com.codeoftheweb.salvo.controllers;

import com.codeoftheweb.salvo.models.Game;
import com.codeoftheweb.salvo.models.GamePlayer;
import com.codeoftheweb.salvo.models.Player;
import com.codeoftheweb.salvo.models.Util;
import com.codeoftheweb.salvo.repositories.GamePlayerRepository;
import com.codeoftheweb.salvo.repositories.GameRepository;
import com.codeoftheweb.salvo.repositories.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class GameController {

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    GameRepository gameRepository;

    @Autowired
    GamePlayerRepository gamePlayerRepository;

    @RequestMapping("/games")
    public Map<String,Object> getGameAll(Authentication authentication) {
        Map<String,Object> map = new LinkedHashMap<>();
        if (Util.isGuest(authentication)){
            map.put("player", "Guest");
        }else{
            Player player = playerRepository.findByUserName(authentication.getName());
            map.put("player",player.makePlayerDTO());
        }
        map.put("games", gameRepository.findAll().stream().map(game -> game.makeGameDTO()).collect(Collectors.toList()));
        return map;
    }

    @RequestMapping(path = "/games", method = RequestMethod.POST)
    public ResponseEntity<Map<String,Object>> createGame(Authentication authentication){
        if (Util.isGuest(authentication)) return new ResponseEntity<>(Util.makeMap("Error", "You must be logged in to start a new game"), HttpStatus.UNAUTHORIZED);
        Player player = playerRepository.findByUserName(authentication.getName());
        Game newGame = new Game();
        GamePlayer gamePlayer = new GamePlayer(newGame,player);
        gameRepository.save(newGame);
        gamePlayerRepository.save(gamePlayer);
        return new ResponseEntity<>(Util.makeMap("gpid", gamePlayer.getId()),HttpStatus.CREATED);
    }

    @RequestMapping(path = "/game/{gameId}/players",method = RequestMethod.POST)
    public ResponseEntity<Map<String,Object>> joinGame(@PathVariable long gameId, Authentication authentication){
        if(Util.isGuest(authentication))
            return new ResponseEntity<>(Util.makeMap("error", "You can't join a game if you're not logged in."),HttpStatus.UNAUTHORIZED);
        Player player = playerRepository.findByUserName(authentication.getName());
        Game gameToJoin = gameRepository.getOne(gameId);
        if(gameToJoin == null)
            return new ResponseEntity<>(Util.makeMap("error", "No such game."),HttpStatus.FORBIDDEN);
        long gamePlayersCount = gameToJoin.getGamePlayers().size();
        if (gamePlayersCount == 1){
            GamePlayer gamePlayer = gamePlayerRepository.save(new GamePlayer(gameToJoin,player));
            return new ResponseEntity<>(Util.makeMap("gpid", gamePlayer.getId()),HttpStatus.CREATED);
        }else
            return new ResponseEntity<>(Util.makeMap("error", "Game is full."),HttpStatus.FORBIDDEN);
    }
}
