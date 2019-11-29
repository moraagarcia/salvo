package com.codeoftheweb.salvo.models;

import com.codeoftheweb.salvo.controllers.AppController;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
}
