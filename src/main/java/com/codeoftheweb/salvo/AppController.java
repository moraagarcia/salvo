package com.codeoftheweb.salvo;

import com.codeoftheweb.salvo.models.GamePlayer;
import com.codeoftheweb.salvo.models.Player;
import com.codeoftheweb.salvo.repositories.GamePlayerRepository;
import com.codeoftheweb.salvo.repositories.GameRepository;
import com.codeoftheweb.salvo.repositories.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AppController {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GamePlayerRepository gamePlayerRepository;

    @Autowired
    private PlayerRepository playerRepository;

    private boolean isGuest(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }

    @RequestMapping("/games")
    public Map<String,Object> getGameAll(Authentication authentication) {
        Map<String,Object> map = new LinkedHashMap<>();
        if (isGuest(authentication)){
            map.put("player", "Guest");
        }else{
            Player player = playerRepository.findByUserName(authentication.getName());
            map.put("player",player.makePlayerDTO());
        }
        map.put("games", gameRepository.findAll().stream().map(game -> game.makeGameDTO()).collect(Collectors.toList()));
        return map;
    }

    @RequestMapping("/game_view/{gamePlayerId}")
    public Map<String, Object> getGameViewAll(@PathVariable Long gamePlayerId) {
        Optional<GamePlayer> optionalGamePlayer = gamePlayerRepository.findById(gamePlayerId);
        if (!optionalGamePlayer.isPresent()) return null;
        GamePlayer gamePlayer = optionalGamePlayer.get();
        Map<String, Object>  map = gamePlayer.getGame().makeGameDTO();
        map.put("ships", gamePlayer.getShips().stream().map(ship -> ship.makeShipDTO()).collect(Collectors.toList()));
        map.put("salvoes",gamePlayer.getGame().getGamePlayers().stream().flatMap(gamePlayer1 -> gamePlayer1.getSalvoes().stream().map(salvo-> salvo.makeSalvoDTO())).collect(Collectors.toList()));
        return map;
    }

    @RequestMapping("/leaderBoard")
    public List<Object> getLeaderboard(){
        List<Player> players = playerRepository.findAll().stream().collect(Collectors.toList());
        players.sort(Comparator.comparing(Player::getTotalScore).reversed());
        List<Object> leaderBoard = players.stream().map(player -> player.makeLeaderBoardDTO()).collect(Collectors.toList());
        return leaderBoard;
    }

    @RequestMapping(path = "/players",method = RequestMethod.POST)
    public ResponseEntity createPlayer(@RequestParam String email,@RequestParam String password){
        if (email.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>("Missing parameters", HttpStatus.FORBIDDEN);
        }

        Player player = playerRepository.findByUserName(email);
        if (player != null) {
            return new ResponseEntity<>("UserName already in use", HttpStatus.CONFLICT);
        }

        playerRepository.save(new Player(email,passwordEncoder.encode(password)));
        return new ResponseEntity<>("Player added", HttpStatus.CREATED);
    }
}