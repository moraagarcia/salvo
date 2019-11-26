package com.codeoftheweb.salvo;

import com.codeoftheweb.salvo.models.*;
import com.codeoftheweb.salvo.repositories.GamePlayerRepository;
import com.codeoftheweb.salvo.repositories.GameRepository;
import com.codeoftheweb.salvo.repositories.PlayerRepository;
import com.codeoftheweb.salvo.repositories.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AppController {

    @Autowired
    ShipRepository shipRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GamePlayerRepository gamePlayerRepository;

    @Autowired
    private PlayerRepository playerRepository;

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

    public List<String> getHits(){
        return new LinkedList<>();  //hardcodeado cambiar mas adelante
    }


    @RequestMapping("/game_view/{gamePlayerId}")
    public ResponseEntity<Map<String,Object>> getGameViewAll(@PathVariable Long gamePlayerId,Authentication authentication) {
        Optional<GamePlayer> optionalGamePlayer = gamePlayerRepository.findById(gamePlayerId);
        if (!optionalGamePlayer.isPresent()) return new ResponseEntity<>(Util.makeMap("error", "No such gamePlayer"),HttpStatus.UNAUTHORIZED);
        GamePlayer gamePlayer = optionalGamePlayer.get();
        if (Util.isGuest(authentication) || playerRepository.findByUserName(authentication.getName()).getId() != gamePlayer.getPlayer().getId())
            return new ResponseEntity<>(Util.makeMap("error","You're not authorized to enter"), HttpStatus.UNAUTHORIZED);
        Map<String, Object>  map = gamePlayer.getGame().makeGameDTO();
        map.put("ships", gamePlayer.getShips().stream().map(ship -> ship.makeShipDTO()).collect(Collectors.toList()));
        map.put("salvoes",gamePlayer.getGame().getGamePlayers().stream().flatMap(gamePlayer1 -> gamePlayer1.getSalvoes().stream().map(salvo-> salvo.makeSalvoDTO())).collect(Collectors.toList()));
        Map<String,Object> hitsMap = new LinkedHashMap<>();
        hitsMap.put("self",getHits());
        hitsMap.put("opponent", getHits());
        map.put("hits", hitsMap);
        return new ResponseEntity<>(map,HttpStatus.ACCEPTED);
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

    @RequestMapping(path = "/games", method = RequestMethod.POST)
    public ResponseEntity<Map<String,Object>> createGame(Authentication authentication){
        if (Util.isGuest(authentication)) return new ResponseEntity<>(Util.makeMap("Error", "You must be logged in to start a new game"),HttpStatus.UNAUTHORIZED);
        Player player = playerRepository.findByUserName(authentication.getName());
        Game newGame = new Game();
        GamePlayer gamePlayer = new GamePlayer(newGame,player);
        gameRepository.save(newGame);
        gamePlayerRepository.save(gamePlayer);
        return new ResponseEntity<>(Util.makeMap("gpid", gamePlayer.getId()),HttpStatus.CREATED);
    }

    @RequestMapping( path = "/games/players/{gamePlayerId}/ships", method = RequestMethod.POST)
    public ResponseEntity<Map<String,Object>> saveShips(@PathVariable long gamePlayerId,@RequestBody List<Ship> ships,Authentication authentication){
        if (Util.isGuest(authentication)) return new ResponseEntity<>(Util.makeMap("error", "You're not logged in"),HttpStatus.UNAUTHORIZED);
        Optional<GamePlayer> optionalGamePlayer = gamePlayerRepository.findById(gamePlayerId);
        if (!optionalGamePlayer.isPresent()) return new ResponseEntity<>(Util.makeMap("error", "No such gamePlayer"),HttpStatus.UNAUTHORIZED);
        GamePlayer gamePlayer = optionalGamePlayer.get();
        if (playerRepository.findByUserName(authentication.getName()).getId() != gamePlayer.getPlayer().getId())
            return new ResponseEntity<>(Util.makeMap("error", "Wrong player"),HttpStatus.UNAUTHORIZED);
        if (!gamePlayer.getShips().isEmpty())
            return new ResponseEntity<>(Util.makeMap("error", "You've already placed your ships"),HttpStatus.FORBIDDEN);
        ships.stream().forEach(ship -> ship.setGamePlayer(gamePlayer));
        shipRepository.saveAll(ships);
        return new ResponseEntity<>(Util.makeMap("OK", "Ships added"),HttpStatus.CREATED);
    }

}