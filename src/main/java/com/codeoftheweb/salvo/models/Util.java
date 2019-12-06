package com.codeoftheweb.salvo.models;

import com.codeoftheweb.salvo.repositories.ScoreRepository;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.*;
import java.util.stream.Collectors;

public class Util {

    public static Map<String,Object> makeMap(String string, Object object){
        Map<String,Object> map = new HashMap<>();
        map.put(string,object);
        return map;
    }

    public static boolean isGuest(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }

    public static List<String> getHitLocations(Salvo salvo,GamePlayer gamePlayer1){
        return salvo.getSalvoLocations().stream().filter(location -> !(checkHits(location,gamePlayer1.getShips())).isEmpty()).collect(Collectors.toList());
    }

    public static Map<String,Object> checkHits(String location,Set<Ship> ships){
        Map<String,Object> map = new LinkedHashMap<>();
        for (Ship ship : ships) {
            if (ship.getShipLocations().contains(location))
                map.put(ship.getType(), location);
        }
        return map;
    }

    public static boolean allShipsSunk(GamePlayer gamePlayer){
        GamePlayer opponent = gamePlayer.getOpponent();
        int[] amountOfHits = {0};
        opponent.getSalvoes().forEach(salvo -> amountOfHits[0] += getHitLocations(salvo,gamePlayer).size());
        return amountOfHits[0] == gamePlayer.getShips().stream().mapToInt(ship -> ship.getShipLocations().size()).sum();
    }


    public static boolean shipLocationsAreValid(List<Ship> ships) {
        List<String> shipLocations = new LinkedList<>();
        ships.forEach(ship -> shipLocations.addAll(ship.getShipLocations()));
        for (String shipLocation:shipLocations) {
            if (shipLocation.charAt(0) > 'J' || shipLocation.charAt(0) < 'A') return false;
            if (shipLocation.length() == 3 && (Integer.parseInt(shipLocation.substring(1)) > 10 || Integer.parseInt(shipLocation.substring(1)) < 1)) return false;
            if (shipLocation.length() == 2 && (Character.getNumericValue(shipLocation.charAt(1)) > 10 || Character.getNumericValue(shipLocation.charAt(1)) < 1))
                return false;
        }
        return true;
    }

    public static void updateGameScore(GamePlayer gamePlayer,String gameState,ScoreRepository scoreRepository){
        GamePlayer opponent = gamePlayer.getOpponent();
        Game game = gamePlayer.getGame();
        if (gameState == "WON"){
            Date date = new Date();
            Score scoreGamePlayer = new Score(game,gamePlayer.getPlayer(),1,date);
            Score scoreOpponent = new Score(game,opponent.getPlayer(),0,date);
            scoreRepository.saveAll(Arrays.asList(scoreGamePlayer,scoreOpponent));
        }
        if (gameState == "LOST"){
            Date date = new Date();
            Score scoreGamePlayer = new Score(game,gamePlayer.getPlayer(),0,date);
            Score scoreOpponent = new Score(game,opponent.getPlayer(),1,date);
            scoreRepository.saveAll(Arrays.asList(scoreGamePlayer,scoreOpponent));
        }
        if (gameState == "TIE"){
            Date date = new Date();
            Score scoreGamePlayer = new Score(game,gamePlayer.getPlayer(),0.5,date);
            Score scoreOpponent = new Score(game,opponent.getPlayer(),0.5,date);
            scoreRepository.saveAll(Arrays.asList(scoreGamePlayer,scoreOpponent));
        }
    }

    public static String getState(GamePlayer gamePlayerSelf){
        GamePlayer gamePlayerOpponent = gamePlayerSelf.getOpponent();
        if (gamePlayerSelf.getShips().isEmpty())
            return "PLACESHIPS";
        if (gamePlayerSelf.getGame().getGamePlayers().size() == 1 || gamePlayerOpponent.getShips().isEmpty())
            return "WAITINGFOROPP";
        if (gamePlayerSelf.getSalvoes().size() < gamePlayerOpponent.getSalvoes().size())
            return "PLAY";
        boolean gamePlayerShipsSunk = allShipsSunk(gamePlayerSelf);
        boolean opponentShipsSunk = allShipsSunk(gamePlayerOpponent);
        if (gamePlayerSelf.getSalvoes().size() == gamePlayerOpponent.getSalvoes().size()){
            if (gamePlayerShipsSunk && opponentShipsSunk) return "TIE";
            if (gamePlayerShipsSunk) return "LOST";
            if (opponentShipsSunk) return "WON";
            else return "PLAY";
        }
        return "WAIT";
    }
}
