package com.codeoftheweb.salvo.models;

import com.codeoftheweb.salvo.controllers.AppController;
import com.codeoftheweb.salvo.repositories.ScoreRepository;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.*;

public class Util {

    public static Map<String,Object> makeMap(String string, Object object){
        Map<String,Object> map = new HashMap<>();
        map.put(string,object);
        return map;
    }

    public static boolean isGuest(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }

    public static boolean allShipsSunk(AppController appController,GamePlayer gamePlayer){
        GamePlayer opponent = gamePlayer.getOpponent();
        int[] amountOfHits = {0};
        opponent.getSalvoes().forEach(salvo -> amountOfHits[0] += appController.getHitLocations(salvo,gamePlayer).size());
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

    public static void updateGameScore(GamePlayer gamePlayer,GamePlayer opponent,String gameState,Game game,ScoreRepository scoreRepository){
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

    public static String getState(GamePlayer gamePlayerSelf, GamePlayer gamePlayerOpponent, AppController appController){
        if (gamePlayerSelf.getShips().isEmpty()) return "PLACESHIPS";
        if (gamePlayerSelf.getGame().getGamePlayers().size() == 1 || gamePlayerOpponent.getShips().isEmpty()) return "WAITINGFOROPP";
        boolean gamePlayerShipsSunk = allShipsSunk(appController,gamePlayerSelf);
        boolean opponentShipsSunk = allShipsSunk(appController,gamePlayerOpponent);
        if (gamePlayerSelf.getSalvoes().size() < gamePlayerOpponent.getSalvoes().size()) return "PLAY";
        if (gamePlayerSelf.getSalvoes().size() == gamePlayerOpponent.getSalvoes().size()){
            if (gamePlayerShipsSunk && opponentShipsSunk) return "TIE";
            if (gamePlayerShipsSunk) return "LOST";
            if (opponentShipsSunk) return "WON";
            if (gamePlayerSelf.getId() < gamePlayerOpponent.getId()) return "PLAY";
        }
        return "WAIT";
    }
}
