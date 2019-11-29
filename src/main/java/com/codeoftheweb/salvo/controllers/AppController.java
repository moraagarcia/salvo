package com.codeoftheweb.salvo.controllers;

import com.codeoftheweb.salvo.models.*;
import com.codeoftheweb.salvo.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AppController {

    @Autowired
    private GamePlayerRepository gamePlayerRepository;

    @Autowired
    private PlayerRepository playerRepository;

    public List<Map<String,Object>> getHits(GamePlayer gamePlayer1, GamePlayer opponent){
        List<Map<String,Object>> hitsList = new LinkedList<>();
        //ordeno los salvos del oponente por turno para recorrerlos facilmente ya que Salvos en gameplayer un set y este no tiene orden
        List<Salvo> opponentSalvoesInOrder = opponent.getSalvoes().stream().sorted(Comparator.comparingInt(Salvo::getTurn)).collect(Collectors.toList());
        for (int i = 0; i < opponent.getSalvoes().size(); i++){
            if (i == 0) hitsList.add(makeHitsDTO(gamePlayer1,opponentSalvoesInOrder.get(i),null));
            else hitsList.add(makeHitsDTO(gamePlayer1,opponentSalvoesInOrder.get(i),(Map<String, Integer>) hitsList.get(i-1).get("damages")));
            //                                                                       ↑ ↑ aca paso por parametro el map de damages del turno anterior para poder acumular los hits de los barcos
        }
        return hitsList;
    }

    public Map<String,Object> makeHitsDTO(GamePlayer gamePlayer1,Salvo salvo,Map<String,Integer> damagesMap){
        Map<String,Object> map = new LinkedHashMap<>();
        map.put("turn", salvo.getTurn());
        List<String> hitLocations = getHitLocations(salvo,gamePlayer1);
        map.put("hitLocations",hitLocations);
        map.put("damages",getDamages(damagesMap,hitLocations,gamePlayer1));
        map.put("missed",gamePlayer1.getShips().size() - hitLocations.size());
        return map;
    }


    public List<String> getHitLocations(Salvo salvo,GamePlayer gamePlayer1){
        return salvo.getSalvoLocations().stream().filter(location -> !(checkHits(location,gamePlayer1.getShips())).isEmpty()).collect(Collectors.toList());
    }

    public Map<String,Object> checkHits(String location,Set<Ship> ships){
        Map<String,Object> map = new LinkedHashMap<>();
        for (Ship ship : ships) {
            if (ship.getShipLocations().contains(location))
                map.put(ship.getType(), location);
        }
        return map;
    }

    public Map<String,Integer> getDamages(Map<String,Integer> damagesMap,List<String> hitLocations,GamePlayer gamePlayer1){
        if (damagesMap == null) damagesMap = new LinkedHashMap<>();
        Map<String,Integer> map = new LinkedHashMap<>();
        map.put("carrierHits", getShipAmountOfHits(gamePlayer1,hitLocations, "carrier"));
        map.put("battleshipHits",getShipAmountOfHits(gamePlayer1, hitLocations, "battleship"));
        map.put("submarineHits", getShipAmountOfHits(gamePlayer1, hitLocations, "submarine"));
        map.put("destroyerHits",getShipAmountOfHits(gamePlayer1,hitLocations, "destroyer"));
        map.put("patrolboatHits",getShipAmountOfHits(gamePlayer1,hitLocations, "patrolboat"));
        map.put("carrier",damagesMap.getOrDefault("carrier", 0) + map.get("carrierHits"));
        map.put("battleship",damagesMap.getOrDefault("battleship", 0) + map.get("battleshipHits"));
        map.put("submarine",damagesMap.getOrDefault("submarine", 0) + map.get("submarineHits"));
        map.put("destroyer",damagesMap.getOrDefault("destroyer", 0) + map.get("destroyerHits"));
        map.put("patrolboat",damagesMap.getOrDefault("patrolboat", 0) + map.get("patrolboatHits"));
        return map;
    }

    public int getShipAmountOfHits(GamePlayer gamePlayer1,List<String> hitLocations,String shipType){
        List<Ship> shipList = gamePlayer1.getShips().stream().filter(ship -> ship.getType().equals(shipType)).collect(Collectors.toList());
        int amountOfHits = (int)shipList.get(0).getShipLocations().stream().filter(location -> hitLocations.contains(location)).count();
        return amountOfHits;
    }


    @RequestMapping("/game_view/{gamePlayerId}")
    public ResponseEntity<Map<String,Object>> getGameViewAll(@PathVariable Long gamePlayerId,Authentication authentication) {
        Optional<GamePlayer> optionalGamePlayer = gamePlayerRepository.findById(gamePlayerId);
        if (!optionalGamePlayer.isPresent()) return new ResponseEntity<>(Util.makeMap("error", "No such gamePlayer"),HttpStatus.UNAUTHORIZED);
        GamePlayer gamePlayer = optionalGamePlayer.get();
        if (Util.isGuest(authentication) || playerRepository.findByUserName(authentication.getName()).getId() != gamePlayer.getPlayer().getId())
            return new ResponseEntity<>(Util.makeMap("error","You're not authorized to enter"), HttpStatus.UNAUTHORIZED);
        Map<String, Object>  map = gamePlayer.getGame().makeGameDTO();
        map.put("gameState",getState(gamePlayer, gamePlayer.getOpponent()));
        map.put("ships", gamePlayer.getShips().stream().map(ship -> ship.makeShipDTO()).collect(Collectors.toList()));
        map.put("salvoes",gamePlayer.getGame().getGamePlayers().stream().flatMap(gamePlayer1 -> gamePlayer1.getSalvoes().stream().map(salvo-> salvo.makeSalvoDTO())).collect(Collectors.toList()));
        Map<String,Object> hitsMap = new LinkedHashMap<>();
        List<GamePlayer> opponentGamePlayerList = gamePlayer.getGame().getGamePlayers().stream().filter(gamePlayer1 -> gamePlayer1.getId() != gamePlayer.getId()).collect(Collectors.toList());
        if (opponentGamePlayerList.isEmpty()) {
            hitsMap.put("self",new LinkedList<>());
            hitsMap.put("opponent", new LinkedList<>());
        }else{
            hitsMap.put("self",getHits(gamePlayer,opponentGamePlayerList.get(0)));
            hitsMap.put("opponent", getHits(opponentGamePlayerList.get(0),gamePlayer));
        }
        map.put("hits", hitsMap);
        return new ResponseEntity<>(map,HttpStatus.ACCEPTED);
    }

    public String getState(GamePlayer gamePlayerSelf, GamePlayer gamePlayerOpponent){
        if (gamePlayerSelf.getShips().isEmpty()) return "PLACESHIPS";
        if (gamePlayerSelf.getGame().getGamePlayers().size() == 1 || gamePlayerOpponent.getShips().isEmpty()) return "WAITINGFOROPP";
        boolean gamePlayerShipsSunk = Util.allShipsSunk(this,gamePlayerSelf);
        boolean opponentShipsSunk = Util.allShipsSunk(this,gamePlayerOpponent);
        if (gamePlayerSelf.getSalvoes().size() < gamePlayerOpponent.getSalvoes().size()) return "PLAY";
        if (gamePlayerSelf.getSalvoes().size() == gamePlayerOpponent.getSalvoes().size()){
            if (gamePlayerShipsSunk && opponentShipsSunk) return "TIE";
            if (gamePlayerShipsSunk) return "LOST";
            if (opponentShipsSunk) return "WON";
            if (gamePlayerSelf.getId() < gamePlayerOpponent.getId()) return "PLAY";
            else return "WAIT";
        }
        return "WAIT";
    }
}