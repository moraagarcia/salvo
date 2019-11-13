package com.codeoftheweb.salvo;

import com.codeoftheweb.salvo.models.GamePlayer;
import com.codeoftheweb.salvo.repositories.GamePlayerRepository;
import com.codeoftheweb.salvo.repositories.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AppController {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GamePlayerRepository gamePlayerRepository;

    @RequestMapping("/games")
    public List<Object> getGameAll() {
        return gameRepository.findAll().stream().map(game -> game.makeGameDTO()).collect(Collectors.toList());
    }

    @RequestMapping("/game_view/{gamePlayerId}")
    public Map<String, Object> getGameViewAll(@PathVariable Long gamePlayerId) {
        GamePlayer gamePlayer = gamePlayerRepository.findById(gamePlayerId).get(); //DUDAA ESTA BIEN??
        Map<String, Object>  map = gamePlayer.getGame().makeGameDTO();
        map.put("ships", gamePlayer.getShips().stream().map(ship -> ship.getShipData()).collect(Collectors.toList()));
        return map;
    }
}